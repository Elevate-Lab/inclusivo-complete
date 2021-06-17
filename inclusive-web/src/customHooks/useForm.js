import React from 'react'
import retext from 'retext'
import equality from 'retext-equality'
import profanity from 'retext-profanities'

export default function useForm(initialValues, validateOnChange = true, validate, initialPromptValues={}) {
    
    const [values, setValues] = React.useState(initialValues)
    const [errors, setErrors] = React.useState({})
    const [name, setName] = React.useState('')

    // Gender neutral text prompt value
    const [getPromptValues, setGetPromptValues] = React.useState({})
    const [promptValues, setPromptValues] = React.useState(initialPromptValues)

    const handleChange = (integer) => event => {
        const {name, value} = event.target
        setName(name)

        // Set gender neutral text prompts value
        if(!integer && value){
            retext().use(equality).use(profanity).process(value, function(err, file) {
                setGetPromptValues(err || file)
            })
        }
        // end

        setValues({
            ...values,
            [name]: integer ? value ? parseInt(value) : value : value
        })
        if(validateOnChange){
            validate({ [name]: value })
        }
    }

    React.useEffect(() => {
        if(Object.keys(getPromptValues).length!=0){
            let profane = [];
            let neutral = [];
            let neutralErr = [];
            getPromptValues.messages.forEach((item) => {
                if(item.source === 'retext-equality'){
                    if(!neutralErr.includes(item.actual) && item.actual!=='working mother' && item.actual!=='mother'){
                        neutralErr.push(item.actual);
                    }
                    item.expected.forEach((correct) => {
                        if(!neutral.includes(correct) && item.actual!=='working mother' && item.actual!=='mother'){
                            neutral.push(correct);
                        }
                    })
                }
                if(item.source === 'retext-profanities'){
                    if(!profane.includes(item.actual)){
                        profane.push(item.actual);
                    }
                }
            })
            let str1 = '';
            let str2 = '';
            let str3 = '';
            profane.forEach((item) => {
                str1 = str1 + item + ', ';
            })
            neutralErr.forEach((item) => {
                str2 = str2 + item + (neutralErr.indexOf(item)===neutralErr.length-1 ? ' ' : ', ');
            })
            neutral.forEach((item) => {
                str3 = str3 + item + (neutral.indexOf(item)===neutral.length-1 ? ' ' : ', ');
            })
            let val1 = (str1.length ? `Please don't use ${str1}${profane.length>1 ? "these words are" : "it's" } profane. ` : ``)
            let val2 = (str2.length ? `Please try not to use " ${str2}", since ${neutralErr.length>1 ? "these words" : "this word"} may be insensitive to some users, use ${str3}instead. ` : ``)
            setPromptValues({
                ...promptValues,
                [name]: val1+val2
            })
        }
    }, [getPromptValues])
    
    return [
        values,
        setValues,
        errors,
        setErrors,
        handleChange,
        promptValues,
        setPromptValues
    ]
}

