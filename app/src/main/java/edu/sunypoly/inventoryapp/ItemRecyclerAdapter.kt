package edu.sunypoly.inventoryapp

import android.os.Handler
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import java.lang.reflect.InvocationHandler

import java.util.ArrayList

/**
 * Handles inflation of inventoryItems; connects ItemViewHolder to ListAllItemsActivity or SearchActivity
 */
class ItemRecyclerAdapter(val handler: Handler, var mDataset: ArrayList<InventoryItem>?) : androidx.recyclerview.widget.RecyclerView.Adapter<ItemViewHolder>() {

    fun getmDataset(): ArrayList<InventoryItem>? {
        return mDataset
    }

    fun setmDataset(mDataset: ArrayList<InventoryItem>) {
        this.mDataset = mDataset
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ItemViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.editable_list_item, viewGroup, false) as ConstraintLayout

        return ItemViewHolder(handler, v)
    }

    override fun onBindViewHolder(itemViewHolder: ItemViewHolder, i: Int) {
        val item = mDataset!![i]
        val line = "Name: " + item.name + "\n\tRoom: " + item.room +
                "\n\tAsset tag: " + item.barcode
        itemViewHolder.itemTextView.text = line
        itemViewHolder.inventoryItem = mDataset!![i]
    }

    override fun getItemCount(): Int {
        return mDataset!!.size
    }
}
