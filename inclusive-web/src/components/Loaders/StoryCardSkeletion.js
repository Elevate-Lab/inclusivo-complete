import React from 'react'
import {
    Grid,
    makeStyles
} from '@material-ui/core'
import Skeleton from "react-loading-skeleton"

const useStyles = makeStyles((theme) => ({
    loaderCard: {
        width: "calc(33.33% - 12px)",
        background: "#fff",
        margin: "18px 20px",
        marginBottom: "0",
        borderRadius: "5px",
        height: "340px",
        maxWidth: "280px",
        '&:hover':{
            boxShadow: "0px 0px 10px -2px rgba(0, 0, 0, 0.15)"
        },
        [theme.breakpoints.down('sm')]: {
            width: "calc(50% - 12px)",
        },
        [theme.breakpoints.down('xs')]: {
            width: "100%"
        },
    }
}))

function StoryCardSkeleton() {
    const classes = useStyles()

    return (
        <Grid container justify="center">
            {[1,2,3].map((data) => {
                return (
                    <Grid item container direction="column" className={classes.loaderCard}>
                        <Grid>
                            <Skeleton height={152} style={{width:"100%", lineHeight: 2}}/>
                        </Grid>
                        <Grid style={{padding: "8px 12px", minHeight: "142px"}}>
                            <Skeleton height={16} style={{width: "70%"}} />
                            <Skeleton height={16} style={{width: "80%", marginTop: "12px"}} />
                            <Skeleton height={16} style={{width: "60%", marginTop: "2px"}} />
                            <Skeleton height={16} style={{width: "70%", marginTop: "2px"}} />
                            <Skeleton height={16} style={{width: "60%", marginTop: "2px"}} />
                        </Grid>
                        <Grid style={{padding: "6px 12px"}}>
                            <Skeleton height={22} width={80} />
                        </Grid>
                    </Grid>
                )
            })}
        </Grid>
    )
}

export default StoryCardSkeleton
