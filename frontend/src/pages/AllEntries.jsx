import { useState, useEffect } from 'react'
import Navbar from '../components/Navbar'
import EntryCard from '../components/EntryCard'
import { getAllEntries, deleteEntry } from '../api'

const TYPES = ['ALL', 'QUOTE', 'NOTE', 'INSIGHT', 'FACT', 'REFLECTION']

export default function AllEntries() {
  const [entries, setEntries] = useState([])
  const [loading, setLoading] = useState(true)
  const [filter, setFilter]   = useState('ALL')
  const [search, setSearch]   = useState('')

  useEffect(() => {
    getAllEntries().then(res => setEntries(res.data)).catch(console.error).finally(() => setLoading(false))
  }, [])

  const handleDelete = async (entryId) => {
    await deleteEntry(entryId)
    setEntries(prev => prev.filter(e => e.id !== entryId))
  }

  const filtered = entries.filter(e => {
    const matchType   = filter === 'ALL' || e.entryType === filter
    const matchSearch = search === '' ||
      e.content.toLowerCase().includes(search.toLowerCase()) ||
      e.bookTitle.toLowerCase().includes(search.toLowerCase())
    return matchType && matchSearch
  })

  return (
    <div style={{ minHeight: '100vh', background: 'var(--bg-secondary)' }}>
      <Navbar />
      <div style={{ maxWidth: '900px', margin: '0 auto', padding: '2rem 1.5rem' }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center',
          marginBottom: '1.5rem', flexWrap: 'wrap', gap: '1rem' }}>
          <h2 style={{ margin: 0, fontWeight: 600, color: 'var(--text-primary)' }}>
            All saved entries{' '}
            <span style={{ fontSize: '1rem', color: 'var(--text-secondary)', fontWeight: 400 }}>({filtered.length})</span>
          </h2>
          <input style={{ padding: '9px 14px', border: '1px solid var(--border-input)', borderRadius: '8px',
            fontSize: '0.9rem', width: '260px', background: 'var(--bg-input)', color: 'var(--text-primary)' }}
            placeholder="Search entries or book title..." value={search} onChange={e => setSearch(e.target.value)} />
        </div>
        <div style={{ display: 'flex', gap: '8px', flexWrap: 'wrap', marginBottom: '1.5rem' }}>
          {TYPES.map(t => (
            <button key={t} onClick={() => setFilter(t)}
              style={{ padding: '5px 14px', border: '1px solid var(--border)', borderRadius: '99px',
                background: filter === t ? 'var(--accent)' : 'var(--bg-card)',
                color: filter === t ? '#fff' : 'var(--text-primary)',
                borderColor: filter === t ? 'var(--accent)' : 'var(--border)',
                cursor: 'pointer', fontSize: '0.8rem' }}>
              {t === 'ALL' ? 'All types' : t.charAt(0) + t.slice(1).toLowerCase()}
            </button>
          ))}
        </div>
        {loading ? (
          <p style={{ color: 'var(--text-secondary)' }}>Loading entries...</p>
        ) : filtered.length === 0 ? (
          <div style={{ textAlign: 'center', marginTop: '4rem', color: 'var(--text-muted)' }}>
            <div style={{ fontSize: '2.5rem', marginBottom: '1rem' }}>💬</div>
            <p>{entries.length === 0 ? 'No entries yet. Open a book and start saving quotes and notes!' : 'No entries match your filter.'}</p>
          </div>
        ) : (
          <div style={{ maxWidth: '700px' }}>
            {filtered.map(entry => <EntryCard key={entry.id} entry={entry} onDelete={handleDelete} showBook={true} />)}
          </div>
        )}
      </div>
    </div>
  )
}
