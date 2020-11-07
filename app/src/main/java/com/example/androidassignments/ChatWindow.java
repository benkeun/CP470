package com.example.androidassignments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatWindow extends AppCompatActivity {
    EditText messageBar;
    ListView listItems;
    Button sendButton;
    ArrayList<String> messages = new ArrayList<String>();
    ChatAdapter messageAdapter;
    ChatDatabaseHelper dbHelper;
    SQLiteDatabase database;
    static String ACTIVITY_NAME="ChatWindow.java";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        messageBar=findViewById(R.id.messageInput);
        listItems=findViewById(R.id.ListView);
        sendButton=findViewById(R.id.sendButton);
        messageAdapter =new ChatAdapter( ChatWindow.this );
        listItems.setAdapter (messageAdapter);
        dbHelper = new ChatDatabaseHelper(this);
        database = dbHelper.getWritableDatabase();
        Cursor c= database.query(false, "MessageTable", null, null, null, null, null, null, null);
        c.moveToFirst();
        while
        (!c.isAfterLast()) {
            String msg = c.getString( c.getColumnIndex( ChatDatabaseHelper.KEY_MESSAGE));
            messages.add(msg);
            Log.i(ACTIVITY_NAME, "SQL MESSAGE:" +msg );
            int count =c.getColumnCount();
            Log.i(ACTIVITY_NAME, "Cursor’s  column count =" + count);
            for (int columnIndex=0;columnIndex<count;columnIndex++){
                Log.i(ACTIVITY_NAME, "Column: "+c.getColumnName(columnIndex));
            }
            c.moveToNext();
        }
        }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.close();
    }

    public void sendMessageFunc(View view){
        String newMsg=messageBar.getText().toString();
        messages.add(newMsg);
        messageAdapter.notifyDataSetChanged(); //this restarts the process of getCount()/getView()
        messageBar.setText("");
        ContentValues cValues= new ContentValues();
        cValues.put(ChatDatabaseHelper.KEY_MESSAGE,newMsg);
        database.insert(ChatDatabaseHelper.TABLE_NAME,"NullPlaceHolder",cValues);

    }
    private class ChatAdapter extends ArrayAdapter<String>{
        Context mContext;
        public ChatAdapter(Context ctx){
            super(ctx,0);
            mContext=ctx;
        }
        public int getCount(){
            return messages.size();
        }
        public String getItem(int position){
            return messages.get(position);
        }
        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result = null ;
            if(position%2 == 0)
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            else
                result = inflater.inflate(R.layout.chat_row_outgoing, null);
            TextView message = result.findViewById(R.id.message_text);
            message.setText(   getItem(position)  ); // get the string at position
            return result;

        }

    }
}