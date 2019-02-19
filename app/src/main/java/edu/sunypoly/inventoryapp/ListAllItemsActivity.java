package edu.sunypoly.inventoryapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

public class ListAllItemsActivity extends AppCompatActivity {

    private ArrayList<InventoryItem> inventoryItems;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_all_items);

        inventoryItems = getItems();

        recyclerView = findViewById(R.id.recycler_view);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new ItemRecyclerAdapter(inventoryItems);
        recyclerView.setAdapter(mAdapter);
    }

    private ArrayList<InventoryItem> getItems(){
        Authenticator authenticator = new Authenticator(this);
        authenticator.login("", "");
        return authenticator.getItems();
    }
}
