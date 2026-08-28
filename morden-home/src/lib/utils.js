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
