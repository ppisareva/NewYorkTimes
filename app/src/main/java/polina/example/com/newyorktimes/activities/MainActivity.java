package polina.example.com.newyorktimes.activities;

import android.app.Dialog;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Date;

import polina.example.com.newyorktimes.R;
import polina.example.com.newyorktimes.model.FilterParameters;
import polina.example.com.newyorktimes.model.TimesResponse;
import polina.example.com.newyorktimes.networks.Networks;
import polina.example.com.newyorktimes.util.Utils;
import retrofit2.Call;
import retrofit2.Callback;

import static android.R.attr.y;
import static junit.runner.Version.id;

public class MainActivity extends AppCompatActivity {
  final FilterParameters filterParameters = new FilterParameters();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        spinnerInit();
    }

    private void spinnerInit() {
        Spinner spinner = (Spinner) findViewById(R.id.spnSort);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sort_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                filterParameters.setSort(adapterView.getItemAtPosition(pos).toString());
                if(filterParameters.getKayWord()!=null) startServiceRequest();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void startServiceRequest(){
        Call<TimesResponse> response = Networks.getService().getNews(filterParameters.getPage(), filterParameters.getKayWord(), filterParameters.getSort(), filterParameters.getCurrentDate(), filterParameters.getDesk());
        response.enqueue(new Callback<TimesResponse>() {
            @Override
            public void onResponse(Call<TimesResponse> call, retrofit2.Response<TimesResponse> response) {
                System.err.println("response.body().status = " + response.body().status);
                System.err.println("response.body().response.docs = " + response.body().response.docs);
            }

            @Override
            public void onFailure(Call<TimesResponse> call, Throwable t) {
                System.err.println("=============================================");
                t.printStackTrace();
            }
        });
    }


    public void onFilter(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setTitle(getString(R.string.filter));
        Button mSubmit = (Button) dialog.findViewById(R.id.btnSubmit);
        final DatePicker mDatePicker = (DatePicker) dialog.findViewById(R.id.dpNews);
        initDatePicker(mDatePicker);
        final CheckBox mCheckArt = (CheckBox) dialog.findViewById(R.id.cbArt);
        mCheckArt.setChecked(filterParameters.isArt());
        final CheckBox mCheckFashion = (CheckBox) dialog.findViewById(R.id.cbFashion);
        mCheckFashion.setChecked(filterParameters.isFashion());
        final CheckBox mCheckSport = (CheckBox) dialog.findViewById(R.id.cbSport);
        mCheckSport.setChecked(filterParameters.isSport());
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mCheckArt.isChecked()||mCheckFashion.isChecked()||mCheckSport.isChecked()){
                    ((TextView) dialog.findViewById(R.id.warning)).setVisibility(View.GONE);
                    filterParameters.setArt(mCheckArt.isChecked());
                    filterParameters.setFashion(mCheckFashion.isChecked());
                    filterParameters.setSport(mCheckSport.isChecked());
                    filterParameters.setDate(Utils.getDateFromDatePicker(mDatePicker));
                    if(filterParameters.getKayWord()!=null) startServiceRequest();
                    dialog.cancel();
                } else {
                    ((TextView) dialog.findViewById(R.id.warning)).setVisibility(View.VISIBLE);
                }


            }
        });
        dialog.show();
    }

    private void initDatePicker(DatePicker mDatePicker) {
        mDatePicker.setMaxDate(System.currentTimeMillis());
        int year = Utils.getYear(filterParameters.getDate());
        int month = Utils.getMonth(filterParameters.getDate());
        int day = Utils.getDay(filterParameters.getDate());
        mDatePicker.updateDate(year, month, day);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setSubmitButtonEnabled(true);
        int searchImgId = android.support.v7.appcompat.R.id.search_button;
        ImageView v = (ImageView) searchView.findViewById(searchImgId);
        v.setImageResource(android.R.drawable.ic_menu_search);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterParameters.setKayWord(query);
                startServiceRequest();
                searchView.clearFocus();


                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterParameters.setKayWord(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
              finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


}
