package com.example.combinedproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

public class LoginActivity extends AppCompatActivity {

    private static DatabaseHandler myDB;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    ImageView googleBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        myDB = new DatabaseHandler(getBaseContext());

        TextView username =(TextView) findViewById(R.id.usernameET);
        TextView password =(TextView) findViewById(R.id.passwordET);

        Button login = (Button) findViewById(R.id.loginBT);
        //admin and admin

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getBaseContext(), username.getText().toString() + " " + password.getText().toString(),
                //        Toast.LENGTH_LONG).show();
                if(username.getText().toString().equals("") || password.getText().toString().equals("")) {
                    Toast.makeText(getBaseContext(), "Username or password missing.",
                            Toast.LENGTH_LONG).show();
                }

                else {
                    Pair<Boolean, Pair<String,String>> result = myDB.doesAccountExists(username.getText().toString(), password.getText().toString());
                    if(result.first) {
                            //Toast.makeText(getBaseContext(), "exists",
                            //        Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), com.example.combinedproject.MainActivity.class);
                            intent.putExtra("name", result.second.first);
                            intent.putExtra("user_type", result.second.second);
                            intent.putExtra("username", username.getText().toString());
                            startActivityForResult(intent, 1);
                    }
                    else {
                            Toast.makeText(getBaseContext(), "Wrong username or password.",
                                Toast.LENGTH_LONG).show();
                    }

                    username.setText("");
                    password.setText("");

                }
                //if(myDB.doesUserExists(username.getText().toString(), "admin", "regular")) {
                //    Toast.makeText(getBaseContext(), "exists",
                //            Toast.LENGTH_LONG).show();
                //}
                //else {
                //    Toast.makeText(getBaseContext(), "doesn't exist",
                //            Toast.LENGTH_LONG).show();
                //}
            }
        });

        //check if internet is available
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            Toast.makeText(getBaseContext(), "internet",
                    Toast.LENGTH_LONG).show();

        }
        else
            Toast.makeText(getBaseContext(), "no internet",
                    Toast.LENGTH_LONG).show();

        Button signup = (Button) findViewById(R.id.signupBT);
        //admin and admin

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getBaseContext(), username.getText().toString() + " " + password.getText().toString(),
                //        Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), com.example.combinedproject.SignupActivity.class);
                startActivityForResult(intent, 1);
            }
        });

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

    public void googleSignIn() {
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent,1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1000){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                task.getResult(ApiException.class);

                gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
                gsc = GoogleSignIn.getClient(this,gso);

                GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
                if(acct!=null) {
                    String googleName = acct.getDisplayName();
                    String googleEmail = acct.getEmail();

                    if(!myDB.doesUserExists(googleEmail, "regular")) {
                        boolean result = myDB.createAccount(googleEmail, "", googleName, "regular", "google");

                        if (result) {
                            Toast.makeText(getBaseContext(), "Account created succesfully.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    Intent intent = new Intent(getApplicationContext(), com.example.combinedproject.MainActivity.class);
                    intent.putExtra("name", googleName);
                    startActivityForResult(intent, 1);
                    //Toast.makeText(getBaseContext(), personName + " " + personEmail,
                    //        Toast.LENGTH_LONG).show();

                }

                //Intent intent = new Intent(getApplicationContext(), com.example.combinedproject.MainActivity.class);
                //startActivityForResult(intent, 1);
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), "Something went wrong with google sign in. Try checking your internet connection.", Toast.LENGTH_LONG).show();
            }
        }
    }
}