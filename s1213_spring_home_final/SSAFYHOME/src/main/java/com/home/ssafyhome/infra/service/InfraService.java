package com.home.ssafyhome.infra.service;

import com.home.ssafyhome.infra.dto.InfraListResponse;
import com.home.ssafyhome.infra.dto.RequestStore;

public interface InfraService {
    InfraListResponse getStoreListInRange(RequestStore request);
}
