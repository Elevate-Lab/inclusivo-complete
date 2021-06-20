import React from 'react'
import {
    Grid,
    Typography,
    makeStyles,
    Button,
    Collapse
} from '@material-ui/core'
import {
    ExpandLess,
    ExpandMore
} from '@material-ui/icons'
import axios from 'axios';
import inclusivo from '../../assets/inclusivo.svg'
import Diversity from '../../assets/Landing/Diversity.svg'
import Quote1 from '../../assets/Landing/Quote1.svg'
import Quote2 from '../../assets/Landing/Quote2.svg'
import Quote3 from '../../assets/Landing/Quote3.svg'
import Question from '../../assets/Landing/Question.svg'
import Upskill from '../../assets/Landing/Upskill.svg'
import Star from '../../assets/Landing/Star.svg'
import UpskillSS from '../../assets/Landing/UpskillSS.svg'
import clsx from 'clsx'

import { baseUrl } from '../../urlConstants'
import { Link } from 'react-router-dom'

import Controls from '../../components/Form/Controls/Controls'
import useForm from '../../customHooks/useForm'
import { useSelector, useDispatch } from 'react-redux';
import { checkUserRequest, checkUserFailure, checkUserSuccess, setUserLoginEmail, setUserRegisterEmail } from '../../actions/authActions/checkUserActions'
import { useHistory } from 'react-router-dom';
import { Swiper, SwiperSlide } from "swiper/react";
import SwiperCore, { Keyboard, Mousewheel, Autoplay, EffectCoverflow, Pagination } from "swiper/core";
SwiperCore.use([Autoplay, Keyboard, Mousewheel, EffectCoverflow, Pagination]);

const useStyles = makeStyles((theme) => ({
    mainContainer: {
        '& .MuiButton-root:hover': {
            background: "#ff3750",
            color: "#fff"
        }
    },
    navItems: {
        padding: "0 10px",
        margin: "0 10px",
        height: "40px",
        cursor: "pointer",
        borderRadius: "5px"
    },
    dropDown :{
        marginTop: "20px",
        background: "#fff",
        zIndex: 4
    },
    nested:{
        marginBottom: "8px"
    },
    container: {
        width: "80%",
        color: "#484848",
    },
    appbar:{
        height: "76px",
        maxHeight: "76px",
        position: "fixed",
        boxShadow: "0px 0px 34px -9px rgba(0, 0, 0, 0.25)",
        zIndex: 2,
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
        fontSize: "2.4em",
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
        fontSize: "1.4rem",
        letterSpacing: "0.05em",
    },
    bodyText2:{
        fontSize: "1.4rem",
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
    quote2:{
        width: "80%"
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
    mySwiper:{
        display: "flex",
        justifyContent: "center"
    }
}))

const initialValues = {
    email: ''
}

function Landing() {
    const classes = useStyles();

    const history = useHistory();
    const dispatch = useDispatch();
    const checkUserDetails = useSelector(state => state.checkUser);

    const [open, setOpen] = React.useState(false)
    const [forEmployer, setForEmployer] = React.useState(false)

    const handleClick = () => {
        setOpen(!open)
    }

    const handleTabChange = (value) => () => {
        setOpen(false)
        setForEmployer(value==0 ? true : false)
        window.scrollTo(0,0)
    }

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
                <Grid item container alignItems='flex-start' className={classes.container}>
                    <Grid item container alignItems='center' style={{paddingTop: "24px", maxWidth: "130px"}}>
                        <img src={inclusivo} 
                            className={clsx(classes.logo)}                    
                        />
                    </Grid>
                    <Grid item container style={{paddingTop: "16px", maxWidth: "calc(100% - 130px)"}}>
                        <Grid item container justify='flex-end'>
                            <Grid item>
                                <Grid item container alignItems='center' className={classes.navItems} onClick={handleClick}>
                                    <Typography style={{marginRight: "4px"}}>How it Works</Typography>
                                    {open ? <ExpandLess /> : <ExpandMore />}
                                </Grid>
                                <Collapse in={open} timeout="auto" unmountOnExit>
                                    <Grid item className={classes.dropDown}>
                                        <Grid item container onClick={handleTabChange(1)} alignItems='center' className={clsx(classes.nested, classes.navItems)}>
                                            <Typography>For Candidates</Typography>
                                        </Grid>
                                        <Grid item container onClick={handleTabChange(0)} alignItems='center' className={clsx(classes.nested, classes.navItems)}>
                                            <Typography>For Employers</Typography>
                                        </Grid>
                                    </Grid>
                                </Collapse>
                            </Grid>
                            <Grid item>
                                <Link to='/signin'>
                                    <Grid item container alignItems='center' justify='center' className={clsx(classes.tabButton,classes.navItems)}>
                                        <Typography>Sign In</Typography>
                                    </Grid>
                                </Link>
                            </Grid>
                        </Grid>
                    </Grid>
                </Grid>
            </Grid>
            <Grid item container className={clsx(classes.container,classes.sectionContainer)}>
                <Grid item xs={12} sm={6}>
                    <Grid item className={classes.bodyTextContainer}>
                        <Typography className={classes.title}>
                            {forEmployer ? "Connect with unique talents" : "Welcome To Inclusivo"}
                        </Typography>
                    </Grid>
                    <Grid item className={classes.bodyTextContainer}>
                        {forEmployer ?
                            <>
                                <Typography className={classes.bodyText} style={{marginBottom: "12px"}}>
                                    Diverse and inclusive workplace makes everyone feel equally involved in all areas of workspace, regardless of who they are or where they come from.
                                </Typography>
                                <Typography className={classes.bodyText}>
                                    Studies have shown that diverse teams are 33% more likely to outperform others. This is where Inclusivo comes in! We help you bring more diversity and uniqueness in your  workforce.
                                </Typography>
                            </>
                            :
                            <Typography className={classes.bodyText} style={{fontSize: "1.6rem"}}>
                                We help candidates to find diverse jobs while being visible and valued. Companies can learn about diversity, equity and inclusion and connect with our unique talent.
                            </Typography>
                        }
                    </Grid>
                    <Grid item style={{marginTop: "20px"}}>
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
                        {forEmployer ? 
                            <Typography className={classes.title} style={{padding: "20px 16px 0 0"}}>
                                Why do you need inclusivo ?
                            </Typography>
                            :
                            <>
                                <Typography className={classes.title} style={{padding: "20px 16px 0 0"}}>
                                    Who Are We ?
                                </Typography>
                                <img src={Star} 
                                    className={classes.star}                  
                                />
                            </>
                        }
                    </Grid>
                    <Grid item className={classes.bodyTextContainer}>
                        {forEmployer ?
                            <Typography className={classes.bodyText}>
                                Inclusivo is the one stop solution for all your talent sourcing need. We aim to bring candidates from diverse backgrounds with their own unique skillsets. Just post your jobs on Inclusivo and the whole process is managed end-to-end with ease!
                            </Typography> 
                            :
                            <Typography className={classes.bodyText}>
                                Inclusivo was build to balance the current job eco-system. We believe that, diverse teams significantly outperform non-diverse teams. Our conscious efforts are aimed at eliminating bias during hiring decisions and educate hiring decisions and folks about diversity and inclusion in the work-post.
                            </Typography>
                        }
                    </Grid>
                </Grid>
            </Grid>

            {forEmployer ?
                <>
                    <Grid item container className={clsx(classes.container,classes.sectionContainer)} style={{marginTop: "60px"}}>
                        <Typography className={classes.title} style={{padding: "20px 16px 0 0"}}>
                            How it Works??
                        </Typography>
                    </Grid>

                    {/* how it works */}
                    <Grid item container className={clsx(classes.container,classes.sectionContainer)} style={{marginTop: "0px"}}>
                        <Grid item container justify='center' item xs={12} sm={4} className={clsx(classes.aboutSection)}>
                            <Grid item>
                                <Typography className={classes.title2} style={{padding: "20px 16px 0 0"}}>
                                    Share about your company culture
                                </Typography>
                                <Typography className={classes.bodyText2}>
                                    Feature stories about diverse folks within your organisation and their experiences. Mention initiatives taken by your company to make it a more inclusive space.
                                </Typography>
                            </Grid>
                        </Grid>
                        <Grid item container justify='center' xs={12} sm={4} className={classes.aboutSection}>
                            <Grid item>
                                <Typography className={classes.title2} style={{padding: "20px 16px 0 0"}}>
                                    Learn more about diversity
                                </Typography>
                                <Typography className={classes.bodyText2}>
                                    Upskill on why organisations today require diverse & inclusive teams through our diversity training module. Read blogs and watch videos to understand the importance of D&I and apply these newly learnt skills within your organisation.
                                </Typography>
                            </Grid>
                        </Grid>
                        <Grid item container justify='center' xs={12} sm={4} className={clsx(classes.aboutSection)}>
                            <Grid item>
                                <Typography className={classes.title2} style={{padding: "20px 16px 0 0"}}>
                                    Post Openings with Ease!
                                </Typography>
                                <Typography className={classes.bodyText2}>
                                    Upload job opportunities comfortably and conveniently with inclusivo. Our platform allows candidates to apply for the jobs reducing efforts of employers.
                                </Typography>
                            </Grid>
                        </Grid>
                    </Grid>
                </>
                :
                <>
                    <Grid item container className={clsx(classes.container,classes.sectionContainer)} style={{marginTop: "140px"}}>
                        <Typography className={classes.title} style={{padding: "20px 16px 0 0"}}>
                            How it Works??
                        </Typography>
                    </Grid>

                    {/* how it works */}
                    <Grid item container className={clsx(classes.container,classes.sectionContainer)} style={{marginTop: "0px"}}>
                        <Grid item container justify='center' item xs={12} sm={4} className={clsx(classes.aboutSection)}>
                            <Grid item>
                                <Typography className={classes.title2} style={{padding: "20px 16px 0 0"}}>
                                    We help Diversify
                                </Typography>
                                <Typography className={classes.bodyText2}>
                                    Inclusivo's prime goal is to help diverse groups find relevant jobs, we make sure the companies onboarded on our platform are more inclined towards making their workpost more inclusive.
                                </Typography>
                            </Grid>
                        </Grid>
                        <Grid item container justify='center' xs={12} sm={4} className={clsx(classes.aboutSection)}>
                            <Grid item>
                                <Typography className={classes.title2} style={{padding: "20px 16px 0 0"}}>
                                    Listen From Community!
                                </Typography>
                                <Typography className={classes.bodyText2}>
                                Candidates can view the initiatives taken by the companies to promote inclusion, also they can listen to the Stories of peers to see the problems while working in a diversified workplace and more importantly, find solutions to them.
                                </Typography>
                            </Grid>
                        </Grid>
                        <Grid item container justify='center' xs={12} sm={4} className={classes.aboutSection}>
                            <Grid item>
                                <Typography className={classes.title2} style={{padding: "20px 16px 0 0"}}>
                                    Push Yourself!
                                </Typography>
                                <Typography className={classes.bodyText2}>
                                Our features also assist candidates upskill and prepare for their dream company as well as interviews. Resources are present to grow confidence in candidates.
                                </Typography>
                            </Grid>
                            <Grid item container justify='space-between'>
                                {/* <img src={Flower1}
                                    style={{transform: 'translate(-20px,0)'}} 
                                    className={clsx(classes.flower)}                    
                                />
                                <img src={Flower2} 
                                    style={{transform: 'translate(20px,0)'}} 
                                    className={clsx(classes.flower)}                    
                                /> */}
                            </Grid>
                        </Grid>
                    </Grid>
                </>
            }

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
                <Swiper
                    effect={"coverflow"}
                    centeredSlides={true}
                    slidesPerView={"auto"}
                    coverflowEffect={{
                        rotate: 50,
                        stretch: 0,
                        depth: 100,
                        modifier: 1,
                        slideShadows: true
                    }}
                    mousewheel={true}
                    keyboard={{
                        enabled: true
                    }}
                    pagination={{
                        clickable: true
                    }}
                    autoplay={{
                        delay: 5000,
                        disableOnInteraction: false
                    }}
                    loop={true}
                >
                    <SwiperSlide className={classes.mySwiper}>
                        <img src={UpskillSS} 
                            className={clsx(classes.quote2)}                    
                        />
                    </SwiperSlide>
                    <SwiperSlide className={classes.mySwiper}>
                        <img src={UpskillSS} 
                            className={clsx(classes.quote2)}                    
                        />
                    </SwiperSlide>
                    <SwiperSlide className={classes.mySwiper}>
                        <img src={UpskillSS} 
                            className={clsx(classes.quote2)}                    
                        />
                    </SwiperSlide>
                    <SwiperSlide className={classes.mySwiper}>
                        <img src={UpskillSS} 
                            className={clsx(classes.quote2)}                    
                        />
                    </SwiperSlide>
                    <SwiperSlide className={classes.mySwiper}>
                        <img src={UpskillSS} 
                            className={clsx(classes.quote2)}                    
                        />
                    </SwiperSlide>
                </Swiper>
            </Grid>
            <Grid className={clsx(classes.container,classes.sectionContainer)} style={{margin: "80px 0"}}>
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
