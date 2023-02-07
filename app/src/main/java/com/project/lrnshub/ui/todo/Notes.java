package com.project.lrnshub.ui.todo;


import com.project.lrnshub.ui.newnote.Items;

import java.io.Serializable;
import java.util.List;

public class Notes implements Serializable {
    private String id;
    private Integer code;
    private String title;
    private List<Items> items;

    public Notes() {
    }

    public Notes(String id, Integer code, String title, List<Items> items) {
        this.code = code;
        this.id = id;
        this.title = title;
        this.items = items;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setItems(List<Items> items) {
        this.items = items;
    }

    public String getTitle() {
        return title;
    }

    public List<Items> getItems() {
        return items;
    }
}
