export default function ProgressBar({ pagesRead = 0, totalPages = 0, isCompleted = false, showLabel = true }) {
  const pct = totalPages > 0
    ? Math.min(100, Math.round((pagesRead / totalPages) * 100))
    : 0

  const barColor = isCompleted ? '#10b981' : pct > 0 ? '#6366f1' : '#e5e7eb'

  return (
    <div>
      {showLabel && (
        <div style={{ display: 'flex', justifyContent: 'space-between',
          fontSize: '0.78rem', color: '#6b7280', marginBottom: '5px' }}>
          <span>
            {isCompleted
              ? '✓ Completed'
              : totalPages > 0
                ? `${pagesRead} / ${totalPages} pages`
                : `${pagesRead} pages read`}
          </span>
          <span style={{ fontWeight: 500 }}>{pct}%</span>
        </div>
      )}
      <div style={{ background: '#f3f4f6', borderRadius: '99px', height: '6px', overflow: 'hidden' }}>
        <div style={{
          width: `${pct}%`,
          height: '6px',
          borderRadius: '99px',
          background: barColor,
          transition: 'width 0.4s ease',
          minWidth: pct > 0 ? '6px' : '0',
        }} />
      </div>
    </div>
  )
}
