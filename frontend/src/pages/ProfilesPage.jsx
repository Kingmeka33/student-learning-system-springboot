import { useEffect, useState } from 'react';
import toast from 'react-hot-toast';
import { api, errorMessage } from '../api/api.js';

export default function ProfilesPage({ user }) {
  const isAdmin = user.role === 'ADMIN';
  const [profiles, setProfiles] = useState([]);
  const [students, setStudents] = useState([]);
  const [loading, setLoading] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const [form, setForm] = useState({ bio: '', avatarUrl: '', studentId: '' });

  async function load() {
    setLoading(true);
    try {
      const [profilesResponse, studentsResponse] = await Promise.all([api.get('/profiles'), api.get('/students')]);
      setProfiles(profilesResponse.data);
      setStudents(studentsResponse.data);
    } catch (error) {
      toast.error(errorMessage(error));
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => { load(); }, []);

  async function submit(event) {
    event.preventDefault();
    setSubmitting(true);
    try {
      await api.post('/profiles', form);
      toast.success('Profile saved successfully');
      setForm({ bio: '', avatarUrl: '', studentId: '' });
      load();
    } catch (error) {
      toast.error(errorMessage(error));
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <section>
      <h2>Profiles</h2>
      {isAdmin && (
        <form className="card form-grid" onSubmit={submit}>
          <input placeholder="Bio" value={form.bio} onChange={(e) => setForm({ ...form, bio: e.target.value })} />
          <input placeholder="Avatar URL" value={form.avatarUrl} onChange={(e) => setForm({ ...form, avatarUrl: e.target.value })} />
          <select value={form.studentId} onChange={(e) => setForm({ ...form, studentId: e.target.value })}>
            <option value="">Select student</option>
            {students.map((student) => <option key={student.id} value={student.id}>{student.name}</option>)}
          </select>
          <button disabled={submitting}>{submitting ? 'Saving...' : 'Save Profile'}</button>
        </form>
      )}
      {loading && <div className="loading">Loading profiles...</div>}
      <div className="list">
        {profiles.map((profile) => (
          <div className="item-card" key={profile.id}>
            <div className="avatar-row">
              {profile.avatarUrl && <img className="avatar" src={profile.avatarUrl} alt="Profile avatar" />}
              <div><h3>{profile.bio}</h3><p>Linked to one student through a one-to-one relationship.</p></div>
            </div>
          </div>
        ))}
      </div>
    </section>
  );
}
