package com.iut.caddie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private ListView listView;

    private final List<String> products = new ArrayList<>();

    private ArrayAdapter<String> listAdapter;

    private DbAdapter bdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        products.add("ketchup");
        products.add("mayo");
        products.add("moutarde");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        setTitle("La super liste de courses");

        listView = findViewById(R.id.list);

        listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,products);

        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                products.remove(position);
                listAdapter.notifyDataSetChanged();
            }
        });

        NavigationBarView navigationBarView = findViewById(R.id.bottom_navigation);

        navigationBarView.setOnItemSelectedListener(item -> {
            switch(item.getItemId()) {
                case(R.id.goto_main):
                    startActivity(new Intent(ListActivity.this, MainActivity.class));
                    return true;
                case(R.id.goto_list):
                    startActivity(new Intent(ListActivity.this, ListActivity.class));
                    return true;
                case(R.id.goto_allList):
                    startActivity(new Intent(ListActivity.this, AllList.class));
                    return true;
                case(R.id.goto_products):
                    startActivity(new Intent(ListActivity.this, ProductsActivity.class));
                    return true;
                default:
                    return false;
            }
        });

    }
}