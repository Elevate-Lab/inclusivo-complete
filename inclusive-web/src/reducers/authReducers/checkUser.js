import {
    CHECK_USER_FAILURE,
    CHECK_USER_REQUEST,
    CHECK_USER_SUCCESS
} from '../../actions/authActions/authTypes';

const initialState = {
    loading: false,
    is_user: false,
    error: '',
    registered: ''
}

const checkUser = (state = initialState, action) => {
    switch (action.type) {
        case CHECK_USER_REQUEST:
            return {
                ...state,
                loading: true
            }

        case CHECK_USER_SUCCESS:
            return {
                ...state,
                loading: false,
                is_user: action.payload.is_user,
                registered: action.payload.registered,
                error: ''
            }

        case CHECK_USER_FAILURE:
            return {
                ...state,
                loading: false,
                is_user: false,
                error: action.payload
            }
    
        default: return state;
    }
}

export default checkUser;