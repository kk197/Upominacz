package com.example.karolina.upominacz.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.example.karolina.upominacz.Data.DatabaseHandler;
import com.example.karolina.upominacz.Model.Item;
import com.example.karolina.upominacz.R;

public class MainActivity extends AppCompatActivity {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText item;
    private EditText info;
    private Button saveButton;
    private DatabaseHandler db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHandler(this);

        byPassActivity();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                createPopupDialog();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createPopupDialog(){

        dialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup, null);
        item = (EditText) view.findViewById(R.id.item);
        info = (EditText) view.findViewById(R.id.info);
        saveButton = (Button) view.findViewById(R.id.saveButton);

        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();

        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if (!item.getText().toString().isEmpty()
                        && !info.getText().toString().isEmpty()) {
                    saveItemToDB(v);
                }
            }
            
        });


    }

    private void saveItemToDB(View v) {

        Item it = new Item();
        String newItem = item.getText().toString();
        String newMoreInfo = info.getText().toString();

        it.setName(newItem);
        it.setMoreInfo(newMoreInfo);

        db.addItem(it);

        Snackbar.make(v, "Zapisano!", Snackbar.LENGTH_LONG).show();

        //Log.d("ID:", String.valueOf(db.getItemsCount()));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                //start a new activity
                startActivity(new Intent(MainActivity.this, ListActivity.class));
            }
        }, 1000); //1s
    }

    public void byPassActivity(){
        // if db is not empty go to ListActivity

        if(db.getItemsCount()>0){
            startActivity(new Intent(MainActivity.this, ListActivity.class));
            finish();
        }
    }
}
