package com.example.androidassignments;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatWindow extends AppCompatActivity {
    final int CLICKITEM =100;
    EditText messageBar;
    ListView listItems;
    Button sendButton;
    ArrayList<String> messages = new ArrayList<String>();
    ChatAdapter messageAdapter;
    ChatDatabaseHelper dbHelper;
    SQLiteDatabase database;
    Cursor c;
    MessageFragment messageFrag;
    boolean wide=false;
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
        listItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle args = new Bundle();
                args.putLong("id",l);
                args.putString("msg",messageAdapter.getItem(i));
                messageFrag=new MessageFragment(ChatWindow.this);
                messageFrag.setArguments(args);
                if (wide){
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.add(R.id.frame,messageFrag);
                ft.commit();}
                else{
                    Intent intent = new Intent(ChatWindow.this,MessageDetails.class);
                    intent.putExtra("args",args);
                    startActivityForResult(intent,CLICKITEM);
                }
            }
        });
        wide= (findViewById(R.id.frame)!=(null));
        dbHelper = new ChatDatabaseHelper(this);
        database = dbHelper.getWritableDatabase();
        updateMessages();
        }
    @Override
    protected void onPause(){
        if (wide){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.remove(messageFrag);
            ft.commit();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.close();
    }
    public void updateMessages(){
        messages= new ArrayList<>();
        c= database.query(false, "MessageTable", null, null, null, null, null, null, null);
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

    public void sendMessageFunc(View view){
        String newMsg=messageBar.getText().toString();
        messages.add(newMsg);
        messageAdapter.notifyDataSetChanged(); //this restarts the process of getCount()/getView()
        messageBar.setText("");
        ContentValues cValues= new ContentValues();
        cValues.put(ChatDatabaseHelper.KEY_MESSAGE,newMsg);
        database.insert(ChatDatabaseHelper.TABLE_NAME,"NullPlaceHolder",cValues);

    }

    public void deleteMsg(int id) {
        if (wide){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.remove(messageFrag);
            ft.commit();
        }
        database.delete(ChatDatabaseHelper.TABLE_NAME,ChatDatabaseHelper.KEY_ID+"="+id,null);
       updateMessages();
       messageAdapter.notifyDataSetChanged();

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
        public long getItemId(int position){
            Log.i("PositionValue", String.valueOf(position));
            c.moveToPosition(position);
            long k = c.getLong( c.getColumnIndex( ChatDatabaseHelper.KEY_ID));
            Log.i("THISISID",String.valueOf(k));
            return k;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CLICKITEM && resultCode==1){
            int id=data.getIntExtra("msgId",-1);
            if (id!=-1){
                deleteMsg(id);
            }
        }
    }
}