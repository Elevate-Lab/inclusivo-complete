import { Facebook, Instagram, LinkedIn, Twitter, YouTube } from '@material-ui/icons';
import { IconButton, makeStyles } from '@material-ui/core';

const useStyles = makeStyles({
  socialIcons: {
    fontSize: 24
  }
})

export const SocialHadles = () => {
  const classes = useStyles();
  return (
    <>
      <IconButton style={{ color: 'black' }}><Instagram className={classes.socialIcons} /></IconButton>
      <IconButton style={{ color: '#4968ad' }}><Facebook className={classes.socialIcons} /></IconButton>
      <IconButton style={{ color: '#49a1eb' }}><Twitter className={classes.socialIcons} /></IconButton>
      <IconButton style={{ color: '#2867B2' }}><LinkedIn className={classes.socialIcons} /></IconButton>
    </>
  )
}
