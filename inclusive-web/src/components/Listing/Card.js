import React from "react";
import { useStyles } from "./Styles";
import {
  Grid,
  ButtonBase,
  Typography,
  IconButton,
  Icon,
} from "@material-ui/core";
import clsx from "clsx";
import { LocationOn, ShareOutlined,BookmarkBorderOutlined,BookmarkBorder} from "@material-ui/icons";
import Moment from "react-moment";
import { Link } from "react-router-dom";
import Tags from "./Tags";
import companyImage from "../../assets/Frame43.png";
import { CheckCircleOutline } from "@material-ui/icons";
import toBeReviewed from "../../assets/Icons/ToBeReviewed.png";
import inProcess from "../../assets/Icons/InProcess.png";
import shortlisted from "../../assets/Icons/ShortListed.png";
import rejected from "../../assets/Icons/Rejected.png";

const Card = ({ data, type, status }) => {
  const classes = useStyles();
  const [isLiked,setIsLiked] = React.useState(false);
  const toFilter = (d) => {
    let date = "";
    if (d[0] === "a") {
      date = "1" + d.slice(1);
      return date;
    } else return d;
  };
  React.useEffect(() => {
    setIsLiked(data.is_liked);
  },[isLiked,data.is_liked]);

  const getStatus = (statusType) => {
    var statusData = {};
    const iconStyle = {
      color: "#FFFFFF",
    };
    switch (statusType) {
      case "Pending":
        statusData = {
          type: "tobereviewed",
          value: "To Be Reviewed",
          icon: (
            <Icon style={iconStyle}>
              <img src={toBeReviewed} alt="tobereviewed" />
            </Icon>
          ),
          color: "rgba(253, 151, 15, 1)",
        };
      case "Process":
        statusData = {
          type: "inProcess",
          value: "In Process",
          icon: (
            <Icon style={iconStyle}>
              <img src={inProcess} alt="inprocess" />
            </Icon>
          ),
          color: "#06B0C5",
        };
      case "Shortlisted":
        statusData = {
          type: "shortlisted",
          value: "Shortlisted",
          icon: (
            <Icon style={iconStyle}>
              <img src={shortlisted} alt="shortlisted" />
            </Icon>
          ),
          color: "#A64A97",
        };
      case "Rejected":
        statusData = {
          type: "rejected",
          value: "Rejected",
          icon: (
            <Icon style={iconStyle}>
              <img src={rejected} alt="rejected" />
            </Icon>
          ),
          color: "#E73E3A",
        };
      case "Selected":
        statusData = {
          type: "accepted",
          value: "Accepted",
          icon: (
            <Icon style={iconStyle}>
              <CheckCircleOutline />
            </Icon>
          ),
          color: "#4AA64E",
        };
    }
    return (
      <Grid
        item
        container
        justify="center"
        style={{
          maxWidth: "250px",
          margin: "5px auto 0px",
          padding: "5px",
          background: `${statusData.color}`,
          borderRadius: "5px",
        }}
      >
        {statusData.icon}&nbsp;{" "}
        <Typography style={{ color: "#fff", fontWeight: "600" }}>
          {statusData.value}
        </Typography>
      </Grid>
    );
  };

  return (
    <Link
      to={
        type === `jobs`
          ? `/home/job/${data.id}`
          : `/home/scholarship/${data.id}`
      }
      className={classes.link}
    >
      <Grid
        container
        xs={12}
        item
        justify="center"
        className={classes.itemContainer}
        key={data.id}
      >
        <Grid item container direction="row">
          <Grid xs={10} item container wrap="nowrap" alignItems="center">
            
              <Grid item style={{ marginLeft: "4px" }}>
                <ButtonBase>
                  <img
                    src={
                      data.company
                        ? data.company.logo_url
                        : companyImage
                    }
                    alt="company logo"
                    className={
                      type === "jobs" ? classes.image2 : classes.image1
                    }
                  />
                </ButtonBase>
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
                  className={clsx(classes.title, classes.ellipsis)}
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
              {type === "jobs" && (
                <Grid item>
                  <Typography variant="caption">
                    {data.job_type} • Vacancy - {data.vacancies} •{" "}
                    <Moment filter={toFilter} fromNow>
                      {data.posted_on}
                    </Moment>
                  </Typography>
                </Grid>
              )}
              {type === "scholarships" && (
                <Grid>
                  <Typography variant="subtitle2">
                    <Moment filter={toFilter} fromNow>
                      {data.posted_on}
                    </Moment>
                  </Typography>
                </Grid>
              )}
            </Grid>
          </Grid>
          <Grid xs={2} item alignItems="flex-end" container direction="column">   
            {isLiked ?
              <IconButton disableRipple className={classes.btn} style={{ marginBottom: "6px" }} >
                <BookmarkBorder style={{ color: "red" }} fontSize="small" />
              </IconButton>
              :
              <IconButton disableRipple className={classes.btn} style={{ marginBottom: "6px" }} >
                <BookmarkBorderOutlined fontSize="small" />
              </IconButton>
            }
            <IconButton disableRipple className={classes.btn}>
              <ShareOutlined fontSize='small' />
            </IconButton>
          </Grid>
        </Grid>
        <Grid item xs={12} className={classes.description}>
        <Typography varaint="subtitle2" className={clsx(classes.jobRole, classes.ellipsis)}>
            {type === "jobs" ? data.job_role : data.description}
          </Typography>
        </Grid>
        {status && getStatus(status)}
        <Grid className={classes.tag} item container xs={12}>
          {!status && data.tags.length > 0 && (
            <Tags data={data} />
          )}
        </Grid>
      </Grid>
    </Link>
  );
};

export default Card;
