import { Link } from "react-router-dom";
import { Sofa } from "lucide-react";
import {
  formatPrice,
  getComponentImageUrl,
  normalizeComponent,
} from "../lib/utils";
import { useAuth } from "../context/AuthContext.js";

export function FurnitureCard({ item }) {
  const normalizedItem = normalizeComponent(item);
  const imageUrl = getComponentImageUrl(normalizedItem);
  const { isAdmin, isClerk, isCustomer } = useAuth();

  return (
    <article className="group flex flex-col overflow-hidden rounded-xl border border-border bg-card transition-all hover:border-primary hover:shadow-lg hover:shadow-primary/5">
      <Link
        to={`/furniture/${normalizedItem.id}`}
        className="flex flex-1 flex-col"
      >
        <div className="relative aspect-4/3 overflow-hidden bg-muted">
          {imageUrl ? (
            <img
              src={imageUrl}
              alt={`Component ${normalizedItem.id}`}
              className="h-full w-full object-cover transition-transform duration-500 group-hover:scale-105"
              crossOrigin="anonymous"
              onError={(event) => {
                event.currentTarget.style.display = "none";
              }}
            />
          ) : (
            <div className="flex h-full w-full items-center justify-center text-muted-foreground">
              <Sofa className="h-10 w-10" />
            </div>
          )}

          {!normalizedItem.in_stock && (
            <span className="absolute left-3 top-3 rounded-full bg-foreground/80 px-2.5 py-1 text-xs font-medium text-background">
              Out of stock
            </span>
          )}
        </div>

        <div className="flex flex-1 flex-col p-4">
          <span className="text-xs font-medium uppercase tracking-wide text-primary">
            {normalizedItem.category}
          </span>

          <h3 className="mt-1 font-display text-lg font-semibold leading-tight text-card-foreground">
            Component {normalizedItem.id}
          </h3>

          {normalizedItem.description && (
            <p className="mt-1 line-clamp-2 text-sm text-muted-foreground">
              {normalizedItem.description}
            </p>
          )}

          <span className="mt-3 text-base font-semibold text-foreground">
            {formatPrice(normalizedItem.price)}
          </span>
        </div>
      </Link>

      {(isAdmin || isClerk) && (
        <Link
          to={`/admin?componentId=${encodeURIComponent(normalizedItem.id)}`}
          state={{ component: normalizedItem }}
          className="mx-4 mb-4 rounded-lg border border-primary px-3 py-2 text-center text-sm font-medium text-primary hover:bg-primary hover:text-primary-foreground"
        >
          Manage component
        </Link>
      )}

      {isCustomer && (
        <Link
          to="/customizer"
          state={{ component: item }}
          className="mx-4 mb-4 rounded-lg border border-primary px-3 py-2 text-center text-sm font-medium text-primary hover:bg-primary hover:text-primary-foreground"
        >
          Add to Custom
        </Link>
      )}

      {/* {isCustomer && (
        <Button type="button" className="mx-4 mb-4">
          Add to Custom
        </Button>
      )} */}
    </article>
  );
}
