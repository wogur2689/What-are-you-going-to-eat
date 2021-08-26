package ac.dongyang.smartapp.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Random;

public class MenuActivity2 extends AppCompatActivity {

    private ImageView iv_roulette; //룰렛이미지 변수 생성
    private float startDegree = 0f; //초기각도 초기값 설정
    private float endDegree = 0f; //끝각도 초기값 설정
    private String cook = "중식"; //저장된 요리
    private String menu; //각도에 따른 메뉴 저장

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu2);

        Intent it = getIntent();
        String msg = it.getStringExtra("cook");
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

        iv_roulette = findViewById(R.id.roulette_menu2); //애니메이션 이미지 인식

        Button btn = findViewById(R.id.crystal); //id를 읽어와서 생성된 버튼 객체에 저장
        btn.setOnClickListener(new View.OnClickListener() { //클릭 이벤트 처리기
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MenuActivity2.this, MapActivity.class);
                if(menu == "볶음밥") {
                    //각도가 0~89에 해당하는 메뉴를 가지고 지도로 이동.
                    it.putExtra("menu", menu);
                    it.putExtra("cook", cook);
                } else if(menu == "탕수육") {
                    //각도가 90~179에 해당하는 메뉴를 가지고 지도로 이동.
                    it.putExtra("menu", menu);
                    it.putExtra("cook", cook);
                } else if(menu == "짬뽕") {
                    //각도가 180~269에 해당하는 메뉴를 가지고 지도로 이동.
                    it.putExtra("menu", menu);
                    it.putExtra("cook", cook);
                } else {
                    //각도가 270~360에 해당하는 메뉴를 가지고 지도로 이동.
                    it.putExtra("menu", menu);
                    it.putExtra("cook", cook);
                }
                startActivity(it);
            }
        });
    }

    public void rotate(View v){
        startDegree = 0; //룰렛은 볶음밥부터 시작.
        Random rand = new Random(); //랜덤 객체 생성
        int degree_rand = rand.nextInt(360); //0~359 사이의 정수 추출
        endDegree = startDegree + 360 * 3 + degree_rand; // 회전 종료 각도 설정 (초기 각도에서 세 바퀴 돌고 0~359 난수만큼 회전
        ObjectAnimator object = ObjectAnimator.ofFloat(iv_roulette,"rotation", startDegree, endDegree);
        //애니메이션 이미지에 대해 초기 각도에서 회전 종료각도까지 회전하는 애니메이션 객체 생성
        object.setInterpolator(new AccelerateDecelerateInterpolator());
        //애니메이션 속력 설정
        object.setDuration(5000);
        //애니메이션 시간(5초)
        object.start();
        //애니메이션 시작
        if(degree_rand >= 0 && degree_rand < 90) {
            menu = "볶음밥";
        } else if(degree_rand >= 90 && degree_rand < 180) {
            menu = "탕수육";
        } else if(degree_rand >= 180 && degree_rand < 270) {
            menu = "짬뽕";
        } else {
            menu = "짜장면";
        }
    }
}
