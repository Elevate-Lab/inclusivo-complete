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
    InputAdornment,
    LinearProgress,
    Snackbar
} from '@material-ui/core';
import { 
    CenterFocusWeak,
    LinkedIn,
    Instagram,
    Facebook,
    Twitter,
    Language,
    Email,
    Phone,
    Sms,
} from '@material-ui/icons';
import { useHistory } from 'react-router-dom';
import { storage } from '../../firebase/index';
import { baseUrl } from '../../urlConstants.js';
import Alert from '@material-ui/lab/Alert';
import Loader from '../../assets/loader/loader'

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

const AddCompany = (props) => {
    const classes = useStyles();
    const history = useHistory();
    const [profileImageValues, setprofileImageValues] = React.useState(null);
    const [profilePreview, setProfilePreview] = React.useState({
        profileImg: blankImage
    })
    const [companyData,setCompanyData] = React.useState({
        name: "",
        title: "",
        short_code: "",
        phone_number: "",
        size: "",
        profile: "",
        description: "",
        email: "",
        address: "",
        website: "",
        twitter: "",
        facebook: "",
        linkedin: "",
        instagram: "",
        logo_url: ""
    })
    const [profileImageSelected, setprofileImageSelected] = React.useState(false);
    const [progress, setProgress] = React.useState(0);
    const [imageUploading, setImageUploading] = React.useState(false);
    const [isAdding,setIsAdding] = React.useState(false);
    const [successMsg,setMsg] = React.useState('');
    const [isSuccess,setIsSuccess] = React.useState(false);
    const handleCloseSuccessMessage = () => {
        setIsSuccess(false);
        if (props.location.state.mode === "ProfileComplete"){
            history.goBack();
        }
    }
 
    const handleChange = (prop) => (event) => {
        setCompanyData({...companyData, [prop]: event.target.value})
    };
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
        const uploadImage = storage.ref(`company/logo/${profileImageValues.name.slice(0, -4)}_${profileImageValues.lastModified}`).put(profileImageValues);
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
                    .ref("company/logo")
                    .child(`${profileImageValues.name.slice(0, -4)}_${profileImageValues.lastModified}`)
                    .getDownloadURL()
                    .then(url => {
                        console.log(url);
                        setProfilePreview({
                            profileImg: url
                        })
                        setCompanyData({ ...companyData, logo_url: url })
                        setprofileImageSelected(false);
                        setImageUploading(false);
                    })
            }
        )
    }

    const handleSubmit = async (e) => {
        e.preventDefault();
        const body = companyData;
        const key = localStorage.getItem('key');
        const requestOptions = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Access-Control-Allow-Origin': '*',
                'Authorization': `token ${key}`,
            },
            body: JSON.stringify(body)
        };
        setIsAdding(true);
        await fetch(`${baseUrl}/company/add/`, requestOptions)
            .then(res => res.json())
            .then(res => {
                if(res.status === "OK"){
                    setIsAdding(false);
                    setMsg(res.message);
                    setIsSuccess(true);
                }
            })
            .catch(err => {
                console.log(err);
            })
    }

    const formTypes = [
        {
            label: "Company Name",
            name: "name",
            type: "text"
        },
        {
            label: "Website Url",
            name: "website",
            type: "text",
            icon: true,
        },
        {
            label: "Email Address",
            name: "email",
            type: "email",
            icon: true,
        },
        {
            label: "Title",
            name: "title",
            type: "text"
        },
        {
            label: "Profile Description",
            type: "textarea",
            name: "profile"
        },
        {
            label: "Description",
            type: "textarea",
            name: "description",
        },
        {
            label: "Size",
            name: "size",
            type: "select",
        },
        {
            label: "Contact Number",
            type: "text",
            name: "phone_number",
            icon: true,
        },
        {
            label: "Short Code",
            type: "text",
            name: "short_code",
            icon: true,
        }
    ]

    const iconsDisplay = (data) => {
        switch (data) {
            case "website":
                return(
                    <Language />
                )
            case "email": 
                return(
                    <Email />
                )
            case "phone_number": 
                    return(
                        <Phone />
                    )
            case "short_code": 
                    return(
                        <Sms />
                    )
            default:
                return;
        }

    }

    const displayField = (data,idx) => {
        return(
            <div className={classes.formInputs} key={idx}>
                <TextField 
                    onChange={handleChange(data.name)}
                    className={classes.fieldInput}
                    name={data.name}
                    id={data.name}
                    label={data.label}
                    type={data.type}
                    {
                        ...(data.type ==="textarea" && {
                            multiline: true,
                            rows: 3,
                            variant: "outlined"
                        })
                    }
                    {
                        ...(data.type === "select" && {
                            select : true,
                            defaultValue: 0
                        })
                    }
                    {
                        ...(data.icon && {
                            InputProps: {
                                    endAdornment: <InputAdornment position="end">
                                        {iconsDisplay(data.name)}                          
                                    </InputAdornment>
                                }
                        })
                    }
                >
                    {
                        data.type ==="select" && [1,2,3,4,5,6,7,8,9,10].map((option) => (
                            <MenuItem key={option} value={option}>
                                {option}
                            </MenuItem>
                        ))
                    }
                </TextField>
            </div>
        )
    }

    const socialMediaLinksField = [
        {
            icon: <LinkedIn />,name: "linkedin"},
        {
            icon: <Twitter />,name: "twitter"},
        {
            icon: <Facebook />,name: "facebook"},
        {
            icon: <Instagram />,name: "instagram"}
        ];
                    
    return (
        <>
            {imageUploading ? (
                <LinearProgress variant="determinate" value={progress} />
            ) : (<></>)}
            {
                isAdding ? <div>
                    <Loader loading={isAdding} />
                </div> : <Grid container direction="column" justify="center" spacing={5} alignItems="center">
                    <Grid item>
                        <Typography className={classes.formName}>
                            Add Company
                </Typography>
                    </Grid>
                    <Grid item>
                        <form className={classes.formContainer}>

                            <Grid container direction="column" alignItems="center" justify="center" className={classes.formInputs}>
                                <Grid item className={classes.profileField}>
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
                            </Grid>
                            {
                                profileImageSelected ? <div>
                                    <Button
                                        onClick={uploadProfileImage}
                                        className={classes.formButton}
                                        variant="contained"
                                        color="secondary"
                                    >
                                        Upload
                                </Button>
                                </div> : (<></>)
                            }

                            {
                                formTypes.map((formType, idx) => (
                                    displayField(formType, idx)
                                ))
                            }

                            {
                                socialMediaLinksField.map((value, idx) => (
                                    <div className={classes.formInputs} key={idx}>
                                        <Grid container>
                                            <Grid item xs={2}>
                                                {value.icon} /
                                    </Grid>
                                            <Grid item xs={10}>
                                                <TextField className={classes.fieldInput} onChange={handleChange(value.name)} />
                                            </Grid>
                                        </Grid>
                                    </div>
                                ))
                            }

                            <div>
                                <Button className={classes.formButton} variant="contained" color="secondary" type="submit" onClick={handleSubmit} >
                                    Add Company
                            </Button>
                            </div>
                        </form>
                    </Grid>
                    <Snackbar open={isSuccess} onClose={handleCloseSuccessMessage}>
                        <Alert onClose={handleCloseSuccessMessage} severity="success">
                            {successMsg}
                        </Alert>
                    </Snackbar>
                </Grid>
            }
        </>
    )
}

export default AddCompany;