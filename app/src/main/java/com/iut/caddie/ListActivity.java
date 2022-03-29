package com.iut.caddie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private ListView listView;

    private List<String> products = new ArrayList<>();
    private String name;

    private ArrayAdapter<String> listAdapter;

    private DbAdapter bdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        products = getIntent().getStringArrayListExtra("listProduits");
        name = getIntent().getStringExtra("listName");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        setTitle(name);

        listView = findViewById(R.id.list);

        listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,products);

        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                return;
            }
        });

        NavigationBarView navigationBarView = findViewById(R.id.bottom_navigation);

        navigationBarView.setOnItemSelectedListener(item -> {
            switch(item.getItemId()) {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        final MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        final Intent intent = new Intent(ListActivity.this, ProductsActivity.class);
        intent.putExtra("addProduct",true);
        intent.putExtra("listName",name);
        startActivity(intent);
        return true;
    }

}
