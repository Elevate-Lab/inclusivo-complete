import React from 'react';
import Moment from 'react-moment';
import {baseUrl} from '../../urlConstants'
import clsx from 'clsx'
import {
    Link
} from 'react-router-dom'
import {
    Grid,
    makeStyles,
    IconButton,
    ButtonBase,
    Typography,
    Chip,
    Button
} from '@material-ui/core';
import {
    ShareOutlined,
    ExpandMore,
    ExpandLess,
    Favorite,
    FavoriteBorder
} from '@material-ui/icons'
import ApplicantsComponent from '../Applicants/ApplicantsComponent'
import '../../style.css';
import DescriptionHeader from './DescriptionHeader';

const useStyles = makeStyles(theme => ({
    mainContainer : {
        maxWidth: "1100px",
        margin : "0px auto"
    },
    subContainer: {
        maxWidth: '1100px',
        marginTop: '16px',
        marginBottom: '8px'
    },
    aboutContainer: {
        paddingTop : "20px",
        margin: "10px 0px",
        border: "1px solid #d9d9d9",
        borderRadius: "3px",
        width: '100%'
    },
    applicationViewContainer: {
        paddingTop : "20px",
        margin: "10px 0px",
        padding: "0 4px",
        width: '100%'
    },
    paddingClass: {
        padding: "0px 20px",
    },
    buttonContainer: {
        marginTop: "4px",
        padding: "10px 20px",
        boxShadow: "0px -1px 10px -4px rgba(0, 0, 0, 0.25)",
        borderRadius: "0px 0px 5px 5px",
    },
    btn: {
        background: "#fff",
        border: "none",
        '&:focus' : {
            outline : "none"
        },
        display: "flex",
        alignItems: "center"
    },
    bodyKey: {
        fontWeight: theme.typography.fontWeightMedium,
        fontSize : "1rem"
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
        margin: "0 10px"
    },
    tabSelected: {
        background: "#ff3750",
        color: "#fff"
    }
}))

const Description = ({type, data, id, isUserEmployer, buttonVisibility}) => {
    const classes = useStyles();
    const [descButtonStatus, setDescButtonStatus] = React.useState({
        text: 'Read More',
        expanded : false 
    })
    const [companyButtonStatus, setCompanyButtonStatus] = React.useState({
        text: 'Read More',
        expanded: false
    }) 
    const [selectionButtonStatus,setSelectionButtonStatus] = React.useState({
        text: 'Read More',
        expanded: false
    })
    const [isLiked,setIsLiked] = React.useState(data.is_liked);
    const [isApplied, setIsApplied] = React.useState(type==="job" ? data.is_applied : null);
    const [tab, setTab] = React.useState(0)

    let authToken=1;
    if (localStorage.getItem('key')) {
        authToken = localStorage.getItem('key');
    }

    // Handlers
    const handleReadMoreAbout = () => {
        if(!descButtonStatus.expanded){
            setDescButtonStatus({
                text: "Read Less",
                expanded: true
            })
        } else {
            setDescButtonStatus({
                text: "Read More",
                expanded: false
            })
        }
    }
    const handleReadMoreSelection = () => {
        if (!selectionButtonStatus.expanded) {
            setSelectionButtonStatus({
                text: "Read Less",
                expanded: true
            })
        } else {
            setSelectionButtonStatus({
                text: "Read More",
                expanded: false
            })
        }
    }
    const handleReadMoreCompany = () => {
        if (!companyButtonStatus.expanded) {
            setCompanyButtonStatus({
                text: "Read Less",
                expanded: true
            })
        } else {
            setCompanyButtonStatus({
                text: "Read More",
                expanded: false
            })
        }
    }
    const handleApply = async (e) => {
        e.preventDefault()
        const body = { 
            status: "Pending"
        }
        if(type==="job"){
            setIsApplied(true)

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
            console.log(data);    
        }
    }
    const handleLikefunc = (value, is_liked) => async() => {
        const body={
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
        const response = await fetch(`${baseUrl}/job/${type==="job"?"":"scholarship/"}${value}/${id}/?=`, requestOptions);
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
        if (d[0]==='a'){
            date = '1'+ d.slice(1)
            return date
        }
        else return d;
    }

    return(
        <Grid container className={classes.mainContainer}>
            <Grid item container direction="row">
                <DescriptionHeader data={data} type={type}/>
                <Grid item container xs={1} alignItems="flex-end" justify="space-evenly" direction="column">
                    {isLiked ?   
                        <IconButton onClick={handleLikefunc("unlike", false)} >
                            <Favorite style={{color: "red"}} fontSize="small" />
                        </IconButton>
                    :  
                        <IconButton onClick={handleLikefunc("like", true)}>
                            <FavoriteBorder style={{color: "black"}} fontSize="small" />
                        </IconButton>
                    }
                    <IconButton>
                        <ShareOutlined fontSize='small' />
                    </IconButton>
                </Grid>
            </Grid>

            {/* Apply */}
            {isUserEmployer ?
                type!=="job" ?
                null
                :
                    !buttonVisibility ?
                    null
                    :
                <Grid container item justify="space-between" className={classes.subContainer}>
                    <Grid item>
                        <Button className={clsx(classes.tabButton,{
                            [classes.tabSelected]: tab===0
                        })} onClick={handleTab(0)}>
                            <Typography style={{fontSize:"14px"}}>
                                Details
                            </Typography>
                        </Button>
                        <Button 
                            className={clsx(classes.tabButton,{
                                [classes.tabSelected]: tab===1
                            })} 
                            onClick={handleTab(1)}>
                            <Typography style={{fontSize:"14px"}}>
                                View Applicantions
                            </Typography>
                        </Button>
                    </Grid>
                </Grid>
            :
                <Grid container item justify="space-between" className={classes.subContainer}>
                    <Grid item>
                        {type==="scholarship" ?
                                <a href={data.apply_url}>
                                    <Button variant="outlined" className={classes.tabButton}>
                                        Apply Link
                                    </Button>
                                </a>
                            :
                                data.is_apply_here ?
                                    type==="job" && isApplied ? 
                                        <Button disabled className={classes.disabledButton}>
                                            Applied
                                        </Button>
                                    :
                                        <Button variant="outlined" className={classes.tabButton} onClick={handleApply}>
                                            Apply
                                        </Button>
                                : 
                                    <a href={data.apply_url}>
                                        <Button variant="outlined" className={classes.tabButton}>
                                            Apply Link
                                        </Button>
                                    </a>
                        }
                    </Grid>
                </Grid>
            }


            {/* Container */}
            {tab===1 ? 
                <Grid item container direction="column" className={classes.applicationViewContainer}>
                    <ApplicantsComponent id={id}/>
                </Grid>
            :
                <>
                    <Grid item container direction="column" className={classes.aboutContainer}>
                        <Grid item container>
                            <Typography variant="h6" className={classes.paddingClass}>
                                {type==="job" ?
                                    "Job Description"
                                :
                                    "Description"
                                }
                            </Typography>
                        </Grid>
                        <Grid item container direction="column">
                            {
                                data.description.length > 200 ? ( 
                                    <>
                                    <Grid xs={12} item className={classes.paddingClass}>
                                        <Typography variant="body2">
                                            {data.description.substring(0, 200)}<span id="dots" style={
                                                descButtonStatus.expanded ? { display: "inline" } : { display: "none" }
                                            } >...</span><span style={
                                                descButtonStatus.expanded ? { display: "inline" } : { display: "none" }
                                            } > {data.description.substring(201,data.description.length-1)} </span>
                                        </Typography>
                                    </Grid>
                                    <Grid xs={12} item className={classes.buttonContainer}>
                                        <button className={classes.btn} onClick={handleReadMoreAbout}>
                                                <span>{descButtonStatus.text}</span>  { 
                                                    descButtonStatus.expanded ? (
                                                        <ExpandLess />
                                                    ) : (
                                                        <ExpandMore />
                                                    )
                                                }
                                        </button>
                                    </Grid>    
                                    </>
                                ) : (
                                    <>
                                        <Grid xs={12} item className={classes.paddingClass}>
                                            <Typography variant="body2">
                                                {data.description}  
                                            </Typography>
                                        </Grid>
                                    </>
                                )
                            }
                        </Grid>
                    </Grid>

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
                                    !data.degrees.length? null :
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
                            { (!data.tags) ?
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
                                            {data.accepted_locations.map((location,idx) => {
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
                            <div className={classes.aboutContainer}>
                                <Grid item container>
                                    <Typography variant="h6" className={classes.paddingClass}>
                                        Selection Process
                                    </Typography>
                                </Grid>
                                <Grid item container direction="column">
                                    {
                                        data.selection_process.length > 200 ? (
                                            <>
                                                <Grid xs={12} item className={classes.paddingClass}>
                                                    <Typography variant="body2">
                                                        {data.selection_process.substring(0, 200)}<span id="dots" style={
                                                            selectionButtonStatus.expanded ? { display: "inline" } : { display: "none" }
                                                        } >...</span><span style={
                                                            selectionButtonStatus.expanded ? { display: "inline" } : { display: "none" }
                                                        } > {data.selection_process.substring(201, data.description.length - 1)} </span>
                                                    </Typography>
                                                </Grid>
                                                <Grid xs={12} item className={classes.buttonContainer}>
                                                    <button className={classes.btn} onClick={handleReadMoreSelection}>
                                                        <span>{selectionButtonStatus.text}</span>  {
                                                            selectionButtonStatus.expanded ? (
                                                                <ExpandLess />
                                                            ) : (
                                                                    <ExpandMore />
                                                                )
                                                        }
                                                    </button>
                                                </Grid>
                                            </>
                                        ) : (
                                                <>
                                                <Grid xs={12} item className={classes.paddingClass}>
                                                    <Typography variant="body2">
                                                        {data.selection_process}  
                                                    </Typography>
                                                </Grid>
                                                </>
                                            )
                                    }
                                </Grid>
                            </div>
                            
                        </Grid>
                    </Grid>

                    <Grid item container direction="column" className={classes.aboutContainer}>
                        <Grid item container>
                            <Typography variant="h6" className={classes.paddingClass}>
                                About Company
                            </Typography>
                        </Grid>
                        <Grid item container direction="column">
                            {
                                data.company.description.length > 200 ? (
                                    <>
                                        <Grid xs={12} item className={classes.paddingClass}>
                                            <Typography variant="body2">
                                                {data.company.description.substring(0, 200)}<span id="dots" style={
                                                    companyButtonStatus.expanded ? { display: "inline" } : { display: "none" }
                                                } >...</span><span style={
                                                    companyButtonStatus.expanded ? { display: "inline" } : { display: "none" }
                                                } > {data.company.description.substring(201, data.description.length - 1)} </span>
                                            </Typography>
                                            {companyButtonStatus.expand ?
                                                <Link to={`/company/${data.company.id}`}>
                                                    <Button>Go to Descripton</Button>
                                                </Link>
                                            :
                                                null
                                            }
                                        </Grid>
                                        <Grid xs={12} item className={classes.buttonContainer}>
                                            <button className={classes.btn} onClick={handleReadMoreCompany}>
                                                <span>{companyButtonStatus.text}</span>  {
                                                    companyButtonStatus.expanded ? (
                                                        <ExpandLess />
                                                    ) : (
                                                            <ExpandMore />
                                                        )
                                                }
                                            </button>
                                        </Grid>
                                    </>
                                ) : (
                                        <>
                                            <Grid xs={12} item className={classes.paddingClass}>
                                                <Typography variant="body2">
                                                    {data.company.description}  
                                                </Typography>
                                                <Link to={`/home/company/${data.company.id}`}>
                                                    <Button>Go to Descripton</Button>
                                                </Link>
                                            </Grid>
                                        </>
                                    )
                            }
                        </Grid>
                    </Grid>
                </>
            }
        </Grid>
    )
}

export default Description;