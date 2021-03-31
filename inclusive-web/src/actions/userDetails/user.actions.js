import {  UserDetailsConstants } from "../constants";
import axios from 'axios';
import { baseUrl } from "../../urlConstants";

export const getUserDetails = () => {
  return async (dispatch) => {
    try {
      dispatch({ type: UserDetailsConstants.GET_USER_DETAILS_REQUEST });
      const res = await axios.get(`${baseUrl}/user/get/0/`,{
        headers: {
          'Authorization': `token ${localStorage.getItem('key')}`, 
          'Content-Type': 'application/json',
        }
      });
      // console.log(res);

      if (res.status === 200) {
        const data = res.data;
        if (data) {
          dispatch({
            type: UserDetailsConstants.GET_USER_DETAILS_SUCCESS,
            payload: data
          })
        }
      }
    }
    catch (error) {
      // console.log(error)
      const message = "Something went Wrong"
      dispatch({
        type: UserDetailsConstants.GET_USER_DETAILS_FAILURE,
        payload: message
      })
    }
  }
}