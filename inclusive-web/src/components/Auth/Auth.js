import React from 'react';
import { useSelector } from 'react-redux';
import loadable from '@loadable/component';
const Login = loadable(() => import('./Login'));
const Register = loadable(() => import('./Register'));

const Auth = (props) => {
    const userRegisterDetails = useSelector(state => state.userRegister);
    const userLoginDetails = useSelector((state) => state.userLogin);
    const [userEmail,setUserEmail] = React.useState(''); 
    const [message,setMessage] = React.useState('');

    const [isUser,setIsUser] = React.useState(true);
    React.useLayoutEffect(() => {
        if(props.location.state !== undefined){
            if(props.location.state.mode === "DefaultLogin"){
                setUserEmail(userLoginDetails.userEmail);
            } else if (props.location.state.mode === "Registration" ){
                setUserEmail(userRegisterDetails.userEmail);
            }
            if(props.location.state.message){
                setMessage(props.location.state.message);
            }
            setIsUser(props.location.state.is_user);
        }
    },[props,userLoginDetails,userRegisterDetails])

    return(
        isUser ? <Login userEmail={userEmail} message={message} /> : <Register userEmail={userEmail}/>
    )
}

export default Auth;