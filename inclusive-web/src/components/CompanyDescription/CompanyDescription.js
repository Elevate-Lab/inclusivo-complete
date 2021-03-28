import React from 'react'
import clsx from 'clsx'
import {
    makeStyles,
    Grid,
    Typography,
    Tab,
    Tabs,
    Button
} from '@material-ui/core'
import {baseUrl} from '../../urlConstants'
import DescriptionHeader from '../Description/DescriptionHeader'
import {tabsData} from './CompanyTabsData' 
import CompanyOverview from './TabComponents/CompanyOverview'
import InititiativesOverview from './TabComponents/InititiativesOverview'
import JobsOverview from './TabComponents/JobsOverview'
import ScholarshipsOverview from './TabComponents/ScholarshipsOverview'
import StoriesOverview from './TabComponents/StoriesOverview'

const useStyles = makeStyles(theme => ({
    mainContainer: { 
        maxWidth: "1100px",
        margin : "0px auto",
        "& .MuiTabs-indicator": {
            display: "none"
        },
    },
    subContainer: {
        marginTop: "12px",
        background: "#fafafa",
        padding: "10px 10px",
        borderRadius: "5px"
    },
    selected: {
        border: "1px solid #ff3750",
    },
    tabContainer: {
        alignItems: "center",
        borderRadius: "5px",
        width: "100%"
    },
    tab: {
        background: "#fff",
        margin: "0 4px",
        borderRadius: "5px",
        padding: "6px",
        flexWrap: "nowrap",
        cursor: "pointer",
        maxWidth: "140px"
    },
    tabImage: {
        width: "28px",
        height: "28px",
        borderRadius: "3px"
    }
}))

function CompanyDescription({match}) {
    const classes = useStyles()
    const [loading, setLoading] = React.useState(true)
    const [company, setCompany] = React.useState({})
    const [value, setValue] = React.useState(0)
    const [error, setError] = React.useState('')

    const getCompanyDetails = async () => {
        let authToken=1;
        if (localStorage.getItem('key')) {
            authToken = localStorage.getItem('key');
        }
        const requestOptions = {
            method: 'GET',
            headers: { 
                'Content-Type': 'application/json', 
                'Access-Control-Allow-Origin': '*',
                'Authorization': `token ${authToken}`,
            },
        };
        const response = await fetch(`${baseUrl}/company/company_details/${match.params.id}`, requestOptions);
        const res = await response.json();
        console.log(res);
        if(res.status==="error"){
            setError(res.message)
        } else{
            setCompany(res.data)
        }
        setLoading(false)
    }

    React.useEffect(()=> {
        getCompanyDetails()
    }, [])

    // handlers

    const handleChange = (tabValue) => () => {
        setValue(tabValue)
        console.log(tabValue)
    }

    const onClick = () => {
        console.log("clicked")
    }

    return (
        <>
            {loading ?
                <Typography>
                    loading...
                </Typography>
            :    
                error ?
                    <Typography>
                        {error}
                    </Typography>
                :
                    <Grid container className={classes.mainContainer}>
                        <Grid item container>
                            <DescriptionHeader data={company} type="company"/>
                        </Grid>
                        <Grid item container className={classes.subContainer} style={{padding: "8px 6px"}}>
                            <Tabs
                                value={value}
                                variant="scrollable"
                                textColor="primary"
                                aria-label="scrollable force tabs example"
                                className={classes.tabContainer}
                            >
                                {tabsData.map((tabData)=>{
                                    return (
                                        <Grid
                                            key={tabData.value}
                                            item
                                            container
                                            direction="row"
                                            alignItems="center"
                                            className={clsx(classes.tab,{
                                                [classes.selected]: tabData.value === value
                                            })}
                                            onClick={handleChange(tabData.value)}
                                        >
                                            <Grid 
                                                item 
                                                container
                                                justify="center"
                                                alignItems="center"
                                                className={classes.tabImage} 
                                                style={{background: tabData.backgroundColor}}
                                            >
                                                <img src={tabData.image} style={{width: "15px"}}/>
                                            </Grid>   
                                            <Typography 
                                                variant="caption" 
                                                style={{margin: "0 6px",
                                                    fontWeight: "600",
                                                    fontSize: "14px",
                                                    color: "#3a3a3a",
                                                    letterSpacing: "0.4px"}}
                                            >
                                                {tabData.tab}
                                            </Typography>
                                        </Grid>
                                    )
                                })}
                            </Tabs>                 
                        </Grid>
                        {
                            value===0 &&
                            <CompanyOverview 
                                company={company}
                                tabsData={tabsData}
                                company_id={match.params.id}
                                handleChange={handleChange}
                            />
                        }
                        <Grid item container direction="column" className={classes.subContainer}>
                            {
                                value===1 && 
                                <InititiativesOverview company_id={match.params.id} overview={false}/>
                            }
                            {
                                value===2 &&
                                <JobsOverview company_id={match.params.id} overview={false} />
                            }
                            {
                                value===3 &&
                                <ScholarshipsOverview company_id={match.params.id} overview={false} />
                            }
                            {
                                value===4 &&
                                <StoriesOverview company_id={match.params.id} overview={false} />
                            }
                        </Grid>
                    </Grid>
            }
        </>
    )
}

export default CompanyDescription
