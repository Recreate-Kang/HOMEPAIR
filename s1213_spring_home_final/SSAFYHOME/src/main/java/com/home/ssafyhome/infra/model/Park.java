package com.home.ssafyhome.infra.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Park extends Locatable {
    private String manageId;
    private String parkName;
    private String typeName;
    private String addressRoad;
    private String address;
    private Integer landArea;
    private String exerciseFacility;
    private String amusementFacility;
    private String convenientFacility;
    private String cultureFacility;
    private String etcFacility;
    private String startDate;
    private String orgraizationName;
    private String phone;
    private String lastUpdate;
    private Integer providerCode;
    private String providerName;
}
