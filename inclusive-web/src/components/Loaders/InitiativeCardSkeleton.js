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
        margin: "12px 6px",
        marginBottom: "0",
        borderRadius: "2px",
        padding: "8px",
        minHeight: "108px",
        cursor: "pointer",
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

function InitiativeCardSkeleton() {
    const classes = useStyles()

    return (
        <Grid container justify="center">
            {[1,2,3,4,5,6].map((data) => {
                return (
                    <Grid item container direction="column" className={classes.loaderCard} key={data}>
                        <Grid item container direction="column">
                            <Skeleton style={{width: "80%", height:"16px"}} />
                        </Grid>
                        <Grid item container direction="column" justify="center">
                            <Skeleton style={{marginTop:"14px", width: "90%", height:"14px"}}/>
                            <Skeleton style={{marginTop:"6px", width: "80%", height:"14px"}}/>
                        </Grid>
                    </Grid>
                )
            })}
        </Grid>
    )
}

export default InitiativeCardSkeleton
