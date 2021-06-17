import React from 'react'
import {
    makeStyles,
    Grid,
    FormControl,
    Typography,
    TextField,
    Tooltip,
    ClickAwayListener
} from '@material-ui/core'
import {
    Error
} from '@material-ui/icons'
import clsx from 'clsx'

const useStyles = makeStyles(theme => ({
    root: {
        width: "100%",
        '& .MuiInputBase-root': {
            display: "block"
        }
    },
    smallForm: {
        maxWidth: "360px",
        minWidth: "140px",
        flex: "1 1",
        marginRight: "20px"
    },
    singleLineInput: {
        '& .MuiOutlinedInput-input':{
            background: "#fafafa",
            borderRadius: "5px",
            padding: "10px 10px",
            width: "calc(100% - 20px)"
        }
    },
    input:{
        fontSize: "14px",
        '& .MuiOutlinedInput-multiline':{
            background: "#fafafa",
            padding: "10px 10px",
            borderRadius: "5px",
            minHeight: "100px"
        },
        "& .MuiOutlinedInput-root .MuiOutlinedInput-notchedOutline":{
            border: "1px solid #e6e6e6 !important",
        },
        "&:hover .MuiOutlinedInput-root .MuiOutlinedInput-notchedOutline": {
            borderColor: "#76B7F3 !important"
        },
        "&:focus-within .MuiOutlinedInput-root .MuiOutlinedInput-notchedOutline": {
            border: "2px solid #76B7F3 !important"
        },
    },
    error: {
        "& .MuiOutlinedInput-root .MuiOutlinedInput-notchedOutline":{
            border: "1px solid #f44336 !important",
        },
        "&:hover .MuiOutlinedInput-root .MuiOutlinedInput-notchedOutline": {
            borderColor: "#f44336 !important"
        },
        "&:focus-within .MuiOutlinedInput-root .MuiOutlinedInput-notchedOutline": {
            border: "2px solid #f44336 !important"
        },
    },
    // tooltip
    customWidth: {
        maxWidth: "360px",
        fontSize: "12px"
    },
    icon: {
        color: "#f44336"
    }
}))

function FormInput({value, handleChange, name, label, error=null, multiline, small, integer, placeholder="type here..", promptValue = ""}) {
    const classes = useStyles()

    const [open, setOpen] = React.useState(false)

    const handleTooltipClose = () => {
        setOpen(false);
    }

    const handleTooltipOpen = () => {
        setOpen(true);
    }

    React.useEffect(() => {
        setOpen(promptValue.length ? true : false)
        console.log("changes")
    }, [promptValue])

    return (
        <Grid item container direction="column" className={clsx({[classes.smallForm]: small})}>
            <Grid item container alignItems='center'>
                <Typography 
                    variant="h6" 
                    style={{fontSize: "14px", margin: "12px 0 8px 0", letterSpacing: "0.4px", flex: "1 1"}}
                    className={clsx({
                        [classes.smallForm]: small
                    })}
                >
                    {label}
                </Typography>
                {promptValue.length ? 
                    <ClickAwayListener onClickAway={handleTooltipClose}>
                        <Grid>
                            <Tooltip
                                PopperProps={{
                                disablePortal: true,
                                }}
                                onClose={handleTooltipClose}
                                open={open}
                                disableFocusListener
                                disableHoverListener
                                disableTouchListener
                                title={promptValue}
                                placement='top-end'
                                classes={{
                                    tooltip: classes.customWidth
                                }}
                            >
                                <Error className= {classes.icon} onClick={handleTooltipOpen}/>
                            </Tooltip>
                        </Grid>
                    </ClickAwayListener>
                :
                    null
                }
            </Grid>
            <FormControl 
                className={clsx(classes.root,{
                    [classes.smallForm]: small
                })}
            >
                <TextField
                    className={clsx(classes.input,{
                        [classes.singleLineInput]: !multiline,
                        [classes.error]: (error || promptValue.length) ? true : false
                    })}
                    value={value}
                    variant="outlined"
                    onChange={handleChange(integer)}
                    placeholder={placeholder}
                    name={name}
                    autoComplete="off"
                    multiline={multiline}
                    {
                        ...((error || promptValue.length) && {
                                        error: true,
                                        helperText: error            
                                    })
                    }
                />

            </FormControl>
        </Grid>
    )
}

export default FormInput
