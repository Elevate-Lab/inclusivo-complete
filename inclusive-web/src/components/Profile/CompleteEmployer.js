import React from 'react';
import {
    makeStyles,
    Grid,
    FormControl,
    InputLabel,
    Button,
    Typography,
    Select,
    MenuItem,
    TextField,
    IconButton,
    Snackbar
} from '@material-ui/core';
import {
    AddCircleOutline
} from '@material-ui/icons'
import Alert from '@material-ui/lab/Alert';
import { baseUrl } from '../../urlConstants';
import "react-datepicker/dist/react-datepicker.css";
import 'react-phone-number-input/style.css'
import PhoneInput from 'react-phone-number-input'
import { Link,useHistory } from 'react-router-dom';

const useStyles = makeStyles(theme => ({
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
        margin: "15% auto"
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

const CompleteEmployer = () => {
    const classes = useStyles();
    const [MobileValues, setMobileValues] = React.useState('');
    const [AltMobileValues, setAltMobileValues] = React.useState('');
    const [companyId,setCompanyId] = React.useState();
    const [RegisteredVia, setRegisteredVia] = React.useState('');
    const [companyList,setCompanyList] = React.useState([{id:'',name:''}]);
    const [fetched,setfetchStatus] = React.useState(false);
    const history = useHistory();
    const [isError, setIsError] = React.useState(false);

    let key = localStorage.getItem('key');

    const getCompanyData = async () => {
        const requestOptions = {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Access-Control-Allow-Origin': '*',
                'Authorization': `token ${key}`,
            }
        };
        try {
            const response = await fetch(`${baseUrl}/company/dropdown/`, requestOptions);
            const data = await response.json();
            //console.log(data);
            setCompanyList(data.data);
            setfetchStatus(true);
        } catch (error) {
            console.log(error);
            setfetchStatus(true);
        }
    }

    React.useState(() => {
        getCompanyData();
    },[]);

    
    const handleChangePhone = (data) => {
        setMobileValues(data)
    };

    const handleChangeAltPhone = (data) => {
        setAltMobileValues(data)
    };
    const handleChange = (event) => {
        if (event.target.name === "registeredVia") {
            setRegisteredVia(event.target.value);
        } else if(event.target.name === "companyId"){
            setCompanyId(event.target.value);
        }
    }; 
    const handleAddCompany = () => {
        history.push({
            pathname : '/addcompany',
            state: {
                from: history.location,
                mode: "ProfileComplete"
            }
        })
    }
    const handleSubmit = async (event) => {
        event.preventDefault();
        const body = {
            company_id : companyId,
            mobile: MobileValues,
            alternate_mobile: AltMobileValues,
            registered_via: RegisteredVia
        }
        const requestOptions = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'token ' + key,
            },
            body: JSON.stringify(body)
        };

        const response = await fetch(`${baseUrl}/user/complete_profile/`, requestOptions);
        const data = await response.json();
        if(data.status === "OK"){
            history.push('/home');
        } else {
            setIsError(true);
        }
        //console.log(data);
    }

    return fetched ? (
        <Grid container direction="column" justify="center" spacing={5} alignItems="center">
            <Grid item>
                <Typography className={classes.formName}>
                    Complete Profile - Employer
                </Typography>
            </Grid>
            <Grid item>
                <form className={classes.formContainer} onSubmit={handleSubmit}>

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
                        <TextField
                            defaultValue=""
                            className={classes.fieldInput}
                            placeholder="Company Name"
                            name="companyId"
                            select
                            label="Company Name"
                            onChange={handleChange}
                        >
                            {companyList.map((option) => (
                                <MenuItem key={option.id} value={option.id}>
                                    {option.name}
                                </MenuItem>
                            ))}
                        </TextField>
                    </div>

                    <Grid container className={classes.formInputs}>
                        <Grid item container justify="center" alignItems="center">
                            <Grid item>
                                Add Your Company
                            </Grid>
                            <Grid item>
                                <IconButton onClick={handleAddCompany}>
                                    <AddCircleOutline color="secondary" />
                                </IconButton>
                            </Grid>
                        </Grid>
                    </Grid>    

                    <div className={classes.formInputs}>
                        <FormControl className={classes.fieldInput}>
                            <InputLabel>Referred Via</InputLabel>
                            <Select
                                defaultValue=""
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
                    <div>
                        <Button className={classes.formButton} variant="contained" color="secondary" type="submit">
                            Continue
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
    ) : (
        <div>
            Loading...
        </div>
    )
}

export default CompleteEmployer;