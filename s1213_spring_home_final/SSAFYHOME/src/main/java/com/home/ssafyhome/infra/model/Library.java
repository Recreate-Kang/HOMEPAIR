package com.home.ssafyhome.infra.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Library extends Locatable {
    private String serviceName;
    private String sidoName;
    private String gunguName;
    private String typeName;
    private Integer totalBook;
    private String addressRoad;
    private String organizationName;
    private String phone;
    private String landArea;
    private Integer buildingArea;
    private String homepage;
    private String lastUpdate;
    private Integer providerCode;
    private String providerName;
}
