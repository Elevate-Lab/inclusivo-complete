import { Box, Hidden } from '@material-ui/core'
import React from 'react'
import { SideBar } from './SideBar'
import { SocialHadles } from './SocialHandles'

export const Header = () => {
  return (
    <>
      <Box className="container-row" style={{ justifyContent: "space-between", padding: "10px" }}>
        <Box flex="1 1 0" ><SideBar /></Box>
        <Box flex="1 1 0" className="container-row" justifyContent="center">
          <img className="headerLogo" src="/images/inclusivo.svg" alt="logo" />
        </Box>
        
        <Box flex="1 1 0" style={{textAlign:"right"}}><Hidden xsDown><SocialHadles /></Hidden></Box>
      </Box>
    </>
  )
}
