import { BrowserRouter, Link, Route, Routes } from './routerShim'
import './App.css'
import { AuthProvider, useAuth } from './context/AuthContext'
import { CardsPage } from './pages/CardsPage'
import { CardDetailPage } from './pages/CardDetailPage'
import { TeamsPage } from './pages/TeamsPage'
import { AllTeamsPage } from './pages/AllTeamsPage'
import { TeamDetailPage } from './pages/TeamDetailPage'
import { UsersPage } from './pages/UsersPage'
import { SignInPage } from './pages/auth/SignInPage'
import { RegisterPage } from './pages/auth/RegisterPage'
import { CallbackPage } from './pages/auth/CallbackPage'

function AppShell() {
  const { isAuthenticated, user, logout } = useAuth()

  return (
    <div className="app-shell">
      <header className="app-header">
        <h1>TeamUp Dashboard</h1>
        <nav className="nav">
          <Link to="/cards">Cards</Link>
          <Link to="/teams">My Teams</Link>
          <Link to="/teams/all">All Teams</Link>
          <Link to="/users">Users</Link>
          {isAuthenticated ? (
            <>
              {user?.preferred_username && (
                <span className="nav-user">{user.preferred_username}</span>
              )}
              <button type="button" className="nav-logout" onClick={logout}>
                Logout
              </button>
            </>
          ) : (
            <>
              <Link to="/signin">Sign in</Link>
              <Link to="/register">Register</Link>
            </>
          )}
        </nav>
      </header>
      <main>
        <Routes>
          <Route path="/" element={<CardsPage />} />
          <Route path="/cards" element={<CardsPage />} />
          <Route path="/cards/:cardId" element={<CardDetailPage />} />
          <Route path="/teams" element={<TeamsPage />} />
          <Route path="/teams/all" element={<AllTeamsPage />} />
          <Route path="/teams/:teamId" element={<TeamDetailPage />} />
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
