import React from 'react';
import {
  Grid,
  Typography,
  Box,
  TextField,
  InputAdornment,
  Button
} from '@material-ui/core';
import {
  Email
} from '@material-ui/icons';
import axios from 'axios';
import { baseUrl } from '../../urlConstants'
import { useSelector, useDispatch } from 'react-redux';
import { checkUserRequest, checkUserFailure, checkUserSuccess, setUserLoginEmail, setUserRegisterEmail } from '../../actions/authActions/checkUserActions'
import { useHistory } from 'react-router-dom';
import { useStyles } from './Styles';
import Loader from '../../assets/loader/loader';
import SwiperCore, { Navigation, Pagination, Autoplay } from 'swiper';

import { Swiper, SwiperSlide } from 'swiper/react';

import ww from '../../assets/Display/workingWoman.png';
import vat from '../../assets/Display/Vat.png';
import wfh from '../../assets/Display/workFromHome.png';
import female from '../../assets/Display/female.png';

// Import Swiper styles
import 'swiper/swiper.scss';
import 'swiper/components/navigation/navigation.scss';
import 'swiper/components/pagination/pagination.scss';

// install Swiper modules
SwiperCore.use([Navigation, Pagination, Autoplay]);

const Landing = (props) => {
  const classes = useStyles();
  const history = useHistory();
  const dispatch = useDispatch();
  const checkUserDetails = useSelector(state => state.checkUser);
  const [email, setEmail] = React.useState('');
  const [formErrors, setFormErrors] = React.useState({});

  const validateForm = () => {
    let temp = {};
    temp.email = (/^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/).test(email) ? "" : "Email is not valid."
    setFormErrors({
      ...temp
    })
    return Object.values(temp).every(value => value === "")
  }
  React.useEffect(() => {

  }, [props])
  const handleEmailChange = (e) => {
    setEmail(e.target.value);
  }
  const handleSubmit = (e) => {
    e.preventDefault();
    if (validateForm()) {
      const userData = {
        email: email
      }
      dispatch(checkUserRequest());
      axios({
        method: "post",
        url: `${baseUrl}/user/check/user/`,
        headers: {
          'Content-Type': 'application/json',
          'Access-Control-Allow-Origin': '*'
        },
        data: JSON.stringify(userData)
      })
        .then(response => {
          let data = {};
          if (response.data.status === "OK") {
            if (response.data.data.is_user) {
              dispatch(setUserLoginEmail(userData.email));
              data = {
                is_user: response.data.data.is_user,
                registered: "YES",
              };
            } else {
              dispatch(setUserRegisterEmail(userData.email));
              data = {
                is_user: response.data.data.is_user,
                registered: "NOT",
              };
            }
            dispatch(checkUserSuccess(data));
            history.push({
              pathname: '/auth',
              state: {
                from: history.location,
                is_user: response.data.data.is_user,
                mode: response.data.data.is_user ? "DefaultLogin" : "Registration"
              }
            })
          }

        })
        .catch(err => {
          console.log(err.response);
          const errorMsg = err.response;
          dispatch(checkUserFailure(errorMsg));
        })
    }
  }

  const DisplayCard = (props) => {
    return (
      <>
        <img
          className={classes.landingImage}
          src={props.img}
          alt="landing"
        />

        <Box>
          <Typography variant="h6" style={{ color: "#FF3750" }}>
            {props.desc}
          </Typography>
        </Box>
      </>
    );
  }

  return (
    <Grid className={classes.landingPage} container >
      <Grid item xs={12} md={6} style={{ margin: '5% auto' }}>
        <img
          className={classes.logo}
          src="/images/inclusivo.svg"
          alt="logo"
        />
        {
          checkUserDetails.loading ? <div style={{ display: "flex", alignItems: "center", justifyContent: "center", marginTop: "150px" }}>
            <Loader loading={checkUserDetails.loading} />
          </div> : <Grid className={classes.container} container direction="column" justify="center">
            <Grid item xs={12}>
              <Typography className={classes.formName}>
                Be a Part of Inclusivo
              </Typography>
            </Grid>
            <Grid item xs={12} className={classes.formContainer}>
              <form>
                <TextField
                  className={classes.formInputs}
                  value={email}
                  type="email"
                  onChange={handleEmailChange}
                  label="Email Address"
                  InputProps={{
                    endAdornment: <InputAdornment position="end"><Email /></InputAdornment>
                  }}
                  {...(formErrors.email && {
                    error: true,
                    helperText: formErrors.email
                  })}
                />
                <Button className={classes.formButton} variant="contained" onClick={handleSubmit} ariaLabel="Continue">
                  Continue
                  </Button>
              </form>
            </Grid>
          </Grid>
        }
      </Grid>
      <Grid item xs={12} md={6} className={classes.landingImageContainer}>
        <Swiper
          id='Banner'
          navigation
          pagination={{ clickable: true }}
          autoplay
          loop
          style={{ height: '95%' }}
        >
          <SwiperSlide>
            <DisplayCard
              desc='Grow in your Career with Endless Oppurtunites'
              img={ww} />
          </SwiperSlide>
          <SwiperSlide>
            <DisplayCard
              desc='Get number of oppurtunities and find your Dream Job...'
              img={vat} />
          </SwiperSlide>
          <SwiperSlide>
            <DisplayCard
              desc="Share your Job Stories and listen to other's"
              img={wfh} />
          </SwiperSlide>
          <SwiperSlide>
            <DisplayCard
              desc='Find the Perfect Candidate to fit into your Team'
              img={female} />
          </SwiperSlide>
        </Swiper>
      </Grid>
    </Grid>
  );
}

export default Landing;