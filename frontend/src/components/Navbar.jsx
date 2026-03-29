import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { useTheme } from '../context/ThemeContext'

export default function Navbar() {
  const { user, logout } = useAuth()
  const { theme, toggleTheme } = useTheme()
  const navigate = useNavigate()

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  return (
    <nav style={{
      display: 'flex', alignItems: 'center', justifyContent: 'space-between',
      padding: '0 2rem', height: '60px',
      borderBottom: `1px solid var(--border)`,
      background: 'var(--bg-card)',
      position: 'sticky', top: 0, zIndex: 100,
    }}>
      <Link to="/dashboard" style={{ fontWeight: 700, fontSize: '1.1rem',
        textDecoration: 'none', color: 'var(--accent)' }}>
        📚 Bookshelf
      </Link>

      <div style={{ display: 'flex', gap: '1.5rem', alignItems: 'center' }}>
        <Link to="/dashboard" style={navLink}>My Books</Link>
        <Link to="/search"    style={navLink}>Search</Link>
        <Link to="/entries"   style={navLink}>All Notes</Link>

        <span style={{ color: 'var(--text-secondary)', fontSize: '0.875rem' }}>
          {user?.name}
        </span>

        {/* Dark / Light toggle */}
        <button onClick={toggleTheme} style={toggleBtn} title="Toggle theme">
          {theme === 'light' ? '🌙' : '☀️'}
        </button>

        <button onClick={handleLogout} style={logoutBtn}>Logout</button>
      </div>
    </nav>
  )
}

const navLink = {
  textDecoration: 'none',
  color: 'var(--text-secondary)',
  fontSize: '0.9rem',
}
const toggleBtn = {
  background: 'var(--bg-hover)',
  border: `1px solid var(--border)`,
  borderRadius: '8px',
  padding: '5px 10px',
  cursor: 'pointer',
  fontSize: '16px',
  lineHeight: 1,
}
const logoutBtn = {
  padding: '6px 14px',
  border: `1px solid var(--border)`,
  borderRadius: '8px',
  background: 'transparent',
  cursor: 'pointer',
  fontSize: '0.875rem',
  color: 'var(--text-secondary)',
}
