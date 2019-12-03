/**
 *
 */
package com.pact.healthapp.components.publishtopic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.pact.healthapp.R;
import com.pact.healthapp.components.login.LoginActivity;
import com.pact.healthapp.data.Constants;
import com.pact.healthapp.http.ServiceEngin;
import com.pact.healthapp.universal.BaseFragmentActivity;
import com.pact.healthapp.utils.SharedPreferenceUtils;

/**
 * @author zhangyl
 */
public class PublishTopicActivity extends BaseFragmentActivity {
    private SharedPreferenceUtils sp = new SharedPreferenceUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContenierView(1);
        // btn_left.setVisibility(View.VISIBLE);
        myApplication.addActivity(this);
        home_titile1.setText(Constants.moduleList.get(2).getModuleName());
        setBackType(0);
        String boardId = getIntent().getStringExtra("boardId");
        btn_left.setVisibility(View.GONE);
        String topicId = getIntent().getStringExtra("topicId");
        String commentId = getIntent().getStringExtra("commentId");
        String nickName = getIntent().getStringExtra("nickName");
        if (commentId != null && !commentId.equals("")) {
            mFragmentManager
                    .beginTransaction()
                    .replace(
                            R.id.login_fl_continer,
                            new PublishCommentFramgent(topicId, commentId,
                                    nickName))
                    .addToBackStack("PublishCommentFramgent").commit();
        } else if (topicId != null && !topicId.equals("")) {
            mFragmentManager
                    .beginTransaction()
                    .replace(R.id.login_fl_continer,
                            new PublishCommentFramgent(topicId))
                    .addToBackStack("PublishCommentFramgent").commit();
        } else {
            mFragmentManager
                    .beginTransaction()
                    .replace(R.id.login_fl_continer,
                            new PublishTopicFramgent(boardId))
                    .addToBackStack("PublishTopicFramgent").commit();
        }

        if (!sp.getLoginState(context)) {
            Intent intent = new Intent(context, LoginActivity.class);
            startActivityForResult(intent, ServiceEngin.REQUEST_LOGIN);
        }
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        // TODO Auto-generated method stub
        super.onActivityResult(arg0, arg1, arg2);
        if (arg0 == ServiceEngin.REQUEST_LOGIN && arg1 == Activity.RESULT_CANCELED) {
            finish();
        }
    }
}
