import axios from 'axios';

const geoDataApi = axios.create({
  baseURL: 'http://freegeoip.net/json/'
});

export default geoDataApi;