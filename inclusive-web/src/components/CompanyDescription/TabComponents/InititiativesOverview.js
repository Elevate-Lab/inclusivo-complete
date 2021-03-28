import React from 'react'
import {
    makeStyles,
    Grid,
    Typography,
} from '@material-ui/core'
import {baseUrl} from '../../../urlConstants'
import clsx from 'clsx'
import InitiativeDescription from '../InitiativeDescription/InitiativeDecription'
import InitiativeCardSkeleton from '../../Loaders/InitiativeCardSkeleton'

const useStyles = makeStyles(theme => ({
    initiativeCards: {
        width: "calc(33.33% - 12px)",
        background: "#fff",
        margin: "12px 6px",
        marginBottom: "0",
        borderRadius: "2px",
        padding: "8px",
        minHeight: "108px",
        cursor: "pointer",
        '&:hover':{
            boxShadow: "0px 0px 10px -2px rgba(0, 0, 0, 0.15)"
        },
        [theme.breakpoints.down('sm')]: {
            width: "calc(50% - 12px)",
        },
        [theme.breakpoints.down('xs')]: {
            width: "100%"
        },
    },
    initiativeName:{
        maxHeight: "40px",
        fontWeight: "600",
        fontSize: "14px",
        color: "#3a3a3a",
        letterSpacing: "0.4px"
    },
    initiativeDesc: {
        // color: "#8a8a8a",
        fontSize: "13px",
        maxHeight: "40px",
    },
    ellipsis: {
        display: "-webkit-box",
        "-webkit-box-orient": "vertical",
        "-webkit-line-clamp": 2,
        overflow: "hidden",
        textOverflow: "ellipsis",
    }
}))

function InititiativesOverview({company_id, overview}) {
    const classes = useStyles()

    const [data, setData] = React.useState([])
    const [loading, setLoading] = React.useState(true)
    const [error, setError] = React.useState('')
    const [showList, setShowList] = React.useState(true)
    const [initiativeInfo, setInitiativeInfo] = React.useState({})

    const getData = async () => {
        let key = localStorage.getItem("key");
        const requestOptions = {
          method: "get",
          headers: {
            Authorization: `token ${key}`,
          },
        };
    
        const response = await fetch(`${baseUrl}/company/initiatives/get_by_company/${company_id}`, requestOptions)
        const res = await response.json()
        console.log(res)
        if(res.status === "error"){
            setError(res.message)
        } else {
            setData(res.data)
        }
        setLoading(false)
    }

    React.useEffect(() => {
        getData()
    },[])

    const handleShowList = (info) => () => {
        console.log("clicked")
        setInitiativeInfo(info)
        setShowList(Object.keys(info).length===0 ? true : false)
    }

    return (
        <>
            {loading ? 
                <InitiativeCardSkeleton />
            :
                error ? 
                    <Grid container justify="center">
                        <Typography variant="caption">
                            {error}
                        </Typography>
                    </Grid>
                :   
                    <>
                        {!showList &&
                            <InitiativeDescription
                                initiativeInfo={initiativeInfo}
                                handleShowList={handleShowList}
                                overview={overview}
                            />
                        }
                        {(showList || !overview) &&
                            <Grid container justify="center">
                                {!showList && <Grid style={{width: "95%", height: "3px", background:"#f1f1f1", borderRadius: "5px", marginTop: "12px"}}></Grid>}
                                {data.slice(0,overview? (data.length > 6) ? 6 : data.length + 1 : data.length + 1).map(initiative => {
                                    return (
                                        <Grid item container direction="column" className={classes.initiativeCards} onClick={handleShowList(initiative)}>
                                            <Grid>
                                                <Typography variant="body2" className={clsx(classes.initiativeName, classes.ellipsis)} gutterBottom>
                                                    {initiative.name}
                                                </Typography>
                                            </Grid>
                                            <Grid>
                                                <Typography variant="body2" className={clsx(classes.initiativeDesc, classes.ellipsis)}>
                                                    {initiative.description}
                                                </Typography>
                                            </Grid>
                                        </Grid>
                                    )
                                })}
                            </Grid>}
                    </>
            }
        </>
    )
}

export default InititiativesOverview
