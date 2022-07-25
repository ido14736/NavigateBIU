package com.example.combinedproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.combinedproject.Navigation.BIUNavigationActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

//the main menu activity
public class MainActivity extends AppCompatActivity {

    Button nav, inf, shu, disconnect, admin;

    //creating the activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getting data from the previous activity
        String name = getIntent().getExtras().getString("name");
        TextView welcome = findViewById(R.id.welcomeET);
        welcome.setText("Welcome, " + name + "\n");

        String username = getIntent().getExtras().getString("username");
        String connType = getIntent().getExtras().getString("user_type");

        //click listener for the admin options button
        admin = findViewById(R.id.adminOptionsBT);
        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdminOptionsActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        //if the connected user isn't admin - hiding the admin options button
        if(connType.equals("admin")) {
            admin.setVisibility(View.VISIBLE);

        }

        nav = findViewById(R.id.navigationButton);
        inf = findViewById(R.id.informationButton);
        shu = findViewById(R.id.shuttlesButton);

        //click listener for the navigation button
        nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BIUNavigationActivity.class);
                intent.putExtra("username", username);
                startActivityForResult(intent, 1);
            }
        });

        //click listener for the information button
        inf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), com.example.combinedproject.Information.MapActivity.class);
                intent.putExtra("username", username);
                startActivityForResult(intent, 1);
            }
        });

        //click listener for the shuttles options button
        shu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), com.example.combinedproject.Shuttles.ShuttlesActivity.class);
                intent.putExtra("username", username);
                startActivityForResult(intent, 1);
            }
        });

        //click listener for the sign out button - closing the current activity and returning to the log in
        //if a user that connected with google signed out - disconnecting from it's account
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        GoogleSignInClient gsc = GoogleSignIn.getClient(this,gso);
        disconnect = findViewById(R.id.disconnectBT);
        disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        finish();
                    }
                });
            }
        });
    }
}