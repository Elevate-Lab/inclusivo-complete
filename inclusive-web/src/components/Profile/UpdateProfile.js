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
import useForm from '../../customHooks/useForm';
import { format } from "date-fns";
import Controls from "../Form/Controls/Controls";

const useStyles = makeStyles(theme => ({
    container: {
        width: "90%",
        margin: "10px auto",
        '& .MuiButton-root:hover': {
            background: "#ff3750",
        }
    },
    formName: {
        margin: "30px 0px 10px 0px",
        color: "red",
        fontSize: "2rem",
        textAlign: "center"
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
        width: "100%",
        padding: "10px"
    },
    formButton: {
        width: "15rem",
        maxWidth: "90vw",
        margin: "20px auto",
        background: "#ff3750",
        color: "#fff",
        height: "36px"
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

const initialValues = {
    first_name: "",
    last_name: "",
    email: "",
    dob: format(Date.now(), 'yyyy-MM-dd'),
    gender: "",
    is_employer: false,
    photo_url: ""
};

const UpdateProfile = (props) => {
    const classes = useStyles();

    const [gender, setGender] = React.useState("");
    const [userType, setUserType] = React.useState("");

    const [profileImageValues, setprofileImageValues] = React.useState(null);
    const [profilePreview, setProfilePreview] = React.useState({
        profileImg: blankImage
    })
    const [profileImageSelected, setprofileImageSelected] = React.useState(false);
    const [progress, setProgress] = React.useState(0);
    const [imageUploading, setImageUploading] = React.useState(false);
    const [isError, setIsError] = React.useState(false)
    const history = useHistory();

    const [values, setValues, errors, setErrors, handleChange] = useForm(initialValues, false, () => { })

    const handleProfileImage = (e) => {
        if (e.target.files[0]) {
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
                    (snapshot.bytesTransferred / snapshot.totalBytes) * 100
                );
                setProgress((oldProgress) => {
                    if (oldProgress === 100) {
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
                    .child(`${profileImageValues.name.slice(0, -4)}_${profileImageValues.lastModified}`)
                    .getDownloadURL()
                    .then(url => {
                        console.log(url);
                        setProfilePreview({
                            profileImg: url
                        })
                        setValues({
                            ...values,
                            photo_url: url
                        });
                        setprofileImageSelected(false);
                        setImageUploading(false);
                    })
            }
        )
    }

    const handleChangeGender = (e) => {
        setGender(e.target.value);
    }

    const handleChangeUserType = (e) => {
        setUserType(e.target.value);
    }

    const submitFunction = async (e) => {
        e.preventDefault();
        // if(validate()){
        // let employerBool;
        // if (IsEmployerValues === 'Employer') {
        //     employerBool = true;
        // } else if(IsEmployerValues === "Candidate") {
        //     employerBool = false;
        // }

        const key = localStorage.getItem('key');

        const body = values
        body.gender = gender
        body.is_employer = userType === "Employer" ? true : false
        console.log(body)

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
                if (res.status === "OK") {
                    if (res.data.is_employer) {
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
                // setIsError(true);
            });
        // }   
    }

    React.useLayoutEffect(() => {
        if (localStorage.getItem('userEmail')) {
            setValues({
                ...values,
                email: localStorage.getItem('userEmail')
            })
        }
    }, []);

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
                <Grid item xs={12} >
                    <form onSubmit={submitFunction} className={classes.formContainer}>
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
                                    ariaLabel="upload"
                                >
                                    Upload
                                </Button>
                            </div> : (<></>)
                        }
                        <div className={classes.formInputs}>
                            <Controls.FormInput
                                value={values.first_name}
                                name="first_name"
                                handleChange={handleChange}
                                label="First Name"
                            />
                        </div>
                        <div className={classes.formInputs}>
                            <Controls.FormInput
                                value={values.last_name}
                                name="last_name"
                                handleChange={handleChange}
                                label="Last Name"
                            />
                        </div>
                        <div className={classes.formInputs}>
                            <Controls.FormInput
                                value={values.email}
                                name="email"
                                handleChange={() => { }}
                                label="Email Address"
                            />
                        </div>
                        <div className={classes.formInputs}>
                            <Grid container direction="column">
                                <Typography variant="h6" style={{ fontSize: "14px", margin: "10px 0px", letterSpacing: "0.4px" }}>
                                    Gender
                            </Typography>
                                <TextField
                                    value={gender}
                                    variant="outlined"
                                    style={{ width: "100%" }}
                                    className={classes.phoneFormInput}
                                    id="gender"
                                    name="gender"
                                    onChange={handleChangeGender}
                                    select
                                >
                                    <MenuItem key="Male" value="Male">
                                        Male
                                </MenuItem>
                                    <MenuItem key="Female" value="Female">
                                        Female
                                </MenuItem>
                                    <MenuItem key="Other" value="Other">
                                        Other
                                </MenuItem>
                                </TextField>
                            </Grid>
                        </div>
                        <div className={classes.formInputs}>
                            <Controls.DatePicker
                                values={values}
                                setValues={setValues}
                                label="Date of Birth"
                                name="dob"
                                onlyFuture={false}
                            />
                        </div>
                        <div className={classes.formInputs}>
                            <Grid container direction="column">
                                <Typography variant="h6" style={{ fontSize: "14px", margin: "10px 0px", letterSpacing: "0.4px" }}>
                                    Who are you ?
                            </Typography>
                                <TextField
                                    value={userType}
                                    variant="outlined"
                                    style={{ width: "100%" }}
                                    className={classes.phoneFormInput}
                                    id="userType"
                                    name="userType"
                                    onChange={handleChangeUserType}
                                    select
                                >
                                    <MenuItem key="Candidate" value="Candidate">
                                        Candidate
                                </MenuItem>
                                    <MenuItem key="Employer" value="Employer">
                                        Employer
                                </MenuItem>
                                </TextField>
                            </Grid>
                        </div>
                        {
                            (
                                <Button className={classes.formButton} variant="contained" type="submit" ariaLabel="Update">
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
                    </form>
                </Grid>
            </Grid>
        </>
    )
}

export default UpdateProfile;
