import React from "react";
import {baseUrl} from '../../urlConstants'
import { useStyles } from "./Styles";
import {
  Grid,
  Typography,
  IconButton,
} from "@material-ui/core";
import clsx from "clsx";
import {
  LocationOn,
  ShareOutlined,
  BookmarkBorderOutlined,
  BookmarkBorder,
} from "@material-ui/icons"
import Moment from "react-moment"
import { Link } from "react-router-dom"
import Tags from "./Tags"
import companyPlaceholder from "../../assets/company_placeholder.png"
import scholarshipPlaceholder from '../../assets/scholarship_placeholder.jpg'
import Share from "../Share/Share"

const Card = ({ data, isApplied = false, type, status, tagsToShow }) => {
  console.log(isApplied)
  const classes = useStyles()
  const [isLiked, setIsLiked] = React.useState(false)
  const [shareDialogOpen, setShareDialogOpen] = React.useState(false)
  const [loading, setLoading] = React.useState(true)
  const [applicationStatus, setApplicationStatus] = React.useState("Loading")
  const [error, setError] = React.useState('')
 
  // Application Status
  let authToken;
  if (localStorage.getItem('key')) {
    authToken = localStorage.getItem('key');
  }
  const requestOptions = {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': 'token ' + authToken,
    },
  };

  React.useEffect(() => {
    if (isApplied) {
      getApplicationStatus();
    }
  }, [])

  const getApplicationStatus = async () => {
    console.log(authToken, data.id)
    const response = await fetch(`${baseUrl}/job/application/status/${data.id}`, requestOptions);
    const d = await response.json();
    if(d.status === "error"){
        setError(d.message)
    } else {
        setApplicationStatus(d.data.status)
        console.log(d)
    }
    setLoading(false)
}

  const handleShareDialogOpen = () => {
    setShareDialogOpen(true)
  }

  const handleShareDialogClose = () => {
    console.log("heelo")
    setShareDialogOpen(false)
  }

  const jobUrl = `https://www.inclusivo.netlify.app/home/job/${data.id}`
  const scholarshipUrl = `https://www.inclusivo.netlify.app/home/scholarship/${data.id}`
  const toFilter = (d) => {
    let date = "";
    if (d[0] === "a") {
      date = "1" + d.slice(1);
      return date;
    } else return d;
  };
  React.useEffect(() => {
    setIsLiked(data.is_liked);
  }, [isLiked, data.is_liked]);

  const getStatusAttr = (value) => {
    switch (value) {
      case 'Published':
        return {
          backgroundColor: "rgba(6,176,197,0.4)",
          color: "#3e3e3e"
        }
      case 'Expired':
        return {
          backgroundColor: "rgba(255,26,26,0.4)",
          color: "#3e3e3e"
        }
      case 'Draft':
        return {
          backgroundColor: "rgba(6,176,197,0.6)",
          color: "#3e3e3e"
        }
      case 'Disabled':
        return {
          backgroundColor: "#E6E6E6",
          color: "#3e3e3e"
        }
      case 'Hired':
        return {
          backgroundColor: "rgba(74,166,78,0.4)",
          color: "#3e3e3e"
        }
      default:
        return {}
    }
  }

  const statusEl = (value) => {
    console.log(value)
    let attr = getStatusAttr(value);
    return (
      <Grid item container justify='center' alignItems='center' className={classes.status} style={attr}>
        <Typography variant="subtitle2">Status: {value === 'Published' ? "Live" : value}</Typography>
      </Grid>
    )
  }

  return (
    <Grid
      container
      xs={12}
      item
      justify="center"
      className={classes.itemContainer}
      key={data.id}
    >
      <Grid item container direction="row">
        <Link
          to={
            type === `jobs`
              ? `/home/job/${data.id}`
              : `/home/scholarship/${data.id}`
          }
          className={classes.link}
        >
          <Grid item container xs={10} direction="row">
            <Grid item container wrap="nowrap" alignItems="center">
              <Grid item style={{ marginLeft: "4px" }}>
                <>
                  {data.company && (
                    <img
                      src={
                        data.company.logo_url.length > 0 ? data.company.logo_url : companyPlaceholder
                      }
                      alt="Company logo"
                      className={
                        type === "jobs" ? classes.image2 : classes.image1
                      }
                    />
                  )}
                  {
                    type === "scholarships" && !data.company && (
                      <img
                        src={
                          scholarshipPlaceholder
                        }
                        alt="Scholarship Logo"
                        className={
                          type === "jobs" ? classes.image2 : classes.image1
                        }
                      />
                    )
                  }
                </>
              </Grid>
              <Grid
                item
                container
                direction="column"
                justify="space-between"
                className={classes.detailsContainer}
              >
                <Grid item>
                  <Typography
                    variant="h6"
                    className={clsx(classes.title, classes.ellipsis2)}
                  >
                    {data.title}
                  </Typography>
                </Grid>
                <Grid item>
                  {type === "jobs" && (
                    <Typography variant="caption">
                      <Grid item container alignItems="center">
                        {data.accepted_locations.length > 0 && (
                          <>
                            <LocationOn className={classes.locationIcon} />
                            {data.accepted_locations.map((location, idx) => (
                              <span key={idx} style={{ marginRight: "5px" }}>
                                {location.name}
                              </span>
                            ))}
                          </>
                        )}
                      </Grid>
                    </Typography>
                  )}
                </Grid>
                <Grid item>
                  {type === "jobs" && (
                    <Typography variant="caption">
                      {data.job_type} • Vacancy - {data.vacancies} •{" "}
                      <Moment filter={toFilter} fromNow>
                        {data.posted_on}
                      </Moment>
                    </Typography>
                  )}
                </Grid>
                <Grid item>
                  {type === "scholarships" && (
                    <Typography variant="caption">
                      <Moment filter={toFilter} fromNow>
                        {data.posted_on}
                      </Moment>
                    </Typography>
                  )}
                </Grid>
              </Grid>
            </Grid>
          </Grid>
        </Link>
        <Grid xs={2} item alignItems="flex-end" container direction="column">
          {isLiked ? (
            <IconButton
              disableRipple
              className={classes.btn}
              style={{ marginBottom: "6px" }}
            >
              <BookmarkBorder style={{ color: "red" }} fontSize="small" />
            </IconButton>
          ) : (
            <IconButton
              disableRipple
              className={classes.btn}
              style={{ marginBottom: "6px" }}
            >
              <BookmarkBorderOutlined fontSize="small" />
            </IconButton>
          )}
          <IconButton disableRipple className={classes.btn} onClick={handleShareDialogOpen}>
            <ShareOutlined fontSize="small" />
          </IconButton>
        </Grid>
      </Grid>
      <Grid item xs={12} className={classes.description}>
        <Typography
          varaint="subtitle2"
          className={clsx(classes.jobRole, classes.ellipsis2)}
        >
          {type === "jobs" ? data.job_role : data.description}
        </Typography>
      </Grid>
      {type === "jobs" ?
        <Grid item container className={classes.statusContainer}>
          {statusEl(data.status)}
        </Grid>
        :
        null
      }
      <Grid className={classes.tag} item container xs={12}>
        {data.tags.length > 0 && status === null && <Tags data={data} />}
      </Grid>
      <Share open={shareDialogOpen} handleClose={handleShareDialogClose} url={type === "jobs" ? jobUrl : scholarshipUrl} />
    </Grid>
  );
};

export default Card;
