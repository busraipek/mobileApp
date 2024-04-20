package com.example.quotesapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import java.util.Locale;

public class SettingsFragment extends Fragment {

    private Switch languageSwitch;
    private static final String LANG_NAME = "LanguagePref";
    private static final String LANGUAGE_KEY = "languageKey";
    private SharedPreferences lsharedPreferences;

    private Switch themeSwitch;
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "ThemePref";
    private static final String THEME_KEY = "themeKey";

    @SuppressLint("WrongViewCast")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        themeSwitch = rootView.findViewById(R.id.theme_switch);
        sharedPreferences = requireActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        boolean isDarkMode = sharedPreferences.getBoolean(THEME_KEY, false);
        themeSwitch.setChecked(isDarkMode);

        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(THEME_KEY, isChecked);
            editor.apply();

            AppCompatDelegate.setDefaultNightMode(isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        });

        initThemeSwitch(rootView);
        initLanguageSwitch(rootView);

        return rootView;
    }

    private void initThemeSwitch(View rootView) {
        themeSwitch = rootView.findViewById(R.id.theme_switch);
        sharedPreferences = requireActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        boolean isDarkMode = sharedPreferences.getBoolean(THEME_KEY, false);
        themeSwitch.setChecked(isDarkMode);

        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(THEME_KEY, isChecked);
            editor.apply();

            AppCompatDelegate.setDefaultNightMode(isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        });
    }

    private void initLanguageSwitch(View rootView) {
        languageSwitch = rootView.findViewById(R.id.switch1);
        lsharedPreferences = requireActivity().getSharedPreferences(LANG_NAME, Context.MODE_PRIVATE);
        String langPref = getLanguagePreference(); // Dil tercihini al
        boolean isTurkish = langPref.equals("tr");
        languageSwitch.setChecked(isTurkish);

        languageSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            saveLanguagePreference(isChecked ? "tr" : "en"); // Dil tercihini kaydet
            setAppLanguage(isChecked ? "tr" : "en"); // Uygulama dilini ayarla
            restartActivity();
        });
    }

    private void saveLanguagePreference(String languageCode) {
        lsharedPreferences = requireActivity().getSharedPreferences(LANG_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = lsharedPreferences.edit();
        editor.putString(LANGUAGE_KEY, languageCode);
        editor.apply();
    }

    // Dil tercihini alıp işleme
    private String getLanguagePreference() {
        lsharedPreferences = requireActivity().getSharedPreferences(LANG_NAME, Context.MODE_PRIVATE);
        // Varsayılan olarak en son kullanılan dil olan değeri döndür
        return lsharedPreferences.getString(LANGUAGE_KEY, getLastUsedLanguage());
    }

    private String getLastUsedLanguage() {
        // Burada en son kullanılan dilin bilgisini kaydettiğiniz bir şekilde alın
        // Örneğin, başka bir SharedPreferences anahtarını kullanarak
        lsharedPreferences = requireActivity().getSharedPreferences(LANG_NAME, Context.MODE_PRIVATE);
        return lsharedPreferences.getString("LastUsedLanguage", "tr");
    }

    private void setAppLanguage(String languageCode) {
        // Buradaki dil ayarı kodu ile uygulama dilini ayarlayın
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.locale = locale;
        requireContext().getResources().updateConfiguration(config, requireContext().getResources().getDisplayMetrics());
    }

    private void restartActivity() {
        requireActivity().recreate();
    }
}

