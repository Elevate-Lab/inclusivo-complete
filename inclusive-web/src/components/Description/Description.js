import React from 'react';
import { baseUrl } from '../../urlConstants'
import clsx from 'clsx'
import {
    Grid,
    makeStyles,
    IconButton,
    Typography,
    Chip,
    Button,
    TextField,
    Dialog,
    DialogActions,
    DialogContent,
    DialogContentText,
    DialogTitle,
    Snackbar,
    CircularProgress
} from '@material-ui/core';
import {
    ShareOutlined,
    Favorite,
    FavoriteBorder
} from '@material-ui/icons'
import MuiAlert from '@material-ui/lab/Alert';
import ApplicantsComponent from '../Applicants/ApplicantsComponent'
import '../../style.css';
import DescriptionHeader from './DescriptionHeader';
import Accordian from './Accordian'
import { useSelector } from 'react-redux'

function Alert(props) {
    return <MuiAlert elevation={6} variant="filled" {...props} />
}  

const useStyles = makeStyles(theme => ({
    mainContainer: {
        maxWidth: "1100px",
        margin: "0px auto",
        '& .MuiButton-root:hover': {
            background: "#ff3750",
            color: "#fff"
        }
    },
    subContainer: {
        maxWidth: '1100px',
        marginTop: '16px',
        marginBottom: '8px'
    },
    aboutContainer: {
        paddingTop: "20px",
        margin: "10px 0px",
        border: "1px solid #d9d9d9",
        borderRadius: "3px",
        width: '100%'
    },
    descriptionContainer: {
        marginTop: "16px",
        padding: "4px 8px 8px 8px",
        background: "#FAFAFA",
        borderRadius: "5px",
    },
    applicationViewContainer: {
        paddingTop: "20px",
        margin: "10px 0px",
        padding: "0 4px",
        width: '100%'
    },
    paddingClass: {
        padding: "0px 20px",
    },
    bodyKey: {
        fontWeight: theme.typography.fontWeightMedium,
        fontSize: "1rem"
    },
    bodyValue: {
        opacity: '0.5',
        fontSize: "0.8rem"
    },
    tag: {
        borderColor: '#ff3750',
        color: '#ff3750',
        height: '24px',
        margin: theme.spacing(0.5),
    },
    disabledButton: {
        border: '1px solid rgba(0,0,0,0.2)'
    },
    tabButton: {
        border: "1px solid #ff3750",
        color: "#ff3750",
        margin: "0 10px",
    },
    tabSelected: {
        background: "#ff3750",
        color: "#fff"
    },
    buttonProgress: {
        color: theme.palette.primary,
        position: 'absolute',
        top: '50%',
        left: '50%',
        marginTop: -8,
        marginLeft: -8,
    }
}))

const Description = ({ type, data, id, buttonVisibility }) => {
    const classes = useStyles();
    const [isLiked, setIsLiked] = React.useState(data.is_liked);
    const [isApplied, setIsApplied] = React.useState(type === "job" ? data.is_applied : null);
    const [tab, setTab] = React.useState(0)
    const userStatus = useSelector(state => state.userStatus)
    
    const [why, setWhy] = React.useState("")
    const [whyError, setWhyError] = React.useState(false)
    
    const [open, setOpen] = React.useState(false)
    const [openSnackbar, setOpenSnackbar] = React.useState(false)
    const [processing, setProcessing] = React.useState(false)
    const [processedMsg, setProcessedMsg] = React.useState("")
    const [severity, setSeverity] = React.useState("success")

    const handleSnackbarClose = (event, reason) => {
        if(reason === 'clickaway'){
            return;
        }
        setOpenSnackbar(false);
    }

    const handleOpen = () => {
        setOpen(true)
    }

    const handleClose = () => {
        setOpen(false)
    }
  
    const handleAnswer = (e) => {
        setWhy(e.target.value)
        if(e.target.value.length!==0) setWhyError(false)
    }

    let authToken = 1;
    if (localStorage.getItem('key')) {
        authToken = localStorage.getItem('key');
    }

    const handleApply = async (e) => {
        e.preventDefault()
        if (type === "job" && why.length!==0) {
            const body = {
                status: "Pending",
                message: why
            }
            setIsApplied(true)
            setProcessing(true)
            const requestOptions = {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Access-Control-Allow-Origin': '*',
                    'Authorization': `token ${authToken}`,
                },
                body: JSON.stringify(body)
            };

            const response = await fetch(`${baseUrl}/job/application/create/${id}/`, requestOptions);
            const data = await response.json();
            console.log(data.status);
            if(data.status === 'Inserted'){
                console.log("Hello")
                setSeverity('success')
                setProcessedMsg('Job apllication submitted')
                setOpenSnackbar(true)
                setProcessing(false)
            }else{
                setSeverity('error')
                setProcessedMsg('Some error occured')
                setIsApplied(false)
                setOpenSnackbar(true)
                setProcessing(false)
            }
        }
        else if(why.length===0){
            setWhyError(true);
        }
    }

    const handleLikefunc = (value, is_liked) => async () => {
        const body = {
            is_liked: is_liked
        }
        setIsLiked(is_liked)
        const requestOptions = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Access-Control-Allow-Origin': '*',
                'Authorization': `token ${authToken}`,
            },
            body: JSON.stringify(body)
        };
        const response = await fetch(`${baseUrl}/job/${type === "job" ? "" : "scholarship/"}${value}/${id}/?=`, requestOptions);
        const data = await response.json();
        console.log(data);
    }
    const handleTab = (value) => (e) => {
        e.preventDefault()
        setTab(value)
    }

    // Methods
    const toFilter = (d) => {
        let date = '';
        if (d[0] === 'a') {
            date = '1' + d.slice(1)
            return date
        }
        else return d;
    }

    return (
        <Grid container className={classes.mainContainer}>
            <Grid item container direction="row">
                <DescriptionHeader data={data} type={type} />
                <Grid item container alignItems="flex-end" direction="column" style={{ flex: "0 1" }}>
                    {isLiked ?
                        <IconButton onClick={handleLikefunc("unlike", false)} >
                            <Favorite style={{ color: "red" }} fontSize="small" />
                        </IconButton>
                        :
                        <IconButton onClick={handleLikefunc("like", true)}>
                            <FavoriteBorder style={{ color: "black" }} fontSize="small" />
                        </IconButton>
                    }
                    <IconButton>
                        <ShareOutlined fontSize='small' />
                    </IconButton>
                </Grid>
            </Grid>

            {/* Apply */}
            {userStatus.isEmployer ?
                type !== "job" ?
                    null
                    :
                    !buttonVisibility ?
                        null
                        :
                        <Grid container item justify="space-between" className={classes.subContainer}>
                            <Grid item>
                                <Button className={clsx(classes.tabButton, {
                                    [classes.tabSelected]: tab === 0
                                })} onClick={handleTab(0)} ariaLabel="Details">
                                    <Typography style={{ fontSize: "14px" }}>
                                        Details
                            </Typography>
                                </Button>
                                <Button
                                    className={clsx(classes.tabButton, {
                                        [classes.tabSelected]: tab === 1
                                    })}
                                    ariaLabel="View Applications"
                                    onClick={handleTab(1)}>
                                    <Typography style={{ fontSize: "14px" }}>
                                        View Applications
                            </Typography>
                                </Button>
                            </Grid>
                        </Grid>
                :
                <Grid container item justify="space-between" className={classes.subContainer}>
                    <Grid item>
                        <Snackbar open={openSnackbar} anchorOrigin={{ vertical:"bottom", horizontal:"center" }} autoHideDuration={6000} onClose={handleSnackbarClose}>
                            <Alert onClose={handleSnackbarClose} severity={severity}>
                                {processedMsg}
                            </Alert>
                        </Snackbar>
                        {type === "scholarship" ?
                            <a href={data.apply_url}>
                                <Button variant="outlined" className={classes.tabButton} ariaLabel="Apply Link">
                                    Apply Link
                                </Button>
                            </a>
                            :
                            data.is_apply_here ?
                                type === "job" && isApplied ?
                                    <Button disabled className={classes.disabledButton} ariaLabel="Applied">
                                        Applied
                                        {processing && <CircularProgress size={16} className={classes.buttonProgress} />}
                                    </Button>
                                    :
                                    <>
                                        <Button variant="outlined" className={classes.tabButton} onClick={handleOpen} ariaLabel="Apply">
                                            Apply
                                        </Button>
                                        <Dialog open={open} onClose={handleClose} aria-labelledby="form-dialog-title">
                                            <DialogTitle id="form-dialog-title">Apply</DialogTitle>
                                            <DialogContent>
                                            <DialogContentText>
                                                Send a message for why you are a good fit for the role?
                                            </DialogContentText>
                                            <TextField
                                                autoFocus
                                                margin="dense"
                                                id="answer"
                                                label="Answer"
                                                type="answer"
                                                fullWidth
                                                onChange={handleAnswer}
                                                error = {whyError}
                                                helperText= {whyError ? "This field should not be empty" : "Required field"}
                                            />
                                            </DialogContent>
                                            <DialogActions>
                                            <Button onClick={handleClose} color="primary">
                                                Cancel
                                            </Button>
                                            <Button onClick={handleApply} color="primary">
                                                Apply
                                            </Button>
                                            </DialogActions>
                                        </Dialog>
                                        
                                    </>
                                :
                                <a href={data.apply_url}>
                                    <Button variant="outlined" className={classes.tabButton} ariaLabel="Apply Link">
                                        Apply Link
                                    </Button>
                                </a>
                        }
                    </Grid>
                </Grid>
            }


            {/* Container */}
            {tab === 1 ?
                <Grid item container direction="column" className={classes.applicationViewContainer}>
                    <ApplicantsComponent id={id} />
                </Grid>
                :
                <Grid className={classes.descriptionContainer}>
                    <Accordian
                        title={type === "job" ? "Job Description" : "Description"}
                        data={data.description}
                    />

                    <Grid item container className={classes.secondContainer}>
                        <Grid item container className="requirements recruitmentTabs">
                            <Grid item>
                                <Typography variant="h6" gutterBottom style={{ fontWeight: '600' }}>
                                    Requirements
                                </Typography>
                            </Grid>
                            <Grid item container direction="column">
                                {!data.accepted_degrees ?
                                    null
                                    :
                                    <Grid item container direction="row">
                                        <Grid item xs={5}>
                                            <Typography className={classes.bodyKey}> Degrees </Typography>
                                        </Grid>
                                        <Grid xs={7} item container>
                                            {data.accepted_degrees.map((degree) => {
                                                return <Typography className={classes.bodyValue} key={degree.id}>{degree.degree_name} </Typography>
                                            })}
                                        </Grid>
                                    </Grid>
                                }
                                {!data.degrees ?
                                    null
                                    :
                                    !data.degrees.length ? null :
                                        <Grid item container direction="row">
                                            <Grid item xs={5}>
                                                <Typography className={classes.bodyKey}> Degrees </Typography>
                                            </Grid>
                                            <Grid xs={7} item container>
                                                {data.degrees.map((degree) => {
                                                    return <Typography className={classes.bodyValue} key={degree.id}>{degree.degree_name} </Typography>
                                                })}
                                            </Grid>
                                        </Grid>
                                }
                                {(!data.max_exp && !data.min_exp) ?
                                    null
                                    :
                                    <Grid item container direction="row">
                                        <Grid item xs={5}>
                                            <Typography className={classes.bodyKey}> Experience </Typography>
                                        </Grid>
                                        <Grid xs={7} item container>
                                            <Typography className={classes.bodyValue}> 2-5 years</Typography>
                                        </Grid>
                                    </Grid>
                                }
                            </Grid>
                        </Grid>
                        <Grid item container className="tags recruitmentTabs">
                            <Typography variant="h6" gutterBottom style={{ fontWeight: '600' }}>
                                Tags
                            </Typography>
                            {(!data.tags) ?
                                <Grid container item>
                                    <Chip
                                        label="Inclusivo"
                                        variant="outlined"
                                        className={classes.tag}
                                    />
                                </Grid>
                                :
                                <Grid container item>
                                    {data.tags.map((tag) => {
                                        return (<Chip
                                            label={tag.name}
                                            variant="outlined"
                                            className={classes.tag}
                                            key={tag.id}
                                        />)
                                    })}
                                    <Chip
                                        label="Inclusivo"
                                        variant="outlined"
                                        className={classes.tag}
                                    />
                                </Grid>
                            }
                        </Grid>
                        <Grid item container className="moreInfo recruitmentTabs">
                            <Typography variant="h6" gutterBottom style={{ fontWeight: '600' }}>
                                More Info
                            </Typography>
                            <Grid item container direction="column">
                                <Grid item container direction="row">
                                    <Grid item xs={5}>
                                        <Typography variant="body1" className={classes.bodyKey}>Last Date</Typography>
                                    </Grid>
                                    <Grid xs={7} item container wrap="nowrap">
                                        <Typography variant="body2" className={classes.bodyValue}>{data.last_date}</Typography>
                                    </Grid>
                                </Grid>
                                {!data.display_salary ?
                                    null
                                    :
                                    <Grid item container direction="row">
                                        <Grid item xs={5}>
                                            <Typography variant="body1" className={classes.bodyKey}>Salary</Typography>
                                        </Grid>
                                        <Grid xs={7} item container wrap="nowrap">
                                            <Typography variant="body2" className={classes.bodyValue}>
                                                {data.min_sal}-{data.max_sal} CTC
                                            </Typography>
                                        </Grid>
                                    </Grid>
                                }
                                <Grid item container direction="row">
                                    <Grid item xs={5}>
                                        <Typography variant="body1" className={classes.bodyKey}>Vacancies</Typography>
                                    </Grid>
                                    <Grid xs={7} item container wrap="nowrap">
                                        <Typography variant="body2" className={classes.bodyValue}>{data.vacancies}</Typography>
                                    </Grid>
                                </Grid>
                                {!data.accepted_locations ?
                                    null
                                    :
                                    !data.accepted_locations.length ? null :
                                        <Grid item container direction="row">
                                            <Grid item xs={5}>
                                                <Typography className={classes.bodyKey}> Locations </Typography>
                                            </Grid>
                                            <Grid xs={7} item container>
                                                {data.accepted_locations.map((location, idx) => {
                                                    return <Typography className={classes.bodyValue} key={location.id}>
                                                        {idx ? ',' : null}
                                                        {location.name}
                                                    </Typography>
                                                })}
                                            </Grid>
                                        </Grid>
                                }
                            </Grid>
                        </Grid>
                        <Grid container item className="selection">
                            <Accordian
                                title="Selection Process"
                                data={data.selection_process}
                            />

                        </Grid>
                    </Grid>
                    {data.company &&
                        <Accordian
                            title="About Company"
                            data={data.company.description}
                            button={true}
                            to={`/home/company/${data.company.id}`}
                            buttonName={"Company Link"}
                        />
                    }
                </Grid>
            }
        </Grid>
    )
}

export default Description;