package edu.sunypoly.inventoryapp

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    private lateinit var prefs: SharedPreferences
    private var username: String? = null
    private var password: String? = null
    private var authenticator: Authenticator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        authenticator = Authenticator.getInstance()

        if (authenticator!!.isLoggedIn){
            logout_button.visibility = View.VISIBLE
        }

        prefs = PreferenceManager.getDefaultSharedPreferences(this)
        username = prefs.getString("username", null)
        password = prefs.getString("password", null)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 0){
            if (resultCode == Activity.RESULT_OK){
                Toast.makeText(this, "Logged in.", Toast.LENGTH_LONG).show()
                logout_button.visibility = View.VISIBLE
            }
        }
    }

    fun login(view: View) {
        startActivityForResult(Intent(this, LoginActivity::class.java), 0)
    }

    fun logout(view: View){
        authenticator!!.logout()
        Toast.makeText(this, "Logged out.", Toast.LENGTH_LONG).show()
        logout_button.visibility = View.INVISIBLE
    }

    fun listAll(view: View) {
        startActivityIfLoggedIn(ListAllItemsActivity::class.java)
    }

    fun search(view: View) {
        startActivityIfLoggedIn(SearchActivity::class.java)
    }

    fun addItem(view: View) {
        startActivityIfLoggedIn(AddItemActivity::class.java)
    }

    fun <T> startActivityIfLoggedIn(c: Class<T>){
        if (authenticator!!.isLoggedIn) {
            startActivity(Intent(this, c))
        } else {
            Toast.makeText(this, "You need to be logged in to perform that action.", Toast.LENGTH_LONG).show()
        }
    }

}
