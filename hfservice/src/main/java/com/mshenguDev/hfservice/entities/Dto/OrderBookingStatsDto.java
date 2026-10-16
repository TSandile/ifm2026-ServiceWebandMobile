package com.mshenguDev.hfservice.entities.Dto;

public class OrderBookingStatsDto {
    private long totalOrders;
    private long approvedOrders;
    private long declinedOrders;
    private long pendingOrders;
    private double totalRevenue;

    public OrderBookingStatsDto() {
    }

    public OrderBookingStatsDto(long totalOrders, long approvedOrders, long declinedOrders,
                               long pendingOrders, double totalRevenue) {
        this.totalOrders = totalOrders;
        this.approvedOrders = approvedOrders;
        this.declinedOrders = declinedOrders;
        this.pendingOrders = pendingOrders;
        this.totalRevenue = totalRevenue;
    }

    public long getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(long totalOrders) {
        this.totalOrders = totalOrders;
    }

    public long getApprovedOrders() {
        return approvedOrders;
    }

    public void setApprovedOrders(long approvedOrders) {
        this.approvedOrders = approvedOrders;
    }

    public long getDeclinedOrders() {
        return declinedOrders;
    }

    public void setDeclinedOrders(long declinedOrders) {
        this.declinedOrders = declinedOrders;
    }

    public long getPendingOrders() {
        return pendingOrders;
    }

    public void setPendingOrders(long pendingOrders) {
        this.pendingOrders = pendingOrders;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
}
