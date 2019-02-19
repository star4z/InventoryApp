package edu.sunypoly.inventoryapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void login(View view){
        EditText u = findViewById(R.id.username_field);
        EditText p = findViewById(R.id.password_field);

        String usr = u.getText().toString();
        String pwd = p.getText().toString();
    }
}
