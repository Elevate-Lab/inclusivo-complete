import React from "react";
import {
  Grid,
  TextField,
  Button,
  IconButton,
  FormControlLabel,
  Checkbox,
  FormGroup,
  withStyles,
  Slider,
  Box,
} from "@material-ui/core";
import { Search, Close } from "@material-ui/icons";
import { baseUrl } from "../../urlConstants";
import { useStyles } from "./Styles";
import CommonCardSkeleton from "../Loaders/CommonCardSkeleton";
import loadable from "@loadable/component";
import clsx from "clsx";
import filterIcon from "../../assets/Icons/filterIcon.svg";
import useForm from "../../customHooks/useForm";
import Controls from "../Form/Controls/Controls";
import { tagList } from "../Scholarships/TagList";
import { Autocomplete } from "@material-ui/lab";
import axios from "axios";
import Company from "../Company/Company";
import { Link } from "react-router-dom";
import CompanyListingSkeleton from "../Loaders/CompanyListingSkeleton";

const Card = loadable(() => import("./Card"));

const Listing = ({ type }) => {
  const classes = useStyles();
  const [openFilters, setOpenFilters] = React.useState(false);
  const [defaultSearchFilters, setDefaultSearchFilters] = React.useState({
    searchField: "title",
    searchText: "",
    searchType: "",
  });
  const [cityData, setCityData] = React.useState({});
  const [filtersSelected, setFiltersSelected] = React.useState({
    jobType: false,
    locations: false,
    tags: false,
    companyName: false,
    salary: false,
    experience: false,
  });
  const selectedFilters = {
    locations: [],
    job_type: "",
  };
  const validate = (values) => { };

  const [values, setValues, errors, setErrors, handleChange] = useForm(
    selectedFilters,
    false,
    validate
  );

  const [companyNameValues, setCompanyNameValues] = React.useState({
    name: "",
  });
  const [tagValues, setTagValues] = React.useState({
    name: "",
    id: null,
  });
  const [expValue, setExpValue] = React.useState([0, 0]);
  const [salaryValue, setSalaryValue] = React.useState([0, 0]);
  const [companyName, setCompanyName] = React.useState({});
  const [isError, setIsError] = React.useState("");
  const [isFetching, setIsFetching] = React.useState(false);
  const [fetchedData, setFetchedData] = React.useState([]);
  const [hasMoreData, sethasMoreData] = React.useState(false);
  const [currentPage, setCurrentPage] = React.useState(1);
  const [allfilters, setAllFilters] = React.useState([]);

  let authToken = "24aa5cfa5084e7716221e1737d2838ceb2f91aaf";
  if (localStorage.getItem("key")) {
    authToken = localStorage.getItem("key");
  }
  const requestOptions = {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      Authorization: "token " + authToken,
    },
  };

  const getCities = async () => {
    const response = await fetch(
      `${baseUrl}/user/location/dropdown/india`,
      requestOptions
    );
    const data = await response.json();
    // console.log(data);
    await setCityData(data.data);
  };

  const getCompanies = async () => {
    const response = await fetch(`${baseUrl}/company/dropdown`, requestOptions);
    const data = await response.json();
    // console.log(data);
    let array = [];
    data.data.forEach((company) => {
      let name = { name: company.name };
      array.push(name);
    });
    await setCompanyName(array);
    // await setCityData(data.data);
  };

  React.useEffect(() => {
    getCities();
    getCompanies();

    return () => {
      setCityData({});
      setCompanyName({});
    };
  }, []);

  React.useEffect(() => {
    if (currentPage === 1 && !hasMoreData) {
      submitFunction([defaultSearchFilters], currentPage);
    }
  }, []);

  React.useEffect(() => { }, [cityData, values, companyName, hasMoreData]);

  const handleOpenFilters = () => {
    setOpenFilters(true);
  };

  const handleCloseFilters = () => {
    setOpenFilters(false);
  };

  const handleChangeSelectedFilters = (event) => {
    setFiltersSelected({
      ...filtersSelected,
      [event.target.name]: event.target.checked,
    });
  };

  const handleExpChange = (event, newValue) => {
    setExpValue(newValue);
  };

  const handleSalaryChange = (event, newValue) => {
    setSalaryValue(newValue);
  };

  const handleCompanyNameValues = (event, newValue) => {
    setCompanyNameValues(newValue);
  };

  const handleTagValues = (event, newValue) => {
    setTagValues(newValue);
  };

  const handleViewMore = (e) => {
    e.preventDefault();
    setCurrentPage(currentPage + 1);
    submitFunction(allfilters, currentPage + 1);
  };

  const handleDefaultSearchField = (e) => {
    setDefaultSearchFilters({
      searchField: "title",
      searchText: e.target.value,
      searchType: "",
    });
  };

  const handleResetButton = (e) => {
    setValues(selectedFilters);
    setTagValues({
      name: "",
      id: null,
    });
    setSalaryValue([0, 0]);
    setExpValue([0, 0]);
    setCompanyNameValues({
      name: "",
    });
    setFiltersSelected({
      locations: false,
      tags: false,
      companyName: false,
      salary: false,
      experience: false,
      jobType: false,
    });
    setAllFilters(defaultSearchFilters);
    setCurrentPage(1);
  };

  const handleDefaultSubmit = (e) => {
    e.preventDefault();
    handleShowResultsSearch(e);
  };
  const handleShowResultsSearch = (e) => {
    e.preventDefault();
    // console.log(values);
    // console.log(companyNameValues);
    // console.log(salaryValue);
    // console.log(expValue);
    // console.log(tagValues);
    let filters = [];
    let filterObject = {};
    if (companyNameValues.name.length > 0 && filtersSelected.companyName) {
      filterObject = {
        searchField: "company_name",
        searchText: companyNameValues.name,
        searchType: "",
      };
      filters.push(filterObject);
    }
    if (values.job_type.length > 0) {
      filterObject = {
        searchField: "job_type",
        searchText: values.job_type,
        searchType: "",
      };
      filters.push(filterObject);
    }
    if (values.locations.length > 0) {
      let text = "";
      if (values.locations.length > 1) {
        for (let i = 1; i < values.locations.length; i++) {
          text =
            text +
            values.locations[i - 1].name +
            "#@#" +
            values.locations[i].name;
        }
      } else {
        text = text + values.locations[0];
      }
      filterObject = {
        searchField: "locations",
        searchText: text,
        searchType: "IN",
      };
      filters.push(filterObject);
    }
    if (filtersSelected.tags && tagValues.name.length > 0) {
      filterObject = {
        searchField: "tags",
        searchType: "",
        searchText: tagValues.name,
      };

      filters.push(filterObject);
    }

    if (salaryValue[1] !== 0 && filtersSelected.salary) {
      filterObject = {
        searchField: "salary",
        searchType: "BETWEEN",
        searchText: salaryValue[0] + "#@#" + salaryValue[1],
      };

      filters.push(filterObject);
    }

    if (expValue[1] !== 0 && filtersSelected.experience) {
      filterObject = {
        searchField: "experience",
        searchText: expValue[0] + "#@#" + expValue[1],
        searchType: "BETWEEN",
      };

      filters.push(filterObject);
    }

    filters.push(defaultSearchFilters);
    setAllFilters(filters);
    let pageNo = 1;
    setCurrentPage(1);
    submitFunction(filters, pageNo);
    setFetchedData([]);
  };

  const submitFunction = async (postData, pageNo) => {
    // console.log(postData,pageNo);
    // console.log("function called")
    // console.log(currentPage);

    const body = {
      filters: {
        sortField: "id",
        sortOrder: "desc",
        search: postData,
        pageNumber: pageNo - 1,
        pageSize: 6,
      },
    };

    setIsFetching(true);
    //console.log(body);

    const url =
      type === "jobs"
        ? "job/get/"
        : type === "scholarships"
          ? "job/scholarship/get/"
          : type === "companies"
            ? "company/get/"
            : "";

    await axios({
      method: "post",
      headers: requestOptions.headers,
      data: body,
      url: `${baseUrl}/${url}`,
    })
      .then((res) => {
        // console.log(res)
        //console.log("function calles")
        if (res.data.status === "OK") {
          if (
            res.data.data.total_count / res.data.data.page_size >
            res.data.data.page_number
          ) {
            sethasMoreData(true);
            setAllFilters(postData);
          } else {
            sethasMoreData(false);
          }
          setIsFetching(false);
          if (type === "jobs") {
            if (res.data.data.jobs.length > 0) {
              res.data.data.jobs.forEach((job) => {
                setFetchedData((prevData) => [...prevData, job]);
              });
            }
            // setFetchedData(res.data.data.jobs);
          }
          if (type === "scholarships") {
            if (res.data.data.scholarships.length > 0) {
              res.data.data.scholarships.forEach((scholarship) => {
                setFetchedData((prevData) => [...prevData, scholarship]);
              });
            }
          }
          if (type === "companies") {
            if (res.data.data.companies.length > 0) {
              res.data.data.companies.forEach((company) => {
                setFetchedData((prevData) => [...prevData, company]);
              });
            }
          }
        }
      })
      .catch((err) => {
        setIsError(true);
        setIsFetching(false);
      });
  };

  const GreenCheckbox = withStyles({
    root: {
      color: "#000",
      "&$checked": {
        color: "#06B0C5",
      },
    },
    checked: {},
  })((props) => <Checkbox color="default" {...props} />);

  return (
    <Grid container className={classes.mainContainer}>
      <form onSumit={handleDefaultSubmit} className={classes.innerContainer}>
        <Grid item xs={12}>
          <TextField
            value={defaultSearchFilters.searchText}
            onChange={handleDefaultSearchField}
            variant="outlined"
            placeholder="Type here..."
            className={clsx(classes.root, classes.input, classes.singleLineInput)}
            InputProps={{
              endAdornment: (
                <Button
                  type="submit"
                  onClick={handleDefaultSubmit}
                  variant="outlined"
                  ariaLabel="Search"
                >
                  <Search />
                </Button>
              ),
            }}
          />
        </Grid>
      </form>
      <Grid
        item
        container
        xs={12}
        className={classes.innerContainer}
        alignItems="center"
        justify="space-between"
      >
        <Button onClick={handleOpenFilters} ariaLabel="filter">
          <img src={filterIcon} alt="filterIcon" />
          &nbsp; Filters
        </Button>
      </Grid>
      {openFilters && (
        <Grid
          item
          container
          xs={12}
          className={clsx(
            classes.innerContainer,
            classes.filtersOuterContainer
          )}
        >
          <Grid item container xs={12} className={classes.filtersContainer}>
            <Grid item container alignItems="center" justify="flex-end" xs={12}>
              {openFilters && (
                <IconButton onClick={handleCloseFilters}>
                  <Close />
                </IconButton>
              )}
            </Grid>
            <Grid item xs={12} container >
              {(type === "jobs" || type === "scholarships") && <Grid item direction="column" container xs={12} md={6} style={{ height: "fit-content" }}>
                {(type === "jobs" || type === "scholarships") && (
                  <Grid item xs={12} container direction="column">
                    <FormGroup className={clsx(classes.checkBoxContainer, classes.labelContainer)} row>
                      <FormControlLabel
                        control={
                          <GreenCheckbox
                            checked={filtersSelected.tags}
                            onChange={handleChangeSelectedFilters}
                            name="tags"
                          />
                        }
                        label="Diversity Tags"
                      />
                    </FormGroup>
                    <Grid item xs={12} className={classes.formFieldContainer}>
                      {filtersSelected.tags && (
                        <Autocomplete
                          value={tagValues}
                          onChange={handleTagValues}
                          id="tags"
                          name="tags"
                          getOptionLabel={(option) => option.name}
                          options={tagList}
                          style={{ maxWidth: "100%", width: "90%" }}
                          renderInput={(params) => (
                            <TextField
                              {...params}
                              className={clsx(classes.root4, classes.input)}
                              variant="outlined"
                            />
                          )}
                        />
                      )}
                    </Grid>
                  </Grid>
                )}
                {type === "jobs" && (
                  <Grid item container direction="column" xs={12}>
                    <FormGroup row className={clsx(classes.checkBoxContainer, classes.labelContainer)}>
                      <FormControlLabel
                        label="Select salary range"
                        control={
                          <GreenCheckbox
                            checked={filtersSelected.salary}
                            onChange={handleChangeSelectedFilters}
                            name="salary"
                          />
                        }
                      />
                    </FormGroup>
                    <Grid item xs={12} className={classes.formFieldContainer}>
                      {filtersSelected.salary && (
                        <>
                          <Slider
                            value={salaryValue}
                            className={classes.sliders}
                            aria-labelledby="discrete-slider"
                            onChange={handleSalaryChange}
                            marks
                            step={1000000}
                            min={0}
                            max={9000000}
                          />
                          <Grid container justify="space-between">
                            <Grid item>{salaryValue[0]}</Grid>
                            <Grid item>to</Grid>
                            <Grid item>{salaryValue[1]}</Grid>
                          </Grid>
                        </>
                      )}
                    </Grid>
                  </Grid>
                )}
                {type === "jobs" && (
                  <Grid item xs={12} container direction="column">
                    <FormGroup row className={clsx(classes.checkBoxContainer, classes.labelContainer)}>
                      <FormControlLabel
                        control={
                          <GreenCheckbox
                            checked={filtersSelected.experience}
                            onChange={handleChangeSelectedFilters}
                            name="experience"
                          />
                        }
                        label="Select experience range"
                      />
                    </FormGroup>
                    <Grid item xs={12} className={classes.formFieldContainer}>
                      {filtersSelected.experience && (
                        <>
                          <Slider
                            value={expValue}
                            className={classes.sliders}
                            aria-labelledby="discrete-slider"
                            valueLabelDisplay="auto"
                            onChange={handleExpChange}
                            marks
                            step={2}
                            min={0}
                            max={20}
                          />
                          <Grid container justify="space-between">
                            <Grid item>{expValue[0]} years</Grid>
                            <Grid item>to</Grid>
                            <Grid item>{expValue[1]} years</Grid>
                          </Grid>
                        </>
                      )}
                    </Grid>
                  </Grid>
                )}
              </Grid>}

              <Grid item container direction="column" xs={12} md={6} style={{ height: "fit-content" }}>
                {(type === "jobs" || type === "scholarships") && (
                  <Grid item container xs={12} direction="column">
                    <FormGroup row className={clsx(classes.checkBoxContainer, classes.labelContainer)}>
                      <FormControlLabel
                        label="Job Type"
                        control={
                          <GreenCheckbox
                            checked={filtersSelected.jobType}
                            onChange={handleChangeSelectedFilters}
                            name="jobType"
                          />
                        }
                      />
                    </FormGroup>
                    {filtersSelected.jobType && (
                      <Grid
                        item
                        xs={12}
                        className={classes.controlsformFieldContainer}
                      >
                        <Controls.FormInput
                          value={values.job_type}
                          name="job_type"
                          handleChange={handleChange}
                          label=""
                          multiline={false}
                          integer={false}
                          small={false}
                        />
                      </Grid>
                    )}
                  </Grid>
                )}
                {(type === "jobs" || type === "scholarships") && (
                  <Grid container item xs={12} direction="column">
                    <FormGroup row className={clsx(classes.checkBoxContainer, classes.labelContainer)}>
                      <FormControlLabel
                        label="Filter by locations"
                        control={
                          <GreenCheckbox
                            checked={filtersSelected.locations}
                            onChange={handleChangeSelectedFilters}
                            name="locations"
                          />
                        }
                      />
                    </FormGroup>
                    {filtersSelected.locations && (
                      <Grid item xs={12} className={classes.controlsformFieldContainer}>
                        <Controls.AddChip
                          label=""
                          values={values}
                          data={cityData}
                          name="locations"
                          setValues={setValues}
                        />
                      </Grid>
                    )}
                  </Grid>
                )}
                <Grid item container direction="column" xs={12}>
                  <FormGroup row className={clsx(classes.checkBoxContainer, classes.labelContainer)}>
                    <FormControlLabel
                      label="Filter by Company Name"
                      control={
                        <GreenCheckbox
                          checked={filtersSelected.companyName}
                          onChange={handleChangeSelectedFilters}
                          name="companyName"
                        />
                      }
                    />
                  </FormGroup>
                  <Grid item xs={12} className={classes.formFieldContainer}>
                    {filtersSelected.companyName && (
                      <Autocomplete
                        value={companyNameValues}
                        onChange={handleCompanyNameValues}
                        id="company-name"
                        getOptionLabel={(option) => option.name}
                        options={companyName}
                        style={{ maxWidth: 300, width: "90%" }}
                        renderInput={(params) => (
                          <TextField
                            {...params}
                            className={clsx(classes.root4, classes.input)}
                            variant="outlined"
                          />
                        )}
                      />
                    )}
                  </Grid>
                </Grid>
              </Grid>
            </Grid>

            <Grid
              item
              container
              xs={12}
              justify="flex-end"
              className={classes.innerContainer}
            >
              <Button style={{ marginRight: "1%" }} onClick={handleResetButton} ariaLabel="reset">
                Reset
              </Button>
              <Button
                onClick={handleShowResultsSearch}
                style={{
                  color: "#06B0C5",
                  marginLeft: "1%",
                }}
                ariaLabel="Show Results"
              >
                Show Results
              </Button>
            </Grid>
          </Grid>
        </Grid>
      )}
      <Grid
        xs={12}
        item
        style={{ background: "#DCDCDC", height: "1px", margin: "0.5rem 10px" }}
      />
      <Grid xs={12} container item className={classes.dataContainer}>
        {(type === "jobs" || type === "scholarships") &&
          fetchedData.length > 0 &&
          !isError &&
          fetchedData.map((data, idx) => (
            <Card data={data} type={type} status={null} key={idx} />
          ))}
        {type === "companies" &&
          fetchedData.length > 0 &&
          fetchedData.map((company, index) => (
            <Grid item key={index} className={classes.companyBox}>
              <Link
                to={`/home/company/${company.id}`}
              >
                <Company company={company} />
              </Link>
            </Grid>
          ))}
        {isError && !hasMoreData && (
          <div>Something Went Wrong, Please Try Again</div>
        )}
        {
          (type === "companies" && isFetching) ? <CompanyListingSkeleton /> : (fetchedData.length === 0 && !isError) && <div>No {type} Found Try Again</div>
        }
        {
          ((type === "jobs" || type === "scholarships") && isFetching) ? <CommonCardSkeleton type={type === "jobs" ? "job" : type} /> : (fetchedData.length === 0 && !isError && !isFetching) && <div>No {type} Found Try Again</div>
        }
        {!isFetching && hasMoreData && !isError && (
          <Grid item xs={12} className={classes.viewMoreContainer}>
            <Button
              onClick={handleViewMore}
              size="small"
              variant="outlined"
              style={{ background: "#fff" }}
              ariaLabel="View More"
            >
              View More
            </Button>
          </Grid>
        )}
      </Grid>
    </Grid>
  );
};

export default Listing;
