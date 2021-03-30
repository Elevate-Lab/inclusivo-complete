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
        marginTop: "10px",
        '&:hover': {
            boxShadow: "0px 0px 10px -2px rgba(0, 0, 0, 0.15)"
        },
        marginLeft: theme.spacing(2),
        marginRight: theme.spacing(2),
        [theme.breakpoints.up('lg')]: {
            marginLeft: theme.spacing(1),
            marginRight: theme.spacing(1),
            flexBasis: "31%",
        },
        [theme.breakpoints.between("sm", "md")]: {
            marginLeft: theme.spacing(1),
            marginRight: theme.spacing(1),
            flexBasis: "44%",
        }, [theme.breakpoints.between("xs", "sm")]: {
            flexBasis: "100%",
        }
    }
}))

const DashboardJobsSkeleton = ({type}) => {
    const classes = useStyles();

    return (
        <Grid container justify="center">
            {
                [1, 2, 3].map((data, idx) => (
                    <Grid container item justify="center" className={classes.loaderCard} key={idx}>
                        <Grid item container direction="row">
                            <Grid xs={10} item container wrap="nowrap" alignItems="center">
                                <Grid item style={{ marginLeft: "8px" }}>
                                    <Skeleton
                                        width={type === "job" ? 76 : 56}
                                        height={type === "job" ? 76 : 56}
                                    />
                                </Grid>
                                <Grid item container direction="column" justify="space-between" style={{ marginLeft: "14px" }}>
                                    <Grid item>
                                        <Skeleton height={16} style={{ width: "80%" }} />
                                    </Grid>
                                    {type === "jobs" &&
                                        <Grid item>
                                            <Skeleton height={14} style={{ width: "60%", marginTop: "6px" }} />
                                        </Grid>
                                    }
                                    <Grid item>
                                        <Skeleton height={14} style={{ width: "50%", marginTop: "6px" }} />
                                    </Grid>
                                </Grid>
                            </Grid>
                            <Grid xs={2} item alignItems="flex-end" container direction="column">
                                <Skeleton height={28} width={28} />
                                <Skeleton height={28} width={28} style={{ marginTop: "6px" }} />
                            </Grid>
                        </Grid>
                        <Grid item container direction="column" style={{ marginTop: "12px", marginLeft: "8px" }}>
                            <Skeleton height={14} style={{ width: "70%" }} />
                        </Grid>
                        <Grid item container xs={12} style={{ marginTop: "8px", marginLeft: "8px" }}>
                            <Skeleton height={14} width={80} style={{ marginRight: "6px" }} />
                            <Skeleton height={14} width={100} style={{ marginRight: "6px" }} />
                            <Skeleton height={14} width={80} />
                        </Grid>
                    </Grid>
                ))
            }
        </Grid>
    )
}

export default DashboardJobsSkeleton;