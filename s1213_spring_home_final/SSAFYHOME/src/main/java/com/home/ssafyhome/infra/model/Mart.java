package com.home.ssafyhome.infra.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Mart extends Locatable {
    private Integer keyId;
    private String serviceType;
    private String serviceId;
    private Integer governmentCode;
    private Double managedCode;
    private String licenseDay;
    private Integer statusType;
    private String statusName;
    private Integer statusTypeDetail;
    private String statusNameDetail;
    private String phone;
    private Double landArea;
    private String postalCode;
    private String address;
    private String addressRoad;
    private String roadnamePostal;
    private String serviceName;
    private String lastUpdate;
    private String updateType;
    private String updateDate;
    private String workingType;
    private Double cordinateX;
    private Double cordinateY;
    private String martType;
}
