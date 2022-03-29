/*
 * Copyright (C) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.iut.caddie;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * dbAdapter create and edit database
 */
public class DbAdapter {

    public static final String KEY_TITLE = "produit";
    public static final String KEY_ROWID = "_id";

    private static final String TAG = "NotesDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    /**
     * Database creation sql statement
     */
    private static final String DATABASE_CREATE_PRODUCTS =
            "create table Products (_id integer primary key autoincrement, "
                    + "produit text not null);";

    private static final String DATABASE_CREATE_LISTS =
            "create table Lists (_id integer primary key autoincrement, "
                    + "list text not null);";

    private static final String DATABASE_CREATE_COMMANDE =
            "create table Commande (productsId integer, "
                    + "listId integer,"
                    + "quantite int not null,"
                    + " FOREIGN KEY (listId) REFERENCES Lists(_id),"
                    + " FOREIGN KEY (productsId) REFERENCES Products(_id),"
                    + " PRIMARY KEY (productsId,listId));";

    private static final String DATABASE_NAME = "data";
    private static final String DATABASE_TABLE = "Products";
    private static final int DATABASE_VERSION = 2;
    /**
     *  Products data start
     */
    private static String[] Products = {"lait", "orange","chocolat","jambon","frite"};

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(DATABASE_CREATE_PRODUCTS);
            db.execSQL(DATABASE_CREATE_LISTS);
            db.execSQL(DATABASE_CREATE_COMMANDE);
            for (String product: Products) {
                ContentValues initialValues = new ContentValues();
                initialValues.put("produit", product);
                db.insert(DATABASE_TABLE, null, initialValues);
            }
            String row = "INSERT INTO Lists values(1,'Auchan');";
            db.execSQL(row);
            db.execSQL("INSERT INTO Commande values(1,1,2)");
            db.execSQL("INSERT INTO Commande values(2,1,3)");
            db.execSQL("INSERT INTO Commande values(3,1,1)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS notes");
            onCreate(db);
        }
    }
    /**
     * create new list
     * @param listName
     */
    public void createLists(String listName){
        ContentValues initialValues = new ContentValues();
        initialValues.put("list", listName);
        mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    public void createProduct(String productName){
        ContentValues initialValues = new ContentValues();
        initialValues.put("Produit", productName);
        mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    public boolean deleteProduct(String productName){
        return mDb.delete(DATABASE_TABLE, KEY_TITLE + "=" +"'" + productName + "'", null) > 0;
    }

    /**
     * create new commande on list with products
     * @param productsId
     * @param listId
     * @param quantite
     */
    public void createCommande(int productsId,int listId,int quantite){
        ContentValues initialValues = new ContentValues();
        initialValues.put("productsId", productsId);
        mDb.insert(DATABASE_TABLE, null, initialValues);
        initialValues.put("listId", listId);
        mDb.insert(DATABASE_TABLE, null, initialValues);
        initialValues.put("quantite", quantite);
        mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    /**
     * Retourne l'id et le nom des listes de courses
     * @return
     */
    public Cursor fetchList(){
        return mDb.query("Lists", new String[] {"_id", "list"}, null, null, null, null, null);
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     *
     * @param ctx the Context within which to work
     */
    public DbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the notes database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     *
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public DbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    /**
     * close acces to the database
     */
    public void close() {
        mDbHelper.close();
    }

    /**
     * Return a Cursor over the list of all notes in the database
     *
     * @return Cursor over all notes
     */
    public Cursor fetchAllProducts() {
        return mDb.query(DATABASE_TABLE, new String[] {"_id", "produit"}, null, null, null, null, "produit");
    }

    /**
     * Retourne les éléments d'une liste de course
     * @param listName nom d'une liste
     */
    public Cursor commandList(String listName){
         return mDb.rawQuery("SELECT P.produit, C.quantite " +
                "FROM Products AS P INNER JOIN Commande AS C ON P._id = C.productsId INNER JOIN Lists AS L ON C.listId = L._id" +
                " WHERE L.list LIKE ?", new String[] {listName});
    }
}
