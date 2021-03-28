import React from 'react';
import blankImage from '../../assets/blank_image.png';
import {
    makeStyles,
    Grid,
    Button,
    Typography,
    MenuItem,
    TextField,
    Avatar,
    Badge,
    withStyles,
    LinearProgress,
    Snackbar
} from '@material-ui/core';
import Alert from '@material-ui/lab/Alert';
import { CenterFocusWeak } from '@material-ui/icons';
import { storage } from '../../firebase/index';
import { baseUrl } from '../../urlConstants';
import { useSelector } from 'react-redux';
import { useHistory } from 'react-router-dom';

const useStyles = makeStyles(theme => ({
    container: {
        width: "90%",
        margin: "10px auto"
    },
    formName: {
        margin: "5px 0px 30px 0px",
        color: "red",
        fontSize: "2rem",
        textAlign: "center"
    },
    formContainer: {
        display: "flex",
        flexDirection: "column",
        justifyContent: "center",
        alignItems: "center",
        width: "60%",
        border: "1px solid #B0B0B0",
        padding: "2%",
        borderRadius: "15px",
        margin: "30px auto",
        [theme.breakpoints.down('xs')]: {
            width: "95%"
        }
    },
    formInputs: {
        width: "90%",
        margin: "12px auto",
        maxWidth: "100%"
    },
    formButton: {
        width: "90%",
        maxWidth: "100%",
        margin: "20px auto"
    },
    profileAvatar: {
        width: theme.spacing(12),
        height: theme.spacing(12),
        borderRadius: "100px"
    },
    profileInputLabel: {
        padding: "0px",
        height: "16px",
        width: "16px"
    },
    profileInputBadge: {
        cursor: "pointer",
        fontSize: "16px"
    },
    profileInputField: {
        display: "none"
    }
}));
const StyledBadge = withStyles((theme) => ({
    badge: {
        color: "#FFFFFF",
        top: 50,
        right: 15,
        backgroundColor: "#FF3750",
        padding: "15px 7px",
        borderRadius: "100px",
    }
}))(Badge);

const UpdateProfile = (props) => {
    const classes = useStyles();
    
    const [IsEmployerValues,setIsEmployerValues] = React.useState('');
    const [GenderValues,setGenderValues] = React.useState('');
    const [DateValues,setDateValues] = React.useState('');
    const [EmailAddressValues,setEmailAddressValues] = React.useState('');
    const [LastNameValues,setLastNameValues] = React.useState('');
    const [FirstNameValues,setFirstNameValues] = React.useState('');
    const [profileImageValues, setprofileImageValues] = React.useState(null);
    const [profilePreview, setProfilePreview] = React.useState({
        profileImg: blankImage
    })
    const [profileImageSelected,setprofileImageSelected] = React.useState(false);
    const [profileImageUrl,setProfileImageUrl] = React.useState('');
    const [progress,setProgress] = React.useState(0);
    const [imageUploading,setImageUploading] = React.useState(false);
    const history = useHistory();
    const [formErrors, setFormErrors] = React.useState({});
    const [isError, setIsError] = React.useState(false);

    const validateForm = () => {
        let temp = {}
        temp.firstName = FirstNameValues ? "" : "First Name is required."
        temp.lastName =  LastNameValues ? "" : "Last Name is required"
        temp.gender = GenderValues ? "" : "Gender is Required"
        temp.dob = DateValues ? "" : "Date of Birth is Required"
        temp.isEmployer = IsEmployerValues ? "" : "Required Field"

        setFormErrors({
            ...temp
        })

        return Object.values(temp).every(value => value === "")
    }

    const handleChange = (event) => {
        if(event.target.name === "firstName"){
            setFirstNameValues(event.target.value);
        }else if(event.target.name === "lastName"){
            setLastNameValues(event.target.value);
        }else if(event.target.name === "gender"){
            setGenderValues(event.target.value);
        }else if(event.target.name === "dob"){
            setDateValues(event.target.value);
        }else if(event.target.name === "isEmployer"){
            setIsEmployerValues(event.target.value);
        }
    };

    const handleProfileImage = (e) => {
        if(e.target.files[0]){
            const reader = new FileReader();
            reader.onload = () => {
                if (reader.readyState === 2) {
                    setProfilePreview({
                        profileImg: reader.result
                    })
                }
            }
            reader.readAsDataURL(e.target.files[0]);
            setprofileImageValues(e.target.files[0]);
            setprofileImageSelected(true);
            console.log(e.target.files[0]);
        }
    }

    const uploadProfileImage = (e) => {
        e.preventDefault();
        setImageUploading(true);
        const uploadImage = storage.ref(`user/profile/${profileImageValues.name.slice(0, -4)}_${profileImageValues.lastModified}`).put(profileImageValues);
        uploadImage.on(
            "state_changed",
            snapshot => {
                const progress = Math.round(
                    (snapshot.bytesTransferred/snapshot.totalBytes) * 100
                );
                setProgress((oldProgress) => {
                    if(oldProgress === 100){
                        return 0;
                    }
                    return progress;
                })
            },
            error => {
                console.log(error);
            },
            () => {
                storage
                    .ref("user/profile")
                    .child(`${profileImageValues.name.slice(0,-4)}_${profileImageValues.lastModified}`)
                    .getDownloadURL()
                    .then(url => {
                        console.log(url);
                        setProfilePreview({
                            profileImg: url
                        })
                        setProfileImageUrl(url);
                        setprofileImageSelected(false);
                        setImageUploading(false);
                    })
            }
        )
    }

    const submitFunction = async (e) => {
        e.preventDefault();
        if(validateForm()){
            let employerBool;
            if (IsEmployerValues === 'Employer') {
                employerBool = true;
            } else if(IsEmployerValues === "Candidate") {
                employerBool = false;
            }

            const key = localStorage.getItem('key');

            const body = {
                first_name: FirstNameValues,
                last_name: LastNameValues,
                email: EmailAddressValues,
                dob: DateValues,
                gender: GenderValues,
                is_employer: employerBool,
                photo_url: profileImageUrl
            }

            const requestOptions = {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Access-Control-Allow-Origin': '*',
                    'Authorization': `token ${key}`,
                },
                body: JSON.stringify(body)
            };
            await fetch(`${baseUrl}/user/update/`, requestOptions).
            then(res => res.json())
            .then(res => {
                if(res.status === "OK"){
                    if(res.data.is_employer){
                        history.push({
                            pathname: '/complete/employer',
                            state: {
                                from: props.location
                            }
                        })
                    } else {
                        history.push({
                            pathname: '/complete/candidate',
                            state: {
                                from: props.location
                            }
                        })
                    }
                } 
            }).catch(err => {
                console.log(err);
                setIsError(true);
            });
        }   
    }

    React.useLayoutEffect(() => {
        if(localStorage.getItem('userEmail')){
            setEmailAddressValues(localStorage.getItem('userEmail'));
        }
    },[]);

    return (
        <>
        {imageUploading ? (
            <LinearProgress variant="determinate" value={progress} />
        ) : (<></>)}
        <Grid container direction="column" justify="center" alignItems="center" className={classes.container}>
            <Grid item xs={12}>
                <Typography className={classes.formName}>
                    Update Profile
                </Typography>
            </Grid>
                <Grid item xs={12} className={classes.formContainer}>
                <form onSubmit={submitFunction}>
                    <Grid container item direction="column" alignItems="center" justify="center" className={classes.formInputs}>
                        <StyledBadge badgeContent={
                            <>
                                <label htmlFor="imageInput" className={classes.profileInputLabel}>
                                    <CenterFocusWeak className={classes.profileInputBadge} />
                                </label>
                                <input 
                                    accept="image/*"
                                    name="profileImage"
                                    type="file" 
                                    id="imageInput" 
                                    className={classes.profileInputField}
                                    onChange={handleProfileImage}
                                />
                            </>
                            }    
                            anchorOrigin={{
                                vertical: "bottom",
                                horizontal: "right"
                            }}
                            >
                            <Avatar 
                                alt="Profile Image" 
                                src={profilePreview.profileImg} 
                                className={classes.profileAvatar} 
                            />
                        </StyledBadge>
                    </Grid>
                    {
                        profileImageSelected ? <div>
                                <Button 
                                    className={classes.formButton} 
                                    variant="contained" 
                                    color="secondary"
                                    onClick={uploadProfileImage} 
                                >
                                    Upload
                                </Button>
                            </div> : (<></>)
                    }
                    <Grid container justify="center" alignItems="center">
                        <TextField
                            id="firstName"
                            name="firstName"
                            label="First Name"
                            onChange={handleChange}
                            value={FirstNameValues} 
                            className={classes.formInputs}
                            {...(formErrors.firstName && {
                                error: true,
                                helperText: formErrors.firstName
                            })}
                        />
                        <TextField
                            id="lastName"
                            name="lastName"
                            label="Last Name"
                            onChange={handleChange}
                            value={LastNameValues} 
                            className={classes.formInputs}
                            {...(formErrors.lastName && {
                                error: true,
                                helperText: formErrors.lastName
                            })}
                        />
                        <TextField
                            id="emailAddress"
                            value={EmailAddressValues}
                            disabled
                            name="emailAddress"
                            label="Email Address"
                            onChange={handleChange}
                            className={classes.formInputs}
                        />   
                        <TextField
                            className={classes.formInputs}
                            id="gender"
                            name="gender"
                            select
                            label="Gender"
                            value={GenderValues}
                            onChange={handleChange}
                            InputLabelProps={{
                                shrink: true,
                            }}
                            {...(formErrors.gender && {
                                error: true,
                                helperText: formErrors.gender.length > 0 ? formErrors.lastName : "Please Select Your Gender"
                            })}
                        >
                            {["Male","Female","Other"].map((option) => (
                                <MenuItem key={option} value={option}>
                                    {option}
                                </MenuItem>
                            ))}
                        </TextField>
                        <TextField
                            className={classes.formInputs}
                            id="dob"
                            name="dob"
                            label="Date of Birth"
                            type="date"
                            value={DateValues}
                            InputLabelProps={{
                                shrink: true,
                            }}
                            onChange={handleChange}
                            {...(formErrors.dob && {
                                error: true,
                                helperText: formErrors.dob
                            })}
                        />
                        <TextField
                            className={classes.formInputs}
                            id="isEmployer"
                            value={IsEmployerValues}
                            onChange={handleChange}
                            name="isEmployer"
                            select
                            {...(formErrors.isEmployer && {
                                error: true,
                                helperText: formErrors.isEmployer
                            })}
                        >
                            {[{label: "Employer",value:true},{label: "Candidate",value:false}].map((option) => (
                                <MenuItem key={option.label} value={option.label}>
                                    {option.label}
                                </MenuItem>
                            ))}
                        </TextField>
                        {
                            (
                                <Button className={classes.formButton} variant="contained" color="secondary" type="submit">
                                    Update
                                </Button>
                            ) 
                        }
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
                    </Grid>
                </form>
            </Grid>
        </Grid>
        </>
    )
}

export default UpdateProfile;