package com.iut.caddie;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ProductsActivity extends AppCompatActivity {

    private ListView listView;

    private final List<String> products = new ArrayList<>();

    private ArrayAdapter<String> listAdapter;
    private boolean addProduct;

    private DbAdapter bdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        setTitle("Liste de produits");
        addProduct = getIntent().getBooleanExtra("addProduct",false);
        System.out.println(addProduct);
        listView = findViewById(R.id.listProducts);

        listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,products);

        listView.setAdapter(listAdapter);

        if(addProduct){
            registerForContextMenu(listView);
        }

        bdd = new DbAdapter(this);

        bdd.open();

        fillData();

    }

    void fillData(){
        products.clear();
        Cursor c = bdd.fetchAllProducts();
        c.moveToFirst();
        while(!c.isAfterLast()){
            products.add(c.getString(c.getColumnIndexOrThrow("produit")));
            c.moveToNext();
        }
        c.close();
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        String title = listView.getItemAtPosition(info.position).toString();
        System.out.println("Ajout du produit");
        return true;
    }
}