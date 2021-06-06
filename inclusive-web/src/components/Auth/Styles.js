import { makeStyles } from '@material-ui/core';

export const useStyles = makeStyles(theme => ({
    sweetLoading: {
        marginTop: "150px"
    },
    formName: {
        margin: "5px 0px 30px 0px",
        color: "red",
        fontSize: "2rem",
        textAlign: "center",
    },
    formContainer: {
        marginTop: "30px",
        display: "flex",
        flexDirection: "column",
        justifyContent: "center",
        alignItems: "center",
        border: "1px solid #B0B0B0",
        padding: "10%",
        borderRadius: "15px",
        margin: "30px auto",
        width: "70%",
        [theme.breakpoints.down('sm')]: {
            padding: "5%",
        },
        [theme.breakpoints.down('xs')]: {
            width: "90%"
        }
    },
    container: {
        width: "90%",
        margin: "auto"
    },
    formInputs: {
        width: "90%",
        margin: "12px auto",
        maxWidth: "100%"
    },
    forgotButton: {
        width: "90%",
        maxWidth: "100%",
        margin: "5% auto"
    },
    formButton: {
        width: "90%",
        maxWidth: "100%",
        margin: "5% auto",
        background: "#ff3750",
        color: "#fff"
    },
    link: {
        textDecoration: "none",
        color: "red"
    }, 
    registerPage: {
        width: "95%",
        margin: "20px auto",
        textAlign: "center",
        '& .MuiButton-root:hover':{
            background: "#ff3750",
        },
        '& .Mui-checked':{
            color: "#ff3750"
        }
    },
    logo: {
        marginBottom: "5%",
    },
    registerImageContainer: {
        margin: "auto",
        [theme.breakpoints.down('sm')]: {
            display: "none"
        }
    },
    registerImage: {
        maxWidth: "100%"
    },
    loginPage: {
        maxWidth: "95%",
        margin: "5% auto",
        textAlign: "center",
        '& .MuiButton-root:hover':{
            background: "#ff3750",
        }
    },
    loginImage: {
        margin: "auto",
        [theme.breakpoints.down('sm')]: {
            display: "none"
        }
    },
    landingPage: {
        maxWidth: "95%",
        textAlign: "center",
        '& .MuiButton-root:hover':{
            background: "#ff3750",
        }
    },
    landingImageContainer: {
        margin: "auto",
        height: '100vh',
        paddingTop: '8%',
        [theme.breakpoints.down('sm')]: {
            display: "none"
        }
    },
    landingImage: {
        height: '32rem'
    }
}));