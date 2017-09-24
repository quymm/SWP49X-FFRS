export function getMatchByDay(data){
    return {
        type: 'GET_MATCH_BY_DAY',
        payloads: data
    }
}

export function getAllField(data){
    return {
        type: 'GET_ALL_FIELD',
        payloads: data
    }
}
export function deleteField(fieldId){
    return {
        type: 'DELETE_FIELD',
        payloads: fieldId
    }
}

export function createField(myFieldName, myFieldType){
    const field = {fieldName: myFieldName, fieldType: myFieldType};
    return {
        type: 'CREATE_FIELD',
        payloads: field
    }
}