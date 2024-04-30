package com.example.quotesapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.Locale;

public class CategoryFragment extends Fragment {
    private DrawerLayout drawerLayout;
    private FragmentManager fragmentManager;
    private String selectedCategoryName; // Global değişken olarak seçilen kategori adını sakla

    private View rootView;
    ListView lsvCategory;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_category, container, false);

        lsvCategory = rootView.findViewById(R.id.lsvCategory);

        ArrayList<Category> arr = new ArrayList<>();
        arr.add(new Category(R.drawable.c_alone, getString(R.string.alone), "G.O.A.T."));
        arr.add(new Category(R.drawable.c_anger, getString(R.string.anger), "G.O.A.T."));
        arr.add(new Category(R.drawable.c_beauty, getString(R.string.beauty), "Everything has beauty, but not everyone sees it."));
        arr.add(new Category(R.drawable.c_best, getString(R.string.best), "G.O.A.T."));
        arr.add(new Category(R.drawable.c_birthday, getString(R.string.birth), "Age is just a number."));
        arr.add(new Category(R.drawable.c_change, getString(R.string.change), "Change is the only constant."));
        arr.add(new Category(R.drawable.c_cool, getString(R.string.cool), "G.O.A.T.")); //GOAT
        arr.add(new Category(R.drawable.c_courage, getString(R.string.courage), "G.O.A.T."));
        arr.add(new Category(R.drawable.c_death, getString(R.string.death), "Everybody is going to be dead one day, just give them time."));
        arr.add(new Category(R.drawable.c_dreams, getString(R.string.dreams), "G.O.A.T."));
        arr.add(new Category(R.drawable.c_experience, getString(R.string.experiance), "G.O.A.T."));
        arr.add(new Category(R.drawable.c_education, getString(R.string.education), "G.O.A.T."));
        arr.add(new Category(R.drawable.c_equality, getString(R.string.equality), "G.O.A.T."));
        arr.add(new Category(R.drawable.c_failure, getString(R.string.failure), "G.O.A.T."));
        arr.add(new Category(R.drawable.c_faith, getString(R.string.faith), "G.O.A.T."));
        arr.add(new Category(R.drawable.c_family, getString(R.string.family), "G.O.A.T."));
        arr.add(new Category(R.drawable.c_fear, getString(R.string.fear), "G.O.A.T."));
        arr.add(new Category(R.drawable.c_life, getString(R.string.life), "G.O.A.T."));
        arr.add(new Category(R.drawable.c_love, getString(R.string.love), "Love is feeling alive."));

        CategoryAdapter adapter = new CategoryAdapter(getContext(), 0, arr);
        lsvCategory.setAdapter(adapter);

// İlk olarak FragmentManager'ı başlatın
        fragmentManager = getActivity().getSupportFragmentManager();

        lsvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Kategoriye tıklandığında yapılacak işlemler
                Category selectedCategory = arr.get(position);
                selectedCategoryName = selectedCategory.getName(); // Global değişkene kategori adını ata
                if (Locale.getDefault().getLanguage().equals("tr")) {
                    switch (selectedCategoryName) {
                        case "Doğum Günü":
                            selectedCategoryName = "BIRTHDAY";
                            break;
                        case "Havalı":
                            selectedCategoryName = "cool";
                            break;
                        case "Güzellik":
                            selectedCategoryName = "beauty";
                            break;
                        case "Değişim":
                            selectedCategoryName = "change";
                            break;
                        case "Ölüm":
                            selectedCategoryName = "death";
                            break;
                        case "Aşk":
                            selectedCategoryName = "love";
                            break;
                        case "Yalnızlık":
                            selectedCategoryName = "alone";
                            break;
                        case "Öfke":
                            selectedCategoryName = "anger";
                            break;
                        case "En İyi":
                            selectedCategoryName = "best";
                            break;
                        case "Cesaret":
                            selectedCategoryName = "courage";
                            break;
                        case "Hayaller":
                            selectedCategoryName = "dreams";
                            break;
                        case "Deneyim":
                            selectedCategoryName = "experience";
                            break;
                        case "Eğitim":
                            selectedCategoryName = "education";
                            break;
                        case "Eşitlik":
                            selectedCategoryName = "equality";
                            break;
                        case "Başarısızlık":
                            selectedCategoryName = "failure";
                            break;
                        case "İnanç":
                            selectedCategoryName = "faith";
                            break;
                        case "Aile":
                            selectedCategoryName = "family";
                            break;
                        case "Korku":
                            selectedCategoryName = "fear";
                            break;
                        case "Hayat":
                            selectedCategoryName = "life";
                            break;
                        default:
                            // Varsayılan olarak İngilizce adıyla devam et
                            break;
                    }
                }
                // Categorical_QuotesFragment'i oluştururken kategori adını parametre olarak iletmek
                Categorical_QuotesFragment categoricalQuotesFragment = new Categorical_QuotesFragment();
                Bundle bundle = new Bundle();
                bundle.putString("categoryName", selectedCategoryName);
                categoricalQuotesFragment.setArguments(bundle);
                // Fragment container'a ekle
                if (categoricalQuotesFragment != null) {
                    fragmentManager.beginTransaction().replace(R.id.content_frame, categoricalQuotesFragment).commit();
                }

            }
        });


        return rootView;
    }
}