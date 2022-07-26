package com.example.combinedproject.Others;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.example.combinedproject.Data.DatabaseHandler;
import com.example.combinedproject.Data.Information;
import com.example.combinedproject.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import java.util.Iterator;
import java.util.List;


/** This class is in-charge of opening the dialog, that'll appear when Information is clicked.
 *  In the dialog the user can decide if to add the clicked information to his favorites list.
 *   We create a dialog builder, to build a dialog.
 *  It functions as an observable for MainAdapter*/

public class ListRowDialog extends AppCompatDialogFragment implements Observable{
    MainAdapter listener;
    private final Context context;
    private final Information pressed_info;
    List<Information> favorites;
    private final String username;
    private static DatabaseHandler myDB;

    // constructor:
    public ListRowDialog(Context c, String username, Information info, List<Information> favorites){
        this.context = c;
        this.username = username;
        this.pressed_info = info;
        this.favorites = favorites;
        myDB = new DatabaseHandler(c);

    }

    /* This method shall be called when the builder's 'show' method shall be called.
     *  the method defines the dialog */

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (getActivity() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            // inflate the view for the dialog:
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.list_layout_dialog, null);
            builder.setView(view);

            // The Info's Name for appearing as the title:
            builder.setTitle(this.pressed_info.getName())
                    // the description to show inside the dialog box:
                    .setMessage(this.pressed_info.getDescription())
                    .setPositiveButton("סגור", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
            // The button for adding to favorites
            Button addToFavorites = view.findViewById(R.id.button);

            // case of information ALREADY IN the user's favorite's table db
            if(myDB.doesFavoriteExists(username, this.pressed_info.getName())) {
                addToFavorites.setText("הסרה מהמועדפים");

            }

            // case of information NOT IN the user's favorite's table db
            else {
                addToFavorites.setText("הוספה למועדפים");
            }
            // set a listener on the button:
            setOnClick(addToFavorites, this.pressed_info, this.favorites);
            return builder.create();
        }

        return new Dialog(this.context);
    }

    /* This method set's a click listener on the 'addToFavorites button */

    private void setOnClick(final Button btn, Information pressed_info, List<Information> favorites){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // case of information ALREADY IN the user's favorite's table db
                if(myDB.doesFavoriteExists(username, pressed_info.getName())) {
                    myDB.removeFavorite(username, pressed_info.getName());  // delete from db
                    // remove from the favorites list:
                    for (Iterator<Information> iterator = favorites.iterator();
                         iterator.hasNext();) {
                        Information info = iterator.next();
                        if (info.equals(pressed_info)){
                            iterator.remove();
                            break;
                        }
                    }

                    // announce to user
                    Toast.makeText(context, "Removed from favorites.",
                            Toast.LENGTH_LONG).show();
                    btn.setText("הוספה למועדפים");
                }

                // case of information NOT IN the user's favorite's table db
                else {
                    myDB.addFavorite(username, pressed_info.getName()); // add to db
                    favorites.add(pressed_info);   // add to the favorites list
                    // announce to user
                    Toast.makeText(context, "Added to favorites.",
                            Toast.LENGTH_LONG).show();
                    btn.setText("הסרה מהמועדפים");
                }

                // notify my observer that data has changed
                notifyChanges();

            }
        });
    }

    /* This method initializes my observer */

    @Override
    public void addObserver(MainAdapter adapter) {
        this.listener = adapter;
    }

    /* This method notify my observer that i've made some changes on the data */

    @Override
    public void notifyChanges() {
        this.listener.update();
    }
}
