package com.example.catku.object;

public class Transaction {
    int id_transaction = 0;
    int id_user = 0;
    int id_category = 0;
    String category = null;
    String type = null;
    String image = null;
    int amount = 0;
    String description = null;
    String memo = null;
    String date = null;
    String time = null;

    public Transaction(int id_transaction, int id_user, int id_category,String category, String type, String image, int amount, String description, String memo, String date, String time){
        this.id_transaction = id_transaction;
        this.id_user = id_user;
        this.id_category = id_category;
        this.category = category;
        this.type = type;
        this.image = image;
        this.amount = amount;
        this.description = description;
        this.memo = memo;
        this.date = date;
        this.time = time;

    }

    public int getId_transaction() {
        return id_transaction;
    }

    public void setId_transaction(int id_transaction) {
        this.id_transaction = id_transaction;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public int getId_category() {
        return id_category;
    }

    public void setId_category(int id_category) {
        this.id_category = id_category;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
