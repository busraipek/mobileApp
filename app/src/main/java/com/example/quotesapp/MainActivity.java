package com.example.quotesapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.View;
import com.google.android.material.navigation.NavigationView;

import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences lsharedPreferences = getSharedPreferences("LanguagePref", Context.MODE_PRIVATE);
        String languageValue = lsharedPreferences.getString("languageKey", "false"); // Varsayılan olarak "false" değeri al
        boolean isTurkish = Boolean.parseBoolean(languageValue);

        Locale locale;

        if (isTurkish) {
            locale = new Locale("tr");
            Locale.setDefault(locale);
        } else {
            locale = new Locale("en");
            Locale.setDefault(locale);
        }

        Configuration config = getResources().getConfiguration();
        config.setLocale(locale);

// Güncellenmiş Configuration objesini updateConfiguration metoduyla uygula
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle navigation view item clicks here.
                displaySelectedScreen(item.getItemId());
                return true;
            }
        });

        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            // set initial fragment
            displaySelectedScreen(R.id.nav_open);
        }

        // ActionBarDrawerToggle kullanmadığımız için, menü tuşuna basıldığında NavigationView'ı açmak için bir OnClickListener ekleyeceğiz
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                } else {
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
            }
        });

        startFavoritesFragment();


        // Tema tercihini kontrol et ve uygun temayı ayarla
        SharedPreferences sharedPreferences = getSharedPreferences("ThemePref", Context.MODE_PRIVATE);
        boolean isDarkMode = sharedPreferences.getBoolean("themeKey", false);
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

    }

    private void displaySelectedScreen(int itemId) {
        Fragment fragment = null;

        if (itemId == R.id.nav_profile) {
            fragment = new ProfileFragment();
        } else if (itemId == R.id.nav_favorites) {
            fragment = new FavoritesFragment();
        } else if (itemId == R.id.nav_quotes) {
            fragment = new QuotesFragment();
        } else if (itemId == R.id.nav_category) {
            fragment = new CategoryFragment();
        } else if (itemId == R.id.nav_settings) {
            fragment = new SettingsFragment();
        } else if (itemId == R.id.nav_open) {
            fragment = new OpenFragment();
        }
        if (fragment != null) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        }

        drawerLayout.closeDrawers();
    }

    private void startFavoritesFragment() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(new FavoritesFragment(), "favorites");
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            super.onBackPressed();
        }
    }
}

