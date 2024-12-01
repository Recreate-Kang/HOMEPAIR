package com.home.ssafyhome.infra.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Subway extends Locatable {
    private Integer keyId;
    private String subwayName;
    private String subwayLine;
}
