package edu.uga.cs.shareshop;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * A Dialog Fragment class that handles entering or editing the price of the Item. If the Item has not already been purchased,
 * the Item will be marked as purchased.
 */
public class PayItemDialogFragment extends DialogFragment {

    // Private variables
    private TextView questionTextField;
    private EditText editTextPrice;
    private Item item;
    private String userEmail;
    private int position;

    /*
        This interface will be used to update and remove the Item from the un-purchased list.
     */
    public interface PayItemDialogListener {
        void onFinishNewJobDialog(int position);
    } // PayItemDialogListener interface

    /*
        onCreateDialog method that is called when the Dialog is being created.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Getting the Item and position of the Item from the un-purchased list.
        item = (Item) getArguments().getSerializable("Item");
        position = getArguments().getInt("Position");
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.pay_item_dialog, (ViewGroup) getActivity().findViewById(R.id.root));

        // Getting the View objects
        questionTextField = layout.findViewById(R.id.questionTextView);
        editTextPrice = layout.findViewById(R.id.editTextPrice);
        questionTextField.setText("What did you pay for the " + item.getName() + " ?");
        // Create a new AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set its view (inflated above)
        builder.setView(layout);
        // Implementing the negative button listener
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            } // onClick
        });
        // Implementing the positive button listener
        builder.setPositiveButton(android.R.string.ok, new ButtonClickListener());
        return builder.create();
    } // onCreateDialog

    private class ButtonClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            // Getting the price from the editTextPrice
            double price = Double.parseDouble(editTextPrice.getText().toString());
            // Getting the user's email
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                userEmail = user.getEmail();
            } else {
                userEmail = "NaN";
            } // if
            // Getting the Firebase information
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference();
            // Creating a query to find the Item to update
            Query editQuery = myRef.child("Items").orderByChild("name").equalTo(item.getName());
            editQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // Getting the Item
                    for (DataSnapshot postSnapShot : snapshot.getChildren()) {
                        // Updating the Item's value
                        postSnapShot.getRef().child("purchased").setValue(true);
                        postSnapShot.getRef().child("purchaser").setValue(userEmail);
                        postSnapShot.getRef().child("price").setValue(price);
                    } // for
                } // onDataChange
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    System.out.println("The read failed: " + error.getMessage());
                } // onCancelled
            });
            PayItemDialogListener listener = (PayItemDialogListener) getActivity();
            listener.onFinishNewJobDialog(position);
            dismiss();
        } // onClick
    } // ButtonClickListener
} // PayItemDialogFragment