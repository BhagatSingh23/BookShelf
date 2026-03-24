import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import { AuthProvider } from './context/AuthContext'
import { ThemeProvider } from './context/ThemeContext'
import { GoogleOAuthProvider } from '@react-oauth/google'
import ProtectedRoute from './components/ProtectedRoute'

import Login      from './pages/Login'
import Register   from './pages/Register'
import OtpVerify  from './pages/OtpVerify'
import Dashboard  from './pages/Dashboard'
import BookSearch from './pages/BookSearch'
import BookDetail from './pages/BookDetail'
import AllEntries from './pages/AllEntries'

const GOOGLE_CLIENT_ID = import.meta.env.VITE_GOOGLE_CLIENT_ID

export default function App() {
  return (
    <GoogleOAuthProvider clientId={GOOGLE_CLIENT_ID}>
      <ThemeProvider>
        <AuthProvider>
          <BrowserRouter>
            <Routes>
              <Route path="/login"      element={<Login />} />
              <Route path="/register"   element={<Register />} />
              <Route path="/verify-otp" element={<OtpVerify />} />
              <Route path="/dashboard"  element={<ProtectedRoute><Dashboard /></ProtectedRoute>} />
              <Route path="/search"     element={<ProtectedRoute><BookSearch /></ProtectedRoute>} />
              <Route path="/books/:id"  element={<ProtectedRoute><BookDetail /></ProtectedRoute>} />
              <Route path="/entries"    element={<ProtectedRoute><AllEntries /></ProtectedRoute>} />
              <Route path="*"           element={<Navigate to="/dashboard" replace />} />
            </Routes>
          </BrowserRouter>
        </AuthProvider>
      </ThemeProvider>
    </GoogleOAuthProvider>
  )
}
