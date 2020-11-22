package com.example.marvelcharacters.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.marvelcharacters.DataContract.CharacterData;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "characterlistmanager";
    private static final String TABLE_ADDED_CHARACTERS = "addedcharacters";
    private static final String TABLE_CAN_BE_ADDED_CHARACTERS = "canbeaddedcharacters";

    private static final String character_name = "character_name";
    private static final String character_description = "character_description";
    private static final String character_image = "character_image";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            String CREATE_ADDED_CHARACTERS_TABLE = "CREATE TABLE " + TABLE_ADDED_CHARACTERS + "("
                    + character_name + " TEXT,"
                    + character_description + " TEXT,"
                    + character_image + " TEXT" + ")";
            sqLiteDatabase.execSQL(CREATE_ADDED_CHARACTERS_TABLE);

            String CAN_BE_ADDED_CHARACTERS_TABLE = "CREATE TABLE " + TABLE_CAN_BE_ADDED_CHARACTERS + "("
                    + character_name + " TEXT,"
                    + character_description + " TEXT,"
                    + character_image + " TEXT" + ")";
            sqLiteDatabase.execSQL(CAN_BE_ADDED_CHARACTERS_TABLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        try {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ADDED_CHARACTERS);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CAN_BE_ADDED_CHARACTERS);

            // Create tables again
            onCreate(sqLiteDatabase);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addCharacter(CharacterData characterData, String tableName) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(character_name, characterData.getCharacterName());
            values.put(character_description, characterData.getCharacterDescription());
            values.put(character_image, characterData.getCharacterImage());

            if (tableName.equals(TABLE_ADDED_CHARACTERS))
                db.insert(TABLE_ADDED_CHARACTERS, null, values);
            else
                db.insert(TABLE_CAN_BE_ADDED_CHARACTERS, null, values);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<CharacterData> getAllCharacters(String tableName) {
        List<CharacterData> characterDataList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + tableName;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                CharacterData characterData = new CharacterData();
                characterData.setCharacterName(cursor.getString(0));
                characterData.setCharacterDescription(cursor.getString(1));
                characterData.setCharacterImage(cursor.getString(2));
                characterDataList.add(characterData);
            } while (cursor.moveToNext());
        }

        return characterDataList;
    }

    public void deleteCharacter(CharacterData characterData, String tableName) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            if (tableName.equals(TABLE_ADDED_CHARACTERS))
                db.delete(TABLE_ADDED_CHARACTERS, character_name + " = ?",
                        new String[]{String.valueOf(characterData.getCharacterName())});
            else
                db.delete(TABLE_CAN_BE_ADDED_CHARACTERS, character_name + " = ?",
                        new String[]{String.valueOf(characterData.getCharacterName())});

            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}