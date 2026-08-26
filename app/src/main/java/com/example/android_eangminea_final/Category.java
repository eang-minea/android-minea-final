package com.example.android_eangminea_final;

public class Category {
    private String name;
    private int iconRes;
    private boolean isSelected;

    public Category(String name, int iconRes, boolean isSelected) {
        this.name = name;
        this.iconRes = iconRes;
        this.isSelected = isSelected;
    }

    public String getName() {
        return name;
    }

    public int getIconRes() {
        return iconRes;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}