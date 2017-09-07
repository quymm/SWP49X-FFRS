
export const SET_NEXT_UPCOMING_SESSION = 'SET_NEXT_UPCOMING_SESSION';
export const setNextUpcomingSession = ({ session, hasNextSession }) => ({
  type: SET_NEXT_UPCOMING_SESSION,
  payload: {session, hasNextSession}
});

export const SET_NO_UPCOMING_SESSION = 'SET_NO_UPCOMING_SESSION';
export const setNoUpcomingSession = () => ({
  type: SET_NO_UPCOMING_SESSION,
});