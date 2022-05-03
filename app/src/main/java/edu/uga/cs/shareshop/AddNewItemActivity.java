package edu.uga.cs.shareshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * This is an Activity that allows the user to add a new Item to their list. The user can enter the name, detail/description, and
 * select the priority.
 */
public class AddNewItemActivity extends AppCompatActivity {

    // Private variables
    private EditText editTextName;
    private EditText editTextDescription;
    private RadioGroup priorityRadioGroup;
    private Button addButton;
    private List<Item> itemsList;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    /*
        onCreate method that is called when the Activity is created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_item);
        // Getting the Views
        editTextName = findViewById(R.id.editTextName);
        editTextDescription = findViewById(R.id.editTextDescription);
        addButton = findViewById(R.id.addButton);
        priorityRadioGroup = findViewById(R.id.radioGroup);
        // Setting the onClickListener for the addButton.
        addButton.setOnClickListener(new AddButtonClickListener());
        // List that holds the items.
        itemsList = new ArrayList<Item>();

        // Getting Firebase information
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Items");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Getting all of the items
                for (DataSnapshot postSnapShot: snapshot.getChildren() ) {
                    Item item = postSnapShot.getValue(Item.class);
                    // Adding to the list
                    itemsList.add(item);
                    Log.d("AddNewItemActivity", "added: " + item);
                } // for
            } // onDataChange
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed: " + error.getMessage());
            } // onCancelled
        });
    } // onCreate

    /*
        Private class that implements the onClickListener for the Add Button. Gets the information from the Views and adds
        the Item to the database.
     */
    private class AddButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // Checking to ensure that the name has been filled as well as a priority has been checked. Warns the user if not.
            if (editTextName.getText().equals("") || priorityRadioGroup.getCheckedRadioButtonId() == -1) {
                Toast.makeText(getApplicationContext(), "Please make sure a name is entered and a priority level is checked.", Toast.LENGTH_SHORT).show();
                return;
            } else {
                // Getting the name from the editTextName view.
                String name = editTextName.getText().toString();
                // Going through the list of items already in the database and looking for duplicates.
                for (Item i : itemsList) {
                    // If duplicate exists, warns the user, and resets the input fields.
                    if (name.equalsIgnoreCase(i.getName())) {
                        Toast.makeText(getApplicationContext(), "This item already exists. Please enter a unique item.", Toast.LENGTH_SHORT).show();
                        // Resetting the input fields
                        editTextName.setText("");
                        editTextDescription.setText("");
                        priorityRadioGroup.clearCheck();
                        return;
                    } // if
                } // for
                // Otherwise, if a duplicate does not exist, gets the information from the rest of the input fields.
                String detail = editTextDescription.getText().toString();
                int selectedId = priorityRadioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = findViewById(selectedId);
                String priority = radioButton.getText().toString();
                final Item item = new Item(name, priority, detail);
                // Adds the item to the list.
                itemsList.add(item);
                // Pushing the value into the database
                myRef.push().setValue(item).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        // Alerts the user that the item has been created.
                        Toast.makeText(getApplicationContext(), "Added " + item.getName() + " to the list!", Toast.LENGTH_SHORT).show();
                        // Resetting the input fields
                        editTextName.setText("");
                        editTextDescription.setText("");
                        priorityRadioGroup.clearCheck();
                    } //onSuccess
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed to create Item for " + item.getName(), Toast.LENGTH_SHORT).show();
                    } //onFailure
                });
            } // if-else
        } // onClick
    } // AddButtonClickListener
} // AddNewItemActivity