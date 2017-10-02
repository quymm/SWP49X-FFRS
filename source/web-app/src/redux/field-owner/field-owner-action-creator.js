export function GetMatchByFieldOwnerAndDay(data){
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

export function createField(myFieldName, myFieldType, myfieldOwnerId){
    const field = {fieldName: myFieldName, fieldType: myFieldType, fieldOwnerId: myfieldOwnerId};
    return {
        type: 'CREATE_FIELD',
        payloads: field
    }
}