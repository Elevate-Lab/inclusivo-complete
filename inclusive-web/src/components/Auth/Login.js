import React from "react";
import axios from 'axios';
import { Grid,Typography,TextField,InputAdornment,IconButton,Button } from "@material-ui/core";
import { loginUserRequest,loginUserFailure,loginUserSuccess } from '../../actions/authActions/loginActions'
import { Email,Visibility,VisibilityOff } from '@material-ui/icons';
import { useSelector,useDispatch } from "react-redux";
import { useHistory } from "react-router-dom";
import { useStyles } from "./Styles";
import { baseUrl } from '../../urlConstants';
import Loader from '../../assets/loader/loader';
import Snackbar from '@material-ui/core/Snackbar';
import Alert from '@material-ui/lab/Alert';

const Login = (props) => {
  const classes = useStyles();
  const dispatch = useDispatch();
  const userLoginDetails = useSelector((state) => state.userLogin);
  const history = useHistory();
  const [succesMsg,setSuccessMsg] = React.useState(false);
  const [isError,setIsError] = React.useState(false);
  const [email,setEmail] = React.useState('');
  const [passwordValues, setPasswordValues] = React.useState({
    password: '',
    showPassword: false,
  });
  const [formErrors,setFormErrors] = React.useState({});
  const validateForm = () => {
    let temp = {};
    temp.password = passwordValues.password ? "" : "Password is required."
    temp.email = (/^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/).test(email) ? "" : "Email is not valid."
    setFormErrors({
      ...temp
    })
    return Object.values(temp).every(value => value === "")
  }
  const handleCloseMessage = () => {
    if(succesMsg){
      setSuccessMsg(false);
    }
    if(isError){
      setIsError(false);
    }
  }
  const handleClickShowPassword = () => {
    setPasswordValues({ ...passwordValues, showPassword: !passwordValues.showPassword });
  };
  const handlePasswordChange = (prop) => (event) => {
    setPasswordValues({ ...passwordValues, [prop]: event.target.value });
  };
  const handleEmailChange = (e) => {
      setEmail(e.target.value);
  }
  const handleSubmit = (e) => {
    e.preventDefault();
    if(validateForm()) {
      const userData = {
        email: email,
        password: passwordValues.password
      }
      dispatch(loginUserRequest());
      axios({
        method: "post",
        url: `${baseUrl}/rest-auth/login/`,
        headers: {
          'Content-Type': 'application/json',
          'Access-Control-Allow-Origin': '*'
        },
        data: JSON.stringify(userData)
      })
        .then(response => {
          if (response.data.key.length > 0) {
            localStorage.setItem('key', response.data.key);
            localStorage.setItem('userEmail', userData.email);
            dispatch(loginUserSuccess(true));
            history.replace({
              pathname: '/profilestatus',
              state : {
                from: history.location
              }
            })
          }
        })
        .catch(err => {
          //console.log(err.response.data.non_field_errors);
          const errorMsg = err.response.data.non_field_errors;
          setIsError(true);
          dispatch(loginUserFailure(errorMsg[0]));
        })
    }
  }
  React.useLayoutEffect(() => {
    if (props.userEmail) {
      setEmail(props.userEmail);
    }
    if(props.message.length > 0 ){
      setSuccessMsg(true);
    }
  }, [props]);

  return (<>
    <Grid className={classes.loginPage} container spacing={2}>
      <Grid item xs={12} md={5}>
        <img className={classes.logo} src="/images/inclusivo.svg" alt="logo" />
        {
          userLoginDetails.loading ? <div style={{top: "40%",position: "absolute",left: "45%"}}>
            <Loader loading={userLoginDetails.loading} />
          </div> : <Grid className={classes.container} container direction="column" justify="center">
            <Grid item xs={12}>
              <Typography className={classes.formName}>
                Login
            </Typography>
            </Grid>
            <Grid item xs={12} className={classes.formContainer}>
              <form>
                <TextField
                  className={classes.formInputs}
                  value={email}
                  onChange={handleEmailChange}
                  id="emailAddress"
                  InputProps={{
                    endAdornment: <InputAdornment position="end"><Email /></InputAdornment>
                  }}
                  label="Email Address"
                  {...(formErrors.password1 && {
                    error: true,
                    helperText: formErrors.password1
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
                  ...(formErrors.password && {
                    error: true,
                    helperText: formErrors.password
                  })
                  } />
                <Button className={classes.formButton} variant="contained" onClick={handleSubmit}>
                  Login
                </Button>
              </form>
              {/* {
                props.message && <Snackbar open={succesMsg} autoHideDuration={6000} onClose={handleCloseMessage}>
                  <Alert onClose={handleCloseMessage} severity="success">
                    {props.messsge}
                  </Alert>
                </Snackbar>
              } */}
              {
                userLoginDetails.error.length > 0 && <Snackbar open={isError} autoHideDuration={6000} onClose={handleCloseMessage}>
                  <Alert onClose={handleCloseMessage} severity="error">
                    {userLoginDetails.error}
                  </Alert>
                </Snackbar>
              }
            </Grid>
          </Grid>
        }
      </Grid>
      <Grid item xs={12} md={5} className={classes.loginImage}>
        <img src="/images/login.svg" alt="login" />
      </Grid>
    </Grid></>
  );
};

export default Login;
