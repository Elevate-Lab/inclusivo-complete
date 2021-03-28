import React, { useState } from 'react';
import {
    makeStyles, 
    Typography,
    Grid,
    Box
} from '@material-ui/core';
import BookmarkBorderIcon from '@material-ui/icons/BookmarkBorder';
import BookmarkIcon from '@material-ui/icons/Bookmark';
import { RecentActors, WorkOutline } from '@material-ui/icons';
import { followCompany } from '../../actions/getCompany/company.actions';
import { useDispatch } from 'react-redux';

const useStyles = makeStyles(theme => ({
    jobRole:{
        fontSize: "1.25rem",
        marginBottom: "2%"
    },
    jobDetails:{
        display: "flex",
        flexDirection: "column",
        justifyContent: "flex-start"
    },
    details:{
        color: "#404040",
        fontSize: "0.9rem",
    },
    logo: {
        height: '90px',
        width: '90px'
    },
    locationIcon: {
        fontSize: "1rem",
        marginLeft: "-0.5rem"
    },
    title:{
        fontSize: "1rem",
        color: 'black'
    },
    description: {
        fontSize: "0.8rem",
        color: "#767676"
    },
    bookmark:{
        textAlign: "center",
        cursor: "pointer",
        color:'black'
    }
}));

const Company = ({company}) => {

    const classes = useStyles();
    const dispatch = useDispatch();

    const [bookmarked, setBookmarked] = useState(false);

    const handleBookmark = (e) => {
        e.preventDefault();
        setBookmarked((prev) => (!prev));
        dispatch(followCompany(company.id, company.is_following));
    }
    return(
        <div className={classes.job}>
            <Grid container display='flex' direction='column' >

                <Box className='container-row' style={{width: '100%'}} justifyContent='space-between'>
                    <Box  className={classes.logo}>
                        <img src = {company.logo_url} alt="company" style={{height:'98%'}}/>
                    </Box>

                    <Grid style={{alignSelf:'flex-start'}} >
                        <div className={classes.bookmark} onClick={handleBookmark} >
                            {bookmarked === true ? <BookmarkIcon style={{color: '#FF3750'}}/> : <BookmarkBorderIcon  />}
                        </div>
                    </Grid>
                </Box>

                <Box style={{marginTop: '1rem', width: '100%'}}>
                    <Typography className={classes.title}>
                        { company.name }
                    </Typography>
                </Box>
                <Box style={{marginTop: '0.5rem', width: '100%'}}>
                    <Typography className={classes.description}>
                        {company.title}
                    </Typography>
                </Box>
                <Box style={{marginTop: '1.5rem' , width:'100%'}} className='container-row'>
                    <Box style={{marginRight: '0.4rem', color:'black'}}>
                        <WorkOutline />
                    </Box>
                    <Typography className={classes.description} style={{color:'black'}}>
                        {company.jobs_count} Live jobs
                    </Typography>
                </Box>
                <Box style={{marginTop: '0.5rem'}} className='container-row'>
                    <Box style={{marginRight: '0.4rem', color:'black'}}>
                        <RecentActors />
                    </Box>
                    <Typography className={classes.description} style={{color:'black'}}>
                        6 Employers
                    </Typography>
                </Box>
               
            </Grid> 
        </div>
    
    )
}

export default Company;