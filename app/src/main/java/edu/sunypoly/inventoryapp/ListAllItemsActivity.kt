package edu.sunypoly.inventoryapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_list_all_items.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

import java.util.ArrayList

private val NO_ITEMS = -1
private val TASK_COMPLETE = 1
private val GET_ITEMS = 0
val UPDATE_ITEMS = 2
private val SERVER_ERROR = -2

class ListAllItemsActivity : AppCompatActivity() {

    private val TAG = javaClass.simpleName

    var inventoryItems: ArrayList<InventoryItem>? = null
    private var mAdapter: RecyclerView.Adapter<*>? = null
    private var layoutManager: RecyclerView.LayoutManager? = null

    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(inputMessage: Message) {
            Log.d(TAG, "Handle message received.")
            when (inputMessage.what) {
                GET_ITEMS -> {
                    Log.v(TAG, "GET ITEMS")
                    updateRecyclerView()
                }
                NO_ITEMS -> {
                    Log.v(TAG, "NO_ITEMS")
                    Toast.makeText(this@ListAllItemsActivity, "No items to display.", Toast.LENGTH_LONG).show()
                    item_info_text.text = "No items"
                }
                SERVER_ERROR -> {
                    Log.v(TAG, "SERVER ERROR")
                    item_info_text.text = "Could not contact server."
                }
                TASK_COMPLETE -> {
                    Log.v(TAG, "TASK_COMPLETE")
                    progress_bar.visibility = View.GONE
                }
                UPDATE_ITEMS -> {
                    Log.v(TAG, "UPDATE ITEMS")
                    updateItems()
                    updateRecyclerView()
                }
            }

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_all_items)

        mAdapter = ItemRecyclerAdapter(handler, ArrayList())
        recycler_view!!.adapter = mAdapter
        layoutManager = LinearLayoutManager(this)
        recycler_view!!.layoutManager = layoutManager

        progress_bar.visibility = View.VISIBLE

        updateItems()
    }

    private fun updateItems() {
        GlobalScope.launch {
            val authenticator = Authenticator.getInstance()
            authenticator.login("", "")
            inventoryItems = authenticator.items
            Log.d(TAG, "Got inventory items; $inventoryItems")
        }.invokeOnCompletion {
            onEventFinish()
        }
    }

    private fun onEventFinish() {
        Log.d(TAG, "onEventFinished called")
        if (inventoryItems != null) {
            handler.sendMessage(handler.obtainMessage(GET_ITEMS))
        } else {
            handler.sendMessage(handler.obtainMessage(NO_ITEMS))
        }
        handler.sendMessage(handler.obtainMessage(TASK_COMPLETE))
    }


    fun updateRecyclerView() {
        mAdapter = ItemRecyclerAdapter(handler, inventoryItems)
        recycler_view.adapter = mAdapter
    }
}
