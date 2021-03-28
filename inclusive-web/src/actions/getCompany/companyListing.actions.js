import { CompanyProfileConstants } from "../constants";
import axios from '../../helpers/axios'

export const getCompanies = ( body ) => {
  return async (dispatch) => {
    try {
      dispatch({type: CompanyProfileConstants.GET_COMPANIES_REQUEST});
      const res = await axios.post(`/company/get/`, body);
      console.log(res);
      if(res.status === 200 ){
        const data = res.data;
        if(data){
          dispatch({
            type : CompanyProfileConstants.GET_COMPANIES_SUCCESS,
            payload : data
          })
        }
      }
    }
    catch (error) {
      console.log(error)
      const message = "Error 404!"
      dispatch({
        type: CompanyProfileConstants.GET_COMPANIES_FAILURE,
        payload : message
      })
    }
  }
}