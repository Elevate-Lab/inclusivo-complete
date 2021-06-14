import React from 'react'
import {
    makeStyles,
    Grid,
    Typography,
    Button
} from '@material-ui/core'
import { baseUrl } from '../../../urlConstants'
import clsx from 'clsx'
import axios from 'axios'
import StoryCardSkeleton from '../../Loaders/StoryCardSkeletion'
import { Link } from 'react-router-dom'

const useStyles = makeStyles(theme => ({
    storyCards: {
        width: "calc(33.33% - 12px)",
        background: "#fff",
        margin: "18px 20px",
        marginBottom: "0",
        borderRadius: "5px",
        height: "340px",
        maxWidth: "280px",
        '&:hover': {
            boxShadow: "0px 0px 10px -2px rgba(0, 0, 0, 0.15)"
        },
        [theme.breakpoints.down('sm')]: {
            width: "calc(50% - 12px)",
        },
        [theme.breakpoints.down('xs')]: {
            width: "100%"
        },
    },
    imgContainer: {
        height: "152px",
        overflow: "hidden",
        background: "#eaeaea"
    },
    img: {
        width: "100%",
        borderTopLeftRadius: "5px",
        borderTopRightRadius: "5px"
    },
    details: {
        padding: "8px",
        minHeight: "142px"
    },
    storyName: {
        maxHeight: "56px",
        fontSize: "14px"
    },
    storyDesc: {
        // color: "#8a8a8a",
        fontSize: "13px",
        maxHeight: "86px",
    },
    btnText: {
        fontSize: "12px",
        fontWeight: 600,
        color: "#ff3750"
    },
    ellipsis: {
        display: "-webkit-box",
        "-webkit-box-orient": "vertical",
        "-webkit-line-clamp": 2,
        overflow: "hidden",
        textOverflow: "ellipsis",
    },
    ellipsis2: {
        display: "-webkit-box",
        "-webkit-box-orient": "vertical",
        "-webkit-line-clamp": 4,
        overflow: "hidden",
        textOverflow: "ellipsis",
    }
}))

function InititiativesOverview({ company_id, overview }) {
    const classes = useStyles()

    const [data, setData] = React.useState([])
    const [loading, setLoading] = React.useState(true)
    const [error, setError] = React.useState('')

    const getData = async () => {
        let key = localStorage.getItem("key");
        const requestOptions = {
            method: "get",
            headers: {
                Authorization: `token ${key}`,
            },
            params: {
                company_id: company_id
            }
        };

        const response = await axios.get(`${baseUrl}/company/story/get/`, requestOptions)
        console.log(response.data)
        if (response.data.status === "error") {
            setError(response.data.message)
        } else {
            setData(response.data.data)
        }
        setLoading(false)
    }

    React.useEffect(() => {
        getData()
    }, [])

    return (
        <>
            {loading ?
                <StoryCardSkeleton />
                :
                error ?
                    <Grid container justify="center">
                        <Typography variant="caption">
                            {error}
                        </Typography>
                    </Grid>
                    :
                    <Grid container justify="center">
                        {data.slice(0, overview ? (data.length > 3) ? 3 : data.length + 1 : data.length + 1).map(story => {
                            return (
                                <Grid item container direction="column" className={classes.storyCards}>
                                    <Grid className={classes.imgContainer}>
                                        <img src={story.photo_url} className={classes.img} />
                                    </Grid>
                                    <Grid className={classes.details}>
                                        <Typography variant="h6" className={clsx(classes.storyName, classes.ellipsis)} gutterBottom>
                                            {story.name}
                                        </Typography>
                                        <Typography variant="body2" className={clsx(classes.storyDesc, classes.ellipsis2)}>
                                            {story.description}
                                        </Typography>
                                    </Grid>
                                    <Grid style={{ padding: "6px 8px" }}>
                                        <Link to={`/home/company/${story.company.id}/story/${story.id}`} key={story.id}>
                                            <Button ariaLabel="Read More">
                                                <Typography className={classes.btnText}>
                                                    READ MORE
                                                </Typography>
                                            </Button>
                                        </Link>
                                    </Grid>
                                </Grid>
                            )
                        })}
                    </Grid>
            }
        </>
    )
}

export default InititiativesOverview
