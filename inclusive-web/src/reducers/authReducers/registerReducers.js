import {
    REGISTER_USER_FAILURE,
    REGISTER_USER_REQUEST,
    REGISTER_USER_SUCCESS,
    SET_USER_REGISTER_EMAIL
} from '../../actions/authActions/authTypes';

const initialState = {
    loading: false,
    response: '',
    userEmail: '',
    error: ''
}

const RegisterReducer = (state = initialState, action) => {
    switch (action.type) {
        case REGISTER_USER_REQUEST:
            return {
                ...state,
                loading: true
            }

        case REGISTER_USER_SUCCESS:
            return {
                ...state,
                loading: false,
                response: action.payload,
                error: ''
            }

        case REGISTER_USER_FAILURE:
            return {
                ...state,
                loading: false,
                response: '',
                error: action.payload
            }
        case SET_USER_REGISTER_EMAIL:
            return {
                ...state,
                userEmail: action.payload
            }

        default: return state;
    }
}

export default RegisterReducer;