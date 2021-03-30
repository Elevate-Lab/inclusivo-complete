import {
    makeStyles
} from '@material-ui/core';

export const useStyles = makeStyles(theme => ({
    dashboardContainer: {
        maxWidth: "1100px",
        margin: "0px auto"
    },
    dashboardHeading : {
        margin: theme.spacing(2),
    },
    dashboardHeading2 : {
        paddingBottom : theme.spacing(2),
        borderBottom: "2px solid rgba(234, 234, 234, 1)"
    },
    appliedJobsContainer: {
        background: "#FAFAFA",
        margin: theme.spacing(2),
        paddingBottom: theme.spacing(2)
    },
    cardContainer: {
        margin: theme.spacing(2),
        paddingTop: theme.spacing(2)
    },
    card: {
        position: "relative",
        maxWidth: "250px",
        flex: "1 1",
        overflow: "visible",
        borderRadius: "5px",
        boxShadow: "none",
        [theme.breakpoints.down('sm')] : {
            maxWidth: "250px"
        }
    },
    cover: {
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        width: "40px",
        height: "40px",
        borderRadius: "5px",
        position: "absolute",
        left: "15px",
        top: "-12px"
    },
    details: {
        left: "20px",
        display: 'flex',
        flexDirection: 'column',
        alignItems: "flex-start",
        marginLeft: "65px",
        '& .MuiCardContent-root:last-child': {
            padding: '12px'
        }
    },
    content: {
        flex: '1 0 auto',
    },
    jobsContainer: {
        display: "flex",
        flexWrap: "wrap",
        alignItems: "center",
        justifyContent:"center"
    },
    jobCard: {
        marginLeft: theme.spacing(2),
        marginRight: theme.spacing(2),
        [theme.breakpoints.up('lg')] : {
            marginLeft: theme.spacing(1),
            marginRight: theme.spacing(1),
            flexBasis: "31%",
        },
        [theme.breakpoints.between("sm","md")]: {
            marginLeft: theme.spacing(1),
            marginRight: theme.spacing(1),
            flexBasis: "44%",
        }, [theme.breakpoints.between("xs","sm")]: {
            flexBasis: "100%",
        }
    },
    jobCard2: {
        marginLeft: theme.spacing(2),
        marginRight: theme.spacing(2),
        [theme.breakpoints.between("sm")]: {
            marginLeft: theme.spacing(1),
            marginRight: theme.spacing(1),
            flexBasis: "44%",
        }, [theme.breakpoints.between("xs", "sm")]: {
            flexBasis: "100%",
        }
    },
    jobCard3: {
        marginLeft: theme.spacing(2),
        marginRight: theme.spacing(2),
        flexBasis : "100%"
    },
    cityCard:{
        maxWidth: "290px",
        position: "relative",
        overflow: "visible",
        height: "110px",
        background: "#fff",
        margin: "30px 8px",
        cursor: "pointer",
        borderRadius: "5px",
        '&:hover':{
            boxShadow: "0px 0px 10px -2px rgba(0, 0, 0, 0.15)"
        },
    },
    cityCardImageContainer: {
        position: "absolute",
        top: "-40px",
        left: "20px",
    },
    cityCardImage:{
        border: "10px solid #fafafa",
    },
    cityCardName: {
        paddingLeft: "166px"
    },
    buttonContainer : {
        marginLeft: theme.spacing(2),
        marginTop: theme.spacing(2)
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
        [theme.breakpoints.between('sm', 'md')]: {
            flexBasis: "45%"
        },
        [theme.breakpoints.up('md')]: {
            flexBasis: "32%"
        }
    }
}));