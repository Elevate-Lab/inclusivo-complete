import React from "react";
import {baseUrl} from '../../urlConstants'
import {
    makeStyles,
    Grid,
    Typography
} from '@material-ui/core'
import RainbowLoader from "../Loaders/RainbowLoader/RainbowLoader";

const useStyles = makeStyles(() => ({
    mainContainer: {
        maxWidth: "1100px",
        margin : "0px auto",
        alignItems : "center"
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
          },
          params: {
              company_id: props.match.params.cid
          }
        };

        const response = await fetch(`${baseUrl}/company/story/get/`, requestOptions)
                                .then(response => response.json());
        console.log(response);
        if(response.status === "error"){
            setError(response.message)
        } else {
            var reqStory =  response.data.find((singleData) => {
                                return singleData.id.toString() === props.match.params.sid;
                            });
            console.log(reqStory);
            setStoryData(reqStory);
        }
        setLoading(false);
    }

    React.useEffect(() => {
        getStoryData()
    },[])

    React.useEffect(() => {
        console.log(storyData)
    },[storyData])

    return (
        <>
        {loading ? 
            <RainbowLoader />
        :
            error ? 
                <Grid>{error}</Grid>
            : 
                <Grid container className={classes.mainContainer} alignItems="center" justify="space-evenly">    
                    <Grid item>
                        <img src={storyData.photo_url} className={classes.image} />
                    </Grid>  
                    <Grid item className={classes.textContainer} align="center">
                        <Typography variant="h3" style={{ fontWeight: '600' }} className={classes.title} >
                            {storyData.name}
                        </Typography>
                        <Typography variant="h5" className={classes.title}>
                            Story by: {storyData.company.name}
                        </Typography>
                        <Typography variant="subtitle1" className={classes.title}>
                            {storyData.description}
                        </Typography>
                    </Grid>
                </Grid>

        } 
    </>
    )

}

export default StoryDescription;
