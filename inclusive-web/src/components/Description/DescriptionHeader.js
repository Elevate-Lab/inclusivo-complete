import React from 'react'
import Moment from 'react-moment'
import {
    makeStyles,
    Grid,
    ButtonBase,
    Typography
} from '@material-ui/core'
import { toFilter } from '../../helpers/methods'

const useStyles = makeStyles(() => ({
    image1: {
        maxWidth: "100px",
        width: "70px",
        margin: "0 16px 0 4px"
    }
}))

function DescriptionHeader({data, type}) {
    const classes = useStyles()

    return (
        <Grid item container xs={11} wrap="nowrap" alignItems="center">
            <Grid item>
                <ButtonBase disableRipple disableTouchRipple>
                    <img src="/images/gojek.png" className={classes.image1} />
                </ButtonBase>
            </Grid>
            <Grid item container direction="column">
                <Typography variant="h6" style={{ fontWeight: '600' }}>
                    {type==="company" ?
                        data.company.name
                    :
                        data.title
                    }
                </Typography>
                {type!=="job" ?
                    null
                :
                    <Typography variant="subtitle2">
                        {data.company.name}
                        <span style={{margin: "0 4px"}}>•</span>  
                        {data.job_type}
                    </Typography>
                }
                {type!=="scholarship" ?
                    null
                :
                    <Typography variant="subtitle2">
                        {data.company.name}
                    </Typography>
                }
                {type!=="company" ?
                    <Typography variant="subtitle2">
                        <Moment filter={toFilter} fromNow>{data.posted_on}</Moment>
                    </Typography>
                :
                    <Typography variant="subtitle2">
                        {data.company.title}
                    </Typography>
                }
            </Grid>
        </Grid>
    )
}

export default DescriptionHeader
