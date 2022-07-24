package com.example.combinedproject;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.combinedproject.Data.DatabaseHandler;

//the activity for signing up
public class SignupActivity extends AppCompatActivity {
    private static DatabaseHandler myDB;

    //creating the activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        myDB = new DatabaseHandler(getBaseContext());

        TextView username =(TextView) findViewById(R.id.signupUsernameET);
        TextView password =(TextView) findViewById(R.id.signupPasswordET);
        TextView name =(TextView) findViewById(R.id.signupNameET);
        Button signUp = (Button) findViewById(R.id.confirmSignupBT);

        //click listener for comleating the sign up
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //checking if all the fields were filled
                if(username.getText().toString().equals("") || password.getText().toString().equals("") || name.getText().toString().equals("")) {
                    Toast.makeText(getBaseContext(), "Please fill all the fields.",
                            Toast.LENGTH_LONG).show();
                }

                else {
                    //creating the account and adding to the DB
                    boolean result = myDB.createAccount(username.getText().toString(), password.getText().toString(), name.getText().toString(), "regular", "regular");

                    //if the user wasn't in the DB
                    if(result) {
                        Toast.makeText(getBaseContext(), "Account created succesfully.",
                                Toast.LENGTH_LONG).show();
                        finish();
                    }

                    //if the user was in the DB
                    else {
                        Toast.makeText(getBaseContext(), "Username already exists, please try again.",
                                Toast.LENGTH_LONG).show();

                        username.setText("");
                        password.setText("");
                        name.setText("");
                    }

                }
            }
        });
    }
}