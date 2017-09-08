import axios from 'axios';

const exchangeRatesApi = axios.create({
  baseURL: 'https://openexchangerates.org/api/latest.json?app_id=d9e1938bdcbc46698ac9eaa171513c7d'
});

export default exchangeRatesApi;