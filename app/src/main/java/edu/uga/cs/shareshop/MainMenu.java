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

/**
 * This is an Activity that acts as the Main Menu allows the user to navigate to different Activities. The user from
 * here can navigate to View Un-purchased Items List, Add New Item, Settle Up, Sign Out, and View Purchased Items List.
 */
public class MainMenu extends AppCompatActivity {

    // Private variables
    private Button viewListButton;
    private Button viewPurchasedListButton;
    private Button addNewItemButton;
    private Button settleUpButton;
    private Button signOutButton;
    private TextView welcomeTextView;
    private String userEmail;

    /*
        onCreate method that is called when the Activity is called.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        // Getting the View Objects
        welcomeTextView = findViewById(R.id.welcomeTextView);
        viewListButton = findViewById(R.id.viewListButton);
        viewPurchasedListButton = findViewById(R.id.viewPurchasedListButton);
        addNewItemButton = findViewById(R.id.addNewItemButton);
        settleUpButton = findViewById(R.id.settleUpButton);
        signOutButton = findViewById(R.id.signOutButton);

        // Setting the onClickListener for viewListButton.
        viewListButton.setOnClickListener(new ViewListButtonListener());
        // Setting the onClickListener for viewPurchasedListButton.
        viewPurchasedListButton.setOnClickListener(new ViewPurchasedListButtonListener());
        // Setting the onClickListener for addNewItemButton.
        addNewItemButton.setOnClickListener(new AddNewItemButtonListener());
        // Setting the onClickListener for settleUpButton.
        settleUpButton.setOnClickListener(new SettleUpButtonListener());
        // Setting the onClickListener for signOutButton.
        signOutButton.setOnClickListener(new SignOutButtonListener());

        // Getting the current logged in user's email.
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userEmail = user.getEmail();
        } else {
            userEmail = "NaN";
        } // if
        // Edits the welcomeTextView to welcome the user with their email.
        welcomeTextView.setText("Welcome " + userEmail + "!");
    } // onCreate

    /*
        Private class that implements the onClickListener for the View List Button. Takes the user to the
        ViewCurrentListActivity.
     */
    private class ViewListButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent mainActivityIntent = new Intent(v.getContext(), ViewCurrentListActivity.class);
            startActivity(mainActivityIntent);
        } // onClick
    } // ViewListButtonListener

    /*
        Private class that implements the onClickListener for the View Purchased List Button. Takes the user to the
        ViewPurchasedListActivity.
     */
    private class ViewPurchasedListButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent mainActivityIntent = new Intent(v.getContext(), ViewPurchasedListActivity.class);
            startActivity(mainActivityIntent);
        } // onClick
    } // ViewPurchasedListButtonListener

    /*
        Private class that implements the onClickListener for the Add New Item Button. Takes the user to the AddNewItemActivity.
     */
    private class AddNewItemButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent mainActivityIntent = new Intent(v.getContext(), AddNewItemActivity.class);
            startActivity(mainActivityIntent);
        } // onClick
    } // AddNewItemButtonListener

    /*
        Private class that implements the onClickListener for the Settle Up Button. Takes the user to the SettleUpActivity.
     */
    private class SettleUpButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent mainActivityIntent = new Intent(v.getContext(), SettleUpActivity.class);
            startActivity(mainActivityIntent);
        } // onClick
    } // SettleUpButtonListener

    /*
        Private class that implements the onClickListener for the Sign Out Button. Logs the user out and takes them to the
        MainActivity.
     */
    private class SignOutButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            AuthUI.getInstance().signOut(getApplicationContext());
            Intent mainActivityIntent = new Intent(v.getContext(), MainActivity.class);
            startActivity(mainActivityIntent);
        } // onClick
    } // SignOutButtonListener
} // MainMenu
