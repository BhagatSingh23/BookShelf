import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import { AuthProvider } from './context/AuthContext'
import { GoogleOAuthProvider } from '@react-oauth/google'
import ProtectedRoute from './components/ProtectedRoute'

import Login      from './pages/Login'
import Register   from './pages/Register'
import OtpVerify  from './pages/OtpVerify'
import Dashboard  from './pages/Dashboard'
import BookSearch from './pages/BookSearch'
import BookDetail from './pages/BookDetail'
import AllEntries from './pages/AllEntries'

// Replace with your actual Google Client ID from console.cloud.google.com
const GOOGLE_CLIENT_ID = 'YOUR_GOOGLE_CLIENT_ID_HERE'

export default function App() {
  return (
    <GoogleOAuthProvider clientId={GOOGLE_CLIENT_ID}>
      <AuthProvider>
        <BrowserRouter>
          <Routes>
            {/* Public routes */}
            <Route path="/login"      element={<Login />} />
            <Route path="/register"   element={<Register />} />
            <Route path="/verify-otp" element={<OtpVerify />} />

            {/* Protected routes — redirect to /login if not authenticated */}
            <Route path="/dashboard" element={
              <ProtectedRoute><Dashboard /></ProtectedRoute>
            } />
            <Route path="/search" element={
              <ProtectedRoute><BookSearch /></ProtectedRoute>
            } />
            <Route path="/books/:id" element={
              <ProtectedRoute><BookDetail /></ProtectedRoute>
            } />
            <Route path="/entries" element={
              <ProtectedRoute><AllEntries /></ProtectedRoute>
            } />

            {/* Default redirect */}
            <Route path="*" element={<Navigate to="/dashboard" replace />} />
          </Routes>
        </BrowserRouter>
      </AuthProvider>
    </GoogleOAuthProvider>
  )
}
