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
    return (
        userStatusData.loading ? <div style={{top: "40%",left: "45%",position: "absolute"}}>
            <Loader loading={true} />
        </div> : (
            userStatusData.isCompleted ? <Redirect to = '/home' /> : <UpdateProfile isCandidate={userStatusData.isCandidate} />
        )
    )
}

export default UserStatus;