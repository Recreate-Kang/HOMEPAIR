package com.home.ssafyhome.infra.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class School extends Locatable {
    private String keyId;
    private String schoolName;
    private String schoolLevel;
    private String foundationDate;
    private String foundationType;
    private String mainBranch;
    private String serviceStatus;
    private String address;
    private String addressRoad;
    private Integer governmentCode;
    private String governmentName;
    private Integer governmentSubCode;
    private String governmentSubName;
    private String createdDate;
    private String updateDate;
    private String dataServeDate;
    private String providerCode;
    private String providerName;
}
