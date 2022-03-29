package com.iut.caddie;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class AllList extends AppCompatActivity {

    private ListView allList;
    private DbAdapter bdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Ouverture et instanciation de la BDD
        bdd = new DbAdapter(this);
        bdd.open();
    }
}
