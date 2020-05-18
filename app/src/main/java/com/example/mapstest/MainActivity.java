package com.example.mapstest;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.widget.Toast;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapOptions;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.LocationOverlay;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;
//
public class MainActivity extends AppCompatActivity
      implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;
    private NaverMap naverMap;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            FragmentManager fm = getSupportFragmentManager();
            MapFragment mapFragment = (MapFragment)fm.findFragmentById(R.id.map);
            if (mapFragment == null) {
                mapFragment = MapFragment.newInstance();
                fm.beginTransaction().add(R.id.map, mapFragment).commit();
            }
            mapFragment.getMapAsync(this);

            locationSource =
                    new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) { // 권한 거부됨
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
            }
            return;
        }
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);
    }

        @UiThread
        @Override
        public void onMapReady(@NonNull NaverMap naverMap) {

            /* 토스트로 위도,경로 나타냄
            LatLng coord = new LatLng(37.5670135, 126.9783740);

            Toast.makeText(getApplicationContext(),
                    "위도: " + coord.latitude + ", 경도: " + coord.longitude,
                    Toast.LENGTH_SHORT).show(); */

            // 사용자 인터페이스 설정
            UiSettings uiSettings = naverMap.getUiSettings();
            uiSettings.setCompassEnabled(true);
            uiSettings.setLocationButtonEnabled(true);
            //기울기 변경 허용
            uiSettings.setRotateGesturesEnabled(true);

            // 클릭시 위도와 경도 정보 토스트로 나타냄
            naverMap.setOnMapClickListener((point, coord) ->
                    Toast.makeText(this, coord.latitude + ", " + coord.longitude,
                            Toast.LENGTH_SHORT).show());

            this.naverMap = naverMap;
            naverMap.setLocationSource(locationSource);

            naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);

            LocationOverlay locationOverlay = naverMap.getLocationOverlay();
            locationOverlay.setVisible(true);
            locationOverlay.setPosition(new LatLng(37.5670135, 126.9783740));
            locationOverlay.setCircleRadius(100);
            //locationOverlay.setIcon(OverlayImage.fromResource(R.drawable.location_overlay_icon));
        }



    }

