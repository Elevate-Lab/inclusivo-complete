import React from 'react'
import {
    Grid,
    Typography,
    makeStyles,
    Button
} from '@material-ui/core'
import axios from 'axios';
import inclusivo from '../../assets/inclusivo.svg'
import Diversity from '../../assets/Landing/Diversity.svg'
import Quote1 from '../../assets/Landing/Quote1.svg'
import Quote2 from '../../assets/Landing/Quote2.svg'
import Quote3 from '../../assets/Landing/Quote3.svg'
import Question from '../../assets/Landing/Question.svg'
import Upskill from '../../assets/Landing/Upskill.svg'
import Star from '../../assets/Landing/Star.svg'
import Flower1 from '../../assets/Landing/Flower1.svg'
import Flower2 from '../../assets/Landing/Flower2.svg'
import UpskillSS from '../../assets/Landing/UpskillSS.svg'
import clsx from 'clsx'
import { baseUrl } from '../../urlConstants'

import Controls from '../../components/Form/Controls/Controls'
import useForm from '../../customHooks/useForm'
import { useSelector, useDispatch } from 'react-redux';
import { checkUserRequest, checkUserFailure, checkUserSuccess, setUserLoginEmail, setUserRegisterEmail } from '../../actions/authActions/checkUserActions'
import { useHistory } from 'react-router-dom';

const useStyles = makeStyles((theme) => ({
    mainContainer: {
        '& .MuiButton-root:hover': {
            background: "#ff3750",
            color: "#fff"
        }
    },
    container: {
        width: "80%",
        color: "#484848",
    },
    appbar:{
        height: "76px",
        position: "fixed",
        boxShadow: "0px 0px 34px -9px rgba(0, 0, 0, 0.25)",
        zIndex: 999,
        background: "#fff"
    },
    logo:{
        height: "20px",
    },
    image:{
        width: "90%"  
    },
    sectionContainer:{
        marginTop: "210px"
    },
    title:{
        fontSize: "3em",
        lineHeight: "1.4em",
        fontWeight: "800",
        letterSpacing: "0.08em",
        marginBottom: "0.6em"
    },
    title2:{
        fontSize: "2.4em",
        lineHeight: "1.4em",
        fontWeight: "800",
        letterSpacing: "0.08em",
        marginBottom: "0.6em"
    },
    bodyText:{
        fontSize: "1.8rem",
        letterSpacing: "0.05em",
    },
    bodyText2:{
        fontSize: "1.6rem",
        letterSpacing: "0.05em",
    },
    bodyText3:{
        fontSize: "2rem",
        fontWeight: "500",
        letterSpacing: "0.05em",
        paddingLeft: "12px"
    },
    quote:{
        width: "100%"
    },
    star: {
        width: "3em"
    },
    upskill:{
        height: "80%"
    },
    bottom:{
        background: "#F3FBFF",
    },
    aboutSection:{
        padding: "30px"
    },
    pd:{
        paddingTop: "80px"
    },
    flower:{
        width: "36px",
        marginTop: "40px"
    },
    form:{
        display: 'flex',
        width: "100%"
    },
    btn:{
        marginTop: "20px",
        height: "39px",
        width: "100%",
    },
    tabButton: {
        background: "#FF3750",
        color: "#fff",
    },
}))

const initialValues = {
    email: ''
}

function Landing() {
    const classes = useStyles();

    const history = useHistory();
    const dispatch = useDispatch();
    const checkUserDetails = useSelector(state => state.checkUser);

    const [values, setValues, errors, setErrors, handleChange] = useForm(initialValues, false, ()=>{})

    const handleSubmit = (e) => {
        e.preventDefault();
        if (validateForm()) {
            const userData = {
                email: values.email
            }
            dispatch(checkUserRequest());
            axios({
                method: "post",
                url: `${baseUrl}/user/check/user/`,
                headers: {
                    'Content-Type': 'application/json',
                    'Access-Control-Allow-Origin': '*'
                },
                data: JSON.stringify(userData)
                })
                .then(response => {
                    let data = {};
                    if (response.data.status === "OK") {
                    if (response.data.data.is_user) {
                        dispatch(setUserLoginEmail(userData.email));
                        data = {
                        is_user: response.data.data.is_user,
                        registered: "YES",
                        };
                    } else {
                        dispatch(setUserRegisterEmail(userData.email));
                        data = {
                        is_user: response.data.data.is_user,
                        registered: "NOT",
                        };
                    }
                    dispatch(checkUserSuccess(data));
                    history.push({
                        pathname: '/auth',
                        state: {
                        from: history.location,
                        is_user: response.data.data.is_user,
                        mode: response.data.data.is_user ? "DefaultLogin" : "Registration"
                        }
                    })
                    }

                })
                .catch(err => {
                    console.log(err.response);
                    const errorMsg = err.response;
                    dispatch(checkUserFailure(errorMsg));
                })
        }
    }

    const validateForm = () => {
        let temp = {};
        temp.email = (/^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/).test(values.email) ? "" : "Email is not valid."
        setErrors({
            ...temp
        })
        return Object.values(temp).every(value => value === "")
    }

    return (
        <Grid container justify='center' className={classes.mainContainer}>
            <Grid item container className={classes.appbar} justify='center'>
                <Grid item container className={classes.container} alignItems='center'>
                    <img src={inclusivo} 
                        className={clsx(classes.logo)}                    
                    />
                </Grid>
            </Grid>
            <Grid item container className={clsx(classes.container,classes.sectionContainer)}>
                <Grid item xs={12} sm={6}>
                    <Grid item className={classes.bodyTextContainer}>
                        <Typography className={classes.title}>
                            Welcome To Inclusivo
                        </Typography>
                    </Grid>
                    <Grid item className={classes.bodyTextContainer}>
                        <Typography className={classes.bodyText}>
                            "We help diverse Techies find jobs & advance their careers while being visible and valued. We also help companies find the unique talent they need. "
                        </Typography>
                    </Grid>
                    <Grid item>
                        <form onSubmit={handleSubmit} className={classes.form}>
                            <Grid item xs={10}>
                                <Controls.FormInput 
                                    value={values.email}
                                    placeholder="enter your email.." 
                                    name="email" 
                                    handleChange={handleChange}
                                    multiline={false}
                                    {...(errors.email && {
                                        error: errors.email
                                    })}
                                />
                            </Grid>
                            <Grid item xs={2}>
                                <Button
                                    className={clsx(classes.tabButton,classes.btn)}
                                    ariaLabel="Join"
                                    onClick={handleSubmit}>
                                    <Typography style={{ fontSize: "14px" }}>
                                        Join
                                    </Typography>
                                </Button>
                            </Grid>
                        </form>
                    </Grid>
                </Grid>
                <Grid item container xs={12} sm={6} justify="flex-end">
                    <img src={Diversity} 
                        className={clsx(classes.image)}                    
                    />
                </Grid>
            </Grid>
            <Grid className={clsx(classes.container,classes.sectionContainer)} style={{marginTop: "140px"}}>
                <img src={Quote1} 
                    className={clsx(classes.quote)}                    
                />
            </Grid>

            {/* Who are we */}
            <Grid item container className={clsx(classes.container,classes.sectionContainer)} style={{marginTop: "140px"}}>
                <Grid item container xs={12} sm={6} justify="flex-start">
                    <img src={Question} 
                        className={clsx(classes.image)}                    
                    />
                </Grid>
                <Grid item xs={12} sm={6}>
                    <Grid item container className={classes.bodyTextContainer}>
                        <Typography className={classes.title} style={{padding: "20px 16px 0 0"}}>
                            Who Are We ?
                        </Typography>
                        <img src={Star} 
                            className={classes.star}                  
                        />
                    </Grid>
                    <Grid item className={classes.bodyTextContainer}>
                        <Typography className={classes.bodyText}>
                            "We help diverse Techies find jobs & advance their careers while being visible and valued. We also help companies find the unique talent they need. "
                        </Typography>
                    </Grid>
                </Grid>
            </Grid>

            {/* how it works */}
            <Grid item container className={clsx(classes.container,classes.sectionContainer)} style={{marginTop: "140px"}}>
                <Grid item container justify='center' item xs={12} sm={4} className={clsx(classes.aboutSection,classes.pd)}>
                    <Grid item>
                        <Typography className={classes.title2} style={{padding: "20px 16px 0 0"}}>
                            Who Are We ?
                        </Typography>
                        <Typography className={classes.bodyText2}>
                            "We help diverse Techies find jobs & advance their careers while being visible and valued. We also help companies find the unique talent they need. "
                        </Typography>
                    </Grid>
                </Grid>
                <Grid item container justify='center' xs={12} sm={4} className={classes.aboutSection}>
                    <Grid item>
                        <Typography className={classes.title2} style={{padding: "20px 16px 0 0"}}>
                            Who Are We ?
                        </Typography>
                        <Typography className={classes.bodyText2}>
                            "We help diverse Techies find jobs & advance their careers while being visible and valued. We also help companies find the unique talent they need. "
                        </Typography>
                    </Grid>
                    <Grid item container justify='space-between'>
                        <img src={Flower1}
                            style={{transform: 'translate(-20px,0)'}} 
                            className={clsx(classes.flower)}                    
                        />
                        <img src={Flower2} 
                            style={{transform: 'translate(20px,0)'}} 
                            className={clsx(classes.flower)}                    
                        />
                    </Grid>
                </Grid>
                <Grid item container justify='center' xs={12} sm={4} className={clsx(classes.aboutSection,classes.pd)}>
                    <Grid item>
                        <Typography className={classes.title2} style={{padding: "20px 16px 0 0"}}>
                            Who Are We ?
                        </Typography>
                        <Typography className={classes.bodyText2}>
                            "We help diverse Techies find jobs & advance their careers while being visible and valued. We also help companies find the unique talent they need. "
                        </Typography>
                    </Grid>
                </Grid>
            </Grid>

            {/*  quote */}
            <Grid className={clsx(classes.container,classes.sectionContainer)} style={{marginTop: "140px"}}>
                <img src={Quote2} 
                    className={clsx(classes.quote)}                    
                />
            </Grid>
            <Grid item container className={clsx(classes.container,classes.sectionContainer)} style={{marginTop: "140px"}}>
                <Grid item xs={1} sm={1}>
                    <img src={Upskill} 
                        className={clsx(classes.upskill)}                    
                    />
                </Grid>
                <Grid item xs={11} sm={11}>
                    <Typography className={classes.bodyText3}>
                        Think we only provide jobs?
                    </Typography>
                    <Typography className={classes.bodyText3} style={{fontSize: "1.8rem" ,marginTop: "4px"}}>
                        Our <span style={{color: "#1E4BEA"}}>upskill module</span> will enlighten you with roadmaps to success.
                    </Typography>
                </Grid>
            </Grid>
            <Grid className={clsx(classes.container,classes.sectionContainer)} style={{marginTop: "40px"}}>
                <img src={UpskillSS} 
                    className={clsx(classes.quote)}                    
                />
            </Grid>
            <Grid className={clsx(classes.container,classes.sectionContainer)} style={{marginTop: "140px"}}>
                <img src={Quote3} 
                    className={clsx(classes.quote)}                    
                />
            </Grid>
            <Grid item container className={classes.bottom} justify='center'>
                <Grid item container className={classes.container} alignItems='center'>
                    <Grid item xs={12} sm={4}>
                        <Typography className={classes.bodyText}>
                            About Us
                        </Typography>
                        <Typography className={classes.bodyText}>
                            Our Crew
                        </Typography>
                        <Typography className={classes.bodyText}>
                            Help
                        </Typography>
                        <Typography className={classes.bodyText}>
                            Contact Us
                        </Typography>
                    </Grid>
                    <Grid item xs={12} sm={4}>
                        <Typography className={classes.bodyText}>
                            About Us
                        </Typography>
                        <Typography className={classes.bodyText}>
                            Our Crew
                        </Typography>
                        <Typography className={classes.bodyText}>
                            Help
                        </Typography>
                        <Typography className={classes.bodyText}>
                            Contact Us
                        </Typography>
                    </Grid>
                    <Grid item xs={12} sm={4}>
                        <Typography className={classes.bodyText}>
                            About Us
                        </Typography>
                        <Typography className={classes.bodyText}>
                            Our Crew
                        </Typography>
                        <Typography className={classes.bodyText}>
                            Help
                        </Typography>
                        <Typography className={classes.bodyText}>
                            Contact Us
                        </Typography>
                    </Grid>
                </Grid>
            </Grid>
        </Grid>
    )
}

export default Landing
