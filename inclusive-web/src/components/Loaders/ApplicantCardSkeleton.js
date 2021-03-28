import React from 'react'
import {
    Grid,
    makeStyles
} from '@material-ui/core'
import Skeleton from "react-loading-skeleton"

const useStyles = makeStyles((theme) => ({
    loaderCard: {
      height:"86px",
      width:"100%", 
      backgroundColor:"#fff", 
      margin: "4px 0",
      borderRadius: "5px"
    }
}))

function LoaderCard() {
    const classes = useStyles()

    return (
        <Grid container justify="center" direction="column">
            <Grid item container justify="center" className={classes.loaderCard}>
                <Grid item container justify="center" alignItems="center" style={{width:"64px"}}>
                    <Skeleton circle={true} width={35} height={35} />
                </Grid>
                <Grid item container direction="column" justify="center" style={{flex: "1 1"}}>
                    <Skeleton width={160} />
                    <Skeleton width={100} style={{marginTop:"8px"}}/>
                </Grid>
            </Grid>
            <Grid item container justify="center" className={classes.loaderCard}>
                <Grid item container justify="center" alignItems="center" style={{width:"64px"}}>
                    <Skeleton circle={true} width={35} height={35} />
                </Grid>
                <Grid item container direction="column" justify="center" style={{flex: "1 1"}}>
                    <Skeleton width={160} />
                    <Skeleton width={100} style={{marginTop:"8px"}}/>
                </Grid>
            </Grid>
        </Grid>
    )
}

export default LoaderCard
