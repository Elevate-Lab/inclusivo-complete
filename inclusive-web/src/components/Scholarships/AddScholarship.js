import React, {useState} from 'react';
import {
    makeStyles,
    Grid,
    Button,
    Typography,
    MenuItem,
    TextField,
    Checkbox,
    FormControlLabel
} from '@material-ui/core';
import {MuiPickersUtilsProvider, KeyboardDatePicker} from '@material-ui/pickers';
import 'date-fns';
import DateFnsUtils from '@date-io/date-fns';
import Tags from '../AddJob/Tags.js';
import Degrees from '../AddJob/Degrees.js';
import { baseUrl } from '../../urlConstants';
import {tagList} from './TagList';
import {format} from 'date-fns';

const useStyles = makeStyles(theme => ({
    formName: {
        color: "red",
        fontSize: "25px",
        marginTop: "5px"
    },
    sectionName: {
        marginTop: "10px",
        width: "95%",
        paddingLeft: "5px",
        fontSize: "20px"
    },
    formContainer: {
        display: "flex",
        flexDirection: "column",
        justifyContent: "center",
        alignItems: "center",
        width: "100%",
        maxWidth: "30rem"
    },
    formInputs: {
        padding: "10px",
        width: "95%"
    },
    fieldInput: {
        fontSize: "70px",
        width: "100%"
    },
    buttonContainer: {
        display: "flex"
    },
    formButton: {
        width: "15rem",
        maxWidth: "90vw",
        margin: "5%"
    },
    dateFieldInput: {
        width: "95%",
    marginLeft: "auto",
    marginRight: "auto",
    background: "#FAFAFA",
    '& .MuiOutlinedInput-root' : {
      borderRadius: "5px",
    },
    '& input' : {
      padding: "10px 14px",
      fontSize: "12px",
      opacity: "0.8"
    },
    "& .MuiOutlinedInput-root .MuiOutlinedInput-notchedOutline": {
      borderColor: "#E6E6E6"
    },
    "&:hover .MuiOutlinedInput-root .MuiOutlinedInput-notchedOutline": {
      borderColor: "#E6E6E6"
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
    }
}));

const AddScholarship = () => {

    const [degreeList, setDegreeList] = useState([]);
    const [selectedDate, setSelectedDate] = useState(new Date());
    const [isApplyHere, setIsApplyHere] = useState(true);
    const classes = useStyles();
    const [scholarshipData, setScholarshipData] = useState({
        title: "",
        description: "",
        selectionProcess: "",
        last_date: format(Date.now(),'yyyy-MM-dd'),
        short_code: "",
        company_id: 1,
        degrees: [],
        tags: [],
        status: 'Draft',
        vacancies: null,
        is_apply_here: false,
        apply_url: ""
    })

    const AddInfo = [
        { label: "Title", name: "title", type: "text"},
        { label: "Description", name: "description", type: "textarea"},
        { label: "Selection Process", name: "selection_process", type: "textarea" },
    ]

    const requirements = [
        { label: "Add Degree", name: "degrees", type: "select" },
    ]

    const additional_info = [
        { label: "Vacancies", name: "vacancies", type: "text" },
        { label: "Add Tag", name: "tags", type: "select" }, 
    ]

    let authToken = 'b48eef6b74aa372c1ab5f65f9b85c11099679043';
    if (localStorage.getItem('key')) {
        authToken = localStorage.getItem('key');
    }
    const requestOptions = {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'token '+authToken,
        },
    };

    // Initial Calls
    React.useEffect(() => {
        getDegrees();
        getCompanyId();
    },[])

    const getDegrees = async ()  => {
        const response = await fetch(`${baseUrl}/user/degree/dropdown`, requestOptions);
        const data = await response.json();
        await setDegreeList(data.data);
    }

    const getCompanyId = async () => {
        const response = await fetch(`${baseUrl}/user/get/0`, requestOptions);
        const data = await response.json();
        setScholarshipData(prevState => {
            return !data ? prevState : {...prevState, company_id: data.data.employer.company.id}
        })
    }
    

    // Input handlers
    const handleDegree = (degree) => (event) => {
        setScholarshipData(prevState => {
            return prevState.degrees.includes(degree) ? prevState : {...prevState, degrees: [...prevState.degrees, degree]};
        });
    }
    
    const handleTags = (tag) => (event) => {
        setScholarshipData(prevState => {
            return prevState.tags.includes(tag) ? prevState : {...prevState, tags: [...prevState.tags, tag]};
        });
    }

    const clearElement = (event, tag) => {
        if(event === "degrees"){
            setScholarshipData(prevState => {
                return {...prevState, degrees: prevState.degrees.filter((item, index) => {
                    return item !== tag ;
                })}
            });
        }
        if(event === "tags"){
            setScholarshipData(prevState => {
                return {...prevState, tags: prevState.tags.filter((item, index) => {
                    return item !== tag ;
                })}
            });
        }
    }

    const handleChange = (prop) => (event) => {
        if(prop==="vacancies"){
            setScholarshipData({...scholarshipData, [prop]: parseInt(event.target.value)})
        }
        else if(prop!=="degrees" && prop!=="tags"){
            setScholarshipData({...scholarshipData, [prop]: event.target.value})
        }
    }

    const handleDateChange = (date) => {
        setSelectedDate(date)
        setScholarshipData(prevState => {
            return {...prevState, last_date: format(date,'yyyy-MM-dd')}
        });
    }

    const handleCheckboxChange = (event) => {
        setScholarshipData(prevState => {
            return {...prevState, is_apply_here: event.target.checked}
        })
    }

    React.useEffect(() => {
        console.log(scholarshipData)
    }, [scholarshipData])

    // Submit handler
    const handleSubmit = (status) => async (e) => {
        e.preventDefault();
        const body = scholarshipData;
        body.status = status
        body.short_code = body.title.toLowerCase().split(' ').join('_')
        const key = localStorage.getItem('key');
        const requestOptions = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Access-Control-Allow-Origin': '*',
                'Authorization': `token ${key}`,
            },
            body: JSON.stringify(body)
        };
        const response = await fetch(`${baseUrl}/job/scholarship/add/`, requestOptions);
        const data = await response.json();
    }

    // Display functions
    const displayField = (data,idx) => {
        return(
            <div className={classes.formInputs} key={idx}>
                {
                    data.name === "degrees" && (
                        <Degrees degrees={scholarshipData.degrees} clear={clearElement} name={data.name}/>
                    )
                }
                {
                    data.name === "tags" && (
                        <Tags tags={scholarshipData.tags} clear={clearElement} name={data.name}/>
                    )
                }
                <TextField 
                    className={classes.fieldInput}
                    onChange={handleChange(data.name)}
                    name={data.name}
                    id={data.name}
                    label={data.label}
                    type={data.type}
                    variant='outlined'
                
                    {
                        ...(data.type ==="textarea" && {
                            multiline: true,
                            rows: 3,
                            variant: "outlined"
                        })
                    }
                    {
                        ...(data.type === "select" && {
                            select : true,
                            defaultValue: ""
                        })
                    }
                >
                    {
                        data.name === "tags" && tagList
                            .map((option) => (
                                <MenuItem key={option.id} value={option.name} onClick={handleTags(option)}>
                                    {option.name}
                                </MenuItem>
                            ))
                    }
                    {
                        data.name === "degrees" && degreeList
                            .map((option) => (
                            <MenuItem key={option.id} value={option.name} onClick={handleDegree(option)}>
                                {option.degree_name}, {option.degree_type}
                            </MenuItem>
                        
                        ))
                    }
                </TextField>
            </div>
        )
    }

    return (
        <>
            <Grid container direction="column" justify="center" alignItems="center">
                <Grid item>
                    <Typography className={classes.formName}>
                        Add Job
                    </Typography>
                    
                </Grid>

                <Grid item>

                    

                    <Typography className={classes.sectionName}>
                    <strong> Add Info</strong>
                    </Typography>
                    <form className={classes.formContainer}>
                        {
                            AddInfo.map((formType,idx) => (
                                displayField(formType, idx)
                            ))
                        }
                    </form>
                    

                    <Typography className={classes.sectionName}>
                    <strong> Requirements </strong>
                    </Typography>
                    
                  
                    <form className={classes.formContainer}>
                        {
                            requirements.map((formType,idx) => (
                                displayField(formType, idx)
                            ))
                        }
                    </form>

                    <Typography className={classes.sectionName}>
                    <strong> Add More Info</strong>
                    </Typography>

                    <form className={classes.formContainer}>
                        {
                            additional_info.map((formType,idx) => (
                                displayField(formType, idx)
                            ))
                        }
                    </form>

                    <form className={classes.formContainer}>
                        <MuiPickersUtilsProvider utils={DateFnsUtils}>
                            <KeyboardDatePicker 
                                inputVariant="outlined"
                                margin="normal"
                                id="date-picker-dialog"
                                label="Last Date"
                                format="dd/MM/yyyy"
                                value={selectedDate}
                                onChange={handleDateChange}
                                KeyboardButtonProps={{'aria-label': 'change date'}}
                                className={classes.dateFieldInput}
                            />
                        </MuiPickersUtilsProvider>
                    </form>
                    
                    <form className={classes.formContainer}>
                        <FormControlLabel
                            control={<Checkbox checked={scholarshipData.is_apply_here} onChange={handleCheckboxChange}/>}
                            label="Apply Here"
                        />
                    </form>
                    
                    <form className={classes.formInputs}>
                        {
                            scholarshipData.is_apply_here ? null : 
                            <TextField 
                                className={classes.fieldInput}
                                onChange={handleChange("apply_url")}
                                name="apply_url"
                                id="apply_url"
                                label="Apply Url"
                                type="text"
                                variant='outlined'
                            ></TextField>
                        }
                    </form>

                    <form className={classes.buttonContainer}>
                        <Button className={classes.formButton} variant="contained" color="secondary" type="submit" onClick={handleSubmit('Draft')}>
                            Save as Draft
                        </Button>
                        <Button className={classes.formButton} variant="contained" color="secondary" type="submit" onClick={handleSubmit('Published')}>
                            Post Job
                        </Button>
                    </form>
                </Grid>
            </Grid>
        </>
    )
}

export default AddScholarship;