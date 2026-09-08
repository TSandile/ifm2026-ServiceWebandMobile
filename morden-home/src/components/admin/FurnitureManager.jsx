import { useEffect, useState } from "react";
import { Pencil, Plus, Trash2, X } from "lucide-react";
import { supabase } from "../../lib/supabase";
import { Button } from "../ui/button";
import { Input } from "../ui/input";
import { Label, Textarea } from "../ui/field";
import { formatPrice, getComponentImageUrl } from "../../lib/utils";

const emptyForm = {
  category: "",
  description: "",
  price: "",
  image: null,
  in_stock: true,
};

export function FurnitureManager({ componentId, selectedComponent, onChange }) {
  const [items, setItems] = useState([]);
  const [form, setForm] = useState(emptyForm);
  const [editingId, setEditingId] = useState(null);
  const [error, setError] = useState(null);
  const [saving, setSaving] = useState(false);
  const [deletingId, setDeletingId] = useState(null);

  async function load() {
    try {
      const response = await fetch(
        "http://localhost:8081/api/components/getAllComponents",
        {
          headers: { Accept: "application/json" },
        },
      );

      if (!response.ok) {
        throw new Error(`Failed to load components (${response.status})`);
      }

      const payload = await response.json().catch(() => null);
      const candidates = [
        payload,
        payload?.data,
        payload?.components,
        payload?.items,
        payload?.result,
      ];

      const normalized = candidates.find((entry) => Array.isArray(entry));

      if (normalized) {
        setItems(normalized);
        return;
      }

      if (payload && typeof payload === "object") {
        const nestedList = Object.values(payload).find((entry) =>
          Array.isArray(entry),
        );

        if (nestedList) {
          setItems(nestedList);
          return;
        }
      }

      setItems([]);
    } catch (err) {
      console.error("Failed to load components from API:", err);
      setItems([]);
    }
  }

  useEffect(() => {
    load();
  }, []);

  useEffect(() => {
    if (!componentId) return;

    let active = true;

    async function loadComponentDetails() {
      setError(null);

      try {
        const response = await fetch(
          `http://localhost:8081/api/components/getComponent/${encodeURIComponent(componentId)}`,
          { headers: { Accept: "application/json" } },
        );
        const responseBody = await response.json().catch(() => null);

        if (!response.ok) {
          throw new Error(
            responseBody?.message ||
              responseBody?.error ||
              `Component request failed with status ${response.status}`,
          );
        }

        const component =
          responseBody?.component ||
          responseBody?.data?.component ||
          responseBody?.data ||
          responseBody;

        if (!active || !component) return;

        startEdit({ ...component, id: component.id ?? componentId });
      } catch (err) {
        if (active) {
          if (selectedComponent) {
            startEdit({ ...selectedComponent, id: componentId });
          } else {
            console.error("Component details request failed:", err);
            setError(
              err.message ||
                "Unable to load component details. Please try again.",
            );
          }
        }
      }
    }

    loadComponentDetails();

    return () => {
      active = false;
    };
  }, [componentId, selectedComponent]);

  function resetForm() {
    setForm(emptyForm);
    setEditingId(null);
    setError(null);
  }

  function startEdit(item) {
    setEditingId(item.id);
    setError(null);

    setForm({
      category: item.category ?? "",
      description: item.description ?? "",
      price: String(item.price),
      image: null,
      //   dimensions: item.dimensions ?? "",
      in_stock: item.in_stock,
    });

    window.scrollTo({
      top: 0,
      behavior: "smooth",
    });
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setError(null);

    const priceNum = Number(form.price);

    if (Number.isNaN(priceNum) || priceNum < 0) {
      return setError("Enter a valid price.");
    }

    setSaving(true);

    let error = null;

    if (editingId) {
      const payload = {
        description: form.description.trim(),
        price: priceNum,
      };

      try {
        const response = await fetch(
          `http://localhost:8081/api/components/updateComponent/${encodeURIComponent(editingId)}`,
          {
            method: "PUT",
            headers: {
              Accept: "application/json",
              "Content-Type": "application/json",
            },
            body: JSON.stringify(payload),
          },
        );

        if (!response.ok) {
          const responseBody = await response.json().catch(() => null);
          error = new Error(
            responseBody?.message ||
              responseBody?.error ||
              `Update failed with status ${response.status}`,
          );
        }
      } catch (err) {
        error = err;
      }
    } else {
      if (!form.image) {
        setSaving(false);
        return setError("Select a component image.");
      }

      const payload = new FormData();
      payload.append("description", form.description.trim());
      payload.append("price", String(priceNum));
      payload.append("image", form.image);

      try {
        const response = await fetch(
          "http://localhost:8081/api/components/registerComponent",
          {
            method: "POST",
            body: payload,
          },
        );

        if (!response.ok) {
          const responseBody = await response.json().catch(() => null);
          error = new Error(
            responseBody?.message ||
              responseBody?.error ||
              `Registration failed with status ${response.status}`,
          );
        }
      } catch (err) {
        error = err;
      }
    }

    setSaving(false);

    if (error) {
      return setError(error.message);
    }

    resetForm();
    await load();
    onChange?.();
  }

  async function handleDelete(id) {
    if (
      !confirm(
        "Delete this component? This also removes its compatibility pairings.",
      )
    ) {
      return;
    }

    setDeletingId(id);
    setError(null);

    try {
      const response = await fetch(
        `http://localhost:8081/api/components/deleteComponent/${encodeURIComponent(id)}`,
        {
          method: "DELETE",
          headers: { Accept: "application/json" },
        },
      );

      if (!response.ok) {
        const responseBody = await response.json().catch(() => null);
        throw new Error(
          responseBody?.message ||
            responseBody?.error ||
            `Delete failed with status ${response.status}`,
        );
      }

      if (editingId === id) resetForm();
      await load();
      onChange?.();
    } catch (err) {
      console.error("Component delete failed:", err);
      setError(err.message || "Delete failed. Please try again.");
    } finally {
      setDeletingId(null);
    }
  }

  return (
    <div className="grid gap-8 lg:grid-cols-[380px_1fr]">
      {/* Form */}
      <form
        onSubmit={handleSubmit}
        className="h-fit rounded-2xl border border-border bg-card p-6 lg:sticky lg:top-24"
      >
        <div className="flex items-center justify-between">
          <h3 className="font-display text-lg font-semibold text-card-foreground">
            {editingId ? "Edit component" : "Add component"}
          </h3>

          {editingId && (
            <button
              type="button"
              onClick={resetForm}
              className="text-muted-foreground hover:text-foreground"
            >
              <X className="h-4 w-4" />
            </button>
          )}
        </div>

        <div className="mt-4 flex flex-col gap-4">
          <div>
            <Label htmlFor="category">Category</Label>

            <Input
              id="category"
              value={form.category}
              onChange={(e) =>
                setForm({
                  ...form,
                  category: e.target.value,
                })
              }
              placeholder="e.g. Leg, Top"
            />
          </div>

          <div className="grid grid-cols-2 gap-3">
            <div>
              <Label htmlFor="price">Price </Label>

              <Input
                id="price"
                type="number"
                min="0"
                step="0.01"
                value={form.price}
                onChange={(e) =>
                  setForm({
                    ...form,
                    price: e.target.value,
                  })
                }
                required
              />
            </div>

            <div>
              <Label htmlFor="image">Component image</Label>

              <Input
                id="image"
                type="file"
                accept="image/*"
                onChange={(e) =>
                  setForm({
                    ...form,
                    image: e.target.files?.[0] ?? null,
                  })
                }
                required={!editingId}
              />
            </div>

            <div>
              <Label htmlFor="description">Description</Label>

              <Textarea
                id="description"
                value={form.description}
                onChange={(e) =>
                  setForm({
                    ...form,
                    description: e.target.value,
                  })
                }
              />
            </div>

            {/* <label className="flex items-center gap-2 text-sm text-foreground">
              <input
                type="checkbox"
                checked={form.in_stock}
                onChange={(e) =>
                  setForm({
                    ...form,
                    in_stock: e.target.checked,
                  })
                }
                className="h-4 w-4 rounded border-border accent-[#05abb9]"
              />
              In stock
            </label> */}

            {error && (
              <p
                className="rounded-lg bg-destructive/10 px-3 py-2 text-sm text-destructive"
                role="alert"
              >
                {error}
              </p>
            )}

            <Button type="submit" disabled={saving}>
              <Plus className="h-4 w-4" />

              {saving
                ? "Saving…"
                : editingId
                  ? "Save changes"
                  : "Add component"}
            </Button>
          </div>
        </div>
      </form>

      {/* List */}
      <div>
        <p className="mb-4 text-sm text-muted-foreground">
          {items.length} components
        </p>

        <div className="flex flex-col gap-3">
          {items.map((item) => (
            <div
              key={item.id}
              className="flex items-center gap-4 rounded-xl border border-border bg-card p-3"
            >
              <div className="h-16 w-16 shrink-0 overflow-hidden rounded-lg bg-muted">
                {getComponentImageUrl(item) && (
                  <img
                    src={getComponentImageUrl(item)}
                    alt={`Component ${item.id}`}
                    className="h-full w-full object-cover"
                    crossOrigin="anonymous"
                  />
                )}
              </div>

              <div className="min-w-0 flex-1">
                <p className="truncate font-medium text-card-foreground">
                  Component #{item.id}
                </p>

                <p className="text-sm text-muted-foreground">
                  {item.category} · {formatPrice(item.price)} ·{" "}
                  {item.in_stock ? "In stock" : "Out of stock"}
                </p>
              </div>

              <div className="flex gap-1">
                <Button
                  variant="ghost"
                  size="icon"
                  disabled={saving || deletingId !== null}
                  onClick={() => startEdit(item)}
                  aria-label="Edit"
                >
                  <Pencil className="h-4 w-4" />
                </Button>

                <Button
                  variant="ghost"
                  size="icon"
                  disabled={saving || deletingId !== null}
                  onClick={() => handleDelete(item.id)}
                  aria-label="Delete"
                >
                  <Trash2 className="h-4 w-4 text-destructive" />
                </Button>
              </div>
            </div>
          ))}

          {items.length === 0 && (
            <div className="rounded-xl border border-dashed border-border py-12 text-center text-muted-foreground">
              No components yet. Add your first one.
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
