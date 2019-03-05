package edu.sunypoly.inventoryapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ListAllItemsActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();

    private ArrayList<InventoryItem> inventoryItems;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_all_items);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        inventoryItems = getItems();

        if (inventoryItems != null) {

            recyclerView = findViewById(R.id.recycler_view);


            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);

            mAdapter = new ItemRecyclerAdapter(inventoryItems);
            recyclerView.setAdapter(mAdapter);

        } else {
            Toast.makeText(this, "No items to display.", Toast.LENGTH_LONG).show();
        }
    }

    private ArrayList<InventoryItem> getItems(){
        Authenticator authenticator = Authenticator.getInstance();
        authenticator.login("", "");
        return authenticator.getItems();
    }
}
