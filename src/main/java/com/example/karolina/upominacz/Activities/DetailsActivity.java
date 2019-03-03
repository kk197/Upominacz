package com.example.karolina.upominacz.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.karolina.upominacz.R;

public class DetailsActivity extends AppCompatActivity {

    private TextView itemName;
    private TextView moreInfo;
    private TextView dateAdded;
    private int itemID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        itemName = (TextView) findViewById(R.id.itemNameDet);
        moreInfo = (TextView) findViewById(R.id.moreInfoDet);
        dateAdded = (TextView) findViewById(R.id.dateAddedDet);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null){
            itemName.setText(bundle.getString("name"));
            moreInfo.setText(bundle.getString("moreInfo"));
            dateAdded.setText(bundle.getString("date"));
            itemID = bundle.getInt("id");
        }
    }
}
