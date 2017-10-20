// import { REHYDRATE } from 'redux-persist/constants';

const defaultState = {
  user: {
    data: {   
    },
    role: undefined,
    // isFetched: false,
    status: {
      ok: false,
      error: false,
      message: '',
    },
  },
};
export const guestReducer = (state = defaultState, action) => {
  switch (action.type) {
    case 'LOGIN_SUCCESSFUL':
      return {
        user: {
          data: action.payloads,
          // isFetched: true,
          role: action.payloads.roleId.roleName,
          status: {
            ok: true,
            error: false,
            message: null,
          },
        },
      };
    // case 'REGISTER_SUCCESSFUL':
    //     return{ registerAccount: action.payloads };
    case 'LOGIN_ERROR':
      return {
        user: {
          data: null,
          role: 1,
          status: {
            ok: false,
            error: true,
            message: action.payloads,
          },
        },
      };
    case 'ACCESS_DENIED':
      return {user: {
        data: null,
        role: 0,
        status: {
          ok: false,
          error: true,
          message: 'Truy cập bị từ chối',
        }
      }};
      case 'LOG_OUT': 
        localStorage.removeItem('auth');
        return {
          user: {
            data: {id: undefined},
            role: 0,
            status: {
              ok: false,
              error: true,
              message: 'Đăng xuất thành công',
            }
          }
          
        }
    default:
      return state;
  }
};

export default guestReducer;
