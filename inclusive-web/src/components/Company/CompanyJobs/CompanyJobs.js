import React from 'react'
import {
    Grid,
    Typography,
} from '@material-ui/core'
import { baseUrl } from '../../../urlConstants'
import { Link } from 'react-router-dom'
import CommonCard from '../../CompanyDescription/TabComponents/CommonCard'
import CommonCardSkeleton from '../../Loaders/CommonCardSkeleton'

function CompanyJobs(props) {
    // console.log(props)
    const id=props.match.params.id
    // let id=18

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

        const response = await fetch(`${baseUrl}/job/company/${id}`, requestOptions)
        const res = await response.json()
        console.log(res.data)
        if (res.status === "error") {
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
                <CommonCardSkeleton type="job" />
                :
                error ?
                    <Grid container justify="center">
                        <Typography variant="caption">
                            {error}
                        </Typography>
                    </Grid>
                    :
                    <Grid container>
                        {data.reverse().map((job) => {
                            return (
                                <Link to={`/home/job/${job.id}`} key={job.id} style={{ width: "100%" }}>
                                    <CommonCard data={job} type="job" />
                                </Link>
                            )
                        })}
                    </Grid>
            }
        </>
    )
}

export default CompanyJobs
