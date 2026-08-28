import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";

import { Button } from "../components/ui/button";
import { Input } from "../components/ui/input";
import { Label } from "../components/ui/field";

const REGISTER_ENDPOINT =
  import.meta.env.VITE_REGISTER_API_URL ??
  "http://localhost:8081/api/users/addUser";

export function Register() {
  const navigate = useNavigate();

  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [phone, setPhone] = useState("");
  const [address, setAddress] = useState("");
  const [error, setError] = useState(null);
  const [submitting, setSubmitting] = useState(false);

  async function handleSubmit(e) {
    e.preventDefault();
    setError(null);

    if (password.length < 6) {
      setError("Password must be at least 6 characters.");
      return;
    }

    setSubmitting(true);
    try {
      const response = await fetch(REGISTER_ENDPOINT, {
        method: "POST",
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          first_name: firstName,
          last_name: lastName,
          email,
          password,
          phone,
          address,
        }),
      });

      const responseBody = await response.json().catch(() => null);

      if (!response.ok) {
        throw new Error(
          responseBody?.message ||
            responseBody?.error ||
            `Registration failed with status ${response.status}`,
        );
      }

      navigate("/", { replace: true });
    } catch (err) {
      console.error("Registration failed:", err);
      setError(err.message || "Registration failed. Please try again.");
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <div className="mx-auto flex min-h-[80vh] max-w-md flex-col justify-center px-4 py-12">
      <div className="rounded-2xl border border-border bg-card p-8 shadow-sm">
        <h1 className="font-display text-2xl font-semibold text-card-foreground">
          Create your account
        </h1>

        <p className="mt-1 text-sm text-muted-foreground">
          Join Morden Home furniture family.
        </p>

        <form onSubmit={handleSubmit} className="mt-6 flex flex-col gap-4">
          <div>
            <Label htmlFor="firstName">First Name</Label>

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
            <Label htmlFor="lastName">Last Name</Label>

            <Input
              id="lastName"
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
            <Label htmlFor="phone">Phone +27</Label>

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

          <div>
            <Label htmlFor="address">Address</Label>

            <Input
              id="address"
              type="text"
              autoComplete="address"
              required
              value={address}
              onChange={(e) => setAddress(e.target.value)}
              placeholder="00  Street Name"
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
