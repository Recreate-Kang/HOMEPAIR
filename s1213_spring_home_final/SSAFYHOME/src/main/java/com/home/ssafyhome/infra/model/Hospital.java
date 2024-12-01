package com.home.ssafyhome.infra.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Hospital extends Locatable {
    private String keyId;
    private String address;
    private String typeCode;
    private String typeName;
    private String emergencyCode;
    private String emergencyName;
    private Integer emergencyBoolean;
    private String etc;
    private String detail;
    private String roughName;
    private String serviceName;
    private String phone1;
    private String phone2;
    private Integer postal1;
    private Integer postal2;
    private String myUnknownColumn;
}
