import {
    USER_STATUS_FAILURE,
    USER_STATUS_SUCCESS,
    USER_STATUS_REQUEST
} from '../../actions/authActions/authTypes';

const initialState = {
    loading: false,
    isEmployer: false,
    isCandidate : false,
    isUpdated: false,
    isCompleted: false,
    isError: false,
    error: null,
    id: ""
}

const UserStatusReducer = (state = initialState,action) => {
    switch(action.type){
        case USER_STATUS_REQUEST : 
            return {
                ...state,
                loading: true
            }
        case USER_STATUS_SUCCESS : 
            return {
                ...state,
                loading: false,
                isEmployer: action.payload.isEmployer,
                isCandidate: action.payload.isCandidate,
                id: action.payload.id,
                isUpdated: action.payload.isUpdated,
                isCompleted: action.payload.isCompleted,
            }     
        case USER_STATUS_FAILURE : 
            return {
                ...state,
                isError: action.payload.isError,
                error: action.payload.error
            }    
        default: return state;    
    }
}

export default UserStatusReducer;