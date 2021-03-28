import { WorkOutline, LocationCity } from "@material-ui/icons";
import { Icon } from "@material-ui/core";
import Community from "../../assets/Icons/Community.svg";
import Cities from "../../assets/Icons/Cities.svg";
import SubscribedIcon from "../../assets/Icons/SubscribedIcon.svg";
import Lists from './Lists';
import CityJobs from './CityJobs';

const CommunityIcon = () => {
  return (
    <Icon>
      <img src={Community} alt="Community" />
    </Icon>
  );
};

const CitiesIcon = () => {
  return (
    <Icon>
      <img src={Cities} alt="Cities" />
    </Icon>
  );
};
const SubscribedCompanyIcon = () => {
  return (
    <Icon>
      <img src={SubscribedIcon} alt="Subscribed" />
    </Icon>
  );
};

const style = {
  width: "24px",
  color: "#fff",
};

export const cardItems = [
  {
    icon: <WorkOutline style={style} />,
    title: "Applied Jobs",
    backgroundColor: "rgba(74, 166, 78, 1)",
    component : <Lists type="applied_jobs" />
  },
  // {
  //   icon: <CommunityIcon style={style} />,
  //   title: "Browse Jobs by Community",
  //   backgroundColor: "rgba(253, 110, 15, 1)",
  // },
  {
    icon: <LocationCity style={style} />,
    title: "Jobs in Preferred City",
    backgroundColor: "rgba(166, 74, 151, 1)",
    component: <Lists type="preferred_city" />
  },
  // {
  //   icon: <CitiesIcon style={style} />,
  //   title: "Jobs in Cities",
  //   backgroundColor: "rgba(197, 70, 6, 1)",
  //   component: <CityJobs/>
  // },
  {
    icon: <SubscribedCompanyIcon style={style} />,
    title: "Subscribed Companies",
    backgroundColor: "rgba(6, 176, 197, 1)",
    component: <Lists type="subscribed_companies" />
  },
];
