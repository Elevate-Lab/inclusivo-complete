import axios from 'axios';
import {
    USER_STATUS_FAILURE,
    USER_STATUS_SUCCESS,
    USER_STATUS_REQUEST
} from './authTypes';
import { baseUrl } from '../../urlConstants';

export const userStatusRequest = () => {
    return {
        type: USER_STATUS_REQUEST
    }
}

export const userStatusSuccess = response => {
    return {
        type: USER_STATUS_SUCCESS,
        payload: response
    }
}

export const userStatusFailure = error => {
    return {
        type: USER_STATUS_FAILURE,
        payload: error
    }
}

export const userStatus = () => {
    return (dispatch) => {
        dispatch(userStatusRequest())
        let key = localStorage.getItem('key');

        axios({
            method: "get",
            url: `${baseUrl}/user/get/0/`,
            headers: {
                "Authorization" : `token ${key}`
            }
        })
        .then(res => {
            //console.log(res);
            var data = res.data;
            var sentRes = {};
            if(data.status === "OK"){
                if(data.data.employer){
                    if(!data.data.employer.id){
                        sentRes = {
                            isCandidate: false,
                            isEmployer: true,
                            isUpdated: true,
                            isCompleted: false,
                            id: ""
                        }
                    } else {
                        if(data.data.employer.user.first_name.length > 0 && data.data.employer.user.last_name.length > 0){
                            sentRes = {
                                isCandidate: false,
                                isEmployer: true,
                                isUpdated: true,
                                isCompleted: true,
                                id: data.data.employer.id
                            }
                        } else {
                            sentRes = {
                                isCandidate: false,
                                isCompleted: false,
                                isEmployer: true,
                                isUpdated: true,
                                id: data.data.employer.id
                            }
                        }
                    }
                } else if(data.data.candidate) {
                    if (!data.data.candidate.id) {
                        sentRes = {
                            isEmployer: false,
                            isCandidate: true,
                            isUpdated: true,
                            isCompleted: false,
                            id: ""
                        }
                    } else {
                        if (data.data.candidate.user.first_name.length > 0 && data.data.candidate.user.last_name.length > 0) {
                            sentRes = {
                                isEmployer: false,
                                isCandidate: true,
                                isUpdated: true,
                                isCompleted: true,
                                id: data.data.candidate.id
                            }
                        } else {
                            sentRes = {
                                isEmployer: false,
                                isCompleted : false,
                                isCandidate: true,
                                isUpdated: true,
                                id: data.data.candidate.id
                            }
                        }
                    }
                }
                dispatch(userStatusSuccess(sentRes));
            } else {
                sentRes = {
                    isError: true,
                    error: data.message
                }
                dispatch(userStatusFailure(sentRes));
            }
        })
        .catch(err => {
            console.log(err);
            var sentRes = {
                isError: true,
                error: "There is error"
            }
            dispatch(userStatusFailure(sentRes));
        })
    }
}