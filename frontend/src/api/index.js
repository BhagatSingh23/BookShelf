import api from './axios'

// ── Auth ──────────────────────────────────────────────────────
export const register = (data) => api.post('/auth/register', data)
export const login = (data) => api.post('/auth/login', data)
export const verifyOtp = (data) => api.post('/auth/verify-otp', data)
export const sendOtp = (email) => api.post('/auth/send-otp', { email })
export const googleLogin = (idToken) => api.post('/auth/google', { idToken })

// ── Books ─────────────────────────────────────────────────────
export const getBooks = () => api.get('/books')
export const getBook = (id) => api.get(`/books/${id}`)
export const addBook = (data) => api.post('/books', data)
export const deleteBook = (id) => api.delete(`/books/${id}`)

// ── Reading Progress ──────────────────────────────────────────
export const updateProgress = (bookId, data) =>
  api.patch(`/books/${bookId}/progress`, data)

// ── Saved Entries (quotes, notes, insights) ───────────────────
export const getEntriesForBook = (bookId) =>
  api.get(`/books/${bookId}/entries`)
export const getAllEntries = () => api.get('/entries')
export const addEntry = (bookId, data) =>
  api.post(`/books/${bookId}/entries`, data)
export const deleteEntry = (entryId) => api.delete(`/entries/${entryId}`)

// ── Open Library Search ───────────────────────────────────────
export const searchBooks = (query) => api.get(`/search?q=${encodeURIComponent(query)}`)
