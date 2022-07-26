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


/** This class is in-charge of all the Information list activity. it also functions
 * as a Listener for text-queries, for the searchable widget in it's menu */

public class ListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{
    private SearchManager searchManager;
    RowsInList rows;
    // A view that shows items in a vertically scrolling two-level list:
    ExpandableListView expendables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);     // load the main view of the activity
        searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        // prepare ExpandableListView to be sent to 'rows':
        expendables = findViewById(R.id.exp_list_view);
        String username = getIntent().getExtras().getString("username"); // name of user
        // initialize the list handler:
        rows = new RowsInList(getBaseContext(), getSupportFragmentManager(), username, expendables);

    }

    /* This function is in-charge of the logic of a pressed-widget, on the upper-menu
     * of the activity */

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // case of 'otMapItem' has been selected:
        if(item.getItemId() == R.id.toMapItem)
        {
            finish();
        }
        // case of user selected to show the information on the map:
        else if(item.getItemId() == R.id.showOnMapItem) {
            Information selectedItem = rows.getSelectedItem();
            // after selection, move the data to the map and start it
            if(selectedItem != null) {

                Intent intent = new Intent();
                intent.putExtra("chosenItemLat", rows.getSelectedItem().getPosition().getLatitude());
                intent.putExtra("chosenItemLng", rows.getSelectedItem().getPosition().getLongitude());
                setResult(RESULT_OK, intent);
                finish();
            }
            // not selected - user must select information in-order for showing it on map!
            else {
                Toast.makeText(getBaseContext(), "Please select an item.",
                        Toast.LENGTH_LONG).show();
            }

        }

        return super.onOptionsItemSelected(item);

    }

    /* This method create the menu that's located above the information list */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        // inflate the menu resource into the parameterized menu:
        inflater.inflate(R.menu.to_map_menu, menu);
        /* get reference to the search button:
           the search widget on the xml menu */
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return true;
            }
            /* case of exiting the search function - collapse all groups that's compatible with the
               current search query: */
            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                rows.collapseAll();
                return true;
            }
        });
        /* create a SearchView out of it, and set it to work for searching an information: */
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.requestFocus();
        return true;
    }

    /* Two methods for handling when a query is been written/submitted:
       It calls on a filter function based on the given string the user wrote, and expands
        every group in the list, that has a matching for the string */

    /* When submitted: */
    @Override
    public boolean onQueryTextSubmit(String s) {
        rows.getAdapter().filterData(s);
        rows.expandAll();
        return false;
    }
    /* When written: */
    @Override
    public boolean onQueryTextChange(String s) {
        rows.getAdapter().filterData(s);
        rows.expandAll();
        return false;
    }

}