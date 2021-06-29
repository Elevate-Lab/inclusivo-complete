import React from "react";
import {
    makeStyles,
    Grid,
    Chip,
    Typography,
    Button
} from '@material-ui/core';
import {
    VideocamRounded,
    LocationOn
} from '@material-ui/icons'
import { Link } from 'react-router-dom';
import clsx from 'clsx'
import UpskillSS from '../../assets/Landing/UpskillSS.svg'
import { Swiper, SwiperSlide } from "swiper/react";
import SwiperCore, { Keyboard, Mousewheel, Autoplay, EffectCoverflow, Pagination } from "swiper/core";
SwiperCore.use([Autoplay, Keyboard, Mousewheel, EffectCoverflow, Pagination]);

const useStyles = makeStyles((theme) => ({
    bContainer: {
        width: "100%",
        backgroundColor: "#fafafa",
        paddingTop: "12px"
    },
    mainContainer: {
        maxWidth: "1100px",
        width: "auto",
        margin: "0px auto",
    },
    imgContainer: {
        width: "180px",
        minWidth: "180px",
    },
    detailsContainer: {
        width: "calc(100% - 180px)",
        padding: "0px 10px"
    },
    tileContainer: {
        padding: "10px",
        minWidth: "200px"
    },
    tile: {
        backgroundColor: "#fff",
        // width: "calc(100% - 20px)",
        marginBottom: "12px",
        padding: "20px",
        borderRadius: "5px",
        boxShadow: "0px 0px 63px -41px rgba(0, 0, 0, 0.25)"
    },
    img: {
        maxWidth: "158px",
        maxHeight: "158px",
        borderRadius: "5px"
    },
    icon: {
        color: 'rgba(255, 255, 255, 0.54)',
    },
    ellipsis: {
        lineHeight: "1.5em",
        maxHeight: "3em",
        overflow: "hidden",
        textOverflow: "ellipsis",
        width: "100%",
        display: "-webkit-box",
        "-webkit-box-orient": "vertical",
        "-webkit-line-clamp": 2,
        lineClamp: 2,
    },
    description: {
        marginTop: "8px"
    },
    description2: {
        marginTop: "4px"
    },
    tileLink: {
        color: "inherit",
        width: "100%"
    },
    swiper:{
        width: "100%"
    },
    mySwiper:{
        width: "100%"
    },
    eventImgContainer: {
        position: "relative",
        width: "100%",
        height: "300px",
        overflow: "hidden",
    },      
    quote2: {
        width: "100%"
    },
    eventImg: {
        position: "absolute",
        top: "-9999px",
        left: "-9999px",
        right: "-9999px",
        bottom: "-9999px",
        margin: "auto",
    },
    eventDetailsContainer:{
        width: "50%",
        height: "100%",
        top: 0,
        left: 0,
        position: "absolute",
        background: "rgba(0,0,0,0.6)",
        zIndex: 2,
        [theme.breakpoints.down('sm')]:{
            width: "100%"
        }
    },
    eventDetails:{
        padding: "30px"
    },
    eventTitle:{
        color: "#fff"
    },
    eventBtn:{
        backgroundColor: "none",
        padding: "4px 40px",
        border: "2px solid #fff"
    },
    eventCard:{
        width: "30%",
        background: "#fff",
        borderRadius: "5px",
        cursor: "pointer",
        margin: "12px 0",
        [theme.breakpoints.down('sm')]:{
            width: "45%"
        },
        [theme.breakpoints.down('xs')]:{
            width: "100%"
        },
        '&:hover':{
            boxShadow: "0 0 20px -5px rgba(0,0,0,0.15)"
        },
    },
    eventCardImg:{
        width: "100%",
        borderRadius: "5px 0 5px 0"
    },
    live:{
        position: "absolute",
        left: "0",
        bottom: "0",
        padding: "4px 20px",
        background: "rgba(0,0,0,0.5)",
        color: "#ff3838"
    }
}))

function Events() {
    const classes = useStyles()

    const swiperEl = (value) => {
        return(
            <SwiperSlide className={classes.mySwiper}>
                <Grid className={classes.eventImgContainer}>
                    <Grid item container className={classes.eventDetailsContainer}>
                        <Grid className={classes.eventDetails}>
                            <Grid style={{padding:"0 0 20px 0"}}>
                                <Typography variant="h5" className={classes.eventTitle}>{value}</Typography>
                                <Typography variant="subtitle1" className={classes.eventTitle}>Hacktoberfest'20: Kick Start Your Journey</Typography>
                            </Grid>
                            <Grid item container alignItems="center" style={{padding:"0 0 20px 0"}}>
                                <LocationOn className={classes.eventTitle} style={{fontSize: "16px", marginRight: "6px"}}/>
                                <Typography variant="subtitle1" className={classes.eventTitle}>Online Event</Typography>
                            </Grid>
                            <Grid style={{padding:"0 0 20px 0"}}>
                                <Typography variant="subtitle1" className={classes.eventTitle}>Tuesday, 15 June, 2021, 7:00 p.m.</Typography>
                                <Typography variant="subtitle1" className={classes.eventTitle}>To Tuesday, 15 June, 2021, 8:00 p.m.</Typography>
                            </Grid>  
                            <Grid item container justify='center'>
                                <Button className={classes.eventBtn}>
                                    <Typography varaint="subtitle1" className={classes.eventTitle}>RSVP</Typography>
                                </Button>
                            </Grid>                                         
                        </Grid>
                    </Grid>
                    <img src={UpskillSS}
                        className={clsx(classes.quote2,classes.eventImg)}
                    />
                </Grid>
            </SwiperSlide>
        )
    }

    const card = (live=false) => {
        return(
            <Grid className={classes.eventCard}>
                <Grid item container style={{position: "relative"}}>
                    <img src={UpskillSS}
                        className={clsx(classes.eventCardImg)}
                    />
                    {
                        live ?
                        <Grid className={classes.live}>
                            <Typography variant="body2" style={{fontWeight: "600"}}>Live</Typography>
                        </Grid> 
                        :
                        null
                    }
                </Grid> 
                <Grid style={{padding: "10px 8px"}}>
                    <Grid item container>
                        <Typography className={classes.ellipsis} variant="h6" style={{fontWeight: "600"}}>Hacktoberfest'20: Kick Start Your Journey</Typography>
                    </Grid>
                    <Grid item container className={classes.description2}>
                        <Typography variant="body2" style={{fontWeight: "600"}}>Online Event</Typography>
                    </Grid>
                    <Grid item container className={classes.description}>
                        <Typography variant="body2" className={classes.ellipsis}>Tuesday, 15 June, 2021, 7:00 p.m.</Typography>
                        <Typography variant="body2" className={classes.ellipsis}>To Tuesday, 15 June, 2021, 8:00 p.m.</Typography>
                    </Grid>
                </Grid>                           
            </Grid>
        )
    }

    return (
        <>
            <Grid container justify='center' className={classes.bContainer}>
                <Grid item container className={classes.mainContainer} style={{width: "100%"}} alignItems='flex-start' justify="space-evenly">
                    <Grid item container xs={12} className={classes.tile} style={{ margin: "12px 8px 12px 8px", padding: "8px" }}>
                        <Swiper
                            centeredSlides={true}
                            slidesPerView={"auto"}
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
                            className={classes.swiper}
                        >
                            {swiperEl("Event 1")}
                            {swiperEl("Event 2")}
                            {swiperEl("Event 3")}
                        </Swiper>
                    </Grid>
                </Grid>
                <Grid item container className={classes.mainContainer} style={{width: "100%"}} alignItems='flex-start' justify="space-evenly">
                    <Grid item container xs={12} className={classes.tile} style={{ margin: "12px 8px 6px 8px", padding: "8px" }}>
                        <Typography variant="h6">Live Events</Typography>
                    </Grid>
                </Grid>
                <Grid item container className={classes.mainContainer} style={{width: "100%"}} alignItems='flex-start' justify="space-evenly">
                    <Grid item container xs={12} justify='space-between' className={classes.tile} style={{background: "none", boxShadow:"none", padding: "8px" }}>
                        {card(true)}
                        {card(true)}
                        {card(true)}
                    </Grid>
                </Grid>
                <Grid item container className={classes.mainContainer} style={{width: "100%"}} alignItems='flex-start' justify="space-evenly">
                    <Grid item container xs={12} className={classes.tile} style={{ margin: "12px 8px 6px 8px", padding: "8px" }}>
                        <Typography variant="h6">Upcoming Events</Typography>
                    </Grid>
                </Grid>
                <Grid item container className={classes.mainContainer} style={{width: "100%"}} alignItems='flex-start' justify="space-evenly">
                    <Grid item container xs={12} justify='space-between' className={classes.tile} style={{background: "none", boxShadow:"none", padding: "8px"}}>
                        {card()}
                        {card()}
                        {card()}
                    </Grid>
                </Grid>
            </Grid>
        </>
    )
}

export default Events