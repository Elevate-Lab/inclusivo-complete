import React from "react";
import axios from "axios";
import { Grid, Button } from "@material-ui/core";
import Card from "../Listing/Card";
import { baseUrl } from "../../urlConstants";
import CommonCardSkeleton from "../Loaders/CommonCardSkeleton";
import { useStyles } from "./Styles";

const CompanyJobs = () => {
  const classes = useStyles();
  const [data, setData] = React.useState([]);
  const [error, setError] = React.useState(false);
  const [loading, setLoading] = React.useState(true);
  const [hasMore, setHasMore] = React.useState(false);
  const [isMoreClicked, setMoreClicked] = React.useState(false);
  const handleViewMore = () => {
    setMoreClicked(!isMoreClicked);
  };
  React.useEffect(() => {
    async function getJobs() {
      let token = localStorage.getItem("key");
      let company_id = "";
      await axios({
        method: "GET",
        headers: {
          Authorization: `token ${token}`,
        },
        url: `${baseUrl}/user/get/0/`,
      })
        .then((res) => {
          company_id = res.data.data.employer.company.id;
        })
        .catch((err) => {
          setLoading(false);
          setError(true);
          return err;
        });
      if (!error) {
        await axios({
          method: "get",
          url: `${baseUrl}/job/company/${company_id}`,
          headers: {
            Authorization: `token ${token}`,
          },
        })
          .then((res) => {
            setLoading(false);
            if (res.data.status === "OK") {
              setData(res.data.data.reverse());
              if (res.data.data.length > 3) {
                setHasMore(true);
              }
            } else {
              setError(true);
            }
          })
          .catch((err) => {
            setLoading(false);
            setError(true);
            return err;
          });
      }
    }
    getJobs();
  }, []);

  return (
    <>
      {loading && (
        <CommonCardSkeleton type="job" />
      )} {(!loading && !error && data.length === 0) && (
        <div>No Jobs Found</div>
      ) }{ (!loading && error) && (
        <div>Something went Wrong! Try Again</div>
      ) }{(!loading && !error && data.length > 0) && (
        data.slice(0, 3).map((data, idx) => (
          <Grid key={idx} item className={classes.jobCard}>
            <Card type="jobs" data={data} />
          </Grid>
        ))
      )}
      {!loading &&
        !error &&
        hasMore &&
        isMoreClicked &&
        data.slice(3, data.length).map((data, idx) => (
          <Grid key={idx} item className={classes.jobCard}>
            <Card type="jobs" data={data} />
          </Grid>
        ))}
      {hasMore && (
        <Grid item xs={12} className={classes.buttonContainer}>
          {!isMoreClicked ? (
            <Button
              style={{
                color: "red",
                background: "rgba(255, 234, 236, 1)",
                width: "100px",
              }}
              onClick={handleViewMore}
            >
              View All
            </Button>
          ) : (
            <Button
              style={{
                color: "red",
                background: "rgba(255, 234, 236, 1)",
                width: "100px",
              }}
              onClick={handleViewMore}
            >
              Close
            </Button>
          )}
        </Grid>
      )}
    </>
  );
};

export default CompanyJobs;
