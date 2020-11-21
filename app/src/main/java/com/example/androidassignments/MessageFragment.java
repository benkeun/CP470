package com.example.androidassignments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MessageFragment extends Fragment {
ChatWindow windowChat;
    public MessageFragment(@Nullable ChatWindow window ) {
        windowChat=window;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View lay= inflater.inflate(R.layout.fragment,container,false);

        TextView text = lay.findViewById(R.id.messageDisplay);
        final TextView id = lay.findViewById(R.id.idTxt);
        Button deleteBtn = lay.findViewById(R.id.deleteBtn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (windowChat==(null)){
                    Intent data = new Intent();
                    data.putExtra("msgId",Integer.parseInt(id.getText().toString()));
                    getActivity().setResult(1, data);
                    getActivity().finish();
                }
                else{
                    windowChat.deleteMsg(Integer.parseInt(id.getText().toString()));
                }
            }
        });
        text.setText(getArguments().getString("msg"));
        id.setText(String.valueOf(getArguments().getLong("id")));
        return lay;

    }
}
