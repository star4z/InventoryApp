package edu.sunypoly.inventoryapp

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

/**
 * This is the activity that is started when the app starts.
 */
class MainActivity : AppCompatActivity() {
    private var authenticator: Authenticator? = null //class which handles talking to the server

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        authenticator = Authenticator.instance //Authenticator is a singleton, so this is the way to access it

        //If the user is logged in, shows the logout button
        if (authenticator!!.isLoggedIn){
            logout_button.visibility = View.VISIBLE
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
    }

    /**
     * Called when login button is clicked (onClick="login" in R.layout.activity_main.xml)
     * starts LoginActivity
     */
    fun login(view: View) {
        startActivityForResult(Intent(this, LoginActivity::class.java), 0)
    }

    /**
     * Logs the user out (via Authenticator) and updates the UI appropriately when logout button is pressed
     */
    fun logout(view: View){
        authenticator!!.logout()
        Toast.makeText(this, "Logged out.", Toast.LENGTH_LONG).show()
        logout_button.visibility = View.INVISIBLE
        username_view.visibility = View.INVISIBLE
    }

    /**
     * starts ListAllItemsActivity when List all button is pressed
     */
    fun listAll(view: View) {
        startActivityIfLoggedIn(ListAllItemsActivity::class.java)
    }

    /**
     * starts SearchActivity when search button is pressed
     */
    fun search(view: View) {
        startActivityIfLoggedIn(SearchActivity::class.java)
    }

    /**
     * Starts AddItemActivity when Add item button is pressed
     */
    fun addItem(view: View) {
        startActivityIfLoggedIn(AddItemActivity::class.java)
    }

    /**
     * Generalized method that ensures the user is logged in before they can do stuff
     */
    fun <T> startActivityIfLoggedIn(c: Class<T>){
        if (authenticator!!.isLoggedIn) {
            startActivity(Intent(this, c))
        } else {
            Toast.makeText(this, "You need to be logged in to perform that action.", Toast.LENGTH_LONG).show()
        }
    }

}
