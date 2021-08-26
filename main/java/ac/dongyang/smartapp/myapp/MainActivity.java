package ac.dongyang.smartapp.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ImageView iv_roulette; //룰렛이미지 변수 생성
    private float startDegree = 0f; //초기각도 초기값 설정
    private float endDegree = 0f; //끝각도 초기값 설정
    private String cook; //각도에 따른 메뉴 저장

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv_roulette = findViewById(R.id.roulette); //애니메이션 이미지 인식
        Toast.makeText(getApplicationContext(),"룰렛을 터치하면 돌아갑니다.", Toast.LENGTH_LONG).show();
        //시작시 뜨는 설명문

        Button btn = findViewById(R.id.crystal); //id를 읽어와서 생성된 버튼 객체에 저장
        btn.setOnClickListener(new View.OnClickListener() { //클릭 이벤트 처리기
            @Override
            public void onClick(View view) {
                if(cook == "한식") {
                    //음식이 한식이면 메뉴액티비티1로 이동.
                    Intent it1 = new Intent(MainActivity.this, MenuActivity1.class);
                    it1.putExtra("cook",cook);
                    startActivity(it1);
                } else if(cook == "중식") {
                    //음식이 중식이면 메뉴액티비티2로 이동.
                    Intent it2 = new Intent(MainActivity.this, MenuActivity2.class);
                    it2.putExtra("cook", cook);
                    startActivity(it2);
                } else if(cook == "일식") {
                    //음식이 일식이면 메뉴액티비티3로 이동.
                    Intent it3 = new Intent(MainActivity.this, MenuActivity3.class);
                    it3.putExtra("cook", cook);
                    startActivity(it3);
                } else {
                    //음식이 양식이면 메뉴액티비티4로 이동.
                    Intent it4 = new Intent(MainActivity.this, MenuActivity4.class);
                    it4.putExtra("cook", cook);
                    startActivity(it4);
                }
            }
        });

        Button memoBtn = findViewById(R.id.memo); //id를 읽어와서 생성된 버튼 객체에 저장
        memoBtn.setOnClickListener(new View.OnClickListener() { //클릭이벤트 처리기
            @Override
            public void onClick(View view) {
                //인텐트 객체를 생성하고 버튼이벤트 활성화시 메모 액티비티로 이동
                Intent intent = new Intent(MainActivity.this, MemoActivity.class);
                intent.putExtra("memo","메모장");
                startActivity(intent);
            }
        });
    }

    public void rotate(View v){
        startDegree = 0; //룰렛은 한식부터 시작.
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
            // 회전후 각도가 0~89이면 양식
            cook = "양식";
        } else if(degree_rand >= 90 && degree_rand < 180) {
            // 회전후 각도가 90~179이면 일식
            cook = "일식";
        } else if(degree_rand >= 180 && degree_rand < 270) {
            // 회전후 각도가 180~269이면 중식
            cook = "중식";
        } else {
            // 회전후 각도가 270~360이면 한식
            cook = "한식";
        }
    }
}


