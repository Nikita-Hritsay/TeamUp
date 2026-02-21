import { createContext, useContext, useEffect, useMemo, useState } from 'react'
import type { ReactElement, ReactNode } from 'react'

type RouterContextValue = {
  pathname: string
  navigate: (to: string) => void
}

const RouterContext = createContext<RouterContextValue | undefined>(undefined)

type BrowserRouterProps = {
  children: ReactNode
}

export function BrowserRouter({ children }: BrowserRouterProps) {
  const [pathname, setPathname] = useState(window.location.pathname)

  useEffect(() => {
    const handlePopState = () => setPathname(window.location.pathname)
    window.addEventListener('popstate', handlePopState)
    return () => window.removeEventListener('popstate', handlePopState)
  }, [])

  const navigate = (to: string) => {
    if (to !== pathname) {
      window.history.pushState({}, '', to)
      setPathname(to)
    }
  }

  const value = useMemo(
    () => ({
      pathname,
      navigate,
    }),
    [pathname],
  )

  return <RouterContext.Provider value={value}>{children}</RouterContext.Provider>
}

type RouteProps = {
  path: string
  element: ReactElement
}

export function Route({ element }: RouteProps) {
  return element
}

type RoutesProps = {
  children: ReactNode
}

export function Routes({ children }: RoutesProps) {
  const router = useRouterContext()
  let match: ReactElement | null = null

  const routes = Array.isArray(children) ? children : [children]

  for (const child of routes) {
    if (!child || typeof child !== 'object') continue
    const element = (child as { props?: RouteProps }).props
    if (!element?.path) continue
    
    // Exact match
    if (element.path === router.pathname) {
      match = element.element
      break
    }
    
    // Parameterized route match (e.g., /cards/:cardId)
    const pathPattern = element.path.replace(/:[^/]+/g, '([^/]+)')
    const regex = new RegExp(`^${pathPattern}$`)
    if (regex.test(router.pathname)) {
      match = element.element
      break
    }
  }

  return match
}

type LinkProps = {
  to: string
  children: ReactNode
  className?: string
}

export function Link({ to, children, className }: LinkProps) {
  const router = useRouterContext()

  const handleClick = (event: React.MouseEvent<HTMLAnchorElement>) => {
    event.preventDefault()
    router.navigate(to)
  }

  return (
    <a href={to} onClick={handleClick} className={className}>
      {children}
    </a>
  )
}

export function useNavigate() {
  const router = useRouterContext()
  return router.navigate
}

export function useLocation() {
  const router = useRouterContext()
  return { pathname: router.pathname }
}

function useRouterContext() {
  const router = useContext(RouterContext)
  if (!router) {
    throw new Error('Router context is unavailable. Wrap components with BrowserRouter.')
  }
  return router
}
