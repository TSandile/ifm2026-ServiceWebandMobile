import { Link } from "react-router-dom";
import { Sofa } from "lucide-react";
import { formatPrice, getComponentImageUrl } from "../lib/utils";
import { useAuth } from "../context/AuthContext.js";

export function FurnitureCard({ item }) {
  const imageUrl = getComponentImageUrl(item);
  const { isAdmin } = useAuth();

  return (
    <article className="group flex flex-col overflow-hidden rounded-xl border border-border bg-card transition-all hover:border-primary hover:shadow-lg hover:shadow-primary/5">
      <Link to={`/furniture/${item.id}`} className="flex flex-1 flex-col">
        <div className="relative aspect-4/3 overflow-hidden bg-muted">
          {imageUrl ? (
            <img
              src={imageUrl}
              alt={`Component ${item.id}`}
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

          {!item.in_stock && (
            <span className="absolute left-3 top-3 rounded-full bg-foreground/80 px-2.5 py-1 text-xs font-medium text-background">
              Out of stock
            </span>
          )}
        </div>

        <div className="flex flex-1 flex-col p-4">
          <span className="text-xs font-medium uppercase tracking-wide text-primary">
            {item.category}
          </span>

          <h3 className="mt-1 font-display text-lg font-semibold leading-tight text-card-foreground">
            Component #{item.id}
          </h3>

          {item.description && (
            <p className="mt-1 line-clamp-2 text-sm text-muted-foreground">
              {item.description}
            </p>
          )}

          <span className="mt-3 text-base font-semibold text-foreground">
            {formatPrice(item.price)}
          </span>
        </div>
      </Link>

      {isAdmin && (
        <Link
          to={`/admin?componentId=${encodeURIComponent(item.id)}`}
          state={{ component: item }}
          className="mx-4 mb-4 rounded-lg border border-primary px-3 py-2 text-center text-sm font-medium text-primary hover:bg-primary hover:text-primary-foreground"
        >
          Manage component
        </Link>
      )}
    </article>
  );
}
