import React from 'react'
import {Snackbar} from '@material-ui/core'

function SubmitStatusSnackbar({open, processedMsg, handleClose}) {
    return (
        <Snackbar
            anchorOrigin={{ vertical:"bottom", horizontal:"center" }}
            open={open}
            onClose={handleClose}
            message={processedMsg}
      />
    )
}

export default SubmitStatusSnackbar
