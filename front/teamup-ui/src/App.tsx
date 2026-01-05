import { BrowserRouter, Link, Route, Routes } from 'react-router-dom'
import './App.css'
import { CardsPage } from './pages/CardsPage'
import { TeamsPage } from './pages/TeamsPage'
import { UsersPage } from './pages/UsersPage'

function App() {
  return (
    <BrowserRouter>
      <div className="app-shell">
        <header className="app-header">
          <h1>TeamUp Dashboard</h1>
          <nav className="nav">
            <Link to="/cards">Cards</Link>
            <Link to="/teams">Teams</Link>
            <Link to="/users">Users</Link>
          </nav>
        </header>
        <main>
          <Routes>
            <Route path="/" element={<CardsPage />} />
            <Route path="/cards" element={<CardsPage />} />
            <Route path="/teams" element={<TeamsPage />} />
            <Route path="/users" element={<UsersPage />} />
          </Routes>
        </main>
      </div>
    </BrowserRouter>
  )
}

export default App
