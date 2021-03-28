import {
    DESKTOP_VIEW_OPEN,
    DESKTOP_VIEW_CLOSE,
    MOBILE_VIEW_OPEN,
    MOBILE_VIEW_CLOSE,
    DARK_MODE_CLOSE,
    DARK_MODE_OPEN
} from './types'

export const desktopViewOpen = () => {
    return {
        type: DESKTOP_VIEW_OPEN,
        payload: 236
    }
}

export const desktopViewClose = () => {
    return {
        type: DESKTOP_VIEW_CLOSE,
        payload: 76
    }
}

export const mobileViewOpen = () => {
    return {
        type: MOBILE_VIEW_OPEN,
        payload: 0
    }
}

export const mobileViewClose = () => {
    return {
        type: MOBILE_VIEW_CLOSE,
        payload: 0
    }
}

export const handleDarkMode= (isDark) => {
    return (dispatch) => {
        isDark ? dispatch({ type: DARK_MODE_CLOSE }) : dispatch({ type: DARK_MODE_OPEN });    
    }
  }

