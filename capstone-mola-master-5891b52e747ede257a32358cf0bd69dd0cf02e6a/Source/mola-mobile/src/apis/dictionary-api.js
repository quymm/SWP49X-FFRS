import axios from 'axios';

const url = 'https://glosbe.com';

var from, dest, phrase;

const DictionaryAPI = axios.create({
  baseURL: url,
});

export default DictionaryAPI;