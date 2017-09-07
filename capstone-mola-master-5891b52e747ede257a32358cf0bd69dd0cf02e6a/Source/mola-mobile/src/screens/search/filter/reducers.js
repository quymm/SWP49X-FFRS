import {
  FILTER_LANGUAGES_APPLIED,
  FILTER_LANGUAGES_CANCELED,
  FILTER_PRICE_APPLIED,
  FILTER_PRICE_CANCELED,
  FILTER_RATING_APPLIED,
  FILTER_RATING_CANCELED
} from './actions';

const INITIAL_STATE = {
  filterBy: {
    languages: [],
    price: {},
    rating: -1
  },
}

export default(state = INITIAL_STATE, action) => {
  switch (action.type) {
    case `${FILTER_LANGUAGES_APPLIED}`:
      return {
        filterBy: {
          ...action.payload,
        },
      }
    case `${FILTER_LANGUAGES_CANCELED}`:
      return {
        filterBy: {
          ...action.payload,
        },
      }
    case `${FILTER_PRICE_APPLIED}`:
      return {
        filterBy: {
          ...action.payload,
        },
      }
    case `${FILTER_PRICE_CANCELED}`:
      return {
        filterBy: {
          ...action.payload,
        },
      }
    case `${FILTER_RATING_APPLIED}`:
      return {
        filterBy: {
          ...action.payload,
        },
      }
    case `${FILTER_RATING_CANCELED}`:
      return {
        filterBy: {
          ...action.payload,
        },
      }
    default:
      return state;
  }

}
