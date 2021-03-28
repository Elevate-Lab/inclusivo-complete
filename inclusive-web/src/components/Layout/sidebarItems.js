import React from 'react'
import {
    DashboardOutlined,
    WorkOutlineOutlined,
    SchoolOutlined,
    BusinessOutlined,
    LibraryBooksOutlined,
} from '@material-ui/icons'

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
    }
]

export const candidateItems = [
    {
        name: 'Liked Jobs',
        icon: <WorkOutlineOutlined style={drawerIcon} />,
        route: '/home/job/liked'
    },
    {
        name: 'Subscribed Companies',
        icon: <BusinessOutlined style={drawerIcon} />,
        route: '/home/company/get/subscribed'
    },
    {
        name: 'Liked Scholarships',
        icon: <SchoolOutlined style={drawerIcon} />,
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