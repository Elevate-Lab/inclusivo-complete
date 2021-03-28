import { WorkOutline } from '@material-ui/icons';
import CompanyJobs from './CompanyJobs';


const style = {
    width: "24px",
    color: "#fff",
};

export const employerCards = [
    {
        icon: <WorkOutline style={style} />,
        title: "Browse Your Latest Jobs",
        backgroundColor: "rgba(74, 166, 78, 1)",
        component: <CompanyJobs type="applied_jobs" />
    }
]