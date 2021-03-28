import { useState } from "react";
import { css } from "@emotion/css";
import {PuffLoader} from "react-spinners";

// Can be a string as well. Need to ensure each key-value pair ends with ;
const override = css`
  display: flex;
  width: "50px"
  margin: 50px auto;
  border-color: red;
`;


function Loader(props) {
    let [color, setColor] = useState("#000000");

    return (
      <PuffLoader color="#c2c2c2" loading={true} css={override} size={50} />
    );
}

export default Loader;