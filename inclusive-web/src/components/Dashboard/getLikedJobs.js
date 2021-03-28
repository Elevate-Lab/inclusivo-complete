import React from 'react';
import { baseUrl } from '../../urlConstants';
import {
    Grid,
} from '@material-ui/core';
import axios from 'axios';
import CommonCardSkeleton from '../Loaders/CommonCardSkeleton';
import Card from '../Listing/Card';
import {useStyles} from './Styles';

const LikedJobs = () => {
    const classes = useStyles();
    const [jobsData, setJobsData] = React.useState([]);
    const [loading, setLoading] = React.useState(true);
    const [error,setError] = React.useState(false);

    React.useEffect(() => {
        getJobs();
    }, []);

    const getJobs = async () => {
        let token = localStorage.getItem('key');
        await axios({
            url: `${baseUrl}/dashboard/get/liked_jobs`,
            method: "GET",
            headers: {
                "Authorization" : `token ${token}`
            }
        }).then((res) => {
            if(res.data.status === "OK"){
                setJobsData(res.data.data);
            } else {
                setError(true);
            }
            setLoading(false);
        }).catch(err => {
            setError(true);
        })
      }
      return(
          loading ? <CommonCardSkeleton type="job" /> 
          : (!loading && jobsData.length === 0) ? <div>Jobs Not Liked</div> 
          : (!loading && error) ? <div>Something Went Wrong! Try Again</div>
          : (!loading && jobsData.length > 0) ?<Grid xs={12} item style={{background: "#fafafa",width: "100%"}}>
            {jobsData.map((jobs,idx) => (
                <Grid key={idx} item className={classes.jobCard} style={{marginTop: "16px",marginBottom:"16px"}}>
                    <Card data={jobs.job_post} type="jobs" />
                </Grid>
            ))}
          </Grid>: <></>
      )
}

export default LikedJobs;