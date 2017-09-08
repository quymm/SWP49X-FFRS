import exchangeRatesApi from '../apis/exchange-rates-api';

class ExchangeRates {
    static EXCHANGERATES;

    static async loadApi() {
        this.EXCHANGERATES = await exchangeRatesApi.get().then(result => {
            return result.data.rates;
        })
    }
}

export default ExchangeRates;