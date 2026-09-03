import { useState } from "react";
import { useLocation } from "react-router-dom";
import { Boxes, Link2 } from "lucide-react";
import { useAuth } from "../context/AuthContext.js";
import { FurnitureManager } from "../components/admin/FurnitureManager";
import { CompatibilityManager } from "../components/admin/CompatibilityManager";
// import { FurnitureManager } from "@/components/admin/FurnitureManager";
// import { CompatibilityManager } from "@/components/admin/CompatibilityManager";

export function Admin() {
  const { profile } = useAuth();
  const location = useLocation();
  const [tab, setTab] = useState("components");
  const componentId = new URLSearchParams(location.search).get("componentId");

  const tabs = [
    { key: "components", label: "Components", icon: Boxes },
    { key: "compatibility", label: "Compatibility", icon: Link2 },
  ];

  return (
    <div className="mx-auto max-w-6xl px-4 py-10 sm:px-6">
      <div className="flex flex-col gap-1">
        <span className="text-sm font-medium text-primary">
          Admin dashboard
        </span>

        <h1 className="font-display text-3xl font-semibold text-foreground">
          Welcome{profile?.name ? `, ${profile.name}` : ""}
        </h1>

        <p className="text-muted-foreground">
          Manage your furniture catalog and component compatibility.
        </p>
      </div>

      <div className="mt-8 flex gap-1 border-b border-border">
        {tabs.map((tabItem) => {
          const TabIcon = tabItem.icon;

          return (
            <button
              key={tabItem.key}
              onClick={() => setTab(tabItem.key)}
              className={
                tab === tabItem.key
                  ? "flex items-center gap-2 border-b-2 border-primary px-4 py-3 text-sm font-medium text-foreground"
                  : "flex items-center gap-2 border-b-2 border-transparent px-4 py-3 text-sm font-medium text-muted-foreground hover:text-foreground"
              }
            >
              <TabIcon className="h-4 w-4" />
              {tabItem.label}
            </button>
          );
        })}
      </div>

      <div className="mt-8">
        {tab === "components" ? (
          <FurnitureManager
            componentId={componentId}
            selectedComponent={location.state?.component}
          />
        ) : (
          <CompatibilityManager />
        )}
      </div>
    </div>
  );
}
