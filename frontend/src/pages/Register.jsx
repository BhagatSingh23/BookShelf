import { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { register } from '../api'

export default function Register() {
  const [form, setForm]       = useState({ name: '', email: '', password: '' })
  const [error, setError]     = useState('')
  const [loading, setLoading] = useState(false)
  const navigate              = useNavigate()

  const handle = (e) => setForm({ ...form, [e.target.name]: e.target.value })

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError(''); setLoading(true)
    try {
      await register(form)
      navigate('/verify-otp', { state: { email: form.email } })
    } catch (err) {
      setError(err.response?.data?.message || 'Registration failed')
    } finally { setLoading(false) }
  }

  return (
    <div style={pageWrap}>
      <div style={card}>
        <h1 style={title}>Create account</h1>
        <p style={sub}>Start building your bookshelf</p>
        {error && <div style={errorBox}>{error}</div>}
        <form onSubmit={handleSubmit}>
          <label style={label}>Name</label>
          <input style={input} name="name" value={form.name} onChange={handle} required placeholder="Your name" />
          <label style={label}>Email</label>
          <input style={input} type="email" name="email" value={form.email} onChange={handle} required placeholder="you@example.com" />
          <label style={label}>Password</label>
          <input style={input} type="password" name="password" value={form.password} onChange={handle} required placeholder="Min 8 characters" />
          <button style={btn} type="submit" disabled={loading}>
            {loading ? 'Creating account...' : 'Create account'}
          </button>
        </form>
        <p style={{ textAlign: 'center', marginTop: '1.5rem', fontSize: '0.875rem', color: 'var(--text-secondary)' }}>
          Already have an account? <Link to="/login" style={{ color: 'var(--accent)' }}>Sign in</Link>
        </p>
      </div>
    </div>
  )
}

const pageWrap = { minHeight: '100vh', display: 'flex', alignItems: 'center',
  justifyContent: 'center', background: 'var(--bg-secondary)', padding: '1rem' }
const card     = { background: 'var(--bg-card)', borderRadius: '16px', padding: '2rem',
  width: '100%', maxWidth: '400px', border: '1px solid var(--border)' }
const title    = { textAlign: 'center', margin: '0 0 4px', fontSize: '1.5rem', color: 'var(--text-primary)' }
const sub      = { textAlign: 'center', color: 'var(--text-secondary)', marginBottom: '1.5rem', fontSize: '0.9rem' }
const label    = { display: 'block', fontSize: '0.85rem', fontWeight: 500, marginBottom: '4px', color: 'var(--text-primary)' }
const input    = { width: '100%', padding: '10px 12px', border: '1px solid var(--border-input)',
  borderRadius: '8px', fontSize: '0.9rem', marginBottom: '1rem', boxSizing: 'border-box',
  background: 'var(--bg-input)', color: 'var(--text-primary)' }
const btn      = { width: '100%', padding: '11px', background: 'var(--accent)', color: '#fff',
  border: 'none', borderRadius: '8px', fontSize: '0.95rem', cursor: 'pointer', fontWeight: 500 }
const errorBox = { background: 'var(--danger-bg)', border: '1px solid var(--danger-border)',
  color: 'var(--danger)', padding: '10px 12px', borderRadius: '8px', marginBottom: '1rem', fontSize: '0.875rem' }
