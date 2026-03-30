import { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { GoogleLogin } from '@react-oauth/google'
import { login, googleLogin } from '../api'
import { useAuth } from '../context/AuthContext'

export default function Login() {
  const [email, setEmail]       = useState('')
  const [password, setPassword] = useState('')
  const [error, setError]       = useState('')
  const [loading, setLoading]   = useState(false)
  const navigate                = useNavigate()
  const { login: authLogin }    = useAuth()

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError(''); setLoading(true)
    try {
      await login({ email, password })
      navigate('/verify-otp', { state: { email } })
    } catch (err) {
      setError(err.response?.data?.message || 'Invalid credentials')
    } finally { setLoading(false) }
  }

  const handleGoogle = async (credentialResponse) => {
    try {
      const res = await googleLogin(credentialResponse.credential)
      authLogin(res.data)
      navigate('/dashboard')
    } catch {
      setError('Google login failed. Please try again.')
    }
  }

  return (
    <div style={pageWrap}>
      <div style={card}>
        <h1 style={title}>📚 Bookshelf</h1>
        <p style={sub}>Sign in to your account</p>

        {error && <div style={errorBox}>{error}</div>}

        <form onSubmit={handleSubmit}>
          <label style={label}>Email</label>
          <input style={input} type="email" value={email}
            onChange={e => setEmail(e.target.value)} required placeholder="you@example.com" />
          <label style={label}>Password</label>
          <input style={input} type="password" value={password}
            onChange={e => setPassword(e.target.value)} required placeholder="••••••••" />
          <button style={btn} type="submit" disabled={loading}>
            {loading ? 'Sending OTP...' : 'Continue with Email'}
          </button>
        </form>

        <div style={{ textAlign: 'center', margin: '1.25rem 0',
          borderTop: '1px solid var(--border)', paddingTop: '1.25rem' }}>
          <p style={{ color: 'var(--text-muted)', fontSize: '0.8rem', marginBottom: '1rem' }}>or</p>
          <div style={{ display: 'flex', justifyContent: 'center' }}>
            <GoogleLogin onSuccess={handleGoogle} onError={() => setError('Google login failed')} />
          </div>
        </div>

        <p style={{ textAlign: 'center', fontSize: '0.875rem', color: 'var(--text-secondary)' }}>
          No account? <Link to="/register" style={{ color: 'var(--accent)' }}>Register</Link>
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
const label    = { display: 'block', fontSize: '0.85rem', fontWeight: 500,
  marginBottom: '4px', color: 'var(--text-primary)' }
const input    = { width: '100%', padding: '10px 12px', border: '1px solid var(--border-input)',
  borderRadius: '8px', fontSize: '0.9rem', marginBottom: '1rem', boxSizing: 'border-box',
  background: 'var(--bg-input)', color: 'var(--text-primary)' }
const btn      = { width: '100%', padding: '11px', background: 'var(--accent)', color: '#fff',
  border: 'none', borderRadius: '8px', fontSize: '0.95rem', cursor: 'pointer', fontWeight: 500 }
const errorBox = { background: 'var(--danger-bg)', border: '1px solid var(--danger-border)',
  color: 'var(--danger)', padding: '10px 12px', borderRadius: '8px',
  marginBottom: '1rem', fontSize: '0.875rem' }
