import React from 'react';
import {
    makeStyles,
    Typography
} from '@material-ui/core';
import ClearIcon from '@material-ui/icons/Clear';
const useStyles = makeStyles( theme => ({
    tags:{
        display: "flex",
        flexWrap: "wrap"
    },
    tag:{
       display: "flex",
       padding:"0.5% 3%",
       border: "1px solid #FF3750",
       borderRadius: "20px",
       marginRight: "2%",
       marginBottom: "2%",
       fontSize: "0.9rem",
       cursor: "pointer",
       userSelect: "none"
    },
    clear: {
        paddingLeft: "0.5rem"
    }
}));

const Tags = ({tags, clear, name}) => {
    const classes = useStyles();

    const makeTag = (tag, idx) => {
        return (
            <Typography key={idx} className={classes.tag}>
                {tag.name}
                <ClearIcon 
                    fontSize="small" 
                    className={classes.clear} 
                    onClick={ () => {clear(name, tag)}}
                    />
            </Typography>
        )
    }
    return(
        
        <div className={classes.tags}>
            {tags.map(makeTag)}
            
        </div>
    )
}

export default Tags;