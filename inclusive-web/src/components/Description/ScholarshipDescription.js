import React from 'react'
import Description from './Description'
import {baseUrl} from '../../urlConstants'
import {
    Grid
} from '@material-ui/core'
import RainbowLoader from '../Loaders/RainbowLoader/RainbowLoader';

function ScholarshipDescription(props) {

    const [loading, setLoading] = React.useState(true)
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
        getScholarshipData()
    },[])

    return (
        <>
            {loading ? 
                <RainbowLoader />
            :
                error ? 
                    <Grid>{error}</Grid>
                :
                    <Description 
                        data={scholarshipData}
                        type="scholarship"
                        id={props.match.params.id}
                    />           
            } 
        </>
    )
}

export default ScholarshipDescription
