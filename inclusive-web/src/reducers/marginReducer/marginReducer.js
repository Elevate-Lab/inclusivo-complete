import {
    DESKTOP_VIEW_OPEN,
    DESKTOP_VIEW_CLOSE,
    MOBILE_VIEW_OPEN,
    MOBILE_VIEW_CLOSE,
    DARK_MODE_CLOSE,
    DARK_MODE_OPEN
} from '../../actions/marginActions/types'

const initialState = {
    marginFromLeft: 236,
    darkMode: false
}

const MarginReducer = (state = initialState, action) => {
    switch(action.type) {
        case DESKTOP_VIEW_OPEN:
            return {
                ...state,
                marginFromLeft: action.payload
            }
        case DESKTOP_VIEW_CLOSE:
            return {
                ...state,
                marginFromLeft: action.payload
            }
        case MOBILE_VIEW_OPEN:
            return {
                ...state,
                marginFromLeft: action.payload
            }
        case MOBILE_VIEW_CLOSE:
            return {
                ...state,
                marginFromLeft: action.payload
            }
        case DARK_MODE_CLOSE:
            return {
                ...state,
                darkMode: false
            }
        case DARK_MODE_OPEN:
            return {
                ...state,
                darkMode: true
            }
        default:
            return state
    }
}

export default MarginReducer