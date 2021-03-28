import React from 'react';
import {baseUrl} from '../../urlConstants'
import { Link } from 'react-router-dom';
import { makeStyles, useTheme } from '@material-ui/core/styles';
import clsx from 'clsx'
import {
    Typography,
    CardContent,
    Card,
    Grid,
    FormControl,
    Select,
    MenuItem
} from '@material-ui/core'
import ApplicantCardSkeleton from '../Loaders/ApplicantCardSkeleton'
import toBeReviewed from '../../assets/Icons/ToBeReviewed.png'
import inProcess from '../../assets/Icons/InProcess.png'
import shortlisted from '../../assets/Icons/ShortListed.png'
import accepted from '../../assets/Icons/Accepted.png'
import rejected from '../../assets/Icons/Rejected.png'
import ApplicantsCard from './ApplicantsCard'
import ApplicantDescriptionComponent from './ApplicantDescriptionComponent'

const useStyles = makeStyles((theme) => ({
  root: {
    display: 'none',
    position: "relative",
    maxWidth: "194px",
    flex : "1 1",
    overflow: "visible",
    borderRadius: "5px",
    boxShadow: "none",
    margin: "6px",
    cursor: "pointer",
    '&:hover':{
        boxShadow: "0px 2px 20px -3px rgba(64, 58, 58, 0.25)"
    },
    [theme.breakpoints.up('md')]: {
        display: "flex"
    }
  },
  selected: {
    boxShadow: "0px 2px 20px -3px rgba(64, 58, 58, 0.25)"
  },
  details: {
    display: 'flex',
    flexDirection: 'column',
    marginTop: "56px",
    marginLeft: "4px",
    '& .MuiCardContent-root:last-child':{
        paddingBottom: '12px'
    }
  },
  content: {
    flex: '1 0 auto',
  },
  cover: {
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    width: "86px",
    height: "86px",
    borderRadius: "5px",
    background: '#FD970F',
    position: "absolute",
    left: "20px",
    top: "-22px"
  },
  controls: {
    display: 'flex',
    alignItems: 'center',
    paddingLeft: theme.spacing(1),
    paddingBottom: theme.spacing(1),
  },
  playIcon: {
    height: 38,
    width: 38,
  },
  dropdownContainer: {
    padding: "8px",
    background: "#FAFAFA",
    borderRadius: "5px",
    [theme.breakpoints.up('md')]: {
        display: "none"
    }
  },
  dropdownImageContainer: {
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    borderRadius: "5px",
    background: '#FD970F',
    width: "36px",
    height: "36px",
  },
  dropdownItem:{
    display: "flex",
    alignItems: "center",
    borderRadius: "5px"
  },
  small: {
      width: "24px",
  },
  formControl: {
    width: "100%",
    background: "#fff",
    borderRadius: '5px',
    '& .MuiSelect-select': {
      '&:focus': {
          background: "#fff !important"
      },
    },
    '& .MuiOutlinedInput-input': {
        padding: "0",
    },
    '& .MuiOutlinedInput-input': {
        padding: "0",
    },
    '& .Mui-focused .MuiOutlinedInput-notchedOutline':{
        border: 'none',
    },
    '& .MuiOutlinedInput-notchedOutline': {
        border: 'none'
    }
  },
  selectEmpty: {
      width: "100%",
  },
  applicantsContainer: {
    marginTop: "16px",
    padding: "4px 8px 8px 8px",
    background: "#FAFAFA",
    borderRadius: "5px",
  },
  messageContainer:{
      minHeight: "200px"
  },
}));


export default function ApplicantsComponent({id}) {
    const classes = useStyles();
    const [cardValue, setCardValue] = React.useState({
        value: 0,
        status: 'Pending'
    })
    const [loading, setLoading] = React.useState(true)
    const [applicantsList, setApplicantsList] = React.useState([])
    const [applicantInfo, setApplicantInfo] = React.useState({})
    const [showList, setShowList] = React.useState(true)
    const [error, setError] = React.useState('')
    const [processing, setProcessStatus] = React.useState(false)
    const [processMsg, setProcessMsg] = React.useState("")

    let authToken=1;
    if (localStorage.getItem('key')) {
        authToken = localStorage.getItem('key');
    }

    const getApplicantsList = async () => {
        const requestOptions = {
            method: 'GET',
            headers: { 
                'Content-Type': 'application/json', 
                'Access-Control-Allow-Origin': '*',
                'Authorization': `token ${authToken}`,
            },
        };
        const response = await fetch(`${baseUrl}/job/applications/${id}/list`, requestOptions);
        const res = await response.json();
        console.log(res);
        if(res.status==="error"){
            setError(res.message)
        } else{
            setApplicantsList(res.data)
        }
        setLoading(false)  
    }

    const updateStatus = async (status, applicant_id) => {
        setProcessStatus(true)
        console.log(status, applicant_id)
        const body = {
            status: status
        }
        const requestOptions = {
            method: 'POST',
            headers: { 
                'Content-Type': 'application/json', 
                'Access-Control-Allow-Origin': '*',
                'Authorization': `token ${authToken}`,
            },
            body: JSON.stringify(body)
        };
        const response = await fetch(`${baseUrl}/job/application/status/update/${applicant_id}/`, requestOptions);
        const data = await response.json();
        if(data.status==="error"){
            console.log(data.message)
            setProcessStatus(false)
            setProcessMsg("error")
        } else{
            getApplicantsList()
            setProcessStatus(false)
            setProcessMsg("success")
        }
    }

    React.useEffect(() => {
        getApplicantsList()
    }, [])

    React.useEffect(() => {
        console.log(applicantsList)
    }, [applicantsList])

    const handleSelectCard = (value, status) => () => {
        setCardValue({
            value, status
        })
        setShowList(true)
    }
    const handleDropdownChange = (event) => {
        setCardValue({
            value: event.target.value,
            status: cardData.filter(data => data.value===event.target.value)[0].status
        })
        setShowList(true)
    }
    const handleShowList = (info) => () => {
        console.log(info)
        setApplicantInfo(info)
        setShowList(!showList)
    }
    
    React.useEffect(() => {
        console.log(cardValue)
    }, [cardValue])
  
    const cardData = [
        {
            name: "To Be Reviewed",
            image: toBeReviewed,
            backgroundColor: '#FD970F',
            status: 'Pending',
            value: 0
        },
        {
            name: "In Process",
            image: inProcess,
            backgroundColor: '#06B0C5',
            status: 'Process',
            value: 1
        },
        {
            name: "Shortlisted",
            image: shortlisted,
            backgroundColor: '#A64A97',
            status: 'Shortlisted',
            value: 2
        },
        {
            name: "Accepted",
            image: accepted,
            backgroundColor: '#4AA64E',
            status: 'Selected',
            value: 3
        },
        {
            name: "Rejected",
            image: rejected,
            backgroundColor: '#E73E3A',
            status: 'Rejected',
            value: 4
        }
    ]

    const display = () => {
        return (
                cardData.map((data) =>{
                    return(
                        <Card 
                            key={data.value}
                            className={clsx(classes.root, {
                                [classes.selected]: cardValue.value===data.value
                            })}
                            onClick={handleSelectCard(data.value, data.status)}
                        >
                            <div 
                                className={classes.cover} 
                                style={{
                                    background: data.backgroundColor
                                }}
                            >
                                <img
                                    alt="Remy Sharp" 
                                    src={data.image}
                                />
                            </div>
                            <div className={classes.details}>
                                <CardContent className={classes.content}>
                                    <Typography>
                                        {data.name}
                                    </Typography>
                                    <Typography variant="subtitle1" color="textSecondary" style={{fontSize: "14px"}}>
                                        {applicantsList.filter((applicant) => applicant.status === data.status).length} Applicants
                                    </Typography>
                                </CardContent>
                            </div>   
                        </Card>
                    )
                })
        )
    }

    const displayDropDown = () => {
        return (
            cardData.map((data) => {
                return(
                    <MenuItem 
                        value={data.value} 
                        name={data.status}
                        key={data.value}
                    >
                        <div className={classes.dropdownItem}>
                            <div 
                                className={classes.dropdownImageContainer}
                                style={{
                                    background: data.backgroundColor
                                }}
                            >
                                <img
                                alt="Remy Sharp" 
                                src={data.image}
                                className={classes.small}
                                />
                            </div> 
                            <Typography style={{paddingLeft: "10px"}}>
                                {data.name}
                            </Typography>
                        </div>
                    </MenuItem>
                )
            })
        )
    }
    
    const theme = useTheme();
    return (
        <>
        <Grid container justify="space-between">
            {display()}
        </Grid>
        <Grid container className={classes.dropdownContainer}>
            <FormControl className={classes.formControl}>
                <Select
                    value={cardValue.value}
                    displayEmpty
                    className={classes.selectEmpty}
                    inputProps={{ 'aria-label': 'Without label' }}
                    variant="outlined"
                    onChange={handleDropdownChange}
                >
                    {displayDropDown()}
                </Select>
            </FormControl>
        </Grid>
        <Grid container className={classes.applicantsContainer}>
            {/* Loader */}
            {loading? 
                <ApplicantCardSkeleton />
            :
                error?
                    <Grid xs={12} item container justify="center" alignItems="center" className={classes.messageContainer}>
                        <Typography variant="body2">
                            {error}  
                        </Typography>
                    </Grid>
                :
                    !applicantsList.length?
                        <Grid xs={12} item container justify="center" alignItems="center" className={classes.messageContainer}>
                            <Typography variant="body2">
                                No applicants 
                            </Typography>
                        </Grid>
                    :   
                        <> 
                        <Grid container direction="column" item>
                            {applicantsList.filter((applicant) => applicant.status ===cardValue.status).length ? 
                                showList ? 
                                    applicantsList.
                                    filter((applicant) => applicant.status ===cardValue.status).
                                    map((applicant) => {
                                        return <ApplicantsCard 
                                                    applicant={applicant} 
                                                    key={applicant.id}
                                                    applied_on={applicant.application_date}
                                                    id={applicant.id}
                                                    handleShowList={handleShowList}
                                                />
                                    })
                                :
                                    <ApplicantDescriptionComponent 
                                        handleShowList={handleShowList}
                                        applicant={applicantInfo}    
                                        updateStatus={updateStatus}
                                        data={cardData}
                                        processing={processing}
                                        processMsg={processMsg}
                                    />
                            :
                                <Grid xs={12} item container justify="center" alignItems="center" className={classes.messageContainer}>
                                    <Typography variant="body2" >
                                        No applicants in {cardValue.status} sections
                                    </Typography>
                                </Grid>
                            }
                        </Grid>
                        </>
            }
        </Grid>
        </>
    );
}