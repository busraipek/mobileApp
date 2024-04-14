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
        arr.add(new Category(R.drawable.c_alone, "alone", "G.O.A.T."));
        arr.add(new Category(R.drawable.c_anger, "anger", "G.O.A.T."));
        arr.add(new Category(R.drawable.c_beauty, "beauty", "Everything has beauty, but not everyone sees it."));
        arr.add(new Category(R.drawable.c_best, "best", "G.O.A.T."));
        arr.add(new Category(R.drawable.c_birthday, "BIRTHDAY", "Age is just a number."));
        arr.add(new Category(R.drawable.c_change, "change", "Change is the only constant."));
        arr.add(new Category(R.drawable.c_cool, "cool", "G.O.A.T.")); //GOAT
        arr.add(new Category(R.drawable.c_courage, "courage", "G.O.A.T."));
        arr.add(new Category(R.drawable.c_death, "death", "Everybody is going to be dead one day, just give them time."));
        arr.add(new Category(R.drawable.c_dreams, "dreams", "G.O.A.T."));
        arr.add(new Category(R.drawable.c_experience, "experience", "G.O.A.T."));
        arr.add(new Category(R.drawable.c_education, "education", "G.O.A.T."));
        arr.add(new Category(R.drawable.c_equality, "equality", "G.O.A.T."));
        arr.add(new Category(R.drawable.c_failure, "failure", "G.O.A.T."));
        arr.add(new Category(R.drawable.c_faith, "faith", "G.O.A.T."));
        arr.add(new Category(R.drawable.c_family, "family", "G.O.A.T."));
        arr.add(new Category(R.drawable.c_fear, "fear", "G.O.A.T."));
        arr.add(new Category(R.drawable.c_life, "life", "G.O.A.T."));
        arr.add(new Category(R.drawable.c_love, "love", "Love is feeling alive."));

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