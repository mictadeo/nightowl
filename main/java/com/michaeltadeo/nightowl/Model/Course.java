package com.michaeltadeo.nightowl.Model;

public class Course {

    private Integer id, alert1, alert2, alertId;
    private String title, startDate, endDate, status, notes, name, phone, email, term;

    public Course(Integer id, String title, String startDate, String endDate, String status,
                  String notes, String name, String phone, String email, String term,
                  Integer alert1, Integer alert2, Integer alertId) {

        this.id = id;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.notes = notes;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.term = term;
        this.alert1 = alert1;
        this.alert2 = alert2;
        this.alertId = alertId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String termId) {
        this.term = termId;
    }

    public Integer getAlert1() {
        return alert1;
    }

    public void setAlert1(Integer alert1) {
        this.alert1 = alert1;
    }

    public Integer getAlert2() { return alert2; }

    public void setAlert2(Integer alert2) { this.alert2 = alert2; }

    public Integer getAlertId() { return alertId; }

    public void setAlertId(Integer alertId) { this.alertId = alertId; }
}
