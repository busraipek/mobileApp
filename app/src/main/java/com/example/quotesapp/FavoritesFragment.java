package com.example.quotesapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class FavoritesFragment extends Fragment {

    private ListView listView;
    private static ArrayAdapter<String> adapter;
    private static ArrayList<String> favoriteQuotes;
    private static DBHelper dbHelper;
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_favorites, container, false);

        listView = rootView.findViewById(R.id.favoritesListView);
        favoriteQuotes = new ArrayList<>();
        adapter = new ArrayAdapter<>(requireContext(), R.layout.list_item, favoriteQuotes);
        listView.setAdapter(adapter);


        // Uzun tıklama olayı dinleyicisi ekleme
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showDeleteDialog(position);
                return true;
            }
        });

        // DBHelper nesnesini oluştur ve favori alıntıları yükle
        dbHelper = new DBHelper(requireContext());
        loadFavoriteQuotes();

        return rootView;
    }

    // Veritabanından favori alıntıları yükleyen metot
    private void loadFavoriteQuotes() {
        favoriteQuotes.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM favorites", null);
        if (cursor.moveToFirst()) {
            do {
                String quote = cursor.getString(cursor.getColumnIndexOrThrow("quote"));
                favoriteQuotes.add(quote);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        adapter.notifyDataSetChanged();
    }

    public static void addToFavorites(String quote) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("quote", quote);
        db.insert("favorites", null, values);
        db.close();
        favoriteQuotes.add(quote);
        adapter.notifyDataSetChanged();
    }

    public static void removeFromFavorites(String quote) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("favorites", "quote = ?", new String[]{quote});
        db.close();
        favoriteQuotes.remove(quote);
        adapter.notifyDataSetChanged();
    }

    // Uzun tıklama sonrası silme işlemi için iletişim kutusu gösterme
    private void showDeleteDialog(final int position) {
        String quote = favoriteQuotes.get(position);
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(requireContext());
        builder.setTitle("Aşağıdaki Alıntıyı Silmek istediğinize emin misiniz?")
                .setMessage(quote)
                .setPositiveButton("Sil", (dialog, which) -> {
                    String deletedQuote = favoriteQuotes.remove(position);
                    adapter.notifyDataSetChanged();
                    removeFromFavorites(deletedQuote);
                    Toast.makeText(requireContext(), "Alıntı silindi", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("İptal", null)
                .create()
                .show();
    }

    // DBHelper sınıfı, SQLite veritabanı işlemlerini yönetir
    static class DBHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "FavoritesDB";
        private static final int DATABASE_VERSION = 1;

        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE favorites (_id INTEGER PRIMARY KEY AUTOINCREMENT, quote TEXT)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS favorites");
            onCreate(db);
        }
    }
}
