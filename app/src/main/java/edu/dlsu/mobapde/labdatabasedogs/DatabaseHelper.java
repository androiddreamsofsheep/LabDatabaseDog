package edu.dlsu.mobapde.labdatabasedogs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by G301 on 11/7/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {


    public static final String SCHEMA = "asian";
    public static final int VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, SCHEMA, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // ROLE: create the tables for the schema
        // It will only be called once by the system
        // -- when the schema with given name doesn't exist yet

        String sql = "CREATE TABLE " + Dog.TABLE_NAME + " ("
                + Dog.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Dog.COLUMN_NAME + " TEXT,"
                + Dog.COLUMN_AGE + " INTEGER,"
                + Dog.COLUMN_BREED + " TEXT"
                + ");";
        sqLiteDatabase.execSQL(sql);

        addDog(new Dog("Jorge", 10, "Puli"), sqLiteDatabase);
        addDog(new Dog("Adam", 6, "Schnauzer"), sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase,
                          int i, int i1) {
        // ROLE: update the current schema
        // Will be called when version number is newer/higher

        // migration
        // drop current tables
        String sql = "DROP TABLE IF EXISTS " + Dog.TABLE_NAME + ";";
        sqLiteDatabase.execSQL(sql);
        // call onCreate
        onCreate(sqLiteDatabase);
    }

    // addDog
    public long addDog(Dog dog){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Dog.COLUMN_NAME, dog.getName());
        contentValues.put(Dog.COLUMN_AGE, dog.getAge());
        contentValues.put(Dog.COLUMN_BREED, dog.getBreed());

        long id = db.insert(Dog.TABLE_NAME,
                null,
                contentValues);
        db.close();
        return id;
    }

    // addDog
    public long addDog(Dog dog, SQLiteDatabase db){

        ContentValues contentValues = new ContentValues();
        contentValues.put(Dog.COLUMN_NAME, dog.getName());
        contentValues.put(Dog.COLUMN_AGE, dog.getAge());
        contentValues.put(Dog.COLUMN_BREED, dog.getBreed());

        long id = db.insert(Dog.TABLE_NAME,
                null,
                contentValues);
        db.close();
        return id;
    }

    // editDog
    public boolean editDog(Dog newDogDetails, int currentId){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Dog.COLUMN_NAME, newDogDetails.getName());
        contentValues.put(Dog.COLUMN_AGE, newDogDetails.getAge());
        contentValues.put(Dog.COLUMN_BREED, newDogDetails.getBreed());

        int rowsAffected = db.update(Dog.TABLE_NAME,
                contentValues,
                Dog.COLUMN_ID + "=?",
                new String[]{newDogDetails.getId()+""});
        db.close();

        return rowsAffected >0;
    }

    // deleteDog
    public boolean deleteDog(long id){
        SQLiteDatabase db = getWritableDatabase();
        int rowsAffected = db.delete(Dog.TABLE_NAME,
                Dog.COLUMN_ID + "=?",
                new String[]{id+""} );
        db.close();
        return rowsAffected >0;
    }

    // getDog
    public Dog getDog(long id){
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(Dog.TABLE_NAME,
                null,
                Dog.COLUMN_ID + "=?",
                new String[]{ id+"" },
                null,
                null,
                null);
        Dog dog = null;
        if(c.moveToFirst()){
            dog = new Dog();
            dog.setName(c.getString(c.getColumnIndex(Dog.COLUMN_NAME)));
            dog.setAge(c.getInt(c.getColumnIndex(Dog.COLUMN_AGE)));
            dog.setBreed(c.getString(c.getColumnIndex(Dog.COLUMN_BREED)));
            dog.setId(id);
        }

        c.close();
        db.close();

        return dog;
    }

    // getAllDogs
    public Cursor getAllDogsCursor(){
        return getReadableDatabase().query(Dog.TABLE_NAME, null,null,null,null,null,null);
    }
}






