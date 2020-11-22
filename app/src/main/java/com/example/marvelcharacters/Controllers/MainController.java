package com.example.marvelcharacters.Controllers;

import android.content.Context;

import com.example.marvelcharacters.DataContract.CharacterData;
import com.example.marvelcharacters.Database.DatabaseHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainController {

    public List<CharacterData> getMyCharacterData(Context context) {
        List<CharacterData> result = new ArrayList<CharacterData>();

        try {
            DatabaseHelper db = new DatabaseHelper(context);
            if (db != null) {
                List<CharacterData> addedCharactersList = db.getAllCharacters("addedcharacters");
                List<CharacterData> canBeAddedCharactersList = db.getAllCharacters("canbeaddedcharacters");
                if (addedCharactersList != null && canBeAddedCharactersList != null) {
                    if (addedCharactersList.size() == 0 && canBeAddedCharactersList.size() == 0) {
                        // Take data from Json File
                        String characterListJson = getCharacterListFromJson(context, "charactersdata.json");
                        Gson gson = new Gson();
                        Type listCharacterDataType = new TypeToken<List<CharacterData>>() {
                        }.getType();
                        List<CharacterData> characterDataList = gson.fromJson(characterListJson, listCharacterDataType);

                        //Add six characters to added table and remaining to can be added table
                        for (int i = 0; i < characterDataList.size(); i++) {
                            if (i < 6) {
                                result.add(characterDataList.get(i));
                                db.addCharacter(characterDataList.get(i), "addedcharacters");
                            } else {
                                db.addCharacter(characterDataList.get(i), "canbeaddedcharacters");
                            }
                        }
                    } else {
                        if (addedCharactersList.size() > 0) {
                            result = addedCharactersList;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getCharacterListFromJson(Context context, String fileName) {
        String jsonString;
        try {
            InputStream is = context.getAssets().open(fileName);

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            jsonString = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return jsonString;
    }

}
