
export const FILTER_LANGUAGES_APPLIED = 'FILTER_LANGUAGES_APPLIED';
export const filterLanguagesApply = (filter) => ({
  type: FILTER_LANGUAGES_APPLIED,
  payload: filter
});


export const FILTER_LANGUAGES_CANCELED = 'FILTER_LANGUAGES_CANCELED';
export const filterLanguagesCancel = (filter) => ({
  type: FILTER_LANGUAGES_CANCELED,
  payload: filter
});


export const FILTER_PRICE_APPLIED = 'FILTER_PRICE_APPLIED';
export const filterPriceApply = (filter) => ({
  type: FILTER_PRICE_APPLIED,
  payload: filter
});


export const FILTER_PRICE_CANCELED = 'FILTER_PRICE_CANCELED';
export const filterPriceCancel = (filter) => ({
  type: FILTER_PRICE_CANCELED,
  payload: filter
});

export const FILTER_RATING_APPLIED = 'FILTER_RATING_APPLIED';
export const filterRatingApply = (filter) => ({
  type: FILTER_RATING_APPLIED,
  payload: filter
});


export const FILTER_RATING_CANCELED = 'FILTER_RATING_CANCELED';
export const filterRatingCancel = (filter) => ({
  type: FILTER_RATING_CANCELED,
  payload: filter
});

