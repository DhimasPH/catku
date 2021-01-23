package com.example.catku.object;

public class Category {
    int id_category = 0;
    String name = null;
    String type = null;
    public Category(int id_category, String name, String type){
        this.id_category = id_category;
        this.name = name;
        this.type = type;
    }

    public int getId_category() {
        return id_category;
    }

    public void setId_category(int id_category) {
        this.id_category = id_category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
