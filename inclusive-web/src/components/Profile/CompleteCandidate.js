import React from 'react';
import {
    makeStyles,
    Grid,
    FormControl,
    InputLabel,
    Input,
    Button,
    Typography,
    Select,
    MenuItem,
    TextareaAutosize,
    TextField,
    Snackbar
} from '@material-ui/core';
import { useHistory } from 'react-router-dom';
import Alert from '@material-ui/lab/Alert';

import { baseUrl } from '../../urlConstants';

import Autocomplete from '@material-ui/lab/Autocomplete';

import "react-datepicker/dist/react-datepicker.css";
import DatePicker from "react-datepicker";

import 'react-phone-number-input/style.css'
import PhoneInput from 'react-phone-number-input'

const useStyles = makeStyles(theme => ({
    container: {
        '& .MuiButton-root:hover':{
            background: "#ff3750",
        }
    },
    formName: {
        color: "red",
        fontSize: "25px"
    },
    formContainer: {
        display: "flex",
        flexDirection: "column",
        justifyContent: "center",
        alignItems: "center",
        width: "90vw",
        maxWidth: "30rem"
    },
    formInputs: {
        padding: "10px",
        width: "100%"
    },
    fieldInput: {
        width: "100%"
    },
    forgotButton: {
        width: "15rem",
        padding: "2%"
    },
    passwordInput: {
        width: "100%"
    },
    formButton: {
        width: "15rem",
        maxWidth: "90vw",
        margin: "15% auto",
        background: "#ff3750",
        color: "#fff"
    },
    PhoneForm: {
        marginTop: "50px",
        padding: "10px",
        width: "100%"
    },
    PhoneFomLabel: {
        marginTop: "-60px",
    }
}));

const CompleteCandidate = () => {
    const classes = useStyles();

    const [NationalityValues, setNationalityValues] = React.useState('');
    const [MobileValues, setMobileValues] = React.useState('');
    const [AltMobileValues, setAltMobileValues] = React.useState('');
    const [JobRoleValues, setJobRoleValues] = React.useState('');
    const [ProfileDescriptionValues, setProfileDescriptionValues] = React.useState('');
    const [ResumeLink, setResumeLink] = React.useState('');
    const [Year, setYear] = React.useState('');
    const [Month, setMonth] = React.useState('');
    const [LinkedinLink, setLinkedinLink] = React.useState('');
    const [GithubLink, setGithubLink] = React.useState('');
    const [TwitterLink, setTwitterLink] = React.useState('');
    const [RegisteredVia, setRegisteredVia] = React.useState('');
    const [startDate, setStartDate] = React.useState(new Date());
    const [cityData, setCityData] = React.useState('');
    const [currentCity, setCurrentCity] = React.useState('');
    const [preferredCity, setPreferredCity] = React.useState('');
    const [diversityTag, setDiversityTag] = React.useState('');
    const history = useHistory();
    const [isError, setIsError] = React.useState(false);


    const diversityTagData = [
        {
            "id": 0,
            "name": "Women",
        },{
            "id": 0,
            "name": "LGTBQIA",
        },{
            "id": 0,
            "name": "Veteran",
        },{
            "id": 0,
            "name": "Working Mother",
        },{
            "id": 0,
            "name": "Specially Abled",
        },
    ]

    const getCities = async () => {
        let authToken = '24aa5cfa5084e7716221e1737d2838ceb2f91aaf';
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

        const response = await fetch(`${baseUrl}/user/location/dropdown?country=India`, requestOptions);
        const data = await response.json();
        setCityData(data["data"]["cities"]);
    }

    if(cityData === ''){
        const cityDataTest = getCities();
    }

    const handleChange = (event) => {
        if (event.target.name === "nationality") {
            setNationalityValues(event.target.value);
        } else if (event.target.name === "jobRole") {
            setJobRoleValues(event.target.value);
        } else if (event.target.name === "description") {
            setProfileDescriptionValues(event.target.value);
        } else if (event.target.name === "resumeLink") {
            setResumeLink(event.target.value);
        } else if (event.target.name === "registeredVia") {
            setRegisteredVia(event.target.value);
        } else if (event.target.name === "twitterLink") {
            setTwitterLink(event.target.value);
        } else if (event.target.name === "linkedinLink") {
            setLinkedinLink(event.target.value);
        } else if (event.target.name === "githubLink") {
            setGithubLink(event.target.value);
        }
    };

    const handleChangeDate = (date) => {
        setYear(date.getFullYear());
        setMonth(date.getMonth() + 1);
        setStartDate(date);
    };

    const handleChangePhone = (data) => {
        setMobileValues(data)
    };

    const handleChangeAltPhone = (data) => {
        setAltMobileValues(data)
    };

    const handleChangeCity = (datevent, values) => {
        setCurrentCity(values);
    };

    const handleChangePreferredCity = (datevent, values) => {
        setPreferredCity(values);
    };

    const handleChangeDiversityTag = (datevent, values) => {
        setDiversityTag(values);
    };

    const submitFunction = async (e) => {
        e.preventDefault();

        const body = {
            nationality: NationalityValues,
            mobile: MobileValues,
            alternate_mobile: AltMobileValues,
            job_role: JobRoleValues,
            profile_description: ProfileDescriptionValues,
            resume_link: ResumeLink,
            year: Year,
            month: Month,
            registered_via: RegisteredVia,
            linkedin: LinkedinLink,
            github: GithubLink,
            twitter: TwitterLink,
            country: currentCity.state.country,
            state: currentCity.state,
            city: currentCity,
            preferred_city: preferredCity,
            diversity_tags: diversityTag,
        }

        console.log(body);
        let authToken = '24aa5cfa5084e7716221e1737d2838ceb2f91aaf';
        if (localStorage.getItem('key')) {
            authToken = localStorage.getItem('key');
        }

        const requestOptions = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'token ' + authToken,
            },
            body: JSON.stringify(body)
        };

        const response = await fetch(`${baseUrl}/user/complete_profile/`, requestOptions);
        const data = await response.json();
        //console.log(data);
        if (data.status === "OK") {
            history.push('/home');
        } else {
            setIsError(true);
        }
    }

    return (
        <Grid container direction="column" justify="center" spacing={5} alignItems="center" className={classes.container}>
            <Grid item>
                <Typography className={classes.formName}>
                    Complete Profile - Candidate
                </Typography>
            </Grid>
            <Grid item>
                <form className={classes.formContainer} onSubmit={submitFunction}>

                    <div className={classes.formInputs}>
                        <FormControl className={classes.fieldInput}>
                            <InputLabel htmlFor="nationality">
                                Nationality
                            </InputLabel>
                            <Input
                                placeholder="Indian"
                                id="nationality"
                                name="nationality"
                                onChange={handleChange}
                            />
                        </FormControl>
                    </div>

                    <div className={classes.PhoneForm}>
                        <FormControl className={classes.fieldInput}>
                            <InputLabel className={classes.PhoneFomLabel} htmlFor="phoneNumber">
                                Phone Number
                            </InputLabel>
                            <PhoneInput
                                name="phoneNumber"
                                id="phoneNumber"
                                placeholder="9999999999"
                                onChange={handleChangePhone} />
                        </FormControl>
                    </div>

                    <div className={classes.PhoneForm}>
                        <FormControl className={classes.fieldInput}>
                            <InputLabel className={classes.PhoneFomLabel} htmlFor="altPhoneNumber">
                                Alternate Phone Number
                            </InputLabel>
                            <PhoneInput
                                name="altPhoneNumber"
                                id="altPhoneNumber"
                                placeholder="9999999999"
                                onChange={handleChangeAltPhone} />
                        </FormControl>
                    </div>

                    <div className={classes.formInputs}>
                        <FormControl className={classes.fieldInput}>
                            <InputLabel htmlFor="jobRole">
                                Job Role
                            </InputLabel>
                            <Input
                                placeholder="Software Engineer"
                                id="jobRole"
                                name="jobRole"
                                onChange={handleChange}
                            />
                        </FormControl>
                    </div>

                    <div className={classes.formInputs}>
                        <FormControl className={classes.fieldInput}>
                            <TextareaAutosize
                                aria-label="empty textarea"
                                rowsMin={5}
                                placeholder="Description"
                                id="description"
                                name="description" onChange={handleChange} />
                        </FormControl>
                    </div>

                    <div className={classes.formInputs}>
                        <FormControl className={classes.fieldInput}>
                            <InputLabel htmlFor="resumeLink">
                                Resume Link
                            </InputLabel>
                            <Input
                                placeholder="www.link.com/abc"
                                id="resumeLink"
                                name="resumeLink"
                                onChange={handleChange}
                            />
                        </FormControl>
                    </div>

                    <div className={classes.formInputs}>
                        <FormControl className={classes.fieldInput}>
                            <InputLabel>Registered Via</InputLabel>
                            <Select
                                id="registeredVia"
                                name="registeredVia"
                                onChange={handleChange}
                            >
                                <MenuItem key="Email" value="Email">
                                    Email
                                </MenuItem>
                                <MenuItem key="Social" value="Social">
                                    Social
                                </MenuItem>
                                <MenuItem key="Careers" value="Careers">
                                    Careers
                                </MenuItem>
                                <MenuItem key="Friend" value="Friend">
                                    Friend
                                </MenuItem>
                            </Select>
                        </FormControl>
                    </div>

                    <div className={classes.formInputs}>
                        <FormControl className={classes.PhoneForm}>
                            <InputLabel className={classes.PhoneFomLabel} htmlFor="dateAvailable">
                                Available Date for the Posititon
                            </InputLabel>
                            <DatePicker
                                name="dateAvailable"
                                id="dateAvailable"
                                selected={startDate}
                                onChange={handleChangeDate} />
                        </FormControl>
                    </div>

                    <div className={classes.formInputs}>
                        <FormControl className={classes.fieldInput}>
                            <InputLabel htmlFor="twitterLink">
                                Twitter Link
                            </InputLabel>
                            <Input
                                placeholder="https://twitter.com/ChittoraGarvit"
                                id="twitterLink"
                                name="twitterLink"
                                onChange={handleChange}
                            />
                        </FormControl>
                    </div>

                    <div className={classes.formInputs}>
                        <FormControl className={classes.fieldInput}>
                            <InputLabel htmlFor="linkedinLink">
                                Linkedin Link
                            </InputLabel>
                            <Input
                                placeholder="https://www.linkedin.com/in/garvit-chittora/"
                                id="linkedinLink"
                                name="linkedinLink"
                                onChange={handleChange}
                            />
                        </FormControl>
                    </div>

                    <div className={classes.formInputs}>
                        <FormControl className={classes.fieldInput}>
                            <InputLabel htmlFor="githubLink">
                                Github Link
                            </InputLabel>
                            <Input
                                placeholder="https://github.com/garvitchittora"
                                id="githubLink"
                                name="githubLink"
                                onChange={handleChange}
                            />
                        </FormControl>
                    </div>

                    <div className={classes.formInputs}>
                        <FormControl className={classes.fieldInput}>
                            <Autocomplete
                                id="current-city"
                                options={cityData}
                                getOptionLabel={(option) => option.name + ", " + option.state.name + ", " + option.state.country.name}
                                onChange={handleChangeCity}
                                renderInput={(params) => <TextField {...params} label="Current City" variant="outlined" />}
                            />
                        </FormControl>
                    </div>

                    <div className={classes.formInputs}>
                        <FormControl className={classes.fieldInput}>
                            <Autocomplete
                                multiple
                                id="tags-outlined"
                                options={cityData}
                                getOptionLabel={(option) => option.name + ", " + option.state.name + ", " + option.state.country.name}
                                filterSelectedOptions
                                onChange={handleChangePreferredCity}
                                renderInput={(params) => (
                                    <TextField
                                        {...params}
                                        variant="outlined"
                                        label="Preferred Cities"
                                    />
                                )}
                            />
                        </FormControl>
                    </div>

                    <div className={classes.formInputs}>
                        <FormControl className={classes.fieldInput}>
                            <Autocomplete
                                multiple
                                id="tags-outlined"
                                options={diversityTagData}
                                getOptionLabel={(option) => option.name }
                                filterSelectedOptions
                                onChange={handleChangeDiversityTag}
                                renderInput={(params) => (
                                    <TextField
                                        {...params}
                                        variant="outlined"
                                        label="Diversity Tag"
                                    />
                                )}
                            />
                        </FormControl>
                    </div>

                    <div >
                        <Button className={classes.formButton} variant="contained" color="secondary" type="submit">
                            Submit
                        </Button>
                    </div>
                    {
                        isError ? (
                            <Snackbar open={isError} autoHideDuration={6000} onClose={() => setIsError(false)}>
                                <Alert onClose={() => setIsError(false)} severity="error">
                                    Please Try Again
                                    </Alert>
                            </Snackbar>
                        ) : (
                            <></>
                        )
                    }
                </form>
            </Grid>
        </Grid>
    )
}

export default CompleteCandidate;