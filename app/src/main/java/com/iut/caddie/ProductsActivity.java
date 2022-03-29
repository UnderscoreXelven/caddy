package com.iut.caddie;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ProductsActivity extends AppCompatActivity {

    private ListView listView;

    private final List<String> products = new ArrayList<>();

    private ArrayAdapter<String> listAdapter;

    private DbAdapter bdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        listView = findViewById(R.id.listProducts);

        bdd = new DbAdapter(this);

        bdd.open();

        fillData();

    }

    void fillData(){
        products.clear();
        Cursor c = bdd.fetchAllNotes();
        c.moveToFirst();
        while(!c.isAfterLast()){
            products.add(c.getString(c.getColumnIndexOrThrow("produit")));
            c.moveToNext();
        }
        c.close();
        listAdapter.notifyDataSetChanged();
    }
}