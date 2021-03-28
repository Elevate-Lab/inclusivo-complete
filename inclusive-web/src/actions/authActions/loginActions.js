import axios from 'axios';
import {
    LOGIN_USER_REQUEST,
    LOGIN_USER_SUCCESS,
    LOGIN_USER_FAILURE
} from './authTypes';
import { baseUrl } from '../../urlConstants';
import { userStatus } from './userStatusActions';

export const loginUserRequest = () => {
    return {
        type: LOGIN_USER_REQUEST
    }
}

export const loginUserSuccess = response => {
    return {
        type: LOGIN_USER_SUCCESS,
        payload: response
    }
}

export const loginUserFailure = error => {
    return {
        type: LOGIN_USER_FAILURE,
        payload: error
    }
}