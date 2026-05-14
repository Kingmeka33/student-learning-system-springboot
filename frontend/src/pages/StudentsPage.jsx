import { useEffect, useState } from 'react';
import toast from 'react-hot-toast';
import { api, errorMessage } from '../api/api.js';

export default function StudentsPage({ user }) {
  const isAdmin = user.role === 'ADMIN';
  const [items, setItems] = useState([]);
  const [search, setSearch] = useState('');
  const [loading, setLoading] = useState(false);
  const [editingId, setEditingId] = useState(null);
  const [form, setForm] = useState({ name: '', email: '' });

  async function load() { setLoading(true); try { const res = await api.get(`/students?search=${search}`); setItems(res.data); } catch (e) { toast.error(errorMessage(e)); } finally { setLoading(false); } }
  useEffect(() => { load(); }, []);
  async function submit(e) { e.preventDefault(); try { if (editingId) { await api.patch(`/students/${editingId}`, form); toast.success('Student updated successfully'); } else { await api.post('/students', form); toast.success('Student saved successfully'); } setForm({ name: '', email: '' }); setEditingId(null); load(); } catch (e) { toast.error(errorMessage(e)); } }
  async function remove(id) { if (!confirm('Delete this student?')) return; try { await api.delete(`/students/${id}`); toast.success('Student deleted successfully'); load(); } catch(e) { toast.error(errorMessage(e)); } }
  return <section><h2>Students</h2>{isAdmin && <form className="card form-grid" onSubmit={submit}><input placeholder="Name" value={form.name} onChange={e=>setForm({...form,name:e.target.value})}/><input placeholder="Email" value={form.email} onChange={e=>setForm({...form,email:e.target.value})}/><button>{editingId?'Update Student':'Save Student'}</button>{editingId && <button type="button" className="btn-secondary" onClick={()=>{setEditingId(null);setForm({name:'',email:''});}}>Cancel</button>}</form>}<div className="search-row"><input placeholder="Search students" value={search} onChange={e=>setSearch(e.target.value)}/><button onClick={load}>Search</button></div>{loading && <div className="loading">Loading students...</div>}<div className="list">{items.map(item => <div className="item-card" key={item.id}><h3>{item.name}</h3><p>{item.email}</p><p>Courses: {item.courses?.map(c=>c.title).join(', ') || 'None'}</p><div className="actions">{isAdmin && <><button onClick={()=>{setEditingId(item.id);setForm({name:item.name,email:item.email});}}>Edit</button><button className="btn-danger" onClick={()=>remove(item.id)}>Delete</button></>}</div></div>)}</div></section>;
}
