import React from "react";
import {
  makeStyles,
  Grid,
  FormControl,
  Button,
  Typography,
  MenuItem,
  TextField,
  Snackbar,
  CircularProgress,
} from "@material-ui/core";
import { format } from "date-fns";
import Alert from "@material-ui/lab/Alert";
import { baseUrl } from "../../urlConstants";
import Autocomplete from "@material-ui/lab/Autocomplete";
import "react-datepicker/dist/react-datepicker.css";
import "react-phone-number-input/style.css";
import clsx from 'clsx';
import Controls from "../Form/Controls/Controls";
import useForm from '../../customHooks/useForm';
import MuiPhoneNumber from 'material-ui-phone-number';
import { useHistory } from "react-router";

const useStyles = makeStyles((theme) => ({
  container: {
    "& .MuiButton-root:hover": {
      background: "#ff3750",
    },
  },
  formName: {
    color: "red",
    fontSize: "25px",
    marginTop: "30px"
  },
  formContainer: {
    display: "flex",
    flexDirection: "column",
    justifyContent: "center",
    alignItems: "center",
    width: "90vw",
    maxWidth: "30rem",
  },
  formInputs: {
    padding: "10px",
    width: "100%",
  },
  fieldInput: {
    width: "100%",
  },
  formButton: {
    width: "15rem",
    maxWidth: "90vw",
    margin: "15% auto",
    background: "#ff3750",
    color: "#fff",
    height: "36px"
  },
  PhoneForm: {
    margin: "0 6px",
    padding: "10px",
    width: "100%",
  },
  phoneFormInput : {
    background: "#fafafa",
    borderRadius: "5px",
    fontSize: "10px",
    '& .MuiOutlinedInput-adornedStart': {
      paddingLeft: "5px",
      marginRight: "0px"
    },
    '& .MuiInputAdornment-positionStart': {
      marginRight: "0px"
    },
    '&:hover .MuiButtonBase-root': {
      background: "#fafafa !important",
    },
    "& .MuiInputAdornment-root": {
      background: "white"
    },
    '& .MuiOutlinedInput-input': {
      padding: "10px !important"
    },
    "& .MuiOutlinedInput-root .MuiOutlinedInput-notchedOutline": {
      border: "1px solid #e6e6e6 !important",
    },
    "&:hover .MuiOutlinedInput-root .MuiOutlinedInput-notchedOutline": {
      borderColor: "#76B7F3 !important"
    },
    "&:focus-within .MuiOutlinedInput-root .MuiOutlinedInput-notchedOutline": {
      border: "2px solid #76B7F3 !important"
    },
  },
  root4: {
    width: "100%",
    '& .MuiAutocomplete-inputRoot': {
      background: "#fafafa",
      paddingTop: "1px",
      paddingBottom: "1px"
    },
    '& .MuiAutocomplete-listbox': {
      overflowX: "hidden"
    },
  },
  input: {
    '& .MuiSelect-outlined': {
      color: "#9F9F9F",
    },
    fontSize: "14px",
    '& .MuiOutlinedInput-multiline': {
      background: "#fafafa",
      padding: "10px 10px",
      borderRadius: "5px",
      minHeight: "100px"
    },
    "& .MuiOutlinedInput-root .MuiOutlinedInput-notchedOutline": {
      border: "1px solid #e6e6e6 !important",
    },
    "&:hover .MuiOutlinedInput-root .MuiOutlinedInput-notchedOutline": {
      borderColor: "#76B7F3 !important"
    },
    "&:focus-within .MuiOutlinedInput-root .MuiOutlinedInput-notchedOutline": {
      border: "2px solid #76B7F3 !important"
    },
  },
}));

const CompleteCandidate = () => {
  const classes = useStyles();

  const [MobileValues, setMobileValues] = React.useState("");
  const [AltMobileValues, setAltMobileValues] = React.useState("");
  const [RegisteredVia, setRegisteredVia] = React.useState("");
  const [cityData, setCityData] = React.useState([]);
  const [currentCity, setCurrentCity] = React.useState("");
  const [isError, setIsError] = React.useState(false);
  const [fetchingCities, setFetchingCities] = React.useState(true);
  const initialValues = {
    nationality: "",
    job_role: "",
    profile_description: "",
    resume_link: "",
    last_date: format(Date.now(), "yyyy-MM-dd"),
    linkedin: '',
    github: '',
    twitter: '',
    country: '',
    state: '',
    city: '',
    preferred_city: [],
    diversity_tags: [],
  };
  const history = useHistory();
  const validate = () => {}

  const [values, setValues, errors, setErrors, handleChange] = useForm(initialValues, false, validate)

  const diversityTagData = [
    {
      id: 0,
      name: "Women",
    },
    {
      id: 0,
      name: "LGTBQIA",
    },
    {
      id: 0,
      name: "Veteran",
    },
    {
      id: 0,
      name: "Working Mother",
    },
    {
      id: 0,
      name: "Specially Abled",
    },
  ];

  const getCities = async () => {
    let authToken = "24aa5cfa5084e7716221e1737d2838ceb2f91aaf";
    if (localStorage.getItem("key")) {
      authToken = localStorage.getItem("key");
    }

    const requestOptions = {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: "token " + authToken,
      },
    };

    const response = await fetch(
      `${baseUrl}/user/location/dropdown/india`,
      requestOptions
    );
    const data = await response.json();
    setCityData(data.data);
    setFetchingCities(false);
  };
  React.useEffect(() => {
    getCities();
  }, []);
  const handleChangeRegisteredVia = (e) => {
    setRegisteredVia(e.target.value);
  }

  const handleChangePhone = (data) => {
    setMobileValues(data);
  };

  const handleChangeAltPhone = (data) => {
    setAltMobileValues(data);
  };

  const handleChangeCity = (datevent, values) => {
    setCurrentCity(values);
  };

  const submitFunction = async (e) => {
    e.preventDefault();
    // console.log(values);

    const body = {
      nationality: values.nationality,
      mobile: parseInt(MobileValues.split(" ")[0] + MobileValues.split(" ")[1].split(/[.\-_]/).join("")),
      alternate_mobile: parseInt(AltMobileValues.split(" ")[0] + AltMobileValues.split(" ")[1].split(/[.\-_]/).join("")),
      job_role: values.job_role,
      profile_description: values.profile_description,
      resume_link: values.resume_link,
      year: values.last_date.slice(0,4),
      month: values.last_date.slice(5,7),
      registered_via: RegisteredVia,
      linkedin: values.linkedin,
      github: values.github,
      twitter: values.twitter,
      country: {
        id: currentCity.country_id,
        name: currentCity.country_name
      },
      state: {
        id: currentCity.state_id,
        name: currentCity.state_name
      },
      city: {
        id: currentCity.id,
        name: currentCity.name
      },
      preferred_city: values.preferred_city,
      diversity_tags: values.diversity_tags,
    };

    console.log(body);
    let authToken = "24aa5cfa5084e7716221e1737d2838ceb2f91aaf";
    if (localStorage.getItem("key")) {
      authToken = localStorage.getItem("key");
    }

    const requestOptions = {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: "token " + authToken,
      },
      body: JSON.stringify(body),
    };

    const response = await fetch(
      `${baseUrl}/user/complete_profile/`,
      requestOptions
    );
    const data = await response.json();
    //console.log(data);
    if (data.status === "OK") {
      history.push("/home");
    } else {
      setIsError(true);
    }
  };

  return (
    <Grid
      container
      direction="column"
      justify="center"
      alignItems="center"
      className={classes.container}
    >
      <Grid item>
        <Typography className={classes.formName}>
          Complete Profile - Candidate
        </Typography>
      </Grid>
      <Grid item>
        <form className={classes.formContainer} onSubmit={submitFunction}>
          <div className={classes.formInputs}>
            <Controls.FormInput 
              value={values.nationality}
              name="nationality"
              handleChange={handleChange}
              label="Nationality"
            />
          </div>
          <div className={classes.PhoneForm}>
            <Grid container direction="column">
              <Typography variant="h6" style={{ fontSize: "14px", letterSpacing: "0.4px" }}>
                Phone Number
              </Typography>
              <MuiPhoneNumber value={MobileValues} className={classes.phoneFormInput} defaultCountry={'us'} variant="outlined" onChange={handleChangePhone} />
            </Grid>
          </div>
          <div className={classes.PhoneForm}>
            <Grid container direction="column">
              <Typography variant="h6" style={{ fontSize: "14px", letterSpacing: "0.4px" }}>
                Alternate Phone Number
              </Typography>
              <MuiPhoneNumber value={AltMobileValues} className={classes.phoneFormInput} defaultCountry={'us'} variant="outlined" onChange={handleChangeAltPhone} />
            </Grid>
          </div>
          <div className={classes.formInputs}>
            <Controls.FormInput
              value={values.job_role}
              name="job_role"
              handleChange={handleChange}
              label="Job Role"
            />
          </div>
          <div className={classes.formInputs}>
            <Controls.FormInput
              value={values.profile_description}
              name="profile_description"
              handleChange={handleChange}
              label="Profile Description"
              multiline={true}
            />
          </div>
          <div className={classes.formInputs}>
            <Controls.FormInput
              value={values.resume_link}
              name="resume_link"
              handleChange={handleChange}
              label="Resume Link"
            />
          </div>
          <div className={classes.formInputs}>
            <Grid container direction="column">
              <Typography variant="h6" style={{ fontSize: "14px",margin: "10px 0px", letterSpacing: "0.4px" }}>
                Registered Via
              </Typography>
            <TextField 
              value={RegisteredVia}
              variant="outlined"
              style={{width: "100%"}}
              className={classes.phoneFormInput}
              id="registeredVia"
              name="registeredVia"
              onChange={handleChangeRegisteredVia}
              select
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
            </TextField>
            </Grid>
          </div>
          <div className={classes.formInputs}>
            <Controls.DatePicker
              values={values}
              setValues={setValues}
              label="Date of Birth"
              name="last_name"
              onlyFuture={false}
            />
          </div>
          <div className={classes.formInputs}>
            <Controls.FormInput
              value={values.twitter}
              name="twitter"
              handleChange={handleChange}
              label="Twitter Link"
            />
          </div>
          <div className={classes.formInputs}>
            <Controls.FormInput
              value={values.linkedin}
              name="linkedin"
              handleChange={handleChange}
              label="Linkedin Link"
            />
          </div>
          <div className={classes.formInputs}>
            <Controls.FormInput
              value={values.github}
              name="github"
              handleChange={handleChange}
              label="Github Link"
            />
          </div>

          <div className={classes.formInputs}>
            <Grid container direction="column">
              <Typography variant="h6" style={{ fontSize: "14px",margin: "10px 0px", letterSpacing: "0.4px" }}>
                Current City
              </Typography>
            <FormControl className={classes.fieldInput}>
              <Autocomplete
                id="current-city"
                options={cityData}
                loading={fetchingCities}
                getOptionLabel={(option) =>
                  option.name +
                  ", " +
                  option.state_name +
                  ", " +
                  option.country_name
                }
                onChange={handleChangeCity}
                renderInput={(params) => (
                  <TextField
                    {...params}
                    className={clsx(classes.root4, classes.input)}
                    variant="outlined"
                    InputProps={{
                      ...params.InputProps,
                      endAdornment: (
                        <React.Fragment>
                          {fetchingCities ? (
                            <CircularProgress color="inherit" size={20} />
                          ) : null}
                          {params.InputProps.endAdornment}
                        </React.Fragment>
                      ),
                    }}
                  />
                )}
              />
            </FormControl>
            </Grid>
          </div>

          <div className={classes.formInputs}>
            <Controls.AddChip
              data={cityData}
              name="preferred_city"
              label="Preferred Cities"
              values={values}
              setValues={setValues}
            />
          </div>

          <div className={classes.formInputs}>
            <Controls.AddChip
              data={diversityTagData}
              name="diversity_tags"
              label="Diversity Tags"
              values={values}
              setValues={setValues}
            />
          </div>
          <div>
            <Button
              className={classes.formButton}
              variant="contained"
              color="secondary"
              type="submit"
            >
              Submit
            </Button>
          </div>
        </form>
          {isError ? (
            <Snackbar
              open={isError}
              autoHideDuration={6000}
              onClose={() => setIsError(false)}
            >
              <Alert onClose={() => setIsError(false)} severity="error">
                Please Try Again
              </Alert>
            </Snackbar>
          ) : (
            <></>
          )}
      </Grid>
    </Grid>
  );
};

export default CompleteCandidate;
