import React, { useEffect } from 'react';
import {
    makeStyles, 
    TextField,
    Typography,
    Button,
    Dialog,
    IconButton,
    DialogActions,
    DialogContent,
    useMediaQuery,
    CircularProgress,
    LinearProgress
} from '@material-ui/core';
import { withStyles } from '@material-ui/core/styles';
import CloseIcon from '@material-ui/icons/Close';
import MuiDialogTitle from '@material-ui/core/DialogTitle';
import { useTheme } from '@material-ui/core/styles';
import PostIcon from '@material-ui/icons/Send';
import { Redirect } from 'react-router-dom';
import placeholderImg from '../../assets/placeholder-image.png';

import { storage } from '../../firebase/index';
import { baseUrl } from '../../urlConstants.js';

const useStyles = makeStyles(theme => ({
    input:{
        margin: "2% 0"
    },
    previewImg:{
        textAlign: "center",
        fontWeight: "600",
        margin:"2% 0",
        border: "1px solid #DFDFDF",
        borderRadius: "15px",
        padding:"2% 5%",

            '& img':{
                    maxWidth:"100%",
                    maxHeight: "100%"
            }
    },
    imageInputLabel:{
        margin: "2% auto",
        width:"10%",
        textAlign:"center",
        padding: "10px",
        border: "1px solid #DFDFDF",
        color:"#FF3750",
        borderRadius: "10px",
        cursor:"pointer"
    },
    imageUploadField: {
        display: "none"
    },
    progressBar: {
        height: 10
    }
}));

const styles = (theme) => ({
    root: {
      margin: 0,
      padding: theme.spacing(2),
    },
    closeButton: {
      position: 'absolute',
      right: theme.spacing(1),
      top: theme.spacing(1),
      color: theme.palette.grey[500],
    }
  });

const AddStory = () => {

    const classes = useStyles();

    const [editing,setEditing] = React.useState(true);

    const [storyImageValues, setStoryImageValues] = React.useState(null);
    const [storyImageSelected, setStoryImageSelected] = React.useState(false);
    const [previewImageUpload, setPreviewImageUpload] = React.useState(false);
    const [imageUploading, setImageUploading] = React.useState(false);
    const [progress,setProgress] = React.useState(0);

    

    const [newStory, setNewStory] = React.useState({title:"", photoUrl:placeholderImg , description:""});
    const newStoryChange = (e) => {
        let id = e.target.id;
        setNewStory((prev)=>({
            ...prev,
            [id]:e.target.value
        }));
    }

    useEffect(()=>{
        console.log("changed");
        setPreviewImageUpload(false);
    },[newStory.photoUrl])

    const [open, setOpen] = React.useState(true);

    const imageFileInput = React.useRef(null);

    const theme = useTheme();
    const fullScreen = useMediaQuery(theme.breakpoints.down('sm'));
       const handleClickOpen = () => {
      setOpen(true);
    };
       const handleClose = () => {
        setOpen(false);
        setEditing(false);
    };

    const DialogTitle = withStyles(styles)((props) => {
        const { children, classes, onClose, ...other } = props;
        return (
          <MuiDialogTitle disableTypography className={classes.root} {...other}>
            <Typography variant="h5">{children}</Typography>
            {onClose ? (
              <IconButton aria-label="close" className={classes.closeButton} onClick={onClose}>
                <CloseIcon />
              </IconButton>
            ) : null}
          </MuiDialogTitle>
        );
      });
    

      const handleAddImageClick = (e) => {
          imageFileInput.current.click();
      }

      const handleStoryImage = (e) => {
          setPreviewImageUpload(true);
        if(e.target.files[0]){
            const reader = new FileReader();
            reader.onload = () => {
                if (reader.readyState === 2) {
                    setNewStory((prev)=>({
                        ...prev,
                        photoUrl: reader.result
                    }))
                }

                console.log("done");
            }
            reader.readAsDataURL(e.target.files[0]);
            setStoryImageValues(e.target.files[0]);
            setStoryImageSelected(true);
            console.log(e.target.files[0]);
        }
    }

    const postStory = async () => {
        // Uploading Image to Firebase first
        const uploadImage = storage.ref(`company/story/${storyImageValues.name.slice(0, -4)}_${storyImageValues.lastModified}`).put(storyImageValues);
        uploadImage.on(
            "state_changed",
            snapshot => {
                const progress = Math.round(
                    (snapshot.bytesTransferred/snapshot.totalBytes) * 100
                );
                setProgress((oldProgress) => {
                    if(oldProgress === 100){
                        return 0;
                    }
                    return progress;
                })
            },
            error => {
                console.log(error);
            },
            () => {
                storage
                    .ref("company/story")
                    .child(`${storyImageValues.name.slice(0,-4)}_${storyImageValues.lastModified}`)
                    .getDownloadURL()
                    .then(url => {
                        console.log(url);
                        setNewStory((prev)=>({
                            ...prev,
                            photoUrl: url
                        }))
                        setStoryImageSelected(false);
                        setImageUploading(false);
                    })
            }
        )
        setOpen(false);
        setEditing(false);

        // Proceed with API call here -
        const body = {
            name: newStory.title,
            description: newStory.description,
            photo_url: newStory.photoUrl
        };
        const key = localStorage.getItem('key');
        const requestOptions = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Access-Control-Allow-Origin': '*',
                'Authorization': `token ${key}`,
            },
            body: JSON.stringify(body)
        };
        const response = await fetch(`${baseUrl}/story/add/`, requestOptions);
        const data = await response.json();
        console.log(data);
    }

      

    return editing?(
        <div>
            <Dialog
                fullScreen={fullScreen}
                maxWidth="md"
                fullWidth
                open={open}
                aria-labelledby="responsive-dialog-title"
                className={classes.storyDialog}
            >
                <DialogTitle id="customized-dialog-title" onClose={handleClose}>
                    New Story
                </DialogTitle>

                <DialogContent dividers>
                    <TextField
                        className={classes.input}
                        id="title" label="Title"
                        value={newStory.title}
                        fullWidth 
                        onChange={newStoryChange}
                    />
                    <Button color="secondary" variant="outlined" onClick={handleAddImageClick}>
                        Add Image
                    </Button>
                    
                    <input 
                        accept="image/*"
                        name="storyImage"
                        type="file" 
                        id="imageInput" 
                        className={classes.imageUploadField}
                        ref={imageFileInput}
                        onChange={handleStoryImage}
                    />
                    
                    <div className={classes.previewImg}>
                        {previewImageUpload?(<CircularProgress color="secondary" />):
                        (<img
                            src={newStory.photoUrl} 
                            onError={(e)=>{e.target.src="/images/placeholder-image.png"}} 
                            alt="preview"
                        />)}
                        <Typography variant="caption" display="block">
                            Image Preview
                        </Typography>
                    </div>

                    <TextField
                        className={classes.input}
                        id="description"
                        label="Description"
                        multiline
                        rows={4}
                        fullWidth
                        // variant="outlined"
                        value={newStory.description}
                        onChange={newStoryChange}
                    />
                </DialogContent>

                <DialogActions>
                    <Button onClick={postStory} color="secondary">
                        <PostIcon />
                    </Button>
                </DialogActions>
                {progress>0 && <LinearProgress variant="determinate" color="secondary" value={progress} className={classes.progressBar}/>}
            </Dialog>
        </div>
    ):<Redirect to="/" />
}

export default AddStory;