import React from 'react'
import {
    Grid,
    Typography,
    makeStyles,
    Button
} from '@material-ui/core'
import {
    ExpandMore,
    ExpandLess,
} from '@material-ui/icons'
import { Link } from 'react-router-dom'

const useStyles = makeStyles(theme => ({
    aboutContainer: {
        marginBottom: "5px",
        marginTop: "5px",
        background: "#fff",
        borderRadius: "3px",
        width: '100%',
        paddingTop: "10px"
    },
    paddingClass: {
        padding: "0 0 10px 20px"
    },
    buttonContainer: {
        marginTop: "4px",
        padding: "10px 20px",
        boxShadow: "0px -1px 10px -5px rgba(0, 0, 0, 0.15)",
        borderRadius: "0px 0px 5px 5px",
    },
    btn: {
        background: "#fff",
        border: "none",
        '&:focus': {
            outline: "none"
        },
        display: "flex",
        alignItems: "center"
    },
}))

function Accordian({ data, title, button = false, to = "", buttonName = "" }) {
    const classes = useStyles()

    const [descButtonStatus, setDescButtonStatus] = React.useState({
        text: 'Read More',
        expanded: false
    })

    const handleReadMore = () => {
        if (!descButtonStatus.expanded) {
            setDescButtonStatus({
                text: "Read Less",
                expanded: true
            })
        } else {
            setDescButtonStatus({
                text: "Read More",
                expanded: false
            })
        }
    }

    return (
        <Grid item container direction="column" className={classes.aboutContainer}>
            <Grid item container>
                <Typography variant="h6" className={classes.paddingClass}>
                    {title}
                </Typography>
            </Grid>
            <Grid item container direction="column">
                {
                    data.length > 200 ? (
                        <>
                            <Grid xs={12} item className={classes.paddingClass}>
                                <Typography variant="body2">
                                    {data.substring(0, 200)}<span id="dots" style={
                                        descButtonStatus.expanded ? { display: "inline" } : { display: "none" }
                                    } ></span><span style={
                                        descButtonStatus.expanded ? { display: "inline" } : { display: "none" }
                                    } > {data.substring(201, data.length - 1)} </span>
                                </Typography>
                                {button && descButtonStatus.expanded &&
                                    <Link to={to}>
                                        <Button ariaLabel={buttonName}>{buttonName}</Button>
                                    </Link>
                                }
                            </Grid>
                            <Grid xs={12} item className={classes.buttonContainer}>
                                <button className={classes.btn} onClick={handleReadMore} ariaLabel={descButtonStatus.text}>
                                    <span>{descButtonStatus.text}</span>  {
                                        descButtonStatus.expanded ? (
                                            <ExpandLess />
                                        ) : (
                                            <ExpandMore />
                                        )
                                    }
                                </button>
                            </Grid>
                        </>
                    ) : (
                        <>
                            <Grid xs={12} item className={classes.paddingClass}>
                                <Typography variant="body2">
                                    {data}
                                </Typography>
                                {button &&
                                    <Link to={to}>
                                        <Button>{buttonName}</Button>
                                    </Link>}
                            </Grid>
                        </>
                    )
                }
            </Grid>
        </Grid>
    )
}

export default Accordian
