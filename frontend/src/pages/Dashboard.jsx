import { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import Navbar from '../components/Navbar'
import BookCard from '../components/BookCard'
import { getBooks } from '../api'

export default function Dashboard() {
  const [books, setBooks]   = useState([])
  const [loading, setLoading] = useState(true)
  const [filter, setFilter] = useState('ALL')
  const navigate            = useNavigate()

  useEffect(() => {
    getBooks().then(res => setBooks(res.data)).catch(console.error).finally(() => setLoading(false))
  }, [])

  const filtered = books.filter(b => {
    if (filter === 'READING')   return !b.isCompleted && b.pagesRead > 0
    if (filter === 'COMPLETED') return b.isCompleted
    return true
  })

  const totalPages = books.reduce((s, b) => s + (b.pagesRead || 0), 0)

  return (
    <div style={{ minHeight: '100vh', background: 'var(--bg-secondary)' }}>
      <Navbar />
      <div style={container}>
        {/* Stats */}
        <div style={statsRow}>
          {[
            { label: 'Total books', value: books.length },
            { label: 'Completed',   value: books.filter(b => b.isCompleted).length },
            { label: 'In progress', value: books.filter(b => !b.isCompleted && b.pagesRead > 0).length },
            { label: 'Pages read',  value: totalPages.toLocaleString() },
          ].map(s => (
            <div key={s.label} style={statCard}>
              <div style={{ fontSize: '1.6rem', fontWeight: 700, color: 'var(--accent)' }}>{s.value}</div>
              <div style={{ fontSize: '0.8rem', color: 'var(--text-secondary)', marginTop: '2px' }}>{s.label}</div>
            </div>
          ))}
        </div>
        {/* Filters + Add */}
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1.25rem' }}>
          <div style={{ display: 'flex', gap: '8px' }}>
            {['ALL', 'READING', 'COMPLETED'].map(f => (
              <button key={f} onClick={() => setFilter(f)}
                style={{ ...filterBtn, ...(filter === f ? filterActive : {}) }}>
                {f === 'ALL' ? 'All' : f === 'READING' ? 'In Progress' : 'Completed'}
              </button>
            ))}
          </div>
          <button onClick={() => navigate('/search')} style={addBtn}>+ Add book</button>
        </div>
        {/* Grid */}
        {loading ? (
          <p style={{ color: 'var(--text-secondary)', textAlign: 'center', marginTop: '3rem' }}>Loading your books...</p>
        ) : filtered.length === 0 ? (
          <div style={{ textAlign: 'center', marginTop: '4rem' }}>
            <div style={{ fontSize: '3rem', marginBottom: '1rem' }}>📚</div>
            <p style={{ color: 'var(--text-secondary)', marginBottom: '1rem' }}>
              {books.length === 0 ? 'Your shelf is empty. Search for a book to get started!' : 'No books match this filter.'}
            </p>
            {books.length === 0 && <button onClick={() => navigate('/search')} style={addBtn}>Search books</button>}
          </div>
        ) : (
          <div style={grid}>
            {filtered.map(book => <BookCard key={book.id} book={book} />)}
          </div>
        )}
      </div>
    </div>
  )
}

const container  = { maxWidth: '1100px', margin: '0 auto', padding: '2rem 1.5rem' }
const statsRow   = { display: 'grid', gridTemplateColumns: 'repeat(4,1fr)', gap: '12px', marginBottom: '2rem' }
const statCard   = { background: 'var(--bg-card)', border: '1px solid var(--border)',
  borderRadius: '12px', padding: '1rem 1.25rem', textAlign: 'center' }
const grid       = { display: 'grid', gridTemplateColumns: 'repeat(auto-fill,minmax(180px,1fr))', gap: '16px' }
const filterBtn  = { padding: '6px 14px', border: '1px solid var(--border)', borderRadius: '8px',
  background: 'var(--bg-card)', cursor: 'pointer', fontSize: '0.85rem', color: 'var(--text-primary)' }
const filterActive = { background: 'var(--accent)', color: '#fff', borderColor: 'var(--accent)' }
const addBtn     = { padding: '8px 18px', background: 'var(--accent)', color: '#fff',
  border: 'none', borderRadius: '8px', cursor: 'pointer', fontWeight: 500, fontSize: '0.9rem' }
