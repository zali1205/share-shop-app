package edu.uga.cs.shareshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * This is an Activity that allows the user to edit an un-purchased Item. It allows them to change the name (does not
 * allow duplicates), the description/detail, and the priority. It will prefill all of the appropriate fields with the data
 * of the Item.
 */
public class EditItemActivity extends AppCompatActivity {

    // Private variables
    private EditText editTextName;
    private EditText editTextDescription;
    private RadioGroup priorityRadioGroup;
    private Button editButton;
    private Item item;

    /*
        onCreate method that is called when the Activity is created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        // Getting the Item from the intent so that we know which Item is supposed to be edited.
        Intent intent = getIntent();
        item = (Item) intent.getSerializableExtra("Item");
        Log.d("EditItemActivity", "Item Name to edit is = " + item);

        // Setting all of the appropriate Views by getting the IDs from findViewById method.
        // Also prefilling the Views with the data from the Item.
        editTextName = findViewById(R.id.editTextName2);
        editTextName.setText(item.getName());
        editTextDescription = findViewById(R.id.editTextDescription2);
        editTextDescription.setText(item.getDetail());
        priorityRadioGroup = findViewById(R.id.radioGroup2);
        // Preselecting the specific Radio Button depending on the Item's priority.
        if (item.getPriority().equals("Please")) {
            priorityRadioGroup.check(R.id.radioButton4);
        } else if (item.getPriority().equals("Wanted")) {
            priorityRadioGroup.check(R.id.radioButton5);
        } else {
            priorityRadioGroup.check(R.id.radioButton6);
        } // if
        editButton = findViewById(R.id.editButton);
        // Setting the editButton's onClickListener.
        editButton.setOnClickListener(new EditButtonOnClickListener());
    } // onCreate

    /*
        Private class that implements the onClickListener for the Edit Button. Gets the information from the Views, finds
        the specific Item in the database, and updates the Item's values accordingly.
     */
    private class EditButtonOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // Getting the name
            String search = item.getName();
            // Getting Firebase information
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference();
            // Making the query to find the Item in the database.
            Query editQuery = myRef.child("Items").orderByChild("name").equalTo(search);
            // Searching for the Item in the database.
            editQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // Found the item in the Database.
                    for (DataSnapshot postSnapShot : snapshot.getChildren()) {
                        // Updating the Item's name, detail, and priority.
                        postSnapShot.getRef().child("name").setValue(editTextName.getText().toString());
                        postSnapShot.getRef().child("detail").setValue(editTextDescription.getText().toString());
                        RadioButton selected = findViewById(priorityRadioGroup.getCheckedRadioButtonId());
                        postSnapShot.getRef().child("priority").setValue(selected.getText().toString());
                        Log.d("EditItemActivity", "Item has been edited.");
                    } // for
                } // onDataChange
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    System.out.println("The read failed: " + error.getMessage());
                } // onCancelled
            });
            // Making a Toast to tell the user that the Item has been updated.
            Toast.makeText(getApplicationContext(), "Item has been updated!", Toast.LENGTH_SHORT).show();
            // Going back to the View Current List Activity class.
            Intent viewCurrentListActivity = new Intent(v.getContext(), ViewCurrentListActivity.class);
            startActivity(viewCurrentListActivity);
        } // onClick
    } // EditButtonOnClickListener
} // EditItemActivity