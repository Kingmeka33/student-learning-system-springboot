import { useEffect, useState } from 'react';
import toast from 'react-hot-toast';
import { api, errorMessage } from '../api/api.js';

export default function CoursesPage({ user }) {
  const isAdmin = user.role === 'ADMIN';
  const [items, setItems] = useState([]);
  const [search, setSearch] = useState('');
  const [loading, setLoading] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const [editingId, setEditingId] = useState(null);
  const [form, setForm] = useState({ title: '', code: '' });

  async function load() {
    setLoading(true);
    try {
      const response = await api.get(`/courses?search=${encodeURIComponent(search)}`);
      setItems(response.data);
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
      if (editingId) {
        await api.patch(`/courses/${editingId}`, form);
        toast.success('Course updated successfully');
      } else {
        await api.post('/courses', form);
        toast.success('Course saved successfully');
      }
      setForm({ title: '', code: '' });
      setEditingId(null);
      load();
    } catch (error) {
      toast.error(errorMessage(error));
    } finally {
      setSubmitting(false);
    }
  }

  async function remove(id) {
    if (!confirm('Delete course?')) return;
    try {
      await api.delete(`/courses/${id}`);
      toast.success('Course deleted successfully');
      load();
    } catch (error) {
      toast.error(errorMessage(error));
    }
  }

  return (
    <section>
      <h2>Courses</h2>
      {isAdmin && (
        <form className="card form-grid" onSubmit={submit}>
          <input placeholder="Course title" value={form.title} onChange={(e) => setForm({ ...form, title: e.target.value })} />
          <input placeholder="Course code" value={form.code} onChange={(e) => setForm({ ...form, code: e.target.value })} />
          <button disabled={submitting}>{submitting ? 'Saving...' : editingId ? 'Update Course' : 'Save Course'}</button>
          {editingId && <button type="button" className="btn-secondary" onClick={() => { setEditingId(null); setForm({ title: '', code: '' }); }}>Cancel</button>}
        </form>
      )}
      <div className="search-row">
        <input placeholder="Search courses" value={search} onChange={(e) => setSearch(e.target.value)} />
        <button onClick={load} disabled={loading}>{loading ? 'Searching...' : 'Search'}</button>
      </div>
      {loading && <div className="loading">Loading courses...</div>}
      <div className="list">
        {items.map((course) => (
          <div className="item-card" key={course.id}>
            <h3>{course.title}</h3>
            <p>Code: {course.code}</p>
            <p>Assignments: {course.assignments?.map((a) => a.title).join(', ') || 'None'}</p>
            <p>Registered students: {course.students?.map((s) => s.name).join(', ') || 'None'}</p>
            {isAdmin && <div className="actions"><button onClick={() => { setEditingId(course.id); setForm({ title: course.title, code: course.code }); }}>Edit</button><button className="btn-danger" onClick={() => remove(course.id)}>Delete</button></div>}
          </div>
        ))}
      </div>
    </section>
  );
}
