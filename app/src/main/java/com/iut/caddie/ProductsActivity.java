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
        setTitle("Liste de produits");

        listView = findViewById(R.id.listProducts);

        listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,products);

        listView.setAdapter(listAdapter);

        bdd = new DbAdapter(this);

        bdd.open();

        fillData();

    }

    void fillData(){
        products.clear();
        Cursor c = bdd.fetchAllProducts();
        c.moveToFirst();
        while(!c.isAfterLast()){
            System.out.println("Coucou :)");
            products.add(c.getString(c.getColumnIndexOrThrow("produit")));
            c.moveToNext();
        }
        c.close();
        System.out.println(products);
        listAdapter.notifyDataSetChanged();
    }
}