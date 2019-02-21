package edu.sunypoly.inventoryapp;

import android.os.AsyncTask;

import java.util.ArrayList;

public class ReadFilesTask extends AsyncTask<Authenticator, Void, ArrayList<InventoryItem>> {

    @Override
    protected ArrayList<InventoryItem> doInBackground(Authenticator... authenticators) {

        return authenticators[0].getItems();
    }
}
