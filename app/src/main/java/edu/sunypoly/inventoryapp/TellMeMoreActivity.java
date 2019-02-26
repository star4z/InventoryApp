package edu.sunypoly.inventoryapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class TellMeMoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tell_me_more);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            TextView textView = findViewById(R.id.root_view);
            textView.setText("test value");
        }
    }
}
