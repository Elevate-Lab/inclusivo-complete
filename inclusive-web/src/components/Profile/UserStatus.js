import React from 'react';
import { useDispatch,useSelector } from 'react-redux';
import { Redirect } from 'react-router-dom';
import { userStatus } from '../../actions/authActions/userStatusActions';
import Loader from '../../assets/loader/loader';
import loadable from '@loadable/component';
const UpdateProfile = loadable(() => import('./UpdateProfile'))

const UserStatus = (props) => {
    window.onbeforeunload = function(e){
        return 'you can not refresh the page';
    }
    const userStatusData = useSelector(state => state.userStatus);
    const dispatch = useDispatch();

    React.useLayoutEffect(() => {
        dispatch(userStatus());
    },[dispatch]);

    const resetSite = () => {
        if(localStorage.getItem('key')){
            localStorage.removeItem('key');
            localStorage.removeItem('userEmail');
        }
        window.location.reload();
    }

    return (<>
        {userStatusData.loading ? <div style={{ display: "flex", alignItems: "center", justifyContent: "center", marginTop: "150px" }}>
            <Loader loading={true} />
        </div> : (
            userStatusData.isCompleted ? <Redirect to = '/home' /> : <UpdateProfile isCandidate={userStatusData.isCandidate} />
        )}
        {
            (userStatusData.error && userStatusData.isError) && resetSite()
        }
    </>)
}

export default UserStatus;