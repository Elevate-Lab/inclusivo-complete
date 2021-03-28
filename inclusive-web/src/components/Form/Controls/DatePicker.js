import React from 'react'
import {makeStyles, Typography} from '@material-ui/core'
import {MuiPickersUtilsProvider, KeyboardDatePicker} from '@material-ui/pickers';
import 'date-fns';
import {format} from 'date-fns';
import DateFnsUtils from '@date-io/date-fns';

const useStyles = makeStyles(theme => ({
    root: {
        
    },
    dateFieldInput: {
        width: "100%",
        flex: "1 1",
        maxWidth: "360px",
        background: "#FAFAFA",
        '& .MuiOutlinedInput-root' : {
        borderRadius: "5px",
        },
        '& input' : {
            padding: "12px 14px",
            fontSize: "14px",
        },
        "& .MuiOutlinedInput-root .MuiOutlinedInput-notchedOutline": {
            borderColor: "#E6E6E6"
        },
        "&:hover .MuiOutlinedInput-root .MuiOutlinedInput-notchedOutline": {
            borderColor: "#76B7F3"
        },
        "& .MuiOutlinedInput-root.Mui-focused .MuiOutlinedInput-notchedOutline": {
            borderColor: "#76B7F3"
        },
        "& .MuiInputLabel-outlined": {
            color: "#E6E6E6"
        },
        "&:hover .MuiInputLabel-outlined": {
            color: "#E6E6E6",
        },
        "& .MuiIconButton-root":{
            padding:"8px",
            borderRadius: "0",
        },
        '& .MuiOutlinedInput-adornedEnd':{
            paddingRight: "0px"
        }
    }
}))

function DatePicker({values, setValues, name, label, onlyFuture}) {
    const classes = useStyles()

    const [selectedDate, setSelectedDate] = React.useState(new Date());

    const handleDateChange = (date) => {
        setSelectedDate(date)
        setValues({
            ...values,
            [name]: format(date,'yyyy-MM-dd')
        })
    }

    return (
        <>
            <Typography variant="h6" style={{fontSize: "14px", margin: "12px 0 8px 0", letterSpacing: "0.4px"}}>{label}</Typography>
            <MuiPickersUtilsProvider utils={DateFnsUtils}>
                <KeyboardDatePicker 
                    inputVariant="outlined"
                    id="date-picker-dialog"
                    name={name}
                    format="dd/MM/yyyy"
                    value={selectedDate}
                    onChange={handleDateChange}
                    KeyboardButtonProps={{'aria-label': 'change date'}}
                    className={classes.dateFieldInput}
                    {
                        ...(onlyFuture && {
                                minDate: new Date()
                            })
                    }
                    InputProps={{readOnly: true}}
                />
            </MuiPickersUtilsProvider>
        </>
    )
}

export default DatePicker
