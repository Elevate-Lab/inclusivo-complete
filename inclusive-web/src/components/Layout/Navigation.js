import React,{useState, useEffect, useLayoutEffect} from 'react'
import {useSelector, useDispatch} from 'react-redux'
import { baseUrl } from '../../urlConstants';
import clsx from 'clsx'
import {
    Link
} from 'react-router-dom'
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
    useTheme
} from '@material-ui/core'
import {
    AccountCircleTwoTone,
    DashboardOutlined,
    ArrowBackIosRounded,
    ArrowForwardIosRounded,
    MenuRounded,
    ClearRounded,
    ExitToAppRounded,
    Brightness1,
    Brightness4,
    Brightness7
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
    drawer: property => ({
        width: property.sidebarWidth,
        flexShrink: 0,
        color: '#ffffff',
    }),
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
    drawerItemText:{
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
    hide:{
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
    const property = {sidebarWidth, marginFromLeft}
    const classes = useStyles(property)

    const toggleSidebar = () => {
        setIsOpen(prevState => !prevState)
    }

    const [isMobileView, setIsMobileView] = useState(false)
    const [isMobileSidebarOpen, setIsMobileSidebarOpen] = useState(false)

    const toggleOnMobileView = () => {
        setIsMobileSidebarOpen(!isMobileSidebarOpen)
        setIsOpen(false)
    }

    // Check if user is employer or candidate
    const getUser = async () => {
        let authToken=1;
        if (localStorage.getItem('key')) {
            authToken = localStorage.getItem('key');
        }
        const requestOptions = {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'token '+authToken,
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
    useEffect (() => {
        console.log(darkMode)
    },[marginDetails])

    useLayoutEffect(() => {
        console.log(darkMode)
        getUser()
        setIsMobileView(checkWidthChange())
        let timeoutId = null;
        const resizeListener = () => {
            clearTimeout(timeoutId);
            timeoutId = setTimeout(() => {setIsMobileView(checkWidthChange())}, 100);
        };
        window.addEventListener('resize', resizeListener);
    
        return () => {
            window.removeEventListener('resize', resizeListener);
        }
    }, [])
   
    useEffect(() =>{
        if(isMobileView && isMobileSidebarOpen){
            setSidebarWidth(236)
            dispatch(mobileViewOpen())
        } else if(isMobileView && !isMobileSidebarOpen) {
            setSidebarWidth(0)
            dispatch(mobileViewClose())
        } else if(!isOpen){
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

    const handleLogout = async () => {
        await localStorage.removeItem('key')
        await localStorage.removeItem('userEmail')
        window.location.reload();
    }

    // display functions
    const list = (data) => {
        return(
            <Link to={data.route} >
                <ListItem button className={classes.drawerItem} key={data.name}>
                    <ListItemIcon style={{minWidth: '36px'}}>{data.icon}</ListItemIcon>
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
                        className={clsx(classes.logo,{
                            [classes.hide]: isOpen
                        })} 
                        alt='Logo'
                    />
                    <img 
                        src="/images/IncMini.svg" 
                        className={clsx(classes.logo,{
                            [classes.hide]: !isOpen
                        })} 
                        alt='Logo'
                    />
                    {
                        isMobileView && isMobileSidebarOpen && 
                        (
                            <IconButton onClick={toggleOnMobileView} style={{color: '#ffffff'}}>
                                <ClearRounded />
                            </IconButton>
                        )
                    }
                </div>
                <div style={{overflowY: "auto", overflowX: "hidden", marginBottom: '52px'}}>
                    <List>
                        {commonItems.map(data => {
                            return list(data)
                        })}
                    </List>
                    <Divider className={classes.divider} />
                    <List>
                        {
                            loading ? 
                                <div className={classes.sweetLoading}>
                                    <Loader loading={loading} color="#ffffff"/>
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
                    {!isMobileView && <Button onClick={toggleSidebar} className={classes.toggleButton}>
                        {!isOpen ?
                            <ArrowBackIosRounded style={{fontSize: '18px'}}/> :
                            <ArrowForwardIosRounded style={{fontSize: '18px'}}/>
                        }
                    </Button>}                 
                    
                    <List className={classes.drawerBottom} style={{ paddingTop: '0px'}}>
                        <ListItem button className={classes.drawerItem} onClick={handleLogout}>
                            <ListItemIcon style={{minWidth: '36px'}}><ExitToAppRounded className={classes.drawerIcon} /></ListItemIcon>
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
                        <div style={{flexGrow: 1}}>
                            <IconButton onClick={toggleOnMobileView}>
                                <MenuRounded />
                            </IconButton>
                        </div>
                    }
                    <div style={{flexGrow: 1}}>
                        <img 
                            src="/images/inclusivo.svg" 
                            className={clsx(classes.logo,{
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
