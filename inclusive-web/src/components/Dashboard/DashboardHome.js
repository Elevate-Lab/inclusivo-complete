import React from "react";
import { Grid, Typography, Card, CardContent } from "@material-ui/core";
import { useStyles } from "./Styles";
import { cardItems } from './CardItems';
import { useSelector } from 'react-redux';
import { employerCards } from './employerCards';

const Dashboard = () => {
  const classes = useStyles();
  const userStatusData = useSelector(state => state.userStatus);

  return (
    <Grid container className={classes.dashboardContainer}>
      <Grid item xs={12} className={classes.dashboardHeading}>
        <Typography
          variant="h1"
          gutterBottom
          className={classes.dashboardHeading2}
          style={{ fontSize: "2.4285714285714284rem" }}
        >
          Dashboard
        </Typography>
      </Grid>
      {
        !userStatusData.isEmployer && !userStatusData.loading && cardItems.map((items) => (
          <Grid item xs={12} container className={classes.appliedJobsContainer}>
            <Grid xs={12} item className={classes.cardContainer}>
              <Card className={classes.card}>
                <div
                  className={classes.cover}
                  style={{ background: `${items.backgroundColor}` }}
                >
                  {items.icon}
                </div>
                <div className={classes.details}>
                  <CardContent className={classes.content}>
                    <Typography style={{ fontWeight: "700" }}>
                      {items.title}
                    </Typography>
                  </CardContent>
                </div>
              </Card>
            </Grid>
            <Grid xs={12} item className={classes.jobsContainer}>
              {items.component && items.component}
            </Grid>
          </Grid>
        ))
      }
      {
        userStatusData.isEmployer && !userStatusData.loading && employerCards.map((items, idx) => (
          <Grid item xs={12} container className={classes.appliedJobsContainer} key={idx}>
            <Grid xs={12} item className={classes.cardContainer}>
              <Card className={classes.card}>
                <div
                  className={classes.cover}
                  style={{ background: `${items.backgroundColor}` }}
                >
                  {items.icon}
                </div>
                <div className={classes.details}>
                  <CardContent className={classes.content}>
                    <Typography style={{ fontWeight: "700" }}>
                      {items.title}
                    </Typography>
                  </CardContent>
                </div>
              </Card>
            </Grid>
            <Grid xs={12} item className={classes.jobsContainer}>
              {items.component && items.component}
            </Grid>
          </Grid>
        ))
      }
    </Grid>
  );
};

export default Dashboard;
