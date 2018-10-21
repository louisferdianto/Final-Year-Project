package com.example.louisferdianto.app1.Models;

public class Ticket {
    private String paymentId;
    private String paidAmount;
    private String buyerName;
    private String EventName;
    private String TicketQty;
    private String dateTimePurchased;
    private String locationVenue,dateEvent,startTime,endTime;
    public Ticket() {
    }

    public Ticket(String paymentId, String paidAmount, String buyerName, String eventName, String ticketQty, String datetime, String locationVenue,String dateEvent,String startTime,String endTime) {
        this.paymentId = paymentId;
        this.paidAmount = paidAmount;
        this.buyerName = buyerName;
        EventName = eventName;
        TicketQty = ticketQty;
        dateTimePurchased = datetime;
        this.locationVenue = locationVenue;
        this.dateEvent = dateEvent;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getLocationVenue() {
        return locationVenue;
    }

    public void setLocationVenue(String locationVenue) {
        this.locationVenue = locationVenue;
    }

    public String getDateEvent() {
        return dateEvent;
    }

    public void setDateEvent(String dateEvent) {
        this.dateEvent = dateEvent;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDateTime() {
        return dateTimePurchased;
    }

    public void setDateTime(String dateTime) {
        this.dateTimePurchased = dateTime;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(String paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getEventName() {
        return EventName;
    }

    public void setEventName(String eventName) {
        EventName = eventName;
    }

    public String getTicketQty() {
        return TicketQty;
    }

    public void setTicketQty(String ticketQty) {
        TicketQty = ticketQty;
    }
}
