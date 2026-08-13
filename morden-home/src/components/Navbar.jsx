import { Link } from "react-router-dom";
import { LayoutGrid, LogOut, Shield, Sofa } from "lucide-react";
import { useAuth } from "../context/AuthContext";

function Button({
  children,
  variant = "default",
  size = "md",
  className = "",
  ...props
}) {
  const variantClass =
    variant === "ghost"
      ? "bg-transparent text-foreground hover:bg-muted"
      : variant === "outline"
        ? "border border-border bg-background text-foreground hover:bg-muted"
        : "bg-primary text-primary-foreground hover:opacity-90";

  const sizeClass =
    size === "sm"
      ? "h-9 px-3 text-sm"
      : size === "lg"
        ? "h-11 px-5"
        : "h-10 px-4";

  return (
    <button
      type="button"
      className={`inline-flex items-center justify-center gap-2 rounded-md font-medium transition ${variantClass} ${sizeClass} ${className}`}
      {...props}
    >
      {children}
    </button>
  );
}

export function Navbar() {
  const { user, isAdmin, signOut } = useAuth();

  const handleSignOut = async () => {
    await signOut();
  };

  return (
    <header className="sticky top-0 z-40 border-b border-border bg-background/90 backdrop-blur">
      <div className="mx-auto flex h-16 max-w-6xl items-center justify-between px-4 sm:px-6">
        <Link to="/" className="flex items-center gap-2">
          <span className="flex h-8 w-8 items-center justify-center rounded-lg bg-primary text-primary-foreground">
            <Sofa className="h-4 w-4" />
          </span>
          <span className="font-display text-xl font-semibold tracking-tight">
            Morden Furn
          </span>
        </Link>

        <nav className="flex items-center gap-1 sm:gap-2">
          <Link to="/">
            <Button variant="ghost" size="sm">
              <LayoutGrid className="h-4 w-4" />
              <span className="hidden sm:inline">Catalog</span>
            </Button>
          </Link>

          {isAdmin && (
            <Link to="/admin">
              <Button variant="ghost" size="sm">
                <Shield className="h-4 w-4" />
                <span className="hidden sm:inline">Admin</span>
              </Button>
            </Link>
          )}

          {user ? (
            <Button variant="outline" size="sm" onClick={handleSignOut}>
              <LogOut className="h-4 w-4" />
              <span className="hidden sm:inline">Sign out</span>
            </Button>
          ) : (
            <div className="flex items-center gap-2">
              <Link to="/login">
                <Button variant="ghost" size="sm">
                  Sign in
                </Button>
              </Link>
              <Link to="/register">
                <Button size="sm">Register</Button>
              </Link>
            </div>
          )}
        </nav>
      </div>
    </header>
  );
}
