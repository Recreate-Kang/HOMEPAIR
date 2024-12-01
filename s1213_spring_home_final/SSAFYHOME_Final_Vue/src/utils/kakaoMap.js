// kakaoMap.js
let kakaoMarkers = [];
export function getKakaoMarkers() {
  console.log('getKakaoMarkers');
  return kakaoMarkers;
}
export function loadKakaoScript(apiKey) {
  return new Promise((resolve, reject) => {
    try {
      // Wait for Kakao Maps SDK to be loaded
      const checkKakao = setInterval(() => {
        if (window.kakao && window.kakao.maps) {
          clearInterval(checkKakao);
          console.log("Kakao Maps SDK found");
          resolve(window.kakao.maps);
        }
      }, 100);

      // Timeout after 10 seconds
      setTimeout(() => {
        clearInterval(checkKakao);
        reject(new Error("Timeout waiting for Kakao Maps SDK"));
      }, 10000);
    } catch (error) {
      reject(error);
    }
  });
}

export function initKakaoMap(container, options = {}) {
  if (!container) {
    console.error("Container element is required");
    return null;
  }

  if (!window.kakao || !window.kakao.maps) {
    console.error("Kakao Maps SDK is not loaded");
    return null;
  }

  const defaultOptions = {
    center: new window.kakao.maps.LatLng(37.5665, 126.9780), // 서울 중심
    level: 3
  };

  const mapOptions = { ...defaultOptions, ...options };
  
  try {
    const map = new window.kakao.maps.Map(container, mapOptions);
    console.log("Map initialized successfully");
    return map;
  } catch (error) {
    console.error("Failed to initialize map:", error);
    return null;
  }
}

export function updateMapMarkers(apartments, kakaoMap, handleMarkerClick) {
  if (!kakaoMap || !apartments) {
    console.error("Missing required parameters");
    return;
  }

  // 기존 마커들의 맵 참조 제거
  kakaoMarkers.forEach(({ marker }) => marker.setMap(null));
  kakaoMarkers = [];

  // 마커 생성 및 추가
  apartments.forEach((apt) => {
    if (!apt.latitude || !apt.longitude) {
      console.warn(`Missing coordinates for apartment: ${apt.aptName}`);
      return;
    }

    // 마커 위치 생성
    const position = new kakao.maps.LatLng(
      Number(apt.latitude),
      Number(apt.longitude)
    );

    // 마커 생성
    const marker = new kakao.maps.Marker({
      position: position,
      map: kakaoMap,
      title: apt.aptName
    });

    // 인포윈도우 내용
    const iwContent = `
      <div style="padding:10px;width:200px;">
        <div style="font-weight:bold;margin-bottom:5px;">${apt.aptName}</div>
        <div>건축년도: ${apt.buildYear}년</div>
      </div>
    `;

    // 인포윈도우 생성
    const infowindow = new kakao.maps.InfoWindow({
      content: iwContent,
      removable: true
    });

    // 클릭 이벤트 등록
    kakao.maps.event.addListener(marker, 'click', function() {
      // 이전에 열린 인포윈도우가 있다면 닫기
      if (kakaoMarkers.some(({ infowindow: iw }) => iw.getMap())) {
        kakaoMarkers.forEach(({ infowindow: iw }) => iw.close());
      }
      infowindow.open(kakaoMap, marker);
      handleMarkerClick(apt);
    });

    kakaoMarkers.push({ marker, item: apt, infowindow });
  });

  // 첫 번째 아파트 위치로 지도 이동
  if (apartments.length > 0) {
    const firstApt = apartments[0];
    const position = new kakao.maps.LatLng(
      Number(firstApt.latitude),
      Number(firstApt.longitude)
    );
    kakaoMap.panTo(position);
  }
}

export function highlightMarker(selectedApt, kakaoMarkers) {
  if (!selectedApt || !kakaoMarkers) return;

  kakaoMarkers.forEach(({ marker, item, infowindow }) => {
    if (item.aptSeq === selectedApt.aptSeq) {
      // 선택된 마커 강조
      const imageSrc = 'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png';
      const imageSize = new kakao.maps.Size(24, 35);
      const markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize);
      marker.setImage(markerImage);
      marker.setZIndex(10);
      infowindow.open(kakaoMap, marker);
    } else {
      // 다른 마커들은 기본 스타일로
      marker.setImage(null);
      marker.setZIndex(1);
      infowindow.close();
    }
  });
}

export function centerMapOnItem(item, kakaoMap, circles) {
  if (item && item.latitude && item.longitude && kakaoMap) {
    const position = new window.kakao.maps.LatLng(item.latitude, item.longitude);

    // Smoothly move the center to the new position over 0.4 seconds
    kakaoMap.panTo(position, { duration: 400 });

    // Clear existing circles
    circles.forEach((circle) => circle.setMap(null));
    circles.length = 0;

    // Add radius circles
    const circle100m = new window.kakao.maps.Circle({
      center: position,
      radius: 100, // 100m
      strokeWeight: 2,
      strokeColor: '#ff0000',
      strokeOpacity: 0.8,
      fillColor: '#ffcccc',
      fillOpacity: 0.3,
    });
    circle100m.setMap(kakaoMap);
    circles.push(circle100m);

    const circle300m = new window.kakao.maps.Circle({
      center: position,
      radius: 300, // 300m
      strokeWeight: 2,
      strokeColor: '#0000ff',
      strokeOpacity: 0.8,
      fillColor: '#ccccff',
      fillOpacity: 0.2,
    });
    circle300m.setMap(kakaoMap);
    circles.push(circle300m);

    const circle1km = new window.kakao.maps.Circle({
      center: position,
      radius: 1000, // 1km
      strokeWeight: 2,
      strokeColor: '#00ff00',
      strokeOpacity: 0.8,
      fillColor: '#ccffcc',
      fillOpacity: 0.1,
    });
    circle1km.setMap(kakaoMap);
    circles.push(circle1km);
  }
}