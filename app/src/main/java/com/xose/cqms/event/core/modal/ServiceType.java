package com.xose.cqms.event.core.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Josekutty on 7/13/2016.
 */
public class ServiceType implements Serializable {


    @Expose
    private Long id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("hospitalID")
    @Expose
    private Long hospitalID;

    @SerializedName("serviceRef")
    @Expose
    private Long serviceRef;

    @Expose
    private Integer statusCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getHospitalID() {
        return hospitalID;
    }

    public void setHospitalID(Long hospitalID) {
        this.hospitalID = hospitalID;
    }

    public Long getServiceRef() {
        return serviceRef;
    }

    public void setServiceRef(Long serviceRef) {
        this.serviceRef = serviceRef;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServiceType)) return false;

        ServiceType that = (ServiceType) o;

        if (!getHospitalID().equals(that.getHospitalID())) return false;
        return getServiceRef().equals(that.getServiceRef());

    }

    @Override
    public int hashCode() {
        int result = getHospitalID().hashCode();
        result = 31 * result + getServiceRef().hashCode();
        return result;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String toString() {
        return name;
    }
}
