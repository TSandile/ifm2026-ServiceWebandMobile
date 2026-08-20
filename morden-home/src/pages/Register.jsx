import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { CheckCircle2 } from "lucide-react";
import { useAuth } from "../context/AuthContext";

import { Button } from "../components/ui/button";
import { Input } from "../components/ui/input";
import { Label } from "../components/ui/field";
// import { Button } from "@/components/ui/button"
// import { Input } from "@/components/ui/input"
// import { Label } from "@/components/ui/field"

export function Register() {
  const { signUp } = useAuth();
  const navigate = useNavigate();

  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [phone, setPhone] = useState("");
  const [error, setError] = useState(null);
  const [submitting, setSubmitting] = useState(false);
  const [confirmSent, setConfirmSent] = useState(false);

  async function handleSubmit(e) {
    e.preventDefault();
    setError(null);

    if (password.length < 6) {
      setError("Password must be at least 6 characters.");
      return;
    }

    setSubmitting(true);
    const { error, needsConfirmation } = await signUp(
      email,
      password,
      fullName,
    );
    setSubmitting(false);

    if (error) {
      setError(error);
      return;
    }

    if (needsConfirmation) {
      setConfirmSent(true);
      return;
    }

    navigate("/", { replace: true });
  }

  if (confirmSent) {
    return (
      <div className="mx-auto flex min-h-[80vh] max-w-md flex-col justify-center px-4 py-12">
        <div className="rounded-2xl border border-border bg-card p-8 text-center shadow-sm">
          <CheckCircle2 className="mx-auto h-12 w-12 text-primary" />

          <h1 className="mt-4 font-display text-2xl font-semibold text-card-foreground">
            Check your inbox
          </h1>

          <p className="mt-2 text-sm text-muted-foreground">
            We sent a confirmation link to{" "}
            <span className="font-medium text-foreground">{email}</span>.
            Confirm your email, then sign in.
          </p>

          <Link to="/login" className="mt-6 inline-block">
            <Button size="lg">Go to sign in</Button>
          </Link>
        </div>
      </div>
    );
  }

  return (
    <div className="mx-auto flex min-h-[80vh] max-w-md flex-col justify-center px-4 py-12">
      <div className="rounded-2xl border border-border bg-card p-8 shadow-sm">
        <h1 className="font-display text-2xl font-semibold text-card-foreground">
          Create your account
        </h1>

        <p className="mt-1 text-sm text-muted-foreground">
          Join Morden Home furn family.
        </p>

        <form onSubmit={handleSubmit} className="mt-6 flex flex-col gap-4">
          <div>
            <Label htmlFor="fullName">First Name</Label>

            <Input
              id="firstName"
              type="text"
              autoComplete="name"
              required
              value={firstName}
              onChange={(e) => setFirstName(e.target.value)}
              placeholder="Msizi"
            />
          </div>

          <div>
            <Label htmlFor="fullName">Last Name</Label>

            <Input
              id="LASTName"
              type="text"
              autoComplete="name"
              required
              value={lastName}
              onChange={(e) => setLastName(e.target.value)}
              placeholder="Tshabalala"
            />
          </div>

          <div>
            <Label htmlFor="email">Email</Label>

            <Input
              id="email"
              type="email"
              autoComplete="email"
              required
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="you@example.com"
            />
          </div>

          <div>
            <Label htmlFor="password">Password</Label>

            <Input
              id="password"
              type="password"
              autoComplete="new-password"
              required
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="At least 6 characters"
            />
          </div>

          <div>
            <Label htmlFor="fullName">Phone +27</Label>

            <Input
              id="phone"
              type="text"
              //   autoComplete="name"
              required
              value={phone}
              onChange={(e) => setPhone(e.target.value)}
              placeholder="012 345 6789"
            />
          </div>

          {error && (
            <p
              className="rounded-lg bg-destructive/10 px-3 py-2 text-sm text-destructive"
              role="alert"
            >
              {error}
            </p>
          )}

          <Button type="submit" size="lg" disabled={submitting}>
            {submitting ? "Creating account…" : "Create account"}
          </Button>
        </form>

        <p className="mt-6 text-center text-sm text-muted-foreground">
          Already have an account?{" "}
          <Link
            to="/login"
            className="font-medium text-primary hover:underline"
          >
            Sign in
          </Link>
        </p>
      </div>
    </div>
  );
}
