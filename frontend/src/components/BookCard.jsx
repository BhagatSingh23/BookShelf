import { useNavigate } from 'react-router-dom'
import ProgressBar from './ProgressBar'

export default function BookCard({ book }) {
  const navigate = useNavigate()

  return (
    <div onClick={() => navigate(`/books/${book.id}`)} style={card}
      onMouseEnter={e => e.currentTarget.style.boxShadow = 'var(--shadow)'}
      onMouseLeave={e => e.currentTarget.style.boxShadow = 'none'}>
      {book.coverUrl
        ? <img src={book.coverUrl} alt={book.title} style={cover} />
        : <div style={coverPlaceholder}>{(book.title || '?')[0].toUpperCase()}</div>
      }
      <div style={{ padding: '0.75rem' }}>
        <div style={{ fontWeight: 600, fontSize: '0.875rem', marginBottom: '2px', color: 'var(--text-primary)',
          overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>{book.title}</div>
        <div style={{ color: 'var(--text-secondary)', fontSize: '0.78rem', marginBottom: '8px',
          overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>{book.author || 'Unknown author'}</div>
        {book.genre && <span style={genreBadge}>{book.genre}</span>}
        <div style={{ marginTop: '10px' }}>
          <ProgressBar pagesRead={book.pagesRead || 0} totalPages={book.totalPages || 0} isCompleted={book.isCompleted} />
        </div>
      </div>
    </div>
  )
}

const card           = { border: '1px solid var(--border)', borderRadius: '12px', overflow: 'hidden',
  cursor: 'pointer', transition: 'box-shadow 0.2s', background: 'var(--bg-card)', boxShadow: 'none' }
const cover          = { width: '100%', height: '160px', objectFit: 'cover', display: 'block' }
const coverPlaceholder = { width: '100%', height: '160px', background: 'var(--bg-hover)',
  display: 'flex', alignItems: 'center', justifyContent: 'center',
  fontSize: '2.5rem', fontWeight: 700, color: 'var(--accent)' }
const genreBadge     = { fontSize: '0.7rem', padding: '2px 8px', borderRadius: '99px',
  background: 'var(--badge-bg)', color: 'var(--badge-text)' }
