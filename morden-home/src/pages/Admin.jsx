import { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import { Boxes, BarChart3, Download } from "lucide-react";
import jsPDF from "jspdf";
import { useAuth } from "../context/AuthContext.js";
import { FurnitureManager } from "../components/admin/FurnitureManager";

export function Admin() {
  const { profile } = useAuth();
  const location = useLocation();
  const [tab, setTab] = useState("components");
  const [report, setReport] = useState(null);
  const [reportError, setReportError] = useState(null);
  const [bookingStats, setBookingStats] = useState(null);
  const [bookingStatsError, setBookingStatsError] = useState(null);
  const componentId = new URLSearchParams(location.search).get("componentId");

  function downloadPdfReport() {
    if (!report) return;

    const doc = new jsPDF();
    const pageWidth = doc.internal.pageSize.getWidth();

    doc.setFontSize(18);
    doc.text("Inventory Report", 14, 18);

    doc.setFontSize(11);
    doc.text(`Generated: ${new Date().toLocaleString()}`, 14, 28);

    const summary = [
      `Total components: ${report.totalComponents ?? 0}`,
      `In stock: ${report.inStockCount ?? 0}`,
      `Out of stock: ${report.outOfStockCount ?? 0}`,
      `Low stock: ${report.lowStockCount ?? 0}`,
      `Inventory value: R${Number(report.inventoryValue ?? 0).toFixed(2)}`,
    ];

    summary.forEach((line, index) => {
      doc.text(line, 14, 40 + index * 8);
    });

    let y = 90;
    doc.setFontSize(12);
    doc.text("Category breakdown", 14, y);
    y += 8;

    const categories = report.categoryBreakdown
      ? Object.entries(report.categoryBreakdown)
      : [];

    categories.forEach(([category, value]) => {
      if (y > 260) {
        doc.addPage();
        y = 20;
      }
      doc.setFontSize(10);
      doc.text(`${category}: ${value}`, 14, y);
      y += 8;
    });

    if (categories.length === 0) {
      doc.setFontSize(10);
      doc.text("No category data available.", 14, y);
      y += 10;
    }

    const lowStockItems = report.items
      ? report.items.filter((item) => item.stockLevel !== null && item.stockLevel <= 5)
      : [];

    if (lowStockItems.length > 0) {
      if (y > 220) {
        doc.addPage();
        y = 20;
      }
      doc.setFontSize(12);
      doc.text("Low stock items", 14, y);
      y += 8;

      lowStockItems.slice(0, 15).forEach((item) => {
        if (y > 260) {
          doc.addPage();
          y = 20;
        }
        doc.setFontSize(9);
        const line = `${item.description} - ${item.stockLevel} left`;
        const wrapped = doc.splitTextToSize(line, pageWidth - 28);
        doc.text(wrapped, 14, y);
        y += wrapped.length * 6;
      });
    }

    doc.save("inventory-report.pdf");
  }

  useEffect(() => {
    async function loadReport() {
      try {
        const response = await fetch("http://localhost:8081/api/components/inventory-report", {
          headers: { Accept: "application/json" },
        });

        if (!response.ok) {
          throw new Error(`Report unavailable (${response.status})`);
        }

        const payload = await response.json();
        setReport(payload);
        setReportError(null);
      } catch (error) {
        setReportError(error.message || "Unable to load stock report.");
      }
    }

    async function loadBookingStats() {
      try {
        const response = await fetch("http://localhost:8081/api/orders/booking-stats", {
          headers: { Accept: "application/json" },
        });

        if (!response.ok) {
          throw new Error(`Booking stats unavailable (${response.status})`);
        }

        const payload = await response.json();
        setBookingStats(payload);
        setBookingStatsError(null);
      } catch (error) {
        setBookingStatsError(error.message || "Unable to load booking stats.");
      }
    }

    loadReport();
    loadBookingStats();
  }, []);

  const tabs = [
    { key: "components", label: "Components", icon: Boxes },
    { key: "report", label: "Reports", icon: BarChart3 },
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
          Manage your furniture catalog, stock levels, and manager reports.
        </p>
      </div>

      <div className="mt-8 flex gap-1 border-b border-border">
        {tabs.map((tabItem) => {
          const TabIcon = tabItem.icon;

          return (
            <button
              key={tabItem.key}
              type="button"
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
          <div className="space-y-6">
            {reportError ? (
              <div className="rounded-xl border border-red-200 bg-red-50 p-4 text-sm text-red-700">
                {reportError}
              </div>
            ) : !report ? (
              <div className="rounded-xl border border-border bg-card p-6 text-sm text-muted-foreground">
                Loading inventory report...
              </div>
            ) : (
              <>
                <div className="mb-4 flex justify-end">
                  <button
                    type="button"
                    onClick={downloadPdfReport}
                    className="inline-flex items-center gap-2 rounded-lg bg-primary px-4 py-2 text-sm font-medium text-primary-foreground shadow-sm transition hover:opacity-90"
                  >
                    <Download className="h-4 w-4" />
                    Download PDF
                  </button>
                </div>

                <div className="grid gap-4 md:grid-cols-4">
                  <div className="rounded-xl border border-border bg-card p-4">
                    <p className="text-sm text-muted-foreground">Total components</p>
                    <p className="mt-2 text-2xl font-semibold">{report.totalComponents ?? 0}</p>
                  </div>
                  <div className="rounded-xl border border-border bg-card p-4">
                    <p className="text-sm text-muted-foreground">In stock</p>
                    <p className="mt-2 text-2xl font-semibold text-emerald-600">{report.inStockCount ?? 0}</p>
                  </div>
                  <div className="rounded-xl border border-border bg-card p-4">
                    <p className="text-sm text-muted-foreground">Out of stock</p>
                    <p className="mt-2 text-2xl font-semibold text-red-600">{report.outOfStockCount ?? 0}</p>
                  </div>
                  <div className="rounded-xl border border-border bg-card p-4">
                    <p className="text-sm text-muted-foreground">Inventory value</p>
                    <p className="mt-2 text-2xl font-semibold">R{Number(report.inventoryValue ?? 0).toFixed(2)}</p>
                  </div>
                </div>

                <div className="mt-6 grid gap-4 md:grid-cols-4">
                  <div className="rounded-xl border border-border bg-card p-4">
                    <p className="text-sm text-muted-foreground">Total bookings</p>
                    <p className="mt-2 text-2xl font-semibold">{bookingStats?.totalOrders ?? 0}</p>
                  </div>
                  <div className="rounded-xl border border-border bg-card p-4">
                    <p className="text-sm text-muted-foreground">Approved</p>
                    <p className="mt-2 text-2xl font-semibold text-emerald-600">{bookingStats?.approvedOrders ?? 0}</p>
                  </div>
                  <div className="rounded-xl border border-border bg-card p-4">
                    <p className="text-sm text-muted-foreground">Declined</p>
                    <p className="mt-2 text-2xl font-semibold text-red-600">{bookingStats?.declinedOrders ?? 0}</p>
                  </div>
                  <div className="rounded-xl border border-border bg-card p-4">
                    <p className="text-sm text-muted-foreground">Revenue</p>
                    <p className="mt-2 text-2xl font-semibold">R{Number(bookingStats?.totalRevenue ?? 0).toFixed(2)}</p>
                  </div>
                </div>

                <div className="grid gap-6 lg:grid-cols-2">
                  <div className="rounded-xl border border-border bg-card p-5">
                    <h3 className="text-lg font-semibold">Category breakdown</h3>
                    <div className="mt-4 space-y-3">
                      {report.categoryBreakdown && Object.entries(report.categoryBreakdown).length > 0 ? (
                        Object.entries(report.categoryBreakdown).map(([category, count]) => (
                          <div key={category} className="flex items-center justify-between text-sm">
                            <span className="text-muted-foreground">{category}</span>
                            <span className="font-medium">{count}</span>
                          </div>
                        ))
                      ) : (
                        <p className="text-sm text-muted-foreground">No category data available.</p>
                      )}
                    </div>
                  </div>

                  <div className="rounded-xl border border-border bg-card p-5">
                    <h3 className="text-lg font-semibold">Low stock alerts</h3>
                    <div className="mt-4 space-y-3">
                      {report.items && report.items.filter((item) => item.stockLevel !== null && item.stockLevel <= 5).length > 0 ? (
                        report.items
                          .filter((item) => item.stockLevel !== null && item.stockLevel <= 5)
                          .slice(0, 10)
                          .map((item) => (
                            <div key={item.id} className="flex items-center justify-between text-sm">
                              <div>
                                <p className="font-medium">{item.description}</p>
                                <p className="text-muted-foreground">{item.category}</p>
                              </div>
                              <span className="rounded-full bg-amber-100 px-2 py-1 text-xs font-medium text-amber-800">
                                {item.stockLevel} left
                              </span>
                            </div>
                          ))
                      ) : (
                        <p className="text-sm text-muted-foreground">No low-stock items at the moment.</p>
                      )}
                    </div>
                  </div>

                  <div className="rounded-xl border border-border bg-card p-5">
                    <h3 className="text-lg font-semibold">Booking status</h3>
                    <div className="mt-4 space-y-3">
                      {bookingStatsError ? (
                        <p className="text-sm text-red-600">{bookingStatsError}</p>
                      ) : (
                        <>
                          <div className="flex items-center justify-between text-sm">
                            <span className="text-muted-foreground">Pending</span>
                            <span className="font-medium">{bookingStats?.pendingOrders ?? 0}</span>
                          </div>
                          <div className="flex items-center justify-between text-sm">
                            <span className="text-muted-foreground">Approved</span>
                            <span className="font-medium">{bookingStats?.approvedOrders ?? 0}</span>
                          </div>
                          <div className="flex items-center justify-between text-sm">
                            <span className="text-muted-foreground">Declined</span>
                            <span className="font-medium">{bookingStats?.declinedOrders ?? 0}</span>
                          </div>
                        </>
                      )}
                    </div>
                  </div>
                </div>
              </>
            )}
          </div>
        )}
      </div>
    </div>
  );
}
