import React from 'react'
import {
    Grid,
    Typography,
} from '@material-ui/core'
import {baseUrl} from '../../../urlConstants'
import {Link} from 'react-router-dom'
import CommonCard from '../../CompanyDescription/TabComponents/CommonCard'
import CommonCardSkeleton from '../../Loaders/CommonCardSkeleton'

function CompanyScholarships(props) {
    const id = props.match.params.id

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
    
        const response = await fetch(`${baseUrl}/job/scholarship/company/${id}`, requestOptions)
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
                    data.length===0 ?
                    <Typography>No Scholarships</Typography>
                    :
                    <Grid container>
                        {data.reverse().map((scholarship) => {
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

export default CompanyScholarships
