import { useEffect, useState } from "react";

import { supabase } from "../lib/supabase";
import { AuthContext } from "./AuthContext.js";

const USER_ROLES = ["CUSTOMER", "CLERK", "MANAGER"];

function getUserRole(user) {
  const role = String(user?.role || "")
    .trim()
    .toUpperCase();

  return role || null;
}

function hasAdminRole(user) {
  return Boolean(
    user?.is_admin ||
    user?.isAdmin ||
    ["ADMIN", "ROLE_ADMIN"].includes(getUserRole(user)),
  );
}

export function AuthProvider({ children }) {
  const [session, setSession] = useState(null);
  const [apiUser, setApiUser] = useState(() => {
    try {
      return JSON.parse(sessionStorage.getItem("morden-api-user")) || null;
    } catch {
      return null;
    }
  });
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
    try {
      const response = await fetch("http://localhost:8081/api/users/login", {
        method: "POST",
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ email, password }),
      });

      const responseBody = await response.json().catch(() => null);

      if (!response.ok) {
        return {
          error:
            responseBody?.message ||
            responseBody?.error ||
            `Sign in failed with status ${response.status}`,
        };
      }

      const loggedInUser =
        responseBody?.user ||
        responseBody?.payload ||
        responseBody?.data?.user ||
        responseBody?.data?.payload ||
        responseBody?.data ||
        responseBody;

      const user =
        loggedInUser && typeof loggedInUser === "object"
          ? loggedInUser
          : { email };

      setApiUser(user);
      sessionStorage.setItem("morden-api-user", JSON.stringify(user));

      return {
        error: null,
        data: responseBody,
        isAdmin: hasAdminRole(loggedInUser),
      };
    } catch (err) {
      console.error("Sign in request failed:", err);
      return {
        error: "Unable to connect to the sign in service. Please try again.",
      };
    }
  }

  async function signOut() {
    setApiUser(null);
    sessionStorage.removeItem("morden-api-user");

    if (supabase) {
      await supabase.auth.signOut();
    }

    setSession(null);
    setProfile(null);
  }

  const value = {
    session,
    user: apiUser || session?.user,
    role: getUserRole(apiUser || session?.user),
    profile,
    loading,
    isAdmin:
      profile?.is_admin ||
      apiUser?.is_admin ||
      apiUser?.isAdmin ||
      hasAdminRole(apiUser),
    isCustomer: getUserRole(apiUser || session?.user) === USER_ROLES[0],
    isClerk: getUserRole(apiUser || session?.user) === USER_ROLES[1],
    isManager: getUserRole(apiUser || session?.user) === USER_ROLES[2],
    signUp,
    signIn,
    signOut,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}
