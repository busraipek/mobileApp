package com.example.quotesapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import java.util.Locale;
public class SettingsFragment<TranslatorOptions> extends Fragment {

    private Spinner languageSpinner;
    private static final String LANG_NAME = "LanguagePref";
    private static final String LANGUAGE_KEY = "languageKey";

    private Switch themeSwitch;
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "ThemePref";
    private static final String THEME_KEY = "themeKey";

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        languageSpinner = rootView.findViewById(R.id.language_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.languages_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(adapter);

        themeSwitch = rootView.findViewById(R.id.theme_switch);
        sharedPreferences = requireActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        // Kaydedilmiş tema tercihini kontrol et ve switch'in durumunu buna göre ayarla
        boolean isDarkMode = sharedPreferences.getBoolean(THEME_KEY, false);
        themeSwitch.setChecked(isDarkMode);


        // Switch'in durumunu dinleyin ve tema modunu değiştirin
        themeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Kullanıcının tercihini sakla
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(THEME_KEY, isChecked);
                editor.apply();

                // Tema modunu değiştir
                if (isChecked) {
                    // Kullanıcı karanlık tema istediğinde, karanlık moda geçin
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    // Kullanıcı karanlık tema istemediğinde, aydınlık moda geçin
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });


        // Kaydedilmiş dil tercihini kontrol et ve Spinner'ı buna göre ayarla
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(LANG_NAME, Context.MODE_PRIVATE);
        String selectedLanguage = sharedPreferences.getString(LANGUAGE_KEY, "");
        if (!selectedLanguage.isEmpty()) {
            int position = adapter.getPosition(selectedLanguage);
            if (position != -1) {
                languageSpinner.setSelection(position);
            }
        }

        // Spinner'daki dil seçimi değiştiğinde işlemleri yap
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String language = parent.getItemAtPosition(position).toString();
                if (language.equalsIgnoreCase("English")) {
                    changeLanguage("en");
                } else if (language.equalsIgnoreCase("Türkçe")) {
                    changeLanguage("tr");
                }
                // Diğer dil seçenekleri buraya eklenmeli
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Hiçbir şey yapılmaz
            }
        });

        return rootView;
    }
    private void changeLanguage(String languageCode) {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(LANG_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LANGUAGE_KEY, languageCode);
        editor.apply();

        // Uygulamanın dilini dinamik olarak değiştirme işlemi
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

    }
}