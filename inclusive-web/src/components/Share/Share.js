import React from 'react';
import { withStyles, makeStyles } from '@material-ui/core/styles';
import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import MuiDialogTitle from '@material-ui/core/DialogTitle';
import MuiDialogContent from '@material-ui/core/DialogContent';
import MuiDialogActions from '@material-ui/core/DialogActions';
import IconButton from '@material-ui/core/IconButton';
import CloseIcon from '@material-ui/icons/Close';
import Typography from '@material-ui/core/Typography';
import {
    EmailShareButton,
    FacebookShareButton,
    RedditShareButton,
    TelegramShareButton,
    FacebookMessengerShareButton,
    LinkedinShareButton,
    TwitterShareButton,
    WhatsappShareButton,
    FacebookIcon,
    EmailIcon,
    FacebookMessengerIcon,
    LinkedinIcon,
    RedditIcon,
    TelegramIcon,
    TwitterIcon,
    WhatsappIcon,
} from "react-share";

const useStyles = makeStyles(theme => ({
    shareBtn: {
        margin: "4px",
    },
    shareBtnIcon: {
        borderRadius :"5px",
        height: "40px",
        width: "40px"
    }
}))

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
    },
});

const DialogTitle = withStyles(styles)((props) => {
    const { children, classes, onClose, ...other } = props;
    return (
        <MuiDialogTitle disableTypography className={classes.root} {...other}>
            <Typography variant="h6">{children}</Typography>
            {onClose ? (
                <IconButton aria-label="close" className={classes.closeButton} onClick={onClose}>
                    <CloseIcon />
                </IconButton>
            ) : null}
        </MuiDialogTitle>
    );
});

const DialogContent = withStyles((theme) => ({
    root: {
        padding: theme.spacing(2),
    },
}))(MuiDialogContent);

const DialogActions = withStyles((theme) => ({
    root: {
        margin: 0,
        padding: theme.spacing(1),
    },
}))(MuiDialogActions);

const Share = ({ open, url, handleClose }) => {
    const classes = useStyles()

    return (
        <Dialog onClose={handleClose} aria-labelledby="customized-dialog-title" open={open}>
            <DialogTitle id="customized-dialog-title" onClose={handleClose}>
                Share to
            </DialogTitle>
            <DialogContent dividers>
                <FacebookShareButton
                    url={url}
                    className={classes.shareBtn}
                >
                    <FacebookIcon className={classes.shareBtnIcon}/>
                </FacebookShareButton>
                <FacebookMessengerShareButton
                    url={url}
                    className={classes.shareBtn}
                >
                    <FacebookMessengerIcon className={classes.shareBtnIcon}/>
                </FacebookMessengerShareButton>
                <WhatsappShareButton
                    url={url}
                    className={classes.shareBtn}
                >
                    <WhatsappIcon className={classes.shareBtnIcon}/>
                </WhatsappShareButton>
                <LinkedinShareButton
                    url={url}
                    className={classes.shareBtn}
                >
                    <LinkedinIcon className={classes.shareBtnIcon}/>
                </LinkedinShareButton>
                <EmailShareButton
                    url={url}
                    className={classes.shareBtn}
                >
                    <EmailIcon className={classes.shareBtnIcon}/>
                </EmailShareButton>
                <TelegramShareButton
                    url={url}
                    className={classes.shareBtn}
                >
                    <TelegramIcon className={classes.shareBtnIcon}/>
                </TelegramShareButton>
                <TwitterShareButton
                    url={url}
                    className={classes.shareBtn}
                >
                    <TwitterIcon className={classes.shareBtnIcon}/>
                </TwitterShareButton>
            </DialogContent>
        </Dialog>
    )
}

export default Share