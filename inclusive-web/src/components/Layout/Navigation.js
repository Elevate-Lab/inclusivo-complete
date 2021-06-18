import React, { useState, useEffect, useLayoutEffect } from 'react'
import { useSelector, useDispatch } from 'react-redux'
import { baseUrl } from '../../urlConstants';
import clsx from 'clsx'
import {
    Link
} from 'react-router-dom'
// import '../../styles/sidebar.css'
import {
    AppBar,
    Toolbar,
    makeStyles,
    IconButton,
    Drawer,
    Divider,
    List,
    ListItem,
    ListItemIcon,
    ListItemText,
    Button,
    Typography,
    Icon,
    Collapse
} from '@material-ui/core'
import ExpandLess from '@material-ui/icons/ExpandLess';
import ExpandMore from '@material-ui/icons/ExpandMore';
import BookIcon from '@material-ui/icons/Book';
import {
    AccountCircleTwoTone,
    DashboardOutlined,
    ArrowBackIosRounded,
    ArrowForwardIosRounded,
    MenuRounded,
    ClearRounded,
} from '@material-ui/icons'
import {
    desktopViewOpen,
    mobileViewOpen,
    desktopViewClose,
    mobileViewClose,
    handleDarkMode
} from '../../actions/marginActions/actions'
import Loader from '../../assets/loader/loader';
import {
    commonItems,
    candidateItems,
    employerItems
} from './sidebarItems'
import Logout from '../../assets/Icons/LogoutIcon.svg';

const drawerIcon = {
    color: '#ffffff',
    fontSize: '16px'
}

const useStyles = makeStyles(theme => ({
    appbar: property => ({
        background: theme.background,
        height: '46px',
        boxShadow: '0px 0px 20px -5px rgba(0, 0, 0, 0.25)',
        width: `calc(100% - ${property.marginFromLeft}px)`,
        marginleft: property.marginFromLeft,
    }),
    toolbar: {
        height: '46px',
        minHeight: '46px',
        color: '#000000',
        justifyContent: 'flex-end'
    },
    bg: {
        zIndex: 1,
        position: "absolute",
        width: "100vw",
        height: "100vh"
    },
    drawer: property => ({
        width: property.sidebarWidth,
        flexShrink: 0,
        color: '#ffffff',
        zIndex: 2,
    }),
    scroll: {
        "&::-webkit-scrollbar":{
            width: "8px",
        },
        "&::-webkit-scrollbar-thumb":{
            background: "rgba(256,256,256,0.15)",
            backgroundClip: "content-box",
            border: '2px solid transparent',
            borderRadius: "10px"
        }
    },
    drawerPaper: property => ({
        width: property.sidebarWidth,
        background: '#212630',
        color: '#ffffff',
        overflowX: "hidden"
    }),
    drawerHeader: {
        height: '46px',
        minHeight: '46px',
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        position: 'sticky',
        top: 0,
        background: '#212630',
        borderBottom: 'solid #AEB6C7 1px',
        zIndex: 5
    },
    drawerItem: {
        margin: '10px 14px',
        height: '36px',
        width: `calc(100% - 28px)`,
        background: '#303746',
        borderRadius: '5px',
        color: '#ffffff'
    },
    drawerItemText: {
        fontSize: '0.8rem',
    },
    drawerIcon: {
        color: '#ffffff',
        fontSize: '16px'
    },
    drawerBottom: {
        borderTop: 'solid #AEB6C7 1px',
        background: '#212630',
        position: 'sticky',
        bottom: 0,
        paddingBottom: "0"
    },
    position: {
        position: 'absolute',
        background: '#212630',
        bottom: 0,
        zIndex: 5,
        width: '100%',
    },
    logo: {
        height: '16px'
    },
    divider: {
        background: '#AEB6C7'
    },
    hide: {
        display: 'none',
    },
    button: property => ({
        marginLeft: `${236}px`,
        marginTop: 56
    }),
    toggleButton: {
        minWidth: 0,
        width: `calc(100% - 28px)`,
        height: '36px',
        padding: '8px',
        margin: '10px 14px',
        color: '#ffffff',
        justifyContent: 'flex-end',
        alignContent: 'center'
    },
    sweetLoading: {
        display: "flex",
        justifyContent: "center"
    },
    nested: {
        paddingLeft: "30px"
    }
}))

const checkWidthChange = () => {
    if (window.innerWidth < 828) return true;
    else return false;
}

function Layout(props) {

    const [loading, setLoading] = useState(true)
    const [sidebarWidth, setSidebarWidth] = useState(236)
    const marginDetails = useSelector(state => state.marginDetails)

    const [isOpen, setIsOpen] = useState(false)
    const [isUserEmployer, setIsUserEmployer] = useState(false)

    const dispatch = useDispatch()

    const darkMode = marginDetails.darkMode
    const marginFromLeft = marginDetails.marginFromLeft
    const property = { sidebarWidth, marginFromLeft }
    const classes = useStyles(property)

    const toggleSidebar = () => {
        setIsOpen(prevState => !prevState)
    }

    const [isMobileView, setIsMobileView] = useState(false)
    const [isMobileSidebarOpen, setIsMobileSidebarOpen] = useState(false)
    const [listOpen, setListOpen] = useState(false)

    const toggleOnMobileView = () => {
        setIsMobileSidebarOpen(!isMobileSidebarOpen)
        setIsOpen(false)
    }

    // Check if user is employer or candidate
    const getUser = async () => {
        let authToken = 1;
        if (localStorage.getItem('key')) {
            authToken = localStorage.getItem('key');
        }
        const requestOptions = {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'token ' + authToken,
            },
        };
        const response = await fetch(`${baseUrl}/user/get/0`, requestOptions);
        const data = await response.json();
        setIsUserEmployer(prevState => {
            return !data.data ? prevState : data.data.employer ? true : false;
        })
        setLoading(false)
    }

    // Effects
    useEffect(() => {
        // console.log(darkMode)
    }, [marginDetails])

    useLayoutEffect(() => {
        // console.log(darkMode)
        getUser()
        setIsMobileView(checkWidthChange())
        let timeoutId = null;
        const resizeListener = () => {
            clearTimeout(timeoutId);
            timeoutId = setTimeout(() => { setIsMobileView(checkWidthChange()) }, 100);
        };
        window.addEventListener('resize', resizeListener);

        return () => {
            window.removeEventListener('resize', resizeListener);
        }
    }, [])

    useEffect(() => {
        if (isMobileView && isMobileSidebarOpen) {
            setSidebarWidth(236)
            dispatch(mobileViewOpen())
        } else if (isMobileView && !isMobileSidebarOpen) {
            setSidebarWidth(0)
            dispatch(mobileViewClose())
        } else if (!isOpen) {
            setSidebarWidth(236)
            dispatch(desktopViewOpen())
        } else {
            setSidebarWidth(76)
            dispatch(desktopViewClose())
        }
    }, [isMobileView, isMobileSidebarOpen, isOpen])

    // handlers
    const changeDarkMode = () => {
        dispatch(handleDarkMode(darkMode));
    }

    const handleTouchItem = () => {
        if (isMobileView) {
            //console.log("Hey!")
            setIsMobileSidebarOpen(false)
        }
    }

    const handleOpenItem = () => {
        setListOpen(!listOpen);
    }

    const handleLogout = async () => {
        await localStorage.removeItem('key')
        await localStorage.removeItem('userEmail')
        window.location.reload();
    }

    // display functions
    const list = (data, key) => {
        return (
            <Link to={data.route} key={key}>
                <ListItem button className={classes.drawerItem} key={data.name} onClick={handleTouchItem}>
                    <ListItemIcon style={{ minWidth: '36px' }}>{data.icon}</ListItemIcon>
                    <ListItemText className={clsx({
                        [classes.hide]: isOpen
                    })}>
                        <Typography className={classes.drawerItemText}>
                            {data.name}
                        </Typography>
                    </ListItemText>
                </ListItem>
            </Link>
        )
    }

    return (
        <>
            {/* Sidebar */}
            <div className={(isMobileView && isMobileSidebarOpen) ? classes.bg : ""} onClick={handleTouchItem}></div>
            <Drawer
                anchor='left'
                className={classes.drawer}
                variant="persistent"
                {
                ...(isMobileView ?
                    {
                        open: isMobileSidebarOpen
                    }
                    :
                    {
                        open: true
                    })
                }
                classes={{
                    paper: classes.drawerPaper,
                }}
                containerstyle={{ transform: 'none' }}
            >
                <div className={classes.drawerHeader}>
                    <img
                        src="/images/Group.svg"
                        className={clsx(classes.logo, {
                            [classes.hide]: isOpen
                        })}
                        alt='Logo'
                    />
                    <img
                        src="/images/IncMini.svg"
                        className={clsx(classes.logo, {
                            [classes.hide]: !isOpen
                        })}
                        alt='Logo'
                    />
                    {
                        isMobileView && isMobileSidebarOpen &&
                        (
                            <IconButton onClick={toggleOnMobileView} style={{ color: '#ffffff' }}>
                                <ClearRounded />
                            </IconButton>
                        )
                    }
                </div>
                <div className={classes.scroll} style={{ overflowY: "auto", overflowX: "hidden", marginBottom: isMobileView ? '52px' : '120px' }}>
                    <List>
                        {commonItems.map((data, key) => {
                            return list(data, key)
                        })}
                        {
                            loading ?
                                null
                                :
                                isUserEmployer ?
                                    <React.Fragment>
                                        <ListItem button onClick={handleOpenItem} className={clsx(classes.drawerItem, {
                                                [classes.hide]: isOpen
                                            })}>
                                            <ListItemIcon style={{ minWidth: '36px' }}><BookIcon style={drawerIcon} /></ListItemIcon>
                                            <ListItemText >
                                                <Typography className={classes.drawerItemText}>
                                                    Diversity Training
                                                </Typography>
                                            </ListItemText>
                                            {listOpen ? <ExpandLess /> : <ExpandMore />}
                                        </ListItem>
                                        <Collapse in={listOpen} timeout="auto" unmountOnExit className={clsx({
                                                [classes.hide]: isOpen
                                            })}>
                                            <List>
                                                <Link to={'/home/blog/list'} style={{color: "#ffffff"}}>
                                                    <ListItem button className={classes.nested}>
                                                        <ListItemText>
                                                            <Typography className={classes.drawerItemText}>
                                                                Blogs
                                                            </Typography>
                                                        </ListItemText>
                                                    </ListItem>
                                                </Link>
                                                <Link to={'/home/video/list'} style={{color: "#ffffff"}}>
                                                    <ListItem button className={classes.nested}>
                                                        <ListItemText>
                                                            <Typography className={classes.drawerItemText}>
                                                                Videos
                                                            </Typography>
                                                        </ListItemText>
                                                    </ListItem>
                                                </Link>
                                            </List>
                                        </Collapse>
                                    </React.Fragment>
                                    :
                                    <React.Fragment>
                                        <ListItem button onClick={handleOpenItem} className={clsx(classes.drawerItem, {
                                                [classes.hide]: isOpen
                                            })}>
                                            <ListItemIcon style={{ minWidth: '36px' }}><BookIcon style={drawerIcon} /></ListItemIcon>
                                            <ListItemText className={clsx({
                                                [classes.hide]: isOpen
                                            })}>
                                                <Typography className={classes.drawerItemText}>
                                                    Upskill Module
                                                </Typography>
                                            </ListItemText>
                                            {listOpen ? <ExpandLess /> : <ExpandMore />}
                                        </ListItem>
                                        <Collapse in={listOpen} timeout="auto" unmountOnExit className={clsx({
                                                [classes.hide]: isOpen
                                            })}>
                                            <List>
                                                <Link to={'/home/blog/list'} style={{color: "#ffffff"}}>
                                                    <ListItem button className={classes.nested}>
                                                        <ListItemText>
                                                            <Typography className={classes.drawerItemText}>
                                                                Blogs
                                                            </Typography>
                                                        </ListItemText>
                                                    </ListItem>
                                                </Link>
                                                <Link to={'/home/video/list'} style={{color: "#ffffff"}}>
                                                    <ListItem button className={classes.nested}>
                                                        <ListItemText>
                                                            <Typography className={classes.drawerItemText}>
                                                                Videos
                                                            </Typography>
                                                        </ListItemText>
                                                    </ListItem>
                                                </Link>
                                            </List>
                                        </Collapse>
                                    </React.Fragment>
                        }
                    </List>
                    <Divider className={classes.divider} />
                    <List>
                        {
                            loading ?
                                <div className={classes.sweetLoading}>
                                    <Loader loading={loading} color="#ffffff" />
                                </div>
                                :
                                isUserEmployer ?
                                    employerItems.map(data => {
                                        return list(data)
                                    })
                                    :
                                    candidateItems.map(data => {
                                        return list(data)
                                    })
                        }
                    </List>
                </div>

                <div className={classes.position}>
                    {!isMobileView && <Button onClick={toggleSidebar} className={classes.toggleButton} ariaLabel="Arrow">
                        {!isOpen ?
                            <ArrowBackIosRounded style={{ fontSize: '18px' }} /> :
                            <ArrowForwardIosRounded style={{ fontSize: '18px' }} />
                        }
                    </Button>}

                    <List className={classes.drawerBottom} style={{ paddingTop: '0px' }}>
                        <ListItem button className={classes.drawerItem} onClick={handleLogout}>
                            <ListItemIcon style={{ minWidth: '36px' }}><Icon className={classes.drawerIcon}><img src={Logout} alt="logout" /></Icon></ListItemIcon>
                            <ListItemText className={clsx({
                                [classes.hide]: isOpen
                            })}>
                                <Typography className={classes.drawerItemText}>
                                    Logout
                                </Typography>
                            </ListItemText>
                        </ListItem>
                    </List>
                </div>

            </Drawer>

            {/* AppBar */}
            <AppBar color={`pallete.text`} className={classes.appbar}>
                <Toolbar className={classes.toolbar}>
                    {isMobileView && !isMobileSidebarOpen &&
                        <div style={{ flexGrow: 1 }}>
                            <IconButton onClick={toggleOnMobileView}>
                                <MenuRounded />
                            </IconButton>
                        </div>
                    }
                    <div style={{ flexGrow: 1 }}>
                        <img
                            src="/images/inclusivo.svg"
                            className={clsx(classes.logo, {
                                [classes.hide]: !(isMobileView && !isMobileSidebarOpen)
                            })}
                            alt='Logo'
                        />
                    </div>
                    <div>
                        <Link to='/home/profile'>
                            <IconButton>
                                <AccountCircleTwoTone />
                            </IconButton>
                        </Link>
                    </div>
                </Toolbar>
            </AppBar>
        </>
    )
}

export default Layout
