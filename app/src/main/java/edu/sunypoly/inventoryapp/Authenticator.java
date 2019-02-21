package edu.sunypoly.inventoryapp;

import android.content.Context;
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

public class Authenticator {
    //characters reserved by JSON and SQL standards
    public static char[] ILLEGAL_CHARACTERS = new char[]{'{', '}', '[', ']', '/', '\\', ':', '#',
            ',', '?', '&', '=','<', '>', '(', ')', '*', '^', '!', '~', '-', '|', ';', '%'};

    private boolean loggedIn;
    private String userKey;
    private Context context;

    public Authenticator(Context context) {
        this.context = context;
        loggedIn = false;
    }

    void login(String username, String password){
        //TODO
        loggedIn = true;
    }

    void logout(){
        loggedIn = false;
    }

    void addItem(InventoryItem item){
        if (loggedIn){

            URL url;
            try {
                url = new URL("http://150.156.202.112:8000/inventory");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("PUT");

                Gson gson = new GsonBuilder().create();
                String json = gson.toJson(item);

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
        } else {
            Toast.makeText(context, "You must be logged in to perform that action.",
                    Toast.LENGTH_LONG).show();
        }
    }

    ArrayList<InventoryItem> getItems(){
        if (loggedIn){

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
        } else {
            Toast.makeText(context, "You must be logged in to perform that action.",
                    Toast.LENGTH_LONG).show();
        }
        return null;
    }

    static String inputStreamToString(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder result  = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            result.append(line);
        }
        return result.toString();
    }
}
