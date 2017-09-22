package polina.example.com.newyorktimes.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import polina.example.com.newyorktimes.R;
import polina.example.com.newyorktimes.listeners.OnDialogActionListener;
import polina.example.com.newyorktimes.model.FilterParameters;
import polina.example.com.newyorktimes.model.New;
import polina.example.com.newyorktimes.util.Utils;

/**
 * Created by polina on 9/22/17.
 */

public class FilterDialog extends DialogFragment {


    private static final String KEY_WORDS = "words";
    private static final String DATE = "date";
    private static final String SORT = "sort";
    private static final String IS_ART = "art";
    private static final String IS_SPORT = "sport";
    private static final String IS_FASHION = "fashion";
    FilterParameters filterPar;
    OnDialogActionListener mListener;
    private DatePicker mDatePicker;
    CheckBox mCheckArt;
    CheckBox mCheckFashion ;
    CheckBox mCheckSport;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnDialogActionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString());
        }
    }

    public FilterDialog() {
    }

    public static FilterDialog newInstance(FilterParameters filterParameters) {
        FilterDialog dialog = new FilterDialog();
        Bundle bundle = new Bundle();
        bundle.putString(DATE, filterParameters.getDate());
        bundle.putBoolean(IS_FASHION, filterParameters.isFashion());
        bundle.putBoolean(IS_ART, filterParameters.isArt());
        bundle.putBoolean(IS_SPORT, filterParameters.isSport());
        dialog.setArguments(bundle);
        return dialog;
    }




    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = getActivity().getLayoutInflater().inflate(R.layout.custom_dialog, null);

        builder.setView(view).setTitle(getString(R.string.filter))
                .setPositiveButton("OK",  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                filterPar.setFashion(mCheckFashion.isChecked());
                filterPar.setArt(mCheckArt.isChecked());
                filterPar.setSport(mCheckSport.isChecked());
                filterPar.setDate(Utils.getDateFromDatePicker(mDatePicker));
                mListener.onDialogPositiveClick(filterPar);
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onDialogNegativeClick(FilterDialog.this);
                }


        });

        initView(getArguments(), view);

        return builder.create();
}

    private void initView(Bundle arguments, View view) {
        Bundle bundle =  getArguments();
        filterPar = new FilterParameters();
        filterPar.setDate(bundle.getString(DATE));
        filterPar.setKayWord(bundle.getString(KEY_WORDS));
        filterPar.setSort(bundle.getString(SORT));
        filterPar.setSport(bundle.getBoolean(IS_SPORT));
        filterPar.setArt(bundle.getBoolean(IS_ART));
        filterPar.setFashion(bundle.getBoolean(IS_FASHION));
        mDatePicker = (DatePicker) view.findViewById(R.id.dpNews);
        initDatePicker(mDatePicker);
        mCheckArt = (CheckBox) view.findViewById(R.id.cbArt);
        mCheckArt.setChecked(filterPar.isArt());
        mCheckFashion = (CheckBox) view.findViewById(R.id.cbFashion);
        mCheckFashion.setChecked(filterPar.isFashion());
        mCheckSport = (CheckBox) view.findViewById(R.id.cbSport);
        mCheckSport.setChecked(filterPar.isSport());

    }


    private void initDatePicker(DatePicker mDatePicker) {
        mDatePicker.setMaxDate(System.currentTimeMillis());
        int year = Utils.getYear(filterPar.getCurrentDate());
        int month = Utils.getMonth(filterPar.getCurrentDate());
        int day = Utils.getDay(filterPar.getCurrentDate());
        if(filterPar.getDate()!=null){
            year = Utils.getYear(filterPar.getDate());
            month = Utils.getMonth(filterPar.getDate());
            day = Utils.getDay(filterPar.getDate());
        }
        mDatePicker.updateDate(year, month, day);

    }


}
