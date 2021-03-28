import React from 'react'
import {
    makeStyles
} from '@material-ui/core'

const useStyles = makeStyles(theme => ({
    root: {
        width: "100%"
    }
}))

function Form(props) {
    const classes = useStyles()

    return (
        <form className={classes.root}>
            {props.children}
        </form>
    )
}

export default Form
