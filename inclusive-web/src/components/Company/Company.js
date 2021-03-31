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
import companyPlaceholder from '../../assets/company_placeholder.png';
import clsx from 'clsx';

const useStyles = makeStyles(theme => ({
    logo: {
        height: '90px',
        width: '90px'
    },
    locationIcon: {
        fontSize: "1rem",
        marginLeft: "-0.5rem"
    },
    title: {
        height: "100%",
        maxHeight: "56px",
        fontSize: "14px",
    },
    ellipsis: {
        display: "-webkit-box",
        "-webkit-box-orient": "vertical",
        "-webkit-line-clamp": 2,
        overflow: "hidden",
        textOverflow: "ellipsis",
    },
    description: {
        //fontSize: "0.8rem",
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
            <Grid container display='flex' direction='column' >

                <Box className='container-row' style={{width: '100%'}} justifyContent='space-between'>
                    <Box  className={classes.logo}>
                        <img src = {company.logo_url ? company.logo_url : companyPlaceholder} alt="company" style={{height:'98%'}}/>
                    </Box>

                    <Grid style={{alignSelf:'flex-start'}} >
                        <div className={classes.bookmark} onClick={handleBookmark} >
                            {bookmarked === true ? <BookmarkIcon style={{color: '#FF3750'}}/> : <BookmarkBorderIcon  />}
                        </div>
                    </Grid>
                </Box>

                <Box style={{marginTop: '1rem', width: '100%'}}>
                    <Typography className={clsx(classes.title, classes.ellipsis)} variant="h6">
                        { company.name }
                    </Typography>
                </Box>
                <Box style={{marginTop: '0.5rem', width: '100%'}}>
                    <Typography className={clsx(classes.title, classes.ellipsis)} variant="caption">
                        {company.title}
                    </Typography>
                </Box>
                <Box style={{marginTop: '1.5rem' , width:'100%'}} className='container-row'>
                    <Box style={{marginRight: '0.4rem', color:'black'}}>
                        <WorkOutline />
                    </Box>
                    <Typography className={classes.description} style={{ color: 'black' }} variant="body2">
                        {company.jobs_count} Live jobs
                    </Typography>
                </Box>
                <Box style={{marginTop: '0.5rem'}} className='container-row'>
                    <Box style={{marginRight: '0.4rem', color:'black'}}>
                        <RecentActors />
                    </Box>
                    <Typography className={classes.description} variant="body2" style={{color:'black'}}>
                        {company.size} Employers
                    </Typography>
                </Box>
               
            </Grid> 
    
    )
}

export default Company;