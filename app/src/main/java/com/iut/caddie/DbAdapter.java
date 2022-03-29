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
 * Simple notes database access helper class. Defines the basic CRUD operations
 * for the notepad example, and gives the ability to list all notes as well as
 * retrieve or modify a specific note.
 *
 * This has been improved from the first version of this tutorial through the
 * addition of better error handling and also using returning a Cursor instead
 * of using a collection of inner classes (which is less scalable and not
 * recommended).
 */
public class DbAdapter {

    public static final String KEY_TITLE = "title";
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
            "create table Commande (productsId integer primary key , "
                    + "listId integer primary key,"
                    + "quantite int not null,"
                    + " FOREIGN KEY (listId) REFERENCES Lists(_id),"
                    + " FOREIGN KEY (productsId) REFERENCES Products(_id));";

    private static final String DATABASE_NAME = "data";
    private static final String DATABASE_TABLE = "notes";
    private static final int DATABASE_VERSION = 2;
    /**
     *  Products data
     */
    private String[] Products = {"lait", "orange","chocolat","jambon","frite"};

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
     * insert produtcs data
     * @param array products data
     */
    public void createProducts(String [] array){
        for (String product: array) {
            ContentValues initialValues = new ContentValues();
            initialValues.put("produit", product);
            mDb.insert(DATABASE_TABLE, null, initialValues);
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

    public void close() {
        mDbHelper.close();
    }


    /**
     * Create a new note using the title and body provided. If the note is
     * successfully created return the new rowId for that note, otherwise return
     * a -1 to indicate failure.
     *
     * @param title the title of the note
     * @return rowId or -1 if failed
     */
    public long createNote(String title) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TITLE, title);

        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    /**
     * Delete the note with the given rowId
     *
     * @param title name of the task
     * @return true if deleted, false otherwise
     */
    public boolean deleteNote(String title) {

        return mDb.delete(DATABASE_TABLE, KEY_TITLE + "=" +"'" + title + "'", null) > 0;
    }

    public boolean deleteAllNotes() {

        return mDb.delete(DATABASE_TABLE, null, null) > 0;
    }

    /**
     * Return a Cursor over the list of all notes in the database
     *
     * @return Cursor over all notes
     */
    public Cursor fetchAllNotes() {

        return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_TITLE}, null, null, null, null, null);
    }

    /**
     * Return a Cursor positioned at the note that matches the given rowId
     *
     * @param rowId id of note to retrieve
     * @return Cursor positioned to matching note, if found
     * @throws SQLException if note could not be found/retrieved
     */
    public Cursor fetchNote(long rowId) throws SQLException {

        Cursor mCursor =

                mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                                KEY_TITLE}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    /**
     * Update the note using the details provided. The note to be updated is
     * specified using the rowId, and it is altered to use the title and body
     * values passed in
     *
     * @param rowId id of note to update
     * @param title value to set note title to
     * @return true if the note was successfully updated, false otherwise
     */
    public boolean updateNote(long rowId, String title) {
        ContentValues args = new ContentValues();
        args.put(KEY_TITLE, title);

        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
}
