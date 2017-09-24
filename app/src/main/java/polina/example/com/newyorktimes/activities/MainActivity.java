package polina.example.com.newyorktimes.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import polina.example.com.newyorktimes.R;
import polina.example.com.newyorktimes.adapter.NewsAdapter;
import polina.example.com.newyorktimes.databinding.ActivityMainBinding;
import polina.example.com.newyorktimes.dialog.FilterDialog;
import polina.example.com.newyorktimes.listeners.EndlessScrollListener;
import polina.example.com.newyorktimes.listeners.OnDialogActionListener;
import polina.example.com.newyorktimes.model.FilterParameters;
import polina.example.com.newyorktimes.model.New;
import polina.example.com.newyorktimes.model.TimesResponse;
import polina.example.com.newyorktimes.networks.Networks;
import polina.example.com.newyorktimes.util.Utils;
import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity implements OnDialogActionListener{
    private FilterParameters filterParameters;
    private List<New> news = new ArrayList<>();
    private NewsAdapter adapter;
    private int spinnerTag =0;
    private boolean resetFilter = false;
    private EndlessScrollListener scrollListener;
    private ActivityMainBinding binding;
    public static final String KEY_WORD = "q";
    public static final String START_DATE = "begin_date";
    public static final String ADVANCED_SORT = "fq";
    public static final String SORT = "sort";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        filterParameters = new FilterParameters();
        initToolbar();
        spinnerInit();
        initRecycleView();
    }



    private void initRecycleView() {
        RecyclerView recyclerView = binding.rvNews;
        recyclerView.setHasFixedSize(true);
        StaggeredGridLayoutManager gaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
        recyclerView.setLayoutManager(gaggeredGridLayoutManager);
        adapter = new NewsAdapter(news, this);
        recyclerView.setAdapter(adapter);
        scrollListener = new EndlessScrollListener(gaggeredGridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                startServiceRequest(filterParameters);
            }
        };
    }

    private void initToolbar() {
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void spinnerInit() {
        Spinner spinner = binding.spnSort;
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
                Toast.makeText(MainActivity.this, getString(R.string.nothing_selected), Toast.LENGTH_LONG);
            }
        });
    }


    public void startServiceRequest(final FilterParameters filterParameters){

        if(Utils.isNetworkAvailable(this)&&Utils.isOnline()) {
            Map<String, String> data = new HashMap<>();
            if (filterParameters.getKayWord() != null) data.put(KEY_WORD, filterParameters.getKayWord());
            if (filterParameters.getDate() != null)
                data.put(START_DATE, filterParameters.getDate());
            if (filterParameters.isChecked()) data.put(ADVANCED_SORT, filterParameters.getDesk());
            if (filterParameters.getSort() != null) data.put(SORT, filterParameters.getSort());

            Call<TimesResponse> response = Networks.getService().getNews(filterParameters.getPage(), data);
            response.enqueue(new Callback<TimesResponse>() {
                @Override
                public void onResponse(Call<TimesResponse> call, retrofit2.Response<TimesResponse> response) {
                    if (resetFilter) {
                        news.clear();
                        adapter.notifyDataSetChanged();
                        filterParameters.setPage(1);
                        news.addAll(Utils.parseResponse(response));
                        adapter.notifyDataSetChanged();
                        scrollListener.resetState();
                        resetFilter = false;
                    } else {
                        filterParameters.setPage(filterParameters.nextPage());
                        news.addAll(Utils.parseResponse(response));
                        adapter.notifyDataSetChanged();
                    }


                }

                @Override
                public void onFailure(Call<TimesResponse> call, Throwable t) {
                   Toast.makeText(MainActivity.this, getString(R.string.failed_to_connect_to_server), Toast.LENGTH_LONG);
                    t.printStackTrace();
                }
            });

        }else {
            Toast.makeText(this, getString(R.string.no_internet_connection), Toast.LENGTH_LONG);
        }
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
