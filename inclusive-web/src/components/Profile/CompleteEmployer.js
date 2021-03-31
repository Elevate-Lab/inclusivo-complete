import React from "react";
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
  Snackbar,
} from "@material-ui/core";
import { AddCircleOutline } from "@material-ui/icons";
import Alert from "@material-ui/lab/Alert";
import { baseUrl } from "../../urlConstants";
import "react-datepicker/dist/react-datepicker.css";
import "react-phone-number-input/style.css";
import MuiPhoneNumber from 'material-ui-phone-number';
import { useHistory } from "react-router-dom";

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
  forgotButton: {
    width: "15rem",
    padding: "2%",
  },
  passwordInput: {
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
  phoneFormInput: {
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
}));

const CompleteEmployer = () => {
  const classes = useStyles();
  const [MobileValues, setMobileValues] = React.useState("");
  const [AltMobileValues, setAltMobileValues] = React.useState("");
  const [companyId, setCompanyId] = React.useState();
  const [RegisteredVia, setRegisteredVia] = React.useState("");
  const [companyList, setCompanyList] = React.useState([{ id: "", name: "" }]);
  const [fetched, setfetchStatus] = React.useState(false);
  const history = useHistory();
  const [isError, setIsError] = React.useState(false);

  let key = localStorage.getItem("key");

  const getCompanyData = async () => {
    const requestOptions = {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        "Access-Control-Allow-Origin": "*",
        Authorization: `token ${key}`,
      },
    };
    try {
      const response = await fetch(
        `${baseUrl}/company/dropdown/`,
        requestOptions
      );
      const data = await response.json();
      //console.log(data);
      setCompanyList(data.data);
      setfetchStatus(true);
    } catch (error) {
      console.log(error);
      setfetchStatus(true);
    }
  };

  React.useState(() => {
    getCompanyData();
  }, []);

  const handleChangePhone = (data) => {
    setMobileValues(data);
  };

  const handleChangeAltPhone = (data) => {
    setAltMobileValues(data);
  };
  const handleChange = (event) => {
    if (event.target.name === "registeredVia") {
      setRegisteredVia(event.target.value);
    } else if (event.target.name === "companyId") {
      setCompanyId(event.target.value);
    }
  };
  const handleAddCompany = () => {
    history.push({
      pathname: "/addcompany",
      state: {
        from: history.location,
        mode: "ProfileComplete",
      },
    });
  };
  const handleSubmit = async (event) => {
    event.preventDefault();
    const body = {
      company_id: companyId,
      mobile: parseInt(MobileValues.split(" ")[0] + MobileValues.split(" ")[1].split(/[.\-_]/).join("")),
      alternate_mobile: parseInt(AltMobileValues.split(" ")[0] + AltMobileValues.split(" ")[1].split(/[.\-_]/).join("")),
      registered_via: RegisteredVia,
    };
    const requestOptions = {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: "token " + key,
      },
      body: JSON.stringify(body),
    };

    const response = await fetch(
      `${baseUrl}/user/complete_profile/`,
      requestOptions
    );
    const data = await response.json();
    if (data.status === "OK") {
      history.push("/home");
    } else {
      setIsError(true);
    }
    //console.log(data);
  };

  return fetched ? (
    <Grid
      container
      direction="column"
      justify="center"
      alignItems="center"
      className={classes.container}
    >
      <Grid item>
        <Typography className={classes.formName}>
          Complete Profile - Employer
        </Typography>
      </Grid>
      <Grid item>
        <form className={classes.formContainer} onSubmit={handleSubmit}>
          <div className={classes.PhoneForm}>
            <Grid container direction="column">
              <Typography
                variant="h6"
                style={{ fontSize: "14px", margin: "10px 0px", letterSpacing: "0.4px" }}
              >
                Phone Number
              </Typography>
              <MuiPhoneNumber
                value={MobileValues}
                className={classes.phoneFormInput}
                defaultCountry={"us"}
                variant="outlined"
                onChange={handleChangePhone}
              />
            </Grid>
          </div>
          <div className={classes.PhoneForm}>
            <Grid container direction="column">
              <Typography
                variant="h6"
                style={{ fontSize: "14px", margin: "10px 0px",  letterSpacing: "0.4px" }}
              >
                Alternate Phone Number
              </Typography>
              <MuiPhoneNumber
                value={AltMobileValues}
                className={classes.phoneFormInput}
                defaultCountry={"us"}
                variant="outlined"
                onChange={handleChangeAltPhone}
              />
            </Grid>
          </div>

          <div className={classes.formInputs}>
            <Grid container direction="column">
              <Typography
                variant="h6"
                style={{ fontSize: "14px", margin: "10px 0px", letterSpacing: "0.4px" }}
              >
                Select Your Company
              </Typography>
            <TextField
              defaultValue=""
              className={classes.phoneFormInput}
              name="companyId"
              select
              variant="outlined"
              onChange={handleChange}
            >
              {companyList.map((option) => (
                <MenuItem key={option.id} value={option.id}>
                  {option.name}
                </MenuItem>
              ))}
            </TextField>
            </Grid>
          </div>

          <Grid container className={classes.formInputs}>
            <Grid item container justify="center" alignItems="center">
              <Grid item>Add Your Company</Grid>
              <Grid item>
                <IconButton onClick={handleAddCompany}>
                  <AddCircleOutline color="secondary" />
                </IconButton>
              </Grid>
            </Grid>
          </Grid>

          <div className={classes.formInputs}>
            <Grid container direction="column">
              <Typography variant="h6" style={{ fontSize: "14px", margin: "10px 0px", letterSpacing: "0.4px" }}>
                Registered Via
              </Typography>
              <TextField
                value={RegisteredVia}
                variant="outlined"
                style={{ width: "100%" }}
                className={classes.phoneFormInput}
                id="registeredVia"
                name="registeredVia"
                onChange={handleChange}
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
          <div>
            <Button
              className={classes.formButton}
              variant="contained"
              color="secondary"
              type="submit"
            >
              Continue
            </Button>
          </div>
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
        </form>
      </Grid>
    </Grid>
  ) : (
    <div>Loading...</div>
  );
};

export default CompleteEmployer;
