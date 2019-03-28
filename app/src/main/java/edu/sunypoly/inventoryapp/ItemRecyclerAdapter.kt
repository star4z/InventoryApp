package edu.sunypoly.inventoryapp

import android.os.Handler
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import java.lang.reflect.InvocationHandler

import java.util.ArrayList

class ItemRecyclerAdapter(val handler: Handler, var mDataset: ArrayList<InventoryItem>?) : RecyclerView.Adapter<ItemViewHolder>() {

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
