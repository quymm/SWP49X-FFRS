package com.dto;

/**
 * Created by MinhQuy on 9/24/2017.
 */
public class InputFieldTypeDTO {
    private String name;

    private String numberPlayer;

    private String description;

    public InputFieldTypeDTO() {
    }

    public InputFieldTypeDTO(String name, String numberPlayer, String description) {
        this.name = name;
        this.numberPlayer = numberPlayer;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumberPlayer() {
        return numberPlayer;
    }

    public void setNumberPlayer(String numberPlayer) {
        this.numberPlayer = numberPlayer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
