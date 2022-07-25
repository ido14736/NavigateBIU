package com.example.combinedproject.Others;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import com.example.combinedproject.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

//shuttles row dialog class(a popup window in the shuttles activity)
public class ShuttlesRowDialog extends AppCompatDialogFragment {
    private final Context context;
    private final String name;
    private final String arrivalTime;
    private final String timeLeft;

    // constructor:
    public ShuttlesRowDialog(Context c, String name, String arrivalTime, String timeLeft){
        this.context = c;
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.timeLeft = timeLeft;
    }

    //creating the dialog
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (getActivity() != null) {
            //creating the builder of the alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            // inflate the view for the dialog:
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.shuttles_layout_dialog, null);
            builder.setView(view);

            //setting the fields of the builder
            builder.setTitle(this.name)
                    .setMessage("Time Left For Arrival:\n" + this.timeLeft + "\nExpected Arrival Time: " + this.arrivalTime)
                    .setPositiveButton("סגור", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
            return builder.create();
        }

        return new Dialog(this.context);
    }
}
