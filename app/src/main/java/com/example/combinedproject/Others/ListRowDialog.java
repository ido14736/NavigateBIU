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
import com.example.combinedproject.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;


/* This class is in-charge of opening the dialog, that'll appear when Information is clicked.
*  We create a dialog builder, to build a dialog */

public class ListRowDialog extends AppCompatDialogFragment {

    private final Context context;
    private final String name; // The Info's Name for appearing as the title
    private final String description; // the description to show inside the dialog box
    private Button addToFavorites;
    private String username;
    private static DatabaseHandler myDB;
//    private RowDialogListener listener;


    // constructor:
    public ListRowDialog(Context c, String Name, String username, String description){
        this.context = c;
        this.name = Name;
        this.description = description;
        this.username = username;

        myDB = new DatabaseHandler(c);

    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (getActivity() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // inflate the view for the dialog:
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.list_layout_dialog, null); // maybe instead of null, put the menu dialog
            builder.setView(view);

            builder.setTitle(this.name)
                    .setMessage(this.description)
                    .setPositiveButton("סגור", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
            addToFavorites = view.findViewById(R.id.button);
            // case of information ALREADY IN the user's favorite's table db
            if(myDB.doesFavoriteExists(username, this.name)) {
                Toast.makeText(context, "Already in favorites.",
                        Toast.LENGTH_LONG).show();
                //TO-DO: delete the info from favorites
                addToFavorites.setText("הסרה מהמועדפים");

            }
            // case of information NOT IN the user's favorite's table db
            else {
                Toast.makeText(context, "Added to favorites.",
                        Toast.LENGTH_LONG).show();
                addToFavorites.setText("הוספה למועדפים");
            }
//            addToFavorites.setText("הוספה למועדפים");
            // #### INSTEAD OF THE 2ND PARAMETER, I CAN HAVE AN INFORMATION VAR,
            // AND IN THE METHOD - SEND OT TO THE DB: for now, just testing it, using print
            setOnClick(addToFavorites, this.name);
            return builder.create();
        }

        return new Dialog(this.context);
    }

//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//        try {
//            listener = (RowDialogListener) context;
//        } catch (ClassCastException e) {
//            // in case this dialog hs been opened from the main activity - it shall fail, if
//            //  we forget to implement it there
//            throw new ClassCastException(context.toString() +
//                    "must implement RowDialogListener");
//        }
//    }

//    /* Interface for sending the Information of the current dialog - to the DB */
//    public interface RowDialogListener{
//        /* He can send the data from the fields of the upper class */
//        void AddFavoriteToDB();
//    }

    private void setOnClick(final Button btn, final String str){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // case of information ALREADY IN the user's favorite's table db
                if(myDB.doesFavoriteExists(username, str)) {
                    //TO-DO: delete the info from favorites
                    myDB.removeFavorite(username, str);
                    Toast.makeText(context, "Already in favorites.",
                            Toast.LENGTH_LONG).show();
                    btn.setText("הוספה למועדפים");

                }
                // case of information NOT IN the user's favorite's table db
                else {
                    myDB.addFavorite(username, str); // add
                    Toast.makeText(context, "Added to favorites.",
                            Toast.LENGTH_LONG).show();
                    btn.setText("הסרה מהמועדפים");
                }



            }
        });
    }


}
