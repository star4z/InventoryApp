package edu.sunypoly.inventoryapp

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.microsoft.identity.client.IAccount
import com.microsoft.identity.client.IPublicClientApplication
import com.microsoft.identity.client.ISingleAccountPublicClientApplication
import com.microsoft.identity.client.PublicClientApplication
import com.microsoft.identity.client.exception.MsalException
import kotlinx.android.synthetic.main.activity_main.*


/**
 * This is the activity that is started when the app starts.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var prefs: SharedPreferences //File type for storing settings (mostly)
    private var username: String? = null
    private var password: String? = null
    private var authenticator: Authenticator? = null //class which handles talking to the server

    /* Azure AD v2 Configs */
    private val AUTHORITY = "https://login.microsoftonline.com/common"

    /* Azure AD Variables */
    private var mSingleAccountApp: ISingleAccountPublicClientApplication? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        authenticator = Authenticator.instance //Authenticator is a singleton, so this is the way to access it

        val applicationCreatedListener = object : IPublicClientApplication.ISingleAccountApplicationCreatedListener {
            override fun onCreated(application: ISingleAccountPublicClientApplication) {
                /**
                 * This test app assumes that the app is only going to support one account.
                 * This requires "account_mode" : "SINGLE" in the config json file.
                 *
                 */
                mSingleAccountApp = application

                loadAccount()
            }

            override fun onError(exception: MsalException) {
//                        txt_log.text = exception.toString()
            }
        }
        PublicClientApplication.createSingleAccountPublicClientApplication(
                this,
                R.raw.auth_config,
                applicationCreatedListener
        )

        //If the user is logged in, shows the logout button
        if (authenticator!!.isLoggedIn) {
            logout_button.visibility = View.VISIBLE
        }


        //This is unused; supposed to be a way to store password without logging in every time, but this was decided to not be a feature
        prefs = PreferenceManager.getDefaultSharedPreferences(this)
        username = prefs.getString("username", null)
        password = prefs.getString("password", null)

    }

    private fun loadAccount() {
        if (mSingleAccountApp == null) {
            return
        }

        mSingleAccountApp!!.getCurrentAccountAsync(object :
                ISingleAccountPublicClientApplication.CurrentAccountCallback {
            override fun onAccountLoaded(activeAccount: IAccount?) {
                updateUI(activeAccount)
            }

            override fun onAccountChanged(priorAccount: IAccount?, currentAccount: IAccount?) {
                if (currentAccount == null) {
                    // Perform a cleanup task as the signed-in account changed.
                    performOperationOnSignOut()
                }
            }

            override fun onError(exception: MsalException) {
//                txt_log.text = exception.toString()
            }
        })
    }

    /**
     * Updates UI based on the current account.
     */
    private fun updateUI(account: IAccount?) {

        if (account != null) {
            logout_button.isEnabled = true
            login_button.isEnabled = false
        } else {
            logout_button.isEnabled = false
            login_button.isEnabled = true
        }
    }

    /**
     * Updates UI when app sign out succeeds
     */
    private fun performOperationOnSignOut() {
        val signOutText = "Signed Out."
        current_user.text = ""
        Toast.makeText(this, signOutText, Toast.LENGTH_SHORT)
                .show()
    }


    /**
     * This is called when an activity that was started with startActivityForResult() returns
     * Only loginActivity calls this
     * Currently only used to show the "Logged in" pop-up when login is successful
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
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
    fun logout(view: View) {
        authenticator!!.logout()
        Toast.makeText(this, "Logged out.", Toast.LENGTH_LONG).show()
        logout_button.visibility = View.INVISIBLE
        current_user.visibility = View.INVISIBLE
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
    fun <T> startActivityIfLoggedIn(c: Class<T>) {
        if (authenticator!!.isLoggedIn) {
            startActivity(Intent(this, c))
        } else {
            Toast.makeText(this, "You need to be logged in to perform that action.", Toast.LENGTH_LONG).show()
        }
    }

}
