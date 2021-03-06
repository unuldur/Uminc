package com.unuldur.uminc;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.usage.UsageEvents;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.unuldur.uminc.model.IEtudiant;
import com.unuldur.uminc.model.IEvent;
import com.unuldur.uminc.model.SimpleEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private int idFragmentActuel = -1;
    private IEtudiant etudiant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            idFragmentActuel = savedInstanceState.getInt("framentid");
        }
        Intent intent = getIntent();
        etudiant = (IEtudiant) intent.getSerializableExtra("etudiant");
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if(idFragmentActuel == -1){
            idFragmentActuel = R.id.nav_accueil;
        }
        Calendar c = Calendar.getInstance();
        c.set(Calendar.SECOND, c.get(Calendar.SECOND) + 30);
        IEvent event = new SimpleEvent("Yay", "201", c, c);
        Calendar c2 = Calendar.getInstance();
        c2.set(Calendar.SECOND, c.get(Calendar.SECOND) + 70);
        IEvent event2 = new SimpleEvent("Yay2", "202", c2, c2);
        List<IEvent> events = new ArrayList<>();
        events.add(event);
        events.add(event2);
        AlarmManagerEvent.getInstance(this).createNotifications(events, 0);
        AlarmManagerEvent.getInstance(this).cancelAlarm(events);
        displaySelectedScreen(idFragmentActuel);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        displaySelectedScreen(item.getItemId());
        idFragmentActuel = item.getItemId();
        return true;
    }

    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_accueil:
                fragment = AccueilFragment.newInstance(etudiant);
                break;
            case R.id.nav_calendar:
                fragment = CalendarFragment.newInstance(etudiant);
                break;
            case R.id.nav_notes:
                fragment = NotesFragment.newInstance(etudiant);
                break;
            case R.id.nav_manage:
                fragment = ParameterFragment.newInstance(etudiant);
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("fragmentid", idFragmentActuel);
        super.onSaveInstanceState(outState);
    }
}
