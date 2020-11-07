package com.example.androidassignments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        messageBar=findViewById(R.id.messageInput);
        listItems=findViewById(R.id.ListView);
        sendButton=findViewById(R.id.sendButton);
        messageAdapter =new ChatAdapter( ChatWindow.this );
        listItems.setAdapter (messageAdapter);

    }
    public void sendMessageFunc(View view){
        messages.add(messageBar.getText().toString());
        messageAdapter.notifyDataSetChanged(); //this restarts the process of getCount()/getView()
        messageBar.setText("");

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