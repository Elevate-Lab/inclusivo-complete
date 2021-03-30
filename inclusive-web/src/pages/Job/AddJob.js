import React from 'react'
import Controls from '../../components/Form/Controls/Controls'
import useForm from '../../customHooks/useForm'
import {useDispatch, useSelector} from 'react-redux'
import {getUserDetails} from '../../actions/userDetails/user.actions';
import {Grid, makeStyles, Button, Typography, CircularProgress} from '@material-ui/core'
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
        flex: "1 1",
        position: "relative"
    },
    buttonProgress: {
        color: theme.palette.primary,
        position: 'absolute',
        top: '50%',
        left: '50%',
        marginTop: -8,
        marginLeft: -8,
    }
}))

const initialValues = {
    company_id: null,
    job_role: "",
    job_type: "",
    title: "",
    description: "",
    short_code: "",
    locations: [],
    degrees: [],
    tags: [],
    is_apply_here: true,
    apply_url: "",
    last_date: format(Date.now(),'yyyy-MM-dd'),
    min_exp: null,
    max_exp: null,
    selection_process: "",
    min_sal: null,
    max_sal: null,
    display_salary: true,
    status: "Draft",
    vacancies: null
}

function AddJob() {

    const classes = useStyles()

    /* ---------------- validate function ------------------- */
    const validate = (fieldValues = values) => {
        const error = {...errors}
        const list = ['title','job_role','job_type','description','selection_process']
        list.forEach((name) => {
            if(name in fieldValues)
                error[name] = fieldValues[name] ? "" : `*Field should not be empty`
        })
        if ("vacancies" in fieldValues)
            error.vacancies = fieldValues.vacancies ? parseInt(fieldValues.vacancies)>0 ? "" : "*Should be greater than 0" : "*Required"
        if ("apply_url" in fieldValues){
            if(!fieldValues.is_apply_here)
                error.apply_url = fieldValues.apply_url ? "" : "*Apply URL should not be empty"
        }
        setErrors({
            ...error
        })
        if(fieldValues === values)
            return Object.keys(error).every(data => {
                return error[data]===""
            })
    }

    const [values, setValues, errors, setErrors, handleChange] = useForm(initialValues, true, validate)

    const [cityList, setCityList] = React.useState({})
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

    const getCities = async () => {
        const response = await fetch(`${baseUrl}/user/location/dropdown/india`, requestOptions)
        const data = await response.json()
        setCityList(data.data)
    }
    
    const getDegrees = async ()  => {
        const response = await fetch(`${baseUrl}/user/degree/dropdown`, requestOptions);
        const data = await response.json();
        await setDegreeList(data.data);
    }

    React.useEffect(() => {
        getCities()
        getDegrees()
    }, [])

    /*----------------- Effects to get company id of employer ---------------*/
    React.useLayoutEffect(() => {
        if(user.pageRequest){
            setValues({
                ...values,
                company_id: user.data.employer ? user.data.employer.company.id : user.data.candidate.company.id
            })
        } else {
            dispatch(getUserDetails())
        }
    }, [])

    React.useEffect(() => {
        if(user.pageRequest){
            setValues({
                ...values,
                company_id: user.data.employer ? user.data.employer.company.id : user.data.candidate.company.id
            })
        }
    }, [user.pageRequest])

    /*------------------ handle form submit ------------------*/
    const handleSubmit = (status) => async (e) => {
        e.preventDefault();
        console.log("Hey I m in submit func")
        if(validate()){
            setProcessing({
                ...processing,
                [status.toLowerCase()]:true
            })
            setDisable(true)
            const body = values;
            body.status = status
            body.short_code = body.title.toLowerCase().split(' ').join('_')
            try{
                const requestOptions = {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Access-Control-Allow-Origin': '*',
                        'Authorization': `token ${authToken}`,
                    },
                    body: JSON.stringify(body)
                }
                const response = await fetch(`${baseUrl}/job/add/`, requestOptions);
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
                    setProcessedMsg(status.toLowerCase()==="draft" ? "Added to drafts":"Job added successfully")
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
            name: "job_role",
            label: "Job Role",
            multiline: false
        },
        {
            name: "title",
            label: "Job Title",
            multiline: false
        },
        {
            name: "job_type",
            label: "Job Type",
            multiline: false
        },
        {
            name: "description",
            label: "Job Description",
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
            <Typography variant="h4" style={{fontWeight: "600", marginTop: "20px"}}>Add Job</Typography>
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
                <Grid container direction="row">
                    <Controls.FormInput 
                        value={values.min_exp} 
                        name="min_exp" 
                        handleChange={handleChange}
                        label="Minimum Experience"
                        multiline={false}
                        small={true}
                        integer={true}
                    />
                    <Controls.FormInput 
                        value={values.max_exp} 
                        name="max_exp" 
                        handleChange={handleChange}
                        label="Maximum Experience"
                        multiline={false}
                        small={true}
                        integer={true}
                    />
                </Grid>
                

                <Typography variant="body2" className={classes.subHeading}>MORE INFORMATION</Typography>
                <Grid container direction="row">
                    <Controls.FormInput 
                        value={values.min_sal} 
                        name="min_sal" 
                        handleChange={handleChange}
                        label="Minimum Salary"
                        multiline={false}
                        small={true}
                        integer={true}
                    />
                    <Controls.FormInput 
                        value={values.max_sal} 
                        name="max_sal" 
                        handleChange={handleChange}
                        label="Maximum Salary"
                        multiline={false}
                        small={true}
                        integer={true}
                    />
                </Grid>
                <Controls.AddChip 
                    data={tagList}
                    name="tags"
                    label="Diversity Tags"
                    values={values}
                    setValues={setValues}
                />
                <Controls.AddChip 
                    data={cityList}
                    name="locations"
                    label="Add Locations"
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
                <Grid container>
                    <Controls.CheckBox
                        values={values}
                        name="is_apply_here"
                        label="Apply Here"
                        setValues={setValues}
                    />
                </Grid>
                {!values.is_apply_here &&
                    <Controls.FormInput 
                        value={values.apply_link} 
                        name="apply_link" 
                        handleChange={handleChange}
                        label="Apply Link"
                        multiline={false}
                        small={true}
                        integer={true}
                        error={errors.apply_url}
                    />
                }
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
                        msg="Post Job"
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
