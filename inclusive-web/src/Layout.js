import React from 'react'
import {
    Route,
    Switch,
    useHistory
} from 'react-router-dom'
import {
    makeStyles
} from '@material-ui/core';
import loadable from '@loadable/component';
import { ProfileDetails } from './pages/Profile/ProfileDetails';
import { ProtectedRoute } from './ProtectedRoute';
import { userStatus } from './actions/authActions/userStatusActions';
import { useSelector, useDispatch } from 'react-redux'
import CompanyListing from './components/Company/CompanyListing'

const ScholarshipDescription = loadable(() => import('./components/Description/ScholarshipDescription'));
const AddCompany = loadable(() => import('./components/Company/AddCompany'));
const CompanyDescription = loadable(() => import('./components/CompanyDescription/CompanyDescription'));
const AddJob = loadable(() => import('./pages/Job/AddJob'));
const JobDescription = loadable(() => import('./components/Description/JobDescription'));
const LikedJobs = loadable(() => import('./components/Dashboard/getLikedJobs'));
const AddStory = loadable(() => import('./components/Stories/AddStory'));
const StoryDescription = loadable(() => import('./components/Stories/StoryDescription'));
const StoryListing = loadable(() => import('./components/Stories/StoryListing'));
const AddScholarship = loadable(() => import('./pages/Scholarship/AddScholarship'));
const ScholarshipList = loadable(() => import('./components/Scholarships/List'));
const JobList = loadable(() => import('./components/JobListings/List'));
const Subscribed = loadable(() => import('./components/Company/Subscribed'));
const InitiativesList = loadable(() => import('./components/initiatives/List'));
const Dashboard = loadable(() => import('./components/Dashboard/DashboardHome'))
const Navigation = loadable(() => import('./components/Layout/Navigation'));
const BlogDescription = loadable(() => import('./components/Blogs/BlogDescription'));
const BlogListing = loadable(() => import('./components/Blogs/BlogListing'));
const VideoDescription = loadable(() => import('./components/Video/VideoDescription'));
const VideoListing = loadable(() => import('./components/Video/VideoListing'));

const useStyle = makeStyles(theme => ({
    bossContainer: props => ({
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        marginLeft: props.marginDetails.marginFromLeft,
        marginTop: '56px',
        padding: '0px 6px',
    })
}))

function Layout(props) {
    const marginDetails = useSelector(state => state.marginDetails)
    const prop = { marginDetails }
    const dispatch = useDispatch();
    const classes = useStyle(prop);
    const history = useHistory();
    const [fetched, setIsfetched] = React.useState(false);

    history.listen((newLocation, action) => {
        if (action === "POP") {
            if (newLocation.pathname === "/complete/employer" || newLocation.pathname === "/complete/candidate" || newLocation.pathname === "/auth") {
                history.go(1);
            }
        }
    })
    React.useEffect(() => {
        async function setUserData() {
            dispatch(userStatus());
            setIsfetched(true);
        }

        setUserData();
    }, [])

    return (
        <>
            <Navigation {...props} />
            {
                fetched ? <div className={classes.bossContainer}>
                    <Switch>
                        {/* profile */}
                        <ProtectedRoute exact path="/home/profile"
                            component={ProfileDetails}
                        />
                        {/* Dashboard Routes */}
                        <ProtectedRoute exact path="/home/" component={Dashboard} />

                        {/* Company Routes */}
                        <ProtectedRoute exact path="/home/company/list" component={CompanyListing} />
                        <ProtectedRoute exact path="/home/company/add" component={AddCompany} />
                        <ProtectedRoute exact path="/home/company/:id" component={CompanyDescription} />
                        <ProtectedRoute exact path='/home/company/get/subscribed' component={Subscribed} />

                        {/* Job Routes */}
                        <Route exact path="/home/job/list" component={JobList} />
                        <Route exact path="/home/job/add" component={AddJob} />
                        <Route exact path="/home/job/liked" component={LikedJobs} />
                        <Route exact path="/home/job/:id" component={JobDescription} />

                        {/*  Story Routes */}
                        <ProtectedRoute exact path="/home/story/add" component={AddStory} />
                        <ProtectedRoute exact path="/home/story/list" component={StoryListing} />
                        <ProtectedRoute exact path="/home/company/:cid/story/:sid" component={StoryDescription} />

                        {/*  Blog Routes */}
                        <ProtectedRoute exact path="/home/blog/list" component={BlogListing} />
                        <ProtectedRoute exact path="/home/blog/:id" component={BlogDescription} />

                        {/*  Video Routes */}
                        <ProtectedRoute exact path="/home/video/list" component={VideoListing} />
                        <ProtectedRoute exact path="/home/video/:id" component={VideoDescription} />

                        {/* Scholarship Routes */}
                        <ProtectedRoute exact path="/home/scholarship/list" component={ScholarshipList} />
                        <ProtectedRoute path="/home/scholarship/add" component={AddScholarship} />
                        <ProtectedRoute path="/home/scholarship/:id" component={ScholarshipDescription} />

                        {/* Initiatives routes */}
                        <ProtectedRoute path="/home/company/:id/initiatives" component={InitiativesList} />
                    </Switch>
                </div> : <div>Loading...</div>
            }

        </>
    );
}

export default Layout
