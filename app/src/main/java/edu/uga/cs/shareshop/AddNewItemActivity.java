package edu.uga.cs.shareshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddNewItemActivity extends AppCompatActivity {


    private EditText editTextName;
    private RadioGroup priorityRadioGroup;
    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_item);
        editTextName = findViewById(R.id.editTextName);
        addButton = findViewById(R.id.addButton);
        priorityRadioGroup = findViewById(R.id.radioGroup);
        addButton.setOnClickListener(new AddButtonClickListener());
    }

    private class AddButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (editTextName.getText().equals("") || priorityRadioGroup.getCheckedRadioButtonId() == -1) {
                Toast.makeText(getApplicationContext(), "Please make sure a name is entered and a priority level is checked.", Toast.LENGTH_SHORT).show();
                return;
            } else {
                String name = editTextName.getText().toString();
                int selectedId = priorityRadioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = findViewById(selectedId);
                String priority = radioButton.getText().toString();
                final Item item = new Item(name, priority);

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Items");
                myRef.push().setValue(item).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(), "Item created for " + item.getName(), Toast.LENGTH_SHORT).show();
                        editTextName.setText("");
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