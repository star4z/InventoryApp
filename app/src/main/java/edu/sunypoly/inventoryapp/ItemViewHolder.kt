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

class ItemViewHolder(val handler: Handler, itemView: ConstraintLayout) : RecyclerView.ViewHolder(itemView) {

    val TAG = "ItemViewHolder"

    var itemTextView: TextView = itemView.findViewById(R.id.item_text_view)
    var editButton: ImageButton = itemView.findViewById(R.id.edit_button)
    var deleteButton: ImageButton = itemView.findViewById(R.id.delete_button)
    var inventoryItem: InventoryItem? = null

    init {

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

        deleteButton.setOnClickListener {
            if (inventoryItem != null) {
                val bundle = inventoryItem!!.toBundle()

                Log.d("ViewHolder", bundle.toString())

                val builder = AlertDialog.Builder(itemView.context)
                builder.apply {
                    setMessage("Are you sure you want to delete this item?")
                    setPositiveButton("YES") { dialog, id ->
                        //Delete item

                        val auth = Authenticator.getInstance()
                        auth.login("","")
                        auth.deleteItem(inventoryItem)
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
