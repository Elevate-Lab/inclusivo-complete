import axios from 'axios';
import {
    REGISTER_USER_REQUEST,
    REGISTER_USER_SUCCESS,
    REGISTER_USER_FAILURE
} from './authTypes';
import { baseUrl } from '../../urlConstants';

export const registerUserRequest = () => {
    return {
        type: REGISTER_USER_REQUEST
    }
}

export const registerUserSuccess = response => {
    return {
        type: REGISTER_USER_SUCCESS,
        payload: response
    }
}

export const registerUserFailure = error => {
    return {
        type: REGISTER_USER_FAILURE,
        payload: error
    }
}
