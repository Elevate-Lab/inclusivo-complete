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

const Degrees = ({degrees, clear, name}) => {
    const classes = useStyles();

    const makeTag = (degree, idx) => {
        return (
            <Typography key={idx} className={classes.tag}>
                {degree.degree_name}, {degree.degree_type}
                <ClearIcon 
                    fontSize="small" 
                    className={classes.clear} 
                    onClick={ () => {clear(name, degree)}}
                    />
            </Typography>
        )
    }
    return(
        
        <div className={classes.tags}>
            {degrees.map(makeTag)}
            
        </div>
    )
}

export default Degrees;