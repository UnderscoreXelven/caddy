package com.iut.caddie;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

public class AllList extends AppCompatActivity {

    private ListView allList;
    private DbAdapter bdd;
    private final List<String> list = new ArrayList<>();
    private ArrayAdapter<String> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_list);
        setTitle("Vos listes de courses");
        //Ouverture et instanciation de la BDD
        bdd = new DbAdapter(this);
        bdd.open();

        allList = findViewById(R.id.allList);

        listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,list);

        fillData();

        allList.setAdapter(listAdapter);

        allList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

            }
        });

        NavigationBarView navigationBarView = findViewById(R.id.bottom_navigation);

        navigationBarView.setOnItemSelectedListener(item -> {
            switch(item.getItemId()) {
                case(R.id.goto_main):
                    startActivity(new Intent(AllList.this, MainActivity.class));
                    return true;
                case(R.id.goto_list):
                    startActivity(new Intent(AllList.this, ListActivity.class));
                    return true;
                case(R.id.goto_allList):
                    startActivity(new Intent(AllList.this, AllList.class));
                    return true;
                case(R.id.goto_products):
                    startActivity(new Intent(AllList.this, ProductsActivity.class));
                    return true;
                default:
                    return false;
            }
        });
    }

    public void fillData(){
        Cursor c  = bdd.fetchList();
        c.moveToFirst();
        while(!c.isAfterLast()){
            list.add(c.getString(c.getColumnIndexOrThrow("list")));
            c.moveToNext();
        }
        c.close();
        listAdapter.notifyDataSetChanged();
    }
}
