import {UserDetailsConstants} from '../../actions/constants'


const initState = {
  data:{
    
  },
  
  loading:false,
  error:null,
  pageRequest:false
}

export default (state = initState, action ) =>{
  switch(action.type){
    case UserDetailsConstants.GET_USER_DETAILS_REQUEST:
      state={
        ...state,
        loading: true
      }
      break;
    case UserDetailsConstants.GET_USER_DETAILS_SUCCESS:
      state={
        ...state,
        loading: false,
        data: action.payload.data,
        pageRequest: true
      }
      break;
    case UserDetailsConstants.GET_USER_DETAILS_FAILURE:
        state={
          ...state,
          loading: false,
          error: action.payload,
          pageRequest: false
        }
      break;

      
  }
  return state
}