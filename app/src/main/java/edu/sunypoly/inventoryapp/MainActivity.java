package edu.sunypoly.inventoryapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    SharedPreferences prefs;
    String username;
    String password;

    boolean loggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        username = prefs.getString("username", null);
        password = prefs.getString("password", null);
        loggedIn = false;
    }

    private void loginToServer() throws IOException {
        URL url = new URL("http://150.156.202.112/inventory");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    }


    public void login(View vew){
        startActivityForResult(new Intent(this, LoginActivity.class), 0);
    }

    public void listAll(View view){
        startActivity(new Intent(this, ListAllItemsActivity.class));
    }

    public void search(View view){

    }

    public void addItem(View view){
        startActivity(new Intent(this, AddItemActivity.class));
    }

}
