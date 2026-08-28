import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import { ArrowLeft, Ruler, Link2, Sofa } from "lucide-react";
import { supabase } from "../lib/supabase";
//import { supabase } from "@/lib/supabase";
import { FurnitureCard } from "../components/ComponentCard";
//import { FurnitureCard } from "@/components/FurnitureCard";
import { formatPrice, getComponentImageUrl } from "../lib/utils";
//import { formatPrice } from "@/lib/utils";

export function FurnitureDetail() {
  const { id } = useParams();
  const [item, setItem] = useState(null);
  const [compatible, setCompatible] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!id) return;
    setLoading(true);

    async function load() {
      const { data: current } = await supabase
        .from("furniture")
        .select("*")
        .eq("id", id)
        .maybeSingle();

      setItem(current);

      // Find all pairings that reference this item, in either column.
      const { data: pairs } = await supabase
        .from("furniture_compatibility")
        .select("*")
        .or(`furniture_a.eq.${id},furniture_b.eq.${id}`);

      if (pairs && pairs.length > 0) {
        const otherIds = pairs.map((p) =>
          p.furniture_a === id ? p.furniture_b : p.furniture_a,
        );

        const { data: others } = await supabase
          .from("furniture")
          .select("*")
          .in("id", otherIds);

        const noteById = new Map();

        pairs.forEach((p) => {
          const otherId = p.furniture_a === id ? p.furniture_b : p.furniture_a;

          noteById.set(otherId, p.note);
        });

        const list = (others || []).map((o) => ({
          item: o,
          note: noteById.get(o.id) ?? null,
        }));

        setCompatible(list);
      } else {
        setCompatible([]);
      }

      setLoading(false);
    }

    load();
  }, [id]);

  if (loading) {
    return (
      <div className="flex min-h-[60vh] items-center justify-center">
        <div className="h-8 w-8 animate-spin rounded-full border-2 border-muted border-t-primary" />
      </div>
    );
  }

  if (!item) {
    return (
      <div className="mx-auto max-w-6xl px-4 py-20 text-center sm:px-6">
        <p className="text-muted-foreground">
          That component could not be found.
        </p>

        <Link
          to="/"
          className="mt-4 inline-block font-medium text-primary hover:underline"
        >
          Back to catalog
        </Link>
      </div>
    );
  }

  return (
    <div className="mx-auto max-w-6xl px-4 py-8 sm:px-6">
      <Link
        to="/"
        className="inline-flex items-center gap-1.5 text-sm text-muted-foreground hover:text-foreground"
      >
        <ArrowLeft className="h-4 w-4" />
        Back to catalog
      </Link>

      <div className="mt-6 grid gap-8 lg:grid-cols-2">
        <div className="relative aspect-4/3 overflow-hidden rounded-2xl border border-border bg-muted">
          {getComponentImageUrl(item) ? (
            <img
              src={getComponentImageUrl(item)}
              alt={`Component ${item.id}`}
              className="h-full w-full object-cover"
              crossOrigin="anonymous"
              onError={(event) => {
                event.currentTarget.style.display = "none";
              }}
            />
          ) : (
            <div className="flex h-full w-full items-center justify-center text-muted-foreground">
              <Sofa className="h-14 w-14" />
            </div>
          )}
        </div>

        <div>
          <span className="text-sm font-medium uppercase tracking-wide text-primary">
            {item.category}
          </span>

          <h1 className="mt-1 font-display text-3xl font-semibold text-foreground">
            Component #{item.id}
          </h1>

          <p className="mt-3 text-2xl font-semibold text-foreground">
            {formatPrice(item.price)}
          </p>

          <span
            className={
              item.in_stock
                ? "mt-3 inline-block rounded-full bg-secondary px-3 py-1 text-xs font-medium text-secondary-foreground"
                : "mt-3 inline-block rounded-full bg-muted px-3 py-1 text-xs font-medium text-muted-foreground"
            }
          >
            {item.in_stock ? "In stock" : "Out of stock"}
          </span>

          {item.description && (
            <p className="mt-5 text-pretty leading-relaxed text-muted-foreground">
              {item.description}
            </p>
          )}

          {item.dimensions && (
            <div className="mt-5 flex items-center gap-2 text-sm text-foreground">
              <Ruler className="h-4 w-4 text-primary" />
              <span>{item.dimensions}</span>
            </div>
          )}
        </div>
      </div>

      {/* Compatible components */}
      <section className="mt-14">
        <div className="flex items-center gap-2">
          <Link2 className="h-5 w-5 text-primary" />

          <h2 className="font-display text-2xl font-semibold text-foreground">
            Pairs well with
          </h2>
        </div>

        <p className="mt-1 text-sm text-muted-foreground">
          Components our studio recommends combining with Component #{item.id}.
        </p>

        {compatible.length === 0 ? (
          <div className="mt-6 rounded-xl border border-dashed border-border py-12 text-center">
            <p className="text-muted-foreground">
              No compatible components have been paired yet.
            </p>
          </div>
        ) : (
          <div className="mt-6 grid gap-6 sm:grid-cols-2 lg:grid-cols-3">
            {compatible.map(({ item: c, note }) => (
              <div key={c.id} className="flex flex-col gap-2">
                <FurnitureCard item={c} />

                {note && (
                  <p className="px-1 text-sm italic text-muted-foreground">
                    “{note}”
                  </p>
                )}
              </div>
            ))}
          </div>
        )}
      </section>
    </div>
  );
}
