package edu.sunypoly.inventoryapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class AddItemActivity extends AppCompatActivity {

    TextView barcodeView;
    TextView qrCodeView;
    EditText nameView;
    EditText typeView;
    EditText serialView;
    EditText roomView;
    EditText brandView;
    EditText acquiredView;

    private Authenticator authenticator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        barcodeView = findViewById(R.id.barcode_display);
        qrCodeView = findViewById(R.id.qr_display);
        nameView = findViewById(R.id.editText);
        typeView = findViewById(R.id.editText2);
        serialView = findViewById(R.id.editText3);
        roomView = findViewById(R.id.editText4);
        brandView = findViewById(R.id.editText5);
        acquiredView = findViewById(R.id.editText6);

        if (getIntent() != null) {


            Bundle extras = getIntent().getExtras();
            if (extras != null) {

                Log.v(getClass().getSimpleName(), Objects.requireNonNull(getIntent().getExtras()).toString());

                barcodeView.setText(Integer.toString(extras.getInt(InventoryItem.BARCODE)));
                qrCodeView.setText(extras.getString(InventoryItem.QR));
                nameView.setText(extras.getString(InventoryItem.NAME));
                typeView.setText(extras.getString(InventoryItem.TYPE));
                serialView.setText(Integer.toString(extras.getInt(InventoryItem.SERIAL)));
                roomView.setText(extras.getString(InventoryItem.ROOM));
                brandView.setText(extras.getString(InventoryItem.BRAND));
                acquiredView.setText(extras.getString(InventoryItem.ACQUIRED));
            }
        }

        authenticator = Authenticator.getInstance();
    }

    public void onAdd(View view) {

        ProgressDialog dialog = ProgressDialog.show(this, "Contacting server...", "Adding item");

        if (!(nameView.getText().toString().length() > 0)) {
            Toast.makeText(this, "Item must at least have a name", Toast.LENGTH_LONG)
                    .show();
            return;
        }

        String barcodeStr = barcodeView.getText().toString();
        String qr = qrCodeView.getText().toString();
        String name = nameView.getText().toString();
        String type = typeView.getText().toString();
        String serialStr = serialView.getText().toString();
        String room = roomView.getText().toString();
        String brand = brandView.getText().toString();
        String acquired = acquiredView.getText().toString();

        int id = -1; //Sending an item with an id of -1 to the database will prompt it to generate a
        // new one.
        int barcode = -1;
        int serial = -1;
        try {
            if (!barcodeStr.isEmpty())
                barcode = Integer.valueOf(barcodeStr);
            if (!serialStr.isEmpty())
                serial = Integer.valueOf(serialStr);
        } catch (NumberFormatException e) {
            dialog.dismiss();
            Toast.makeText(this, "Encountered a number field with a string in it.",
                    Toast.LENGTH_SHORT).show();
        }

        InventoryItem item = new InventoryItem(id, barcode, qr, name, type, serial, room, brand,
                acquired);

        if (containsIllegalCharacter(item)) {
            dialog.dismiss();
            return;
        }

        authenticator.login("", "");

        boolean success = authenticator.addItem(item);

        dialog.dismiss();

        if (success) {
            Toast.makeText(this, "Added item.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Could not add item.", Toast.LENGTH_LONG).show();
        }
    }

    boolean containsIllegalCharacter(InventoryItem item) {
        HashMap<String, String> map = item.getFields();

        for (String key : map.keySet()) {
            if (!key.equals(InventoryItem.ID) && !key.equals(InventoryItem.BARCODE) &&
                    !key.equals(InventoryItem.SERIAL)) {
                for (char c : Objects.requireNonNull(map.get(key)).toCharArray()) {
                    for (char d : Authenticator.ILLEGAL_CHARACTERS) {
                        if (c == d) {
                            Toast.makeText(this, "Field " + key + " cannot contain " +
                                    d, Toast.LENGTH_LONG).show();
                            Log.v(getClass().getSimpleName(), "Field " + key + " cannot contain " +
                                    d + "");
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }
}
