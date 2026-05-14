import axios from 'axios';

export const api = axios.create({ baseURL: import.meta.env.VITE_API_URL || 'http://localhost:3000' });

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('accessToken');
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

export function saveSession(response) {
  localStorage.setItem('accessToken', response.accessToken);
  localStorage.setItem('currentUser', JSON.stringify(response.user));
}
export function getCurrentUser() {
  const value = localStorage.getItem('currentUser');
  return value ? JSON.parse(value) : null;
}
export function clearSession() {
  localStorage.removeItem('accessToken');
  localStorage.removeItem('currentUser');
}
export function errorMessage(error) {
  return error?.response?.data?.message || error.message || 'Something went wrong';
}


export function getErrorMessage(error, fallback = "Something went wrong.") {
  const data = error?.response?.data;

  if (typeof data === "string") return data;
  if (data?.message) return data.message;
  if (data?.error) return data.error;

  return fallback;
}
