package edu.sunypoly.inventoryapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

/**
 * This is the activity that is started when the app starts.
 */
class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.java.simpleName

    private var authenticator: Authenticator? = null //class which handles talking to the server

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        authenticator = Authenticator.instance //Authenticator is a singleton, so this is the way to access it

        updateUI(false)

    }

    override fun onResume() {
        super.onResume()

        initializeUI()
    }

    private fun initializeUI() {
        login_button.setOnClickListener {
            //TODO: add login logic
            authenticator?.login("", "")
            updateUI(true)
        }
    }

    /**
     * This is called when an activity that was started with startActivityForResult() returns
     * Only loginActivity calls this
     * Currently only used to show the "Logged in" pop-up when login is successful
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0){
            if (resultCode == Activity.RESULT_OK){
                Toast.makeText(this, "Logged in.", Toast.LENGTH_LONG).show()
                logout_button.visibility = View.VISIBLE
            }
        }

        logout_button.setOnClickListener {
            updateUI(false)
        }

        list_all_button.setOnClickListener {
            startActivity(Intent(this, ListAllItemsActivity::class.java))
        }

        search_button.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }

        add_item_button.setOnClickListener {
            startActivity(Intent(this, AddItemActivity::class.java))
        }
    }

    /**
     * Updates UI based on the current account.
     */
    private fun updateUI(loggedIn: Boolean) {

        if (loggedIn) {
            logout_button.isEnabled = true
            login_button.isEnabled = false
            list_all_button.isEnabled = true
            search_button.isEnabled = true
            add_item_button.isEnabled = true
        } else {
            logout_button.isEnabled = false
            login_button.isEnabled = true
            list_all_button.isEnabled = false
            search_button.isEnabled = false
            add_item_button.isEnabled = false
        }
    }
}
