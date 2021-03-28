import React,{useState} from 'react';
import { makeStyles } from '@material-ui/core/styles';
import SwipeableDrawer from '@material-ui/core/Drawer';
import Button from '@material-ui/core/Button';
import {List, ListItem, ListItemText, Divider} from '@material-ui/core/';
import MenuIcon from '@material-ui/icons/Menu';
import { SocialHadles } from './SocialHandles';

export const SideBar = () => {
  const [sideDrawer, setSideDrawer] = useState(false);
  const useStyles = makeStyles({
    list: {
      width: 300,
    },
    fullList: {
      width: 'auto',
    },
    BackdropProps: {
      background: 'rgba(0, 0, 0, 0.1)'
    }
  });
  const classes = useStyles();
  const toggleDrawer = ( open) => (event) => {
    if (event && event.type === 'keydown' && (event.key === 'Tab' || event.key === 'Shift')) {
      return;
    }

    setSideDrawer( open );
  };
  const list = () => (
    <div
      className={classes.list}
      role="presentation"
      onClick={toggleDrawer( false)}
      onKeyDown={toggleDrawer ( false)}
    >
      <List>
        {['Profile', 'Companies', 'Jobs', 'Applications'].map((text, index) => (
          <ListItem button key={text}>
            <ListItemText primary={text} />
          </ListItem>
        ))}
      </List>
      <Divider />
      <List>
        {['Contact Us', 'Follow Us', 'No Dream Too Big'].map((text, index) => (
          <ListItem button key={text}>
            <ListItemText primary={text} />
          </ListItem>
        ))}
      </List>
      <SocialHadles />
    </div>
  );

  return (
    <>
      <Button onClick={toggleDrawer(true)} style={{marginLeft:"0"}}><MenuIcon style={{fontSize: "30px" ,color: "black" }} /></Button>
      <SwipeableDrawer
        ModalProps={{
          BackdropProps: {
            classes: {
              root: classes.BackdropProps
            }
          }
        }}
        anchor="left"
        open={sideDrawer}
        onClose={toggleDrawer(false)}
        onOpen={toggleDrawer(true)}
      >
        {list()}
      </SwipeableDrawer>
    </>
  )
}