package com.example.miniproject1.model;

import java.io.Serializable;

public class Room implements Serializable {
    private String roomId;
    private String roomName;
    private double price;
    private String status;
    private String tenantName;
    private String phone;

    public Room() {
    }

    public Room(String roomId, String roomName, double price, String status, String tenantName, String phone) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.price = price;
        this.status = status;
        this.tenantName = tenantName;
        this.phone = phone;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
