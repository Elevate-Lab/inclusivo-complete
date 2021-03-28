import React, { useState } from "react";
import {
  makeStyles,
  Grid,
  Button,
  Typography,
  MenuItem,
  TextField,
  Checkbox,
  CircularProgress
} from "@material-ui/core";
import {
  MuiPickersUtilsProvider,
  KeyboardDatePicker,
} from "@material-ui/pickers";
import "date-fns";
import DateFnsUtils from "@date-io/date-fns";
import Tags from "./Tags.js";
import { baseUrl } from "../../urlConstants";
import Autocomplete from '@material-ui/lab/Autocomplete';
import {List } from "react-virtualized";
import "react-virtualized/styles.css";

const useStyles = makeStyles((theme) => ({
  formName: {
    color: "red",
    fontSize: "25px",
    marginTop: "5px",
  },
  sectionName: {
    marginTop: "10px",
    width: "95%",
    paddingLeft: "5px",
    fontSize: "20px",
  },
  formContainer: {
    display: "flex",
    flexDirection: "column",
    justifyContent: "center",
    alignItems: "center",
    width: "100%",
    maxWidth: "30rem",
  },
  formInputs: {
    padding: "10px",
    width: "95%",
  },
  fieldInput: {
    fontSize: "70px",
    width: "100%",
  },
  buttonContainer: {
    display: "flex",
  },
  formButton: {
    width: "15rem",
    maxWidth: "90vw",
    margin: "5%",
  },
}));

const AddJob = () => {
  const [degrees, setDegrees] = useState([]);
  const [jobtags, setTags] = useState([]);
  const [locations, setLocations] = useState([]);
  const [selectedDate, setSelectedDate] = useState(new Date("2021-02-14"));
  const classes = useStyles();
  const [cityData, setCityData] = useState([]);
  const [loadingCities,setLoadingCities] = React.useState(false);

  const AddInfo = [
    { label: "Job Role", name: "role", type: "text" },
    { label: "Job Type", name: "jobtype", type: "text" },
    { label: "Title", name: "title", type: "text" },
    { label: "Job Description", name: "description", type: "textarea" },
    { label: "Selection Process", name: "process", type: "textarea" },
  ];

  const requirements = [
    { label: "Add Degree", name: "degree", type: "select" },
    { label: "Min Experience", name: "min_exp", type: "select" },
    { label: "Max Experience", name: "max_exp", type: "select" },
  ];

  const additional_info = [
    { label: "Vacancies", name: "vacancies", type: "text" },
    { label: "Add Tag", name: "tags", type: "select" },
    { label: "Min CTC", name: "min_ctc", type: "select" },
    { label: "Max CTC", name: "max_ctc", type: "select" },
  ];

  const getCities = async () => {
    let authToken = "b48eef6b74aa372c1ab5f65f9b85c11099679043";
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
    setLoadingCities(true);
    const response = await fetch(
      `${baseUrl}/user/location/dropdown/india`,
      requestOptions
    );
    const data = await response.json();
    setLoadingCities(false);
    setCityData(data.data);
    // setCityData(data["data"]["cities"]);
  };

  React.useEffect(() => {
    getCities();
  },[])

  const handleDegree = (event) => {
    const degree = event.target.attributes["data-value"].nodeValue;
    setDegrees((prevTags) => {
      return prevTags.includes(degree) ? prevTags : [...prevTags, degree];
    });
  };

  const handleTags = (event) => {
    const tag = event.target.attributes["data-value"].nodeValue;
    setTags((prevTags) => {
      return prevTags.includes(tag) ? prevTags : [...prevTags, tag];
    });
  };

  const handleLocations = (event) => {
    const location = event.target.attributes["value"]["value"];
    setLocations((prevTags) => {
      return prevTags.includes(location) ? prevTags : [...prevTags, location];
    });
  };

  const clearElement = (event, tag) => {
    console.log(event);
    if (event === "degree") {
      setDegrees((prevTags) => {
        return prevTags.filter((item, index) => {
          return item !== tag;
        });
      });
    }
    if (event === "tags") {
      setTags((prevTags) => {
        return prevTags.filter((item, index) => {
          return item !== tag;
        });
      });
    }
    if (event === "locations") {
      setLocations((prevTags) => {
        return prevTags.filter((item, index) => {
          return item !== tag;
        });
      });
    }
  };

  const handleDateChange = (date) => {
    setSelectedDate(date);
  };

  const displayField = (data, idx) => {
    return (
      <div className={classes.formInputs} key={idx}>
        {data.name === "degree" && (
          <Tags tags={degrees} clear={clearElement} name={data.name} />
        )}
        {data.name === "tags" && (
          <Tags tags={jobtags} clear={clearElement} name={data.name} />
        )}
        {data.name === "locations" && (
          <Tags tags={locations} clear={clearElement} name={data.name} />
        )}
        <TextField
          className={classes.fieldInput}
          name={data.name}
          id={data.name}
          label={data.label}
          type={data.type}
          allowFontScaling={false}
          {...(data.type === "textarea" && {
            multiline: true,
            rows: 3,
            variant: "outlined",
          })}
          {...(data.type === "select" && {
            select: true,
            defaultValue: 0,
          })}
        >
          {data.name === "tags" &&
            [
              "HR",
              "Hiring",
              "Content Writing",
              "Full-time",
              "Part-time",
              "Recruiting",
              "Event Management",
              "Management",
              "development",
              "Front-end",
              "Backend",
              "Excel",
              "Database",
              "My-SQL",
              "Internship",
              "Full-Time",
            ].map((option) => (
              <MenuItem key={option} value={option} onClick={handleTags}>
                {option}
              </MenuItem>
            ))}
          {data.name === "degree" &&
            ["BTech.", "MBA", "MTech.", "MPhil.", "MBBS", "Dental"].map(
              (option) => (
                <MenuItem key={option} value={option} onClick={handleDegree}>
                  {option}
                </MenuItem>
              )
            )}
          {(data.name === "min_exp" || data.name === "max_exp") &&
            ["1", "2", "3", "4", "5", "6", "7", "8", "9", "> 10 years"].map(
              (option) => (
                <MenuItem key={option} value={option}>
                  {option}
                </MenuItem>
              )
            )}

          {(data.name === "min_ctc" || data.name === "max_ctc") &&
            [
              "1 lpa",
              "2 lpa",
              "3 lpa",
              "4 lpa",
              "5 lpa",
              "6 lpa",
              "7 lpa",
              "8 lpa",
              "9 lpa",
              "10 lpa",
              "11 lpa",
              "12 lpa",
              "13 lpa",
              "14 lpa",
              "> 15 lpa",
            ].map((option) => (
              <MenuItem key={option} value={option}>
                {option}
              </MenuItem>
            ))}
        </TextField>
      </div>
    );
  };
  const ListboxComponent = React.forwardRef(function ListboxComponent(
    props,
    ref
  ) {
    const { children, role, ...other } = props;
    const itemCount = Array.isArray(children) ? children.length : 0;
    const itemSize = 36;

    return (
      <div ref={ref}>
        <div {...other}>
          <List
            height={250}
            width={300}
            rowHeight={itemSize}
            overscanCount={5}
            rowCount={itemCount}
            rowRenderer={props => {
              return React.cloneElement(children[props.index], {
                style: props.style
              });
            }}
            role={role}
          />
        </div>
      </div>
    );
  });

  return (
    <>
      <Grid
        container
        direction="column"
        justify="center"
        spacing={5}
        alignItems="center"
      >
        <Grid item>
          <Typography className={classes.formName}>Add Job</Typography>
        </Grid>

        <Grid item>
          <Typography className={classes.sectionName}>
            <strong> Add Info</strong>
          </Typography>
          <form className={classes.formContainer}>
            {AddInfo.map((formType, idx) => displayField(formType, idx))}
          </form>

          <Typography className={classes.sectionName}>
            <strong> Requirements </strong>
          </Typography>

          <form className={classes.formContainer}>
            {requirements.map((formType, idx) => displayField(formType, idx))}
          </form>

          <Typography className={classes.sectionName}>
            <strong> Add More Info</strong>
          </Typography>

          <form className={classes.formContainer}>
            {additional_info.map((formType, idx) =>
              displayField(formType, idx)
            )}
          </form>
          <form className={classes.formContainer}>
            <div className={classes.formInputs}>
              <Autocomplete
                className={classes.fieldInput}
                id="virtualize-demo"
                style={{ width: "100%" }}
                getOptionLabel={(option) => option.name + ", " + option.state_name + ", " + option.country_name}
                disableListWrap
                ListboxComponent={ListboxComponent}
                loading={loadingCities}
                options={cityData}
                renderInput={params => (
                  <TextField
                    {...params}
                    label="Add Location"
                    variant="standard"
                    InputProps={{
                      ...params.InputProps,
                      endAdornment: (
                        <React.Fragment>
                          {loadingCities ? <CircularProgress color="inherit" size={20} /> : null}
                          {params.InputProps.endAdornment}
                        </React.Fragment>
                      ),
                    }}
                  />
                )}
              />
            </div>
          </form>

          <Typography>
            <Checkbox />
            Display Salary
          </Typography>

          <MuiPickersUtilsProvider utils={DateFnsUtils}>
            <KeyboardDatePicker
              margin="normal"
              id="date-picker-dialog"
              label="Last Date"
              format="dd/MM/yyyy"
              value={selectedDate}
              onChange={handleDateChange}
              KeyboardButtonProps={{ "aria-label": "change date" }}
            />
          </MuiPickersUtilsProvider>

          <div className={classes.buttonContainer}>
            <Button
              className={classes.formButton}
              variant="contained"
              color="secondary"
              type="submit"
            >
              Save as Draft
            </Button>
            <Button
              className={classes.formButton}
              variant="contained"
              color="secondary"
              type="submit"
            >
              Post Job
            </Button>
          </div>
        </Grid>
      </Grid>
    </>
  );
};

export default AddJob;
