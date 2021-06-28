import React from "react";
import { baseUrl } from '../../urlConstants'
import {
    makeStyles,
    Grid,
    Typography,
    Chip
} from '@material-ui/core'
import {
    VideocamRounded
} from '@material-ui/icons'
import clsx from 'clsx'
import {Link} from 'react-router-dom'
import RainbowLoader from "../Loaders/RainbowLoader/RainbowLoader";

const useStyles = makeStyles(() => ({
    bContainer:{
        width: "100%",
        backgroundColor: "#fafafa",
        paddingTop: "12px"
    },
    mainContainer: {
        maxWidth: "1100px",
        margin: "0px auto",
    },
    tileContainer:{
        padding: "10px",
        minWidth: "200px"
    },
    tile: {
        backgroundColor: "#fff",
        // width: "calc(100% - 20px)",
        marginBottom: "12px",
        padding: "20px",
        borderRadius: "5px",
        boxShadow: "0px 0px 63px -41px rgba(0, 0, 0, 0.25)"
    },
    tileLink:{
        color: "inherit",
        width: "100%"
    },
    chipContainer:{
        backgroundColor: "#fff",
        // width: "calc(100% - 20px)",
        marginBottom: "12px",
        padding: "0px 20px 10px 20px",
        borderRadius: "5px",
        boxShadow: "0px 0px 63px -41px rgba(0, 0, 0, 0.25)"
    },
    chip: {
        borderRadius: "5px",
        marginRight: "12px",
        marginTop: "10px",
        fontWeight :"600",
        background: "#e6e6e6"
    },
    ellipsis: {
        lineHeight: "1.5em",
        maxHeight: "3em",
        overflow: "hidden",
        textOverflow: "ellipsis",
        width: "100%",
        display: "-webkit-box",
        "-webkit-box-orient": "vertical",
        "-webkit-line-clamp": 2,
        lineClamp: 2,
    },
    imgContainer: {
        width: "150px",
        minWidth: "150px",
    },
    img:{
        maxWidth: "150px",
        maxHeight: "150px",
        borderRadius: "5px"
    },
    detailsContainer: {
        width: "calc(100% - 150px)",
        paddingLeft: "10px"
    },
}))


function StoryDescription(props) {
    const classes = useStyles()
    const [loading, setLoading] = React.useState(true)
    const [error, setError] = React.useState('')
    const [storyData, setStoryData] = React.useState({})
    const [loadingRelatedVideos, setLoadingRelatedVideos] = React.useState(true)
    const [errorRelatedVideos, setErrorRelatedVideos] = React.useState('')
    const [relatedVideos, setRelatedVideos] = React.useState({})

    const getStoryData = async () => {
        let key = localStorage.getItem("key");
        const requestOptions = {
            method: "GET",
            headers: {
                Authorization: `token ${key}`,
            }
        };
        const response = await fetch(`${baseUrl}/upskill/video/get/${props.match.params.id}/individual`, requestOptions)
            .then(response => response.json());
        console.log(response);
        if (response.status === "error") {
            setError(response.message)
        } else {
            setStoryData(response.data);
        }

        setLoading(false);
    }

    const getRelatedVideos = async () => {
        let key = localStorage.getItem("key");
        const requestOptions = {
            method: "GET",
            headers: {
                Authorization: `token ${key}`,
            },
        };

        const response = await fetch(`${baseUrl}/upskill/video/get/`, requestOptions)
            .then(response => response.json());
        console.log(response);
        if (response.status === "error") {
            setErrorRelatedVideos(response.message)
        } else {
            setRelatedVideos(response.data);
        }
        setLoadingRelatedVideos(false);
    }

    const extractId = (str) => {
        return str.substring(str.indexOf('=')+1)
    }

    React.useEffect(() => {
        getStoryData()
        getRelatedVideos()
        window.scrollTo(0,0)
    }, [props.match])

    React.useEffect(() => {
        console.log(storyData)
    }, [storyData])

    React.useEffect(() => {
        console.log(relatedVideos)
    }, [relatedVideos])

    return (
        <>
            {loading ?
                <RainbowLoader />
                :
                error ?
                    <Grid>{error}</Grid>
                    :
                    <Grid container justify='center' className={classes.bContainer}>
                        <Grid container className={classes.mainContainer} justify="space-evenly">
                            <Grid item container xs={12} className={classes.tile} style={{margin: "12px 8px 12px 8px", padding: "8px"}}>
                                <iframe width="100%" height="500px" title="youtube video" frameBorder="none" src={`https://www.youtube.com/embed/${extractId(storyData.video_link)}`}>
                                </iframe>
                            </Grid>
                            <Grid item style={{flex: "2 2 calc(100% - 390px)"}} className={classes.tileContainer}>
                                <Grid item container className={classes.tile}>
                                    <Grid item container>
                                        <Typography variant="h4" style={{fontWeight: "600"}}>
                                            {storyData.name}
                                        </Typography>
                                    </Grid>
                                </Grid>
                                {storyData.tags.length && 
                                <Grid item container className={classes.chipContainer}>
                                    {storyData.tags.map((tag) => {
                                        return(
                                            <Chip
                                                key={tag.id}
                                                label = {tag.name}
                                                className={classes.chip}
                                            />
                                        )
                                    })}
                                </Grid>}
                                <Grid item container className={clsx(classes.tile,classes.description)}>
                                    <Typography variant="body2">
                                        {storyData.description}
                                    </Typography>
                                </Grid>
                            </Grid>
                            <Grid item className={clsx(classes.rightElements,classes.tileContainer)} style={{flex: "1 1 390px"}}>
                                <Grid item container className={classes.tile}>
                                    <Typography variant="body1" style={{fontWeight: "600"}}>
                                        Videos section helps you keep updated with skills and technologies required to get in most of the companies.
                                    </Typography>
                                </Grid>
                                <Typography variant="body1" style={{fontWeight: "600", margin: "8px 0"}} >
                                    Recent Videos
                                </Typography>
                                {loadingRelatedVideos ?
                                    <Typography variant="body1" style={{marginBottom: "8px"}} >
                                        Loading...
                                    </Typography>
                                    :
                                    errorRelatedVideos ?
                                        <Typography variant="body1" style={{marginBottom: "8px"}} >
                                            {errorRelatedVideos}
                                        </Typography>
                                        :
                                        relatedVideos.map((blog)=> {
                                            return (
                                                <Grid item container key={blog.id} className={classes.tile} style={{padding: "10px"}}>
                                                    <Link to={`/home/video/${blog.id}`} className={classes.tileLink}>
                                                        <Grid item container xs={12}>
                                                            <Grid item container justify={'center'} alignItems={'center'} className={classes.imgContainer}>
                                                                <img className={classes.img} src={`https://img.youtube.com/vi/${extractId(blog.video_link)}/0.jpg`} style={{width: "100%"}} alt={blog.name} />
                                                            </Grid>
                                                            <Grid item className={classes.detailsContainer}>
                                                                <Grid item container>
                                                                    <Typography className={classes.ellipsis} variant="subtitle1" style={{fontWeight: "600"}}>{blog.name}</Typography>
                                                                </Grid>
                                                                <Grid item container>
                                                                    <VideocamRounded style={{fontSize: "18px", marginRight: "4px"}}/>
                                                                    <Typography variant="body2" style={{fontWeight: "600"}}>by {blog.author_credits}</Typography>
                                                                </Grid>
                                                            </Grid>
                                                        </Grid>
                                                    </Link>
                                                </Grid>
                                            )
                                        })
                                }
                            </Grid>
                        </Grid>
                    </Grid>
            }
        </>
    )

}

export default StoryDescription;
