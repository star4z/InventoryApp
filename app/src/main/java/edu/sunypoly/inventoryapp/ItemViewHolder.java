package edu.sunypoly.inventoryapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.Serializable;

public class ItemViewHolder extends RecyclerView.ViewHolder {

    ConstraintLayout itemView;
    TextView itemTextView;
    ImageButton imageButton;
    InventoryItem inventoryItem = null;


    public ItemViewHolder(@NonNull final ConstraintLayout itemView) {
        super(itemView);
        this.itemView = itemView;
        itemTextView = itemView.findViewById(R.id.item_text_view);
        imageButton = itemView.findViewById(R.id.edit_button);

        itemTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inventoryItem != null) {
                    Bundle bundle = new Bundle();
                    try {
                        bundle.putByteArray("item", inventoryItem.toByteArray());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    Intent intent = new Intent(itemView.getContext(),
                            TellMeMoreActivity.class);
                    intent.putExtras(bundle);

                    itemView.getContext().startActivity(intent);
                } else {
                    Toast.makeText(itemView.getContext(), "Can't open, item is null.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inventoryItem != null) {
                    Bundle bundle = inventoryItem.toBundle();

                    Log.d("ViewHolder", bundle.toString());

                    Intent intent = new Intent(itemView.getContext(),
                            AddItemActivity.class);
                    intent.putExtras(bundle);

                    itemView.getContext().startActivity(intent);
                } else {
                    Toast.makeText(itemView.getContext(), "Can't edit, item is null.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
