package com.project.lrnshub.ui.newnote;


import java.io.Serializable;

public class Items implements Serializable {
    private String id;
    private boolean checked;
    private String text;

    public Items() {
    }

    public Items(String id, boolean checked, String text) {
        this.id = id;
        this.checked = checked;
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean getChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
