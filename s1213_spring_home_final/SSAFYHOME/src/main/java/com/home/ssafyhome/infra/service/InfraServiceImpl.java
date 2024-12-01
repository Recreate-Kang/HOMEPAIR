package com.home.ssafyhome.infra.service;

import com.home.ssafyhome.infra.dto.InfraListResponse;
import com.home.ssafyhome.infra.dto.RequestStore;
import com.home.ssafyhome.infra.mapper.InfraMapper;
import com.home.ssafyhome.infra.model.*;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class InfraServiceImpl implements InfraService {

    private static final double ONE_KM_LATITUDE = 0.011 * 1;
    private static final double ONE_KM_LONGITUDE = 0.009 * 1;
    private static final double LATITUDE_METERS = 111320;
    private static final double LONGITUDE_METERS_AT_EQUATOR = 111320;
    InfraMapper infraMapper;

    InfraServiceImpl(InfraMapper infraMapper) {
        this.infraMapper = infraMapper;
    }

    @Override
    public InfraListResponse getStoreListInRange(RequestStore request) {
        double orginLatitude = Double.parseDouble(request.getLatitude());
        double orginLongitude = Double.parseDouble(request.getLongitude());

        double minLatitude = orginLatitude - ONE_KM_LATITUDE;
        double maxLatitude = orginLatitude + ONE_KM_LATITUDE;
        double minLongitude = orginLongitude - ONE_KM_LONGITUDE;
        double maxLongitude = orginLongitude + ONE_KM_LONGITUDE;

        List<Hospital> hospitalList = infraMapper.findHospitalInRange(minLatitude, maxLatitude, minLongitude, maxLongitude);
        List<Library> libraryList = infraMapper.findLibraryInRange(minLatitude, maxLatitude, minLongitude, maxLongitude);
        List<Mart> martList = infraMapper.findMartInRange(minLatitude, maxLatitude, minLongitude, maxLongitude);
        List<Park> parkList = infraMapper.findParkInRange(minLatitude, maxLatitude, minLongitude, maxLongitude);
        List<School> schoolList = infraMapper.findSchoolInRange(minLatitude, maxLatitude, minLongitude, maxLongitude);
        List<Subway> subwayList = infraMapper.findSubwayInRange(minLatitude, maxLatitude, minLongitude, maxLongitude);
        estimateDistance(hospitalList, orginLatitude, orginLongitude);
        estimateDistance(libraryList, orginLatitude, orginLongitude);
        estimateDistance(martList, orginLatitude, orginLongitude);
        estimateDistance(parkList, orginLatitude, orginLongitude);
        estimateDistance(schoolList, orginLatitude, orginLongitude);
        estimateDistance(subwayList, orginLatitude, orginLongitude);
        sortByDistance(hospitalList);
        sortByDistance(libraryList);
        sortByDistance(martList);
        sortByDistance(parkList);
        sortByDistance(schoolList);
        sortByDistance(subwayList);

        return InfraListResponse.builder()
                .hospitalList(hospitalList)
                .libraryList(libraryList)
                .parkList(parkList)
                .schoolList(schoolList)
                .subwayList(subwayList)
                .build();
    }

    private void sortByDistance(List<? extends Locatable> place) {
        place.sort(Comparator.comparingDouble((Locatable p) -> p.getDistance()));
    }

    private void estimateDistance(List<? extends Locatable> place, double originLatitude, double originLongitude) {
        double cosLat = Math.cos(Math.toRadians(originLatitude));
        double longitudeMeters = LONGITUDE_METERS_AT_EQUATOR * cosLat;
        place.forEach(locatable -> {
            double deltaLat = (locatable.getLatitude() - originLatitude) * LATITUDE_METERS;
            double deltaLon = (locatable.getLongitude() - originLongitude) * longitudeMeters;
            double distance = Math.sqrt(deltaLat * deltaLat + deltaLon * deltaLon);
            locatable.setDistance(distance);
        });
    }
}

