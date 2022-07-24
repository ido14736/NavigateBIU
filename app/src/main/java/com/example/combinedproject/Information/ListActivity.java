package com.example.combinedproject.Information;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.SearchView;
import android.widget.Toast;
import com.example.combinedproject.R;
import com.example.combinedproject.Data.Information;
import com.example.combinedproject.Others.RowsInList;

//the list information activity
public class ListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener,
        SearchView.OnCloseListener{
    private SearchManager searchManager;
    private android.widget.SearchView searchView;
    private MenuItem searchItem;
    RowsInList rows;
    ExpandableListView expendables;
    private String username;

    //creating the activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        expendables = findViewById(R.id.exp_list_view);

        //getting data from the previous activity
        username = getIntent().getExtras().getString("username");

        //setting up the rowsInList object
        rows = new RowsInList(getBaseContext(), getSupportFragmentManager(), username, expendables, searchManager);
        rows.setOnServiceClickedListener();
    }

    //when pressing something on the menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //if the user chose to return to the map
        if(item.getItemId() == R.id.toMapItem)
        {
            finish();
        }

        //if the user pressed on the show on map button
        else if(item.getItemId() == R.id.showOnMapItem) {
            Information selectedItem = rows.getSelectedItem();

            //returning to the map with the information on the selected service
            if(selectedItem != null) {
                Intent intent = new Intent();

                intent.putExtra("chosenItemLat", rows.getSelectedItem().getPosition().getLatitude());
                intent.putExtra("chosenItemLng", rows.getSelectedItem().getPosition().getLongitude());
                setResult(RESULT_OK, intent);
                finish();
            }

            //if the user didn't choose a service
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