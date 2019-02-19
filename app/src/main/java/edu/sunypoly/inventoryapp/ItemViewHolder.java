package edu.sunypoly.inventoryapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inventoryItem != null) {
                    itemView.getContext().startActivity(new Intent(itemView.getContext(),
                            AddItemActivity.class));
                } else {
                    Toast.makeText(itemView.getContext(), "Can't edit, item is null.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
