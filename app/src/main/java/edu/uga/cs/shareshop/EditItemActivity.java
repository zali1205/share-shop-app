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
 *
 */
public class EditItemActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextDescription;
    private RadioGroup priorityRadioGroup;
    private Button editButton;
    private List<Item> itemsList;
    private String itemName;
    private Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        Intent intent = getIntent();
        item = (Item) intent.getSerializableExtra("Item");
        Log.d("EditItemActivity", "Item Name to edit is = " + item);

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
            String search = item.getName();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference();
            Query editQuery = myRef.child("Items").orderByChild("name").equalTo(search);
            editQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot postSnapShot : snapshot.getChildren()) {
                        Toast.makeText(getApplicationContext(), "I AM HERE!",Toast.LENGTH_SHORT).show();
                        postSnapShot.getRef().child("name").setValue(editTextName.getText().toString());
                        postSnapShot.getRef().child("detail").setValue(editTextDescription.getText().toString());
                        RadioButton selected = findViewById(priorityRadioGroup.getCheckedRadioButtonId());
                        postSnapShot.getRef().child("priority").setValue(selected.getText().toString());
                        Log.d("EditItemActivity", "Item has been edited.");
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            Toast.makeText(getApplicationContext(), "Item has been updated!", Toast.LENGTH_SHORT).show();
            Intent viewCurrentListActivity = new Intent(v.getContext(), ViewCurrentListActivity.class);
            startActivity(viewCurrentListActivity);

        }
    }
}