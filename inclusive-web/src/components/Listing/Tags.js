import React from 'react';
import {
    Grid,
    Chip,
    makeStyles
} from '@material-ui/core'

const useStyles = makeStyles(theme => ({
    chip: {
        fontSize: "11px",
        fontWeight: "600",
        color: "#ff3750",
        height: "20px",
        border: "1px solid #ff3750",
        margin: "4px 6px",
        letterSpacing: "0.5px"
    }
}))

const Tags = ({data}) => {
    const classes= useStyles();

    return(
        data.tags.length > 0 && data.tags.map((tags, idx) => (
            <Grid item key={idx}>
                <Chip
                    label={tags.name}
                    variant="outlined"
                    className={classes.chip}
                    color="secondary"
                />
            </Grid>
        ))
    )
}

export default Tags;