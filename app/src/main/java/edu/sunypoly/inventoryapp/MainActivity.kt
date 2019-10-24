package edu.sunypoly.inventoryapp

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.microsoft.identity.client.*
import com.unboundid.ldap.sdk.AddRequest
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import com.unboundid.ldap.sdk.LDAPConnection
import com.unboundid.ldap.sdk.LDAPException
import java.text.SimpleDateFormat


/**
 * This is the activity that is started when the app starts.
 */
class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.java.simpleName

    private lateinit var prefs: SharedPreferences //File type for storing settings (mostly)
//    private var username: String? = null
//    private var password: String? = null
    private var authenticator: Authenticator? = null //class which handles talking to the server

    /* Azure AD v2 Configs */
    private val AUTHORITY = "https://login.microsoftonline.com/common"

    /* Azure AD Variables */
    private var mSingleAccountApp: ISingleAccountPublicClientApplication? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        authenticator = Authenticator.instance //Authenticator is a singleton, so this is the way to access it

//        val applicationCreatedListener = object : IPublicClientApplication.ISingleAccountApplicationCreatedListener {
//            override fun onCreated(application: ISingleAccountPublicClientApplication) {
//                /**
//                 * This test app assumes that the app is only going to support one account.
//                 * This requires "account_mode" : "SINGLE" in the config json file.
//                 *
//                 */
//                mSingleAccountApp = application
//
//                loadAccount()
//            }
//
//            override fun onError(exception: MsalException) {
////                        txt_log.text = exception.toString()
//            }
//        }
//        PublicClientApplication.createSingleAccountPublicClientApplication(
//                this,
//                R.raw.auth_config,
//                applicationCreatedListener
//        )

        updateUI(false)

        //If the user is logged in, shows the logout button
//        if (authenticator!!.isLoggedIn) {
//            logout_button.visibility = View.VISIBLE
//        }
    }

    override fun onResume() {
        super.onResume()

//        val instances = ServerInstance.getInstances(this)

        initializeUI()

//        loadAccount()
    }

    private class LDAPConnectTask(): AsyncTask<Void, Void, String>() {

        //    private val address = "https://10.156.193.2"
        private val address = "150.156.192.2"
        //private val port = 389 //MS ActiveDirectory
        //private val port = 636 // AD w/ SSL
        private val port = 7389 //LDAP
        //private val port = 7636 //LDAPS (w/ SSL)
        private val bindDN = "cn=users,dc=cs,dc=sunyit,dc=edu"
        private var loginFlag = true

        private val password = "nF6p:*}xQ7t"

        private lateinit var c: LDAPConnection
        private lateinit var addRequest: AddRequest

        override fun doInBackground(vararg params: Void?): String? {
            testConnect()
            return "Result"
        }

        private fun testConnect() {
            try {
                c = LDAPConnection(address, port, bindDN, password)
                c.connectionName = "Demo Connection"
                val conName = c.connectionName
                val time = c.connectTime
                val formatter = SimpleDateFormat("dd-MM-yy HH:mm:ss", Locale.getDefault())
                val dateString = formatter.format(Date(time))
                Log.d("LDAPConnectTask", "connected")
//                Toast.makeText(baseContext, "Connected to LDAP server....connection_name=$conName at time$dateString", Toast.LENGTH_LONG).show()

            } catch (e: LDAPException) {
                loginFlag = false
                e.printStackTrace()
//                Toast.makeText(baseContext, "No connection was established", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                if (loginFlag) {
                    c.close()
//                    Toast.makeText(baseContext, "Connection Closed successfully", Toast.LENGTH_LONG).show()
                }
            }
        }

    }



    private fun initializeUI() {
        login_button.setOnClickListener(View.OnClickListener {
//            if (mSingleAccountApp == null) {
//                return@OnClickListener
//            }
            val task = LDAPConnectTask()
            task.execute()
            val result = try {
                task.get()
            } catch (e: Exception) {
                AuthenticatorStatus.ServerError
            }

            Log.d(TAG, result.toString())
            updateUI(true)

//            mSingleAccountApp!!.signIn(this, "", getScopes(), getAuthInteractiveCallback())
        })

        logout_button.setOnClickListener(View.OnClickListener {
            updateUI(false)
//            if (mSingleAccountApp == null) {
//                return@OnClickListener
//            }
/*
            *//**
             * Removes the signed-in account and cached tokens from this app.
             *//*
            mSingleAccountApp!!.signOut(object : ISingleAccountPublicClientApplication.SignOutCallback {
                override fun onSignOut() {
                    updateUI(null)
                    performOperationOnSignOut()
                }

                override fun onError(exception: MsalException) {
                    exception.printStackTrace()
                }
            })*/
        })
    }

   /* private fun loadAccount() {
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
    }*/

    private fun getScopes(): Array<String> {
        val scope = "https://login.microsoftonline.com/cb22c659-8123-4b7f-95ee-d99779c14e3f/oauth2/v2.0/authorize"
        return scope.toLowerCase(Locale.getDefault()).split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
    }

    /**
     * Callback used for interactive request.
     * If succeeds we use the access token to call the Microsoft Graph.
     * Does not check cache.
     *//*
    private fun getAuthInteractiveCallback(): AuthenticationCallback {
        return object : AuthenticationCallback {

            override fun onSuccess(authenticationResult: IAuthenticationResult) {
                *//* Successfully got a token, use it to call a protected resource - MSGraph *//*
                Log.d(TAG, "Successfully authenticated")
                Log.d(TAG, "ID Token: " + authenticationResult.account.claims!!["id_token"])

                *//* Update account *//*
                updateUI(authenticationResult.account)

                *//* call graph *//*
//                callGraphAPI(authenticationResult)
            }

            override fun onError(exception: MsalException) {
                *//* Failed to acquireToken *//*
                Log.d(TAG, "Authentication failed: $exception")
//                displayError(exception)

                if (exception is MsalClientException) {
                    *//* Exception inside MSAL, more info inside MsalError.java *//*
                } else if (exception is MsalServiceException) {
                    *//* Exception when communicating with the STS, likely config issue *//*
                }
            }

            override fun onCancel() {
                *//* User canceled the authentication *//*
                Log.d(TAG, "User cancelled login.")
            }
        }
    }*/

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

    /*  */
    /**
     * Called when login button is clicked (onClick="login" in R.layout.activity_main.xml)
     * starts LoginActivity
     *//*
    fun login(view: View) {
        startActivityForResult(Intent(this, LoginActivity::class.java), 0)
    }

    */
    /**
     * Logs the user out (via Authenticator) and updates the UI appropriately when logout button is pressed
     *//*
    fun logout(view: View) {
        authenticator!!.logout()
        Toast.makeText(this, "Logged out.", Toast.LENGTH_LONG).show()
        logout_button.visibility = View.INVISIBLE
        current_user.visibility = View.INVISIBLE
    }

    */
    /**
     * starts ListAllItemsActivity when List all button is pressed
     *//*
    fun listAll(view: View) {
        startActivityIfLoggedIn(ListAllItemsActivity::class.java)
    }

    */
    /**
     * starts SearchActivity when search button is pressed
     *//*
    fun search(view: View) {
        startActivityIfLoggedIn(SearchActivity::class.java)
    }

    */
    /**
     * Starts AddItemActivity when Add item button is pressed
     *//*
    fun addItem(view: View) {
        startActivityIfLoggedIn(AddItemActivity::class.java)
    }

    */
    /**
     * Generalized method that ensures the user is logged in before they can do stuff
     *//*
    fun <T> startActivityIfLoggedIn(c: Class<T>) {
        if (authenticator!!.isLoggedIn) {
            startActivity(Intent(this, c))
        } else {
            Toast.makeText(this, "You need to be logged in to perform that action.", Toast.LENGTH_LONG).show()
        }
    }
*/
}
