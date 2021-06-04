import React from 'react';
import './style.css'
import { BrowserRouter, Route, Switch} from 'react-router-dom';
import Landing from './components/Auth/Landing';
import CompleteCandidate from './components/Profile/CompleteCandidate';
import CompleteEmployer from './components/Profile/CompleteEmployer';
import { NonProtectedRoute, ProtectedRoute } from './ProtectedRoute';
import UserStatus from './components/Profile/UserStatus';
import Auth from './components/Auth/Auth';
import Legal from './components/Legal/Legal';
import AddCompany from './components/Company/AddCompany';
import Layout from './Layout'
import { withRoot } from './withRoot';

function App() {

  return (
    // <Provider store={store}>
      <>
        <BrowserRouter>
          <Switch>
            <Route exact path="/legal" component={Legal} />
            <NonProtectedRoute exact path="/" component={Landing} />
            <NonProtectedRoute exact path="/auth" component={Auth} />
            <ProtectedRoute exact path="/complete/candidate" component={CompleteCandidate} />
            <ProtectedRoute exact path="/complete/employer" component={CompleteEmployer} />
            <ProtectedRoute path="/home" component={Layout} />
            <ProtectedRoute path="/profilestatus" component = {UserStatus} />
            <ProtectedRoute path="/addcompany" component = {AddCompany} />
          </Switch>
        </BrowserRouter>
      </>
    // </Provider>
  );
}

export default withRoot(App);
