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

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;

import polina.example.com.newyorktimes.R;
import polina.example.com.newyorktimes.model.FilterParameters;
import polina.example.com.newyorktimes.model.TimesResponse;
import polina.example.com.newyorktimes.networks.Networks;
import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {
  final FilterParameters filterParameters = new FilterParameters();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Call<TimesResponse> response = Networks.getService().getNews(filterParameters.getPage(), "a", filterParameters.getSort(), filterParameters.getCurrentDate());
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
        final CheckBox mCheckArt = (CheckBox) dialog.findViewById(R.id.cbArt);
        final CheckBox mCheckFashion = (CheckBox) dialog.findViewById(R.id.cbFashion);
        final CheckBox mCheckSport = (CheckBox) dialog.findViewById(R.id.cbSport);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterParameters.setArt(mCheckArt.isChecked());
                filterParameters.setFashion(mCheckFashion.isChecked());
                filterParameters.setSport(mCheckSport.isChecked());

            }
        });
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setSubmitButtonEnabled(true);
        int searchImgId = android.support.v7.appcompat.R.id.search_button;
        ImageView v = (ImageView) searchView.findViewById(searchImgId);
        v.setImageResource(android.R.drawable.ic_menu_search);
//        // Customize searchview text and hint colors
//        int searchEditId = android.support.v7.appcompat.R.id.search_src_text;
//        EditText et = (EditText) searchView.findViewById(searchEditId);
//        et.setTextColor(Color.BLACK);
//        et.setHintTextColor(Color.BLACK);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
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
