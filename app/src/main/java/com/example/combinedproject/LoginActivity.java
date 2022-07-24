package com.example.combinedproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.combinedproject.Data.DatabaseHandler;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

//the login activity
public class LoginActivity extends AppCompatActivity {
    private static DatabaseHandler myDB;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    ImageView googleBtn;

    //creating the activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        myDB = new DatabaseHandler(getBaseContext());

        TextView username =(TextView) findViewById(R.id.usernameET);
        TextView password =(TextView) findViewById(R.id.passwordET);

        Button login = (Button) findViewById(R.id.loginBT);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if the user didn't enter a username or a password
                if(username.getText().toString().equals("") || password.getText().toString().equals("")) {
                    Toast.makeText(getBaseContext(), "Username or password missing.",
                            Toast.LENGTH_LONG).show();
                }

                else {
                    //checking if the entered username and password exists
                    Pair<Boolean, Pair<String,String>> result = myDB.doesAccountExists(username.getText().toString(), password.getText().toString());

                    //if exists - starting the main menu
                    if(result.first) {
                            Intent intent = new Intent(getApplicationContext(), com.example.combinedproject.MainActivity.class);
                            intent.putExtra("name", result.second.first);
                            intent.putExtra("user_type", result.second.second);
                            intent.putExtra("username", username.getText().toString());
                            startActivityForResult(intent, 1);
                    }

                    //if doesn't exist
                    else {
                            Toast.makeText(getBaseContext(), "Wrong username or password.",
                                Toast.LENGTH_LONG).show();
                    }

                    username.setText("");
                    password.setText("");

                }
            }
        });

        Button signup = (Button) findViewById(R.id.signupBT);

        //if the user chose to sign up - starting the sign up activity
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), com.example.combinedproject.SignupActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        //handling signing up with google
        googleBtn = findViewById(R.id.googleSignIn);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);

        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignIn();
            }
        });
    }

    //sending a request to sign in
    public void googleSignIn() {
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent,1000);
    }

    //signing in with an google account
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //a request to sign in
        if(requestCode == 1000){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                task.getResult(ApiException.class);

                //configure sign in to request the google account
                gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

                //build a GoogleSignInClient with the options specified by gso
                gsc = GoogleSignIn.getClient(this,gso);

                //checking for existing google account
                //if the user signed in before - acct will be != null
                GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
                if(acct!=null) {
                    //get data from the account
                    String googleName = acct.getDisplayName();
                    String googleEmail = acct.getEmail();

                    //checking if user doesn't exist in the DB
                    if(!myDB.doesUserExists(googleEmail, "google")) {
                        //adding the account to the DB
                        boolean result = myDB.createAccount(googleEmail, "", googleName, "regular", "google");

                        if (result) {
                            Toast.makeText(getBaseContext(), "Account created succesfully.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    //starting the main menu after signing in
                    Intent intent = new Intent(getApplicationContext(), com.example.combinedproject.MainActivity.class);
                    intent.putExtra("name", googleName);
                    intent.putExtra("user_type", "regular");
                    intent.putExtra("username", googleEmail);
                    startActivityForResult(intent, 1);
                }

            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), "Something went wrong with google sign in. Try checking your internet connection.", Toast.LENGTH_LONG).show();
            }
        }
    }
}