import { CssBaseline } from "@material-ui/core";
import { createMuiTheme } from "@material-ui/core/styles";
import { ThemeProvider } from "@material-ui/styles";
import * as React from "react";
import { useSelector } from "react-redux";


// A theme with custom primary and secondary color.
// It's optional.


const lightTheme = createMuiTheme({
  breakpoints: {
    values: {
      xs: 0,
      sm: 600,
      md: 900,
      lg: 1200,
      xl: 1800,
    },
  },
  typography: {
    // ...typography,
    // Tell Material-UI what the font-size on the html element is.
    // htmlFontSize: 10, // 1 rem = 10px, 10/16 = 62.5%

    // "@include respond(tab-land)": {
    //   // width < 1200?
    //   htmlFontSize: 9, //1 rem = 9px, 9/16 = 56.25%
    // },

    // "@include respond(tab-port)": {
    //   // width < 900?
    //   htmlFontSize: 8, //1 rem = 8px, 8/16 = 50%
    // },

    // "@include respond(big-desktop)": {
    //   htmlFontSize: 12, //1rem = 12, 12/16 = 75%
    // },
    // h1: {
    //   fontSize: "3.2rem",
    //   lineHeight: "3.5rem",
    // },
    // h2: {
    //   fontWeight: "500",
    //   fontSize: "2rem",
    //   lineHeight: "2.2rem",
    //   color: "#000",
    //   textTransform: "none",
    // },
    // h3: {
    //   fontFamily: "Ramabhadra",
    //   fontSize: "1.6rem",
    //   lineHeight: "3rem",
    //   letterSpacing: "0.195em",
    //   textTransform: 'uppercase'
    // },
    // h4: {
    //   fontWeight: "500",
    //   fontSize: "1.5rem",
    //   lineHeight: "1.8rem",
    // },
    // h5: {
    //   fontWeight: "500",
    //   fontSize: "1.4rem",
    //   lineHeight: "1.6rem",
    // },
    // h6: {
    //   fontWeight: "500",
    //   fontSize: "1.2rem",
    //   lineHeight: "1.4rem",
    // },
    fontSize: 16,
    fontFamily: ['Inter', 'sans-serif'],
    fontWeightLight: "normal",
    fontWeightMedium: "bold",
    fontWeightBold: "bolder",
    fontWeightRegular: "normal",
  },
  palette: {
    primary: {
      main: "#06B0C5",
      contrastText: "#fff",
    },
    secondary: {
      main: "#4AA64E",
      contrastText: "#fff",
    },
  },
  
  overrides: {
    MuiCssBaseline: {
      "@global": {
          body: {
            //FOr variable in body
            backgroundColor: "#fff"
          }
        }
      },
      
    MuiInputBase: {
      input: {
        fontSize: 16,
      },
    },
    MuiButton: {
      root: {
        padding: '1rem'
      }
    },
    MuiIconButton: {
      root: {
        padding: '1rem'
      }
    },
    MuiDialog: {
      paper: {
        borderRadius: '3px !important'
      }
    },
    
    
    MuiTab: {
      root: {},
      wrapper: {},
    },
  },
});

  const darkTheme = createMuiTheme({
    // breakpoints: {
    //     values: {
    //       xs: 0,
    //       sm: 600,
    //       md: 900,
    //       lg: 1200,
    //       xl: 1800,
    //     },
    //   },
      typography: {
        
        fontSize: 16,
        fontFamily: ['Inter', 'sans-serif'],
        fontWeightLight: "normal",
        fontWeightMedium: "bold",
        fontWeightBold: "bolder",
        fontWeightRegular: "normal",
      },
      // palette: {
      //   type:'dark',
      //   primary: {
      //     main: "#FF3750",
      //     contrastText: "#fff",
      //   },
      //   secondary: {
      //     main: "#3668CC",
      //     contrastText: "#fff",
      //   },
      // },
      overrides: {
        MuiCssBaseline: {
          "@global": {
              body: {
                //FOr variable in body
                backgroundColor: "#fff"
              }
            }
          },
      MuiInputBase: {
        input: {
          fontSize: 16,
        },
      },
      MuiButton: {
        root: {
          padding: '1rem'
        }
      },
      MuiIconButton: {
        root: {
          padding: '1rem'
        }
      },
      MuiDialog: {
        paper: {
          borderRadius: '3px !important'
        }
      },
    }
      

  })

const choosedTheme = 0;
//Make it one and save, to see the dark Modeee!!!
export function withRoot(Component) {
  function WithRoot(props) {
    const darkMode = useSelector(state => state.marginDetails.darkMode)
    
    return (
      <ThemeProvider theme={darkMode ? darkTheme : lightTheme}>
        {/* Reboot kickstart an elegant, consistent, and simple baseline to build upon. */}
        <CssBaseline />
        <Component {...props} {...choosedTheme ? darkTheme : lightTheme} />
      </ThemeProvider>
    );
  }

  return WithRoot;
}
