import React from 'react';
import { createRoot } from 'react-dom/client';
import { Toaster } from 'react-hot-toast';
import App from './App.jsx';
import './styles.css';

createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <App />
    <Toaster
      position="top-right"
      toastOptions={{
        duration: 5000,
        style: {
          fontWeight: 700,
          padding: '16px 18px',
          borderRadius: '12px',
          boxShadow: '0 12px 30px rgba(0,0,0,0.18)',
        },
        success: {
          style: {
            background: '#dcfce7',
            color: '#166534',
            border: '1px solid #86efac',
          },
        },
        error: {
          style: {
            background: '#fee2e2',
            color: '#991b1b',
            border: '1px solid #fca5a5',
          },
        },
      }}
    />
  </React.StrictMode>
);
