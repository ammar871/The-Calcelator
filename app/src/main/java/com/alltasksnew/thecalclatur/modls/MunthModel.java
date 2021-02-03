package com.alltasksnew.thecalclatur.modls;

public class MunthModel {
    private String numberMunth;
    private String nameMunth;

    public MunthModel(String numberMunth, String nameMunth) {
        this.numberMunth = numberMunth;
        this.nameMunth = nameMunth;
    }

    public String getNumberMunth() {
        return numberMunth;
    }

    public void setNumberMunth(String numberMunth) {
        this.numberMunth = numberMunth;
    }

    public String getNameMunth() {
        return nameMunth;
    }

    public void setNameMunth(String nameMunth) {
        this.nameMunth = nameMunth;
    }
}
