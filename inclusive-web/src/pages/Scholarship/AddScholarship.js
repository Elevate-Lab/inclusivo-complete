import React from 'react'
import Controls from '../../components/Form/Controls/Controls'
import useForm from '../../customHooks/useForm'
import {useDispatch, useSelector} from 'react-redux'
import {getUserDetails} from '../../actions/userDetails/user.actions';
import {Grid, makeStyles, Button, Typography} from '@material-ui/core'
import {tagList} from '../../components/Scholarships/TagList'
import {baseUrl} from '../../urlConstants'
import {format} from 'date-fns';
import StatusButton from '../../components/SubmitButtons/StatusButton';
import SubmitStatusSnackbar from '../../components/SnackBars/SubmitStatusSnackbar';

const useStyles = makeStyles((theme)=>({
    root:{
        maxWidth:"100%", 
        width: "100%", 
        padding: "10px 30px"
    },
    subHeading: {
        fontSize: "14px",
        color: "#838383",
        marginTop: "36px",
        letterSpacing: "0.5px"
    },
    btn: {
        padding: "6px 12px",
        margin: "36px 10px 36px 0",
        maxWidth: "180px",
        flex: "1 1"
    }
}))

const initialValues = {
    company_id: null,
    title: "",
    description: "",
    short_code: "",
    degrees: [],
    tags: [],
    is_apply_here: true,
    apply_url: "",
    last_date: format(Date.now(),'yyyy-MM-dd'),
    selection_process: "",
    status: "Draft",
    vacancies: null
}

const initialPromptValues = {
    title: "",
    description: "",
    selection_process: ""
}

function AddJob() {

    const classes = useStyles()

    /* ---------------- validate function ------------------- */
    const validate = (fieldValues = values) => {
        const error = {...errors}
        if ("title" in fieldValues)
            error.title = fieldValues.title ? "" : "*Scholarship Title should not be empty"
        if ("description" in fieldValues)
            error.description = fieldValues.description ? "" : "*Scholarship Description should not be empty"
        if ("selection_process" in fieldValues)
            error.selection_process = fieldValues.selection_process ? "" : "*Selection Process should not be empty"
        if ("vacancies" in fieldValues)
            error.vacancies = fieldValues.vacancies ? parseInt(fieldValues.vacancies)>0 ? "" : "*Should be greater than 0" : "*Required"
        if ("apply_url" in fieldValues)
            error.apply_url = fieldValues.apply_url ? "" : "*Apply URL should not be empty"

        setErrors({
            ...error
        })
        if(fieldValues === values)
            return Object.keys(error).every(data => error[data]==="")
    }

    const [values, setValues, errors, setErrors, handleChange, promptValues, setPromptValues] = useForm(initialValues, true, validate, initialPromptValues)
    const [degreeList, setDegreeList] = React.useState({})
    const [processing, setProcessing] = React.useState({
        published: false,
        draft: false
    })
    const [processedMsg, setProcessedMsg] = React.useState('')
    const [disable, setDisable] = React.useState(false)
    const [openSnackbar, setOpenSnackbar] = React.useState(false)

    /* ------------ handle snackbar -------------*/
    const handleClose = () => {
        setOpenSnackbar(false)
    }

    const dispatch = useDispatch()

    const user = useSelector(state => state.user)

    /* ------------- functions to fetch data -----------------*/
    let authToken
    if (localStorage.getItem("key")) {
        authToken = localStorage.getItem("key")
    }

    const requestOptions = {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            Authorization: "token " + authToken,
        },
    }
    
    const getDegrees = async ()  => {
        const response = await fetch(`${baseUrl}/user/degree/dropdown`, requestOptions);
        const data = await response.json();
        await setDegreeList(data.data);
    }

    React.useEffect(() => {
        getDegrees()
    }, [])

    /*----------------- Effects to get company id of employer ---------------*/
    React.useLayoutEffect(() => {
        if(Object.keys(user.data).length){
            setValues({
                ...values,
                company_id: user.data.employer ? user.data.employer.company.id : user.data.candidate.company.id
            })
        } else {
            dispatch(getUserDetails())
        }
    }, [])

    React.useEffect(() => {
        if(Object.keys(user.data).length){
            setValues({
                ...values,
                company_id: user.data.employer ? user.data.employer.company.id : user.data.candidate.company.id
            })
        }
    }, [user.data])

    /*------------------ handle form submit ------------------*/
    const handleSubmit = (status) => async (e) => {
        e.preventDefault();
        if(validate()){
            const body = values;
            body.status = status
            body.short_code = body.title.toLowerCase().split(' ').join('_')
            const requestOptions = {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Access-Control-Allow-Origin': '*',
                    'Authorization': `token ${authToken}`,
                },
                body: JSON.stringify(body)
            };
            try{
                const response = await fetch(`${baseUrl}/job/scholarship/add/`, requestOptions);
                const data = await response.json();
                if(data.status === "error"){
                    setProcessedMsg("Some feilds are empty or not correctly filled")
                    setProcessing({
                        ...processing,
                        [status.toLowerCase()]:false
                    })
                    setDisable(false)
                    setOpenSnackbar(true)
                } else {
                    setProcessedMsg(status.toLowerCase()==="draft" ? "Added to drafts":"Scholarship added successfully")
                    setProcessing({
                        ...processing,
                        [status.toLowerCase()]:false
                    })
                    setDisable(true)
                    setOpenSnackbar(true)
                }
            } catch(error) {
                setProcessedMsg("Internal server error")
                setProcessing({
                    ...processing,
                    [status.toLowerCase()]:false
                })
                setDisable(true)
                setOpenSnackbar(true)
            }
            
        }
    }

    /* ---------- detect changes after submit to enable button -----------*/
    React.useEffect(() => {
        setDisable(false)
    }, [values])
    

    const basic_overview = [
        {
            name: "title",
            label: "Scholarship Title",
            multiline: false
        },
        {
            name: "description",
            label: "Scholarship Description",
            multiline: true
        },
        {
            name: "selection_process",
            label: "Selection Process",
            multiline: true
        },
    ]
    return (
        <Grid className={classes.root}>
            <Typography variant="h4" style={{fontWeight: "600", marginTop: "20px"}}>Add Scholarship</Typography>
            <Controls.Form>
                <Typography variant="body2" className={classes.subHeading} style={{marginTop: "30px"}}>BASIC OVERVIEW</Typography>
                {basic_overview.map((info) => {
                    return (
                        <Controls.FormInput 
                            value={values[info.name]} 
                            name={info.name} 
                            handleChange={handleChange}
                            label={info.label}
                            multiline={info.multiline}
                            key={info.name}
                            error={errors[info.name]}
                            promptValue={promptValues[info.name]}
                        />
                    )
                })}

                <Typography variant="body2" className={classes.subHeading}>REQUIREMENTS</Typography>
                <Controls.AddChip 
                    data={degreeList}
                    name="degrees"
                    label="Accepted Degrees"
                    values={values}
                    setValues={setValues}
                />                

                <Typography variant="body2" className={classes.subHeading}>MORE INFORMATION</Typography>
                <Controls.AddChip 
                    data={tagList}
                    name="tags"
                    label="Diversity Tags"
                    values={values}
                    setValues={setValues}
                />
                <Controls.FormInput 
                    value={values.vacancies} 
                    name="vacancies" 
                    handleChange={handleChange}
                    label="Vacancy(s)"
                    multiline={false}
                    small={true}
                    integer={true}
                    error={errors.vacancies}
                />
                <Controls.DatePicker
                    values={values}
                    setValues={setValues}
                    label="Last Date"
                    name="last_date"
                    onlyFuture={true}
                />
                <Controls.FormInput 
                    value={values.apply_url} 
                    name="apply_url" 
                    handleChange={handleChange}
                    label="Apply URL"
                    multiline={false}
                    small={true}
                    error={errors.apply_url}
                />
                <Grid container justify="center">
                <StatusButton
                        handleSubmit = {handleSubmit}
                        disable={disable}
                        status="Draft"     
                        msg="Save as Draft"
                        processing={processing.draft}
                    />
                    <StatusButton
                        handleSubmit = {handleSubmit}
                        disable={disable}
                        status="Published"     
                        msg="Post Scholarship"
                        processing={processing.published}
                    />
                    <SubmitStatusSnackbar
                        open={openSnackbar}
                        handleClose={handleClose}
                        processedMsg={processedMsg}
                    />
                </Grid>
            </Controls.Form>
        </Grid>
    )
}

export default AddJob
