import React from 'react'
import {
    IconButton,
    Grid,
    Button,
    Typography,
    Chip,
    makeStyles,
    Snackbar,
    CircularProgress
} from '@material-ui/core'
import {
    ClearRounded
} from '@material-ui/icons'
import {
    Link
} from 'react-router-dom'
import MuiAlert from '@material-ui/lab/Alert';
import { green } from '@material-ui/core/colors';
import blank_image from '../../assets/blank_image.png'
import Moment from 'react-moment';
import clsx from 'clsx'
import { TextField } from '@material-ui/core';
import { baseUrl } from '../../urlConstants';

const useStyles = makeStyles(theme => ({
    root: {
        padding: "10px",
        background: "#fff",
        marginTop: "4px",
    },
    closeBtn: {
        background: "#FAFAFA",
        borderRadius: "5px",
        padding: "6px"
    },
    btnText: {
        color: "#fff"
    },
    container: {
        padding: "0 64px",
        [theme.breakpoints.down('xs')]: {
            flexDirection: "column",
            padding: 0
        }
    },
    displayProfile: {
        width: "120px",
        borderRadius: "5px",
        padding: "10px",
        [theme.breakpoints.down('xs')]: {
            width: "80px"
        }
    },
    subContainer1: {
        flex: "1 1",
        borderRadius: "5px"
    },
    subContainer2: {
        flex: "3 2",
        borderRadius: "5px"
    },
    title: {
        fontWeight: 600,
        letterSpacing: "0.05em",
        color: '#5a5a5a'
    },
    caption: {
        color: "#7a7a7a"
    },
    chip: {
        borderRadius: "5px",
        background: "#fafafa"
    },
    my: {
        margin: "8px 0"
    },
    my2: {
        margin: "0 0 8px 0"
    },
    my3: {
        margin: "0 0 4px 0"
    },
    mx: {
        margin: "0px 4px"
    },
    mx2: {
        margin: "0 8px"
    },
    mx3: {
        margin: "0 16px"
    },
    detailsContainer: {
        background: "#fafafa",
        padding: "16px 16px 4px 16px"
    },
    buttonSuccess: {
        backgroundColor: green[500],
        '&:hover': {
            backgroundColor: green[700],
        },
    },
    buttonProgress: {
        color: green[200],
        position: 'absolute',
        top: '50%',
        left: '50%',
        marginTop: -12,
        marginLeft: -12,
    },
    wrapper: {
        margin: theme.spacing(1),
        position: 'relative',
    },
}))

function Alert(props) {
    return <MuiAlert elevation={6} variant="filled" {...props} />;
}

function ApplicantDescriptionComponent({ handleShowList, applicant, updateStatus, data, processing, processMsg }) {
    const classes = useStyles()
    const [success, setSuccess] = React.useState(false);
    const [disable, setDisable] = React.useState(false);
    const [snackOpen, setSnackOpen] = React.useState(false);
    const [snackMsg, setSnackMsg] = React.useState("Processed");
    const [recruiterNotesText, setRecruiterNotesText] = React.useState("");

    const timer = React.useRef();

    let buttonClassname = clsx({
        [classes.buttonSuccess]: success,
        [classes.mx]: true
    });

    React.useEffect(() => {
        return () => {
            clearTimeout(timer.current);
        };
    }, []);

    let authToken = 1;
    if (localStorage.getItem('key')) {
        authToken = localStorage.getItem('key');
    }

    const getNotes = async () => {
        const requestOptions = {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Access-Control-Allow-Origin': '*',
                'Authorization': `token ${authToken}`,
            },
        };
        const response = await fetch(`${baseUrl}/job/application/status/${applicant.id}`, requestOptions);
        const res = await response.json();
        if (res.data?.[res.data.length - 1].recruiter_notes) {
            setRecruiterNotesText(res.data[res.data.length - 1].recruiter_notes);
        }
    }

    React.useEffect(() => {
        getNotes();
    }, []);

    const handleButtonClick = () => {
        if (!processing) {
            setSuccess(false);
            setDisable(true);
        }
    };

    const handleSnackClose = (event, reason) => {
        if (reason === 'clickaway') {
            return;
        }
        setSnackOpen(false);
    };

    // Methods
    const toFilter = (d) => {
        let date = '';
        if (d[0] === 'a') {
            date = '1' + d.slice(1)
            return date
        }
        else return d;
    }

    const aboutBasicData = [
        {
            key: "Date of Birth:",
            value: applicant.candidate.user.dob
        },
        {
            key: "Nationality:",
            value: applicant.candidate.nationality
        },
        {
            key: "Gender:",
            value: applicant.candidate.user.gender
        },
    ]
    const aboutContactData = [
        {
            key: "Email Address:",
            value: applicant.candidate.user.email
        },
        {
            key: "Contact Number:",
            value: applicant.candidate.mobile
        },
        {
            key: "Address:",
            value: applicant.candidate.city.name + ", " + applicant.candidate.city.state.country.name
        }
    ]

    const handleClick = (type, id, textmsg) => () => {
        updateStatus(type, id, recruiterNotesText);
        handleButtonClick();
        setSnackOpen(true);
        setSnackMsg(textmsg);
    }

    const displayButton = (status) => {
        switch (status) {
            case "Pending":
                return (
                    <>
                        <div className={classes.wrapper}>
                            <Button
                                variant="contained"
                                color="secondary"
                                className={buttonClassname}
                                disabled={processing || disable}
                                onClick={handleClick("Process", applicant.id, "Processed")}
                                style={{ backgroundColor: !disable ? data.filter((value) => value.status === "Process")[0].backgroundColor : null }}
                                size="small"
                                ariaLabel="Mark as Reviewed"
                            >
                                <Typography variant="caption" className={clsx(classes.btnText, classes.mx3)}>
                                    <strong>Mark as Reviewed</strong>
                                </Typography>
                            </Button>
                            {processing && <CircularProgress size={24} className={classes.buttonProgress} />}
                            {
                                processMsg !== "" && !processing ?
                                    processMsg === "success" ?
                                        <Snackbar
                                            open={snackOpen}
                                            autoHideDuration={1500}
                                            onClose={handleSnackClose}
                                            anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}
                                        >
                                            <Alert onClose={handleSnackClose} severity="success">
                                                Candidate {snackMsg}
                                            </Alert>
                                        </Snackbar>
                                        :
                                        <Snackbar open={snackOpen} autoHideDuration={1500} onClose={handleSnackClose} anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}>
                                            <Alert onClose={handleSnackClose} severity="error">
                                                Some error occurred
                                        </Alert>
                                        </Snackbar>
                                    :
                                    null
                            }

                        </div>

                    </>
                )
            case "Process":
                return (
                    <>

                        {["Shortlisted", "Rejected"]
                            .map((action) => {
                                return (
                                    <div className={classes.wrapper}>
                                        <Button
                                            variant="contained"
                                            color="secondary"
                                            ariaLabel={action.slice(0, action.length - 2)}
                                            className={buttonClassname}
                                            disabled={processing || disable}
                                            onClick={handleClick(action, applicant.id, action)}
                                            style={{ backgroundColor: !disable ? data.filter((value) => value.status === action)[0].backgroundColor : null }}
                                            size="small"
                                        >
                                            <Typography variant="caption" className={clsx(classes.btnText, classes.mx3)}>
                                                <strong>{action.slice(0, action.length - 2)}</strong>
                                            </Typography>
                                        </Button>
                                        {processing && <CircularProgress size={24} className={classes.buttonProgress} />}
                                    </div>

                                )
                            })
                        }
                        {
                            processMsg !== "" && !processing ?
                                processMsg === "success" ?
                                    <Snackbar open={snackOpen} autoHideDuration={1500} onClose={handleSnackClose} anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}>
                                        <Alert onClose={handleSnackClose} severity={snackMsg === "Rejected" ? "warning" : "success"}>
                                            Candidate {snackMsg}
                                        </Alert>
                                    </Snackbar>
                                    :
                                    <Snackbar open={snackOpen} autoHideDuration={1500} onClose={handleSnackClose} anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}>
                                        <Alert onClose={handleSnackClose} severity="error">
                                            Some error occurred
                                        </Alert>
                                    </Snackbar>
                                :
                                null
                        }


                    </>
                )
            case "Shortlisted":
                return (
                    <>
                        <div className={classes.wrapper}>
                            <Button
                                variant="contained"
                                color="secondary"
                                ariaLabel="Accept"
                                className={buttonClassname}
                                disabled={processing || disable}
                                onClick={handleClick("Selected", applicant.id, "Shortlisted")}
                                style={{ backgroundColor: !disable ? data.filter((value) => value.status === "Selected")[0].backgroundColor : null }}
                                size="small"
                            >
                                <Typography variant="caption" className={clsx(classes.btnText, classes.mx3)}>
                                    <strong>Accept</strong>
                                </Typography>
                            </Button>
                            <Button
                                variant="contained"
                                color="secondary"
                                ariaLabel="Reject"
                                className={buttonClassname}
                                disabled={processing || disable}
                                onClick={handleClick("Rejected", applicant.id, "Rejected")}
                                style={{ backgroundColor: !disable ? data.filter((value) => value.status === "Rejected")[0].backgroundColor : null }}
                                size="small"
                            >
                                <Typography variant="caption" className={clsx(classes.btnText, classes.mx3)}>
                                    <strong>Reject</strong>
                                </Typography>
                            </Button>
                            {processing && <CircularProgress size={24} className={classes.buttonProgress} />}
                            {
                                processMsg !== "" && !processing ?
                                    processMsg === "success" ?
                                        <Snackbar open={snackOpen} autoHideDuration={1500} onClose={handleSnackClose} anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}>
                                            <Alert onClose={handleSnackClose} severity={snackMsg === "Rejected" ? "warning" : "success"}>
                                                Candidate {snackMsg}
                                            </Alert>
                                        </Snackbar>
                                        :
                                        <Snackbar open={snackOpen} autoHideDuration={1500} onClose={handleSnackClose} anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}>
                                            <Alert onClose={handleSnackClose} severity="error">
                                                Some error occurred
                                    </Alert>
                                        </Snackbar>
                                    :
                                    null
                            }
                        </div>

                    </>
                )
            default:
                return null
        }
    }

    return (
        <Grid container className={classes.root}>
            <Grid item container justify="space-between" alignItems="center">
                <Typography variant="caption" className={classes.mx2}>
                    <Moment filter={toFilter} fromNow>{applicant.application_date}</Moment>
                </Typography>
                <IconButton disableRipple onClick={handleShowList({})} className={classes.closeBtn}>
                    <ClearRounded fontSize="small" />
                </IconButton>
            </Grid>

            <Grid item container justify="center" className={classes.container}>
                <Grid item container direction="column" alignItems="center" className={classes.subContainer1}>

                    {/* DP section */}
                    <Grid item container direction="column" alignItems="center" className={classes.my}>
                        <img
                            src={applicant.candidate.user.photo_url ? applicant.candidate.user.photo_url : blank_image}
                            alt="profile"
                            className={classes.displayProfile}
                        />
                        <Typography variant="caption" className={classes.title} style={{ fontSize: "14px" }}>
                            {applicant.candidate.user.first_name} {applicant.candidate.user.last_name}
                        </Typography>
                        <Typography variant="caption" className={classes.caption}>
                            {applicant.candidate.job_role}
                        </Typography>
                    </Grid>

                    {/* Diversity Section */}
                    <Grid item container direction="column" alignItems="center" className={classes.my}>
                        <Typography className={clsx(classes.title, classes.my)}>
                            Diversity Tags
                        </Typography>
                        {applicant.candidate.diversity_tags.map((tag) => {
                            return (
                                <Chip
                                    label={tag.name}
                                    key={tag.id}
                                    className={clsx(classes.chip, classes.my2)}
                                />
                            )
                        })}
                    </Grid>
                </Grid>
                <Grid item container direction="column" alignItems="center" className={clsx(classes.subContainer2, classes.my)}>

                    {/* Profile Description section */}
                    <Grid item container className={clsx(classes.my, classes.detailsContainer)}>
                        <Typography style={{ fontSize: "14px" }}>
                            {applicant.candidate.profile_description}
                        </Typography>
                        <Grid item container justify="center" className={clsx(classes.my)}>
                            <a href={applicant.candidate.resume_link}>
                                <Button className={classes.mx} ariaLabel="Resume Link">
                                    <Typography variant="caption" display="block" style={{ color: "#4694E7" }}>
                                        Resume Link
                                    </Typography>
                                </Button>
                            </a>
                            <Link to={`/profile/${applicant.candidate.id}`}>
                                <Button className={classes.mx} ariaLabel="Profile Link">
                                    <Typography variant="caption" display="block" style={{ color: "#4694E7" }}>
                                        Profile Link
                                    </Typography>
                                </Button>
                            </Link>
                        </Grid>
                    </Grid>

                    {/* Profile Details section */}
                    <Grid item container direction="column" className={clsx(classes.my, classes.detailsContainer)}>
                        <Typography className={clsx(classes.title)}>
                            About
                        </Typography>
                        <Typography variant="caption" className={clsx(classes.caption, classes.my)}>
                            BASIC INFORMATION
                        </Typography>
                        {aboutBasicData.map((data) => {
                            return (
                                <Grid item container className={classes.my3}>
                                    <Typography variant="caption" className={clsx(classes.title)} style={{ minWidth: "110px" }}>
                                        {data.key}
                                    </Typography>
                                    <Typography variant="caption" className={classes.caption}>
                                        {data.value}
                                    </Typography>
                                </Grid>
                            )
                        })}
                        <Typography variant="caption" className={clsx(classes.caption, classes.my)}>
                            CONTACT INFORMATION
                        </Typography>
                        {aboutContactData.map((data) => {
                            return (
                                <Grid item container className={classes.my3}>
                                    <Typography variant="caption" className={clsx(classes.title)} style={{ minWidth: "110px" }}>
                                        {data.key}
                                    </Typography>
                                    <Typography variant="caption" className={classes.caption}>
                                        {data.value}
                                    </Typography>
                                </Grid>
                            )
                        })}
                        <Typography variant="caption" className={clsx(classes.caption, classes.my)}>
                            NOTES
                        </Typography>
                        <TextField value={recruiterNotesText} placeholder="Recruiter Notes" onChange={(event) => {
                            setRecruiterNotesText(event.target.value);
                        }} />
                    </Grid>
                </Grid>
            </Grid>
            <Grid item container justify="center">
                {displayButton(applicant.status)}
            </Grid>
        </Grid>
    )
}

export default ApplicantDescriptionComponent
