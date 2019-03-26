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

    public ArrayList<InventoryItem> getmDataset() {
        return mDataset;
    }

    public void setmDataset(ArrayList<InventoryItem> mDataset) {
        this.mDataset = mDataset;
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
        InventoryItem item = mDataset.get(i);
        String line = "Name: " + item.getName() + "\n\tRoom: " + item.getRoom() +
                "\n\tAsset tag: " + item.getBarcode();
        itemViewHolder.getItemTextView().setText(line);
        itemViewHolder.setInventoryItem(mDataset.get(i));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
