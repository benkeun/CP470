package com.example.androidassignments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.util.Xml;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class WeatherForecast extends AppCompatActivity {
    ImageView weatherImg;
    TextView CurTemp;
    TextView MinTemp;
    TextView MaxTemp;
    ProgressBar weatherProg;
    List<String> cityList;
    TextView cityName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);
        weatherImg=findViewById(R.id.weatherImg);
        CurTemp=findViewById(R.id.CurTemp);
        MinTemp=findViewById(R.id.MinTemp);
        MaxTemp=findViewById(R.id.MaxTemp);
        weatherProg=findViewById(R.id.weatherProg);


        get_a_city();
    }
    public void get_a_city() {
        cityList = Arrays.asList(getResources().getStringArray(R.array.cities));

        final Spinner citySpinner = findViewById(R.id.cityListSpinner);
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(
                        this, R.array.cities, android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(adapter);
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView <?> adapterView, View view, int i, long l) {
                TextView text= (TextView) adapterView.getChildAt(0);
                if (!(text).equals(null)){
                    text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    text.setTextSize(20);}
                new ForecastQuery(cityList.get(i)).execute("this will go to background");
                weatherProg.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView <?> adapterView) {

            }
        });
    }

    class ForecastQuery extends AsyncTask<String,Integer,String>{
        String cTemp;
        String miTemp;
        String maTemp;
        Bitmap curImg;
        String icon;
        String urlString;
        String city;
        ForecastQuery(String city) {
            this.city=city;
            this.urlString="https://api.openweathermap.org/data/2.5/weather?q="+city+",ca&APPID=a53b596ea2bd3be638d2def1c62ff086&mode=xml&units=metric";
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();

                InputStream weather= conn.getInputStream();
                try {
                    XmlPullParser parser = Xml.newPullParser();
                    parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    parser.setInput(weather, null);
                    while (parser.next() != XmlPullParser.END_DOCUMENT) {
                        if (parser.getEventType() != XmlPullParser.START_TAG) {
                            continue;
                        }
                        String name = parser.getName();
                        Log.i("WEATHER",name);
                        if (name.equals("temperature")) {
                            cTemp=parser.getAttributeValue(null,"value");
                            publishProgress(25);
                            miTemp=parser.getAttributeValue(null,"min");
                            publishProgress(50);
                            maTemp=parser.getAttributeValue(null,"max");
                            publishProgress(75);
                        } else if (name.equals("weather")) {
                            icon = parser.getAttributeValue(null, "icon") + (".png");
                            if (fileExistence(icon)) {
                                FileInputStream fis = null;
                                try {
                                    fis = openFileInput(icon);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                curImg = BitmapFactory.decodeStream(fis);
                                Log.i("WeatherForecast","Found Image Locally");
                            }else{
                                    curImg = HttpUtils.getImage("https://openweathermap.org/img/w/" + icon);
                                    FileOutputStream outputStream = openFileOutput(icon, Context.MODE_PRIVATE);
                                    curImg.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                                    outputStream.flush();
                                    outputStream.close();
                                }
                            publishProgress(100);
                            }
                        }


                } finally {
                    weather.close();
                }

            }catch (Exception e){
                Log.e("WeatherForecast","error"+ e.toString());
            }
            return null;
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            Log.i ("vlaues",  values[0] +"-------------------------------") ;
           weatherProg.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            CurTemp.setText("Current Temperature is:\n"+cTemp+"°C");
            MinTemp.setText("Minimum Temperature is:\n"+miTemp+"°C");
            MaxTemp.setText("Maximum Temperature is:\n"+maTemp+"°C");
            weatherImg.setImageBitmap(curImg);
            weatherProg.setVisibility(View.INVISIBLE);
        }

        public boolean fileExistence(String fname){
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();   }
    }
    static class HttpUtils {
        public static Bitmap getImage(URL url) {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    return BitmapFactory.decodeStream(connection.getInputStream());
                } else
                    return null;
            } catch (Exception e) {
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }
        public static Bitmap getImage(String urlString) {
            try {
                URL url = new URL(urlString);
                return getImage(url);
            } catch (MalformedURLException e) {
                return null;
            }
        }

    }

}