import { 
    BASE_URL, 
    GET_MATCH_BY_DAY,
    ADD_FIELD,
    REMOVE_FIELD,
    CHECK_TIME_SLOT,

    } from './base-URL';

export function fetchMatchByDay(paramDay){
    return fetch(BASE_URL+GET_MATCH_BY_DAY+'&day='+paramDay)
    .then(res => res.json());
}

export function fetchAddField(paramFieldName, paramFieldType){
    return fetch(BASE_URL+ADD_FIELD+'&'+paramFieldName+'&'+paramFieldType)
    .then(res => res.status);
}

export function fetchRemoveField(paramFieldId){
    return fetch(BASE_URL+REMOVE_FIELD+'&fieldId'+paramFieldId)
    .then(res => res.status);
}

export function fetchCheckTimeSlotStatus(paramFieldOwnerId, paramDay, paramduration, paramFieldType){
    return fetch(BASE_URL+CHECK_TIME_SLOT
        +'&fieldOwnerId'+paramFieldOwnerId
        +'&day'+paramDay
        +'&duration'+paramduration
        +'&fieldType'+paramFieldType)
        .then(res => res.status);
}
