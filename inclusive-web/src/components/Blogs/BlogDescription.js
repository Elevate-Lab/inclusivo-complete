import React from "react";
import { baseUrl } from '../../urlConstants'
import {
    makeStyles,
    Grid,
    Typography
} from '@material-ui/core'
import Loader from '../../assets/loader/loader';

const useStyles = makeStyles(() => ({
    mainContainer: {
        maxWidth: "1100px",
        margin: "0px auto",
        alignItems: "center"
    },
    image: {
        maxWidth: "500px",
        width: "450px",
        borderRadius: "10px",
        margin: "20px"
    },
    title: {
        margin: "10px 0 20px"
    },
    item: {
        alignItems: "center"
    },
    textContainer: {
        width: "400px",
        margin: "50px 0"
    }
}))


function StoryDescription(props) {
    const classes = useStyles()
    const [loading, setLoading] = React.useState(true)
    const [error, setError] = React.useState('')
    const [storyData, setStoryData] = React.useState({})

    const getStoryData = async () => {
        let key = localStorage.getItem("key");
        const requestOptions = {
            method: "GET",
            headers: {
                Authorization: `token ${key}`,
            }
        };
        const response = await fetch(`${baseUrl}/upskill/blog/get/${props.match.params.id}/individual`, requestOptions)
            .then(response => response.json());
        console.log(response);
        if (response.status === "error") {
            setError(response.message)
        } else {
            setStoryData(response.data);
        }

        setLoading(false);
    }

    React.useEffect(() => {
        getStoryData()
    }, [])

    React.useEffect(() => {
        console.log(storyData)
    }, [storyData])

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
                    <>
                        <Grid container className={classes.mainContainer} alignItems="center" justify="space-evenly">
                            <Grid item>
                                <img src={storyData.photo_url} className={classes.image} alt={storyData.name} />
                            </Grid>
                            <Grid item className={classes.textContainer} align="center">
                                <Typography variant="h3" style={{ fontWeight: '600' }} className={classes.title} >
                                    {storyData.name}
                                </Typography>
                                <Typography variant="h5" className={classes.title}>
                                    Story by: {storyData.author_credits}
                                </Typography>
                            </Grid>
                        </Grid>
                        <Typography variant="subtitle1" className={classes.title}>
                            {storyData.description}
                        </Typography>
                    </>
            }
        </>
    )

}

export default StoryDescription;
