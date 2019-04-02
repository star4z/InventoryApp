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
        var status: AuthenticatorStatus = AuthenticatorStatus.AuthError
        GlobalScope.launch {
            val authenticator = Authenticator.getInstance()
            authenticator.login("", "")
            status = authenticator.items
            Log.d(TAG, status.message)
            when (status) {
                AuthenticatorStatus.GotItems -> {
                    inventoryItems = (status as AuthenticatorStatus.ListStatus).data
                }
            }
            Log.d(TAG, "Got inventory items; $inventoryItems")
        }.invokeOnCompletion {
            onEventFinish(status)
        }
    }

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


    fun updateRecyclerView() {
        mAdapter = ItemRecyclerAdapter(handler, inventoryItems)
        recycler_view.adapter = mAdapter
    }
}
