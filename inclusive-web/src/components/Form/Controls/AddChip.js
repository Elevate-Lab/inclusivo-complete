import React from 'react'
import { 
    makeStyles,
    TextField,
    Grid,
    IconButton,
    Typography,
    Chip
} from '@material-ui/core'
import {
    AddRounded,
    ClearRounded
} from '@material-ui/icons'
import {
    Autocomplete
} from '@material-ui/lab'
import clsx from 'clsx'


const useStyles = makeStyles(theme => ({
    root: {
        '& .MuiAutocomplete-inputRoot': {
            background: "#fafafa",
            paddingTop: "1px",
            paddingBottom: "1px"
        },
        '& .MuiAutocomplete-listbox': {
            overflowX: "hidden"
        },
        flex: "1 1",
        maxWidth: "360px"
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
    addBtn: {
        background: "#fafafa",
        borderRadius: "5px",
        height: "40px",
        width: "40px",
        boxShadow: "0px 0px 6px rgba(0, 0, 0, 0.12)",
        marginLeft: "12px"
    },
    disabled: {
        boxShadow: "none",
        background: "#fafafa",

    },
    chip: {
        borderRadius: "5px",
        margin: "6px 8px 0 0px",
        fontWeight :"600",
        background: "#e6e6e6"
    }
}))

function AddChip({data, values, setValues, name, label}) {

    const classes = useStyles()

    const [chipInputValue, setChipInputValue] = React.useState('')
    const [chipValue, setChipValue] = React.useState(null)
    
    // handle input change
    const handleChipValueChange = (event, newValue) => {
        setChipValue(newValue)
    }

    const handleChipInputValueChange = (event, newInputValue) => {
        setChipInputValue(newInputValue)
    }

    // handle info change
    const handleClick = () => {
        setValues({
            ...values,
            [name]: values[name].includes(chipValue) ? values[name] : [...values[name], chipValue]
        })
        setChipValue(null)
        setChipInputValue("")
    }

    const handleDeleteChip = (chip) =>() => {
        setValues({
            ...values,
            [name]: values[name].filter(val => val.id !== chip.id)
        })
    }

    const getLabel = (data,name) => {
        switch (name){
            case "locations":
                return data.name + ", " + data.state_name + ", " + data.country_name
            case "degrees":
                return data.degree_name + ", " + data.degree_type
            case "preferred_city":
                return data.name + ", " + data.state_name + ", " + data.country_name
            default:
                return data.name
        }
    }

    return (
        <>  
            <Grid container style={{fontSize: "14px", margin: "6px 0 8px 0"}}>
                {values[name].map((chip) => {
                    return (
                        <Chip
                            key={chip.id}
                            label = {getLabel(chip, name)}
                            onDelete={handleDeleteChip(chip)}
                            className={classes.chip}
                        />
                    )
                })}
            </Grid>
            <Typography variant="h6" style={{fontSize: "14px", margin: "8px 0 8px 0", letterSpacing: "0.4px"}}>{label}</Typography>
            <form 
                onSubmit={handleClick} 
                value={chipValue}
            >
                <Grid container>
                    <Autocomplete 
                        value={chipValue}
                        onChange={handleChipValueChange}
                        inputValue={chipInputValue}
                        onInputChange={handleChipInputValueChange}
                        renderOption={data => <React.Fragment>{getLabel(data, name)}</React.Fragment>}
                        getOptionLabel={data => getLabel(data, name)}
                        id="controllable-states-demo"
                        options={data}
                        style={{ width: 300 }}
                        className={classes.root}
                        renderInput={
                            (params) => 
                                <TextField 
                                    {...params}  
                                    className={clsx(classes.input, classes.singleLineInput)}
                                    value={chipValue}
                                    variant="outlined"
                                />
                        }
                    />
                    <IconButton
                        disabled={chipValue ? false  : true}
                        disableRipple
                        className={clsx(classes.addBtn,{
                            [classes.disabled]: chipValue ? false : true
                        })}
                        onClick={handleClick}
                        type="submit"
                    >
                        <AddRounded fontSize="small"/>
                    </IconButton>
                </Grid>
            </form>
        </>
    )
}

export default AddChip
