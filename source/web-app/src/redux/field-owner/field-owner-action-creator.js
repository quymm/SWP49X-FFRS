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