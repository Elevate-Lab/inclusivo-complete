import React from 'react';
import axios from 'axios';
import { baseUrl } from '../../urlConstants';
import { useStyles } from './Styles';
import { Grid, Button, Box } from '@material-ui/core';
import { Link } from 'react-router-dom';
import DashboardJobsSkeleton from '../Loaders/DashboardJobsSkeleton';
import CompanyListingSkeleton from '../Loaders/CompanyListingSkeleton';
import loadable from '@loadable/component';
const CardComponent = loadable(() => import('../Listing/Card'));
const CompanyComponent = loadable(() => import('../Company/Company'));

const Lists = ({ type }) => {
    const classes = useStyles();
    const [data, setData] = React.useState([]);
    const [isMore, setIsMore] = React.useState(false);
    const [isMoreClicked, setMoreClicked] = React.useState(false);
    const [isLoading, setIsLoading] = React.useState(true);
    const handleViewMore = () => {
        setMoreClicked(!isMoreClicked);
    }
    const [noOfItems, setNoOfItems] = React.useState(0);

    React.useEffect(() => {
        const fetchJobs = async () => {
            let token = localStorage.getItem('key');
            let url = '';
            if (type === "applied_jobs") {
                url = `${baseUrl}/job/candidate`
            } else if (type === "preferred_city") {
                url = `${baseUrl}/dashboard/get/jobs_location/`
            } else if (type === "subscribed_companies") {
                url = `${baseUrl}/dashboard/get/subscribed_companies`
            }
            await axios({
                method: "get",
                url: url,
                headers: {
                    "Authorization": `token ${token}`
                }
            })
                .then(res => {
                    setIsLoading(false);
                    setData(res.data.data);
                    setNoOfItems(res.data.data.length);
                    if (res.data.data.length > 3) {
                        setIsMore(true);
                    } else {
                        setIsMore(false);
                    }
                })
        }
        fetchJobs();
    }, [type])
    return (
        <>
            {(isLoading && (type === "applied_jobs" || type === "preferred_cities")) ? <DashboardJobsSkeleton type={type} /> : (isLoading && type === "subscribed_companies") ? <CompanyListingSkeleton /> : (!isLoading && data.length > 0) ? data.slice(0, 3).map((data, idx) => (
                <>{(type === "applied_jobs" || type === "preferred_city") && <Grid key={idx} item container className={noOfItems >= 3 ? classes.jobCard : noOfItems === 2 ? classes.jobCard2 : classes.jobCard3}>
                    <CardComponent type="jobs" data={type === "applied_jobs" ? data.job : data} status={type === "applied_jobs" ? data.status : null} />
                </Grid>}
                    {type === "subscribed_companies" && <Grid item className={classes.companyBox} key={idx}>
                        <Link to={`/home/company/${data.subscribed_company.company.id}`}>
                            <CompanyComponent company={data.subscribed_company.company} />
                        </Link>
                    </Grid>}</>
            )) : (!isLoading && data.length === 0) && (<div>
                Nothing to Show
            </div>)}
            {isMoreClicked && data.length > 0 && data.slice(3, data.length).map((data, idx) => (
                <>{(type === "applied_jobs" || type === "preferred_city") && <Grid key={idx} item className={classes.jobCard}>
                    <CardComponent type="jobs" data={type === "applied_jobs" ? data.job : data} />
                </Grid>}
                    {type === "subscribed_companies" && <Grid item className={classes.companyBox} key={idx}>
                        <Link to={`/home/company/${data.subscribed_company.company.id}`}>
                            <CompanyComponent company={data.subscribed_company.company} />
                        </Link>
                    </Grid>}</>
            ))}
            {isMore && <Grid item xs={12} className={classes.buttonContainer}>
                {!isMoreClicked ? (<Button style={{ color: "red", background: "rgba(255, 234, 236, 1)", width: "100px" }} ariaLabel="View All" onClick={handleViewMore}>
                    View All
                </Button>
                ) : (<Button style={{ color: "red", background: "rgba(255, 234, 236, 1)", width: "100px" }} ariaLabel="Close" onClick={handleViewMore}>
                    Close
                </Button>
                )}
            </Grid>
            }
        </>
    )
}

export default Lists;