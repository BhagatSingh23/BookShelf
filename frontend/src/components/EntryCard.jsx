const TYPE_COLORS = {
  QUOTE:      { bg: '#eff6ff', color: '#1d4ed8', darkBg: '#1e3a5f', darkColor: '#93c5fd', label: 'Quote' },
  NOTE:       { bg: '#f0fdf4', color: '#15803d', darkBg: '#14532d', darkColor: '#86efac', label: 'Note' },
  INSIGHT:    { bg: '#fdf4ff', color: '#7e22ce', darkBg: '#3b0764', darkColor: '#d8b4fe', label: 'Insight' },
  FACT:       { bg: '#fff7ed', color: '#c2410c', darkBg: '#431407', darkColor: '#fdba74', label: 'Fact' },
  REFLECTION: { bg: '#fdf2f8', color: '#be185d', darkBg: '#500724', darkColor: '#f9a8d4', label: 'Reflection' },
}

export default function EntryCard({ entry, onDelete, showBook = false }) {
  const t = TYPE_COLORS[entry.entryType] || TYPE_COLORS.NOTE

  return (
    <div style={{ border: '1px solid var(--border)', borderRadius: '10px',
      padding: '1rem', background: 'var(--bg-card)', marginBottom: '12px' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '8px' }}>
        <div style={{ display: 'flex', gap: '8px', alignItems: 'center' }}>
          <span style={{ fontSize: '0.7rem', padding: '2px 10px', borderRadius: '99px',
            fontWeight: 500, background: t.bg, color: t.color }}>{t.label}</span>
          {entry.pageRef && (
            <span style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>p. {entry.pageRef}</span>
          )}
        </div>
        {onDelete && (
          <button onClick={() => onDelete(entry.id)} style={{ background: 'none', border: 'none',
            cursor: 'pointer', color: 'var(--text-muted)', fontSize: '0.8rem', padding: '2px 6px' }}>✕</button>
        )}
      </div>
      {showBook && (
        <div style={{ fontSize: '0.75rem', color: 'var(--text-secondary)', marginBottom: '6px', fontStyle: 'italic' }}>
          {entry.bookTitle}
        </div>
      )}
      <p style={{ margin: 0, fontSize: '0.9rem', lineHeight: 1.6, color: 'var(--text-primary)',
        borderLeft: `3px solid ${t.color}`, paddingLeft: '12px' }}>{entry.content}</p>
      <div style={{ marginTop: '8px', fontSize: '0.72rem', color: 'var(--text-muted)' }}>
        {new Date(entry.createdAt).toLocaleDateString()}
      </div>
    </div>
  )
}
