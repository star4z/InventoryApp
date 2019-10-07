package edu.sunypoly.inventoryapp

import android.os.AsyncTask
import android.util.Log
import com.google.gson.GsonBuilder
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*
import java.util.concurrent.ExecutionException

/**
 * Handles all the server stuff
 */
class Authenticator//Private constructor is used to make the class a singleton
private constructor() {
    //Used for nicer Log calls
    private val TAG = this.javaClass.simpleName

    /**
     * @return true if user is logged in, else false
     */
    internal var isLoggedIn: Boolean = false

    /**
     * Returns list of all items in a AuthenticatorStatus.ListStatus if the server is running,
     * else returns an appropriate error status
     * @return
     */
    internal val items: AuthenticatorStatus
        get() {
            if (isLoggedIn) {
                val readFilesTask = ReadFilesTask()
                readFilesTask.execute()
                try {
                    return readFilesTask.get()
                } catch (e: Exception) {
                    return AuthenticatorStatus.ServerError
                }

            } else {
                return AuthenticatorStatus.AuthError
            }
        }


    init {
        try {
            url = URL("http://150.156.202.112:8000/inventory")
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }

    }


    /**
     * Handles logging in
     * Obviously missing some things
     * @param username
     * @param password
     * @return
     */
    internal fun login(username: String, password: String): Boolean {
        //TODO
        isLoggedIn = true
        return isLoggedIn
    }

    /**
     * Logs the user out.
     */
    internal fun logout() {
        isLoggedIn = false
    }

    /**
     * Starts async task to add the item
     * @param item
     * @return
     */
    internal fun addItem(item: InventoryItem): AuthenticatorStatus {
        return if (isLoggedIn) {
            val addItemsTask = AddItemsTask(item)
            addItemsTask.execute()
            try {
                addItemsTask.get()
            } catch (e: Exception) {
                AuthenticatorStatus.ServerError
            }

        } else {
            Log.e(TAG, "Not logged in.")
            AuthenticatorStatus.AuthError
        }
    }


    /**
     * Asynchronously adds the item
     * Probably helpful to look at the documentation for android.os.AsyncTask
     */
    private class AddItemsTask internal constructor(private val item: InventoryItem) : AsyncTask<Void, Void, AuthenticatorStatus>() {


        override fun doInBackground(vararg voids: Void): AuthenticatorStatus {
            return addItemToDatabase(item)
        }

        private fun addItemToDatabase(item: InventoryItem): AuthenticatorStatus {
            val url: URL
            try {
                //Defines URL
                url = URL("http://150.156.202.112:8000/inventory")
                //Tries to connect to URL via HTTP protocol
                val httpURLConnection = url.openConnection() as HttpURLConnection
                httpURLConnection.requestMethod = "POST"

                //Inits GSON
                val gson = GsonBuilder().create()
                //GSON converts item to json
                val json = gson.toJson(item)

                httpURLConnection.doOutput = true
                //Connects to output stream
                val out = DataOutputStream(httpURLConnection.outputStream)
                out.writeBytes(json)//writes the json
                out.flush()//actually writes the json (Java stream thing, this needs to be called)
                out.close()//Closes the stream

                //Not necessary calls, but helpful if you want to fine tune things for some reason
                httpURLConnection.connectTimeout = 5000
                httpURLConnection.readTimeout = 5000

                val input = inputStreamToString(httpURLConnection.inputStream)

                return AuthenticatorStatus.AddedItem
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return AuthenticatorStatus.ServerError
        }

    }

    internal fun deleteItem(item: InventoryItem): Boolean {
        if (isLoggedIn) {
            val deleteItemsTask = DeleteItemsTask()
            deleteItemsTask.execute(item)
            try {
                return deleteItemsTask.get()
            } catch (e: ExecutionException) {
                e.printStackTrace()
                Log.e(TAG, "ExecutionException " + e.message)
                return false
            } catch (e: InterruptedException) {
                e.printStackTrace()
                Log.e(TAG, "InterruptedException " + e.message)
                return false
            }

        } else {
            Log.e(TAG, "Not logged in.")
            return false
        }
    }

    private class DeleteItemsTask : AsyncTask<InventoryItem, Void, Boolean>() {
        private val TAG = "DeleteItemsTask"

        override fun doInBackground(vararg items: InventoryItem): Boolean? {
            try {

                val httpURLConnection = url!!.openConnection() as HttpURLConnection
                httpURLConnection.requestMethod = "DELETE"

                val gson = GsonBuilder().create()
                val json = gson.toJson(items[0])

                Log.v(TAG, json)


                httpURLConnection.doOutput = true
                val out = DataOutputStream(httpURLConnection.outputStream)
                out.writeBytes(json)
                out.flush()
                out.close()

                httpURLConnection.connectTimeout = 5000
                httpURLConnection.readTimeout = 5000

                val input = inputStreamToString(httpURLConnection.inputStream)
                Log.v(TAG, input)

                return true
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return false
        }
    }


    class ReadFilesTask : AsyncTask<Void, Void, AuthenticatorStatus>() {

        private val itemsFromServer: AuthenticatorStatus
            get() {
                val url: URL
                try {
                    url = URL("http://150.156.202.112:8000/inventory")
                    val httpCon = url.openConnection() as HttpURLConnection
                    httpCon.requestMethod = "GET"

                    val gson = GsonBuilder().create()

                    val input = inputStreamToString(httpCon.inputStream)

                    val inventoryItems = gson.fromJson(input, Array<InventoryItem>::class.java)

                    return if (inventoryItems != null) {
                        AuthenticatorStatus.ListStatus("Successfully got items.", ArrayList(Arrays.asList(*inventoryItems)))
                    } else {
                        AuthenticatorStatus.NoItems
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                    return AuthenticatorStatus.ServerError
                }

            }

        override fun doInBackground(vararg voids: Void): AuthenticatorStatus {
            return itemsFromServer
        }
    }

    companion object {
        //characters reserved by JSON and SQL standards
        internal val ILLEGAL_CHARACTERS = charArrayOf('{', '}', '[', ']', '/', '\\', ':', '#', ',', '?', '&', '=', '<', '>', '(', ')', '*', '^', '!', '~', '-', '|', ';', '%')

        //This is used to make the class a singleton
        //This is how other classes access this class
        val instance = Authenticator()
        private var url: URL? = null


        @Throws(IOException::class)
        internal fun inputStreamToString(`is`: InputStream): String {
            val br = BufferedReader(InputStreamReader(`is`))
            val result = StringBuilder()
            var line = br.readLine()
            while (line != null) {
                result.append(line)
                line = br.readLine()
            }
            return result.toString()
        }
    }


}
