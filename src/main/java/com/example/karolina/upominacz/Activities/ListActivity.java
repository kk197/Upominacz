package com.example.karolina.upominacz.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

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
    private List<Item> listItems; //change!
    private DatabaseHandler db;

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

}
