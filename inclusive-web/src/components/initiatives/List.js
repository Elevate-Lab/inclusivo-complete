import React from "react";
import { baseUrl } from "../../urlConstants";
import { useStyles } from "./Styles";
import {
  Grid,
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  TextField,
  Snackbar,
} from "@material-ui/core";
import Card from "./Card";
import Loader from "../../assets/loader/loader";
import { Alert } from "@material-ui/lab";

const InitiativesList = (props) => {
  const classes = useStyles();
  const [data, setData] = React.useState([]);
  const [dialogOpen, setDialog] = React.useState(false);
  const [initaitiveName, setInititaiveName] = React.useState("");
  const [desc, setDesc] = React.useState("");
  const [isloading, setIsLoading] = React.useState(false);
  const [isAdding, setIsAdding] = React.useState(false);
  const [isAdded, setIsAdded] = React.useState(false);
  const [isError, setIsError] = React.useState(false);
  const [errorMessage, setErrMessage] = React.useState("");

  const handleDialogClose = () => {
    setDialog(false);
  };
  const handleDialogOpen = () => {
    setDialog(true);
  };

  const handleCloseSnackBar = () => {
    setIsError(false);
  };

  const handleFormChange = (e) => {
    if (e.target.name === "name") {
      setInititaiveName(e.target.value);
    } else if (e.target.name === "description") {
      setDesc(e.target.value);
    }
  };

  const getData = () => {
    let id = 1;
    let key = localStorage.getItem("key");
    const requestOptions = {
      method: "get",
      headers: {
        Authorization: `token ${key}`,
      },
    };

    setIsLoading(true);

    fetch(`${baseUrl}/company/initiatives/get_by_company/${props.match.params.id}`, requestOptions)
      .then((res) => res.json())
      .then((res) => {
        if (res.status === "error") {
          setIsError(true);
          setErrMessage(res.message);
        } else {
          console.log(res.data)
          setIsError(false);
          setData(res.data);
        }
        setIsLoading(false);
      })
      .catch((err) => {
        console.log(err);
        setIsError(true);
        setIsLoading(false);
      });
  };

  const handleAddInitiatives = () => {
    let key = localStorage.getItem("key");
    let body = {
      name: initaitiveName,
      description: desc,
    };

    const requestOptions = {
      method: "post",
      headers: {
        Authorization: `token ${key}`,
        "Content-Type": "application/json",
      },
      body: JSON.stringify(body),
    };
    if (isAdded) {
      setIsAdded(!isAdded);
    }
    setIsAdding(true);

    fetch(`${baseUrl}/company/initiatives/add/`, requestOptions)
      .then((res) => res.json())
      .then((res) => {
        if (res.status === "error") {
          setIsError(true);
          setErrMessage(res.message);
        } else {
          setIsError(false);
          setIsAdded(true);
        }
        setIsAdding(false);
        handleDialogClose();
      });
  };

  React.useEffect(() => {
    getData();
  }, [isAdded]);

  return (
    <div className={classes.mainContainer}>
      <Button
        onClick={handleDialogOpen}
        variant="outlined"
        className={classes.addButton}
        ariaLabel="Add initiative"
      >
        Add initiative
      </Button>
      <Dialog
        open={dialogOpen}
        onClose={handleDialogClose}
        aria-labelledby="form-dialog-title"
      >
        <DialogTitle id="form-dialog-title">Add Initaitive</DialogTitle>
        {isAdding ? (
          <div className={classes.sweetLoading}>
            <Loader loading={isAdding} />
          </div>
        ) : (
          <DialogContent>
            <TextField
              autoFocus
              className={classes.dialogForm}
              variant="outlined"
              value={initaitiveName}
              name="name"
              id="name"
              label="Name"
              type="text"
              onChange={handleFormChange}
            />
            <TextField
              variant="outlined"
              className={classes.dialogForm}
              autoFocus
              value={desc}
              name="description"
              id="description"
              label="Description"
              type="text"
              onChange={handleFormChange}
            />
            <DialogActions>
              <Button
                variant="contained"
                size="small"
                color="secondary"
                onClick={handleDialogClose}
                ariaLabel="Close"
              >
                Close
              </Button>
              <Button
                variant="contained"
                size="small"
                color="secondary"
                onClick={handleAddInitiatives}
                ariaLabel="Add"
              >
                Add
              </Button>
            </DialogActions>
          </DialogContent>
        )}
      </Dialog>
      {isloading ? (
        <div className={classes.sweetLoading}>
          <Loader loading={isloading} />
        </div>
      ) : (
        <Grid container justify="space-between">
          {data.length > 0 && data.map((data) => <Card data={data} />)}
        </Grid>
      )}
      {!isloading && isError && (
        <Snackbar
          open={isError}
          autoHideDuration={6000}
          onClose={handleCloseSnackBar}
        >
          <Alert
            onClose={handleCloseSnackBar}
            severity="error"
            variant="filled"
          >
            {errorMessage}
          </Alert>
        </Snackbar>
      )}
    </div>
  );
};

export default InitiativesList;
