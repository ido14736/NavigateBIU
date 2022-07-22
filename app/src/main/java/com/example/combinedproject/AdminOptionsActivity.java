package com.example.combinedproject;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.combinedproject.Data.InformationHandler;
import java.util.Arrays;

//the admin options menu activity
public class AdminOptionsActivity extends AppCompatActivity {
    private String[] adminOptions;
    AutoCompleteTextView et1, et2;
    EditText et3, et4, et5;
    Button confirm;
    Spinner spinner;
    ArrayAdapter<String> typesAdapter, fieldsAdapter, namesAdapter;

    //creating the activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_options);

        //a list with the options of an admin user
        adminOptions = new String[] {"Add Service", "Edit Service", "Remove Service"};

        et1 = findViewById(R.id.firstET);
        et2 = findViewById(R.id.secondET);
        et3 = findViewById(R.id.thirdET);
        et4 = findViewById(R.id.fourthET);
        et5 = findViewById(R.id.fifthET);

        //making sure that the DB is initialized
        boolean addedSuccesfully = InformationHandler.initializeInformation(getBaseContext());
        if(!addedSuccesfully){
            //failed to add
            Toast.makeText(getBaseContext(), "Error While Initializing the DB.",
                    Toast.LENGTH_LONG).show();
        }

        //creating the adapters for the spinners
        typesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, InformationHandler.getEnglish_types());
        fieldsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, InformationHandler.getFields());
        namesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, InformationHandler.getNamesList());

        //creating the main spinner for the admin options
        spinner = findViewById(R.id.adminOptionsSP);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, adminOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //when selecting an option from the spinner - setting the activity with the matching fields to the chosen option
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //the adding option chosen
                if(spinner.getSelectedItem().toString().equals(adminOptions[0])) {
                    et1.setVisibility(View.VISIBLE);
                    et1.setText("");
                    et1.setHint("Service Name");
                    et1.setAdapter(null);
                    et2.setVisibility(View.VISIBLE);
                    et2.setText("");
                    et2.setHint("Service Type");
                    et2.setAdapter(typesAdapter);
                    et3.setVisibility(View.VISIBLE);
                    et3.setText("");
                    et3.setHint("Service Position Latitude");
                    et4.setVisibility(View.VISIBLE);
                    et4.setText("");
                    et4.setHint("Service Position Longitude");
                    et5.setVisibility(View.VISIBLE);
                    et5.setText("");
                    et5.setHint("Service Description");
                }

                //the editing option chosen
                else if(spinner.getSelectedItem().toString().equals(adminOptions[1])) {
                    et1.setVisibility(View.VISIBLE);
                    et1.setText("");
                    et1.setHint("Service Name");
                    et1.setAdapter(namesAdapter);
                    et2.setVisibility(View.VISIBLE);
                    et2.setText("");
                    et2.setHint("Field To Edit");
                    et2.setAdapter(fieldsAdapter);
                    et3.setVisibility(View.VISIBLE);
                    et3.setText("");
                    et3.setHint("New Field Value");
                    et4.setVisibility(View.INVISIBLE);
                    et5.setVisibility(View.INVISIBLE);
                }

                //the removing option chosen
                else if(spinner.getSelectedItem().toString().equals(adminOptions[2])) {
                    et1.setVisibility(View.VISIBLE);
                    et1.setText("");
                    et1.setHint("Service Name");
                    et1.setAdapter(namesAdapter);
                    et2.setVisibility(View.INVISIBLE);
                    et3.setVisibility(View.INVISIBLE);
                    et4.setVisibility(View.INVISIBLE);
                    et5.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        //preforming the chosen action with the values that the admin entered
        confirm = findViewById(R.id.adminConfirmBT);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean result = false;

                //preforms addition
                if(spinner.getSelectedItem().toString().equals(adminOptions[0])) {
                    //if the admin didn't fill all the fields
                    if(et1.getText().toString().equals("") || et2.getText().toString().equals("") || et3.getText().toString().equals("") || et4.getText().toString().equals("") || et5.getText().toString().equals("")) {
                        Toast.makeText(getBaseContext(), "Please fill all the fields.",
                                Toast.LENGTH_LONG).show();
                    }

                    //preforming the action
                    else {
                        if(Arrays.asList(InformationHandler.getEnglish_types()).contains(et2.getText().toString())) {
                            result = InformationHandler.addMarkerToDB(et1.getText().toString(), et2.getText().toString(), Double.parseDouble(et3.getText().toString()), Double.parseDouble(et4.getText().toString()), et5.getText().toString());
                        }
                        else {
                            Toast.makeText(getBaseContext(), "Invalid service type.",
                                    Toast.LENGTH_LONG).show();
                            result = true;
                        }
                    }
                }

                //preforms edit
                else if(spinner.getSelectedItem().toString().equals(adminOptions[1])) {
                    //if the admin didn't fill all the fields
                    if(et1.getText().toString().equals("") || et2.getText().toString().equals("") || et3.getText().toString().equals("")) {
                        Toast.makeText(getBaseContext(), "Please fill all the fields.",
                                Toast.LENGTH_LONG).show();
                    }

                    //preforming the action
                    else {
                        if(Arrays.asList(InformationHandler.getFields()).contains(et2.getText().toString())) {
                            result = InformationHandler.editMarkerInDB(et1.getText().toString(), et2.getText().toString(), et3.getText().toString());
                        }
                        else {
                            Toast.makeText(getBaseContext(), "Invalid field.",
                                    Toast.LENGTH_LONG).show();
                            result = true;
                        }
                    }

                }

                //preforms remove
                else if(spinner.getSelectedItem().toString().equals(adminOptions[2])) {
                    //if the admin didn't fill all the fields
                    if(et1.getText().toString().equals("")) {
                        Toast.makeText(getBaseContext(), "Please fill all the fields.",
                                Toast.LENGTH_LONG).show();
                    }

                    //preforming the action
                    else {
                        result = InformationHandler.removeMarkerFromDB(et1.getText().toString());
                    }
                }

                //if something went wrong while preformin the action
                if(!result) {
                    Toast.makeText(getBaseContext(), "Error occurred in action.",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getBaseContext(), "The action compleated successfully.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}