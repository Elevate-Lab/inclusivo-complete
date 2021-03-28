import React from 'react'
import Description from './Description'
import {baseUrl} from '../../urlConstants'
import {
    Grid
} from '@material-ui/core'
import Loader from '../../assets/loader/loader';

function ScholarshipDescription(props) {

    const [loading, setLoading] = React.useState(true)
    const [isUserEmployer, setIsUserEmployer] = React.useState(false)
    const [scholarshipData, setScholarshipData] = React.useState({})
    const [error, setError] = React.useState('')

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

    const checkUserEmployer = async () => {
        const response = await fetch(`${baseUrl}/user/get/0`, requestOptions);
        const data = await response.json();
        setIsUserEmployer(prevState => {
            return !data.data ? prevState : data.data.employer ? true : false;
        })
    }

    const getScholarshipData = async () => {
        const response = await fetch(`${baseUrl}/job/scholarship/get/${props.match.params.id}`, requestOptions);
        const data = await response.json();
        if(data.status === "error"){
            setError(data.message)
        } else {
            setScholarshipData(data.data)
        }
        setLoading(false)
    }

    React.useEffect(() => {
        checkUserEmployer()
        getScholarshipData()
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
                        data={scholarshipData}
                        type="scholarship"
                        id={props.match.params.id}
                        isUserEmployer={isUserEmployer}
                    />           
            } 
        </>
    )
}

export default ScholarshipDescription
