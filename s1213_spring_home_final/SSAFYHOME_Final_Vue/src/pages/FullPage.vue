<template>
  <div class="full-page">
    <!-- Toggle button to open the sidebar -->
    <div class="sidebar-container">
      <button 
        class="toggle-button" 
        @click="toggleSidebar" 
        :style="{ left: isSidebarOpen ? '400px' : '20px' }"
        >
        <span class="toggle-icon">{{ isSidebarOpen ? '<' : '>' }}</span>
      </button>
      <Sidebar 
        :class="['sidebar', { show: isSidebarOpen }]"
        @updateMap="updateMapCoordinates"
        @showDetails="toggleSelectedItem"
      />
    </div>
    
    <!-- Map container -->
    <div class="map-container">
      <div id="map" class="map"></div>
      <ChatSidebar v-model:isOpen="showChat" />
    </div>

    <DetailsContainer 
      v-if="isSidebarOpen && apartmentStore.selectedApt"
      :class="['details-container', { show: isDetailsOpen }]" 
    />
    <button 
      v-if="apartmentStore.selectedApt"
      @click="toggleDetails" 
      class="toggle-button toggle-right"
    >
      <span class="toggle-icon">{{ isDetailsOpen ? '&gt;' : '&lt;' }}</span>
    </button>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, computed, onUnmounted } from 'vue';
import { useApiStore } from '../stores/apiStore';
import { useApartmentStore } from '../stores/apartmentStore';
import { useInfraStore } from '../stores/infraStore';
import Sidebar from './Sidebar.vue';
import DetailsContainer from '../components/DetailsContainer.vue';
import ChatSidebar from '../components/chat/ChatSidebar.vue';

const apiStore = useApiStore();
const apartmentStore = useApartmentStore();
const infraStore = useInfraStore();
const isSidebarOpen = ref(false);
const isDetailsOpen = ref(false);
const showChat = ref(true);
const kakaoMap = ref(null);
const kakaoMarkers = ref([]);
const circles = ref([]);
const infraMarkers = ref([]); // 인프라 마커 배열
let mapEventListeners = [];

// 지도 스크립트 로딩 최적화
async function loadKakaoScript(apiKey) {
  if (window.kakao?.maps) return window.kakao.maps;

  return new Promise((resolve, reject) => {
    const script = document.createElement('script');
    script.src = `//dapi.kakao.maps/v2/maps/sdk.js?appkey=${apiKey}&libraries=services&autoload=false`;
    script.async = true;
    
    script.onload = () => {
      window.kakao.maps.load(() => resolve(window.kakao.maps));
    };
    script.onerror = reject;

    document.head.appendChild(script);
  });
}

// 지도 초기화 최적화
onMounted(async () => {
  try {
    const kakaoApiKey = '';
    await loadKakaoScript(kakaoApiKey);

    const container = document.getElementById('map');
    const options = {
      center: new kakao.maps.LatLng(37.5012, 127.039585),
      level: 3
    };

    kakaoMap.value = new kakao.maps.Map(container, options);

    // 아파트 목록 변경 감지
    watch(() => apartmentStore.apartments, (newApartments) => {
      if (newApartments) {
        updateMapMarkers(newApartments);
      }
    }, { deep: true });

    // 인프라 데이터 변경 감지
    watch(
      [
        () => infraStore.hospitals,
        () => infraStore.libraries,
        () => infraStore.marts,
        () => infraStore.parks,
        () => infraStore.schools,
        () => infraStore.subways
      ],
      () => {
        // 어떤 인프라 데이터든 변경되면 마커 생성
        updateInfraMarkers();
      },
      { deep: true }
    );

    // 아파트 선택 시 지도 이동 및 인프라 마커 생성
    watch(() => apartmentStore.selectedApt, (newApt) => {
      if (newApt) {
        handleAptSelect(newApt);
      } else {
        // 선택 해제 시 원과 인프라 마커 제거
        circles.value.forEach((circle) => circle.setMap(null));
        circles.value = [];
        infraMarkers.value.forEach(marker => marker.setMap(null));
        infraMarkers.value = [];
      }
    });

    // DetailsContainer가 활성화될 때 사이드바도 함께 활성화
    watch(apartmentStore.showDetails, (newValue) => {
      if (newValue) {
        isSidebarOpen.value = true;
      }
    });

  } catch (error) {
    console.error('Error during map initialization:', error);
  }
});

// 지도 좌표 업데이트
const updateMapCoordinates = (coordinates) => {
  if (!kakaoMap.value || !coordinates) return;
  
  const position = new kakao.maps.LatLng(
    parseFloat(coordinates.lat),
    parseFloat(coordinates.lng)
  );
  
  kakaoMap.value.panTo(position);
  
  // 현재 아파트 목록의 마커도 업데이트
  if (apartmentStore.apartments.length > 0) {
    updateMapMarkers(apartmentStore.apartments);
  }
};

// 아파트 마커 업데이트
const updateMapMarkers = (apartments) => {
  // 기존 마커 제거
  kakaoMarkers.value.forEach(({ marker }) => marker.setMap(null));
  kakaoMarkers.value = [];

  // 새 마커 생성
  apartments.forEach((apt) => {
    const position = new kakao.maps.LatLng(apt.latitude, apt.longitude);
    
    const marker = new kakao.maps.Marker({
      position: position,
      map: kakaoMap.value,
      title: apt.apartmentName,
    });

    // 클릭 이벤트 추가
    kakao.maps.event.addListener(marker, 'click', () => {
      apartmentStore.selectApartment(apt);
    });

    kakaoMarkers.value.push({ marker, item: apt });
  });

  // 첫 번째 마커 위치로 지도 중심 이동
  if (apartments.length > 0) {
    const firstPosition = new kakao.maps.LatLng(
      apartments[0].latitude,
      apartments[0].longitude
    );
    kakaoMap.value.setCenter(firstPosition);
  }
};

// 인프라 마커 업데이트
const updateInfraMarkers = () => {
  // 기존 마커 제거
  infraMarkers.value.forEach(({ marker, overlay }) => {
    if (marker) marker.setMap(null);
    if (overlay) overlay.setMap(null);
  });
  infraMarkers.value = [];

  // 새 마커 생성
  const addMarkers = (items, title, icon) => {
    if (items && items.length > 0) {
      items.forEach(item => {
        if (!item.latitude || !item.longitude) return;

        const position = new kakao.maps.LatLng(
          parseFloat(item.latitude),
          parseFloat(item.longitude)
        );

        // 마커 내용
        const markerContent = document.createElement('div');
        markerContent.className = 'infra-marker';
        markerContent.innerHTML = `
          <div class="infra-circle-icon">${icon}</div>
        `;

        // 마커 생성
        const marker = new kakao.maps.CustomOverlay({
          position: position,
          content: markerContent,
          yAnchor: 1.3
        });

        // 정보창 내용
        const infoContent = document.createElement('div');
        infoContent.className = 'infra-overlaybox';
        infoContent.innerHTML = `
          <div class="infra-boxtitle">${title} 정보
            <div class="infra-close" onclick="this.parentElement.parentElement.style.display='none'">×</div>
          </div>
          <div class="infra-first">
            <div class="infra-circle-icon">${icon}</div>
            <div class="infra-title">${getInfraName(item, title)}</div>
          </div>
          <ul class="infra-list">
            ${item.address ? `
              <li class="infra-item">
                <span class="infra-label">주소</span>
                <span class="infra-content">${item.address}</span>
              </li>
            ` : ''}
            ${item.distance ? `
              <li class="infra-item">
                <span class="infra-label">거리</span>
                <span class="infra-content">${Math.round(item.distance)}m</span>
                <span class="infra-arrow infra-up"></span>
              </li>
            ` : ''}
          </ul>
        `;

        // 정보창 생성
        const infoOverlay = new kakao.maps.CustomOverlay({
          position: position,
          content: infoContent,
          yAnchor: 1.5,
          zIndex: 2
        });

        // 마커 클릭 이벤트
        markerContent.addEventListener('click', () => {
          // 다른 정보창들 닫기
          infraMarkers.value.forEach(({ overlay }) => {
            if (overlay) overlay.setMap(null);
          });
          infoOverlay.setMap(kakaoMap.value);
        });

        // 마커를 지도에 표시
        marker.setMap(kakaoMap.value);
        infraMarkers.value.push({ marker, overlay: infoOverlay });
      });
    }
  };

  // 각 카테고리별 마커 생성
  addMarkers(infraStore.hospitals, '병원', '🏥');
  addMarkers(infraStore.libraries, '도서관', '📚');
  addMarkers(infraStore.marts, '마트', '🏪');
  addMarkers(infraStore.parks, '공원', '🌳');
  addMarkers(infraStore.schools, '학교', '🏫');
  addMarkers(infraStore.subways, '지하철', '🚇');
};

// 인프라 이름 가져오기 함수
const getInfraName = (item, type) => {
  switch(type) {
    case '병원': return item.serviceName || '이름 없음';
    case '도서관': return item.serviceName || '이름 없음';
    case '마트': return item.serviceName || '이름 없음';
    case '공원': return item.parkName || '이름 없음';
    case '학교': return item.schoolName || '이름 없음';
    case '지하철': return item.subwayName || '이름 없음';
    default: return '이름 없음';
  }
};

// 지도 이동 함수
const moveToLocation = (lat, lng) => {
  if (!kakaoMap.value) return;
  
  const moveLatLng = new kakao.maps.LatLng(lat, lng);
  kakaoMap.value.panTo(moveLatLng);
  //kakaoMap.value.setLevel(3);
};

// 아파트 선택 처리 함수
const handleAptSelect = async (apt) => {
  if (!apt || !apt.latitude || !apt.longitude) return;
  
  try {
    // 지도 이동
    moveToLocation(parseFloat(apt.latitude), parseFloat(apt.longitude));
    
    // 원 표시
    drawCircles(parseFloat(apt.latitude), parseFloat(apt.longitude));
    
    // 인프라 정보 로드
    await infraStore.getStoreListInRange(apt.latitude, apt.longitude);
    
    // 상세 정보 표시 설정
    apartmentStore.showDetails = true;
  } catch (error) {
    console.error('Error handling apartment selection:', error);
  }
};

// 원 그리기 함수
const drawCircles = (lat, lng) => {
  if (!kakaoMap.value) return;

  // 기존 원 제거
  circles.value.forEach((circle) => circle.setMap(null));
  circles.value = [];

  const position = new kakao.maps.LatLng(lat, lng);
  
  // 원 설정
  const circleConfigs = [
    { radius: 100, color: '#ff0000', fillColor: '#ffcccc', opacity: 0.3 },
    { radius: 300, color: '#0000ff', fillColor: '#ccccff', opacity: 0.2 },
    { radius: 1000, color: '#00ff00', fillColor: '#ccffcc', opacity: 0.1 }
  ];

  // 원 생성
  circleConfigs.forEach(config => {
    const circle = new kakao.maps.Circle({
      center: position,
      radius: config.radius,
      strokeWeight: 2,
      strokeColor: config.color,
      strokeOpacity: 0.8,
      fillColor: config.fillColor,
      fillOpacity: config.opacity
    });
    circle.setMap(kakaoMap.value);
    circles.value.push(circle);
  });
};

// 아파트 선택 시 실행되는 함수
function toggleSelectedItem(item) {
  if (apartmentStore.selectedApt?.id === item.id) {
    apartmentStore.selectedApt = null;
    infraStore.clearInfraData();
    resetMarkers();
    isDetailsOpen.value = false;
  } else {
    apartmentStore.selectedApt = item;
    isSidebarOpen.value = true;
    isDetailsOpen.value = true;
    highlightMarker(item);
    handleAptSelect(item);
  }
}

function highlightMarker(item) {
  // 모든 마커의 스타일 초기화
  kakaoMarkers.value.forEach(({ marker, item: apt }) => {
    const markerElement = marker.getContent();
    markerElement.classList.remove('selected');
  });

  // 선택된 마커 강조
  const selectedMarker = kakaoMarkers.value.find(({ item: apt }) => apt.aptSeq === item.aptSeq);
  if (selectedMarker) {
    const markerElement = selectedMarker.marker.getContent();
    markerElement.classList.add('selected');
  }
}

function centerMapOnItem(item) {
  if (item && item.latitude && item.longitude && kakaoMap.value) {
    const position = new kakao.maps.LatLng(
      parseFloat(item.latitude),
      parseFloat(item.longitude)
    );
    
    // 부드럽게 해당 위치로 이동
    kakaoMap.value.panTo(position);
    
    // 지도 레벨 조정 (줌 레벨)
    kakaoMap.value.setLevel(3);

    // Clear existing circles
    circles.value.forEach((circle) => circle.setMap(null));
    circles.value = [];

    // Add circles with different radiuses
    const circleConfigs = [
      { radius: 100, color: '#ff0000', fillColor: '#ffcccc', opacity: 0.3 },
      { radius: 300, color: '#0000ff', fillColor: '#ccccff', opacity: 0.2 },
      { radius: 1000, color: '#00ff00', fillColor: '#ccffcc', opacity: 0.1 }
    ];

    circleConfigs.forEach(config => {
      const circle = new kakao.maps.Circle({
        center: position,
        radius: config.radius,
        strokeWeight: 2,
        strokeColor: config.color,
        strokeOpacity: 0.8,
        fillColor: config.fillColor,
        fillOpacity: config.opacity
      });
      circle.setMap(kakaoMap.value);
      circles.value.push(circle);
    });
  }
}

function resetMarkers() {
  kakaoMarkers.value.forEach(({ marker }) => marker.setMap(null));
  kakaoMarkers.value = [];
  
  // Clear circles when resetting
  circles.value.forEach((circle) => circle.setMap(null));
  circles.value = [];
  
  // Clear infra markers
  infraMarkers.value.forEach(marker => {
    if (marker.marker) marker.marker.setMap(null);
    if (marker.overlay) marker.overlay.setMap(null);
  });
  infraMarkers.value = [];
}

// 컴포넌트 정리
onUnmounted(() => {
  // 이벤트 리스너 제거
  mapEventListeners.forEach(listener => {
    kakao.maps.event.removeListener(listener);
  });
  
  // Clear all markers and circles
  resetMarkers();
});

function toggleSidebar() {
  isSidebarOpen.value = !isSidebarOpen.value;
}

function toggleDetails() {
  isDetailsOpen.value = !isDetailsOpen.value;
}

const getInitialButtonStyle = computed(() => {
  return {
    left: '15px',  // 위치 조정
    top: '100px'
  }
});

const getToggleButtonStyle = computed(() => {
  // Sidebar가 열려있고 DetailsContainer도 열려있는 경우
  if (isSidebarOpen.value && apartmentStore.selectedApt) {
    return {
      left: '785px',
      top: '100px'
    }
  }
  // Sidebar만 열려있는 경우
  return {
    left: '385px',
    top: '100px'
  }
});
</script>

<style>
/* 전역 스타일로 변경 */
.full-page {
  position: relative;
  width: 100%;
  height: 100vh;
  overflow: hidden;
  touch-action: none;  /* 모바일 스크롤 방지 */
}

body {
  overflow: hidden;  /* 전체 페이지 스크롤 방지 */
  margin: 0;
  padding: 0;
}

.map-container {
  position: fixed;
  top: 60px;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 100;
}

.map {
  width: 100%;
  height: 100%;
}

.sidebar-container {
  position: fixed;
  left: 0;
  top: 60px;
  bottom: 0;
  z-index: 200;
}

.sidebar {
  position: relative;
  width: 400px;
  height: 100%;
  background: white;
  transform: translateX(-100%);
  transition: transform 0.3s ease;
  box-shadow: 2px 0 5px rgba(0, 0, 0, 0.1);
  overflow-y: auto;
  scrollbar-width: none;  /* Firefox */
  -ms-overflow-style: none;  /* IE and Edge */
}

.sidebar::-webkit-scrollbar {
  display: none;  /* Chrome, Safari, Opera */
}

.sidebar.show {
  transform: translateX(0);
}

.details-container {
  position: fixed;
  top: 80px;
  left: 420px; /* sidebar width (400px) + 20px gap */
  width: 380px;
  max-height: calc(100vh - 100px);
  background: white;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
  padding: 25px;
  border-radius: 12px;
  overflow-y: auto;
  transform: translateX(-100%);
  transition: transform 0.3s ease;
  z-index: 100;
  scrollbar-width: none;
  -ms-overflow-style: none;
}

.details-container.show {
  transform: translateX(0);
}

.details-container::-webkit-scrollbar {
  display: none;
}

.details-container .toggle-button {
  position: absolute;
  top: 15px;
  right: 15px;
  width: 30px;
  height: 30px;
  border: none;
  background: transparent;
  cursor: pointer;
  font-size: 20px;
  color: #666;
  transition: color 0.2s;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1001;
}

.details-container .toggle-button:hover {
  color: #009688;
}
.toggle-button {
  position: absolute;
  z-index: 150;  /* 지도보다 위, 컨테이너보다 아래 */
  background: white;
  color: #333;
  border: none;
  border-radius: 0 4px 4px 0;
  width: 30px;
  height: 60px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  box-shadow: 2px 0 4px rgba(0, 0, 0, 0.2);
  transition: all 0.3s ease; /* 애니메이션 속도 조정 */
  padding: 0;
  line-height: 1;
  top: 45px;
}

.toggle-button:hover {
  background: #00796b;
  transform: translateX(2px);
}

.toggle-icon {
  display: inline-block;
  font-family: monospace;
  transform: scaleY(1.5);
}


/* 인프라 마커 기본 스타일 */
.infra-marker {
  position: relative;
  cursor: pointer;
  width: 32px;
  height: 32px;
  background: #fff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2px 6px rgba(0,0,0,0.3);
  border: 2px solid #2b2d36;
  transition: transform 0.2s ease;
}

.infra-marker:hover {
  transform: scale(1.1);
}

.infra-marker .infra-circle-icon {
  font-size: 18px;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
}

.infra-overlaybox {
  position: relative;
  width: 280px;
  background: #2b2d36 !important;
  border-radius: 8px;
  padding: 15px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.3);
}

.infra-overlaybox div, .infra-overlaybox ul {
  overflow: hidden;
  margin: 0;
  padding: 0;
}

.infra-overlaybox li {
  list-style: none;
}

.infra-boxtitle {
  color: #fff !important;
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 10px;
  padding-bottom: 10px;
  border-bottom: 1px solid rgba(255,255,255,0.2);
  position: relative;
}

.infra-close {
  position: absolute;
  right: 0;
  top: 0;
  color: #fff !important;
  cursor: pointer;
  font-size: 18px;
  opacity: 0.8;
}

.infra-close:hover {
  opacity: 1;
}

.infra-first {
  position: relative;
  padding: 15px 0;
  display: flex;
  align-items: center;
  border-bottom: 1px solid rgba(255,255,255,0.1);
}

.infra-circle-icon {
  width: 40px;
  height: 40px;
  background: rgba(255,255,255,0.1);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  margin-right: 12px;
}

.infra-title {
  color: #fff !important;
  font-size: 15px;
  font-weight: 500;
}

.infra-list {
  padding: 10px 0;
}

.infra-item {
  position: relative;
  margin-bottom: 8px;
  padding: 8px 0;
  color: #aaabaf !important;
  font-size: 13px;
  display: flex;
  align-items: center;
  border-bottom: 1px solid rgba(255,255,255,0.1);
}

.infra-item:last-child {
  border-bottom: none;
  margin-bottom: 0;
}

.infra-label {
  color: #aaabaf !important;
  width: 60px;
}

.infra-content {
  color: #fff !important;
  flex: 1;
}

.infra-arrow {
  position: absolute;
  right: 10px;
  width: 5px;
  height: 3px;
  background: url('https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/updown.png') no-repeat;
}

.infra-arrow.infra-up {
  background-position: 0 -40px;
}

.infra-item:hover {
  background: rgba(255,255,255,0.1);
}

.infra-item:hover .infra-arrow.infra-up {
  background-position: 0 0;
}

.navbar {
  z-index: 1000;
}

.mypage-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 2rem;
}

.profile-section {
  margin-bottom: 2rem;
}

.profile-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}

.profile-header h1 {
  font-size: 2rem;
  color: #2b2d36;
  font-weight: 600;
  margin: 0;
}

.edit-profile-btn {
  padding: 0.75rem 1.5rem;
  background-color: #009688;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 1rem;
  font-weight: 500;
  display: flex;
  align-items: center;
  gap: 8px;
}

.edit-profile-btn:hover {
  background-color: #00796b;
  transform: translateY(-2px);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.profile-card {
  background-color: #f8f9fa;
  border-radius: 12px;
  padding: 2rem;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.profile-info {
  display: flex;
  align-items: center;
  gap: 2rem;
}

.avatar {
  width: 80px;
  height: 80px;
  background-color: #009688;
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 2rem;
  font-weight: 600;
}

.info-details {
  flex: 1;
}

.info-details h2 {
  font-size: 1.5rem;
  color: #2b2d36;
  margin: 0 0 0.5rem 0;
  font-weight: 600;
}

.info-details .email {
  color: #666;
  margin: 0 0 1rem 0;
  font-size: 1rem;
}

.account-badge {
  display: inline-block;
  padding: 0.5rem 1rem;
  border-radius: 20px;
  font-size: 0.875rem;
  font-weight: 500;
}

.account-badge.premium {
  background-color: #009688;
  color: white;
}

.apartment-section, .places-section {
  background-color: #f8f9fa;
  border-radius: 12px;
  padding: 1.5rem;
  margin-bottom: 2rem;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.apartment-section h2, .places-section h2 {
  font-size: 1.5rem;
  color: #2b2d36;
  margin: 0 0 1.5rem 0;
  font-weight: 600;
}

.no-apartment, .no-places {
  background-color: white;
  border-radius: 8px;
  padding: 2.5rem;
  text-align: center;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.no-apartment i, .no-places i {
  font-size: 3.5rem;
  color: #009688;
  margin-bottom: 1.5rem;
}

.no-apartment p, .no-places p {
  color: #2b2d36;
  margin: 0 0 1.5rem 0;
  font-size: 1.1rem;
}

.select-apt-btn, .add-place-btn {
  padding: 0.75rem 1.5rem;
  background-color: #009688;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 1rem;
  font-weight: 500;
  display: flex;
  align-items: center;
  gap: 8px;
}

.select-apt-btn:hover, .add-place-btn:hover {
  background-color: #00796b;
  transform: translateY(-2px);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.select-apt-btn i, .add-place-btn i {
  font-size: 1rem;
}
</style>