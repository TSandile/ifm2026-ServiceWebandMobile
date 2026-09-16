import { useEffect, useMemo, useState } from "react";
import { Link, useLocation } from "react-router-dom";
import { Check, Plus, X } from "lucide-react";
import { Button } from "../components/ui/button";
import {
  formatPrice,
  getComponentImageUrl,
  normalizeComponent,
} from "../lib/utils";

const COMPONENTS_ENDPOINT =
  import.meta.env.VITE_API_COMPONENTS_URL ??
  "http://localhost:8081/api/components/getAllComponents";

const PRODUCT_OPTIONS = [
  {
    value: "kitchen-chair",
    label: "Build Kitcken Chair",
    terms: ["kitchen chair", "chair"],
  },
  {
    value: "kitchen-table",
    label: "Build Kitchen Table",
    terms: ["kitchen table", "table"],
  },
  {
    value: "dining-chair",
    label: "Build Dinning chair",
    terms: ["dining chair", "dinning chair", "chair"],
  },
  {
    value: "dining-table",
    label: "Build a Dinning Table",
    terms: ["dining table", "dinning table", "table"],
  },
];

function getComponents(payload) {
  const components = Array.isArray(payload)
    ? payload
    : Array.isArray(payload?.data)
      ? payload.data
      : Array.isArray(payload?.components)
        ? payload.components
        : Array.isArray(payload?.payload)
          ? payload.payload
          : [];

  return components.map(normalizeComponent);
}

export function Customizer() {
  const location = useLocation();
  const startingComponent = location.state?.component;
  const [components, setComponents] = useState([]);
  const [selected, setSelected] = useState(
    startingComponent ? [normalizeComponent(startingComponent)] : [],
  );
  const [product, setProduct] = useState("");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const controller = new AbortController();

    async function loadComponents() {
      try {
        const response = await fetch(COMPONENTS_ENDPOINT, {
          signal: controller.signal,
          headers: { Accept: "application/json" },
        });

        if (!response.ok) {
          throw new Error(`Request failed with status ${response.status}`);
        }

        const payload = await response.json();
        setComponents(getComponents(payload));
      } catch (err) {
        if (err.name !== "AbortError") {
          console.error("Failed to load customizer components:", err);
          setError("We could not load the available components.");
        }
      } finally {
        if (!controller.signal.aborted) setLoading(false);
      }
    }

    loadComponents();

    return () => controller.abort();
  }, []);

  const selectedIds = useMemo(
    () => new Set(selected.map((component) => String(component.id))),
    [selected],
  );

  const total = selected.reduce(
    (sum, component) => sum + Number(component.price || 0),
    0,
  );

  const visibleComponents = useMemo(() => {
    if (!product) return components;

    const selectedProduct = PRODUCT_OPTIONS.find(
      (option) => option.value === product,
    );

    if (!selectedProduct) return components;

    return components.filter((component) => {
      const category = String(component.category ?? "").toLowerCase();
      return selectedProduct.terms.some((term) => category.includes(term));
    });
  }, [components, product]);

  function toggleComponent(component) {
    const componentId = String(component.id);

    setSelected((current) =>
      current.some((item) => String(item.id) === componentId)
        ? current.filter((item) => String(item.id) !== componentId)
        : [...current, component],
    );
  }

  return (
    <div className="mx-auto max-w-6xl px-4 py-10 sm:px-6">
      <Link to="/" className="text-sm font-medium text-primary hover:underline">
        Back to catalog
      </Link>

      <div className="mt-6 flex flex-col gap-3 sm:flex-row sm:items-end sm:justify-between">
        <div>
          <p className="text-sm font-medium uppercase tracking-wide text-primary">
            Build your set
          </p>
          <div className="mt-1 flex flex-col gap-3 sm:flex-row sm:items-center">
            <h1 className="font-display text-3xl font-semibold text-foreground">
              Customizer
            </h1>
            <label className="sr-only" htmlFor="product-builder">
              Customize by end Product
            </label>
            <select
              id="product-builder"
              value={product}
              onChange={(event) => setProduct(event.target.value)}
              className="rounded-lg border border-border bg-card px-3 py-2 text-sm font-medium text-card-foreground shadow-sm outline-none transition focus:border-primary focus:ring-2 focus:ring-primary/20"
            >
              <option value="">Cusomize by end Product</option>
              {PRODUCT_OPTIONS.map((option) => (
                <option key={option.value} value={option.value}>
                  {option.label}
                </option>
              ))}
            </select>
          </div>
          <p className="mt-2 text-muted-foreground">
            Select multiple components to assemble your desired furniture.
          </p>
        </div>

        <div className="rounded-xl border border-border bg-card px-4 py-3 sm:min-w-48">
          <p className="text-sm text-muted-foreground">
            {selected.length} selected
          </p>
          <p className="mt-1 text-xl font-semibold text-card-foreground">
            {formatPrice(total)}
          </p>
        </div>
      </div>

      <section className="mt-8 grid gap-8 lg:grid-cols-[minmax(0,1fr)_320px]">
        <div>
          <h2 className="font-display text-xl font-semibold text-foreground">
            Available components
          </h2>

          {loading ? (
            <p className="mt-6 text-muted-foreground">Loading components...</p>
          ) : error ? (
            <p className="mt-6 rounded-xl border border-dashed border-border p-6 text-muted-foreground">
              {error}
            </p>
          ) : visibleComponents.length === 0 ? (
            <p className="mt-6 rounded-xl border border-dashed border-border p-6 text-muted-foreground">
              No components are available for this product.
            </p>
          ) : (
            <div className="mt-4 grid gap-4 sm:grid-cols-2">
              {visibleComponents.map((component) => {
                const isSelected = selectedIds.has(String(component.id));
                const imageUrl = getComponentImageUrl(component);

                return (
                  <article
                    key={component.id}
                    className="overflow-hidden rounded-xl border border-border bg-card"
                  >
                    <div className="aspect-4/3 overflow-hidden bg-muted">
                      {imageUrl && (
                        <img
                          src={imageUrl}
                          alt={`Component ${component.id}`}
                          className="h-full w-full object-cover"
                          crossOrigin="anonymous"
                        />
                      )}
                    </div>
                    <div className="p-4">
                      <p className="text-xs uppercase tracking-wide text-primary">
                        {component.category}
                      </p>
                      <h3 className="mt-1 font-semibold text-card-foreground">
                        Component {component.id}
                      </h3>
                      <div className="mt-3 flex items-center justify-between gap-3">
                        <span className="font-medium text-foreground">
                          {formatPrice(component.price)}
                        </span>
                        <Button
                          type="button"
                          variant={isSelected ? "secondary" : "default"}
                          size="sm"
                          onClick={() => toggleComponent(component)}
                        >
                          {isSelected ? (
                            <Check aria-hidden="true" />
                          ) : (
                            <Plus aria-hidden="true" />
                          )}
                          {isSelected ? "Selected" : "Add"}
                        </Button>
                      </div>
                    </div>
                  </article>
                );
              })}
            </div>
          )}
        </div>

        <aside className="h-fit rounded-xl border border-border bg-card p-5 lg:sticky lg:top-24">
          <h2 className="font-display text-xl font-semibold text-card-foreground">
            Your furniture
          </h2>

          {selected.length === 0 ? (
            <p className="mt-4 text-sm text-muted-foreground">
              Add components to see your furniture assembly here.
            </p>
          ) : (
            <div className="mt-4 flex flex-col gap-3">
              {selected.map((component) => (
                <div
                  key={component.id}
                  className="flex items-center justify-between gap-3 border-b border-border pb-3"
                >
                  <div className="min-w-0">
                    <p className="truncate text-sm font-medium text-card-foreground">
                      Component {component.id}
                    </p>
                    <p className="text-sm text-muted-foreground">
                      {formatPrice(component.price)}
                    </p>
                  </div>
                  <button
                    type="button"
                    onClick={() => toggleComponent(component)}
                    className="shrink-0 rounded-md p-1 text-muted-foreground hover:bg-muted hover:text-foreground"
                    aria-label={`Remove component ${component.id}`}
                  >
                    <X className="h-4 w-4" />
                  </button>
                </div>
              ))}
              <p className="flex justify-between pt-1 font-semibold text-card-foreground">
                <span>Total</span>
                <span>{formatPrice(total)}</span>
              </p>
            </div>
          )}
        </aside>
      </section>
    </div>
  );
}
