package com.iut.caddie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NavigationBarView navigationBarView = findViewById(R.id.bottom_navigation);

        navigationBarView.setOnItemSelectedListener(item -> {
            switch(item.getItemId()) {
                case(R.id.goto_main):
                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                    return true;
                case(R.id.goto_list):
                    startActivity(new Intent(MainActivity.this, ListActivity.class));
                    return true;
                case(R.id.goto_allList):
                    startActivity(new Intent(MainActivity.this, AllList.class));
                    return true;
                case(R.id.goto_products):
                    startActivity(new Intent(MainActivity.this, ProductsActivity.class));
                    return true;
                default:
                    return false;
            }
        });
    }
}