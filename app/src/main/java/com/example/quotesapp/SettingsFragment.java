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

public class SettingsFragment extends Fragment {

    private Spinner languageSpinner;
    private static final String PREF_NAME = "LanguagePref";
    private static final String LANGUAGE_KEY = "languageKey";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        languageSpinner = rootView.findViewById(R.id.language_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.languages_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(adapter);

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String selectedLanguage = sharedPreferences.getString(LANGUAGE_KEY, "en"); // Varsayılan dil İngilizce

        // Kaydedilmiş dil tercihini kontrol et ve spinner'ı buna göre ayarla
        int languagePosition = adapter.getPosition(selectedLanguage);
        languageSpinner.setSelection(languagePosition);

        // Spinner'ın seçim değişikliklerini dinleyin ve dil değiştirme fonksiyonunu çağırın
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLanguage = parent.getItemAtPosition(position).toString();
                changeLanguage(selectedLanguage); // Seçilen dile göre dil değiştirme işlemi
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Burada bir şey yapmanıza gerek yok
            }
        });

        return rootView;
    }

    private void changeLanguage(String languageCode) {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LANGUAGE_KEY, languageCode);
        editor.apply();

        // Uygulamanın dilini dinamik olarak değiştirme işlemi
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        // Aktiviteyi yeniden başlatın veya ekranı güncelleyin
         // Örneğin, mevcut aktiviteyi yeniden başlatmak için
    }
}