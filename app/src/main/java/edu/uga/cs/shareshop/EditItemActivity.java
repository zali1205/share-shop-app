package edu.uga.cs.shareshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class EditItemActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextDescription;
    private RadioGroup priorityRadioGroup;
    private Button editButton;
    private List<Item> itemsList;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private String itemName;
    private Item item = new Item();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Items");

        Intent intent = getIntent();
        String itemName = intent.getStringExtra("Item");
        Log.d("EditItemActivity", "Item Name to edit is = " + itemName);

        Query searchQuery = myRef.child("Items").orderByChild("name").equalTo(itemName);
        searchQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapShot : snapshot.getChildren()) {
                    item = postSnapShot.getValue(Item.class);
                    Log.d("EditItemActivity", "Found Item from Firebase = " + item.getName());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed: " + error.getMessage());
            }
        });

        editTextName = findViewById(R.id.editTextName2);
        editTextName.setText(item.getName());
        editTextDescription = findViewById(R.id.editTextDescription2);
        editTextDescription.setText(item.getDetail());
        priorityRadioGroup = findViewById(R.id.radioGroup2);
        if (item.getPriority().equals("Please")) {
            priorityRadioGroup.check(R.id.radioButton4);
        } else if (item.getPriority().equals("Wanted")) {
            priorityRadioGroup.check(R.id.radioButton5);
        } else {
            priorityRadioGroup.check(R.id.radioButton6);
        }
        editButton = findViewById(R.id.editButton);
        editButton.setOnClickListener(new EditButtonOnClickListener());



    }

    private class EditButtonOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

        }
    }
}