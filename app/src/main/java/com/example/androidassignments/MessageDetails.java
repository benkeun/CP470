package com.example.androidassignments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

public class MessageDetails extends AppCompatActivity {
    MessageFragment messageFrag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);
        Bundle args= getIntent().getBundleExtra("args");
        messageFrag=new MessageFragment(null);
        messageFrag.setArguments(args);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.messageFrame,messageFrag);
        ft.commit();}

    }
