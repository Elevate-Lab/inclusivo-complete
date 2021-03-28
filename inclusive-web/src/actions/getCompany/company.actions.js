import { CompanyProfileConstants } from "../constants";
import axios from '../../helpers/axios'
import store from '../../store';

export const getCompanyDetails = (id) => {
  return async (dispatch) => {
    try {
      dispatch({ type: CompanyProfileConstants.GET_COMPANY_DETAILS_REQUEST });
      const res = await axios.get(`/company/company_details/${id}/`);
      console.log(res);

      if (res.status === 200) {
        const data = res.data;
        if (data) {
          dispatch({
            type: CompanyProfileConstants.GET_COMPANY_DETAILS_SUCCESS,
            payload: data
          })
        }
      }
    }
    catch (error) {
      console.log(error)
      const message = "Error 404! Bad Request, The company id is wrong"
      dispatch({
        type: CompanyProfileConstants.GET_COMPANY_DETAILS_FAILURE,
        payload: message
      })
    }
  }
}

export const followCompany = (id, is_following) => {
  return async (dispatch) => {
    try {
      const following = is_following;
      // store.getState().companyDetails.is_following;
      if (following) {
        const res = await axios.post(`/company/unfollow/${id}/`);
        console.log(res);
        //API NOT WORKING CORRECTLY FOR NOW
        dispatch({
          type: CompanyProfileConstants.UNFOLLOW_COMPANY_SUCCESS
        })
      }
      else {
        const res = await axios.post(`/company/follow/${id}/`);
        if (res.status === 200) {
          dispatch({
            type: CompanyProfileConstants.FOLLOW_COMPANY_SUCCESS
          })
        }
      }
    }
    catch (error) {
      console.log(error);
    }
  }
}