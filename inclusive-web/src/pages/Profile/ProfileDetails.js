import {
  Box,
  Grid,
  IconButton,
  makeStyles,
  Typography,
  useMediaQuery,
  useTheme,
} from "@material-ui/core";
import { GitHub, Language, LinkedIn, Twitter } from "@material-ui/icons";
import React, { useEffect } from "react";
import { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Link } from "react-router-dom";
import { getUserDetails } from "../../actions/userDetails/user.actions";
import profilePlaceholder from '../../assets/blank_image.png'

const useStyles = makeStyles((theme) => ({
  mainContainer: (pc) => ({
    background: "#FAFAFA",
    display: "flex",
    borderRadius: "0.3rem",
    padding: pc ? "2rem" : "0",
  }),
  card: {
    padding: "1rem",
    borderRadius: "0.5rem",
    background: "white",
    height:'100%'
  },
  profileContainer: {
    width: "70%",

    //background: 'gray',
    position: "relative",
    marginBottom: "1rem",
    overflow: "hidden",
  },
  h1Text: {
    fontWeight: "500",
  },
  h2Text: {
    fontWeight: "500",
    fontSize: "1.4rem",
  },
  descText: {
    color: "#7A7A7A",
  },
}));


export const ProfileDetails = () => {
  const dispatch = useDispatch();

  const theme = useTheme();
  const pc = useMediaQuery(theme.breakpoints.up("sm"));
 
  const classes = useStyles(pc);

  const user = useSelector((state) => state.user);
  //const [isCandidate, setIsCandidate] = useState(user.data.employer ? true : false);
  const [candidate, setCandidate] = useState(user.data.candidate ? user.data.candidate : null)
  const [employer, setEmployer] = useState(user.data.employer ? user.data.employer : null)

  useEffect(() => {
    dispatch(getUserDetails());
  }, []);
  useEffect(() => {
    //console.log(user);
    if(user.data.candidate){
        //setIsCandidate(true);
        setCandidate(user.data.candidate)
    }
    else {
        //setIsCandidate(false);
        setEmployer(user.data.employer)
    }
    
    //console.log(employer)
  }, [user]);

  const KeyValue = (props) => {
      return(
        <Grid container style={{ marginBottom: "0.4rem" }}>
        <Grid item xs={4}>
          <Typography className={classes.h1Text}>
            {props.data}
          </Typography>
        </Grid>
        <Grid item xs={8}>
          <Typography className={classes.descText}>
            {props.value}
          </Typography>
        </Grid>
      </Grid>
      )
  }
  return (
    <>
      {user.pageRequest ? (
          candidate ?
            (<Box style={{ width: "100%" }}>
            <Box style={{ padding: pc ? "1rem" : "0.1rem" }}>
              <Grid container className={classes.mainContainer}>
                <Grid item sm={3} xs={12}>
                  <Box style={{ padding: "1rem" , height:'100%'}}>
                    <Box className={`container-column ${classes.card}`}>
                      <Box className={classes.profileContainer}>
                        <img
                          style={{ width: "100%" }}
                          src={candidate.user.photo_url ? candidate.user.photo_url : profilePlaceholder}
                        ></img>
                      </Box>
                      <Typography
                        style={{ textTransform: "uppercase", fontWeight: "500" }}
                      >
                        {candidate.user.first_name} {candidate.user.last_name}
                      </Typography>
                      <Typography style={{ color: "#7A7A7A" }}>{candidate.job_role}</Typography>
                      <Typography
                        style={{
                          fontWeight: "500",
                          fontSize: "1.4rem",
                          margin: "1.5rem 0",
                        }}
                      >
                        Diversity Tags
                      </Typography>
                      {candidate.diversity_tags ? candidate.diversity_tags.map((tag,idx) => {
                          return(
                            <Box
                            key={idx}
                            className="container-row"
                            justifyContent="center"
                            style={{
                              background: "#FAFAFA",
                              borderRadius: "0.3rem",
                              padding: "0.2rem",
                              width: "70%",
                              marginBottom: "1rem",
                            }}
                          >
                            <Typography style={{ color: "4A4A4A" }}>{tag.name}</Typography>
                          </Box>
                          )
                      }) : "No Tags" }
                    </Box>
                  </Box>
                </Grid>
                <Grid item sm={9} xs={12}>
                  <Box style={{ padding: "1rem", height:'100%' }}>
                    <Box className={classes.card} style={{ paddingLeft: "3rem" }}>
                      <Typography
                        className={classes.h2Text}
                        style={{ marginBottom: "1rem" }}
                      >
                        About
                      </Typography>
                      <Typography
                        className={classes.descText}
                        style={{ textTransform: "uppercase", marginBottom: "1rem" }}
                      >
                        Basic Information
                      </Typography>
        
                          <KeyValue data="Gender" value= {candidate.user.gender} />
                          <KeyValue data="DOB" value= {candidate.user.dob} />
                          <KeyValue data="City" value= {candidate.city.name} />
                          <KeyValue data="State" value= {candidate.state.name} />
                          <KeyValue data="Country" value= {candidate.country.name} />
                          <KeyValue data="Nationality" value= {candidate.nationality} />
    
                      
                      <Box
                        style={{
                          height: "2px",
                          width: "85%",
                          background: "#FAFAFA",
                          margin: "1rem 0",
                        }}
                      />
                      <Typography
                        className={classes.descText}
                        style={{ textTransform: "uppercase", marginBottom: "1rem" }}
                      >
                        Conatct Details
                      </Typography>
                        <KeyValue data="Email" value= {candidate.user.email} />
                        <KeyValue data="Mobile No." value={candidate.mobile} />
                        <KeyValue data="Alternate Mobile No." value= {candidate.alternate_mobile} />
                    </Box>
                  </Box>
                </Grid>
                <Grid item sm={3} xs={12}>
                  <Box style={{ padding: "1rem", height:'100%' }}>
                    <Box className={`${classes.card}`}>
                      <Typography
                        className={classes.h2Text}
                        style={{ marginBottom: "1rem" }}
                      >
                        Elsewhere
                      </Typography>
                      <Grid container style={{ marginBottom: "1rem" }}>
                        <Grid item xs={4}>
                          
                          <IconButton
                          href={candidate.linkedin}
                            disableRipple
                            style={{ color: "#2867B2", padding: "0" }}
                          >
                              
                            <LinkedIn className={classes.socialIcons} />
                          </IconButton>
                        </Grid>
                        <Grid item xs={8}>
                          <Typography className={classes.descText}>
                            LinkedIn
                          </Typography>
                        </Grid>
                      </Grid>
                      <Grid container style={{ marginBottom: "1rem" }}>
                        <Grid item xs={4}>
                          <IconButton
                          href={candidate.github}
                            disableRipple
                            style={{ color: "black", padding: "0" }}
                          >
                            <GitHub className={classes.socialIcons} />
                          </IconButton>
                        </Grid>
                        <Grid item xs={8}>
                          <Typography className={classes.descText}>
                            Github
                          </Typography>
                        </Grid>
                      </Grid>
                      <Grid container style={{ marginBottom: "1rem" }}>
                        <Grid item xs={4}>
                          <IconButton href={candidate.twitter} style={{ color: "#49a1eb", padding: "0" }}>
                            <Twitter className={classes.socialIcons} />
                          </IconButton>
                        </Grid>
                        <Grid item xs={8}>
                          <Typography className={classes.descText}>
                            Twitter
                          </Typography>
                        </Grid>
                      </Grid>
                    </Box>
                  </Box>
                </Grid>
    
                <Grid item sm={9} xs={12}>
                  <Box style={{ padding: "1rem", height:'100%' }}>
                    <Box className={`${classes.card}`}>
                      <Typography
                        className={classes.h2Text}
                        style={{ marginBottom: "1rem" }}
                      >
                        Profile Description
                      </Typography>
                      <Typography className={classes.descText}>
                        {candidate.profile_description}
                      </Typography>
                      <a href={candidate.resume_link} style={{ fontSize: "1.4rem" }}>
                        RESUME LINK
                      </a>
                    </Box>
                  </Box>
                </Grid>
    
                <Grid item xs={12}>
                  <Box style={{ padding: "1rem" }}>
                    <Box className={`${classes.card}`}>
                      <Typography
                        className={classes.h2Text}
                        style={{ marginBottom: "1rem" }}
                      >
                        Job Conditions
                      </Typography>
                      <Typography
                        className={classes.descText}
                        style={{ textTransform: "uppercase", marginBottom: "1rem" }}
                      >
                        Preferred location
                      </Typography>
                      {candidate.preferred_city ? candidate.preferred_city.map((city, index) => {
                        return (
                          <Grid container style={{ marginBottom: "0.4rem" }}>
                            <Grid item xs={4}>
                              <Typography className={classes.h1Text}>
                                City {index+1}
                              </Typography>
                            </Grid>
                            <Grid item xs={8}>
                              <Typography className={classes.descText}>
                                {city.name}
                              </Typography>
                            </Grid>
                          </Grid>
                        );
                      }): "No city"}
                      <Box
                        style={{
                          height: "2px",
                          width: "85%",
                          background: "#FAFAFA",
                          margin: "1rem 0",
                        }}
                      />
                      <Typography
                        className={classes.descText}
                        style={{ textTransform: "uppercase", marginBottom: "1rem" }}
                      >
                        Relocation 
                      </Typography>
                      <Grid container style={{ marginBottom: "0.4rem" }}>
                            <Grid item xs={4}>
                              <Typography className={classes.h1Text}>
                              Will you relocate to another city?
                              </Typography>
                            </Grid>
                            <Grid item xs={8}>
                              <Typography className={classes.descText}>
                                {candidate.is_realloaction ? "Yes" : "No"}
                              </Typography>
                            </Grid>
                          </Grid>
    
                          <Box
                        style={{
                          height: "2px",
                          width: "85%",
                          background: "#FAFAFA",
                          margin: "1rem 0",
                        }}
                      />
                      <Typography
                        className={classes.descText}
                        style={{ textTransform: "uppercase", marginBottom: "1rem" }}
                      >
                        available for work 
                      </Typography>
                      <Grid container style={{ marginBottom: "0.4rem" }}>
                            <Grid item xs={4}>
                              <Typography className={classes.h1Text}>
                              When can you start?
                              </Typography>
                            </Grid>
                            <Grid item xs={8}>
                              <Typography className={classes.descText}>
                                {candidate.month}, {candidate.year}
                              </Typography>
                            </Grid>
                          </Grid>
                    </Box>
                  </Box>
                </Grid>
              </Grid>
            </Box>
          </Box>):
          employer &&
            (<Box style={{ width: "100%" }}>
            <Box style={{ padding: pc ? "2.5rem" : "0.1rem" }}>
              <Grid container className={classes.mainContainer}>
                <Grid item sm={3} xs={12}>
                  <Box style={{ padding: "1rem", height:'100%' }}>
                    <Box className={`container-column ${classes.card}`}>
                      <Box className={classes.profileContainer}>
                        <img
                          style={{ width: "100%" }}
                          src={employer.user.photo_url ? employer.user.photo_url : profilePlaceholder}
                        ></img>
                      </Box>
                      <Typography
                        style={{ textTransform: "uppercase", fontWeight: "500" }}
                      >
                        {employer.user.first_name} {employer.user.last_name}
                      </Typography>
                      <Typography style={{ color: "#7A7A7A" }}>Employer</Typography>
                      <Typography
                        style={{
                          fontWeight: "500",
                          fontSize: "1.4rem",
                          margin: "1.5rem 0",
                        }}
                      >
                        Company
                      </Typography>
                      
                      <Link
                        to={`/home/company/${employer.company.id}`}
                        className="container-row"
                        
                        style={{
                          background: "#FAFAFA",
                          borderRadius: "0.3rem",
                          padding: "0.2rem",
                          width: "70%",
                          marginBottom: "1rem",
                          justifyContent:"center",
                          color:'#4A4A4A'
                        }}
                      >
                        <Typography style={{ color: "4A4A4A" }}>{employer.company.name}</Typography>
                      </Link>
                     
                      
                    </Box>
                  </Box>
                </Grid>
                <Grid item sm={9} xs={12}>
                  <Box style={{ padding: "1rem", height:'100%' }}>
                    <Box className={classes.card} style={{ paddingLeft: "3rem" }}>
                      <Typography
                        className={classes.h2Text}
                        style={{ marginBottom: "1rem" }}
                      >
                        About
                      </Typography>
                      <Typography
                        className={classes.descText}
                        style={{ textTransform: "uppercase", marginBottom: "1rem" }}
                      >
                        Basic Information
                      </Typography>
        
                        <KeyValue data="Registered Date" value= "24th March, 2021" />
                          <KeyValue data="City" value= "Alahabad" />
                          <KeyValue data="State" value= "UP" />
                          <KeyValue data="Country" value= "India" />
                          
    
                      
                      <Box
                        style={{
                          height: "2px",
                          width: "85%",
                          background: "#FAFAFA",
                          margin: "1rem 0",
                        }}
                      />
                      <Typography
                        className={classes.descText}
                        style={{ textTransform: "uppercase", marginBottom: "1rem" }}
                      >
                        Conatct Details
                      </Typography>
                        <KeyValue data="Email" value= {employer.user.email} />
                        <KeyValue data="Mobile No." value= {employer.mobile} />
                        <KeyValue data="Alternate Mobile No." value= {employer.alternate_mobile} />
                    </Box>
                  </Box>
                </Grid>
                <Grid item sm={3} xs={12}>
                  <Box style={{ padding: "1rem", height:'100%' }}>
                    <Box className={`${classes.card}`}>
                      <Typography
                        className={classes.h2Text}
                        style={{ marginBottom: "1rem" }}
                      >
                        Elsewhere
                      </Typography>
                      
                      <Grid container style={{ marginBottom: "1rem" }}>
                          
                        <Grid item xs={4}>
                          
                          <IconButton
                            disableRipple
                            style={{ color: "#2867B2", padding: "0" }}
                          >
                              
                            <LinkedIn className={classes.socialIcons} />
                          </IconButton>
                        </Grid>
                        <Grid item xs={8}>
                          <Typography className={classes.descText}>
                            LinkedIn
                          </Typography>
                        </Grid>
                      </Grid>
                      <Grid container style={{ marginBottom: "1rem" }}>
                        <Grid item xs={4}>
                          <IconButton
                            disableRipple
                            style={{ color: "black", padding: "0" }}
                          >
                            <GitHub className={classes.socialIcons} />
                          </IconButton>
                        </Grid>
                        <Grid item xs={8}>
                          <Typography className={classes.descText}>
                            Github
                          </Typography>
                        </Grid>
                      </Grid>
                      <Grid container style={{ marginBottom: "1rem" }}>
                        <Grid item xs={4}>
                          <IconButton style={{ color: "#49a1eb", padding: "0" }}>
                            <Twitter className={classes.socialIcons} />
                          </IconButton>
                        </Grid>
                        <Grid item xs={8}>
                          <Typography className={classes.descText}>
                            Twitter
                          </Typography>
                        </Grid>
                      </Grid>
                      <Grid container style={{ marginBottom: "1rem" }}>
                          
                        <Grid item xs={4}>
                          
                          <IconButton
                            disableRipple
                            style={{  padding: "0" }}
                            href={employer.company.website}
                          >
                              
                            <Language className={classes.socialIcons} />
                          </IconButton>
                        </Grid>
                        <Grid item xs={8}>
                          <Typography className={classes.descText}>
                            Website
                            
                          </Typography>
                        </Grid>
                      </Grid>
                    </Box>
                  </Box>
                </Grid>
    
                <Grid item sm={9} xs={12}>
                  <Box style={{ padding: "1rem", height:'100%' }}>
                    <Box className={`${classes.card}`}>
                      <Typography
                        className={classes.h2Text}
                        style={{ marginBottom: "1rem" }}
                      >
                        Profile Description
                      </Typography>
                      <Typography className={classes.descText}>
                        Lorem Ipsum is simply dummy text of the printing and
                        typesetting industry. Lorem Ipsum has been the industry's
                        standard dummy text ever since the 1500s, when an unknown
                        printer took a galley of type and scrambled it to make a
                        type specimen book. It has survived not only five centuries,
                        but also the leap into electronic typesetting, remaining
                        essentially unchanged
                      </Typography>
                      
                    </Box>
                  </Box>
                </Grid>
    
               </Grid>
            </Box>
          </Box>)
            ):
            "loading"}
      
    </>
  );
};
