import React from "react";
import {
  Grid,
  Typography,
  IconButton,
  Dialog,
  DialogActions,
  DialogTitle,
  Button,
} from "@material-ui/core";
import { DeleteForever } from "@material-ui/icons";
import { useStyles } from "./Styles";
import { baseUrl } from "../../urlConstants";
import Loader from "../../assets/loader/loader";

const Card = ({ data }) => {
  const [canEdit, setCanEdit] = React.useState(false);
  const [dialogOpen, setDialog] = React.useState(false);
  const [isDeleting, setIsDeleting] = React.useState(false);

  const handleDialogClose = () => {
    setDialog(false);
  };
  const handleDialogOpen = () => {
    setDialog(true);
  };
  const deleteInitiative = () => {
    let key = localStorage.getItem("key");

    const requestOptions = {
      method: "DELETE",
      headers: {
        Authorization: `token ${key}`,
      },
    };

    setIsDeleting(true);
    fetch(`${baseUrl}/company/initiatives/delete/${data.id}`, requestOptions)
      .then((res) => res.json())
      .then((res) => {
        console.log(res);
        setIsDeleting(false);
      })
      .catch((err) => {
        console.log(err);
        setIsDeleting(false);
      });
  };
  React.useLayoutEffect(() => {
    let key = localStorage.getItem("key");

    const requestOptions = {
      method: "get",
      headers: {
        Authorization: `token ${key}`,
      },
    };

    fetch(`${baseUrl}/user/get/0/`, requestOptions)
      .then((res) => res.json())
      .then((res) => {
        if (res.data.candidate) {
          setCanEdit(false);
        } else if (res.data.employer) {
          if (res.data.employer.id === data.company) {
            setCanEdit(true);
            handleDialogClose();
          } else {
            setCanEdit(false);
            handleDialogClose();
          }
        }
      })
      .catch((err) => console.log(err));
  }, [isDeleting]);
  const classes = useStyles();

  return (
    !data.removed && (
      <Grid
        key={data.id}
        className={classes.cardItem}
        container
        justify="center"
        alignItems="flex-start"
        direction="column"
        item
      >
        <Grid item container justify="space-between" alignItems="center">
          <Typography variant="h6" gutterBottom>
            {data.name}
          </Typography>
          {canEdit && (
            <IconButton
              size="small"
              color="secondary"
              onClick={handleDialogOpen}
            >
              <DeleteForever fontSize="small" />
            </IconButton>
          )}
        </Grid>
        <Grid item className={classes.descContainer}>
          {data.description}
        </Grid>
        <Dialog
          open={dialogOpen}
          onClose={handleDialogClose}
          aria-labelledby="form-dialog-title"
        >
          <DialogTitle id="form-dialog-title">
            Are You Sure You Want to delete this initaitive ?
          </DialogTitle>
          {isDeleting ? (
            <div className={classes.sweetLoading}>
              <Loader loading={isDeleting} />
            </div>
          ) : (
            <DialogActions>
              <Button onClick={handleDialogClose} color="primary">
                Cancel
              </Button>
              <Button onClick={deleteInitiative} color="primary">
                Yes Sure!
              </Button>
            </DialogActions>
          )}
        </Dialog>
      </Grid>
    )
  );
};

export default Card;
