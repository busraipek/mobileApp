package com.example.quotesapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class QuotesFragment extends Fragment {

    private TextView quoteTextView;
    private TextView authorTextView;
    private boolean active = false;
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_quotes, container, false);

        quoteTextView = rootView.findViewById(R.id.Quote);
        authorTextView = rootView.findViewById(R.id.authorTextView);
        Button btnNextQuote = rootView.findViewById(R.id.btnNextQuote);
        final ImageButton btnFavorite = rootView.findViewById(R.id.btnFavorite);

        // Günün sözünü çekme işlemi
        fetchQuoteOfTheDay();
        // "Next Quote" butonuna tıklama olayı ekleme
        btnNextQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Yeni bir alıntı çekme işlemi
                fetchQuoteOfTheDay();
            }
        });

        // Favori butonuna tıklama olayı ekleme
        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation scaleAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.scale_anim);
                btnFavorite.startAnimation(scaleAnim);

                if (active) {
                    // Buton pasif hale getirilir ve alıntı favorilerden kaldırılır
                    btnFavorite.setImageResource(R.drawable.id_like);
                    removeFromFavorites();
                } else {
                    // Buton aktif hale getirilir ve alıntı favorilere eklenir
                    btnFavorite.setImageResource(R.drawable.id_likeaktif);
                    addToFavorites();
                }

                active = !active;
            }
        });

        return rootView;
    }

    private void fetchQuoteOfTheDay() {
        // API Anahtarını tanımla
        final String API_KEY = "RnAKuJVl7LcJlxfEwcOkfw==0Dq0V084fG7bdqTZ";

        // HTTP isteği oluşturma
        String url = "https://api.api-ninjas.com/v1/quotes?category=";
        StringRequest stringRequest = new StringRequest
                (Request.Method.GET, url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            // JSON yanıtından alıntı metnini ve yazarını alma
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject quoteObject = jsonArray.getJSONObject(0);
                            String quoteText = quoteObject.getString("quote");
                            String author = quoteObject.getString("author");

                            // Alıntı metnini ve yazarını metin görüntüleyicide gösterme
                            quoteTextView.setText(quoteText);
                            authorTextView.setText("- " + author); // Yazarı göstermek için textview'e ekleyin

                            // Favori butonunu pasif hale getir
                            active = false;
                            ImageButton btnFavorite = rootView.findViewById(R.id.btnFavorite); // rootView kullanılarak btnFavorite bulunuyor
                            btnFavorite.setImageResource(R.drawable.id_like);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Hata durumunda işleme geç
                        quoteTextView.setText(getString(R.string.quotations));
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("X-Api-Key", API_KEY);
                return headers;
            }
        };

        // Volley ile isteği kuyruğa ekleme ve başlatma
        Volley.newRequestQueue(requireContext()).add(stringRequest);
    }

    private void addToFavorites() {
        String quote = quoteTextView.getText().toString() + "  \n" + authorTextView.getText().toString();
        FavoritesFragment.addToFavorites(quote);
        Toast.makeText(getActivity(), getString(R.string.quotefav), Toast.LENGTH_SHORT).show();
    }

    private void removeFromFavorites() {
        String quote = quoteTextView.getText().toString() + "  \n" + authorTextView.getText().toString();
        FavoritesFragment.removeFromFavorites(quote);
        Toast.makeText(getActivity(), getString(R.string.delete), Toast.LENGTH_SHORT).show();
    }
}
