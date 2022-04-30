package edu.uga.cs.shareshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainMenu extends AppCompatActivity {

    private Button viewListButton;
    private Button addNewItemButton;
    private Button settleUpButton;
    private Button signOutButton;
    private TextView welcomeTextView;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        welcomeTextView = findViewById(R.id.welcomeTextView);
        viewListButton = findViewById(R.id.viewListButton);
        viewListButton.setOnClickListener(new ViewListButtonListener());
        addNewItemButton = findViewById(R.id.addNewItemButton);
        addNewItemButton.setOnClickListener(new AddNewItemButtonListener());
        settleUpButton = findViewById(R.id.settleUpButton);
        settleUpButton.setOnClickListener(new SettleUpButtonListener());
        signOutButton = findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(new SignOutButtonListener());
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            userEmail = user.getEmail();
        } else {
            userEmail = "NaN";
        }
        welcomeTextView.setText("Welcome " + userEmail + "!");
    }

    private class ViewListButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent mainActivityIntent = new Intent(v.getContext(), ViewCurrentListActivity.class);
            startActivity(mainActivityIntent);
        }
    }

    private class AddNewItemButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent mainActivityIntent = new Intent(v.getContext(), AddNewItemActivity.class);
            startActivity(mainActivityIntent);
        }
    }

    private class SettleUpButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent mainActivityIntent = new Intent(v.getContext(), SettleUpActivity.class);
            startActivity(mainActivityIntent);
        }
    }

    private class SignOutButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            AuthUI.getInstance().signOut(getApplicationContext());
            Intent mainActivityIntent = new Intent(v.getContext(), MainActivity.class);
            startActivity(mainActivityIntent);
        }
    }

}
