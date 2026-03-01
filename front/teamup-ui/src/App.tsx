import { BrowserRouter, Link, Route, Routes } from './routerShim'
import './App.css'
import { AuthProvider, useAuth } from './context/AuthContext'
import { HomePage } from './pages/HomePage'
import { TeamDetailPage } from './pages/TeamDetailPage'
import { CreateTeamPage } from './pages/CreateTeamPage'
import { CreateCardPage } from './pages/CreateCardPage'
import { CardDetailPage } from './pages/CardDetailPage'
import { ProfilePage } from './pages/ProfilePage'
import { UsersPage } from './pages/UsersPage'
import { SignInPage } from './pages/auth/SignInPage'
import { RegisterPage } from './pages/auth/RegisterPage'
import { CallbackPage } from './pages/auth/CallbackPage'

function AppShell() {
  const { isAuthenticated, user, logout } = useAuth()

  return (
    <div className="app-shell">
      <header className="app-header">
        <h1 className="app-title">TeamUp</h1>
        <nav className="nav">
          <Link to="/" className="nav-link">Home</Link>
          <Link to="/create-team" className="nav-link">Create Team</Link>
          <Link to="/profile" className="nav-link">Profile</Link>
          {isAuthenticated ? (
            <>
              {user?.preferred_username && (
                <span className="nav-user">{user.preferred_username}</span>
              )}
              <button type="button" className="btn btn-secondary nav-logout" onClick={logout}>
                Logout
              </button>
            </>
          ) : (
            <>
              <Link to="/signin" className="nav-link">Sign in</Link>
              <Link to="/register" className="nav-link">Register</Link>
            </>
          )}
        </nav>
      </header>
      <main className="main">
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/teams/:teamId/create-card" element={<CreateCardPage />} />
          <Route path="/teams/:teamId" element={<TeamDetailPage />} />
          <Route path="/create-team" element={<CreateTeamPage />} />
          <Route path="/profile" element={<ProfilePage />} />
          <Route path="/cards/:cardId" element={<CardDetailPage />} />
          <Route path="/users" element={<UsersPage />} />
          <Route path="/signin" element={<SignInPage />} />
          <Route path="/register" element={<RegisterPage />} />
          <Route path="/callback" element={<CallbackPage />} />
        </Routes>
      </main>
    </div>
  )
}

function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <AppShell />
      </AuthProvider>
    </BrowserRouter>
  )
}

export default App
