package entity;

import java.io.Serializable;

public class Grain implements Serializable {
    private String idGrain;
    private String grainCode;
    private String grainName;
    private String grainType;
    private double grainPrice;
    private double grainShelfLife;
    private String grainRemark;
    private String capacity;
    private String notice;

    // 构造方法
    public Grain() {}

    public Grain(String grainCode, String grainName, String grainType, double grainPrice, double grainShelfLife, String grainRemark) {
        this.grainCode = grainCode;
        this.grainName = grainName;
        this.grainType = grainType;
        this.grainPrice = grainPrice;
        this.grainShelfLife = grainShelfLife;
        this.grainRemark = grainRemark;
    }



    // Getter和Setter方法
    public String getIdGrain() {
        return idGrain;
    }

    public void setIdGrain(String idGrain) {
        this.idGrain = idGrain;
    }

    public String getGrainCode() {
        return grainCode;
    }

    public void setGrainCode(String grainCode) {
        this.grainCode = grainCode;
    }

    public String getGrainName() {
        return grainName;
    }

    public void setGrainName(String grainName) {
        this.grainName = grainName;
    }

    public String getGrainType() {
        return grainType;
    }

    public void setGrainType(String grainType) {
        this.grainType = grainType;
    }

    public double getGrainPrice() {
        return grainPrice;
    }

    public void setGrainPrice(double grainPrice) {
        this.grainPrice = grainPrice;
    }

    public double getGrainShelfLife() {
        return grainShelfLife;
    }

    public void setGrainShelfLife(double grainShelfLife) {
        this.grainShelfLife = grainShelfLife;
    }

    public String getGrainRemark() {
        return grainRemark;
    }

    public void setGrainRemark(String grainRemark) {
        this.grainRemark = grainRemark;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }
}
