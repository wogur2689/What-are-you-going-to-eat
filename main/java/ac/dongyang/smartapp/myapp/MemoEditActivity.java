package ac.dongyang.smartapp.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MemoEditActivity extends AppCompatActivity {
    private EditText mEditName, mEditFoodName, mEditTaste; //항목을 저장
    private int mItem = -1; //인덱스 저장
    private DBHelper mDBHelper; //DBHelper 객체 생성
    private SQLiteDatabase mDB; // SQLite 객체 생성
    private int mISelectedID = 0; //현재 선택된 항목의 아이디

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_edit);
        //선언한 객체에 id값을 얻어와서 저장
        mEditName = findViewById(R.id.editTextWName);
        mEditFoodName = findViewById(R.id.editTextFoodName);
        mEditTaste = findViewById(R.id.editTextTaste);

        mDBHelper = new DBHelper(getApplicationContext()); //DBHelper 객체 생성

        Intent intent = getIntent(); //인텐트를 얻어옴.
        if(intent != null) {
            mItem = intent.getIntExtra("item", -1); //현재 항목의 인덱스 값을 얻어옴.
            mISelectedID = intent.getIntExtra("id", 0); //현재 선택된 항목의 아이디 값을 얻어옴.
            if(mItem != -1){
                mEditName.setText(intent.getStringExtra("name")); //EditText에 식당 이름을 보여줌
                mEditFoodName.setText(intent.getStringExtra("foodName")); //EditText에 식사한 음식을 보여줌
                mEditTaste.setText(intent.getStringExtra("taste")); //EditText에 맛 평가를 보여줌
            }
        }

        //취소버튼을 누르고 돌아가기
        Button btnCancel =  findViewById(R.id.buttonCancel); //버튼 객체를 얻어옴
        btnCancel.setOnClickListener(new View.OnClickListener() { //클릭리스너 이벤트 구현
            @Override
            public void onClick(View view) {
                finish(); //메모액티비티로 돌아감.
            }
        });

        Button btnSave = findViewById(R.id.buttonSave); //버튼 객체를 얻어옴
        btnSave.setOnClickListener(new View.OnClickListener() { //클릭리스너 이벤트 구현
            @Override
            public void onClick(View view) {
                //읽어온 값들을 얻어와 변수타입에 맞게 변수에 저장.
                String sName = mEditName.getText().toString().trim();
                String sFoodName = mEditFoodName.getText().toString().trim();
                String sTaste = mEditTaste.getText().toString().trim();

                //하나라도 입력 안할시 토스트를 띄우고 저장하지 않음.
                if(sName.isEmpty() || sFoodName.isEmpty() || sTaste.isEmpty()){
                    Toast.makeText(getApplicationContext(),"빠짐없이 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                mDB = mDBHelper.getReadableDatabase(); //DBHelper 객체를 얻어옴.
                //static final String SQL_SELECT_ID = "SELECT ID FROM "  + TABLE_NAME + " WHERE " + COL_NAME + "=?";
                Cursor cursor = mDB.rawQuery(DBContract.SQL_SELECT_ID, new String[] {sName}); //커서 객체 생성
                if(cursor.getCount() != 0) { //중복된 식당 이름이 있을 수 있는 경우
                    cursor.moveToNext(); //첫번째 레코드를 가리킴.
                    if(mItem == -1){ //추가하는 경우
                        Toast.makeText(getApplicationContext(),"중복된 항목이 있습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    } else if(mISelectedID != cursor.getInt(0)) {//수정하는 경우
                        Toast.makeText(getApplicationContext(), "중복된 항목이 있습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                Intent intent = new Intent(); //인텐트 생성
                //입력한 값을 넘겨줌.
                intent.putExtra("item",mItem);
                intent.putExtra("name",sName);
                intent.putExtra("foodName", sFoodName);
                intent.putExtra("taste", sTaste);
                setResult(RESULT_OK, intent); //제대로 반환하는 것인지 확인하고 넘김.
                finish(); //메인액티비티로 넘어감.
            }
        });
    }
}