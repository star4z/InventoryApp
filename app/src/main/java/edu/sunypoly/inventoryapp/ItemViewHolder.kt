package edu.sunypoly.inventoryapp

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import java.io.IOException

/**
 * This handles stuff related to the views that are inflated in the RecyclerView in both ListAllItemsActivity and SearchActivity
 */
class ItemViewHolder(val handler: Handler, itemView: ConstraintLayout) : RecyclerView.ViewHolder(itemView) {

    val TAG = "ItemViewHolder"

    var itemTextView: TextView = itemView.findViewById(R.id.item_text_view)
    var editButton: ImageButton = itemView.findViewById(R.id.edit_button)
    var deleteButton: ImageButton = itemView.findViewById(R.id.delete_button)
    var inventoryItem: InventoryItem? = null

    init {

        //when the item is clicked, opens a basic page of more information
        itemTextView.setOnClickListener {
            if (inventoryItem != null) {
                val bundle = Bundle()
                try {
                    bundle.putByteArray("item", inventoryItem!!.toByteArray())
                } catch (e: IOException) {
                    e.printStackTrace()
                }


                val intent = Intent(itemView.context,
                        TellMeMoreActivity::class.java)
                intent.putExtras(bundle)

                itemView.context.startActivity(intent)
            } else {
                Toast.makeText(itemView.context, "Can't open, item is null.",
                        Toast.LENGTH_LONG).show()
            }
        }

        //when the edit button is clicked, opens AddItemActivity to edit it
        editButton.setOnClickListener {
            if (inventoryItem != null) {
                val bundle = inventoryItem!!.toBundle()

                Log.d("ViewHolder", bundle.toString())

                val intent = Intent(itemView.context,
                        AddItemActivity::class.java)
                intent.putExtras(bundle)

                itemView.context.startActivity(intent)
            } else {
                Toast.makeText(itemView.context, "Can't edit, item is null.",
                        Toast.LENGTH_LONG).show()
            }
        }

        //When the delete button is clicked, deletes the item from the database and updates the UI
        deleteButton.setOnClickListener {
            if (inventoryItem != null) {
                val bundle = inventoryItem!!.toBundle()

                Log.d("ViewHolder", bundle.toString())

                //Double checks with the user before deleting
                val builder = AlertDialog.Builder(itemView.context)
                builder.apply {
                    setMessage("Are you sure you want to delete this item?")
                    setPositiveButton("YES") { dialog, id ->
                        //Delete item

                        val auth = Authenticator.instance
                        auth.login("","")
                        auth.deleteItem(inventoryItem!!)
                        handler.sendMessage(handler.obtainMessage(UPDATE_ITEMS))
                    }
                    setNegativeButton("NO") { _, _ -> }
                }
                builder.create().show()
            } else {
                Toast.makeText(itemView.context, "Can't edit, item is null.",
                        Toast.LENGTH_LONG).show()
            }
        }
    }
}
