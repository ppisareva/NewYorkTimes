package polina.example.com.newyorktimes.listeners;

import android.support.v4.app.DialogFragment;

import polina.example.com.newyorktimes.model.FilterParameters;

/**
 * Created by polina on 9/22/17.
 */

public interface OnDialogActionListener {

        void onDialogPositiveClick(FilterParameters filterParameters);
        void onDialogNegativeClick(DialogFragment dialog);
    }

