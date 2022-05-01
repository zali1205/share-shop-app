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

public class AddNewItemActivity extends AppCompatActivity {


    private EditText editTextName;
    private EditText editTextDescription;
    private RadioGroup priorityRadioGroup;
    private Button addButton;
    private List<Item> itemsList;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_item);
        editTextName = findViewById(R.id.editTextName);
        editTextDescription = findViewById(R.id.editTextDescription);
        addButton = findViewById(R.id.addButton);
        priorityRadioGroup = findViewById(R.id.radioGroup);
        addButton.setOnClickListener(new AddButtonClickListener());
        itemsList = new ArrayList<Item>();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Items");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapShot: snapshot.getChildren() ) {
                    Item item = postSnapShot.getValue(Item.class);
                    itemsList.add(item);
                    Log.d("AddNewItemActivity", "added: " + item);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "The read failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private class AddButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (editTextName.getText().equals("") || priorityRadioGroup.getCheckedRadioButtonId() == -1) {
                Toast.makeText(getApplicationContext(), "Please make sure a name is entered and a priority level is checked.", Toast.LENGTH_SHORT).show();
                return;
            } else {
                String name = editTextName.getText().toString();

                for (Item i : itemsList) {
                    if (name.equalsIgnoreCase(i.getName())) {
                        Toast.makeText(getApplicationContext(), "This item already exists. Please enter a unique item.", Toast.LENGTH_SHORT).show();
                        editTextName.setText("");
                        editTextDescription.setText("");
                        priorityRadioGroup.clearCheck();
                        return;
                    }
                }
                String detail = editTextDescription.getText().toString();
                int selectedId = priorityRadioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = findViewById(selectedId);
                String priority = radioButton.getText().toString();
                final Item item = new Item(name, priority, detail);
                itemsList.add(item);

                myRef.push().setValue(item).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(), "Item created for " + item.getName(), Toast.LENGTH_SHORT).show();
                        editTextName.setText("");
                        editTextDescription.setText("");
                        priorityRadioGroup.clearCheck();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed to create Item for " + item.getName(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

    }
}