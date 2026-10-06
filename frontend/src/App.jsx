import { useEffect, useState } from 'react'
import { Link, Navigate, Route, Routes, useNavigate } from 'react-router-dom'
import api from './api'
import { getUser, isLoggedIn, logout } from './auth'

function Layout({ children }) {
  const navigate = useNavigate()
  const user = getUser()

  function signOut() {
    logout()
    navigate('/login')
  }

  return (
    <div className="app">
      <nav className="nav">
        <Link className="brand" to="/">Campus<span>Find</span></Link>
        <div className="navlinks">
          <Link to="/">Browse</Link>
          {user && <Link to="/dashboard">Dashboard</Link>}
          {user && <Link to="/report">Report</Link>}
          {user?.role === 'ADMIN' && <Link to="/admin">Admin</Link>}
          {user ? (
            <button className="ghost" onClick={signOut}>Logout</button>
          ) : (
            <Link className="button small" to="/login">Login</Link>
          )}
        </div>
      </nav>
      <main>{children}</main>
      <footer>CampusFind • Campus Lost & Found MVP</footer>
    </div>
  )
}

function Protected({ children }) {
  return isLoggedIn() ? children : <Navigate to="/login" replace />
}

function Home() {
  const [items, setItems] = useState([])
  const [q, setQ] = useState('')
  const [type, setType] = useState('')

  async function load() {
    const { data } = await api.get('/items', { params: { q, type } })
    setItems(data)
  }

  useEffect(() => { load() }, [])

  return (
    <section className="container">
      <div className="hero">
        <div>
          <p className="eyebrow">CAMPUS LOST & FOUND</p>
          <h1>Find it. Report it. <span>Return it.</span></h1>
          <p>One simple place for students to report lost items, share found items, and safely reconnect owners with their belongings.</p>
          <div className="actions">
            <Link className="button" to="/report">Report an item</Link>
            {!isLoggedIn() && <Link className="button secondary" to="/register">Create account</Link>}
          </div>
        </div>
        <div className="hero-card">
          <div className="emoji">🎒</div>
          <strong>CampusFind</strong>
          <p>Lost your calculator? Found someone's ID card? Start here.</p>
        </div>
      </div>

      <div className="section-head">
        <div>
          <p className="eyebrow">ACTIVE REPORTS</p>
          <h2>Browse campus items</h2>
        </div>
      </div>

      <div className="filters">
        <input placeholder="Search item, location..." value={q} onChange={e => setQ(e.target.value)} onKeyDown={e => e.key === 'Enter' && load()} />
        <select value={type} onChange={e => { setType(e.target.value); setTimeout(load, 0) }}>
          <option value="">All</option>
          <option value="LOST">Lost</option>
          <option value="FOUND">Found</option>
        </select>
        <button className="button" onClick={load}>Search</button>
      </div>

      <div className="grid">
        {items.map(item => <ItemCard key={item.id} item={item} />)}
        {!items.length && <div className="empty">No active reports found.</div>}
      </div>
    </section>
  )
}

function ItemCard({ item }) {
  return (
    <Link className="card item-card" to={`/items/${item.id}`}>
      <div className="item-top">
        <span className={`pill ${item.type.toLowerCase()}`}>{item.type}</span>
        <span>{item.status}</span>
      </div>
      <h3>{item.title}</h3>
      <p>{item.description}</p>
      <div className="meta">📍 {item.location}</div>
      <div className="meta">📅 {item.itemDate}</div>
      <div className="meta">🏷 {item.category?.name}</div>
    </Link>
  )
}

function Login() {
  const [form, setForm] = useState({ email: '', password: '' })
  const [error, setError] = useState('')
  const navigate = useNavigate()

  async function submit(e) {
    e.preventDefault()
    setError('')
    try {
      const { data } = await api.post('/auth/login', form)
      localStorage.setItem('token', data.token)
      localStorage.setItem('user', JSON.stringify({ id: data.userId, name: data.name, email: data.email, role: data.role }))
      navigate('/dashboard')
    } catch (err) { setError(err.response?.data?.message || 'Login failed') }
  }

  return <AuthPage title="Welcome back" subtitle="Sign in to manage reports and claims." form={form} setForm={setForm} onSubmit={submit} error={error} login />
}

function Register() {
  const [form, setForm] = useState({ name: '', email: '', password: '' })
  const [error, setError] = useState('')
  const navigate = useNavigate()

  async function submit(e) {
    e.preventDefault()
    setError('')
    try {
      const { data } = await api.post('/auth/register', form)
      localStorage.setItem('token', data.token)
      localStorage.setItem('user', JSON.stringify({ id: data.userId, name: data.name, email: data.email, role: data.role }))
      navigate('/dashboard')
    } catch (err) { setError(err.response?.data?.message || 'Registration failed') }
  }

  return <AuthPage title="Create your account" subtitle="Join your campus Lost & Found." form={form} setForm={setForm} onSubmit={submit} error={error} />
}

function AuthPage({ title, subtitle, form, setForm, onSubmit, error, login }) {
  return (
    <section className="auth">
      <form className="auth-card" onSubmit={onSubmit}>
        <p className="eyebrow">CAMPUSFIND</p>
        <h1>{title}</h1>
        <p>{subtitle}</p>
        {!login && <label>Name<input required value={form.name} onChange={e => setForm({...form, name: e.target.value})} /></label>}
        <label>Email<input required type="email" value={form.email} onChange={e => setForm({...form, email: e.target.value})} /></label>
        <label>Password<input required type="password" minLength="6" value={form.password} onChange={e => setForm({...form, password: e.target.value})} /></label>
        {error && <div className="error">{error}</div>}
        <button className="button full">{login ? 'Login' : 'Create account'}</button>
        <p className="switch">{login ? <>New here? <Link to="/register">Create an account</Link></> : <>Already registered? <Link to="/login">Login</Link></>}</p>
      </form>
    </section>
  )
}

function Report() {
  const [categories, setCategories] = useState([])
  const [form, setForm] = useState({ title: '', description: '', type: 'LOST', location: '', itemDate: new Date().toISOString().slice(0, 10), categoryId: '', imageUrl: '' })
  const [message, setMessage] = useState('')
  const navigate = useNavigate()

  useEffect(() => { api.get('/categories').then(r => setCategories(r.data)) }, [])

  async function submit(e) {
    e.preventDefault()
    try {
      await api.post('/items', { ...form, categoryId: Number(form.categoryId) })
      setMessage('Report submitted. It will appear after admin approval.')
      setTimeout(() => navigate('/dashboard'), 900)
    } catch (err) { setMessage(err.response?.data?.message || 'Could not create report') }
  }

  return <section className="container narrow">
    <div className="section-head"><div><p className="eyebrow">NEW REPORT</p><h2>Report an item</h2></div></div>
    <form className="card form" onSubmit={submit}>
      <div className="segmented">
        <button type="button" className={form.type === 'LOST' ? 'selected' : ''} onClick={() => setForm({...form, type:'LOST'})}>I lost something</button>
        <button type="button" className={form.type === 'FOUND' ? 'selected' : ''} onClick={() => setForm({...form, type:'FOUND'})}>I found something</button>
      </div>
      <label>Item name<input required value={form.title} onChange={e => setForm({...form,title:e.target.value})} placeholder="e.g. Black scientific calculator" /></label>
      <label>Description<textarea required rows="5" value={form.description} onChange={e => setForm({...form,description:e.target.value})} placeholder="Describe the item without revealing sensitive identifying details." /></label>
      <div className="two">
        <label>Category<select required value={form.categoryId} onChange={e => setForm({...form,categoryId:e.target.value})}><option value="">Choose...</option>{categories.map(c => <option key={c.id} value={c.id}>{c.name}</option>)}</select></label>
        <label>Location<input required value={form.location} onChange={e => setForm({...form,location:e.target.value})} placeholder="Central Library" /></label>
      </div>
      <div className="two">
        <label>Date<input required type="date" value={form.itemDate} onChange={e => setForm({...form,itemDate:e.target.value})} /></label>
        <label>Image URL (optional)<input value={form.imageUrl} onChange={e => setForm({...form,imageUrl:e.target.value})} placeholder="https://..." /></label>
      </div>
      {message && <div className="notice">{message}</div>}
      <button className="button">Submit report</button>
    </form>
  </section>
}

function Dashboard() {
  const [items, setItems] = useState([])
  const [claims, setClaims] = useState([])
  const user = getUser()

  async function load() {
    const [a,b] = await Promise.all([api.get('/items/mine'), api.get('/claims/my')])
    setItems(a.data); setClaims(b.data)
  }
  useEffect(() => { load() }, [])

  return <section className="container">
    <div className="dashboard-head">
      <div><p className="eyebrow">DASHBOARD</p><h1>Hello, {user?.name} 👋</h1><p>Manage your reports and claim activity.</p></div>
      <Link className="button" to="/report">+ Report item</Link>
    </div>
    <div className="stats">
      <div className="stat"><span>My reports</span><strong>{items.length}</strong></div>
      <div className="stat"><span>Lost</span><strong>{items.filter(i=>i.type==='LOST').length}</strong></div>
      <div className="stat"><span>Found</span><strong>{items.filter(i=>i.type==='FOUND').length}</strong></div>
      <div className="stat"><span>My claims</span><strong>{claims.length}</strong></div>
    </div>
    <div className="two-col">
      <div>
        <h2>My reports</h2>
        <div className="list">{items.map(i => <Link className="list-row" to={`/items/${i.id}`} key={i.id}><div><strong>{i.title}</strong><small>{i.type} • {i.location}</small></div><span className="pill">{i.status}</span></Link>)}</div>
      </div>
      <div>
        <h2>My claims</h2>
        <div className="list">{claims.map(c => <div className="list-row" key={c.id}><div><strong>{c.item?.title}</strong><small>{c.message}</small></div><span className={`pill ${c.status.toLowerCase()}`}>{c.status}</span></div>)}</div>
      </div>
    </div>
  </section>
}

function ItemDetails() {
  const [item, setItem] = useState(null)
  const [claims, setClaims] = useState([])
  const [message, setMessage] = useState('')
  const [claimText, setClaimText] = useState('')
  const [answer, setAnswer] = useState('')
  const user = getUser()
  const id = location.pathname.split('/').pop()

  async function load() {
    const { data } = await api.get(`/items/${id}`)
    setItem(data)
    if (user && data.reporter?.id === user.id) {
      const r = await api.get(`/items/${id}/claims`)
      setClaims(r.data)
    }
  }
  useEffect(() => { load() }, [id])

  async function claim() {
    try {
      await api.post(`/items/${id}/claims`, { message: claimText, verificationAnswer: answer })
      setMessage('Claim submitted successfully.')
      load()
    } catch (err) { setMessage(err.response?.data?.message || 'Claim failed') }
  }

  async function action(url) {
    try { await api.put(url); load() }
    catch (err) { setMessage(err.response?.data?.message || 'Action failed') }
  }

  if (!item) return <section className="container"><div className="empty">Loading...</div></section>

  const owner = user && user.id === item.reporter?.id
  return <section className="container narrow">
    <Link className="back" to="/">← Back to browse</Link>
    <div className="detail card">
      <div className="item-top"><span className={`pill ${item.type.toLowerCase()}`}>{item.type}</span><span>{item.status}</span></div>
      <h1>{item.title}</h1>
      {item.imageUrl && <img className="detail-image" src={item.imageUrl} alt={item.title} />}
      <p className="detail-desc">{item.description}</p>
      <div className="detail-grid"><div><small>Location</small><strong>📍 {item.location}</strong></div><div><small>Date</small><strong>📅 {item.itemDate}</strong></div><div><small>Category</small><strong>🏷 {item.category?.name}</strong></div><div><small>Reported by</small><strong>{item.reporter?.name}</strong></div></div>
      {message && <div className="notice">{message}</div>}

      {user && item.type === 'FOUND' && !owner && item.status === 'ACTIVE' && <div className="claim-box">
        <h3>Think this item is yours?</h3>
        <p>Provide enough information for the finder to verify your claim. Keep sensitive details private.</p>
        <textarea rows="4" placeholder="Why do you believe this is your item?" value={claimText} onChange={e=>setClaimText(e.target.value)} />
        <input placeholder="Verification detail (optional)" value={answer} onChange={e=>setAnswer(e.target.value)} />
        <button className="button" onClick={claim}>Submit claim</button>
      </div>}

      {owner && claims.length > 0 && <div className="claim-list">
        <h3>Claims for this item</h3>
        {claims.map(c => <div className="claim-row" key={c.id}>
          <div><strong>{c.claimant?.name}</strong><p>{c.message}</p><small>{c.verificationAnswer || 'No verification detail provided'}</small></div>
          {c.status === 'PENDING' && <div className="actions"><button className="button small" onClick={()=>action(`/claims/${c.id}/approve`)}>Approve</button><button className="button danger small" onClick={()=>action(`/claims/${c.id}/reject`)}>Reject</button></div>}
        </div>)}
      </div>}

      {owner && item.status === 'CLAIM_APPROVED' && <button className="button" onClick={()=>action(`/items/${id}/returned`)}>Mark as returned</button>}
    </div>
  </section>
}

function Admin() {
  const [items, setItems] = useState([])
  async function load() { setItems((await api.get('/admin/items/pending')).data) }
  useEffect(() => { load() }, [])
  async function action(id, kind) { await api.put(`/admin/items/${id}/${kind}`); load() }
  async function remove(id) { await api.delete(`/admin/items/${id}`); load() }

  return <section className="container">
    <p className="eyebrow">ADMIN</p><h1>Report moderation</h1>
    <p>Review new reports before they become visible to students.</p>
    <div className="list admin-list">
      {items.map(i => <div className="list-row" key={i.id}>
        <div><strong>{i.title}</strong><small>{i.type} • {i.category?.name} • {i.location} • by {i.reporter?.name}</small><p>{i.description}</p></div>
        <div className="actions"><button className="button small" onClick={()=>action(i.id,'approve')}>Approve</button><button className="button danger small" onClick={()=>action(i.id,'reject')}>Reject</button><button className="ghost danger-text" onClick={()=>remove(i.id)}>Delete</button></div>
      </div>)}
      {!items.length && <div className="empty">No pending reports 🎉</div>}
    </div>
  </section>
}

export default function App() {
  return <Layout><Routes>
    <Route path="/" element={<Home />} />
    <Route path="/login" element={<Login />} />
    <Route path="/register" element={<Register />} />
    <Route path="/items/:id" element={<ItemDetails />} />
    <Route path="/dashboard" element={<Protected><Dashboard /></Protected>} />
    <Route path="/report" element={<Protected><Report /></Protected>} />
    <Route path="/admin" element={<Protected><Admin /></Protected>} />
  </Routes></Layout>
}
