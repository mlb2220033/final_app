package com.example.finalproject.model;

import com.example.finalproject.adapter.NestedAdapter;

import java.util.List;

public class HotelFacilities {
    String facName, facIcon;
    List<String> nestedList;
    boolean isExpandable;

    public HotelFacilities() {
    }

    public HotelFacilities(String facName, String facIcon, List<String> nestedList) {
        this.facName = facName;
        this.facIcon = facIcon;
        this.nestedList = nestedList;
        this.isExpandable = true;
    }

    public String getFacName() {
        return facName;
    }

    public void setFacName(String facName) {
        this.facName = facName;
    }

    public String getFacIcon() {
        return facIcon;
    }

    public void setFacIcon(String facIcon) {
        this.facIcon = facIcon;
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

    @Override
    public String toString() {
        return "HotelFacilities{" +
                "facName='" + facName + '\'' +
                ", nestedList=" + nestedList +
                '}';
    }
}
