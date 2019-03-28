package edu.sunypoly.inventoryapp;

import android.os.AsyncTask;
import android.util.Log;

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
    private final String TAG = this.getClass().getSimpleName();
    //characters reserved by JSON and SQL standards
     static final char[] ILLEGAL_CHARACTERS = new char[]{'{', '}', '[', ']', '/', '\\', ':', '#',
            ',', '?', '&', '=', '<', '>', '(', ')', '*', '^', '!', '~', '-', '|', ';', '%'};

    private static final Authenticator authenticator = new Authenticator();

    private boolean loggedIn;
    private String userKey;
    private static URL url;

    private Authenticator() {
        try {
            url = new URL("http://150.156.202.112:8000/inventory");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static Authenticator getInstance() {
        return authenticator;
    }


    boolean login(String username, String password) {
        //TODO
        loggedIn = true;
        return loggedIn;
    }

    void logout() {
        loggedIn = false;
    }

    boolean isLoggedIn(){
        return loggedIn;
    }

    boolean addItem(InventoryItem item) {
        if (loggedIn) {
            AddItemsTask addItemsTask = new AddItemsTask(item);
            addItemsTask.execute(this);
            try {
                return addItemsTask.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
                Log.e(TAG, "ExecutionException " + e.getMessage());
                return false;
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.e(TAG, "InterruptedException " + e.getMessage());
                return false;
            }
        } else {
            Log.e(TAG, "Not logged in.");
            return false;
        }
    }

    private boolean addItemToDatabase(InventoryItem item) {
        URL url;
        try {
            url = new URL("http://150.156.202.112:8000/inventory");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");

            Gson gson = new GsonBuilder().create();
            String json = gson.toJson(item);

            Log.v(TAG, json);


            httpURLConnection.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(httpURLConnection.getOutputStream());
            out.writeBytes(json);
            out.flush();
            out.close();

            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setReadTimeout(5000);

            String input = inputStreamToString(httpURLConnection.getInputStream());
            Log.v(TAG, input);

            return true;
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
            Log.e(TAG, "You must be logged in to perform that action.");
        }
        return null;
    }

    boolean deleteItem(InventoryItem item) {
        if (loggedIn) {
            DeleteItemsTask deleteItemsTask = new DeleteItemsTask();
            deleteItemsTask.execute(item);
            try {
                return deleteItemsTask.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
                Log.e(TAG, "ExecutionException " + e.getMessage());
                return false;
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.e(TAG, "InterruptedException " + e.getMessage());
                return false;
            }
        } else {
            Log.e(TAG, "Not logged in.");
            return false;
        }
    }

    private static class DeleteItemsTask extends AsyncTask<InventoryItem, Void, Boolean> {
        private final String TAG = "DeleteItemsTask";

        @Override
        protected Boolean doInBackground(InventoryItem... items) {
            try {

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("DELETE");

                Gson gson = new GsonBuilder().create();
                String json = gson.toJson(items[0]);

                Log.v(TAG, json);


                httpURLConnection.setDoOutput(true);
                DataOutputStream out = new DataOutputStream(httpURLConnection.getOutputStream());
                out.writeBytes(json);
                out.flush();
                out.close();

                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setReadTimeout(5000);

                String input = inputStreamToString(httpURLConnection.getInputStream());
                Log.v(TAG, input);

                return true;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;
        }
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

            if (inventoryItems != null) {
                return new ArrayList<>(Arrays.asList(inventoryItems));
            } else {
                return null;
            }
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
