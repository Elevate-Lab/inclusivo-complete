import React from 'react'
import { FormControl, FormControlLabel, Checkbox as MuiCheckbox } from '@material-ui/core';

export default function Checkbox({name, label, values, setValues}) {

    return (
        <FormControl style={{marginTop:"12px"}}>
            <FormControlLabel
                control={<MuiCheckbox
                    name={name}
                    color="primary"
                    checked={values[name]}
                    onChange={event => setValues({
                        ...values,
                        [name]: !values[name]
                    })}
                />}
                label={label}
            />
        </FormControl>
    )
}