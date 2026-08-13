import { createContext, useContext, useEffect, useState } from "react";
// import { supabase } from "@/lib/supabase";
import { supabase } from "../lib/supabase";

const AuthContext = createContext(undefined);

export function AuthProvider({ children }) {
  const [session, setSession] = useState(null);
  const [profile, setProfile] = useState(null);
  const [loading, setLoading] = useState(true);

  async function loadProfile(userId) {
    if (!supabase) return null;

    const { data } = await supabase
      .from("profiles")
      .select("*")
      .eq("id", userId)
      .maybeSingle();

    setProfile(data || null);
  }

  useEffect(() => {
    if (!supabase) {
      setLoading(false);
      return undefined;
    }

    let active = true;

    supabase.auth.getSession().then(async ({ data }) => {
      if (!active) return;

      setSession(data.session);

      if (data.session?.user) {
        await loadProfile(data.session.user.id);
      }

      setLoading(false);
    });

    const { data: sub } = supabase.auth.onAuthStateChange(
      (_event, newSession) => {
        setSession(newSession);

        if (newSession?.user) {
          loadProfile(newSession.user.id);
        } else {
          setProfile(null);
        }
      },
    );

    return () => {
      active = false;
      sub.subscription.unsubscribe();
    };
  }, []);

  async function signUp(email, password, fullName) {
    if (!supabase) {
      return {
        error:
          "Supabase is not configured. Add VITE_SUPABASE_URL and VITE_SUPABASE_ANON_KEY.",
        needsConfirmation: false,
      };
    }

    const { data, error } = await supabase.auth.signUp({
      email,
      password,
      options: {
        emailRedirectTo:
          import.meta.env.VITE_DEV_SUPABASE_REDIRECT_URL ||
          window.location.origin,
        data: {
          full_name: fullName,
          is_admin: false,
        },
      },
    });

    if (error) {
      return {
        error: error.message,
        needsConfirmation: false,
      };
    }

    const needsConfirmation = !data.session;

    return {
      error: null,
      needsConfirmation,
    };
  }

  async function signIn(email, password) {
    if (!supabase) {
      return {
        error:
          "Supabase is not configured. Add VITE_SUPABASE_URL and VITE_SUPABASE_ANON_KEY.",
      };
    }

    const { error } = await supabase.auth.signInWithPassword({
      email,
      password,
    });

    if (error) {
      if (error.message.toLowerCase().includes("email not confirmed")) {
        return {
          error: "Please confirm your email address before signing in.",
        };
      }

      if (error.status === 429) {
        return {
          error: "Too many attempts. Please wait a moment and try again.",
        };
      }

      if (error.message.toLowerCase().includes("invalid login")) {
        return {
          error: "Invalid email or password.",
        };
      }

      return {
        error: error.message,
      };
    }

    return {
      error: null,
    };
  }

  async function signOut() {
    if (!supabase) return;

    await supabase.auth.signOut();
    setProfile(null);
  }

  const value = {
    session,
    user: session?.user || null,
    profile,
    loading,
    isAdmin: profile?.is_admin || false,
    signUp,
    signIn,
    signOut,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const ctx = useContext(AuthContext);

  if (!ctx) {
    throw new Error("useAuth must be used within an AuthProvider");
  }

  return ctx;
}
