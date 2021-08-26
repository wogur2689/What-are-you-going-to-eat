package ac.dongyang.smartapp.myapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MemoActivity extends AppCompatActivity {
    //멤버변수 선언
    private ListView mListView; //리스트뷰
    private SimpleAdapter mSAdapter; //심플어댑터
    private ArrayList<HashMap<String,String>> mListData; //리스트뷰를 저장할 Array객체
    private int mISelectedItem = -1; //현재 선택된 항목의 인덱스 값 저장 (-1은 항목x)

    private DBHelper mDBHelper; //DBHelper 객체 생성
    private SQLiteDatabase mDB; //SQLite 객체 생성
    private int mISelectedID = 0; // 현재 선택된 항목의 아이디
    private int mID = 0; //현재 SQLite에 저장된 아이디값중 가장 큰값

    public void onClickAdd(View v){ //추가
        Intent intent = new Intent(getApplicationContext(), MemoEditActivity.class); //MemoEditActivity.class로 이동.
        intent.putExtra("item", -1); // mISelectedItem
        intent.putExtra("id", 0); //mISelectedID
        startActivityForResult(intent, 200); //이동
    }

    public void onClickEdit(View v){ //편집
        if(mISelectedItem == -1){ //사용자가 항목을 선택 안했을시 토스트를 띄우고 반환.
            Toast.makeText(getApplicationContext(),"항목을 선택해 주세요.",Toast.LENGTH_SHORT).show();
            return;
        }

        //name, foodName, taste
        HashMap<String,String> item =  ((HashMap<String,String>)mSAdapter.getItem(mISelectedItem)); //HashMap객체를 생성
        Intent intent = new Intent(MemoActivity.this, MemoEditActivity.class); //현위치에서 MemoEditActivity.class로 이동을 명시
        //HashMap에서 선택한 항목의 값을 얻어와서,이동할때 같이 넘기기 위해 저장
        intent.putExtra("name", item.get("name"));
        intent.putExtra("foodName", item.get("foodName"));
        intent.putExtra("taste", item.get("taste"));
        intent.putExtra("item", mISelectedItem);
        intent.putExtra("id", mISelectedID);
        startActivityForResult(intent, 200);
        //MemoActivity.java로 이동.
    }

    public void onClickDel(View v) {
        if(mISelectedItem == -1) {
            Toast.makeText(getApplicationContext(), "항목을 선택해 주세요.", Toast.LENGTH_SHORT).show();
            return; //선택한 항목이 없을시 토스트를 띄우고 반환
        }
        mDB.delete(DBContract.TABLE_NAME, String.valueOf(mISelectedItem), null); //DB에서 삭제
        mListData.remove(mISelectedItem); //리스트데이터에서 선택한 항목을 삭제
        mSAdapter.notifyDataSetChanged(); //리스트 목록 변경.
        mISelectedItem = -1; //선택한 항목을 저장하는 변수 초기화
        mISelectedID = 0; // 현재 선택된 항목의 아이디 초기화
        mListView.clearChoices(); //리스트뷰에 선택된 것을 초기화
    }

    private void loadTable() {
        mDB = mDBHelper.getReadableDatabase(); //읽어오기
        mListData.clear(); //항목들의 정보를 초기화

        Cursor cursor = mDB.rawQuery(DBContract.SQL_LOAD, null); //커서 객체 생성
        while(cursor.moveToNext()) { //모든 레코드를 하나씩 가지고 오기 위한 반복문
            HashMap<String, String> hitem = new HashMap<>(); //HashMap객체 생성
            int nID = cursor.getInt(0); //ID값 얻어옴.
            mID = Math.max(mID, nID); //ID값의 최고값을 변수에 저장함.
            //HashMap 객체에 String형식으로 변환하여 저장.
            hitem.put("id", String.valueOf(nID));
            hitem.put("name",cursor.getString(1));
            hitem.put("foodName", cursor.getString(2));
            hitem.put("taste", cursor.getString(3));
            mListData.add(hitem); //항목에 레코드를 추가.
        }

        mSAdapter.notifyDataSetChanged(); //리스트 목록 변경.
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        Intent intent = getIntent(); //메인액티비티에서 넘긴 값을 얻어옴.
        String memo = intent.getStringExtra("memo"); //얻은 값을 메모라는 변수에 저장
        Toast.makeText(getApplicationContext(), memo, Toast.LENGTH_SHORT).show(); //메모변수에 저장된 값을 토스트로 출력

        // name, foodname, taste
        mListData = new ArrayList<>(); //ArrayList 객체 생성
        mSAdapter = new SimpleAdapter(this, mListData, R.layout.simple_list_item_activated_3,
                new String[] {"name", "foodName","taste"}, new int[] {R.id.text1, R.id.text2, R.id.text3});
        //SimpleAdapter 객체 생성
        mListView = findViewById(R.id.listView); //id를 찾아서 얻어와 리스트뷰객체 생성
        mListView.setAdapter(mSAdapter); //리스트뷰에서 어댑터를 보여줌.

        mDBHelper = new DBHelper(getApplicationContext()); //mDBHelper 객체 생성
        loadTable(); //앱 실행 이전에 저장된 항목 반환

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mISelectedItem = i; //클릭할 항목의 인덱스를 저장
                HashMap<String,String> item = ((HashMap<String,String>)mSAdapter.getItem(i)); //object, 선택한 항목의 객체를 가지고옴.
                Toast.makeText(getApplicationContext(),item.get("name"), Toast.LENGTH_SHORT).show(); //식당의 이름을 토스트로 표시
                mISelectedID = Integer.parseInt(item.get("id")); //항목의 아이디를 가져와 정수로 변환한 후 저장.
            }
        });

        Button back = findViewById(R.id.buttonback); //버튼 객체를 생성하고 id값을 찾아서 저장
        back.setOnClickListener(new View.OnClickListener() { //클릭 이벤트 처리기
            @Override
            public void onClick(View view) {
               Intent it = new Intent(getApplicationContext(), MainActivity.class); //메인액티비티로 이동.
                startActivity(it);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) { //resultcode가 제대로된 데이터를 받았는지 확인.
            int item = data.getIntExtra("item", -1);  //넘겨준 값의 인덱스 값을 받음.

            HashMap<String,String> hitem = new HashMap<>();  //HashMap객체 생성.
            //하나하나 데이터를 받아 저장.
            hitem.put("name", data.getStringExtra("name"));  //식당 이름
            hitem.put("foodName", data.getStringExtra("foodName")); //음식 이름
            hitem.put("taste", data.getStringExtra("taste")); //맛 평가

            ContentValues values = new ContentValues(); //각큘럼의 내용들을 저장하는 객체 생성
            values.put(DBContract.COL_NAME, data.getStringExtra("name")); //식당 이름 저장
            values.put(DBContract.COL_FNAME, data.getStringExtra("foodName")); //음식 이름 저장
            values.put(DBContract.COL_TASTE, data.getStringExtra("taste")); //맛 평가 저장

            mDB = mDBHelper.getWritableDatabase(); //DBHelper 객체 생성
            if(item == -1) {//추가
                values.put(DBContract.COL_ID, ++mID); //ID값 추가, 생성할때마다 1씩 증가
                hitem.put("id", String.valueOf(mID)); //ID값을 스트링으로 바꿔서 HashMap에도 추가
                mDB.insert(DBContract.TABLE_NAME, null, values); //DBHelper에도 추가
                mListData.add(hitem); //추가
            }
            else {//수정
                hitem.put("id", String.valueOf(mISelectedID)); //ID값을 스트링으로 바꿔서 HashMap에도 추가
                mDB.update(DBContract.TABLE_NAME, values, "id=" + mISelectedID, null); //DBHelper에 있는 값을 수정
                mListData.set(item, hitem); //변경
            }

            mSAdapter.notifyDataSetChanged(); //리스트 목록 변경.
        }else //취소
            Toast.makeText(this,"취소되었습니다.",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() { //DB접속 종료.
        super.onDestroy();
        if(mDBHelper != null)
            mDBHelper.close();
    }
}