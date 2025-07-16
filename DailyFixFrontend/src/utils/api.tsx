import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8080",
  withCredentials: true, // So cookies/session from Spring Boot are used
});

export default api;
