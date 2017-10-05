
package com.dto;

/**
 * Created by MinhQuy on 9/24/2017.
 */
public class InputFieldDTO {
    private String fieldName;

    private Integer fieldOwnerId;

    private Integer fieldTypeId;

    public InputFieldDTO() {
    }

    public InputFieldDTO(String fieldName, Integer fieldOwnerId, Integer fieldTypeId) {
        this.fieldName = fieldName;
        this.fieldOwnerId = fieldOwnerId;
        this.fieldTypeId = fieldTypeId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Integer getFieldOwnerId() {
        return fieldOwnerId;
    }

    public void setFieldOwnerId(Integer fieldOwnerId) {
        this.fieldOwnerId = fieldOwnerId;
    }

    public Integer getFieldTypeId() {
        return fieldTypeId;
    }

    public void setFieldTypeId(Integer fieldTypeId) {
        this.fieldTypeId = fieldTypeId;
    }
}

