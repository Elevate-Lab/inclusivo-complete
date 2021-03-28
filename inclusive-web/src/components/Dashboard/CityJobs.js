import React from 'react'
import {useStyles} from './Styles'
import {
    Grid,
    Typography
} from '@material-ui/core'
import pune from '../../assets/Cities/pune.png'
import mumbai from '../../assets/Cities/mumbai.png'
import banglore from '../../assets/Cities/banglore.png'
import chennai from '../../assets/Cities/chennai.png'
import gurgaon from '../../assets/Cities/gurgaon.png'
import delhi from '../../assets/Cities/delhi.png'

const cardData = [
    {
        name: "Banglore",
        img: banglore
    },
    {
        name: "Delhi",
        img: delhi
    },
    {
        name: "Mumbai",
        img: mumbai
    },
    {
        name: "Chennai",
        img: chennai
    },
    {
        name: "Pune",
        img: pune
    },
    {
        name: "Gurgaon",
        img: gurgaon
    },
]

function CityJobs() {
    const classes = useStyles()
    return (
        <>
            {cardData.map((data) => {
                return (
                    <Grid container alignItems="center" className={classes.cityCard}>
                        <Grid className={classes.cityCardImageContainer}>
                            <img src={data.img} alt={data.name} className={classes.cityCardImage}/>
                        </Grid>
                        <Grid className={classes.cityCardName}>
                            <Typography variant="h6" style={{fontSize: "14px"}}>
                                {data.name}
                            </Typography>
                        </Grid>
                    </Grid>
                )
            })}
        </>
    )
}

export default CityJobs
