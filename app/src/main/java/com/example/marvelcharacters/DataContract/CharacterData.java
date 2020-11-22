package com.example.marvelcharacters.DataContract;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CharacterData {
    @SerializedName("CharacterName")
    @Expose
    private String characterName;
    @SerializedName("CharacterDescription")
    @Expose
    private String characterDescription;
    @SerializedName("CharacterImage")
    @Expose
    private String characterImage;

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public String getCharacterDescription() {
        return characterDescription;
    }

    public void setCharacterDescription(String characterDescription) {
        this.characterDescription = characterDescription;
    }

    public String getCharacterImage() {
        return characterImage;
    }

    public void setCharacterImage(String characterImage) {
        this.characterImage = characterImage;
    }
}