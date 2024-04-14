package com.example.quotesapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Categorical_QuotesFragment extends Fragment {
    private String selectedCategoryName;
    private TextView quotesTextView;
    private TextView authorTextView;
    private ImageView categoryImageView;
    private ArrayList<String> quotesList = new ArrayList<>();
    private ArrayList<String> authorList = new ArrayList<>();
    private int currentIndex = 0;
    private boolean active = false;

    private FragmentManager fragmentManager;

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_categorical__quotes, container, false);

        quotesTextView = rootView.findViewById(R.id.C_Quotestextview);
        authorTextView = rootView.findViewById(R.id.C_authortextview);
        categoryImageView = rootView.findViewById(R.id.C_imgCategory);
        final ImageButton btnFavorite = rootView.findViewById(R.id.C_favoritebutton);
        final ImageButton btnBack = rootView.findViewById(R.id.backbutton);

        Bundle bundle = getArguments();
        if (bundle != null) {
            selectedCategoryName = bundle.getString("categoryName");
            int categoryImageId = getCategoryImageId(selectedCategoryName); // Kategoriye göre resim ID'sini al

            // Resmi değiştir
            categoryImageView = rootView.findViewById(R.id.C_imgCategory);
            categoryImageView.setImageResource(categoryImageId);
            // Kategori adını TextView'e set et
        }

        // API'den alıntıları çekme işlemi
        fetchQuotes();

        // Sağa ve sola kaydırma işlemleri
        rootView.setOnTouchListener(new OnSwipeTouchListener(requireActivity()) {
            public void onSwipeRight() {
                handleRightSwipe();
            }

            public void onSwipeLeft() {
                handleLeftSwipe();
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

        fragmentManager = getActivity().getSupportFragmentManager();

        // Geri butonuna tıklama olayı ekleme
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.content_frame, new CategoryFragment()).commit();
            }
        });

        return rootView;
    }
    private int getCategoryImageId(String categoryName) {
        switch (categoryName) {
            case "BIRTHDAY":
                return R.drawable.c_birthday;
            case "cool":
                return R.drawable.c_cool;
            case "beauty":
                return R.drawable.c_beauty;
            case "change":
                return R.drawable.c_change;
            case "death":
                return R.drawable.c_death;
            case "love":
                return R.drawable.c_love;
            case "alone":
                return R.drawable.c_alone;
            case "anger":
                return R.drawable.c_anger;
            case "best":
                return R.drawable.c_best;
            case "courage":
                return R.drawable.c_courage;
            case "dreams":
                return R.drawable.c_dreams;
            case "experiance":
                return R.drawable.c_experience;
            case "education":
                return R.drawable.c_education;
            case "equality":
                return R.drawable.c_equality;
            case "failure":
                return R.drawable.c_failure;
            case "faith":
                return R.drawable.c_failure;
            case "family":
                return R.drawable.c_family;
            case "fear":
                return R.drawable.c_fear;
            case "life":
                return R.drawable.c_life;
            // Diğer kategoriler için case'ler ekle
            default:
                return R.drawable.love; // Varsayılan resim
        }
    }

    private void fetchQuotes() {
        // API url ve anahtar
        String url = "https://api.api-ninjas.com/v1/quotes?category="+selectedCategoryName;
        String apiKey = "RnAKuJVl7LcJlxfEwcOkfw==0Dq0V084fG7bdqTZ";

        // JSON dizisi isteği oluşturma
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // API yanıtından rastgele bir alıntıyı al
                            JSONObject quoteObject = response.getJSONObject(0);
                            String quote = quoteObject.getString("quote");
                            String author = quoteObject.getString("author");

                            // Alıntıyı listeye ekleme
                            quotesList.add(quote);
                            authorList.add("- " + author);

                            // İlk alıntıyı gösterme
                            quotesTextView.setText(quote);
                            authorTextView.setText("- " + author);

                            // Favori butonunu pasif hale getir
                            active = false;
                            ImageButton btnFavorite = rootView.findViewById(R.id.C_favoritebutton); // rootView kullanılarak btnFavorite bulunuyor
                            btnFavorite.setImageResource(R.drawable.id_like);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Hata durumunda kullanıcıyı bilgilendirme
                        Toast.makeText(getActivity(), "Alıntılar getirilemedi.", Toast.LENGTH_SHORT).show();
                    }
                }) {
            // İsteğe başlık ekleme (API anahtarı)
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("X-Api-Key", apiKey);
                return headers;
            }
        };

        // Volley ile isteği kuyruğa ekleme ve başlatma
        Volley.newRequestQueue(requireContext()).add(jsonArrayRequest);
    }

    private void handleLeftSwipe() {
        // Sola kaydırma işlemi için method
        currentIndex++; // İndisi bir arttır (sonraki al
        if (currentIndex >= quotesList.size()) {
            // Liste sonunalaşıldıysa yeni alıntı çek
            fetchQuotes();
        } else {
            // Listeden sonraki alıntıyı göster
            String quote = quotesList.get(currentIndex);
            String author = authorList.get(currentIndex);
            quotesTextView.setText(quote);
            authorTextView.setText(author);

            // Favori kontrolü yap
            checkFavorite(quote + "  \n" + author);
        }
    }

    private void handleRightSwipe() {
        // Sağa kaydırma işlemi için method
        if (currentIndex > 0) {
            currentIndex--; // İndisi bir azalt (önceki alıntıyı göstermek için)
            String quote = quotesList.get(currentIndex);
            String author = authorList.get(currentIndex);
            quotesTextView.setText(quote);
            authorTextView.setText(author);

            // Favori kontrolü yap
            checkFavorite(quote + "  \n" + author);
        }
    }

    private void addToFavorites() {
        String quote = quotesTextView.getText().toString() + "  \n" + authorTextView.getText().toString();
        FavoritesFragment.addToFavorites(quote);
        Toast.makeText(getActivity(), "Söz favorilere eklendi.", Toast.LENGTH_SHORT).show();
    }

    private void removeFromFavorites() {
        String quote = quotesTextView.getText().toString() + "  \n" + authorTextView.getText().toString();
        FavoritesFragment.removeFromFavorites(quote);
        Toast.makeText(getActivity(), "Söz favorilerden kaldırıldı.", Toast.LENGTH_SHORT).show();
    }

    private void checkFavorite(final String quote) {
        // Veritabanından favori alıntıları yüklemek için DBHelper nesnesini kullan
        FavoritesFragment.DBHelper dbHelper = new FavoritesFragment.DBHelper(requireContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM favorites WHERE quote = ?", new String[]{quote});

        boolean isActive = false;

        // Eğer cursor'da veri varsa, metin favorilerde bulunuyor demektir
        if (cursor.getCount() > 0) {
            isActive = true;
        }

        cursor.close();
        db.close();

        // Favori butonunun görünümünü güncelle
        ImageButton btnFavorite = rootView.findViewById(R.id.C_favoritebutton);
        if (isActive) {
            btnFavorite.setImageResource(R.drawable.id_likeaktif);
            active=true;
        } else {
            btnFavorite.setImageResource(R.drawable.id_like);
            active=false;
        }
    }


}