import React from 'react'
import {
    Button,
    Typography,
    CircularProgress,
    makeStyles
} from '@material-ui/core'

const useStyles = makeStyles(theme => ({
    btn: {
        padding: "6px 12px",
        margin: "36px 10px 36px 0",
        maxWidth: "180px",
        flex: "1 1",
        position: "relative"
    },
    buttonProgress: {
        color: theme.palette.primary,
        position: 'absolute',
        top: '50%',
        left: '50%',
        marginTop: -8,
        marginLeft: -8,
    }
}))

function StatusButton({handleSubmit, msg, status, disable, processing}) {
    const classes = useStyles()
    return (
        <Button 
            onClick={handleSubmit(status)}
            className={classes.btn}
            disableRipple
            disabled={disable}
            style={{background: !disable ? status==="Draft" ? "#06B0C5" : "#4AA64E" : "#e6e6e6"}}
        >
            <Typography 
                variant="h6" 
                style={{fontSize: "12px", letterSpacing: "0.4px", color: "#fff"}}
            >
                {msg}
            </Typography>
            {processing && <CircularProgress size={16} className={classes.buttonProgress} />}
        </Button>
    )
}

export default StatusButton
