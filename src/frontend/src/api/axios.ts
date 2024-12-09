import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8080", // Adjust this URL to match your backend
});

export default api;
