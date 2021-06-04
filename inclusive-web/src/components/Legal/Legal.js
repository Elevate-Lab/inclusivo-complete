import React from 'react'
import ReactMarkdown from 'react-markdown'
import gfm from 'remark-gfm'
import clsx from 'clsx'
import {
    Grid,
    Typography,
    makeStyles
} from '@material-ui/core'
import {
    CloseRounded,
    MenuRounded
} from '@material-ui/icons'
import inclusivo from '../../assets/inclusivo.svg'
import UserAgreementMd from '../../assets/Legal/user_agreement.md'
import PrivacyPolicyMd from '../../assets/Legal/privacy_policy.md'
import CookiePolicyMd from '../../assets/Legal/cookie_policy.md'
import CopyrightPolicyMd from '../../assets/Legal/copyright_policy.md'

const useStyles = makeStyles((theme) => ({
    header: {
        height: "128px",
        padding: "30px 80px",
        boxShadow: "0.5px 0.5px 14px -4px rgba(0,0,0,0.4)",
        position: "relative"
    },
    mobileHeader: {
        padding: "16px",
        height: "64px"
    },
    openMenu: {
        height: "100%"
    },
    logoContainer: {
        paddingBottom:"20px"
    },
    logo: {
        height: "20px"
    },
    navList:{
        display: "flex",
    },
    navItems: {
        margin:"10px 20px",
        color: "rgba(0,0,0,0.5)",
    },
    closeBtn: {
        position: "absolute",
        right: "16px",
        top: "16px",
        height: "28px",
        width: "28px",
        backgroundColor: "#fafafa"
    },
    hide: {
        display: "none !important"
    },
    selected: {
        color: "rgba(0,0,0,0.8)"
    }
}))

const checkWidthChange = () => {
    if (window.innerWidth < 960) return true;
    else return false;
}

function Legal() {
    const classes = useStyles();

    // const [markdown, setMarkdown] = React.useState("Markdown")
    const [isMobileView, setIsMobileView] = React.useState(false)
    const [isOpen, setIsOpen] = React.useState(false)
    const [UserAgreement, setUserAgreement] = React.useState("Loading")
    const [PrivacyPolicy, setPrivacyPolicy] = React.useState("Loading")
    const [CookiePolicy, setCookiePolicy] = React.useState("Loading")
    const [CopyrightPolicy, setCopyrightPolicy] = React.useState("Loading")
    const [tabValue, setTabValue] = React.useState(0)

    // const handleChange = (e) => {
    //     setMarkdown(e.target.value);
    // }
    
    const handleClick = (value) => () => {
        setIsOpen(false)
        setTabValue(value)
        console.log(value)
    }

    const toggleMenu = () => {
        setIsOpen(!isOpen)
    }

    const markdown = (value) => {
        switch(value){
            case 0: 
                return UserAgreement
            case 1: 
                return PrivacyPolicy
            case 2: 
                return CookiePolicy
            case 3: 
                return CopyrightPolicy
        }
    }

    React.useLayoutEffect(() => {
        // console.log(darkMode)
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

    React.useEffect(()=> {
        fetch(UserAgreementMd)
        .then(response => {
            return response.text()
        })
        .then(text => {
            setUserAgreement(text)
        })

        fetch(PrivacyPolicyMd)
        .then(response => {
            return response.text()
        })
        .then(text => {
            setPrivacyPolicy(text)
        })

        fetch(CookiePolicyMd)
        .then(response => {
            return response.text()
        })
        .then(text => {
            setCookiePolicy(text)
        })

        fetch(CopyrightPolicyMd)
        .then(response => {
            return response.text()
        })
        .then(text => {
            setCopyrightPolicy(text)
        })
    }, [])

    React.useEffect(() => {
        console.log(isMobileView)
        setIsOpen(isMobileView ? isOpen : false)
    }, [isMobileView])

    const tabsData = [
        {
            value: 0,
            name: "USER AGREEMENT"
        },
        {
            value: 1,
            name: "PRIVACY POLICY",
        },
        {
            value: 2,
            name: "COOKIE POLICY"
        },
        {
            value: 3,
            name: "COPYRIGHT POLICY"
        },
    ]

    return (
        <Grid container xs={12}>
            <Grid item container xs={12} justify={'center'} alignItems={'center'} className={clsx(classes.header, {[classes.mobileHeader]: isMobileView, [classes.openMenu]: isOpen})}>
                <Grid item container xs={12} className={classes.logoContainer} justify={isMobileView ? 'center' : 'flex-start'} alignItems={'center'}>
                    <img src={inclusivo} 
                        className={clsx(classes.logo,{
                            [classes.display]: isOpen && isMobileView
                        })}                    
                    />
                    <Grid 
                        item 
                        container 
                        justify='center' 
                        alignItems='center' 
                        className={clsx(classes.closeBtn,{
                            [classes.hide]: !(isMobileView && isOpen)
                        })}
                        onClick={toggleMenu}
                    >
                        <CloseRounded />
                    </Grid>
                    <Grid 
                        item 
                        container 
                        justify='center' 
                        alignItems='center' 
                        className={clsx(classes.closeBtn,{
                            [classes.hide]: !(isMobileView && !isOpen)
                        })}
                        onClick={toggleMenu}
                    >
                        <MenuRounded />
                    </Grid>
                </Grid>
                <Grid item container xs={12} justify={'center'} alignItems={'center'} className={clsx(classes.navList,{
                   [classes.hide]: isMobileView && !isOpen
                })}>
                    {tabsData.map((tab)=>{
                        return(
                            <Grid 
                                item 
                                className={clsx(classes.navItems,{
                                    [classes.selected]: tab.value===tabValue
                                })}
                                onClick={handleClick(tab.value)} 
                            >
                                <Typography variant="h6">
                                    {tab.name}
                                </Typography>
                            </Grid>
                        )
                    })}
                </Grid>
            </Grid>
            <Grid container item justify='center'>
                <Grid item xs={10}>
                    <ReactMarkdown remarkPlugins={[gfm]} children={markdown(tabValue)}/>
                </Grid>
            </Grid>
        </Grid>
    )
}

export default Legal
