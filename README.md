# inclusivo
With a mission to help get jobs to everyone, Inclusivo is focused upon diversity hiring. We belong, You belong.

Inclusivo is an online platform that makes it easier for people from underprivileged communities to find and get relevant jobs in their respective fields. We connect job seekers and job givers by focusing exclusively on inclusion; we support women, working mothers, the specially challenged, the veterans, LGBTQIA+ folks, and other underrepresented communities.

### Figma Design

https://www.figma.com/file/HM6gDn00Ivq9yhK2WCQptU/Inclusivo?node-id=2%3A243


### Get it on Google Play Store
https://play.google.com/store/apps/details?id=com.dsciiita.inclusivo

## WEB APP

To run web app locally:

- Install required packages using `npm i` in inclusiv-web directory 
- Go to firebase and create a new project. Go to project settings and register web application
- Create a new file `firebaseConfig.js` in `inclusive-web/src/firebase/` and add
```
export const firebaseConfig = {
    apiKey: process.env.REACT_APP_API_KEY,
    authDomain: process.env.REACT_APP_AUTH_DOMAIN,
    projectId: process.env.REACT_APP_PROJECT_ID,
    storageBucket: process.env.REACT_APP_STORAGE_BUCKET,
    messagingSenderId: process.env.REACT_APP_MSG_SENDER_ID,
    appId: process.env.REACT_APP_APP_ID,
    measurementId: process.env.REACT_APP_MEASUREMENT_ID
}
```
- Create env file in inclusive-web directory and add the respective keys from config firebase SDK snippet provided in project settings in firebase console
```
REACT_APP_API_KEY=<apiKey>
REACT_APP_AUTH_DOMAIN=<authDomain>
REACT_APP_PROJECT_ID=<projectId>
REACT_APP_STORAGE_BUCKET=<storageBucket>
REACT_APP_MSG_SENDER_ID=<messagingSenderId>
REACT_APP_APP_ID=<appId>
REACT_APP_MEASUREMENT_ID=<measurementId>
```
- Start the development server using command `npm start` in inclusive-web directory
- Make sure your server runs on PORT `3000` otherwise the app will throw cors error.
