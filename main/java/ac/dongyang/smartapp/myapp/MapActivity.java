package ac.dongyang.smartapp.myapp;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {
    //코드 출처: Step by Step 안드로이드 프로그래밍 개정 5판
    private static final int PERMISSION_REQUEST_CODE = 100; //위치반환구현체에 사용.

    private NaverMap mMap;
    private FusedLocationSource mLocationSource;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //다른 요리를 결정하기 위해 처음으로 돌아가는 버튼 이벤트
        Button button = findViewById(R.id.buttonReset); //id값을 찾아와 버튼객체 생성과 함께 저장
        button.setOnClickListener(new View.OnClickListener() { //버튼 클릭 리스너 구현
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MapActivity.this, MainActivity.class); // 인텐트 객체를 생성하여 어떤 액티비티로 갈건지 명시.
                startActivity(in); //메인액티비티로 이동
            }
        });

        //지금까지 결정한 요리와 메뉴를 띄우는 버튼 이벤트
        Button btn = findViewById(R.id.buttonCrystal); //id값을 찾아와 버튼객체 생성과 함께 저장
        btn.setOnClickListener(new View.OnClickListener() { //버튼 클릭 리스너 구현
            @Override
            public void onClick(View view) {
                //xml에 있는 텍스트뷰 객체생성
                TextView textView1 = findViewById(R.id.cookMap);
                TextView textView2 = findViewById(R.id.menuMap);
                //메뉴액티비티에서 넘겨받은 값 저장
                Intent intent = getIntent();
                String food1 = intent.getStringExtra("cook");
                String food2 = intent.getStringExtra("menu");
                //텍스트뷰에 넘겨받은 값을 표시
                textView1.setText(food1);
                textView2.setText(food2);
            }
        });

        //지도를 출력할 프래그먼트 인식
        MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) { //지도가 없으면 지도를 생성
            mapFragment = MapFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.map, mapFragment).commit();
        }

        //지도 사용이 준비되면 onMapReady 콜백 메소드 호출
        mapFragment.getMapAsync(this);

        //위치를 반환하는 구현체인 FusedLocationSource 생성
        mLocationSource = new FusedLocationSource(this, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) { //지도 객체를 여러 메소드에서 사용할 수 있도록 글로벌 객체로 할당
        //코드 출처 https://stickode.com/detail.html?no=1494
        mMap = naverMap;
        //네이버맵 객체 받아서 네이버맵 객체에 위치 소스 지정
        mMap.setLocationSource(mLocationSource);
        mMap.setLocationTrackingMode(LocationTrackingMode.Follow);

        //지도상에 나침반을 이용한 현위치 표시
        UiSettings uiSettings = mMap.getUiSettings(); //UISettings 객체 얻어와 변수에 저장
        uiSettings.setCompassEnabled(true); //나침반
        uiSettings.setScaleBarEnabled(true); //거리
        uiSettings.setZoomControlEnabled(true); //줌
        uiSettings.setLocationButtonEnabled(true); //내가 있는곳

        locationListener = new LocationListener() {
            public void onLocationChanged(@NonNull Location location) { //사용 x
            }
            //사용자에 의해 Provider가 사용 가능하게 설정될 때
            public void onProviderEnabled(@NonNull String provider) {
                alertProvider(provider);
            }
            //사용자에 의해 Provider가 사용 불가능하게 설정될 때
            public void onProviderDisabled(@NonNull String provider) {
                checkProvider(provider);
            }
        };
        //시스템 위치 서비스 관리 객체 생성
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        //정확히 위치 접근 권한이 설정되어 있지 않으면 사용자에게 권한 요구
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            return;
        }
        String locationProvider;
        //GPS에 의한 위치 변경 요구
        locationProvider = locationManager.GPS_PROVIDER;
        locationManager.requestLocationUpdates(locationProvider,1,1,locationListener);
    }
    // GPS가 꺼져있는 경우
    public void checkProvider(String provider) {
        Toast.makeText(this, provider + "에 의한 위치서비스가 꺼져 있습니다.", Toast.LENGTH_SHORT).show();
        //설정에서 GPS를 사용할 것인지 여부를 묻는 창으로 이동
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
    }
    //GPS를 켤 경우
    public void alertProvider(String provider) {
        Toast.makeText(this, provider + "서비스가 켜졌습니다!", Toast.LENGTH_SHORT).show();
    }
}
