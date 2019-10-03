package edu.sunypoly.inventoryapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_list_all_items.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

import java.util.ArrayList

val NO_ITEMS = -1
val TASK_COMPLETE = 1
val GET_ITEMS = 0
val UPDATE_ITEMS = 2
val SERVER_ERROR = -2

/**
 * Lists every item in the database
 */
class ListAllItemsActivity : AppCompatActivity() {

    private val TAG = javaClass.simpleName

    var inventoryItems: ArrayList<InventoryItem>? = null
    private var mAdapter: androidx.recyclerview.widget.RecyclerView.Adapter<*>? = null
    private var layoutManager: androidx.recyclerview.widget.RecyclerView.LayoutManager? = null

    //Handler is needed to update the UI based on calls made on other threads
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


    /**
     * Called when the activity is first created.
     * Inits basic values
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_all_items)

        //Connects adapter to recycler_view (it's a RecyclerView)
        mAdapter = ItemRecyclerAdapter(handler, ArrayList())
        recycler_view!!.adapter = mAdapter
        //Sets the RecyclerView as a vertical linear layout (all items are in 1 column)
        layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        recycler_view!!.layoutManager = layoutManager

        //Displays loading thingy
        progress_bar.visibility = View.VISIBLE

        //Gets items from database
        updateItems()
    }

    /**
     * Starts asynchronous call to get inventory items from server
     */
    private fun updateItems() {
        //Status is used to receive more complicated results from authenticator. See the class' default instances.
        var status: AuthenticatorStatus = AuthenticatorStatus.AuthError

        //Using Kotlin coroutines to start async task
        GlobalScope.launch {
            val authenticator = Authenticator.instance
//            authenticator.login("", "")

            //Gets items from authenticator in the form of a status
            status = authenticator.items
            Log.d(TAG, status.message)
            when (status) {
                AuthenticatorStatus.GotItems -> {
                    //if the status is of the correct type, get the list of items from it
                    inventoryItems = (status as AuthenticatorStatus.ListStatus).data
                }
            }
            Log.d(TAG, "Got inventory items; $inventoryItems")
        }.invokeOnCompletion {
            onEventFinish(status)
        }
    }

    /**
     * Called once the authenticator does its stuff.
     * Tells handler what to do.
     */
    private fun onEventFinish(status: AuthenticatorStatus) {
        Log.d(TAG, "onEventFinished called")

        when (status) {
            AuthenticatorStatus.GotItems -> {
                handler.sendMessage(handler.obtainMessage(GET_ITEMS))
            }
            AuthenticatorStatus.NoItems -> {
                handler.sendMessage(handler.obtainMessage(NO_ITEMS))
            }
            else -> {
                handler.sendMessage(handler.obtainMessage(SERVER_ERROR))
            }
        }
        handler.sendMessage(handler.obtainMessage(TASK_COMPLETE))
    }


    /**
     * When inventory items is changed, this needs to be called to update the views
     * Probably could be done other ways but this works for what we need it to since this is usually
     * called when inventory items is initially null.
     */
    fun updateRecyclerView() {
        mAdapter = ItemRecyclerAdapter(handler, inventoryItems)
        recycler_view.adapter = mAdapter
    }
}
