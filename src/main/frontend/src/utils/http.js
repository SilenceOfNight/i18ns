import axios from 'axios';

const instance = axios.create();
instance.interceptors.response.use(
  response => {
    const { status, data } = response;

    if (200 <= status && 300 > status) {
      return { data };
    }
    const error = new Error('Failed to request.');
    error.response = response;
    return { error };
  },
  error => {
    return { error };
  }
);

export default instance;
