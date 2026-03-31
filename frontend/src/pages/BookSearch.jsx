import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import Navbar from '../components/Navbar'
import SearchResultCard from '../components/SearchResultCard'
import { searchBooks, addBook } from '../api'

export default function BookSearch() {
  const [query, setQuery]     = useState('')
  const [results, setResults] = useState([])
  const [loading, setLoading] = useState(false)
  const [adding, setAdding]   = useState(null)
  const [added, setAdded]     = useState(new Set())
  const [error, setError]     = useState('')
  const navigate              = useNavigate()

  const handleSearch = async (e) => {
    e.preventDefault()
    if (!query.trim()) return
    setLoading(true); setError(''); setResults([])
    try {
      const res = await searchBooks(query)
      setResults(res.data)
      if (res.data.length === 0) setError('No books found. Try a different search term.')
    } catch {
      setError('Search failed. Please try again.')
    } finally { setLoading(false) }
  }

  const handleAdd = async (book) => {
    const key = book.olBookId || book.title
    setAdding(key)
    try {
      const res = await addBook({
        title:      book.title,
        author:     book.author,
        genre:      book.genre,
        totalPages: book.totalPages,
        coverUrl:   book.coverUrl,
        olBookId:   book.olBookId,
        olUrl:      book.olUrl,
      })
      setAdded(prev => new Set([...prev, key]))
      navigate(`/books/${res.data.id}`)
    } catch {
      setError('Failed to add book. Please try again.')
    } finally { setAdding(null) }
  }

  return (
    <div style={{ minHeight: '100vh', background: '#f9fafb' }}>
      <Navbar />
      <div style={container}>
        <h2 style={{ marginBottom: '1.5rem', fontWeight: 600, fontSize: '1.3rem' }}>
          Search books
        </h2>
        <form onSubmit={handleSearch} style={{ display: 'flex', gap: '10px', marginBottom: '2rem' }}>
          <input
            style={searchInput}
            value={query}
            onChange={e => setQuery(e.target.value)}
            placeholder="Search by title, author, or keyword..."
            autoFocus
          />
          <button style={searchBtn} type="submit" disabled={loading}>
            {loading ? 'Searching...' : 'Search'}
          </button>
        </form>
        {error && <p style={{ color: '#dc2626', marginBottom: '1rem', fontSize: '0.9rem' }}>{error}</p>}
        <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
          {results.map((book, i) => {
            const key = book.olBookId || book.title
            return (
              <SearchResultCard
                key={i}
                book={book}
                onAdd={handleAdd}
                isAdded={added.has(key)}
                isAdding={adding === key}
              />
            )
          })}
        </div>
        {results.length === 0 && !loading && !error && (
          <div style={{ textAlign: 'center', marginTop: '5rem', color: '#9ca3af' }}>
            <div style={{ fontSize: '3rem', marginBottom: '1rem' }}>🔍</div>
            <p style={{ fontSize: '1rem', marginBottom: '6px' }}>Search for any book, author, or topic</p>
            <p style={{ fontSize: '0.85rem' }}>Powered by Open Library — millions of books available, free</p>
          </div>
        )}
        {loading && (
          <div style={{ textAlign: 'center', marginTop: '3rem', color: '#6b7280' }}>
            Searching Open Library...
          </div>
        )}
      </div>
    </div>
  )
}

const container   = { maxWidth: '860px', margin: '0 auto', padding: '2rem 1.5rem' }
const searchInput = { flex: 1, padding: '11px 14px', border: '1px solid #d1d5db',
  borderRadius: '8px', fontSize: '0.95rem', outline: 'none' }
const searchBtn   = { padding: '11px 24px', background: '#6366f1', color: '#fff',
  border: 'none', borderRadius: '8px', cursor: 'pointer', fontWeight: 500,
  fontSize: '0.95rem', whiteSpace: 'nowrap' }
