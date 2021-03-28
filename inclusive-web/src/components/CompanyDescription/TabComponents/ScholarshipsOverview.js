import React from 'react'
import {
    Grid,
    Typography,
} from '@material-ui/core'
import {baseUrl} from '../../../urlConstants'
import {Link} from 'react-router-dom'
import CommonCard from './CommonCard'
import CommonCardSkeleton from '../../Loaders/CommonCardSkeleton'

function ScholarshipsOverview({company_id, overview}) {

    const [loading, setLoading] = React.useState(true)
    const [error, setError] = React.useState('')
    const [data, setData] = React.useState({})

    const getData = async () => {
        let key = localStorage.getItem("key");
        const requestOptions = {
          method: "get",
          headers: {
            Authorization: `token ${key}`,
          },
        };
    
        const response = await fetch(`${baseUrl}/job/scholarship/company/${company_id}`, requestOptions)
        const res = await response.json()
        console.log(res.data)
        if(res.status === "error"){
            setError(res.message)
        } else {
            setData(res.data)
        }
        setLoading(false)
    }

    React.useEffect(() => {
        getData()
    }, [])

    return (
        <>
            {loading ? 
                <CommonCardSkeleton type="scholarship" />
            :
                error ? 
                    <Grid container justify="center">
                        <Typography variant="caption">
                            {error}
                        </Typography>
                    </Grid>
                :   
                    <Grid container>
                        {data.reverse().slice(0, overview? (data.length > 2) ? 2 : data.length+1 : data.length+1).map((scholarship) => {
                            return (
                                <Link to={`/home/scholarship/${scholarship.id}`} key={scholarship.id} style={{width: "100%"}}>
                                    <CommonCard data={scholarship} type="scholarship" />
                                </Link>
                                )
                            })}
                    </Grid>
            }            
        </>
    )
}

export default ScholarshipsOverview
