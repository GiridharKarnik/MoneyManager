package com.giridharkarnik.moneymanager.modelobjects;

import android.content.Context;

import java.io.Serializable;

public class StatsInformationObject implements Serializable{

    private String typeOfChart;
    private String typeOfInfo;
    private int statsMonth;
    private int statsYear;
    private int barPieLine;

    public StatsInformationObject(String typeOfChart, String typeOfInfo, int statsMonth, int statsYear, int barPieLine) {
        this.typeOfChart = typeOfChart;
        this.typeOfInfo = typeOfInfo;
        this.statsMonth = statsMonth;
        this.statsYear = statsYear;
        this.barPieLine = barPieLine;
    }

    public String getTypeOfChart() {
        return typeOfChart;
    }

    public void setTypeOfChart(String typeOfChart) {
        this.typeOfChart = typeOfChart;
    }

    public String getTypeOfInfo() {
        return typeOfInfo;
    }

    public void setTypeOfInfo(String typeOfInfo) {
        this.typeOfInfo = typeOfInfo;
    }

    public int getStatsMonth() {
        return statsMonth;
    }

    public void setStatsMonth(int month) {
        this.statsMonth = month;
    }

    public int getStatsYear() {
        return statsYear;
    }

    public void setStatsYear(int year) {
        this.statsYear = year;
    }

    public int getBarPieLine() {
        return barPieLine;
    }

    public void setBarPieLine(int barPieLine) {
        this.barPieLine = barPieLine;
    }

    @Override
    public String toString() {
        return "something" +typeOfChart;
    }
}
