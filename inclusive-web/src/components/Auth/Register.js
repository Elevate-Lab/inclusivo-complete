import React from "react";
import { Grid, Typography, TextField, InputAdornment, IconButton, Button, Snackbar } from "@material-ui/core";
import { Email, Visibility, VisibilityOff } from '@material-ui/icons'
import { useSelector, useDispatch } from "react-redux";
import { useHistory } from "react-router-dom";
import { useStyles } from "./Styles";
import axios from 'axios';
import { registerUserRequest, registerUserSuccess, registerUserFailure } from '../../actions/authActions/registerActions';
import Loader from '../../assets/loader/loader';
import { baseUrl } from '../../urlConstants';
import Alert from '@material-ui/lab/Alert';

const Register = (props) => {
  const classes = useStyles();
  const dispatch = useDispatch();
  const userRegisterDetails = useSelector((state) => state.userRegister);
  const [error, setError] = React.useState('')
  const [isError, setIsError] = React.useState(false);
  const [successMsg, setSuccessMsg] = React.useState('');
  const [isSuccess, setIsSuccess] = React.useState(false);
  const history = useHistory();
  const [email, setEmail] = React.useState('')
  const [conpasswordValues, setConPasswordValues] = React.useState({
    password: '',
    showPassword: false,
  });
  const [passwordValues, setPasswordValues] = React.useState({
    password: '',
    showPassword: false,
  });
  const [formErrors, setFormErrors] = React.useState({});
  const handleCloseError = () => {
    setIsError(false);
  }
  const handleCloseSuccessMessage = () => {
    setIsSuccess(false);
    history.push({
      pathname: '/auth',
      state: {
        mode: "Resgistration",
        from: history.location,
        is_user: true,
      }
    })
  }
  const validateForm = () => {
    let temp = {}
    temp.password1 = passwordValues.password ? "" : "Password is required."
    temp.password2 = conpasswordValues.password === passwordValues.password ? "" : "Passwords are not same."
    temp.email = (/^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/).test(email) ? "" : "Email is not valid."
    setFormErrors({
      ...temp
    })
    return Object.values(temp).every(value => value === "")
  }
  const handleClickShowPassword = () => {
    setPasswordValues({ ...passwordValues, showPassword: !passwordValues.showPassword });
  };
  const handlePasswordChange = (prop) => (event) => {
    setPasswordValues({ ...passwordValues, [prop]: event.target.value });
  };
  const handleClickShowConPassword = () => {
    setConPasswordValues({ ...conpasswordValues, showPassword: !conpasswordValues.showPassword });
  };
  const handleConPasswordChange = (prop) => (event) => {
    setConPasswordValues({ ...conpasswordValues, [prop]: event.target.value });
  };
  const handleEmailChange = (e) => {
    setEmail(e.target.value);
  }
  const handleSubmit = (e) => {
    e.preventDefault();
    if (validateForm()) {
      const userData = {
        email: email,
        password1: passwordValues.password,
        password2: conpasswordValues.password,
      }
      dispatch(registerUserRequest());
      axios({
        method: "post",
        url: `${baseUrl}/rest-auth/registration/`,
        headers: {
          'Content-Type': 'application/json',
          'Access-Control-Allow-Origin': '*'
        },
        data: JSON.stringify(userData)
      })
        .then(response => {
          //console.log(response);
          if (response.data.detail) {
            setIsSuccess(true);
            setSuccessMsg(response.data.detail);
          }
          dispatch(registerUserSuccess());
        })
        .catch(err => {
          //console.log(err.response.data.email);
          const errorMsg = err.response.data.email;
          dispatch(registerUserFailure(errorMsg));
          setIsError(true);
          setError(errorMsg[0]);
        })
    }
  }
  React.useLayoutEffect(() => {
    //console.log(props);
    if (props.userEmail) {
      setEmail(props.userEmail);
    }
  }, [props]);

  return (
    <Grid className={classes.registerPage} container>
      <Grid item xs={12} md={6} className={classes.registerImageContainer}>
        <img
          className={classes.registerImage}
          src="/images/register.svg"
          alt="register"
        />
      </Grid>

      <Grid item xs={12} md={6}>
        <img className={classes.logo} src="/images/inclusivo.svg" alt="logo" />
        {
          userRegisterDetails.loading ? <div style={{ display: "flex", alignItems: "center", justifyContent: "center", marginTop: "150px" }}>
            <Loader loading={userRegisterDetails.loading} />
          </div> : <Grid
            className={classes.container}
            container
            direction="column"
            justify="center"
          >
            <Grid item xs={12}>
              <Typography className={classes.formName}>Register</Typography>
            </Grid>
            <Grid item xs={12} className={classes.formContainer}>
              <form>
                <TextField
                  onChange={handleEmailChange}
                  value={email}
                  className={classes.formInputs}
                  type="email"
                  id="emailAddress"
                  InputProps={{
                    endAdornment: <InputAdornment position="end"><Email /></InputAdornment>
                  }}
                  label="Email Address"
                  {...(formErrors.email && {
                    error: true,
                    helperText: formErrors.email
                  })}
                />
                <TextField
                  className={classes.formInputs}
                  value={passwordValues.password}
                  label="Password"
                  id="standard-adornment-password"
                  type={passwordValues.showPassword ? 'text' : 'password'}
                  onChange={handlePasswordChange('password')}
                  InputProps={{
                    endAdornment: <InputAdornment position="end">
                      <IconButton
                        onClick={handleClickShowPassword}
                      >
                        {passwordValues.showPassword ? <Visibility /> : <VisibilityOff />}
                      </IconButton>
                    </InputAdornment>
                  }}
                  {
                  ...(formErrors.password1 && {
                    error: true,
                    helperText: formErrors.password1
                  })
                  } />
                <TextField
                  className={classes.formInputs}
                  label="Confirm Password"
                  value={conpasswordValues.password}
                  type={conpasswordValues.showPassword ? 'text' : 'password'}
                  onChange={handleConPasswordChange('password')}
                  InputProps={{
                    endAdornment: <InputAdornment position="end">
                      <IconButton
                        onClick={handleClickShowConPassword}
                      >
                        {conpasswordValues.showPassword ? <Visibility /> : <VisibilityOff />}
                      </IconButton>
                    </InputAdornment>
                  }}
                  {...(formErrors.password2 && {
                    error: true,
                    helperText: formErrors.password2
                  })}
                />
                <div>
                  <Button className={classes.formButton} variant="contained" color="secondary" onClick={handleSubmit}>
                    Register
                </Button>
                </div>
                <Snackbar open={isError} autoHideDuration={6000} onClose={handleCloseError}>
                  <Alert variant="filled" onClose={handleCloseError} severity="error">
                    {error}
                  </Alert>
                </Snackbar>
                <Snackbar open={isSuccess} onClose={handleCloseSuccessMessage}>
                  <Alert variant="filled" onClose={handleCloseSuccessMessage} severity="success">
                    {successMsg}
                  </Alert>
                </Snackbar>
              </form>
            </Grid>
          </Grid>
        }
      </Grid>
    </Grid>
  );
};

export default Register;
