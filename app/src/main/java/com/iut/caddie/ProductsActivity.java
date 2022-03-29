package com.iut.caddie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

public class ProductsActivity extends AppCompatActivity {

    private ListView listView;
<<<<<<< Updated upstream

    private EditText editText;

=======
    private String name = "";
>>>>>>> Stashed changes
    private final List<String> products = new ArrayList<>();

    private ArrayAdapter<String> listAdapter;
    private boolean addProduct;

    private DbAdapter bdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        setTitle("Liste de produits");

        final Button btn= findViewById(R.id.button);
        editText = findViewById(R.id.editText);
        addProduct = getIntent().getBooleanExtra("addProduct",false);
        System.out.println(addProduct);
        listView = findViewById(R.id.listProducts);

        listView = findViewById(R.id.listProducts);
        listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,products);
        listView.setAdapter(listAdapter);

        if(addProduct){
            registerForContextMenu(listView);
            name = getIntent().getStringExtra("listName");
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    String title = products.get(position);
                    System.out.println("Ajout du produit "+title+" pour la liste "+name);
                }
            });

        }

        bdd = new DbAdapter(this);
        bdd.open();
        fillData();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bdd.createProduct(editText.getText().toString());
                fillData();
                listAdapter.notifyDataSetChanged();
            }
        });
        NavigationBarView navigationBarView = findViewById(R.id.bottom_navigation);

        navigationBarView.setOnItemSelectedListener(item -> {
            switch(item.getItemId()) {
                case(R.id.goto_allList):
                    startActivity(new Intent(ProductsActivity.this, AllList.class));
                    return true;
                case(R.id.goto_products):
                    startActivity(new Intent(ProductsActivity.this, ProductsActivity.class));
                    return true;
                default:
                    return false;
            }
        });

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

}