import { useEffect, useState } from 'react';
import toast from 'react-hot-toast';
import { api, errorMessage } from '../api/api.js';

export default function AssignmentsPage({ user }) {
  const isAdmin = user.role === 'ADMIN';
  const [items, setItems] = useState([]);
  const [courses, setCourses] = useState([]);
  const [loading, setLoading] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const [form, setForm] = useState({ title: '', dueDate: '', courseId: '' });

  async function load() {
    setLoading(true);
    try {
      const [assignmentsResponse, coursesResponse] = await Promise.all([api.get('/assignments'), api.get('/courses')]);
      setItems(assignmentsResponse.data);
      setCourses(coursesResponse.data);
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
      await api.post('/assignments', form);
      toast.success('Assignment saved successfully');
      setForm({ title: '', dueDate: '', courseId: '' });
      load();
    } catch (error) {
      toast.error(errorMessage(error));
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <section>
      <h2>Assignments</h2>
      {isAdmin && (
        <form className="card form-grid" onSubmit={submit}>
          <input placeholder="Title" value={form.title} onChange={(e) => setForm({ ...form, title: e.target.value })} />
          <input type="date" value={form.dueDate} onChange={(e) => setForm({ ...form, dueDate: e.target.value })} />
          <select value={form.courseId} onChange={(e) => setForm({ ...form, courseId: e.target.value })}>
            <option value="">Select course</option>
            {courses.map((course) => <option key={course.id} value={course.id}>{course.title}</option>)}
          </select>
          <button disabled={submitting}>{submitting ? 'Saving...' : 'Save Assignment'}</button>
        </form>
      )}
      {loading && <div className="loading">Loading assignments...</div>}
      <div className="list">{items.map((item) => <div className="item-card" key={item.id}><h3>{item.title}</h3><p>Due: {item.dueDate}</p></div>)}</div>
    </section>
  );
}
