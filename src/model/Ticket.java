package model;

import java.sql.Timestamp;

public class Ticket {

    private int id;
    private String title;
    private String description;
    private String status;
    private String priority;
    private int customerId;
    private int assignedTo;
    private Timestamp createdAt;
    private Timestamp completedAt;
    private String customerName;
    private String assignedName;

    public Ticket() {
    }

    public Ticket(int id, String title, String description, String status, String priority, int customerId, int assignedTo, Timestamp createdAt, Timestamp completedAt, String customerName, String assignedName) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.customerId = customerId;
        this.assignedTo = assignedTo;
        this.createdAt = createdAt;
        this.completedAt = completedAt;
        this.customerName = customerName;
        this.assignedName = assignedName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(int assignedTo) {
        this.assignedTo = assignedTo;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Timestamp completedAt) {
        this.completedAt = completedAt;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAssignedName() {
        return assignedName;
    }

    public void setAssignedName(String assignedName) {
        this.assignedName = assignedName;
    }
}
