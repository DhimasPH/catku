package com.example.catku.object;

public class Schedule {
    int id_schedule = 0;
    String set_every = null;
    int id_category = 0;
    String time = null;
    String date = null;
    String description = null;
    int amount = 0;
    String type = null;

    public Schedule(int id_schedule, String set_every, int id_category, String time, String date, String description, int amount, String type){
        this.id_schedule = id_schedule;
        this.set_every = set_every;
        this.id_category = id_category;
        this.time = time;
        this.date = date;
        this.description = description;
        this.amount = amount;
        this.type = type;
    }

    public int getId_schedule() {
        return id_schedule;
    }

    public void setId_schedule(int id_schedule) {
        this.id_schedule = id_schedule;
    }

    public String getSet_every() {
        return set_every;
    }

    public void setSet_every(String set_every) {
        this.set_every = set_every;
    }

    public int getId_category() {
        return id_category;
    }

    public void setId_category(int id_category) {
        this.id_category = id_category;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
