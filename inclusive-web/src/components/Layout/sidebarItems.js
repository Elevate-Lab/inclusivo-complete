import React from 'react'
import {
    DashboardOutlined,
    WorkOutlineOutlined,
    SchoolOutlined,
    BusinessOutlined,
    LibraryBooksOutlined,
} from '@material-ui/icons';
import VideoCallIcon from '@material-ui/icons/VideoCall';
import BookIcon from '@material-ui/icons/Book';

import {
    Icon
} from '@material-ui/core';
import SavedScholarship from '../../assets/Icons/SavedScholarshipIcon.svg';
import LikedJobs from '../../assets/Icons/LikedJobsIcon.svg';

const drawerIcon = {
    color: '#ffffff',
    fontSize: '16px'
}

export const commonItems = [
    {
        name: 'Dashboard',
        icon: <DashboardOutlined style={drawerIcon} />,
        route: '/home'
    },
    {
        name: 'Jobs',
        icon: <WorkOutlineOutlined style={drawerIcon} />,
        route: '/home/job/list'
    },
    {
        name: 'Companies',
        icon: <BusinessOutlined style={drawerIcon} />,
        route: '/home/company/list'
    },
    {
        name: 'Stories',
        icon: <LibraryBooksOutlined style={drawerIcon} />,
        route: '/home/story/list'
    },
    {
        name: 'Scholarships',
        icon: <SchoolOutlined style={drawerIcon} />,
        route: '/home/scholarship/list'
    },
]

export const candidateItems = [
    {
        name: 'Liked Jobs',
        icon: <Icon style={drawerIcon}> <img src={LikedJobs} alt="liked jobs" /></Icon>,
        route: '/home/job/liked'
    },
    {
        name: 'Subscribed Companies',
        icon: <BusinessOutlined style={drawerIcon} />,
        route: '/home/company/get/subscribed'
    },
    {
        name: 'Liked Scholarships',
        icon: <Icon style={drawerIcon}><img src={SavedScholarship} alt="saved scholarship" /></Icon>,
        route: '/home/scholarship/liked'
    }
]

export const employerItems = [
    {
        name: 'Add Jobs',
        icon: <WorkOutlineOutlined style={drawerIcon} />,
        route: '/home/job/add'
    },
    {
        name: 'Add Scholarship',
        icon: <SchoolOutlined style={drawerIcon} />,
        route: '/home/scholarship/add'
    },
    {
        name: 'Add Story',
        icon: <LibraryBooksOutlined style={drawerIcon} />,
        route: '/home/story/add'
    }
]