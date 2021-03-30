import React from 'react'
import Description from './Description'
import {baseUrl} from '../../urlConstants'
import {
    Grid
} from '@material-ui/core'
import Loader from '../../assets/loader/loader';

function JobDescription(props) {

    const [loading, setLoading] = React.useState(true)
    const [jobData, setJobData] = React.useState({})
    const [error, setError] = React.useState('')
    const [currEmployerCompany, setCurrentEmployerCompany] = React.useState(-1)
    const [currentJobCompany, setCurrentJobCompany] = React.useState(-1)

    let authToken=1;
    if (localStorage.getItem('key')) {
        authToken = localStorage.getItem('key');
    }
    const requestOptions = {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'token '+authToken,
        },
    };

    const setEmployerId = async() => {
        const response = await fetch(`${baseUrl}/user/get/0`, requestOptions);
        const data = await response.json();
        setCurrentEmployerCompany(prevState => {
            return !data.data ? prevState : data.data.employer ? data.data.employer.company.id : prevState;
        })
    }

    const getJobData = async () => {
        const response = await fetch(`${baseUrl}/job/get/${props.match.params.id}`, requestOptions);
        const data = await response.json();
        if(data.status === "error"){
            setError(data.message)
        } else {
            console.log(data);
            setJobData({...data.data.job, is_applied: data.data.is_applied})
            setCurrentJobCompany(data.data.job.company.id)
        }
        setLoading(false)
    }

    React.useEffect(() => {
        setEmployerId()
        getJobData()
    },[])

    return (
        <>
            {loading ? 
                <Grid>
                    <Loader loading={loading} />
                </Grid>  
            :
                error ? 
                    <Grid>{error}</Grid>
                :
                    <Description 
                        data={jobData}
                        type="job"
                        id={props.match.params.id}
                        buttonVisibility={currEmployerCompany===currentJobCompany}
                    />           
            } 
        </>
    )
}

export default JobDescription
