package com.example.androidassignments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class TestToolbar extends AppCompatActivity {
    String snackText;
    EditText toolInput;
    View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toolbar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        snackText=getString(R.string.penguin);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, getString(R.string.realBar), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
    public boolean onCreateOptionsMenu (Menu m){
        getMenuInflater().inflate(R.menu.toolbar_menu, m );
        return true;

    }
    public boolean onOptionsItemSelected(MenuItem mi){
        int id=mi.getItemId();
        switch(id) {
            case R.id.MenuOne:
                Snackbar.make(findViewById(R.id.toolbar), snackText, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Log.d("Toolbar", "Penguin!");
                break;
            case R.id.MenuTwo:
                AlertDialog.Builder builder = new AlertDialog.Builder(TestToolbar.this);
                builder.setTitle(R.string.dialog_title);
// Add the buttons
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
// Create the AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();

                Log.d("Toolbar", "Reindeer!");
                break;
            case R.id.MenuThree:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(TestToolbar.this);
                // Get the layout inflater
                LayoutInflater inflater = this.getLayoutInflater();

                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                v=inflater.inflate(R.layout.dialog_toolbar, null);
                builder2.setView(v)
                        // Add action buttons
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                toolInput=v.findViewById(R.id.toolInput);
                                snackText=toolInput.getText().toString();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        }).setTitle(getString(R.string.dialTool));
                AlertDialog dialog2=builder2.create();
                dialog2.show();
                Log.d("Toolbar", "Snowman!");
                break;
            case R.id.About:
                Toast toast = Toast.makeText(this,getString(R.string.versionAndName),Toast.LENGTH_LONG);
                toast.show();
                break;
        }
        return true;
    }


}