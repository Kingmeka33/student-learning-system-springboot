import { useState } from 'react';
import toast from 'react-hot-toast';
import { api, errorMessage, saveSession } from '../api/api.js';

export default function AuthPage({ onAuth }) {
  const [mode, setMode] = useState('login');
  const [form, setForm] = useState({ name: '', email: '', password: '', role: 'STUDENT' });
  const [loading, setLoading] = useState(false);

  async function submit(e) {
    e.preventDefault();
    setLoading(true);
    try {
      const payload = mode === 'login' ? { email: form.email, password: form.password } : form;
      const res = await api.post(`/auth/${mode}`, payload);
      saveSession(res.data);
      toast.success(mode === 'login' ? 'Logged in successfully' : 'Account created successfully');
      onAuth(res.data.user);
    } catch (error) {
      toast.error(errorMessage(error));
    } finally { setLoading(false); }
  }

  return <div className="auth-page"><form className="auth-card" onSubmit={submit}>
    <h1>{mode === 'login' ? 'Login' : 'Create Account'}</h1>
    {mode === 'register' && <input placeholder="Name" value={form.name} onChange={e => setForm({ ...form, name: e.target.value })} />}
    <input placeholder="Email" value={form.email} onChange={e => setForm({ ...form, email: e.target.value })} />
    <input placeholder="Password" type="password" value={form.password} onChange={e => setForm({ ...form, password: e.target.value })} />
    {mode === 'register' && <select value={form.role} onChange={e => setForm({ ...form, role: e.target.value })}><option value="STUDENT">Student</option><option value="ADMIN">Admin</option></select>}
    <button disabled={loading}>{loading ? 'Please wait...' : mode === 'login' ? 'Login' : 'Register'}</button>
    <button type="button" className="btn-secondary" onClick={() => setMode(mode === 'login' ? 'register' : 'login')}>{mode === 'login' ? 'Need an account?' : 'Already have an account?'}</button>
  </form></div>;
}
