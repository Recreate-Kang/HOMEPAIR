package com.home.ssafyhome.infra.mapper;


import com.home.ssafyhome.infra.model.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface InfraMapper {

    List<Hospital> findHospitalInRange(@Param("minLatitude") double minLatitude,
                                       @Param("maxLatitude") double maxLatitude,
                                       @Param("minLongitude") double minLongitude,
                                       @Param("maxLongitude") double maxLongitude);

    List<Library> findLibraryInRange(@Param("minLatitude") double minLatitude,
                                     @Param("maxLatitude") double maxLatitude,
                                     @Param("minLongitude") double minLongitude,
                                     @Param("maxLongitude") double maxLongitude);

    List<Mart> findMartInRange(@Param("minLatitude") double minLatitude,
                               @Param("maxLatitude") double maxLatitude,
                               @Param("minLongitude") double minLongitude,
                               @Param("maxLongitude") double maxLongitude);

    List<Park> findParkInRange(@Param("minLatitude") double minLatitude,
                               @Param("maxLatitude") double maxLatitude,
                               @Param("minLongitude") double minLongitude,
                               @Param("maxLongitude") double maxLongitude);

    List<School> findSchoolInRange(@Param("minLatitude") double minLatitude,
                                   @Param("maxLatitude") double maxLatitude,
                                   @Param("minLongitude") double minLongitude,
                                   @Param("maxLongitude") double maxLongitude);

    List<Subway> findSubwayInRange(@Param("minLatitude") double minLatitude,
                                   @Param("maxLatitude") double maxLatitude,
                                   @Param("minLongitude") double minLongitude,
                                   @Param("maxLongitude") double maxLongitude);

}

