import { useState, useEffect } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import Navbar from '../components/Navbar'
import EntryCard from '../components/EntryCard'
import { getBook, updateProgress, getEntriesForBook, addEntry, deleteEntry, deleteBook } from '../api'

const ENTRY_TYPES = ['QUOTE', 'NOTE', 'INSIGHT', 'FACT', 'REFLECTION']

export default function BookDetail() {
  const { id }                      = useParams()
  const navigate                    = useNavigate()
  const [book, setBook]             = useState(null)
  const [entries, setEntries]       = useState([])
  const [loading, setLoading]       = useState(true)
  const [pagesRead, setPagesRead]   = useState(0)
  const [isCompleted, setCompleted] = useState(false)
  const [savingProg, setSavingProg] = useState(false)

  // New entry form state
  const [entryType, setEntryType]   = useState('QUOTE')
  const [content, setContent]       = useState('')
  const [pageRef, setPageRef]       = useState('')
  const [savingEntry, setSavingEntry] = useState(false)

  useEffect(() => {
    Promise.all([getBook(id), getEntriesForBook(id)])
      .then(([bookRes, entriesRes]) => {
        setBook(bookRes.data)
        setPagesRead(bookRes.data.pagesRead || 0)
        setCompleted(bookRes.data.isCompleted || false)
        setEntries(entriesRes.data)
      })
      .catch(() => navigate('/dashboard'))
      .finally(() => setLoading(false))
  }, [id])

  const saveProgress = async () => {
    setSavingProg(true)
    try {
      await updateProgress(id, { pagesRead, isCompleted })
      setBook(prev => ({ ...prev, pagesRead, isCompleted }))
    } finally { setSavingProg(false) }
  }

  const handleAddEntry = async (e) => {
    e.preventDefault()
    if (!content.trim()) return
    setSavingEntry(true)
    try {
      const res = await addEntry(id, { entryType, content, pageRef })
      setEntries(prev => [res.data, ...prev])
      setContent(''); setPageRef('')
    } finally { setSavingEntry(false) }
  }

  const handleDeleteEntry = async (entryId) => {
    await deleteEntry(entryId)
    setEntries(prev => prev.filter(e => e.id !== entryId))
  }

  const handleDeleteBook = async () => {
    if (!window.confirm('Remove this book from your shelf?')) return
    await deleteBook(id)
    navigate('/dashboard')
  }

  if (loading) return <div style={{ minHeight: '100vh', background: '#f9fafb' }}><Navbar /><p style={{ padding: '2rem', color: '#6b7280' }}>Loading...</p></div>
  if (!book) return null

  const pct = book.totalPages > 0 ? Math.min(100, Math.round((pagesRead / book.totalPages) * 100)) : 0

  return (
    <div style={{ minHeight: '100vh', background: '#f9fafb' }}>
      <Navbar />
      <div style={container}>

        {/* Book header */}
        <div style={header}>
          {book.coverUrl
            ? <img src={book.coverUrl} alt={book.title} style={coverImg} />
            : <div style={coverPlaceholder}>{book.title[0]}</div>
          }
          <div style={{ flex: 1 }}>
            <h1 style={{ margin: '0 0 6px', fontSize: '1.5rem', fontWeight: 700 }}>{book.title}</h1>
            <p style={{ margin: '0 0 8px', color: '#6b7280' }}>by {book.author}</p>
            <div style={{ display: 'flex', gap: '8px', flexWrap: 'wrap', marginBottom: '12px' }}>
              {book.genre && <span style={tag}>{book.genre}</span>}
              {book.totalPages > 0 && <span style={tag}>{book.totalPages} pages</span>}
              {book.isCompleted && <span style={{ ...tag, background: '#dcfce7', color: '#15803d' }}>✓ Completed</span>}
            </div>
            {book.olUrl && (
              <a href={book.olUrl} target="_blank" rel="noreferrer" style={readLink}>
                Read on Open Library ↗
              </a>
            )}
            <div style={{ marginTop: '12px' }}>
              <button onClick={handleDeleteBook} style={deleteBookBtn}>Remove from shelf</button>
            </div>
          </div>
        </div>

        <div style={twoCol}>

          {/* Left — Reading progress */}
          <div>
            <div style={section}>
              <h3 style={sectionTitle}>Reading progress</h3>

              {/* Progress bar */}
              <div style={{ marginBottom: '1rem' }}>
                <div style={{ display: 'flex', justifyContent: 'space-between',
                  fontSize: '0.85rem', color: '#6b7280', marginBottom: '6px' }}>
                  <span>{pagesRead} / {book.totalPages || '?'} pages</span>
                  <span>{pct}%</span>
                </div>
                <div style={{ background: '#f3f4f6', borderRadius: '99px', height: '8px' }}>
                  <div style={{ width: `${pct}%`, height: '8px', borderRadius: '99px',
                    background: isCompleted ? '#10b981' : '#6366f1', transition: 'width 0.3s' }} />
                </div>
              </div>

              {/* Pages slider */}
              <label style={label}>Pages read</label>
              <input type="range" min={0} max={book.totalPages || 1000}
                value={pagesRead} step={1}
                onChange={e => { setPagesRead(Number(e.target.value)); setCompleted(false) }}
                style={{ width: '100%', marginBottom: '1rem' }} />

              <label style={label}>Pages read (type exact number)</label>
              <input type="number" min={0} max={book.totalPages || 9999}
                value={pagesRead} style={numInput}
                onChange={e => { setPagesRead(Number(e.target.value)); setCompleted(false) }} />

              {/* Completed checkbox */}
              <label style={{ display: 'flex', alignItems: 'center', gap: '8px',
                cursor: 'pointer', fontSize: '0.9rem', marginBottom: '1rem' }}>
                <input type="checkbox" checked={isCompleted}
                  onChange={e => {
                    setCompleted(e.target.checked)
                    if (e.target.checked && book.totalPages) setPagesRead(book.totalPages)
                  }} />
                Mark as completed
              </label>

              <button onClick={saveProgress} disabled={savingProg} style={saveBtn}>
                {savingProg ? 'Saving...' : 'Save progress'}
              </button>
            </div>
          </div>

          {/* Right — Saved entries */}
          <div>
            <div style={section}>
              <h3 style={sectionTitle}>Add a quote, note or insight</h3>

              <form onSubmit={handleAddEntry}>
                {/* Type selector */}
                <div style={{ display: 'flex', gap: '6px', flexWrap: 'wrap', marginBottom: '12px' }}>
                  {ENTRY_TYPES.map(t => (
                    <button key={t} type="button"
                      onClick={() => setEntryType(t)}
                      style={{ ...typeBtn, ...(entryType === t ? typeBtnActive : {}) }}>
                      {t.charAt(0) + t.slice(1).toLowerCase()}
                    </button>
                  ))}
                </div>

                <label style={label}>Content</label>
                <textarea
                  style={textarea} required value={content}
                  onChange={e => setContent(e.target.value)}
                  placeholder={
                    entryType === 'QUOTE'      ? '"Enter the quote here..."' :
                    entryType === 'NOTE'       ? 'Write your note...' :
                    entryType === 'INSIGHT'    ? 'What insight did you gain?' :
                    entryType === 'FACT'       ? 'A useful fact from this book...' :
                    'Your reflection...'
                  }
                />

                <label style={label}>Page reference (optional)</label>
                <input style={numInput} value={pageRef} placeholder="e.g. 142"
                  onChange={e => setPageRef(e.target.value)} />

                <button style={saveBtn} type="submit" disabled={savingEntry}>
                  {savingEntry ? 'Saving...' : 'Save entry'}
                </button>
              </form>
            </div>

            {/* Entries list */}
            <h3 style={{ ...sectionTitle, marginTop: '1.5rem' }}>
              Saved entries ({entries.length})
            </h3>
            {entries.length === 0
              ? <p style={{ color: '#9ca3af', fontSize: '0.875rem' }}>
                  No entries yet. Add a quote or note above!
                </p>
              : entries.map(entry => (
                  <EntryCard key={entry.id} entry={entry} onDelete={handleDeleteEntry} />
                ))
            }
          </div>
        </div>
      </div>
    </div>
  )
}

const container     = { maxWidth: '1000px', margin: '0 auto', padding: '2rem 1.5rem' }
const header        = { display: 'flex', gap: '1.5rem', alignItems: 'flex-start',
  background: '#fff', border: '1px solid #e5e7eb', borderRadius: '16px',
  padding: '1.5rem', marginBottom: '1.5rem' }
const coverImg      = { width: '100px', height: '140px', objectFit: 'cover',
  borderRadius: '6px', flexShrink: 0 }
const coverPlaceholder = { width: '100px', height: '140px', background: '#e0e7ff',
  borderRadius: '6px', display: 'flex', alignItems: 'center', justifyContent: 'center',
  fontSize: '2.5rem', fontWeight: 700, color: '#6366f1', flexShrink: 0 }
const tag           = { fontSize: '0.75rem', padding: '2px 10px', borderRadius: '99px',
  background: '#f3f4f6', color: '#6b7280' }
const readLink      = { fontSize: '0.85rem', color: '#6366f1', textDecoration: 'none' }
const deleteBookBtn = { marginTop: '8px', background: 'none', border: '1px solid #fecaca',
  color: '#dc2626', padding: '5px 12px', borderRadius: '6px',
  cursor: 'pointer', fontSize: '0.8rem' }
const twoCol        = { display: 'grid', gridTemplateColumns: '1fr 1.4fr', gap: '1.5rem' }
const section       = { background: '#fff', border: '1px solid #e5e7eb',
  borderRadius: '12px', padding: '1.25rem' }
const sectionTitle  = { margin: '0 0 1rem', fontWeight: 600, fontSize: '1rem' }
const label         = { display: 'block', fontSize: '0.82rem', fontWeight: 500,
  color: '#374151', marginBottom: '4px' }
const numInput      = { width: '100%', padding: '8px 10px', border: '1px solid #d1d5db',
  borderRadius: '8px', fontSize: '0.9rem', marginBottom: '1rem', boxSizing: 'border-box' }
const textarea      = { width: '100%', padding: '10px', border: '1px solid #d1d5db',
  borderRadius: '8px', fontSize: '0.9rem', minHeight: '100px', resize: 'vertical',
  marginBottom: '0.75rem', boxSizing: 'border-box', fontFamily: 'inherit' }
const saveBtn       = { width: '100%', padding: '10px', background: '#6366f1', color: '#fff',
  border: 'none', borderRadius: '8px', cursor: 'pointer', fontWeight: 500, fontSize: '0.9rem' }
const typeBtn       = { padding: '5px 12px', border: '1px solid #e5e7eb', borderRadius: '99px',
  background: '#fff', cursor: 'pointer', fontSize: '0.78rem', color: '#374151' }
const typeBtnActive = { background: '#6366f1', color: '#fff', borderColor: '#6366f1' }
