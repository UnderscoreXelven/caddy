package com.iut.caddie;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.list);

        listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,products);

        listView.setAdapter(listAdapter);

    }
}