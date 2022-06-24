package com.example.combinedproject.Information;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContentInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.SearchView;
import android.widget.Toast;


import com.example.combinedproject.R;
import com.example.combinedproject.Data.Information;
import com.example.combinedproject.Others.RowsInList;


import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener,
        SearchView.OnCloseListener{
    private SearchManager searchManager;
    private android.widget.SearchView searchView;
    private MenuItem searchItem;
    private MenuItem showOnMapItem;
    RowsInList rows;
    ExpandableListView expendables;
    Button ShowOnMap;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        // prepare views to be sent to 'rows'
        expendables = findViewById(R.id.exp_list_view);
        ShowOnMap = (Button) findViewById(R.id.Service_to_navigation);

        //the name of the user
        username = getIntent().getExtras().getString("username");
        rows = new RowsInList(getBaseContext(), getSupportFragmentManager(), username, expendables, ShowOnMap, searchManager);
         // set 'rows' into action
        rows.setOnServiceClickedListener(); // service


        //showOnMapItem = findViewById(R.id.showOnMapItem);
        //showOnMapItem.setEnabled(false);

        // Slide slide = new Slide(Gravity.LEFT);
        //this.getWindow().setEnterTransition(slide);
        // set the listener for when a child has been pressed

        ShowOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "pressed",
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.toMapItem)
        {
            //Intent intent = new Intent();
//
            ////intent.putExtra("editTextValue", rows.getSelectedItem().getName());
            //intent.putExtra("chosenItemLat", rows.getSelectedItem().getPosition().getLatitude());
            //intent.putExtra("chosenItemLng", rows.getSelectedItem().getPosition().getLongitude());
            //setResult(RESULT_OK, intent);
            //finish();


            finish();

            //startActivity(new Intent(getApplicationContext(), MapActivity.class));
            //overridePendingTransition(R.anim.left_slide_in, R.anim.right_slide_out);
            //overridePendingTransition(R.anim.right_slide, R.anim.left_slide);
        }

        else if(item.getItemId() == R.id.showOnMapItem) {
            Information selectedItem = rows.getSelectedItem();

            if(selectedItem != null) {
                Intent intent = new Intent();

                //intent.putExtra("editTextValue", rows.getSelectedItem().getName());
                intent.putExtra("chosenItemLat", rows.getSelectedItem().getPosition().getLatitude());
                intent.putExtra("chosenItemLng", rows.getSelectedItem().getPosition().getLongitude());
                setResult(RESULT_OK, intent);
                finish();
            }
            else {
                Toast.makeText(getBaseContext(), "Please select an item.",
                        Toast.LENGTH_LONG).show();
            }

        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.to_map_menu, menu);
        searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);
        searchView.requestFocus();
        return true;
    }

    @Override
    public boolean onClose() {
        rows.getAdapter().filterData("k");
        rows.expandAll();
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        rows.getAdapter().filterData(s);
        rows.expandAll();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        rows.getAdapter().filterData(s);
        rows.expandAll();
        return false;
    }
}