import React from 'react'
import './styles.css'
import {
    Grid,
    makeStyles
} from '@material-ui/core'

const useStyles = makeStyles({
    loaderContainer:{
        height: "80px",
        width: "80px",
        boxShadow: "0px 0px 46px -8px rgba(0, 0, 0, 0.25)",
        borderRadius: "5px"
    }
})

function RainbowLoader() {
    const classes = useStyles()
    return (
        <Grid container justify='center' alignItems='center' style={{height: "calc(100vh - 140px)"}}>
            <Grid item container justify='center' alignItems='center' className={classes.loaderContainer}>
                <div class="Loading-progress"></div>
            </Grid>
        </Grid>
    )
}

export default RainbowLoader
