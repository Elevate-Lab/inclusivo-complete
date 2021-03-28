import axios from "axios";
import { baseUrl } from "../urlConstants";

let token;
if(localStorage.getItem('key')) 
 token = localStorage.getItem("key");

const axiosIntance = axios.create({
  baseURL: baseUrl,
  headers: {
    'Authorization': token ? `token ${token}` : "",
    'Content-Type': 'application/json',
    'Access-Control-Allow-Origin': '*',
  }

});

export default axiosIntance;