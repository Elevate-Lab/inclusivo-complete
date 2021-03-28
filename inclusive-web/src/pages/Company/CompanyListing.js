import {
  Box,
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  fade,
  FormControl,
  FormControlLabel,
  FormLabel,
  Grid,
  IconButton,
  InputAdornment,
  InputBase,
  makeStyles,
  MenuItem,
  Radio,
  RadioGroup,
  TextField,
  useMediaQuery,
  withStyles,
} from "@material-ui/core";
import { FilterList, Search } from "@material-ui/icons";
import { Autocomplete, Pagination } from "@material-ui/lab";
import React, { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Link } from "react-router-dom";
import { getCompanies } from "../../actions/getCompany/companyListing.actions";
import Company from "../../components/Company/Company";

const useStyles = makeStyles((theme) => ({
  mainContainer: {
    paddingLeft: "6px",
    paddingTop: "5px",
    width: "100%",
  },
  containerrow: {
    margin: theme.spacing(2),
    paddingTop: theme.spacing(2),
  },
  mainContainerMobile: {
    width: "100%",
    paddingTop: "5px",
  },
  filterList: {
    color: "#FF3750",
    fontSize: "1.1rem",
  },
  filterIcon: {
    color: "#FF3750",
    fontSize: "2.3rem",
  },
  dialogForm: {
    margin: theme.spacing(2),
    minWidth: 200,
  },
  dialogFormContainer: {
    display: "flex",
    direction: "column",
  },
  listContainer : {
    paddingTop: theme.spacing(2),
    margin: theme.spacing(2),
    background: "#fafafa"
  }
}));

const BootstrapInput = withStyles((theme) => ({
  root: {
    "label + &": {
      marginTop: theme.spacing(3),
    },
    width: "100%",
  },
  input: {
    borderRadius: 10,
    position: "relative",
    backgroundColor: theme.palette.common.white,
    border: "1px solid #ced4da",
    fontSize: 16,
    width: "100%",
    padding: "10px 12px",
    transition: theme.transitions.create(["border-color", "box-shadow"]),
    // Use the system font instead of the default Roboto font.
    fontFamily: [
      "-apple-system",
      "BlinkMacSystemFont",
      '"Segoe UI"',
      "Roboto",
      '"Helvetica Neue"',
      "Arial",
      "sans-serif",
      '"Apple Color Emoji"',
      '"Segoe UI Emoji"',
      '"Segoe UI Symbol"',
    ].join(","),
    "&:focus": {
      boxShadow: `${fade("#FF3750", 0.25)} 0 0 0 0.2rem`,
      borderColor: "#FF3750",
    },
  },
}))(InputBase);

export const CompanyListing = () => {
  const pageSize = 4;
  const matches = useMediaQuery("(max-width:950px)");

  const companies = useSelector((state) => state.companyList);
  const companyList = companies.data.companies;

  const [openFilters, setOpenFilters] = React.useState(false);
  const [searchField, setSearchField] = React.useState("title");
  const [sortOrder, setSortOrder] = React.useState("asc");
  const [searchType, setSearchType] = React.useState("");
  const [searchText, setSearchText] = React.useState("");
  //const [locationSelect,setLocationSelect] = React.useState(false);
  //const [cityData, setCityData] = React.useState([]);

  const [currentPage, setCurrentPage] = React.useState(1);
  const [totalPages, setTotalPages] = React.useState(10);
  // const [totalJobs,setTotalJobs] = React.useState(0);
  // const [jobsData,setJobsData] = React.useState([]);

  const handleOpenFilters = () => {
    setOpenFilters(true);
  };

  const handleCloseFilters = () => {
    setOpenFilters(false);
  };

  const handleOrder = (e) => {
    setSortOrder(e.target.value);
  };

  const handleSearchField = (e) => {
    setSearchField(e.target.value);
  };

  const handleSearchText = (e) => {
    setSearchText(e.target.value);
  };
  const handlePageChange = (event, value) => {
    setCurrentPage(value);
  };

  const onSearch = (e) => {
    e.preventDefault();
    setCurrentPage(1);
    const filters = {
      filters: {
        sortField: "id",
        sortOrder: sortOrder,
        search: [
          {
            searchText: searchText,
            searchField: searchField,
            searchType: searchType,
          },
        ],
        pageNumber: currentPage - 1,
        pageSize: pageSize,
      },
    };
    dispatch(getCompanies(filters));
  };

  const dispatch = useDispatch();
  const classes = useStyles();
  const isSideBarVanish = useMediaQuery("(max-width:827px)");

  useEffect(async () => {
    const filters = {
      filters: {
        sortField: "id",
        sortOrder: "asc",
        search: [],
        pageNumber: 0,
        pageSize: pageSize,
      },
    };
    await dispatch(getCompanies(filters));
    console.log(companies);
    console.log(companies.data.total_count / pageSize);
  }, []);
  useEffect(() => {
    console.log(companies);
    console.log(companyList);
    setTotalPages(Math.ceil(companies.data.total_count / pageSize));
  }, [companies]);

  React.useEffect(() => {
    const filters = {
      filters: {
        sortField: "id",
        sortOrder: sortOrder,
        search: [],
        pageNumber: currentPage - 1,
        pageSize: pageSize,
      },
    };
    dispatch(getCompanies(filters));
  }, [currentPage, sortOrder]);
  return (
    <>
      {/* Dialogs */}
      <Dialog open={openFilters} onClose={handleCloseFilters}>
        <DialogTitle>Apply Filters</DialogTitle>
        <DialogContent>
          <form className={classes.dialogFormContainer}>
            <Grid item>
              <TextField
                defaultValue="title"
                className={classes.dialogForm}
                label="Search in Field"
                name="searchField"
                select
                onChange={handleSearchField}
              >
                {[
                  { name: "title", label: "Title" },
                  { label: "Name", name: "name" },
                  { label: "Id", name: "id" },
                ].map((option, idx) => (
                  <MenuItem key={idx} value={option.name}>
                    {option.label}
                  </MenuItem>
                ))}
              </TextField>
            </Grid>
            <Grid item>
              <FormControl component="fieldset">
                <FormLabel component="legend">Order</FormLabel>
                <RadioGroup
                  aria-label="gender"
                  name="order"
                  value={sortOrder}
                  onChange={handleOrder}
                >
                  <FormControlLabel
                    value="asc"
                    control={<Radio />}
                    label="Ascending"
                  />
                  <FormControlLabel
                    value="desc"
                    control={<Radio />}
                    label="Descending"
                  />
                </RadioGroup>
              </FormControl>
            </Grid>
          </form>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseFilters} color="primary">
            Cancel
          </Button>
          <Button onClick={handleCloseFilters} color="primary">
            Ok
          </Button>
        </DialogActions>
      </Dialog>

      <Grid
        container
        className={
          isSideBarVanish
            ? classes.mainContainerMobile
            : classes.mainContainer + " container-column"
        }
      >
        <Box
          style={{
            display: "flex",
            height: "4rem",
            background: "#F0F0F0",
            borderRadius: "10px",
            maxWidth: "100%",
            width: "100%",
            flexDirection:"row",
            justifyContent: "space-around",
            margin: "10px 16px"
          }}
        >
          <div style={{ width: "75%",alignItems: "center",display:"flex" }}>
          <form onSubmit={onSearch} style={{width: "100%"}}>
            <Autocomplete
              freeSolo
              id="free-solo-2-demo"
              disableClearable
              options={["temp1", "temp2", "temp3"]}
              renderInput={(params) => (
                <BootstrapInput
                  {...params}
                  label="Search input"
                  onChange={handleSearchText}
                  ref={params.InputProps.ref}
                  inputProps={params.inputProps}
                  endAdornment={
                    <InputAdornment position="end">
                      <IconButton type="submit" style={{ padding: "0" }}>
                        <Search />
                      </IconButton>
                    </InputAdornment>
                  }
                  //inputProps={{ ...params.InputProps, type: "search" }}
                />
              )}
            />
          </form>
          </div>
          <div
          //className={classes.filterList}
          >
            <Button
              className={classes.filterList}
              style={{ width: "100%" }}
              disableRipple
              disableFocusRipple
              onClick={handleOpenFilters}
            >
              <FilterList className={classes.filterIcon} />
              {!matches ? <>&nbsp; Filter</> : null}
            </Button>
          </div>
        </Box>
        <Grid
          container
          className={classes.listContainer}
          style={{ marginTop: "1rem" }}
        >
          {companies.pageRequest
            ? companyList.map((company, index) => {
                return (
                  <Grid item sm={6} xs={12} md={4} lg={3}>
                    <Link to={`/home/company/${company.id}`}>
                      <Box
                        style={{
                          background: "#fff",
                          margin: "0 1rem 1rem 1rem",
                          borderRadius: "5px",
                          padding: "1.5rem",
                        }}
                      >
                        <Company company={company} />
                      </Box>
                    </Link>
                  </Grid>
                );
              })
            : "Loading"}
        </Grid>
        <Box
          className="container-column"
          style={{ margin: "2rem 0", width: "100%" }}
        >
          <Pagination
            count={totalPages}
            page={currentPage}
            onChange={handlePageChange}
          />
        </Box>
      </Grid>
    </>
  );
};
