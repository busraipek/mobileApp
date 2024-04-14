package com.example.quotesapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class OpenFragment extends Fragment {

    private static final String PREF_NAME = "MyPrefs";
    private static final String PREF_PREVIOUS_QUOTE = "previous_quote";
    private static final String PREF_PREVIOUS_DAY = "previous_day";

    private TextView quoteTextView;
    private TextView timeTextView;
    private boolean isTyping = false;
    private Handler handler;
    private String previousDayQuote = "";
    private int previousDay = 0; // Önceki güne ait alıntıyı saklamak için değişken
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_open, container, false);

        quoteTextView = rootView.findViewById(R.id.tvQuote);
        timeTextView = rootView.findViewById(R.id.tvTime);

        sharedPreferences = requireActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        previousDayQuote = sharedPreferences.getString(PREF_PREVIOUS_QUOTE, "");
        previousDay = sharedPreferences.getInt(PREF_PREVIOUS_DAY, 0);

        // Günün sözünü çekme işlemi
        checkAndUpdateQuoteOfTheDay();

        // TextView'e tıklama dinleyicisi ekleyin
        quoteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isTyping) {
                    isTyping = true;
                    slowTypeText(quoteTextView.getText().toString(), quoteTextView);
                }
            }
        });

        handler = new Handler();

        // Zamanı güncelleme işlemi için zamanlayıcı başlat
        startTimerToUpdateTime();

        return rootView;
    }

    private void checkAndUpdateQuoteOfTheDay() {

        // Handler nesnesi null ise başlat
        if (handler == null) {
            handler = new Handler();
        }
        // Gün değişimini kontrol et
        int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);

        if (currentDay != previousDay) {
            previousDay = currentDay;
            fetchQuoteOfTheDay();
        } else {
            // Eğer önceki güne ait alıntı varsa, o alıntıyı göster
            slowTypeText(previousDayQuote, quoteTextView);
        }
    }

    private void fetchQuoteOfTheDay() {
        // HTTP isteği oluşturma
        String url = "https://favqs.com/api/qotd";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // JSON yanıtından alıntı metnini alma
                            JSONObject quoteObject = response.getJSONObject("quote");
                            String quoteText = quoteObject.getString("body");
                            String authorText = quoteObject.getString("author");

                            // Alıntı metnini metin görüntüleyicide gösterme
                            if (quoteText.length() <= 200) {
                                quoteTextView.setText(quoteText + "\n\n" + " -" + authorText);

                                // Metinler çekildikten sonra slowTypeText'i başlat
                                slowTypeText(quoteText + "\n\n" + " -" + authorText, quoteTextView);

                                // Önceki güne ait alıntıyı güncelle
                                previousDayQuote = quoteText + "\n\n" + " -" + authorText;
                                sharedPreferences.edit().putString(PREF_PREVIOUS_QUOTE, previousDayQuote).apply();

                                // Güncellenmiş gün değerini sakla
                                previousDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
                                sharedPreferences.edit().putInt(PREF_PREVIOUS_DAY, previousDay).apply();
                            } else {
                                // Alıntı 200 karakterden uzunsa, yeni alıntı çek
                                fetchQuoteOfTheDay();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Hata durumunda işleme geç
                        quoteTextView.setText("Hata: Alıntı getirilemedi.");
                    }
                });

        // Volley ile isteği kuyruğa ekleme ve başlatma
        Volley.newRequestQueue(requireContext()).add(jsonObjectRequest);
    }


    private void slowTypeText(final String text, final TextView textView) {
        textView.setText(""); // TextView'i temizle

        // Text yazım işlemi
        for (int i = 0; i < text.length(); i++) {
            final int index = i;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    textView.append(String.valueOf(text.charAt(index)));
                    if (index == text.length() - 1) {
                        isTyping = false; // Yazma işlemi tamamlandı
                    }
                }
            }, 75 * i); // Her karakter arasında 75 ms gecikme
        }
    }

    private void startTimerToUpdateTime() {
        // Her saniyede bir zamanı güncellemek için zamanlayıcı başlat
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateTime(); // Zamanı güncelle
                handler.postDelayed(this, 1000); // 1 saniye sonra tekrar çağır
            }
        }, 1000); // Başlangıçta bir saniye sonra çalıştır
    }

    private void updateTime() {
        // Zamanı güncelle
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault());
        String currentDateAndTime = sdf.format(new Date());
        timeTextView.setText(currentDateAndTime);
    }
}
