import React from 'react'
import {
    makeStyles,
    Grid,
    Typography,
    IconButton
} from '@material-ui/core'
import {
    ClearRounded
} from '@material-ui/icons'
import clsx from 'clsx'

const useStyles = makeStyles(theme => ({
    root: {
        padding: "12px 16px",
        background: "#fff",
        marginTop: "12px",
        minHeight: "200px",
        position: "relative"
    },
    closeBtn: {
        background: "#FAFAFA",
        borderRadius: "5px",
        padding: "6px"
    },
    initiativeName:{
        maxHeight: "40px",
        fontWeight: "600",
        fontSize: "14px",
        color: "#3a3a3a",
        letterSpacing: "0.4px"
    },
    initiativeDesc: {
        // color: "#8a8a8a",
        fontSize: "13px",
        maxHeight: "40px",
        width: "85%"
    },
    position:{
        position: "absolute",
        right: "10px",
        top: "10px"
    },
    margin: {
        marginTop: "0"
    }
}))

function InitiativeDecription({initiativeInfo, handleShowList, overview}) {
    const classes=useStyles()
    return (
        <Grid container direction="column" className={clsx(classes.root,{[classes.margin]: !overview})}>
            <Grid item container justify="flex-end" className={classes.position}>
                <IconButton disableRipple onClick={handleShowList({})} className={classes.closeBtn}>
                    <ClearRounded fontSize="small"/>
                </IconButton>
            </Grid>
            <Grid>
                <Typography variant="body2" className={clsx(classes.initiativeName)} gutterBottom>
                    {initiativeInfo.name}
                </Typography>
                <Typography variant="body2" className={clsx(classes.initiativeDesc)}>
                    {initiativeInfo.description}
                </Typography>
            </Grid>
        </Grid>
    )
}

export default InitiativeDecription
