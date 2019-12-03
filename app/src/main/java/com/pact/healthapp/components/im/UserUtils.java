package com.pact.healthapp.components.im;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.pact.healthapp.R;
import com.pact.healthapp.components.im.applib.User;
import com.pact.healthapp.http.ServiceEngin;
import com.pact.healthapp.universal.MyApplication;

public class UserUtils {
    /**
     * 根据username获取相应user，由于demo没有真实的用户数据，这里给的模拟的数据；
     *
     * @param username
     * @return
     */
    public static User getUserInfo(Context context, String username) {
        User user;
        if (MyApplication.getInstance().getContactList() == null) {
            user = new User(username);
        } else {
            user = MyApplication.getInstance().getContactList().get(username);
        }

        if (user == null) {
            user = new User(username);
        }

        if (user != null) {
            // demo没有这些数据，临时填充
            JSONArray ja = new JSONArray();
            ja.add(username);
            JSONObject jo = new JSONObject();
            jo.put("ids", ja);
            JSONObject result = JSON.parseObject(ServiceEngin.Request(context,
                    "000", "getHeadImgAndNickName", jo.toJSONString()));
            if (result != null && result.getJSONArray("customerinfos") != null) {
                JSONArray users = result.getJSONArray("customerinfos");
                JSONObject userjo = users.getJSONObject(0);
                user.setNick(userjo.getString("nickName"));
                user.setAvatar(userjo.getString("headImg"));
            }
        }
        return user;
    }

    /**
     * 设置用户头像
     *
     * @param username
     */
    public static void setUserAvatar(Context context, String username,
                                     ImageView imageView) {
        User user = getUserInfo(context, username);
        BitmapUtils bitmapUtils = new BitmapUtils(context);
        BitmapDisplayConfig bitmapDisplayConfig = new BitmapDisplayConfig();
        bitmapDisplayConfig.setLoadFailedDrawable(context.getResources()
                .getDrawable(R.mipmap.set_headimg_default));
        bitmapDisplayConfig.setLoadingDrawable(context.getResources()
                .getDrawable(R.mipmap.set_headimg_default));
        bitmapUtils.configDefaultDisplayConfig(bitmapDisplayConfig);
        if (user != null && user.getAvatar() != null) {
            bitmapUtils.display(imageView, user.getAvatar());
        } else {
            bitmapUtils.display(imageView, "");
        }
    }

}
