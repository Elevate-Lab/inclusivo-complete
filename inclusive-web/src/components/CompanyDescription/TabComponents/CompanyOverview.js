import React from 'react'
import clsx from 'clsx'
import {
    makeStyles,
    Grid,
    Typography,
    Button
} from '@material-ui/core'
import {
    ExpandLess,
    ExpandMore
} from '@material-ui/icons'
import SectionTitle from './SectionTitle'
import InitiativesOverview from './InititiativesOverview'
import JobsOverview from './JobsOverview'
import ScholarshipsOverview from './ScholarshipsOverview'
import StoriesOverview from './StoriesOverview'
import ViewAll from './ViewAll'

const useStyles = makeStyles(theme => ({
    subContainer: {
        marginTop: "12px",
        background: "#fafafa",
        padding: "10px 10px",
        borderRadius: "5px"
    },
    container: {
        background: "#fff",
        borderRadius: "5px",
    },
    paddingClass: {
        padding: "10px 10px",
    },
    descriptionTitle: {
        fontWeight: "600",
        color: "#3a3a3a",
        letterSpacing: "0.4px"
    },
    mt: {
        marginTop: "8px"
    },
    buttonContainer: {
        marginTop: "4px",
        padding: "10px 20px",
        boxShadow: "0px -1px 10px -5px rgba(0, 0, 0, 0.15)",
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
}))

function CompanyOverview({company, tabsData, company_id, handleChange}) {
    const [descButtonStatus, setDescButtonStatus] = React.useState({
        text: 'Read More',
        expanded : false 
    })

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

    const classes = useStyles()
    return (
        <>
            <Grid item container alignItems="center" className={classes.subContainer}>
                <Grid item container direction="column" className={classes.container}>
                    <Grid item container>
                        <Typography className={clsx(classes.paddingClass, classes.descriptionTitle)}>
                            Description
                        </Typography>
                    </Grid>
                    <Grid item container direction="column">
                        {
                            company.company.description.length > 200 ? ( 
                                <>
                                <Grid xs={12} item className={classes.paddingClass}>
                                    <Typography variant="body2">
                                        {company.company.description.substring(0, 200)}<span id="dots" style={
                                            descButtonStatus.expanded ? { display: "inline" } : { display: "none" }
                                        } >...</span><span style={
                                            descButtonStatus.expanded ? { display: "inline" } : { display: "none" }
                                        } > {company.company.description.substring(201,company.company.description.length-1)} </span>
                                    </Typography>
                                </Grid>
                                <Grid xs={12} item className={classes.buttonContainer}>
                                    <button className={classes.btn} onClick={handleReadMoreAbout}>
                                            <span style={{color: "#3a3a3a"}}>{descButtonStatus.text}</span>  { 
                                                descButtonStatus.expanded ? (
                                                    <ExpandLess style={{color: "#7a7a7a"}}/>
                                                ) : (
                                                    <ExpandMore style={{color: "#7a7a7a"}}/>
                                                )
                                            }
                                    </button>
                                </Grid>    
                                </>
                            ) : (
                                <>
                                    <Grid xs={12} item className={classes.paddingClass}>
                                        <Typography variant="body2">
                                            {company.company.description}  
                                        </Typography>
                                    </Grid>
                                </>
                            )
                        }
                    </Grid> 
                </Grid>
            </Grid>
            <Grid item container direction="column" className={classes.subContainer}>
                <SectionTitle 
                    backgroundColor={tabsData[1].backgroundColor} 
                    image={tabsData[1].image} 
                    name={tabsData[1].tab}
                />
                <InitiativesOverview company_id={company_id} overview={true}/>
                <ViewAll handleChange={handleChange} value={1}/>
            </Grid>
            <Grid item container direction="column" className={classes.subContainer}>
                <SectionTitle 
                    backgroundColor={tabsData[2].backgroundColor} 
                    image={tabsData[2].image} 
                    name={tabsData[2].tab}
                />
                <JobsOverview company_id={company_id} overview={true}/>
                <ViewAll handleChange={handleChange} value={2}/>
            </Grid>
            <Grid item container direction="column" className={classes.subContainer}>
                <SectionTitle 
                    backgroundColor={tabsData[3].backgroundColor} 
                    image={tabsData[3].image} 
                    name={tabsData[3].tab}
                />
                <ScholarshipsOverview company_id={company_id} overview={true}/>
                <ViewAll handleChange={handleChange} value={3}/>
            </Grid>
            <Grid item container direction="column" className={classes.subContainer}>
                <SectionTitle 
                    backgroundColor={tabsData[4].backgroundColor} 
                    image={tabsData[4].image} 
                    name={tabsData[4].tab}
                />
                <StoriesOverview company_id={company_id} overview={true}/> 
                <ViewAll handleChange={handleChange} value={4}/>
            </Grid>
        </>
    )
}

export default CompanyOverview
