package com.example.karolina.upominacz.UI;


import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.karolina.upominacz.Activities.DetailsActivity;
import com.example.karolina.upominacz.Data.DatabaseHandler;
import com.example.karolina.upominacz.Model.Item;
import com.example.karolina.upominacz.R;

import java.util.List;

public class RecyclerViewAdater extends RecyclerView.Adapter<RecyclerViewAdater.ViewHolder> {

    private Context context;
    private List<Item> itemList;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;
    private LayoutInflater inflater;

    public RecyclerViewAdater(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public RecyclerViewAdater.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_row, viewGroup, false);

        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdater.ViewHolder viewHolder, int i) {

        Item item = itemList.get(i);

        viewHolder.itemName.setText(item.getName());
        viewHolder.moreInfo.setText(item.getMoreInfo());
        viewHolder.dateAdded.setText(item.getDateItemAdded());

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView itemName;
        public TextView moreInfo;
        public TextView dateAdded;
        public Button editButton;
        public Button deleteButton;
        public int id;

        public ViewHolder(@NonNull View view, Context ctx) {
            super(view);
            context = ctx;
            itemName = (TextView) view.findViewById(R.id.name);
            moreInfo = (TextView) view.findViewById(R.id.moreInfo);
            dateAdded = (TextView) view.findViewById(R.id.dateAdded);
            editButton = (Button) view.findViewById(R.id.editButton);
            deleteButton = (Button) view.findViewById(R.id.deleteButton);

            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);

            view.setOnClickListener(new View.OnClickListener(){
    //gdy klikniemy cala karte przejdzie do kolejnego okna
                @Override
                public void onClick(View v) {
                    //go to next
                    int position = getAdapterPosition();

                    Item item = itemList.get(position);
                    Intent intent = new Intent(context, DetailsActivity.class);
                    intent.putExtra("name", item.getName());
                    intent.putExtra("moreInfo", item.getMoreInfo());
                    intent.putExtra("id", item.getId());
                    intent.putExtra("date", item.getDateItemAdded());
                    context.startActivity(intent);

                }
            });
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.editButton:
                    int position = getAdapterPosition();
                    Item item = itemList.get(position);
                    editItem(item);

                    break;
                case R.id.deleteButton:
                    position = getAdapterPosition();
                    item = itemList.get(position);
                    deleteItem(item.getId());
                    break;

            }

        }

        public void deleteItem(final int id) {
            //create an AlertDIalog
            alertDialogBuilder = new AlertDialog.Builder(context);

            inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.confirmation_dialog, null);

            Button noButton = (Button) view.findViewById(R.id.noButton);
            Button yesButton = (Button) view.findViewById(R.id.yesButton);

            alertDialogBuilder.setView(view);
            dialog = alertDialogBuilder.create();
            dialog.show();

            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //delete item
                    DatabaseHandler db = new DatabaseHandler(context);
                    db.deleteItem(id);
                    itemList.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    dialog.dismiss();

                }
            });
        }

        public void editItem(final Item item){
            alertDialogBuilder = new AlertDialog.Builder(context);

            inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.popup, null);

            final EditText itemName = (EditText) view.findViewById(R.id.item);
            final EditText info = (EditText) view.findViewById(R.id.info);
            final TextView title = (TextView) view.findViewById(R.id.tile);

            title.setText("Edytuj element");
            Button saveButton = (Button) view.findViewById(R.id.saveButton);

            alertDialogBuilder.setView(view);
            dialog = alertDialogBuilder.create();
            dialog.show();

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseHandler db = new DatabaseHandler(context);
                    //update
                    item.setName(itemName.getText().toString());
                    item.setMoreInfo(info.getText().toString());

                    if (!itemName.getText().toString().isEmpty()
                            && !info.getText().toString().isEmpty()){
                        db.updateItem(item);
                        notifyItemChanged(getAdapterPosition(),item);
                    }else{
                        Snackbar.make(v, "Dodaj nazwę i informacje", Snackbar.LENGTH_LONG).show();
                    }

                    dialog.dismiss();
                }
            });
        }
    }
}
