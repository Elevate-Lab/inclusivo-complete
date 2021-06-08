import React from 'react'
import {
    Button,
    Typography,
    makeStyles
} from '@material-ui/core'

const useStyles = makeStyles(theme => ({
    btn: {
        background: "#FFEAEC",
        borderRadius: "5px",
        marginTop: "10px",
        width: "142px",
        "&:hover": {
            background: "#ff3750",
            "& .MuiTypography-h6": {
                color: "#fff !important",
            }
        }
    },
    btnText: {
        fontSize: "12px",
        color: "#ff3750",
    }
}))

function ViewAll({ handleChange, value }) {
    const classes = useStyles()
    return (
        <Button
            disableRipple
            className={classes.btn}
            onClick={handleChange(value)}
            ariaLabel="View All"
        >
            <Typography variant="h6" className={classes.btnText}>
                View All
            </Typography>
        </Button>
    )
}

export default ViewAll
