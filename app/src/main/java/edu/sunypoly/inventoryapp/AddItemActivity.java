package edu.sunypoly.inventoryapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AddItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
    }

    void onAdd(View view) {
        TextView barcode = findViewById(R.id.barcode_display);
        TextView qrCode = findViewById(R.id.qr_display);
        EditText name = findViewById(R.id.editText);
        EditText type = findViewById(R.id.editText2);
        EditText serial = findViewById(R.id.editText3);
        EditText room = findViewById(R.id.editText4);
        EditText brand = findViewById(R.id.editText5);
        EditText acquired = findViewById(R.id.editText6);

        TextView message = findViewById(R.id.message);


        if (name.getText().toString().length() > 0) {
            InventoryItem item = new InventoryItem();
            item.id = generateId();
            try {
                item.barcode = Integer.valueOf(barcode.getText().toString());
            } catch (NumberFormatException e) {
                item.barcode = -1;
            }
            item.qr = qrCode.getText().toString();
            item.name = name.getText().toString();
            item.type = type.getText().toString();
            try {
                item.serial = Integer.valueOf(serial.getText().toString());
            } catch (NumberFormatException e){
                item.serial = -1;
            }
            item.room = room.getText().toString();
            item.brand = brand.getText().toString();
            item.acquired = acquired.getText().toString();

            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();

            String output = gson.toJson(item);

            Log.v("AddItemActivity", output);

            message.setText(output);
        } else {
            Toast.makeText(this, "Item must at least have a name", Toast.LENGTH_LONG)
                    .show();
        }
    }

    /**
     * Should check database for id values and return max(id) + 1
     *
     * @return unused id for use in inventory database
     */
    private int generateId() {
        return -1;
    }
}
