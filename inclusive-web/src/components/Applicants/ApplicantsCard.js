import React from 'react';
import {
    makeStyles
} from '@material-ui/core';
import {
    Avatar,
    Button,
    Typography,
    CardContent,
    Card,
    Grid
} from '@material-ui/core'
import Moment from 'react-moment';
import blank_image from '../../assets/blank_image.png'
import { Link } from 'react-router-dom';

const useStyles = makeStyles((theme) => ({
    root: {
        display: 'flex',
        boxShadow: 'none',
        margin: '4px 0',
        borderRadius: '5px',
        flexDirection: 'column',
        transitionDelay: "0s",
        cursor: "pointer",
        '& .MuiCardContent-root:last-child': {
            paddingBottom: "8px",
            paddingLeft: "0"
        },
        '&:hover': {
            boxShadow: "0px 2px 10px -1px rgba(64, 58, 58, 0.25)"
        },
        [theme.breakpoints.up('sm')]: {
            flexDirection: 'row'
        }
    },
    detailContainer: {
        display: "flex",
        flex: "1 1"
    },
    details: {
        display: 'flex',
        flexDirection: 'column',
        flex: "1 1"
    },
    content: {
        flex: '1 0 auto',
    },
    cover: {
        width: 76,
    },
    controls: {
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        paddingRight: theme.spacing(1),
        paddingBottom: theme.spacing(1),
    },
    playIcon: {
        height: 38,
        width: 38,
    },
}));

export default function ApplicantsCard({ applicant, applied_on, id, handleShowList }) {
    const classes = useStyles();

    // Methods
    const toFilter = (d) => {
        let date = '';
        if (d[0] === 'a') {
            date = '1' + d.slice(1)
            return date
        }
        else return d;
    }
    return (
        <Card className={classes.root} onClick={handleShowList(applicant)}>
            <div className={classes.detailContainer}>
                <Grid container justify="center" alignItems="center" className={classes.cover}>
                    <Avatar
                        alt="Remy Sharp"
                        src={applicant.candidate.user.photo_url ? applicant.candidate.user.photo_url : blank_image}
                        className={classes.small}
                    />
                </Grid>
                <div className={classes.details}>
                    <CardContent className={classes.content}>
                        <Typography>
                            {applicant.candidate.user.first_name} {applicant.candidate.user.last_name}
                        </Typography>
                        <Typography variant="subtitle1" color="textSecondary">
                            {applicant.candidate.country.name}
                            <span style={{ margin: "0 4px" }}>•</span>
                            <Moment filter={toFilter} fromNow>{applied_on}</Moment>
                        </Typography>
                    </CardContent>
                </div>
            </div>
            <div className={classes.controls}>
                <a href={applicant.candidate.resume_link}>
                    <Button ariaLabel="Resume Link">
                        <Typography variant="caption" display="block" style={{ color: "#4694E7" }}>
                            Resume Link
                        </Typography>
                    </Button>
                </a>
                <Link to={`/profile/${applicant.candidate.id}`}>
                    <Button ariaLabel="Profile Link">
                        <Typography variant="caption" display="block" style={{ color: "#4694E7" }}>
                            Profile Link
                        </Typography>
                    </Button>
                </Link>
            </div>
        </Card>
    );
}