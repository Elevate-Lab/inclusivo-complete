import React from 'react'
import { baseUrl } from '../../urlConstants'
import Company from './Company'
import { Grid, Box, Snackbar } from '@material-ui/core'
import { Link } from 'react-router-dom'
import { Alert } from '@material-ui/lab'
import CommonCardSkeleton from '../Loaders/CommonCardSkeleton'

const Subscribed = () => {
  const [data, setData] = React.useState([])
  const [isError, setIsError] = React.useState(false)
  const [loading, setLoading] = React.useState(false)
  const [errMessage, setError] = React.useState('')
  const handleCloseSnackBar = () => {
    setIsError(false)
  }

  const getCompanies = async () => {
    let key = localStorage.getItem('key')
    const requestOptions = {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `token ${key}`,
      },
    }

    setLoading(true)

    await fetch(`${baseUrl}/dashboard/get/subscribed_companies`, requestOptions)
      .then((res) => res.json())
      .then((res) => {
        console.log(res)
        if (res.status === 'error') {
          setIsError(true)
          setError(res.message)
        } else {
          setIsError(false)
          setData(res.data)
        }
        setLoading(false)
      })
      .catch((err) => {
        console.log(err)
        setIsError(true)
        setLoading(false)
      })
  }

  React.useEffect(() => {
    getCompanies()
  }, [])

  return (
    <>
      {loading && !isError ? (
        <CommonCardSkeleton type="scholarship" />
      ) : !loading && data.length === 0 && !isError ? (
        <span>Nothing Subscribed</span>
      ) : (
        <Grid container style={{ marginTop: '1rem' }}>
          {data.length > 0 &&
            data.map((company, idx) => (
              <Grid item sm={6} xs={12} md={4} lg={3} key={idx}>
                <Link
                  to={`/home/company/${company.subscribed_company.company.id}`}
                >
                  <Box
                    style={{
                      border: '1px solid #989898',
                      margin: '0 1rem 1rem 1rem',
                      borderRadius: '5px',
                      padding: '1.5rem',
                    }}
                  >
                    <Company company={company.subscribed_company.company} />
                  </Box>
                </Link>
              </Grid>
            ))}
        </Grid>
      )}
      {!loading && isError && (
        <Snackbar
          open={isError}
          autoHideDuration={6000}
          onClose={handleCloseSnackBar}
        >
          <Alert
            onClose={handleCloseSnackBar}
            severity="error"
            variant="filled"
          >
            {errMessage}
          </Alert>
        </Snackbar>
      )}
    </>
  )
}

export default Subscribed
