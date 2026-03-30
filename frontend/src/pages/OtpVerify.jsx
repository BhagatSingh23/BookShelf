import { useState } from 'react'
import { useNavigate, useLocation } from 'react-router-dom'
import { verifyOtp, sendOtp } from '../api'
import { useAuth } from '../context/AuthContext'

export default function OtpVerify() {
  const [code, setCode]       = useState('')
  const [error, setError]     = useState('')
  const [resent, setResent]   = useState(false)
  const [loading, setLoading] = useState(false)
  const navigate              = useNavigate()
  const location              = useLocation()
  const { login }             = useAuth()
  const email                 = location.state?.email || ''

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError(''); setLoading(true)
    try {
      const res = await verifyOtp({ email, code })
      login(res.data)
      navigate('/dashboard')
    } catch {
      setError('Invalid or expired code. Please try again.')
    } finally { setLoading(false) }
  }

  const handleResend = async () => {
    await sendOtp(email)
    setResent(true)
    setTimeout(() => setResent(false), 3000)
  }

  return (
    <div style={pageWrap}>
      <div style={card}>
        <h2 style={{ textAlign: 'center', marginBottom: '8px', color: 'var(--text-primary)' }}>Check your email</h2>
        <p style={{ textAlign: 'center', color: 'var(--text-secondary)', marginBottom: '1.5rem', fontSize: '0.9rem' }}>
          We sent a 6-digit code to <strong>{email}</strong>
        </p>
        {error  && <div style={errorBox}>{error}</div>}
        {resent && <div style={successBox}>Code resent! Check your inbox.</div>}
        <form onSubmit={handleSubmit}>
          <input style={{ ...input, fontSize: '1.5rem', textAlign: 'center', letterSpacing: '0.5em' }}
            type="text" maxLength={6} value={code}
            onChange={e => setCode(e.target.value.replace(/\D/g, ''))}
            placeholder="000000" required />
          <button style={btn} type="submit" disabled={loading || code.length < 6}>
            {loading ? 'Verifying...' : 'Verify code'}
          </button>
        </form>
        <p style={{ textAlign: 'center', marginTop: '1rem', fontSize: '0.875rem', color: 'var(--text-secondary)' }}>
          Didn't get it?{' '}
          <button onClick={handleResend} style={{ background: 'none', border: 'none',
            color: 'var(--accent)', cursor: 'pointer', fontSize: '0.875rem', textDecoration: 'underline' }}>
            Resend code
          </button>
        </p>
      </div>
    </div>
  )
}

const pageWrap  = { minHeight: '100vh', display: 'flex', alignItems: 'center',
  justifyContent: 'center', background: 'var(--bg-secondary)', padding: '1rem' }
const card      = { background: 'var(--bg-card)', borderRadius: '16px', padding: '2rem',
  width: '100%', maxWidth: '400px', border: '1px solid var(--border)' }
const input     = { width: '100%', padding: '12px', border: '1px solid var(--border-input)',
  borderRadius: '8px', marginBottom: '1rem', boxSizing: 'border-box',
  background: 'var(--bg-input)', color: 'var(--text-primary)' }
const btn       = { width: '100%', padding: '11px', background: 'var(--accent)', color: '#fff',
  border: 'none', borderRadius: '8px', fontSize: '0.95rem', cursor: 'pointer', fontWeight: 500 }
const errorBox  = { background: 'var(--danger-bg)', border: '1px solid var(--danger-border)',
  color: 'var(--danger)', padding: '10px 12px', borderRadius: '8px', marginBottom: '1rem', fontSize: '0.875rem' }
const successBox = { background: '#f0fdf4', border: '1px solid #bbf7d0', color: '#15803d',
  padding: '10px 12px', borderRadius: '8px', marginBottom: '1rem', fontSize: '0.875rem' }
