import { useState } from 'react';
import AuthPage from './pages/AuthPage.jsx';
import StudentsPage from './pages/StudentsPage.jsx';
import CoursesPage from './pages/CoursesPage.jsx';
import ProfilesPage from './pages/ProfilesPage.jsx';
import AssignmentsPage from './pages/AssignmentsPage.jsx';
import EnrollmentsPage from './pages/EnrollmentsPage.jsx';
import { clearSession, getCurrentUser } from './api/api.js';

export default function App() {
  const [currentUser, setCurrentUser] = useState(getCurrentUser());
  const [activePage, setActivePage] = useState('students');
  const [toast, setToast] = useState(null);

  const showToast = (message, type = 'success') => {
    setToast({ message, type });
    setTimeout(() => setToast(null), 5000);
  };

  if (!currentUser) return <AuthPage onAuth={setCurrentUser} />;

  const pages = {
    students: <StudentsPage user={currentUser} showToast={showToast} />,
    courses: <CoursesPage user={currentUser} showToast={showToast} />,
    profiles: <ProfilesPage user={currentUser} showToast={showToast} />,
    assignments: <AssignmentsPage user={currentUser} showToast={showToast} />,
    enrollments: <EnrollmentsPage currentUser={currentUser} showToast={showToast} />,
  };

  return (
    <div className="app-shell">
      {toast && (
        <div className="toast-container">
          <div className={`toast ${toast.type}`}>{toast.message}</div>
        </div>
      )}

      <header className="top-header">
        <div>
          <h1>Student Learning System</h1>
          <p>Spring Boot + React + MySQL</p>
        </div>

        <div className="header-actions">
          <span className="badge">{currentUser.role}</span>
          <strong>{currentUser.name}</strong>
          <button
            className="btn-danger"
            onClick={() => {
              clearSession();
              setCurrentUser(null);
            }}
          >
            Logout
          </button>
        </div>
      </header>

      <nav className="tab-nav">
        {Object.keys(pages).map((page) => (
          <button
            className={activePage === page ? 'tab active' : 'tab'}
            onClick={() => setActivePage(page)}
            key={page}
          >
            {page}
          </button>
        ))}
      </nav>

      <main className="main-content">{pages[activePage]}</main>
    </div>
  );
}
