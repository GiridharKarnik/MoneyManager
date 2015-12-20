package com.giridharkarnik.moneymanager;

public class NavigationSectionModel {

    private String sectionTitle;
    private int sectionImage;

    public NavigationSectionModel(String sectionTitle, int sectionImage) {
        this.sectionTitle = sectionTitle;
        this.sectionImage = sectionImage;
    }


    public String getSectionTitle() {
        return sectionTitle;
    }

    public void setSectionTitle(String sectionTitle) {
        this.sectionTitle = sectionTitle;
    }

    public int getSectionImage() {
        return sectionImage;
    }

    public void setSectionImage(int sectionImage) {
        this.sectionImage = sectionImage;
    }
}
