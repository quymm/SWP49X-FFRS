package com.dto;

/**
 * Created by MinhQuy on 9/24/2017.
 */
public class InputFieldTypeDTO {
    private String name;

    private String numberPlayer;

    public InputFieldTypeDTO() {
    }

    public InputFieldTypeDTO(String name, String numberPlayer) {
        this.name = name;
        this.numberPlayer = numberPlayer;
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

}
