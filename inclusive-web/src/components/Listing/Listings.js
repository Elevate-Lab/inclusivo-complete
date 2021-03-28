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
  Link,
  Box
} from "@material-ui/core";
import {
  Search,
  Close,
} from "@material-ui/icons";
import { baseUrl } from "../../urlConstants";
import { useStyles } from "./Styles";
import CommonCardSkeleton from "../Loaders/CommonCardSkeleton";
import loadable from "@loadable/component";
import clsx from "clsx";
import filterIcon from "../../assets/Icons/filterIcon.svg";
import useForm from "../../customHooks/useForm";
import Controls from "../Form/Controls/Controls";
import { tagList } from "../Scholarships/TagList";
import { Autocomplete } from '@material-ui/lab';
import axios from "axios";
import Company from '../Company/Company';

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
  const [values, setValues, handleChange] = useForm(selectedFilters);
  const [companyNameValues,setCompanyNameValues] = React.useState({
    name: ''
  });
  const [tagValues,setTagValues] = React.useState({
    name: '',
    id: null
  });
  const [expValue, setExpValue] = React.useState([0, 0]);
  const [salaryValue, setSalaryValue] = React.useState([0, 0]);
  const [companyName, setCompanyName] = React.useState({});
  const [isError,setIsError] = React.useState('');
  const [isFetching,setIsFetching] = React.useState(false);
  const [fetchedData,setFetchedData] = React.useState([]);
  const [hasMoreData,sethasMoreData] = React.useState(false);
  const [currentPage,setCurrentPage] = React.useState(1);
  const [allfilters,setAllFilters] = React.useState([]);

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
    submitFunction([defaultSearchFilters]);

    return () => {
      setCityData({});
      setCompanyName({});
    };
  }, [currentPage]);

  React.useEffect(() => {}, [cityData, values, companyName]);

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
  }

  const handleTagValues = (event, newValue) => {
    setTagValues(newValue);
  }

  const handleViewMore = (e) => {
    setCurrentPage((prev) => prev + 1);
    submitFunction(allfilters);
  }

  const handleDefaultSearchField = (e) => {
    setDefaultSearchFilters({
      searchField: "title",
      searchText: e.target.value,
      searchType: ""
    });
  }

  const handleResetButton = (e) => {
    setValues(selectedFilters);
    setTagValues({
      name: '',
      id: null
    });
    setSalaryValue([0,0]);
    setExpValue([0,0]);
    setCompanyNameValues({
      name: '',
    });
    setFiltersSelected({
      locations: false,
      tags: false,
      companyName: false,
      salary: false,
      experience: false,
      jobType: false
    });
    setAllFilters(defaultSearchFilters);
  }

  const handleDefaultSubmit = (e) => {
    e.preventDefault();
    handleShowResultsSearch(e);
  }
  const handleShowResultsSearch = (e) => {
    e.preventDefault();
    // console.log(values);
    // console.log(companyNameValues);
    // console.log(salaryValue);
    // console.log(expValue);
    // console.log(tagValues);
    let filters = [];
    let filterObject = {};
    if(companyNameValues.name.length > 0 && filtersSelected.companyName){
      filterObject = {
        searchField: "company_name",
        searchText: companyNameValues.name,
        searchType: ""
      }
      filters.push(filterObject);
    } 
    if(values.job_type.length > 0){
      filterObject = {
        searchField: "job_type",
        searchText: values.job_type,
        searchType: ""
      }
      filters.push(filterObject);
    }
    if(values.locations.length > 0){
      let text = "";
      if (values.locations.length > 1) {
        for (let i = 1; i < values.locations.length; i++) {
          text = text + values.locations[i - 1].name + "#@#" + values.locations[i].name;
        }
      } else {
        text = text + values.locations[0];
      }
      filterObject= {
        searchField: "locations",
        searchText: text,
        searchType: "IN"
      }
      filters.push(filterObject);
    }
    if(filtersSelected.tags && tagValues.name.length > 0){
      filterObject ={
        searchField: "tags",
        searchType: "",
        searchText: tagValues.name
      }

      filters.push(filterObject)
    }

    if(salaryValue[1] !== 0 && filtersSelected.salary){
      filterObject={
        searchField: "salary",
        searchType: "BETWEEN",
        searchText: salaryValue[0] + '#@#' + salaryValue[1]
      }

      filters.push(filterObject);
    }

    if(expValue[1] !== 0 && filtersSelected.experience){
      filterObject = {
        searchField: "experience",
        searchText: expValue[0] + '#@#' + expValue[1],
        searchType: "BETWEEN"
      }

      filters.push(filterObject);
    }

    filters.push(defaultSearchFilters);
    setAllFilters(filters);
    submitFunction(filters);
    setFetchedData([]);
  }

  const submitFunction = async (postData) => {
    // console.log(postData);
    const body = {
      filters: {
        sortField: "id",
        sortOrder: "desc",
        search: postData,
        pageNumber: currentPage-1,
        pageSize: 6
      }
    }

    setIsFetching(true);
    // console.log(body);

    const url = type === "jobs" ? 'job/get/' : type === "scholarships" ? 'job/scholarship/get/' : type ==="companies" ? 'company/get/' : ""

    await axios({
      method: "post",
      headers: requestOptions.headers,
      data: body,
      url: `${baseUrl}/${url}`
    }).then(res => {
      // console.log(res)
      if(res.data.status === "OK"){
        if (res.data.data.total_count / res.data.data.page_size > res.data.data.page_number) {
          sethasMoreData(true);
        } else {
          sethasMoreData(false);
        }
        setIsFetching(false);
        if(type === "jobs"){
          if(res.data.data.jobs.length > 0){
            res.data.data.jobs.forEach((job) => {
              setFetchedData(prevData => [...prevData,job])
            })
          }
          // setFetchedData(res.data.data.jobs);
        } 
        if(type==="scholarships"){
          if(res.data.data.scholarships.length > 0){
            res.data.data.scholarships.forEach((scholarship) => {
              setFetchedData(prevData => [...prevData, scholarship])
            })
          }
        }
        if(type==="companies") {
          if(res.data.data.companies.length > 0){
            res.data.data.companies.forEach(company => {
              setFetchedData(prevData => [...prevData,company])
            })
          }
        }
      }
    })
    .catch(err => {
      setIsError(true);
      setIsFetching(false);
    })
  }

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
      <Grid item xs={12} className={classes.innerContainer}>
        <TextField
          value={defaultSearchFilters.searchText}
          onChange={handleDefaultSearchField}
          variant="outlined"
          placeholder="Type here..."
          className={clsx(classes.root, classes.input, classes.singleLineInput)}
          InputProps={
            {
              endAdornment: <Button type="submit" onClick={handleDefaultSubmit} variant="outlined"><Search /></Button>
            }
          }
        />
      </Grid>
      <Grid
        item
        container
        xs={12}
        className={classes.innerContainer}
        alignItems="center"
        justify="space-between"
      >
        <Button onClick={handleOpenFilters}>
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
            <Grid item xs={12} container>
              <Grid item container xs={12} md={6}>
                {(type === "jobs" || type === "scholarships") && (
                  <Grid item xs={12}>
                    <FormGroup className={classes.checkBoxContainer} row>
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
                      {filtersSelected.tags && (
                        <Autocomplete
                          value={tagValues}
                          onChange={handleTagValues}
                          id="tags"
                          name="tags"
                          getOptionLabel={(option) => option.name}
                          options={tagList}
                          style={{ maxWidth: "100%",width: "90%" }}
                          renderInput={(params) => <TextField {...params} className={clsx(classes.root4,classes.input)} variant="outlined" />}
                        />
                      )}
                  </Grid>
                )}
                {type === "jobs" && (
                  <Grid item xs={12}>
                    <FormGroup row className={classes.checkBoxContainer}>
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
                      {
                        filtersSelected.salary && <><Slider
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
                      </Grid></>}
                    </FormGroup>
                  </Grid>
                )}
                {type === "jobs" && (
                  <Grid item xs={12}>
                    <FormGroup row className={classes.checkBoxContainer}>
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
                      {
                        filtersSelected.experience && <><Slider
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
                      }
                    </FormGroup>
                  </Grid>
                )}
              </Grid>
              <Grid item container xs={12} md={6}>
                {(type === "jobs" || type === "scholarships") && (
                  <Grid item xs={12}>
                    <FormGroup row className={classes.checkBoxContainer}>
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
                      {filtersSelected.jobType && (
                          <Controls.FormInput
                            value={values.job_type}
                            name="job_type"
                            handleChange={handleChange}
                            label=""
                            multiline={false}
                            integer={false}
                          />
                      )}
                    </FormGroup>
                  </Grid>
                )}
                {(type === "jobs" || type === "scholarships") && (
                  <Grid item xs={12}>
                    <FormGroup row className={classes.checkBoxContainer}>
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
                      {filtersSelected.locations && (
                          <Controls.AddChip
                            label=""
                            values={values}
                            data={cityData}
                            name="locations"
                            setValues={setValues}
                          />
                      )}
                    </FormGroup>
                  </Grid>
                )}
                <Grid item xs={12}>
                  <FormGroup row className={classes.checkBoxContainer}>
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
                    {filtersSelected.companyName && (
                      <Autocomplete
                        value={companyNameValues}
                        onChange={handleCompanyNameValues}
                        id="company-name"
                        getOptionLabel={(option) => option.name}
                        options={companyName}
                        style={{ maxWidth: 300,width: "90%" }}
                        renderInput={(params) => <TextField {...params} className={clsx(classes.root4, classes.input)}  variant="outlined" />}
                      />
                    )}
                  </FormGroup>
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
              <Button style={{ marginRight: "1%" }} onClick={handleResetButton}>Reset</Button>
              <Button
                onClick={handleShowResultsSearch}
                style={{
                  color: "#06B0C5",
                  marginLeft: "1%",
                }}
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
        {
          ((type==="jobs" || type ==="scholarships") && fetchedData.length > 0 && !isError) && fetchedData.map((data) => (
            <Card data={data} type={type} />
          ))
        }
        {
          type === "companies" && fetchedData.length > 0 && fetchedData.map((company, index) => (
          <Grid item sm = { 6} xs = { 12} md = { 4} lg = { 3} >
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
          </Grid>))}
        {
          (fetchedData.length === 0 && !isError) && <div>No {type} Found Try Again</div> 
        }
        {
          isError && !hasMoreData && <div>Something Went Wrong, Please Try Again</div>
        }
        {isFetching && (
          <CommonCardSkeleton type="job" />
        )}
        {!isFetching && hasMoreData && !isError && (
          <Grid item className={classes.viewMoreContainer}>
            <Button
              onClick={handleViewMore}
              size="small"
              variant="outlined"
              style={{ background: "#fff", }}
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
