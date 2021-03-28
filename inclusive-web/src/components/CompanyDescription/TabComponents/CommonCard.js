import React from 'react'
import {
    makeStyles,
    Grid,
    Typography,
    IconButton
} from '@material-ui/core'
import {
    BookmarkBorderOutlined,
    ShareOutlined,
    LocationOn
} from '@material-ui/icons'
import companyImage from '../../../assets/Frame43.png'
import clsx from 'clsx'
import {toFilter} from '../../../helpers/methods'
import Moment from 'react-moment'
import Tags from '../../Listing/Tags'


const useStyles = makeStyles(theme => ({
    itemContainer: {
        padding: "8px",
        background: "#fff",
        marginTop: "12px",
        borderRadius: "5px",
        '&:hover':{
            boxShadow: "0px 0px 10px -2px rgba(0, 0, 0, 0.15)"
        },
        color: "#000"
    },
    logo: {
        width: "68px",
        marginRight: "10px"
    },
    btn: {
        background: "#FAFAFA",
        borderRadius: "5px",
        padding: "6px"
    },
    title:{
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
    locationIcon: {
        fontSize: "13px",
        color: "#6a6a6a",
        marginRight: "4px"
    },
    tagContainer: {
        paddingTop: "8px"
    }
}))

function CommonCard({data, type}) {
    const classes = useStyles()

    return (
        <Grid container item justify="center" className={classes.itemContainer}>
            <Grid item container direction="row">
                <Grid xs={10} item container wrap="nowrap" alignItems="center">
                    <Grid item style={{marginLeft: "4px"}}>
                        <img 
                            src={data.company.logo_url ? data.company.logo_url!=="" ? data.company.logo_url : companyImage : companyImage} 
                            alt="company logo" 
                            className={classes.logo}
                            {
                                ...(type==="scholarship" && {
                                        style: {width: "56px"}
                                    })
                            }
                        />
                    </Grid>
                    <Grid item container direction="column" justify="space-between" style={{marginLeft: "4px"}}>
                        <Grid item>
                            <Typography 
                                variant="h6" 
                                className={clsx(classes.title, classes.ellipsis)}
                            >
                                {data.title}
                            </Typography>
                        </Grid>
                        {type==="job" &&
                        <Grid item>
                            <Typography variant="caption">
                                <Grid item container alignItems="center">
                                    {data.accepted_locations.length > 0 && <><LocationOn className={classes.locationIcon} /> 
                                    {data.accepted_locations.map((location, idx) => (
                                            <span key={idx} style={{marginRight: "5px"}}>{location.name}</span>
                                        ))}
                                    </>}
                                </Grid>
                            </Typography>
                        </Grid>}
                        <Grid item>
                            <Typography variant="caption">
                                {type==="job" ? `${data.job_type} •` : ""} Vacancy - {data.vacancies} • <Moment filter={toFilter} fromNow>{data.posted_on}</Moment>
                            </Typography>
                        </Grid>
                    </Grid>
                </Grid>
                <Grid xs={2} item alignItems="flex-end" container direction="column">
                    <IconButton disableRipple className={classes.btn} style={{marginBottom: "6px"}}>
                        <BookmarkBorderOutlined fontSize='small' />
                    </IconButton>
                    <IconButton disableRipple className={classes.btn}>
                        <ShareOutlined fontSize='small' />
                    </IconButton>
                </Grid>
            </Grid>
            <Grid item container style={{marginTop: "4px"}}>
                <Typography varaint="subtitle2" className={clsx(classes.jobRole, classes.ellipsis)}>
                    {
                        type==="job" ? data.job_role : data.description
                    }
                </Typography>
            </Grid>
            <Grid item container xs={12} className={classes.tagContainer}>
                {
                    data.tags.length > 0 && <Tags data={data} />
                }
            </Grid>
        </Grid>
    )
}

export default CommonCard
