import { clsx } from "clsx";
import { twMerge } from "tailwind-merge";

export function cn(...inputs) {
  return twMerge(clsx(inputs));
}

export function formatPrice(value) {
  return new Intl.NumberFormat("en-ZA", {
    style: "currency",
    currency: "ZAR",
    minimumFractionDigits: 0,
    maximumFractionDigits: 2,
  }).format(value);
}

export function getComponentImageUrl(component) {
  const componentId = component?.id ?? component?.componentId;

  return componentId
    ? `http://localhost:8081/api/components/getImage/${encodeURIComponent(componentId)}`
    : null;
}

export function normalizeComponent(component) {
  if (!component || typeof component !== "object") return component;

  const rawStockLevel = component.stock_level ?? component.stockLevel;
  const stockLevel =
    rawStockLevel === null ||
    rawStockLevel === undefined ||
    rawStockLevel === ""
      ? null
      : Number(rawStockLevel);

  return {
    ...component,
    stock_level: Number.isFinite(stockLevel) ? stockLevel : 0,
    in_stock: Number.isFinite(stockLevel)
      ? stockLevel > 0
      : component.in_stock === true || component.in_stock === "true",
  };
}
