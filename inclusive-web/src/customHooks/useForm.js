import React from 'react'

export default function useForm(initialValues, validateOnChange = true, validate) {
    
    const [values, setValues] = React.useState(initialValues)
    const [errors, setErrors] = React.useState({})

    const handleChange = (integer) => event => {
        const {name, value} = event.target
        setValues({
            ...values,
            [name]: integer ? value ? parseInt(value) : value : value
        })
        if(validateOnChange){
            validate({ [name]: value })
        }
    }
    
    return [
        values,
        setValues,
        errors,
        setErrors,
        handleChange
    ]
}

