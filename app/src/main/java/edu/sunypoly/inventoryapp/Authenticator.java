package edu.sunypoly.inventoryapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class Authenticator {
    private  final String TAG = this.getClass().getSimpleName();
    //characters reserved by JSON and SQL standards
    static final char[] ILLEGAL_CHARACTERS = new char[]{'{', '}', '[', ']', '/', '\\', ':', '#',
            ',', '?', '&', '=', '<', '>', '(', ')', '*', '^', '!', '~', '-', '|', ';', '%'};

    private static final Authenticator authenticator = new Authenticator();

    private boolean loggedIn;
    private String userKey;

    private Authenticator() {
    }

    public static Authenticator getInstance() {
        return authenticator;
    }


    void login(String username, String password) {
        //TODO
        loggedIn = true;
    }

    void logout() {
        loggedIn = false;
    }

    boolean addItem(InventoryItem item) {
        if (loggedIn) {
//            ProgressDialog progressDialog = ProgressDialog.show(context
//                    , "Contacting " +
//                            "server...", "Adding item: " + item);
            AddItemsTask addItemsTask = new AddItemsTask(item);
            addItemsTask.execute(this);
//            Toast.makeText(context, "Added item.", Toast.LENGTH_LONG)
//                    .show();
            try {
                return addItemsTask.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
                return false;
            } catch (InterruptedException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
//            Toast.makeText(context, "You must be logged in to perform that action.",
//                    Toast.LENGTH_LONG).show();
        }
    }

    private boolean addItemToDatabase(InventoryItem item) {
        URL url;
        try {
            url = new URL("http://150.156.202.112:8000/inventory");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("PUT");

            Gson gson = new GsonBuilder().create();
            String json = gson.toJson(item);

            Log.v("AddItemActivity", json);


            httpURLConnection.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(httpURLConnection.getOutputStream());
            out.writeBytes(json);
            out.flush();
            out.close();

            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setReadTimeout(5000);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    private static class AddItemsTask extends AsyncTask<Authenticator, Void, Boolean> {

        private InventoryItem item;

        AddItemsTask(InventoryItem item) {
            this.item = item;
        }

        @Override
        protected Boolean doInBackground(Authenticator... authenticators) {
            return authenticators[0].addItemToDatabase(item);
        }
    }

    ArrayList<InventoryItem> getItems() {
        if (loggedIn) {
            ReadFilesTask readFilesTask = new ReadFilesTask();
            readFilesTask.execute(this);
            try {
                return readFilesTask.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
//            Toast.makeText(context, "You must be logged in to perform that action.",
//                    Toast.LENGTH_LONG).show();
            Log.e(TAG, "You must be logged in to perform that action.");
        }
        return null;
    }

    private ArrayList<InventoryItem> getItemsFromServer() {
        URL url;
        try {
            url = new URL("http://150.156.202.112:8000/inventory");
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setRequestMethod("GET");

            Gson gson = new GsonBuilder().create();

            String input = inputStreamToString(httpCon.getInputStream());

            InventoryItem[] inventoryItems = gson.fromJson(input, InventoryItem[].class);

            return new ArrayList<>(Arrays.asList(inventoryItems));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class ReadFilesTask extends AsyncTask<Authenticator, Void, ArrayList<InventoryItem>> {

        @Override
        protected ArrayList<InventoryItem> doInBackground(Authenticator... authenticators) {
            return authenticators[0].getItemsFromServer();
        }
    }


    static String inputStreamToString(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            result.append(line);
        }
        return result.toString();
    }
}
