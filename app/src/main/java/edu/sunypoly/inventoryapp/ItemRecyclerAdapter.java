package edu.sunypoly.inventoryapp;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

public class ItemRecyclerAdapter extends RecyclerView.Adapter<ItemViewHolder> {

    private ArrayList<InventoryItem> mDataset;

    public ItemRecyclerAdapter(ArrayList<InventoryItem> dataset){
        mDataset = dataset;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.editable_list_item, viewGroup, false);

        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int i) {
        itemViewHolder.itemTextView.setText(mDataset.get(i).toString());
        itemViewHolder.inventoryItem = mDataset.get(i);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
