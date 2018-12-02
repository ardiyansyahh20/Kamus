package com.ardi.kamus.main;

import android.database.SQLException;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.SearchView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ardi.kamus.R;
import com.ardi.kamus.adapter.SearchAdapter;
import com.ardi.kamus.db.KamusHelper;
import com.ardi.kamus.entity.Kamus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer_layout;
    @BindView(R.id.nav_view)
    NavigationView nav_view;
    @BindView(R.id.search_view)
    SearchView search_view;
    @BindView(R.id.recycle_view)
    RecyclerView recycle_view;

    private KamusHelper kamusHelper;
    private SearchAdapter adapter;

    private ArrayList<Kamus> kamuses = new ArrayList<>();
    private boolean isEnglish = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer_layout.setDrawerListener(toggle);
        toggle.syncState();

        nav_view.setNavigationItemSelectedListener(this);

        kamusHelper = new KamusHelper(this);

        search_view.onActionViewExpanded();
        search_view.setOnQueryTextListener(this);

        setupList();
        loadData();
        nav_view.getMenu().getItem(0).setChecked(true);

    }

    @Override
    public void onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)){
            drawer_layout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_english_indonesia){
            isEnglish = true;

            loadData();
        }

        if (id == R.id.nav_indonesia_english){
            isEnglish = false;

            loadData();
        }

        drawer_layout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setupList(){
        adapter = new SearchAdapter();
        recycle_view.setLayoutManager(new LinearLayoutManager(this));
        recycle_view.setAdapter(adapter);
    }

    private void loadData(String search){
        try {
            kamusHelper.open();
            if (search.isEmpty()){
                kamuses = kamusHelper.getAllData(isEnglish);
            }else {
                kamuses = kamusHelper.getDataByName(search, isEnglish);
            }

            String hint;
            if (isEnglish){
                getSupportActionBar().setSubtitle(getResources().getString(R.string.english_indonesia));
                hint = getResources().getString(R.string.find_words);
            }else {
                getSupportActionBar().setSubtitle(getResources().getString(R.string.indonesia_english));
                hint = getResources().getString(R.string.cari_kata);
            }
            search_view.setQueryHint(hint);
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            kamusHelper.close();
        }
        adapter.replaceAll(kamuses);
    }

    private void loadData(){
        loadData("");
    }

    @Override
    public boolean onQueryTextSubmit(String keyword) {
        loadData(keyword);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String keyword) {
        loadData(keyword);
        return false;
    }
}
