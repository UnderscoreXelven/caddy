package com.iut.caddie;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

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
        setContentView(R.layout.activity_main);

        //Ouverture et instanciation de la BDD
        bdd = new DbAdapter(this);
        bdd.open();

        listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,list);

        fillData();

        allList.setAdapter(listAdapter);

        allList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

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
