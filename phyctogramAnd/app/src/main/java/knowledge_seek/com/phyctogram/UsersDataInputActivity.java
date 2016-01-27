package knowledge_seek.com.phyctogram;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pkmmte.view.CircularImageView;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import knowledge_seek.com.phyctogram.domain.Height;
import knowledge_seek.com.phyctogram.domain.Users;
import knowledge_seek.com.phyctogram.kakao.common.BaseActivity;
import knowledge_seek.com.phyctogram.retrofitapi.HeightAPI;
import knowledge_seek.com.phyctogram.retrofitapi.ServiceGenerator;
import knowledge_seek.com.phyctogram.retrofitapi.UsersAPI;
import knowledge_seek.com.phyctogram.util.Utility;
import retrofit.Call;

/**
 * Created by dkfka on 2015-12-10.
 */
public class UsersDataInputActivity extends BaseActivity {

    //데이터
    private Height usersHeight;

    //레이아웃정의 - 슬라이드메뉴
    private ImageButton btn_left;
    private LinearLayout ic_screen;
    private CircularImageView img_profile;      //슬라이드 내 이미지
    private TextView tv_member_name;            //슬라이드 내 이름

    private EditText et_input_height;       //키
    private Button btn_users_height;       //키 저장
    private TextView tv_users_name;     //아이 이름 출력

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //화면 페이지
        ic_screen = (LinearLayout) findViewById(R.id.ic_screen);
        LayoutInflater.from(this).inflate(R.layout.include_users_data_input, ic_screen, true);

        //데이터셋팅
        usersHeight = new Height();

        //슬라이드 내 이미지, 셋팅
        img_profile = (CircularImageView) findViewById(R.id.img_profile);
        if (memberImg != null) {
            img_profile.setImageBitmap(memberImg);
        }

        //슬라이드 내 이름, 셋팅
        tv_member_name = (TextView) findViewById(R.id.tv_member_name);
        if (memberName != null) {
            tv_member_name.setText(memberName);
        }

        //메인페이지 내 아이 이름 출력
        tv_users_name = (TextView) findViewById(R.id.tv_users_name);
        if (nowUsers != null) {
            tv_users_name.setText(nowUsers.getName());
        }

        //슬라이드 내 아이 목록(ListView)에서 아이 선택시
        lv_usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                nowUsers = (Users) usersListSlideAdapter.getItem(position);
                Log.d("-진우-", "선택한 아이 : " + nowUsers.toString());
                Toast.makeText(getApplicationContext(), "'" + nowUsers.getName() + "' 아이를 선택하였습니다", Toast.LENGTH_LONG).show();

                tv_users_name.setText(nowUsers.getName());

                //선택 아이로 인한 순서 변경
                Utility.seqChange(usersList, nowUsers.getUser_seq());
                //내 아이 목록 셋팅
                usersListSlideAdapter.setUsersList(usersList);
                usersListSlideAdapter.notifyDataSetChanged();
            }
        });

        //레이아웃 정의
        btn_left = (ImageButton) findViewById(R.id.btn_left);
        btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuLeftSlideAnimationToggle();
            }
        });


        et_input_height = (EditText)findViewById(R.id.et_input_height);

        //키 저장
        btn_users_height = (Button)findViewById(R.id.btn_users_height);
        btn_users_height.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String height_str = et_input_height.getText().toString();
                Log.d("-진우-", "입력된 키 : " + height_str);
                if(nowUsers == null){
                    Toast.makeText(getApplicationContext(), "내 아이 관리에서 아이를 등록해주세요", Toast.LENGTH_SHORT).show();
                    return ;
                }
                if(!checkHeight(height_str)){
                    return ;
                }


                double height = Double.valueOf(height_str);
                usersHeight.setUser_seq(nowUsers.getUser_seq());
                usersHeight.setHeight(height);

                Log.d("-진우-", "키 저장하기 : " + usersHeight.toString());
                Log.d("-진우-", "json : " + Utility.height2json(usersHeight));

                RegisterHeightTask task = new RegisterHeightTask(usersHeight);
                task.execute();

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("-진우-", "UsersDataInputActivity.onResume() 실행");

        //슬라이드메뉴 셋팅
        initSildeMenu();

        //슬라이드메뉴 내 아이 목록 셋팅
        usersListSlideAdapter.setUsersList(usersList);
        int height = getListViewHeight(lv_usersList);
        lv_usersList.getLayoutParams().height = height;
        usersListSlideAdapter.notifyDataSetChanged();

        //슬라이드메뉴 셋팅(내 아이목록, 계정이미지)
        //UsersDataInputTask task = new UsersDataInputTask();
        //task.execute(img_profile);

        Log.d("-진우-", "UsersDataInputActivity 에 onResume() : " + member.toString());

        Log.d("-진우-", "UsersDataInputActivity.onResume() 끝");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    //height 내용 체크
    private boolean checkHeight(String height_str){
        //Log.d("-진우-", "자릿수 : " + height_str.length());
        if(height_str.length() <= 0 || height_str.length() > 7){
            Toast.makeText(getApplicationContext(), "키를 확인해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    //키 저장하기
    private class RegisterHeightTask extends AsyncTask<Void, Void, String>{

        private Height heightTask;
        private ProgressDialog dialog = new ProgressDialog(UsersDataInputActivity.this);

        public RegisterHeightTask(Height height){
            this.heightTask = height;
        }

        @Override
        protected void onPreExecute() {
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("잠시만 기다려주세요");
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            String result = null;
            HeightAPI service = ServiceGenerator.createService(HeightAPI.class, "Height");
            Call<String> call = service.registerHeight(heightTask);
            try {
                result = call.execute().body();
            } catch (IOException e){
                Log.d("-진우-", "키 저장 실패");
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if(result != null && result.equals("success")){
                Toast.makeText(getApplicationContext(), "저장하였습니다", Toast.LENGTH_SHORT).show();
                /*Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("member", member);
                startActivity(intent);
                finish();*/
                onBackPressed();
            } else {
                Log.d("-진우-", "저장하는데 실패하였습니다");
            }

            dialog.dismiss();
            super.onPostExecute(result);
        }
    }



    //직접입력페이지 초기 데이터조회(슬라이드 내 아이 목록)
    private class UsersDataInputTask extends AsyncTask<Object, Void, Bitmap> {

        private ProgressDialog dialog = new ProgressDialog(UsersDataInputActivity.this);
        private List<Users> usersTask;
        private CircularImageView img_profileTask;

        @Override
        protected void onPreExecute() {
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("잠시만 기다려주세요");
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(Object... params) {
            Bitmap mBitmap = null;
            img_profileTask = (CircularImageView)params[0];

            //슬라이드메뉴에 있는 내 아이 목록
            /*UsersAPI service = ServiceGenerator.createService(UsersAPI.class);
            Call<List<Users>> call = service.findUsersByMember(String.valueOf(member.getMember_seq()));
            try {
                usersTask = call.execute().body();
            } catch (IOException e) {
                Log.d("-진우-", "내 아이 목록 가져오기 실패");
            }*/

            /*String image_url = null;
            if(member.getJoin_route().equals("kakao")){
                image_url = member.getKakao_thumbnailimagepath();
                //이미지 불러오기
                InputStream in = null;
                try {
                    Log.d("-진우-", "이미지 주소 : " + image_url);
                    in = new URL(image_url).openStream();
                    mBitmap = BitmapFactory.decodeStream(in);
                    in.close();
                } catch (Exception e){
                    e.printStackTrace();
                }
            } else if(member.getJoin_route().equals("facebook")){
                image_url = "http://graph.facebook.com/" + member.getFacebook_id() + "/picture?type=large";
                //이미지 불러오기
                InputStream in = null;
                try {
                    //페이스북은 jpg파일이 링크 걸린 것이 아니다.
                    //http://graph.facebook.com/userid/picture?type=large
                    Log.d("-진우-", "이미지 주소 : " + image_url);

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(image_url)
                            .build();
                    com.squareup.okhttp.Response response = client.newCall(request).execute();
                    in = response.body().byteStream();
                    mBitmap = BitmapFactory.decodeStream(in);
                    in.close();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }*/
            return mBitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            /*if(bitmap != null){
                Log.d("-진우-", "이미지읽어옴");
                img_profileTask.setImageBitmap(bitmap);
            }
            if (usersTask != null && usersTask.size() > 0) {
                Log.d("-진우-", "내 아이는 몇명? " + usersTask.size());
                for (Users u : usersTask) {
                    Log.d("-진우-", "내아이 : " + u.toString());
                }
                usersList = usersTask;

                usersListSlideAdapter.setUsersList(usersList);
                if (nowUsers == null) {
                    nowUsers = usersTask.get(0);
                }
                Log.d("-진우-", "메인 유저는 " + nowUsers.toString());
                tv_users_name.setText(nowUsers.getName());
            } else {
                Log.d("-진우-", "성공했으나 등록된 내아이가 없습니다.");
            }

            int height = getListViewHeight(lv_usersList);
            lv_usersList.getLayoutParams().height = height;
            usersListSlideAdapter.notifyDataSetChanged();*/

            dialog.dismiss();
            super.onPostExecute(bitmap);
        }
    }
}
