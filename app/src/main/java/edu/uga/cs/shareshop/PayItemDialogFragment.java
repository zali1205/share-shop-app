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

public class PayItemDialogFragment extends DialogFragment {

    private TextView questionTextField;
    private EditText editTextPrice;
    private Item item;
    private String userEmail;

    public interface PayItemDialogListener {
        void onFinishNewJobDialog();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        item = (Item) getArguments().getSerializable("Item");

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.pay_item_dialog, (ViewGroup) getActivity().findViewById(R.id.root));

        questionTextField = layout.findViewById(R.id.questionTextView);
        editTextPrice = layout.findViewById(R.id.editTextPrice);
        questionTextField.setText("What did you pay for the " + item.getName() + " ?");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(layout);
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton(android.R.string.ok, new ButtonClickListener());
        return builder.create();

    }

    private class ButtonClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            double price = Double.parseDouble(editTextPrice.getText().toString());
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                userEmail = user.getEmail();
            } else {
                userEmail = "NaN";
            }
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference();
            Query editQuery = myRef.child("Items").orderByChild("name").equalTo(item.getName());
            editQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot postSnapShot : snapshot.getChildren()) {
                        postSnapShot.getRef().child("isPurchased").setValue(true);
                        postSnapShot.getRef().child("purchaser").setValue(userEmail);
                        postSnapShot.getRef().child("price").setValue(price);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            PayItemDialogListener listener = (PayItemDialogListener) getActivity();
            listener.onFinishNewJobDialog();
            dismiss();
        }
    }



}