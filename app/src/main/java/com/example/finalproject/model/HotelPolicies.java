package com.example.finalproject.model;

import java.util.List;

public class HotelPolicies {
    String polName, polIcon;
    List<String> nestedList;
    boolean isExpandable;

    public HotelPolicies() {
    }

    public HotelPolicies(String polName, String polIcon, List<String> nestedList) {
        this.polName = polName;
        this.polIcon = polIcon;
        this.nestedList = nestedList;
        this.isExpandable = true;
    }

    public String getPolName() {
        return polName;
    }

    public void setPolName(String polName) {
        this.polName = polName;
    }

    public String getPolIcon() {
        return polIcon;
    }

    public void setPolIcon(String polIcon) {
        this.polIcon = polIcon;
    }

    public List<String> getNestedList() {
        return nestedList;
    }

    public void setNestedList(List<String> nestedList) {
        this.nestedList = nestedList;
    }

    public boolean isExpandable() {
        return isExpandable;
    }

    public void setExpandable(boolean expandable) {
        isExpandable = expandable;
    }
}
