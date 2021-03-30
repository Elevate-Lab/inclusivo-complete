import React from 'react';
import {
    Grid,
    makeStyles
} from '@material-ui/core'
import Skeleton from "react-loading-skeleton";
const useStyles = makeStyles((theme) => ({
    loaderCard: {
        background: "#fff",
        padding: "10px",
        margin: "10px auto",
        '&:hover': {
            boxShadow: "0px 0px 10px -2px rgba(0, 0, 0, 0.15)"
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
}))

const CompanyListingSkeleton = () => {
    const classes = useStyles();

    return(
        <Grid container>
            {
                [1, 2, 3, 4, 5].map((data, idx) => (
                    <Grid container item className={classes.loaderCard} key={idx}>
                        <Grid container item xs={12} justify="space-between">
                            <Grid item>
                                <Skeleton height={70} width={70} />
                            </Grid>
                            <Grid item>
                                <Skeleton height={14} width={14} />                            
                            </Grid>
                        </Grid>
                        <Grid item xs={12} style={{marginTop: "10px"}}>
                            <Skeleton height={20} width={250} />
                        </Grid>
                        <Grid item xs={12} style={{ marginTop: "10px" }}>
                            <Skeleton height={40} width={350}/>
                        </Grid>
                        <Grid item xs={12} container direction="column" style={{margin: "10px 0px"}}>
                            <Grid item xs={4} style={{ margin: "5px 0px" }}>
                                <Skeleton width={100} height={18} />
                            </Grid>
                            <Grid item xs={4} style={{ margin: "5px 0px" }}>
                                <Skeleton width={100} height={18} />
                            </Grid>
                        </Grid>
                    </Grid>
                ))
            }
        </Grid>
    )
}

export default CompanyListingSkeleton;