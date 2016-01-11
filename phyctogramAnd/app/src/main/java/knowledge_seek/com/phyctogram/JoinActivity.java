package knowledge_seek.com.phyctogram;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import java.io.IOException;

import knowledge_seek.com.phyctogram.domain.Member;
import knowledge_seek.com.phyctogram.phyctogram.SaveSharedPreference;
import knowledge_seek.com.phyctogram.retrofitapi.MemberAPI;
import knowledge_seek.com.phyctogram.retrofitapi.ServiceGenerator;
import knowledge_seek.com.phyctogram.util.Utility;
import retrofit.Call;

/**
 * Created by dkfka on 2015-11-26.
 */
public class JoinActivity extends Activity {

    //데이터
    private Member memberActivity;
    private Member member;

    //레이아웃정의
    private EditText et_name;
    private EditText et_email;
    private EditText et_pw;
    private EditText et_pw1;
    private Button btn_join_member;
    CheckBox agreement1, agreement2;
    private ScrollView sv_layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        member = new Member();
        et_name = (EditText)findViewById(R.id.et_name);
        et_email = (EditText)findViewById(R.id.et_email);
        et_pw = (EditText)findViewById(R.id.et_pw);
        et_pw1 = (EditText)findViewById(R.id.et_pw1);
        btn_join_member = (Button)findViewById(R.id.btn_join_member);
        sv_layout = (ScrollView)findViewById(R.id.sv_layout);
        sv_layout.setVerticalScrollBarEnabled(false);
        sv_layout.setHorizontalScrollBarEnabled(false);

        //가입
        btn_join_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                member.setName(et_name.getText().toString());
                member.setEmail(et_email.getText().toString());
                member.setPassword(et_pw.getText().toString());
                //멤버 내용 체크
                if(!checkMember(member)){
                    return ;
                }

                //패스워드체크
                //Log.d("-진우-", "#" + et_pw.getText().toString() + "#, #" + et_pw1.getText().toString() + "#");
                //Log.d("-진우-", "#" + et_pw.getText().toString().length() + "#, #" + et_pw1.getText().toString().length() + "#");
                if(!checkpw(et_pw.getText().toString(), et_pw1.getText().toString())){
                   return ;
                }

                //약관 동의체크
                if(!(agreement1.isChecked() && agreement2.isChecked())){
                    Toast.makeText(getApplicationContext(), "이용약관 및 개인정보취급방침에 동의해주십시요.", Toast.LENGTH_SHORT).show();
                    return ;
                }

                //멤버 가입
                member.setJoin_route("phyctogram");
                Log.d("-진우-", member.toString());
                Log.d("-진우-", Utility.member2json(member));
                registerMember(member);

                //redirectMainActivity(memberActivity);
            }
        });


        //이용약관 동의 및 개인정보취급방침 동의
        agreement1 = (CheckBox) findViewById(R.id.agreement1);
        agreement2 = (CheckBox) findViewById(R.id.agreement2);
        agreement1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Context mContext = getApplicationContext();
                    LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);

                    View layout;
                    layout = inflater.inflate(R.layout.popup_agree, (ViewGroup) findViewById(R.id.agreeText));
                    AlertDialog.Builder aDialog = new AlertDialog.Builder(JoinActivity.this);

                    aDialog.setTitle("이용약관");
                    aDialog.setView(layout);

                    aDialog.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

                    AlertDialog ad = aDialog.create();
                    ad.show();
                }
            }
        });
        agreement2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Context mContext2 = getApplicationContext();
                    LayoutInflater inflater2 = (LayoutInflater) mContext2.getSystemService(LAYOUT_INFLATER_SERVICE);

                    View layout;
                    layout = inflater2.inflate(R.layout.popup_agree2, (ViewGroup) findViewById(R.id.agreeText));
                    AlertDialog.Builder aDialog2 = new AlertDialog.Builder(JoinActivity.this);

                    aDialog2.setTitle("개인정보취급방침");
                    aDialog2.setView(layout);

                    aDialog2.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

                    AlertDialog ad2 = aDialog2.create();
                    ad2.show();
                }
            }
        });


    }

    //패스워드 체크
    private boolean checkpw(String word1, String word2){
        if(word1.length() <= 0 || word2.length() <= 0 || !word1.equals(word2)){
            Toast.makeText(getApplicationContext(), "비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    //멤버 내용 체크
    private boolean checkMember(Member member){
        if(member.getName().length() <= 0 || member.getEmail().length() <= 0 || member.getPassword().length() <= 0){
            Toast.makeText(getApplicationContext(), "내용을 확인해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    //멤버저장
    private void registerMember(Member member){

        RegisterMemberTask task = new RegisterMemberTask();
        task.execute(member);

    }

    private void redirectMainActivity(Member member) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("member", member);
        startActivity(intent);
        finish();
    }

    //멤버 읽어오기
    private class RegisterMemberTask extends AsyncTask<Object, Void, Member> {

        private ProgressDialog dialog = new ProgressDialog(JoinActivity.this);
        private Member memberTask;

        @Override
        protected void onPreExecute() {
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("잠시만 기다려주세요");
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected Member doInBackground(Object... params) {
            Member member = null;
            memberTask = (Member)params[0];
            Log.d("-진우-", "멤버 읽어오기 : " + memberTask.toString());
            MemberAPI service = ServiceGenerator.createService(MemberAPI.class, "Member");
            Call<Member> call = service.registerMember(memberTask);
            try {
                member = call.execute().body();
            } catch (IOException e){
                e.printStackTrace();
            }
            return member;
        }

        @Override
        protected void onPostExecute(Member member) {
            if(member != null) {
                Log.d("-진우-", "픽토그램 가입 성공 결과1 : " + member.toString());
                Toast.makeText(getApplicationContext(), "픽토그램 회원이 되셨습니다", Toast.LENGTH_SHORT).show();
                memberActivity = member;
                //가입완료후 로그인유지를 위해 preference를 사용한다.
                SaveSharedPreference.setMemberSeq(getApplicationContext(), String.valueOf(memberActivity.getMember_seq()));
                redirectMainActivity(memberActivity);
            } else {
                Toast.makeText(getApplicationContext(), "이미 가입된 이메일입니다.", Toast.LENGTH_SHORT).show();
            }

            dialog.dismiss();
            super.onPostExecute(member);
        }
    }

}
