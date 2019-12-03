/**
 *
 */
package com.pact.healthapp.components.set;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.pact.healthapp.R;
import com.pact.healthapp.components.checkupaccount.CheckupAccountActivity;
import com.pact.healthapp.components.favorite.MyFavoriteActivity;
import com.pact.healthapp.components.login.LoginActivity;
import com.pact.healthapp.components.serviceorder.ServiceOrderActivity;
import com.pact.healthapp.data.Constants;
import com.pact.healthapp.http.ServiceEngin;
import com.pact.healthapp.universal.BaseFragment;
import com.pact.healthapp.universal.WebviewActivity;
import com.pact.healthapp.utils.SharedPreferenceUtils;
import com.pact.healthapp.view.CommonDialog;
import com.pact.healthapp.view.CommonToast;
import com.pact.healthapp.view.RoundImageView;

/**
 * @author zhangyl
 */
public class SetFragment extends BaseFragment implements OnClickListener {
    private SharedPreferenceUtils sp = new SharedPreferenceUtils();
    private View view;
    private LinearLayout set_myfavorite, set_aboutus, set_changepwd,
            set_logout, set_mycheckupaccount, set_myserviceorder;
    private RelativeLayout setuserinfo;

    private RoundImageView userinfo_img;
    private TextView userinfo_nickname, userinfo_phonenumber;
    private CommonDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        view = inflater.inflate(R.layout.set_layout, null);
        ((SetActivity) getActivity()).setTitle("我的新华");
        ((SetActivity) getActivity()).setBackType(2);// fragment一层一层返回
        initView();
        loadData();
        return view;
    }

    private void initView() {
        // 个人信息设置
        setuserinfo = (RelativeLayout) view.findViewById(R.id.set_userinfo);
        setuserinfo.setOnClickListener(this);
        userinfo_img = (RoundImageView) view
                .findViewById(R.id.set_userinfo_img);
        userinfo_nickname = (TextView) view
                .findViewById(R.id.set_userinfo_nickname);
        userinfo_phonenumber = (TextView) view
                .findViewById(R.id.set_userinfo_phonenumber);

        // 我的收藏
        set_myfavorite = (LinearLayout) view.findViewById(R.id.set_myfavorite);
        set_myfavorite.setOnClickListener(this);

        // 关于我们
        set_aboutus = (LinearLayout) view.findViewById(R.id.set_aboutus);
        set_aboutus.setOnClickListener(this);

        //我的体检账户
        set_mycheckupaccount = (LinearLayout) view.findViewById(R.id.set_mycheckupaccount);
        set_mycheckupaccount.setOnClickListener(this);

        //我的服务单
        set_myserviceorder = (LinearLayout) view.findViewById(R.id.set_myserviceorder);
        set_myserviceorder.setOnClickListener(this);

        // 修改密码
        set_changepwd = (LinearLayout) view.findViewById(R.id.set_changepwd);
        set_changepwd.setOnClickListener(this);

        // 注销
        set_logout = (LinearLayout) view.findViewById(R.id.set_logout);
        set_logout.setOnClickListener(this);
        if (!sp.getLoginState(context)) {
            ((TextView) set_logout.findViewById(R.id.set_logout_tv))
                    .setText(R.string.login_str);
        } else {
            ((TextView) set_logout.findViewById(R.id.set_logout_tv))
                    .setText(R.string.logout_str);
        }
    }

    private void loadData() {
        // TODO Auto-generated method stub
        BitmapUtils bitmapUtils = new BitmapUtils(context);
        BitmapDisplayConfig bitmapDisplayConfig = new BitmapDisplayConfig();
        bitmapDisplayConfig.setLoadFailedDrawable(context.getResources()
                .getDrawable(R.mipmap.set_headimg_default));
        bitmapDisplayConfig.setLoadingDrawable(context.getResources()
                .getDrawable(R.mipmap.set_headimg_default));
        bitmapUtils.configDefaultDisplayConfig(bitmapDisplayConfig);
        bitmapUtils.display(userinfo_img, sp.getLoginInfo(context, "headImg"));
        userinfo_nickname.setText(sp.getLoginInfo(context, "nickName"));
        userinfo_phonenumber.setText("手机号："
                + sp.getLoginInfo(context, "phoneNumber"));
        if (sp.getLoginInfo(context, "phoneNumber").equals("")) {
            userinfo_phonenumber.setText("");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.set_userinfo: // 个人信息设置
                if (!sp.getLoginState(context)) {
                    Intent intent = new Intent(context, LoginActivity.class);
                    startActivityForResult(intent, ServiceEngin.REQUEST_LOGIN);
                    return;
                }
                ((SetActivity) getActivity()).setContenierView(1);// 隐藏Tab，显示Title
                ((SetActivity) getActivity()).setBackType(0);// fragment一层一层返回
                /**
                 * ft事务是全局的变量，只能commit一次。 在这里用局部ft事务去做commit。
                 */
                mFragmentTransaction = getActivity().getSupportFragmentManager()
                        .beginTransaction();
                mFragmentTransaction
                        .replace(R.id.login_fl_continer, new SetUserInfoFragment())
                        .addToBackStack("SetFragment").commit();
                break;

            case R.id.set_myfavorite: // 我的收藏
                startActivity(new Intent(context, MyFavoriteActivity.class));
                break;

            case R.id.set_mycheckupaccount://我的体检账户
                startActivity(new Intent(context, CheckupAccountActivity.class));
                break;

            case R.id.set_myserviceorder://我的服务单
                startActivity(new Intent(context, ServiceOrderActivity.class));
                break;

            case R.id.set_aboutus: // 关于我们
                Intent aboutus = new Intent(context, WebviewActivity.class);
                aboutus.putExtra("url", Constants.SET_ABOUTUS_URL);
                aboutus.putExtra("title", "关于我们");
                startActivity(aboutus);
                break;

            case R.id.set_changepwd: // 修改密码
                if (!sp.getLoginState(context)) {
                    Intent intent = new Intent(context, LoginActivity.class);
                    startActivityForResult(intent, 98);
                    return;
                }
                if (sp.getLoginState(context)
                        && sp.getLoginInfo(context, "otherLogin").equals("true")) {
                    CommonToast.makeText(context, "第三方账号登录，不需要修改密码");
                    return;
                }
                ((SetActivity) getActivity()).setContenierView(1);// 隐藏Tab，显示Title
                ((SetActivity) getActivity()).setBackType(0);// fragment一层一层返回
                mFragmentTransaction = getActivity().getSupportFragmentManager()
                        .beginTransaction();
                mFragmentTransaction
                        .replace(R.id.login_fl_continer, new ChangepwdFragment())
                        .addToBackStack("ChangepwdFragment").commit();
                break;
            case R.id.set_logout: // 注销
                if (!sp.getLoginState(context)) {
                    startActivity(new Intent(context, LoginActivity.class));
                } else {
                    dialog = new CommonDialog(context, 2, "注销", "否",
                            new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    SharedPreferenceUtils sp = new SharedPreferenceUtils();
                                    String phoneNumber = sp.getLoginInfo(
                                            getActivity(), "account");
                                    Intent intent = new Intent(context,
                                            LoginActivity.class);
                                    intent.putExtra("phoneNumber", phoneNumber);
                                    sp.clearUserinfo(context);
                                    // LoginUser.getInstance().clearLoginState();
                                    dialog.dismiss();
                                    startActivity(intent);
                                }
                            }, null, null, "注销当前登录?");
                    dialog.show();
                }
                break;
        }
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        ((SetActivity) getActivity()).btn_left.setVisibility(View.INVISIBLE);
        ((SetActivity) getActivity()).home_tabs.setVisibility(View.VISIBLE);
        ((SetActivity) getActivity()).home_tab5_cb.setSelected(true);
        initView();
        loadData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ServiceEngin.REQUEST_LOGIN) {// 个人设置
                ((SetActivity) getActivity()).setContenierView(1);
                ((SetActivity) getActivity()).setBackType(0);
                mFragmentTransaction = getActivity()
                        .getSupportFragmentManager().beginTransaction();
                mFragmentTransaction
                        .replace(R.id.login_fl_continer,
                                new SetUserInfoFragment())
                        .addToBackStack("SetUserInfoFragment").commit();
            } else if (requestCode == 98) {// 修改密码
                ((SetActivity) getActivity()).setContenierView(1);
                ((SetActivity) getActivity()).setBackType(0);
                mFragmentTransaction = getActivity()
                        .getSupportFragmentManager().beginTransaction();
                mFragmentTransaction
                        .replace(R.id.login_fl_continer,
                                new ChangepwdFragment())
                        .addToBackStack("ChangepwdFragment").commit();
            }
        }

    }

}
