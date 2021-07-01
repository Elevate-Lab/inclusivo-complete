import {
    makeStyles
} from '@material-ui/core';

export const useStyles = makeStyles(theme => ({
    sweetLoading : {
        margin: "10px auto",
        display: "flex",
        alignItems: "center",
        justifyContent: "center" 
    },
    mainContainer: {
        maxWidth: "1100px",
        margin: "0px auto",
    },
    innerContainer: {
        width: "100%",
        margin: "0.5rem auto 0.5rem",
        padding: "0px 10px"
    },
    filtersOuterContainer: {
        marginLeft: "10px",
        marginRight: "10px",
        padding: "10px",
        background: "#FAFAFA",
    },
    filtersContainer: {
        background: "#ffffff",
        marginLeft: "auto",
        marginRight: "auto"
    },
    checkBoxContainer: {
        marginLeft: "10px"
    },
    root: {
        width: "100%",
        '& .MuiInputBase-root': {
            display: "flex"
        },
        '& .MuiButton-outlined': {
            padding: "10px 0px",
            border: "none",
            borderLeft: "1px solid #e6e6e6",
            borderRadius: "0px"
        },
        '& .MuiOutlinedInput-adornedEnd': {
            paddingRight: "0px"
        }
    },
    root4: {
        width: "100%",
        '& .MuiAutocomplete-inputRoot': {
            background: "#fafafa",
            paddingTop: "1px",
            paddingBottom: "1px"
        },
        '& .MuiAutocomplete-listbox': {
            overflowX: "hidden"
        },
    },
    slider: {
        width: "90%",
        margin: "0px auto"
    },
    singleLineInput: {
        '& .MuiOutlinedInput-input': {
            background: "#fafafa",
            borderRadius: "0px",
            padding: "10px 10px",
            width: "calc(100% - 20px)"
        }
    },
    input: {
        '& .MuiSelect-outlined': {
            color: "#9F9F9F",
        },
        fontSize: "14px",
        '& .MuiOutlinedInput-multiline': {
            background: "#fafafa",
            padding: "10px 10px",
            borderRadius: "5px",
            minHeight: "100px"
        },
        "& .MuiOutlinedInput-root .MuiOutlinedInput-notchedOutline": {
            border: "1px solid #e6e6e6 !important",
        },
        "&:hover .MuiOutlinedInput-root .MuiOutlinedInput-notchedOutline": {
            borderColor: "#76B7F3 !important"
        },
        "&:focus-within .MuiOutlinedInput-root .MuiOutlinedInput-notchedOutline": {
            border: "2px solid #76B7F3 !important"
        },
    },
    btn: {
        background: "#FAFAFA",
        borderRadius: "5px",
        padding: "6px"
    },
    link: {
        textDecoration: "none",
        color: "#000",
        width: "83%",
    },
    itemContainer: {
        marginTop: "12px",
        background: "#fff",
        padding: "8px",
        borderRadius: "5px",
        '&:hover': {
            boxShadow: "0px 0px 10px -2px rgba(0, 0, 0, 0.15)"
        }
    },
    image1: {
        width: "56px",
        marginRight: "10px"
    },
    image2: {
        width: "65px",
        marginRight: "10px"
    },
    title: {
        maxHeight: "56px",
        fontSize: "14px"
    },
    jobRole: {
        maxHeight: "56px",
        paddingLeft: "12px",
        fontWeight: "600",
        fontSize: "14px",
        color: "#3a3a3a",
        letterSpacing: "0.4px"
    },
    ellipsis: {
        display: "-webkit-box",
        "-webkit-box-orient": "vertical",
        "-webkit-line-clamp": 2,
        overflow: "hidden",
        textOverflow: "ellipsis",
    },
    filterButton: {
        maxWidth: "20%",
        [theme.breakpoints.down('xs')]: {
            maxWidth: "40% !important"
        }
    },
    tag: {
        paddingTop: '8px'
    },
    sliderContainer: {
        fontFamily: "Roboto,Helvetica,Arial,sans-serif",
        margin: theme.spacing(2),
        '& .MuiSlider-valueLabel' : {
            innerWidth: "120"
        }
    },
    viewMoreContainer: {
        display: "flex",
        alignItems: "flex-end",
        justifyContent:"flex-end",
        marginTop: theme.spacing(2)
    },
    locationIcon: {
        fontSize: "13px",
        color: "#6a6a6a",
        marginRight: "4px"
    },
    details: {
        color: "#404040",
        fontSize: "0.8rem",
    },
    detailsContainer: {
        paddingLeft: "5px"
    },
    description: {
        marginTop: "4px",
    },
    dataContainer : {
        background: "#FAFAFA",
        padding: "10px 10px 20px 10px",
        margin: "12px 10px 0px 10px",
        borderRadius: "5px"
    },
    labelContainer: {
        width: "95%",
        maxWidth: "95% !important",
        margin: "0px auto",
        textAlign: "center",
        '& .MuiFormControlLabel-label': {
            fontWeight: "700",
            color: "#6A6A6A"
        }
    },
    formFieldContainer: {
        width: "80%",
        maxWidth: "90% !important",
        margin: "0px auto",
        '& div:first-child' : {
            margin: "0px auto",
            maxWidth: "100% !important",
        }
    },
    controlsformFieldContainer: {
        width: "72%",
        maxWidth: "80% !important",
        margin: "0px auto",
        '& h6': {
            margin: "0px !important"
        }
    },
    companyBox: {
        background: "#fff",
        borderRadius: "5px",
        padding: "1.5rem",
        margin: "0.5rem auto",
        '&:hover': {
            boxShadow: "0px 0px 10px -2px rgba(0, 0, 0, 0.15)"
        },
        '& a': {
            textDecoration: "none",
            color: "black"
        },
        [theme.breakpoints.up('xs')]: {
            flexBasis: "95%"
        },
        [theme.breakpoints.between('sm','md')]: {
            flexBasis: "45%"
        },
        [theme.breakpoints.up('md')]: {
            flexBasis: "32%"
        }
    },
    ellipsis2: {
        lineHeight: "2em",
        maxHeight: "2em",
        overflow: "hidden",
        textOverflow: "ellipsis",
        width: "100%",
        display: "-webkit-box",
        "-webkit-box-orient": "vertical",
        "-webkit-line-clamp": 1,
        lineClamp: 1,
    },
    statusContainer:{
        padding: "0 12px",
        maxWidth: "360px"
    },
    status: {
        height: "40px",
        borderRadius: "5px",
        letterSpacing: "0.05rem",
    }
}))
