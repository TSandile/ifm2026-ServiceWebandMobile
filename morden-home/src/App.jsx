// import "./App.css";

import { Route, Routes } from "react-router-dom";
import { Navbar } from "./components/Navbar";
import { Home } from "./pages/Home";
import { Register } from "./pages/Register";

function NotFound() {
  return (
    <div className="flex min-h-[40vh] items-center justify-center px-4 text-center">
      <div>
        <h1 className="text-3xl font-bold">404</h1>
        <p className="mt-2 text-muted-foreground">Page not found.</p>
      </div>
    </div>
  );
}

function App() {
  return (
    <div className="flex min-h-screen flex-col">
      <Navbar />
      <main className="flex-1">
        <Routes>
          <Route path="/" element={<Home />} />
          {/* <Route path="/furniture/:id" element={<FurnitureDetail />} /> */}
          {/* <Route path="/login" element={<Login />} /> */}
          <Route path="/register" element={<Register />} />
          {/* <Route
            path="/admin"
            element={
              <RequireAdmin>
                <Admin />
              </RequireAdmin>
            }
          /> */}
          <Route path="*" element={<NotFound />} />
        </Routes>
      </main>
      <footer className="border-t border-border py-8">
        <div className="mx-auto max-w-6xl px-4 text-sm text-muted-foreground sm:px-6">
          Build Your Morden Home, With Morden Furniture.
        </div>
      </footer>
    </div>
  );
}

export default App;
