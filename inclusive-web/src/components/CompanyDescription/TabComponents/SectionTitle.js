import React from 'react'
import clsx from 'clsx'
import {
    makeStyles,
    Grid,
    Typography,
    Card,
    CardContent
} from "@material-ui/core"

const useStyles = makeStyles(theme => ({
    root: {
        position: "relative",
        overflow: "visible",
        borderRadius: "5px",
        boxShadow: "none",
        maxWidth: "200px",
        marginTop: "20px",
    },
    cover: {
        width: "40px",
        height: "40px",
        borderRadius: "5px",
        background: '#FD970F',
        position: "absolute",
        left: "16px",
        top: "-16px"
    },
    details: {
        display: 'flex',
        flexDirection: 'column',
        marginLeft: "4px",
        '& .MuiCardContent-root:last-child':{
            paddingBottom: '8px',
            paddingLeft: '72px'
        }
    },
    content: {
        flex: '1 0 auto',
        padding: "8px"
    },
    name: {
        fontWeight: "600",
        fontSize: "14px",
        color: "#3a3a3a",
        letterSpacing: "0.4px"
    }
}))

function SectionTitle({backgroundColor, image, name}) {
    const classes = useStyles()
    return (
        <Card 
            className={clsx(classes.root)}
        >
            <Grid
                item
                container
                justify="center"
                alignItems="center" 
                className={classes.cover} 
                style={{
                    background: backgroundColor
                }}
            >
                <img
                    alt="Remy Sharp" 
                    src={image}
                    style={{width: "22px"}}
                />
            </Grid>
            <div className={classes.details}>
                <CardContent className={classes.content}>
                    <Typography varaint="subtitle2" className={classes.name}>
                        {name}
                    </Typography>
                </CardContent>
            </div>   
        </Card>
    )
}

export default SectionTitle
