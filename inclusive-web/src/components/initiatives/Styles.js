import { makeStyles } from "@material-ui/core";

export const useStyles = makeStyles((theme) => ({
  mainContainer: {
    margin: "auto 10px",
    maxWidth: "1100px",
    fontFamily: "Roboto,Helvetica,Arial,sans-serif",
  },
  cardItem: {
    margin: theme.spacing(1),
    padding: "2%",
    background: "#FFFFFF",
    borderRadius: "5px",
    border: "1px solid rgba(223, 223, 223, 1)",
    [theme.breakpoints.down("sm")]: {
      flexBasis: "45%",
    },
    [theme.breakpoints.down("xs")]: {
      flexBasis: "95%",
    },
    [theme.breakpoints.between("md", "lg")]: {
      flexBasis: "30%",
    },
  },
  dialogForm: {
    margin: theme.spacing(1),
  },
  sweetLoading: {
    margin: "20px auto",
  },
  descContainer: {
    fontSize: "0.9rem",
  },
  addButton: {
    margin: theme.spacing(1),
  },
}));
