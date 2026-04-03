export default function SearchResultCard({ book, onAdd, isAdded, isAdding, isDuplicate }) {
  const btnLabel = isAdded ? '✓ Added to shelf'
    : isDuplicate ? 'Already in shelf'
    : isAdding ? 'Adding...'
    : '+ Add to shelf'

  const btnBg = isAdded ? '#10b981'
    : isDuplicate ? '#6b7280'
    : '#6366f1'

  return (
    <div style={{ background: 'var(--bg-card)', border: '1px solid var(--border)',
      borderRadius: '12px', padding: '1rem 1.25rem', display: 'flex', gap: '1rem', alignItems: 'center' }}>
      {book.coverUrl
        ? <img src={book.coverUrl} alt={book.title} style={coverImg} />
        : <div style={coverPlaceholder}>{(book.title || '?')[0].toUpperCase()}</div>
      }
      <div style={{ flex: 1, minWidth: 0 }}>
        <div style={{ fontWeight: 600, fontSize: '1rem', marginBottom: '4px', color: 'var(--text-primary)',
          overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>{book.title}</div>
        <div style={{ color: 'var(--text-secondary)', fontSize: '0.875rem', marginBottom: '8px' }}>
          by {book.author || 'Unknown author'}
        </div>
        <div style={{ display: 'flex', gap: '6px', flexWrap: 'wrap' }}>
          {book.genre && <span style={tag}>{book.genre}</span>}
          {book.totalPages > 0 && <span style={tag}>{book.totalPages} pages</span>}
        </div>
      </div>
      <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', alignItems: 'flex-end', flexShrink: 0 }}>
        {book.olUrl && (
          <a href={book.olUrl} target="_blank" rel="noreferrer"
            style={{ fontSize: '0.8rem', color: 'var(--accent)', textDecoration: 'none', whiteSpace: 'nowrap' }}>
            Read on Open Library ↗
          </a>
        )}
        <button onClick={() => !isAdded && !isDuplicate && onAdd(book)}
          disabled={isAdded || isAdding || isDuplicate}
          style={{ padding: '7px 16px', color: '#fff', border: 'none', borderRadius: '8px',
            fontSize: '0.85rem', fontWeight: 500, whiteSpace: 'nowrap',
            cursor: (isAdded || isDuplicate) ? 'default' : 'pointer',
            background: btnBg, opacity: isAdding ? 0.7 : 1 }}>
          {btnLabel}
        </button>
      </div>
    </div>
  )
}

const coverImg = { width: '60px', height: '85px', objectFit: 'cover', borderRadius: '6px', flexShrink: 0 }
const coverPlaceholder = { width: '60px', height: '85px', background: 'var(--bg-hover)', borderRadius: '6px',
  display: 'flex', alignItems: 'center', justifyContent: 'center',
  fontSize: '1.5rem', fontWeight: 700, color: 'var(--accent)', flexShrink: 0 }
const tag = { fontSize: '0.72rem', padding: '2px 10px', borderRadius: '99px',
  background: 'var(--badge-bg)', color: 'var(--badge-text)' }
