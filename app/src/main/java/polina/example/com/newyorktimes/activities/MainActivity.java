package polina.example.com.newyorktimes.activities;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import polina.example.com.newyorktimes.R;
import polina.example.com.newyorktimes.adapter.NewsAdapter;
import polina.example.com.newyorktimes.dialog.FilterDialog;
import polina.example.com.newyorktimes.listeners.EndlessScrollListener;
import polina.example.com.newyorktimes.listeners.OnDialogActionListener;
import polina.example.com.newyorktimes.model.Doc;
import polina.example.com.newyorktimes.model.FilterParameters;
import polina.example.com.newyorktimes.model.New;
import polina.example.com.newyorktimes.model.TimesResponse;
import polina.example.com.newyorktimes.networks.Networks;
import polina.example.com.newyorktimes.util.Utils;
import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity implements OnDialogActionListener{
  private FilterParameters filterParameters;
    List<New> news = new ArrayList<>();
    NewsAdapter adapter;
    int spinnerTag =0;
    boolean resetFilter = false;
    private EndlessScrollListener scrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        filterParameters = new FilterParameters();
        spinnerInit();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvNews);
        recyclerView.setHasFixedSize(true);
        StaggeredGridLayoutManager gaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
        recyclerView.setLayoutManager(gaggeredGridLayoutManager);
        adapter = new NewsAdapter(news, this);
        recyclerView.setAdapter(adapter);

        scrollListener = new EndlessScrollListener(gaggeredGridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                startServiceRequest(filterParameters);

            }
        };
        recyclerView.addOnScrollListener(scrollListener);
    }

    private void spinnerInit() {
        Spinner spinner = (Spinner) findViewById(R.id.spnSort);
        final ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(this,
                R.array.sort_array, R.layout.simple_spinner_item);
        adapterSpinner.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterSpinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                filterParameters.setSort(adapterView.getItemAtPosition(pos).toString());
                filterParameters.setPage(0);
                resetFilter = true;
                if(spinnerTag!=pos){
                    startServiceRequest(filterParameters);
                    spinnerTag = pos;
                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    public void startServiceRequest(final FilterParameters filterParameters){
        //@Query("page") int page, @Query("q") String search, @Query("sort") String sort, @Query("begin_date") String data, @Query("fq") String news_desk
        Map<String, String> data = new HashMap<>();
        if(filterParameters.getKayWord()!=null) data.put("q", filterParameters.getKayWord());
        if(filterParameters.getDate()!=null) data.put("begin_date", filterParameters.getDate());
        if(filterParameters.isChecked()) data.put("fq", filterParameters.getDesk());
        if(filterParameters.getSort()!=null) data.put("sort", filterParameters.getSort());

        Call<TimesResponse> response = Networks.getService().getNews(filterParameters.getPage(), data);
        response.enqueue(new Callback<TimesResponse>() {
            @Override
            public void onResponse(Call<TimesResponse> call, retrofit2.Response<TimesResponse> response) {
               if(resetFilter){
                   news.clear();
                   adapter.notifyDataSetChanged();
                   filterParameters.setPage(1);
                   news.addAll(Utils.parseResponse(response));
                   adapter.notifyDataSetChanged();
                   resetFilter = false;
               } else {
                   filterParameters.setPage(filterParameters.nextPage());
                   news.addAll(Utils.parseResponse(response));
                   adapter.notifyDataSetChanged();
               }





            }

            @Override
            public void onFailure(Call<TimesResponse> call, Throwable t) {
                System.err.println("=============================================");
                t.printStackTrace();
            }
        });
    }

    public void onFilter(View view) {
        FilterDialog dialog = FilterDialog.newInstance(filterParameters);
        dialog.show(getSupportFragmentManager(), "fragment_filter");


    }



    @Override
    public void onDialogPositiveClick(FilterParameters filter) {
        resetFilter = true;
        filterParameters.setSport(filter.isSport());
        filterParameters.setFashion(filter.isFashion());
        filterParameters.setArt(filter.isArt());
        filterParameters.setDate(filter.getDate());
        filterParameters.setPage(0);
       startServiceRequest(filterParameters);

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.getDialog().cancel();
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
                startServiceRequest(filterParameters);
                resetFilter = true;
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
