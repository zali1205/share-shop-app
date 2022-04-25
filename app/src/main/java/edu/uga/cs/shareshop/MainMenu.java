package edu.uga.cs.shareshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class MainMenu extends AppCompatActivity {

    private Button signOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        signOutButton = findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(new SignOutButtonListener());
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
