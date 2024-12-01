package com.home.ssafyhome.infra.dto;

import com.home.ssafyhome.infra.model.*;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class InfraListResponse {
    private List<Hospital> hospitalList;
    private List<Library> libraryList;
    private List<Mart> martList;
    private List<Park> parkList;
    private List<School> schoolList;
    private List<Subway> subwayList;

}
