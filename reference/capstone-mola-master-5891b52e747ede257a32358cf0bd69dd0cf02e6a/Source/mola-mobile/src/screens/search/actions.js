import SearchAPI  from '../../apis/search';
const search = new SearchAPI();

export const SEARCH_COURSE_NAME = 'SEARCH_COURSE_NAME';
export const searchCourse = (name) => ({
  type: SEARCH_COURSE_NAME,
  payload: search.searchCourse(name)
});


export const SEARCH_FILTER_APPLIED = 'SEARCH_FILTER_APPLIED';
export const searchFilterApply = (newResult) => ({
  type: SEARCH_FILTER_APPLIED,
  payload: newResult
});

export const SEARCH_FILTER_CANCEL = 'SEARCH_FILTER_CANCEL';
export const searchFilterCancel = () => ({
  type: SEARCH_FILTER_CANCEL,
});



