package edu.sunypoly.inventoryapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.util.HashMap;

/**
 * Lists all the fields of an InventoryItem
 */
public class TellMeMoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tell_me_more);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            InventoryItem item = null;
            try {
                item = InventoryItem.fromByteArray(extras.getByteArray("item"));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            TextView textView = findViewById(R.id.root_view);
            assert item != null;
            HashMap<String, String> hashMap = item.getFields();
            StringBuilder stringBuilder = new StringBuilder();
            for (String key: hashMap.keySet()){
                stringBuilder.append(key.charAt(0));
                stringBuilder.append(key.substring(1).toLowerCase());
                stringBuilder.append(": ");
                stringBuilder.append(hashMap.get(key));
                stringBuilder.append("\n");
            }
            textView.setText(stringBuilder.toString());
        }
    }
}
