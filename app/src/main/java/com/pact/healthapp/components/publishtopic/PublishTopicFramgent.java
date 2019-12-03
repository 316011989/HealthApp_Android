/**
 *
 */
package com.pact.healthapp.components.publishtopic;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pact.healthapp.R;
import com.pact.healthapp.http.Des3;
import com.pact.healthapp.http.EnginCallback;
import com.pact.healthapp.http.ServiceEngin;
import com.pact.healthapp.http.UploadCallback;
import com.pact.healthapp.http.UploadFileEngin;
import com.pact.healthapp.universal.BaseFragment;
import com.pact.healthapp.utils.CommonUtil;
import com.pact.healthapp.utils.SharedPreferenceUtils;
import com.pact.healthapp.view.CommonToast;
import com.pact.healthapp.view.imagepick.adapter.AddPictrueGVAdapter;
import com.pact.healthapp.view.imagepick.ui.PhotoWallActivity;
import com.pact.healthapp.view.imageshow.ImageShowActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 发布帖子功能
 *
 * @author zhangyl
 */
@SuppressLint("ValidFragment")
public class PublishTopicFramgent extends BaseFragment implements
        OnClickListener {
    private SharedPreferenceUtils sp = new SharedPreferenceUtils();
    private View view;
    private TextView btn_left;//
    private Button btn_right;
    @ViewInject(R.id.publishtopic_edit_title)
    private EditText publishtopic_edit_title;// 帖子标题输入框
    @ViewInject(R.id.publishtopic_edit_content)
    private EditText publishtopic_edit_content;// 帖子内容输入框
    @ViewInject(R.id.publishtopic_content_num)
    private TextView publishtopic_content_num;// 帖子字数
    @ViewInject(R.id.publishtopic_gridview)
    private GridView publishtopic_gridview;

    private PopupWindow pop_cancelpublish;
    private View cancelpublishView; // pop_cancelpublish窗口
    private Button publishtopic_popup_save;// pop中选择保存
    private Button publishtopic_popup_delete;// pop中选择不保存
    private Button publishtopic_popup_cancel;// pop中取消

    private PopupWindow pop_takephoto;
    private final int PIC_FROM_CAMERA = 1;// 选择拍照
    private final int PIC_FROM＿LOCALPHOTO = 0;// 选择本地相册
    private final int SAVE_IMAGE = 2;// 裁剪后保存图片
    private View takephotoView; // pop_takephoto
    private Button publishtopic_popup_takephpto;// pop中选择拍照
    private Button publishtopic_popup_choicepic;// pop中选择相册
    private Button publishtopic_popup_canceltake;// pop中取消
    private Uri photoUri;// 图片uri
    private ArrayList<String> picsname = new ArrayList<String>();// 上传的图片名称
    private ArrayList<String> picsname_upload = new ArrayList<String>();// 发布帖子时的图片名称(此名称为上传成功后服务器返回的url)
    private String topicTitle;// 帖子标题
    private String topicContent;// 帖子内容
    private String photoname;// 当前图片名称

    private String boardId;// 版块id
    private ArrayList<String> imagePathList = new ArrayList<String>();// 帖子所有图片本地路径
    private AddPictrueGVAdapter adapter;// 帖子图片适配器

    private SharedPreferences draft_sp;
    private final String TOPIC_DRAFT = "draft";

    private int picCounts = 0;// 上传图片成功数量

    public PublishTopicFramgent() {
    }

    public PublishTopicFramgent(String boardId) {
        this.boardId = boardId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.publishtopic_layout, null);
        ViewUtils.inject(this, view);
        isHaveDraft();
        getActivity().setTitle("发布话题");
        btn_left = ((PublishTopicActivity) getActivity()).leftView;
        btn_right = ((PublishTopicActivity) getActivity()).btn_right;
        btn_left.setVisibility(View.VISIBLE);
        btn_left.setText("取消");
        btn_right.setText("发布");
        btn_right.setBackgroundColor(getResources().getColor(
                android.R.color.transparent));
        btn_right.setOnClickListener(this);
        btn_right.setBackgroundColor(getResources().getColor(
                android.R.color.transparent));
        if (draft_sp.getBoolean("IsHaveDraft", false)) {
            btn_right.setTextColor(getResources().getColor(
                    android.R.color.white));
        } else {
            btn_right.setTextColor(getResources().getColor(
                    android.R.color.darker_gray));
        }
        btn_left.setOnClickListener(this);
        btn_right.setClickable(false);
        publishtopic_edit_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (publishtopic_edit_content.getText().toString().length() > 0
                        && publishtopic_edit_title.getText().toString()
                        .length() > 0) {
                    btn_right.setTextColor(getResources().getColor(
                            android.R.color.white));
                    btn_right.setClickable(true);
                } else {
                    btn_right.setTextColor(getResources().getColor(
                            android.R.color.darker_gray));
                    btn_right.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (publishtopic_edit_content.getText().toString().length() > 0
                        && publishtopic_edit_title.getText().toString()
                        .length() > 0) {
                    btn_right.setTextColor(getResources().getColor(
                            android.R.color.white));
                    btn_right.setClickable(true);
                } else {
                    btn_right.setTextColor(getResources().getColor(
                            android.R.color.darker_gray));
                    btn_right.setClickable(false);
                }
            }

        });
        publishtopic_edit_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                publishtopic_content_num.setText(s.length() + "/5000");
                if (publishtopic_edit_content.getText().toString().length() > 0
                        && publishtopic_edit_title.getText().toString()
                        .length() > 0) {
                    btn_right.setTextColor(getResources().getColor(
                            android.R.color.white));
                    btn_right.setClickable(true);
                } else {
                    btn_right.setTextColor(getResources().getColor(
                            android.R.color.darker_gray));
                    btn_right.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (publishtopic_edit_content.getText().toString().length() > 0
                        && publishtopic_edit_title.getText().toString()
                        .length() > 0) {
                    btn_right.setTextColor(getResources().getColor(
                            android.R.color.white));
                    btn_right.setClickable(true);
                } else {
                    btn_right.setTextColor(getResources().getColor(
                            android.R.color.darker_gray));
                    btn_right.setClickable(false);
                }
            }

        });
        adapter = new AddPictrueGVAdapter(context, imagePathList);
        publishtopic_gridview.setAdapter(adapter);
        publishtopic_gridview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == parent.getCount() - 1 && imagePathList != null
                        && imagePathList.size() < 5) {
                    ((InputMethodManager) context
                            .getSystemService(Activity.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(getActivity()
                                            .getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    pop_takephoto.showAtLocation(view, Gravity.BOTTOM
                            | Gravity.CENTER_HORIZONTAL, 0, 0);
                } else {
                    Intent intent = new Intent(context, ImageShowActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("PATHS", imagePathList);
                    bundle.putInt("POSITION", position);
                    bundle.putInt("TYPE", 1);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

            }
        });

        initpop_cancelpublish();// 创建pop_cancelpublish
        initpop_takephoto();// 创建takephoto
        return view;
    }

    /**
     * 判断是否存在草稿,如果存在,显示草稿内容
     *
     * @author zhangyl
     */
    private void isHaveDraft() {
        draft_sp = context.getSharedPreferences(TOPIC_DRAFT,
                Activity.MODE_PRIVATE);
        if (draft_sp.getBoolean("IsHaveDraft", false)) {
            if (!draft_sp.getString("draftTitle", "").equals("")) {
                publishtopic_edit_title.setText(draft_sp.getString(
                        "draftTitle", ""));
            }
            if (!draft_sp.getString("draftContent", "").equals("")) {
                publishtopic_edit_content.setText(draft_sp.getString(
                        "draftContent", ""));
            }
            if (!draft_sp.getString("draftPics", "").equals("")) {// 草稿中有图片路径
                imagePathList = (ArrayList<String>) JSON.parseArray(
                        draft_sp.getString("draftPics", ""), String.class);
            }
            if (!draft_sp.getString("draftPicNames", "").equals("")) {
                picsname = (ArrayList<String>) JSON.parseArray(
                        draft_sp.getString("draftPicNames", ""), String.class);
            }
            // 清空草稿
            draft_sp.edit().putString("draftTitle", "")
                    .putString("draftContent", "").putString("draftPics", "")
                    .putString("draftPicNames", "").commit();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_btn_right:
                if (imagePathList != null && imagePathList.size() > 0) {
                    uploadPics();// 上传图片,成功后发布帖子相关信息
                } else {
                    forumPostTopic();
                }
                break;
            case R.id.home_btn_left_view:// 取消发布
                topicTitle = publishtopic_edit_title.getText().toString();
                topicContent = publishtopic_edit_content.getText().toString();
                if (imagePathList.size() > 0 || !topicTitle.equals("")
                        || !topicContent.equals("")) {// 有内容需要提问是否保存
                    ((InputMethodManager) context
                            .getSystemService(Activity.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(getActivity()
                                            .getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    pop_cancelpublish.showAtLocation(view, Gravity.BOTTOM
                            | Gravity.CENTER_HORIZONTAL, 0, 0);
                } else {// 没有内容要保存
                    pop_cancelpublish.dismiss();
                    getActivity().finish();
                }
                break;
            case R.id.publishtopic_popup_save:// pop中点击保存
                pop_cancelpublish.dismiss();
                // CommonToast.makeText(context, "保存草稿");
                saveDraft();
                getActivity().finish();
                break;
            case R.id.publishtopic_popup_delete:// pop中不保存
                pop_cancelpublish.dismiss();
                // CommonToast.makeText(context, "不保存草稿");
                getActivity().finish();
                break;
            case R.id.publishtopic_popup_cancel:// pop中取消
                pop_cancelpublish.dismiss();
                break;
            case R.id.publishtopic_popup_takephpto:// pop中点击相机
                doHandlerPhoto();
                break;
            case R.id.publishtopic_popup_choicepic:// pop中从相册选取
                Intent intent = new Intent(context, PhotoWallActivity.class);
                startActivityForResult(intent, PIC_FROM＿LOCALPHOTO);
                pop_takephoto.dismiss();
                break;
            case R.id.publishtopic_popup_canceltake:// pop中取消
                pop_takephoto.dismiss();
                break;
        }
    }

    /**
     * 保存草稿
     *
     * @author zhangyl
     */
    private void saveDraft() {
        draft_sp.edit()
                .putBoolean("IsHaveDraft", true)
                .putString("draftTitle",
                        publishtopic_edit_title.getText().toString())
                .putString("draftContent",
                        publishtopic_edit_content.getText().toString())
                .putString("draftPics", JSON.toJSONString(imagePathList))
                .putString("draftPicNames", JSON.toJSONString(picsname))
                .commit();
    }

    /**
     * @author zhangyl
     */
    private void forumPostTopic() {
        topicTitle = publishtopic_edit_title.getText().toString();
        topicContent = publishtopic_edit_content.getText().toString();
        if (topicTitle.equals("")) {
            CommonToast.makeText(context, "请输入帖子标题");
            return;
        }
        if (topicTitle.length() > 30) {
            CommonToast.makeText(context, "标题最多三十字");
            return;
        }
        if (topicContent.equals("")) {
            CommonToast.makeText(context, "请输入帖子内容");
            return;
        }
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("customerId", sp.getLoginInfo(context, "customerId"));
        map.put("token", sp.getLoginInfo(context, "token"));
        map.put("boardId", boardId);
        map.put("title", topicTitle);
        map.put("content", topicContent);
        map.put("releaseTime", "" + System.currentTimeMillis());
        map.put("pics", picsname_upload);
        String servicePara = JSON.toJSONString(map);
        ServiceEngin.Request(context, "000", "forumPostTopic", servicePara,
                new EnginCallback(context) {
                    @Override
                    public void onSuccess(ResponseInfo arg0) {
                        super.onSuccess(arg0);
                        String result = Des3.decode(arg0.result.toString());
                        JSONObject obj = JSON.parseObject(result);
                        if (obj.get("resultCode") != null
                                && obj.get("resultCode").toString().equals("0")) {
                            getActivity().finish();
                        }
                    }
                });
    }

    /**
     * 上传帖子添加过的图片
     *
     * @author zhangyl
     */
    private void uploadPics() {
        // TODO Auto-generated method stub
        topicTitle = publishtopic_edit_title.getText().toString();
        topicContent = publishtopic_edit_content.getText().toString();
        if (topicTitle.equals("")) {
            CommonToast.makeText(context, "请输入帖子标题");
            return;
        }
        if (topicTitle.length() > 30) {
            CommonToast.makeText(context, "标题最多三十字");
            return;
        }
        if (topicContent.equals("")) {
            CommonToast.makeText(context, "请输入帖子内容");
            return;
        }
        for (int i = 0; i < imagePathList.size(); i++) {
            String name_item = picsname.get(i);
            String path = imagePathList.get(i);
            UploadFileEngin.uploadFile(name_item, path, context,
                    new UploadCallback(context) {
                        @Override
                        public void onSuccess(ResponseInfo arg0) {
                            super.onSuccess(arg0);
                            String headImg = arg0.result.toString();// 返回的图片名称(url)
                            picsname_upload.add(headImg);
                            picCounts++;
                            if (picCounts == imagePathList.size()) {
                                // CommonToast.makeText(context, "图片全部上传成功");
                                Log.e("图片全部上传成功", "图片全部上传成功");
                                forumPostTopic();
                                canclDialog();
                            }
                        }

                        @Override
                        public void onFailure(HttpException arg0, String arg1) {
                            // TODO Auto-generated method stub
                            // super.onFailure(arg0, arg1);
                            canclDialog();
                            CommonToast.makeText(context, "图片上传失败");
                        }
                    });
        }
    }

    /**
     * 创建pop_takephoto方法
     */
    private void initpop_cancelpublish() {
        // 加载PopupWindow的布局文件
        cancelpublishView = LayoutInflater.from(context).inflate(
                R.layout.publishtopic_popup_cancelpublish, null);
        pop_cancelpublish = new PopupWindow(cancelpublishView,
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        pop_cancelpublish.setFocusable(true);// 获取popup中控件焦点 ,设置PopupWindow可触摸
        pop_cancelpublish.setBackgroundDrawable(new ColorDrawable(0xb0000000));
        pop_cancelpublish.setOutsideTouchable(true);
        pop_cancelpublish.setAnimationStyle(R.style.popupwindow_anim);
        publishtopic_popup_save = (Button) cancelpublishView
                .findViewById(R.id.publishtopic_popup_save);
        publishtopic_popup_delete = (Button) cancelpublishView
                .findViewById(R.id.publishtopic_popup_delete);
        publishtopic_popup_cancel = (Button) cancelpublishView
                .findViewById(R.id.publishtopic_popup_cancel);
        publishtopic_popup_save.setOnClickListener(this);
        publishtopic_popup_delete.setOnClickListener(this);
        publishtopic_popup_cancel.setOnClickListener(this);
    }

    /**
     * 创建pop_takephoto方法
     */
    private void initpop_takephoto() {
        // 加载PopupWindow的布局文件
        takephotoView = LayoutInflater.from(context).inflate(
                R.layout.publishtopic_popup_takepic, null);
        pop_takephoto = new PopupWindow(takephotoView,
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        pop_takephoto.setFocusable(true);// 获取popup中控件焦点 ,设置PopupWindow可触摸
        pop_takephoto.setBackgroundDrawable(new ColorDrawable(0xb0000000));
        pop_takephoto.setOutsideTouchable(true);
        pop_takephoto.setAnimationStyle(R.style.popupwindow_anim);
        publishtopic_popup_takephpto = (Button) takephotoView
                .findViewById(R.id.publishtopic_popup_takephpto);
        publishtopic_popup_choicepic = (Button) takephotoView
                .findViewById(R.id.publishtopic_popup_choicepic);
        publishtopic_popup_canceltake = (Button) takephotoView
                .findViewById(R.id.publishtopic_popup_canceltake);
        publishtopic_popup_takephpto.setOnClickListener(this);
        publishtopic_popup_choicepic.setOnClickListener(this);
        publishtopic_popup_canceltake.setOnClickListener(this);

    }

    /*
     * 根据不同方式选择图片 ,0-本地相册选择，1为拍照
     */
    private void doHandlerPhoto() {
        try {
            // 保存裁剪后的图片文件
            File pictureFileDir = new File(
                    Environment.getExternalStorageDirectory(),
                    "/ncihealth/Image");
            if (!pictureFileDir.exists()) {
                pictureFileDir.mkdirs();
            }
            photoname = CommonUtil.getFileNameByDate() + ".jpeg";
            File picFile = new File(pictureFileDir, photoname);
            if (!picFile.exists()) {
                picFile.createNewFile();
            }
            photoUri = Uri.fromFile(picFile);
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(cameraIntent, PIC_FROM_CAMERA);
            pop_takephoto.dismiss();
        } catch (Exception e) {
            Log.i("HandlerPicError", "处理图片出现错误");
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PIC_FROM_CAMERA: // 拍照
                    try {
                        if (photoUri != null) {
                            String path = CommonUtil.getRealFilePath(context,
                                    photoUri);
                            if (photoname != null && !photoname.equals("")) {
                                picsname.add(photoname);
                            }
                            imagePathList.add(path);
                            adapter.notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case PIC_FROM＿LOCALPHOTO:
                    int code = data.getIntExtra("code", -1);
                    if (code != 100) {
                        return;
                    }
                    ArrayList<String> paths = data.getStringArrayListExtra("paths");
                    // 添加，去重
                    boolean hasUpdate = false;
                    for (String path : paths) {
                        if (!imagePathList.contains(path)) {
                            // 最多9张
                            if (imagePathList.size() == 5) {
                                CommonToast.makeText(context, "最多可添加5张图片。");
                                break;
                            }
                            imagePathList.add(path);
                            photoname = CommonUtil.getFileNameByDate() + ".jpeg";
                            picsname.add(photoname);
                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    try {
                                        Thread.sleep(1);
                                    } catch (InterruptedException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                }
                            });
                            hasUpdate = true;
                        }
                    }
                    if (hasUpdate) {
                        adapter.notifyDataSetChanged();
                    }
                    break;
            }

        } else {// 将当前图片名称和base都设置为"",防止添加多次或者添加不同名称的重复图片
            photoname = "";
        }
    }

}
