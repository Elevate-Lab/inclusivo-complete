import React from 'react'
import Moment from 'react-moment'
import {
    makeStyles,
    Grid,
    ButtonBase,
    Typography
} from '@material-ui/core'
import { toFilter } from '../../helpers/methods'
import companyPlaceholder from '../../assets/company_placeholder.png'

const useStyles = makeStyles(() => ({
    image1: {
        maxWidth: "100px",
        width: "70px",
        margin: "0 16px 0 4px"
    }
}))

function DescriptionHeader({ data, type }) {
    const classes = useStyles()

    return (
        <Grid item container wrap="nowrap" alignItems="center" style={{ flex: "1 1" }}>
            <Grid item>
                <ButtonBase disableRipple disableTouchRipple>
                    {data.company &&
                        <img
                            src={data.company.logo_url ? data.company.logo_url : companyPlaceholder}
                            className={classes.image1}
                            alt="Company logo"
                        />
                    }
                </ButtonBase>
            </Grid>
            <Grid item container direction="column">
                <Typography variant="h5" style={{ fontWeight: '600' }}>
                    {type === "company" ?
                        data.company.name
                        :
                        data.title
                    }
                </Typography>
                {type !== "job" ?
                    null
                    :
                    <Typography variant="subtitle2">
                        {data.company ? data.company.name : null}
                        <span style={{ margin: "0 4px" }}>•</span>
                        {data.job_type}
                    </Typography>
                }
                {type !== "scholarship" ?
                    null
                    :
                    <Typography variant="subtitle2">
                        {data.company ? data.company.name : null}
                    </Typography>
                }
                {type !== "company" ?
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
