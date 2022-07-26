package com.example.combinedproject.Others;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.combinedproject.Data.Information;
import com.example.combinedproject.Data.InformationHandler;
import com.example.combinedproject.R;
import java.util.ArrayList;
import java.util.List;
import androidx.fragment.app.FragmentManager;

/** This class is used to provide data and Views from some data to our expandable list view.
 *  It functions as an observer for RowDialog */

public class MainAdapter extends BaseExpandableListAdapter implements Observer{
    private final Context context;
    private final FragmentManager fm;
    private final String username;
    List<Service> parentRowList; // this is mutable - it's changeable depending on the user's search
    List<Service> originalList; // this just saves the original services and their info
    List<Information> favorites; // keep the favorites list, it's dynamically changeable


    // constructor
    public MainAdapter(Context context, FragmentManager fm, String username, List<Service> services){
        this.context = context;
        this.fm = fm;
        this.username = username;
        (this.parentRowList = new ArrayList<>()).addAll(services);
        (this.originalList = new ArrayList<>()).addAll(services);
        // initialize the local favorites list: it shall be send to the dialog
        for (Service s : this.parentRowList){
            if (s.getName().equals("מועדפים")){
                this.favorites = s.getChildList();
            }
        }

    }

    /* This method returns the number of services of the menu */

    @Override
    public int getGroupCount() {
        return parentRowList.size();
    }

    /* This method returns the number of info's - based on a service location */

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.parentRowList.get(groupPosition).getChildList().size();
    }

    /* This method returns the name of a clicked service - based on it's location */

    @Override
    public Object getGroup(int groupPosition) {
        return this.parentRowList.get(groupPosition);
    }

    /* This method returns the name of a clicked info of a clicked service */

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.parentRowList.get(groupPosition).getChildList().get(childPosition);
    }

    /* This method returns the given groupPosition */

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    /* This method returns the given childPosition */

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    /* This method indicates whether the child and group IDs are stable across changes
       to the underlying data */

    @Override
    public boolean hasStableIds() {
        return true;
    }

    /* This method gets a View that displays the given group (service).
       It loads the service's xml, set it's elements and returns it

    * params: groupPosition - the position of the group for which the View is returned
    *         isExpanded - whether the group is expanded or collapsed
    *         convertView - the old view to reuse, if possible
    *         parent - the parent that this view will eventually be attached to

    * returns: the View corresponding to the group at the specified position*/


    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup
            parent) {
        // get service
        Service group = (Service) getGroup(groupPosition);

        // check if old view is null. if so - inflate it
        if (convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.parent_row, null);
        }

        // set the icon - based on the service's type
        ImageView serviceIcon = (ImageView) convertView.findViewById(R.id.parent_icon);
        String type = InformationHandler.getEnglishTypeByHebrewType(group.getName());
        int drawableId = context.getResources().getIdentifier(type, "drawable",
                context.getPackageName());
        serviceIcon.setImageResource(drawableId);

        // set text
        TextView textview = convertView.findViewById((R.id.parent_text));
        textview.setText(group.getName());
        return convertView;
    }

    /* This method gets a View that displays the data for the given child within the given group.
       It loads the info's xml, set it's elements and returns it

      params: groupPosition - the position of the group that contains the child
              childPosition - the position of the child (for which the View is returned)
              within the group
    *         isLastChild - whether the child is the last child within the group
    *         convertView - the old view to reuse, if possible
    *         parent - the parent that this view will eventually be attached to

    * returns: the View corresponding to the group at the specified position*/

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
                             ViewGroup parent) {
        // the selected info
        Information child = (Information) getChild(groupPosition, childPosition);

        // if old child's view is null - inflate it
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.child_row, null);
        }


        // get reference to a TextView from the xml, and fill it with the
        // text from the clicked child:
        TextView childTextView = convertView.findViewById(R.id.child_text);
        childTextView.setText(child.getName());

        return convertView;

    }

    /* This method calls the RowDialog's 'show' method for uploading the dialog */

    public void openDialog(Information child, List<Information> favorites) {
        ListRowDialog rowDialog = new ListRowDialog(this.context, username, child, favorites);
        rowDialog.addObserver(this);  // add myself as an observer
        rowDialog.show(this.fm, "Dialog");
    }


    /* This method returns whether the child at the specified position is selectable. */

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    /* This method searches for each of the info's inside the services and creates a new list of
     *  services - of which whom only contains info's that holds the query in their names */

    public void filterData(String query) {
        query = query.toLowerCase();

        // clear the mutable list:
        parentRowList.clear();

        // case of query is empty - fill from the original list into the mutable parentRowList:
        if  (query.isEmpty()){
            parentRowList.addAll(originalList);
        } else {

            /* Go through each service in original list.
             *  for each one - loop all over his info's. if one of them contains the query -
             *   add it to a newly info's list. At the end - create a new service and add it to
             *   our mutable 'parentRowList' */

            // go through each service in original list:
            for (Service parentRow : originalList){
                List<Information> childList = parentRow.getChildList();  // get current info's:
                List<Information> newList = new ArrayList<Information>();  // new info's list
                // check if current service's info contains query:
                for (Information childRow : childList){
                    if  (childRow.getName().toLowerCase().contains(query)){
                        newList.add(childRow);
                    }
                }
                // create a new Service from the new info list, and add to the mutable services list
                if (newList.size() > 0){
                    Service nParentRow = new Service(parentRow.getName(), newList, 5);
                    parentRowList.add(nParentRow);
                }
            }
        }
        notifyDataSetChanged();
    }

    /* This method updates my listener */

    @Override
    public void update() {
        notifyDataSetChanged();
    }
}
