package com.michaeltadeo.nightowl.Model;

public class Assessment {

    private Integer id, alert1, alertId;
    private String title, dueDate, goalDate, type, course;

    public Assessment(Integer id, String title, String dueDate, String goalDate, String type,
                      String course, Integer alert1, Integer alertId) {

        this.id = id;
        this.title = title;
        this.dueDate = dueDate;
        this.goalDate = goalDate;
        this.type = type;
        this.course = course;
        this.alert1 = alert1;
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

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getGoalDate() {
        return goalDate;
    }

    public void setGoalDate(String goalDate) {
        this.goalDate = goalDate;
    }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

    public String getCourse() { return course; }

    public void setCourse(String course) { this.course = course; }

    public Integer getAlert1() { return alert1; }

    public void setAlert1(Integer alert1) { this.alert1 = alert1; }

    public Integer getAlertId() { return alertId; }

    public void setAlertId(Integer alertId) { this.alertId = alertId; }

}
