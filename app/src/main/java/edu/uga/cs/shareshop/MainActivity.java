package edu.uga.cs.shareshop;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;

/**
 * This is where the application starts and begins. This application requires the user to log in via Google Firebase
 * in order to continue. The Sign In Button also acts as a register if the user inputs an email that has not been
 * registered with the database before.
 */
public class MainActivity extends AppCompatActivity {

    // Private variables
    private Button signInButton;
    private Button registerButton;

    /*
        onCreate method that is called when the Activity is created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Getting the IDs for the buttons.
        signInButton = findViewById(R.id.button);
        registerButton = findViewById(R.id.button2);
        // Assigning the onClickListeners for the buttons.
        signInButton.setOnClickListener(new SignInButtonOnClickListener());
    }

    /*
        Private class that implements the onClickListener for the Sign In Button. Signs the users into the Firebase Database
        and registers the user if they do not have an email already in the database.
     */
    private class SignInButtonOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            List<AuthUI.IdpConfig> providers = Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build());
            Intent signInIntent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setIsSmartLockEnabled(false)
                    .setAvailableProviders(providers)
                    .build();
            signInLauncher.launch(signInIntent);
        }
    }

    /*
        ActivityResultLauncher class
        The signInLauncher variable is a launcher that allows us to start the AuthUI's logging in process that will
        return to the MainActivity when completed. When the logging in process is done, the overridden onActivityResult
        method will be called.
     */
    private ActivityResultLauncher<Intent> signInLauncher =
            registerForActivityResult(
                    new FirebaseAuthUIActivityResultContract(),
                    new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                        @Override
                        public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                            onSignInResult(result);
                        }
                    }
            );

    /*
        This method is called once the sign-in activity completes. Once logged in, the MainActivity transitions to the
        MainMenu Activity.
     */
    private void onSignInResult( FirebaseAuthUIAuthenticationResult result ) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            // after a successful sign in, start the job leads management activity
            Intent intent = new Intent( this, MainMenu.class );
            startActivity( intent );
        }
        else {
            // Sign in failed. If response is null the user canceled the
            Toast.makeText( getApplicationContext(),
                    "Sign in failed",
                    Toast.LENGTH_SHORT).show();
        }
    }

}