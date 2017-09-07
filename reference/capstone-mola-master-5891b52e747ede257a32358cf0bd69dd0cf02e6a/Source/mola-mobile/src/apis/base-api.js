export default class BaseAPI {
  constructor(token) {
    this.token = token;
  }

  getToken(){
    return this.token;
  }
}