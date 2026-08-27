import { useEffect, useMemo, useState } from "react";
import { ArrowRight } from "lucide-react";
import { getComponentImageUrl } from "../lib/utils";

const COMPONENTS_ENDPOINT =
  import.meta.env.VITE_API_COMPONENTS_URL ??
  "http://localhost:8080/api/components/getAllComponents";

function getComponents(payload) {
  if (Array.isArray(payload)) return payload;
  if (Array.isArray(payload?.data)) return payload.data;
  if (Array.isArray(payload?.components)) return payload.components;
  return [];
}

function FurnitureCard({ item }) {
  const image = getComponentImageUrl(item);

  return (
    <article className="overflow-hidden rounded-2xl border border-border bg-background shadow-sm">
      {image ? (
        <img
          src={image}
          alt={item.name}
          className="h-56 w-full object-cover"
          crossOrigin="anonymous"
          onError={(event) => {
            event.currentTarget.src = "/hero-furniture.png";
          }}
        />
      ) : (
        <img
          src="/hero-furniture.png"
          alt={item.name}
          className="h-56 w-full object-cover"
        />
      )}
      <div className="p-4">
        <div className="flex items-start justify-between gap-3">
          <div>
            <p className="text-xs uppercase tracking-[0.2em] text-muted-foreground">
              {item.category}
            </p>
            <h3 className="mt-2 text-xl font-semibold text-foreground">
              {item.name}
            </h3>
          </div>
          <span className="rounded-full bg-primary/10 px-2 py-1 text-sm font-medium text-primary">
            {new Intl.NumberFormat("en-US", {
              style: "currency",
              currency: "USD",
              maximumFractionDigits: 0,
            }).format(item.price)}
          </span>
        </div>

        <p className="mt-3 line-clamp-3 text-sm text-muted-foreground">
          {item.description ||
            "Modular furniture designed for flexible living spaces."}
        </p>

        <div className="mt-4 flex items-center justify-between text-sm text-muted-foreground">
          <span>{item.in_stock ? "In stock" : "Out of stock"}</span>
          <span>{item.dimensions || "Custom sizing"}</span>
        </div>
      </div>
    </article>
  );
}

export function Home() {
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [activeCategory, setActiveCategory] = useState("All");

  useEffect(() => {
    const controller = new AbortController();

    async function fetchComponents() {
      setLoading(true);
      setError("");
      try {
        const response = await fetch(COMPONENTS_ENDPOINT, {
          signal: controller.signal,
          headers: { Accept: "application/json" },
        });

        if (!response.ok) {
          throw new Error(`Request failed with status ${response.status}`);
        }

        const payload = await response.json();
        setItems(getComponents(payload));
      } catch (err) {
        if (err.name !== "AbortError") {
          console.error("Failed to load components:", err);
          setItems([]);
          setError("We could not load the components. Please try again.");
        }
      } finally {
        if (!controller.signal.aborted) setLoading(false);
      }
    }

    fetchComponents();

    return () => controller.abort();
  }, []);

  const categories = useMemo(() => {
    return ["All", ...Array.from(new Set(items.map((i) => i.category)))];
  }, [items]);

  const filtered =
    activeCategory === "All"
      ? items
      : items.filter((i) => i.category === activeCategory);

  return (
    <div>
      <section className="border-b border-border bg-secondary/40">
        <div className="mx-auto grid max-w-6xl gap-8 px-4 py-16 sm:px-6 lg:grid-cols-2 lg:items-center lg:py-24">
          <div>
            <span className="inline-flex items-center rounded-full bg-primary/10 px-3 py-1 text-sm font-medium text-accent">
              Morden by design
            </span>
            <h1 className="mt-4 font-display text-4xl font-semibold leading-tight tracking-tight text-foreground text-balance sm:text-5xl">
              Furniture components that fit together, beautifully.
            </h1>
            <p className="mt-4 max-w-md text-pretty text-lg text-muted-foreground">
              Browse our catalog of modular pieces and discover which components
              pair perfectly to build your ideal space.
            </p>
            <a
              href="#catalog"
              className="mt-6 inline-flex items-center gap-2 text-base font-medium text-primary hover:underline"
            >
              Explore the catalog <ArrowRight className="h-4 w-4" />
            </a>
          </div>

          <div className="relative aspect-4/3 overflow-hidden rounded-2xl border border-border bg-muted">
            <img
              src="/hero-furniture.png"
              alt="A styled living space built from modular Nordhaus furniture components"
              className="h-full w-full object-cover"
              crossOrigin="anonymous"
            />
          </div>
        </div>
      </section>

      <section id="catalog" className="mx-auto max-w-6xl px-4 py-12 sm:px-6">
        <div className="flex flex-col gap-4 sm:flex-row sm:items-end sm:justify-between">
          <div>
            <h2 className="font-display text-2xl font-semibold text-foreground">
              The catalog
            </h2>
            <p className="mt-1 text-sm text-muted-foreground">
              {filtered.length} components available
            </p>
          </div>

          <div className="flex flex-wrap gap-2">
            {categories.map((cat) => (
              <button
                key={cat}
                onClick={() => setActiveCategory(cat)}
                className={
                  activeCategory === cat
                    ? "rounded-full bg-primary px-4 py-1.5 text-sm font-medium text-primary-foreground"
                    : "rounded-full border border-border bg-background px-4 py-1.5 text-sm font-medium text-muted-foreground hover:border-primary hover:text-foreground"
                }
              >
                {cat}
              </button>
            ))}
          </div>
        </div>

        {loading ? (
          <div className="grid gap-6 py-12 sm:grid-cols-2 lg:grid-cols-3">
            {Array.from({ length: 6 }).map((_, i) => (
              <div key={i} className="animate-pulse">
                <div className="aspect-4/3 rounded-xl bg-muted" />
                <div className="mt-3 h-4 w-2/3 rounded bg-muted" />
                <div className="mt-2 h-4 w-1/3 rounded bg-muted" />
              </div>
            ))}
          </div>
        ) : error ? (
          <div className="rounded-xl border border-dashed border-border py-20 text-center">
            <p className="text-muted-foreground">{error}</p>
            <button
              type="button"
              onClick={() => window.location.reload()}
              className="mt-4 rounded-full bg-primary px-4 py-2 text-sm font-medium text-primary-foreground"
            >
              Try again
            </button>
          </div>
        ) : filtered.length === 0 ? (
          <div className="rounded-xl border border-dashed border-border py-20 text-center">
            <p className="text-muted-foreground">
              No furniture components yet. Check back soon.
            </p>
          </div>
        ) : (
          <div className="mt-8 grid gap-6 sm:grid-cols-2 lg:grid-cols-3">
            {filtered.map((item) => (
              <FurnitureCard key={item.id} item={item} />
            ))}
          </div>
        )}
      </section>
    </div>
  );
}
