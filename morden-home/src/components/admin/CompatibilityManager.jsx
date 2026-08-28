import { useEffect, useState } from "react";
import { Link2, Plus, Trash2 } from "lucide-react";
import { supabase } from "../../lib/supabase";
import { Button } from "../ui/button";
// import { Button } from "@/components/ui/button";
import { Label, Select, Textarea } from "../ui/field";
// import { Label, Select, Textarea } from "@/components/ui/field";

export function CompatibilityManager() {
  const [furniture, setFurniture] = useState([]);
  const [pairs, setPairs] = useState([]);
  const [aId, setAId] = useState("");
  const [bId, setBId] = useState("");
  const [note, setNote] = useState("");
  const [error, setError] = useState(null);
  const [saving, setSaving] = useState(false);

  async function load() {
    const [{ data: f }, { data: p }] = await Promise.all([
      supabase.from("furniture").select("*").order("name"),

      supabase
        .from("furniture_compatibility")
        .select("*")
        .order("created_at", { ascending: false }),
    ]);

    const list = f ?? [];

    setFurniture(list);

    const byId = new Map(list.map((item) => [item.id, item]));

    setPairs(
      (p ?? []).map((row) => ({
        ...row,
        a: byId.get(row.furniture_a),
        b: byId.get(row.furniture_b),
      })),
    );
  }

  useEffect(() => {
    load();
  }, []);

  async function handleSubmit(e) {
    e.preventDefault();
    setError(null);

    if (!aId || !bId) {
      return setError("Select two components.");
    }

    if (aId === bId) {
      return setError("A component cannot pair with itself.");
    }

    // Normalize order so (A,B) and (B,A) are treated as the same pair.
    const [furniture_a, furniture_b] = [aId, bId].sort();

    setSaving(true);

    const { error } = await supabase.from("furniture_compatibility").insert({
      furniture_a,
      furniture_b,
      note: note.trim() || null,
    });

    setSaving(false);

    if (error) {
      if (error.code === "23505") {
        return setError("These two components are already paired.");
      }

      return setError(error.message);
    }

    setAId("");
    setBId("");
    setNote("");

    await load();
  }

  async function handleDelete(id) {
    if (!confirm("Remove this pairing?")) return;

    const { error } = await supabase
      .from("furniture_compatibility")
      .delete()
      .eq("id", id);

    if (error) {
      setError(error.message);
      return;
    }

    await load();
  }

  return (
    <div className="grid gap-8 lg:grid-cols-[380px_1fr]">
      <form
        onSubmit={handleSubmit}
        className="h-fit rounded-2xl border border-border bg-card p-6 lg:sticky lg:top-24"
      >
        <h3 className="font-display text-lg font-semibold text-card-foreground">
          Pair components
        </h3>

        <p className="mt-1 text-sm text-muted-foreground">
          Define which components are compatible with each other.
        </p>

        <div className="mt-4 flex flex-col gap-4">
          <div>
            <Label htmlFor="a">Component A</Label>

            <Select id="a" value={aId} onChange={(e) => setAId(e.target.value)}>
              <option value="">Select…</option>

              {furniture.map((f) => (
                <option key={f.id} value={f.id}>
                  {f.name}
                </option>
              ))}
            </Select>
          </div>

          <div>
            <Label htmlFor="b">Component B</Label>

            <Select id="b" value={bId} onChange={(e) => setBId(e.target.value)}>
              <option value="">Select…</option>

              {furniture.map((f) => (
                <option key={f.id} value={f.id}>
                  {f.name}
                </option>
              ))}
            </Select>
          </div>

          <div>
            <Label htmlFor="note">Note (optional)</Label>

            <Textarea
              id="note"
              value={note}
              onChange={(e) => setNote(e.target.value)}
              placeholder="Why these pair well together…"
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

          <Button type="submit" disabled={saving || furniture.length < 2}>
            <Plus className="h-4 w-4" />

            {saving ? "Saving…" : "Create pairing"}
          </Button>

          {furniture.length < 2 && (
            <p className="text-sm text-muted-foreground">
              Add at least two components before pairing.
            </p>
          )}
        </div>
      </form>

      <div>
        <p className="mb-4 text-sm text-muted-foreground">
          {pairs.length} pairings
        </p>

        <div className="flex flex-col gap-3">
          {pairs.map((pair) => (
            <div
              key={pair.id}
              className="rounded-xl border border-border bg-card p-4"
            >
              <div className="flex items-center justify-between gap-3">
                <div className="flex flex-wrap items-center gap-2 text-sm font-medium text-card-foreground">
                  <span>{pair.a?.name ?? "Unknown"}</span>

                  <Link2 className="h-4 w-4 text-primary" />

                  <span>{pair.b?.name ?? "Unknown"}</span>
                </div>

                <Button
                  variant="ghost"
                  size="icon"
                  onClick={() => handleDelete(pair.id)}
                  aria-label="Remove pairing"
                >
                  <Trash2 className="h-4 w-4 text-destructive" />
                </Button>
              </div>

              {pair.note && (
                <p className="mt-2 text-sm italic text-muted-foreground">
                  “{pair.note}”
                </p>
              )}
            </div>
          ))}

          {pairs.length === 0 && (
            <div className="rounded-xl border border-dashed border-border py-12 text-center text-muted-foreground">
              No pairings yet.
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
