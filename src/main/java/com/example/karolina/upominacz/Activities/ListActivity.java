package com.example.karolina.upominacz.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.karolina.upominacz.Data.DatabaseHandler;
import com.example.karolina.upominacz.Model.Item;
import com.example.karolina.upominacz.R;
import com.example.karolina.upominacz.UI.RecyclerViewAdater;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewAdater recyclerViewAdapter;
    private List<Item> dbItemList;
    private List<Item> listItems;
    private DatabaseHandler db;

    private AlertDialog.Builder dialogBuilder;
    private EditText item;
    private EditText info;
    private Button saveButton;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
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

        db = new DatabaseHandler(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbItemList = new ArrayList<>();
        listItems = new ArrayList<>();

        //get items from db
        dbItemList = db.getAllItems();

        for (Item i : dbItemList){
            Item it = new Item();
            it.setName(i.getName());
            it.setMoreInfo(i.getMoreInfo());
            it.setId(i.getId());
            it.setDateItemAdded(i.getDateItemAdded());

            listItems.add(it);
        }

        recyclerViewAdapter = new RecyclerViewAdater(this, listItems);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();
    }

    private void createPopupDialog() {

        dialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup, null);
        item = (EditText) view.findViewById(R.id.item);
        info = (EditText) view.findViewById(R.id.info);
        saveButton = (Button) view.findViewById(R.id.saveButton);

        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

        //Log.d("Item added ID:", String.valueOf(db.getItemsCount()));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                //start a new activity
                startActivity(new Intent(ListActivity.this, ListActivity.class));
            }
        }, 1000); //1s
    }

}
