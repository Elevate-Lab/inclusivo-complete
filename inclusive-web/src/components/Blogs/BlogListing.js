import React from "react";
import {baseUrl} from '../../urlConstants';
import {
    makeStyles,
    Grid,
    GridList,
    GridListTile,
    GridListTileBar,
    IconButton 
} from '@material-ui/core';
import InfoIcon from '@material-ui/icons/Info';
import Loader from '../../assets/loader/loader';
import {Link} from 'react-router-dom'

const useStyles = makeStyles((theme) => ({
    root: {
        display: 'flex',
        flexWrap: 'wrap',
        justifyContent: 'space-around',
        overflow: 'hidden',
        backgroundColor: theme.palette.background.paper,
    },
    mainContainer: {
        maxWidth: "1000px",
        width: "auto",
        margin : "0px auto",
        alignItems : "center"

    },
    image: {
        width: 250,
        '&:hover':{
            boxShadow: "0px 0px 10px -2px rgba(0, 0, 0, 0.15)",
            zoom: "1.2",
            transitionDelay: "5000ms",
        },
    },
    tile: {
        // margin: "10px 40px",
        [theme.breakpoints.up('xs')]: {
            width: "100% !important"
        },
        [theme.breakpoints.between('sm', 'md')]: {
            width: "48% !important"
        },
        [theme.breakpoints.up('md')]: {
            width: "33.33% !important"
        },
        height: "auto"
    },
    icon: {
        color: 'rgba(255, 255, 255, 0.54)',
      },
}))

function BlogListing(props) {
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
        };

        const response = await fetch(`${baseUrl}/upskill/blog/get/`, requestOptions)
                                .then(response => response.json());
        console.log(response);
        if(response.status === "error"){
            setError(response.message)
        } else {
            setStoryData(response.data);
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
            <Grid>
                <Loader loading={loading} />
            </Grid>  
        :
            error ? 
                <Grid>{error}</Grid>
            : 
            <div className={classes.root}>
                <GridList className={classes.mainContainer} cols={3} cellHeight={300} justify="space-evenly">
                    {storyData.map((story) => {
                        return (
                            <GridListTile key={story.id}  cols={1} className={classes.tile}>    
                                <img className={classes.image} src={story.photo_url} style={{width: "100%"}} alt={story.name} />
                                <GridListTileBar
                                    title={story.name}
                                    subtitle={<span>{"by " + story.author_credits}</span>}
                                    style={{width: "100%"}}
                                    actionIcon={
                                        <Link to={`/home/blog/${story.id}`} key={story.id}>
                                            <IconButton className={classes.icon}>
                                                <InfoIcon />
                                            </IconButton>
                                        </Link>
                                    }
                                />
                            </GridListTile>
                        )
                    })}
                </GridList>
            </div>

        } 
        </>
    )
}

export default BlogListing;