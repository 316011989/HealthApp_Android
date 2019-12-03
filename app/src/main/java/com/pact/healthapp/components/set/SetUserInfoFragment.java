/**
 *
 */
package com.pact.healthapp.components.set;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.pact.healthapp.R;
import com.pact.healthapp.components.login.LoginActivity;
import com.pact.healthapp.http.Des3;
import com.pact.healthapp.http.EnginCallback;
import com.pact.healthapp.http.ServiceEngin;
import com.pact.healthapp.http.UploadCallback;
import com.pact.healthapp.http.UploadFileEngin;
import com.pact.healthapp.universal.BaseFragment;
import com.pact.healthapp.utils.CommonUtil;
import com.pact.healthapp.utils.SharedPreferenceUtils;
import com.pact.healthapp.view.CommonDialog;
import com.pact.healthapp.view.CommonToast;
import com.pact.healthapp.view.DatePickerPopWindow;
import com.pact.healthapp.view.DatePickerPopWindow.OnTimeSelectListener;
import com.pact.healthapp.view.RoundImageView;

import java.io.File;
import java.util.HashMap;

/**
 * @author zhangyl
 */
public class SetUserInfoFragment extends BaseFragment implements
        OnClickListener {
    private SharedPreferenceUtils sp = new SharedPreferenceUtils();
    private CommonDialog commonDialog;
    private View view;
    private RoundImageView set_setheadimg;
    private Button set_submituserinfo;

    private String headImg, nickName, phoneNumber, email, idCard, gender,
            birth;
    boolean marriage;

    private TextView setuserinfo_borndate, setuserinfo_mobile;
    private EditText setuserinfo_nickname, setuserinfo_email,
            setuserinfo_idcardnum;
    private RadioButton set_radio_male, set_radio_female, set_radio_married,
            set_radio_unmarried;

    private PopupWindow pop_takephoto;// 选择头像的popwindow
    private final int PIC_FROM_CAMERA = 1;// 选择拍照
    private final int PIC_FROM＿LOCALPHOTO = 0;// 选择本地相册
    private final int SAVE_IMAGE = 2;// 裁剪后保存图片
    private View takephotoView; // pop_takephoto的view
    private Button publishtopic_popup_takephpto;// pop中选择拍照
    private Button publishtopic_popup_choicepic;// pop中选择相册
    private Button publishtopic_popup_canceltake;// pop中取消
    private Uri photoUri;// 图片uri
    private String photoname = "";// 当前图片名称
    private String photopath = "";

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.setuserinfo_layout, null);
        ((SetActivity) getActivity()).setTitle("个人设置");
        ((SetActivity) getActivity()).btn_right.setVisibility(View.INVISIBLE);
        initview();
        initdata();
        initpop_takephoto();// 创建takephoto
        return view;
    }

    /**
     * 初始化UI控件
     *
     * @author zhangyl
     */
    private void initview() {
        set_setheadimg = (RoundImageView) view
                .findViewById(R.id.set_setheadimg);
        set_setheadimg.setOnClickListener(this);
        setuserinfo_nickname = (EditText) view
                .findViewById(R.id.setuserinfo_nickname);
        setuserinfo_mobile = (TextView) view
                .findViewById(R.id.setuserinfo_mobile);
        setuserinfo_email = (EditText) view
                .findViewById(R.id.setuserinfo_email);
        setuserinfo_idcardnum = (EditText) view
                .findViewById(R.id.setuserinfo_idcardnum);
        set_radio_male = (RadioButton) view.findViewById(R.id.set_radio_male);
        set_radio_female = (RadioButton) view
                .findViewById(R.id.set_radio_female);
        setuserinfo_borndate = (TextView) view
                .findViewById(R.id.setuserinfo_borndate);
        setuserinfo_borndate.setOnClickListener(this);
        set_radio_married = (RadioButton) view
                .findViewById(R.id.set_radio_married);
        set_radio_unmarried = (RadioButton) view
                .findViewById(R.id.set_radio_unmarried);
        set_submituserinfo = (Button) view
                .findViewById(R.id.set_submituserinfo);
        set_submituserinfo.setOnClickListener(this);

    }

    /**
     * 加载个人设置数据
     */
    private void initdata() {
        BitmapUtils bitmapUtils = new BitmapUtils(context);
        BitmapDisplayConfig bitmapDisplayConfig = new BitmapDisplayConfig();
        bitmapDisplayConfig.setLoadFailedDrawable(context.getResources()
                .getDrawable(R.mipmap.set_headimg_default));
        bitmapDisplayConfig.setLoadingDrawable(context.getResources()
                .getDrawable(R.mipmap.set_headimg_default));
        bitmapUtils.configDefaultDisplayConfig(bitmapDisplayConfig);
        bitmapUtils
                .display(set_setheadimg, sp.getLoginInfo(context, "headImg"));
        if (sp.getLoginInfo(context, "nickName").isEmpty()) {
            final String str = setuserinfo_nickname.getHint().toString();
            setuserinfo_nickname
                    .setOnFocusChangeListener(new OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (hasFocus) {
                                setuserinfo_nickname.setHint(null);
                            } else {
                                setuserinfo_nickname.setHint(str);
                            }
                        }

                    });
        } else {
            setuserinfo_nickname.setText(sp.getLoginInfo(context, "nickName"));
        }

        setuserinfo_mobile.setText(sp.getLoginInfo(context, "phoneNumber"));
        setuserinfo_email.setText(sp.getLoginInfo(context, "email"));

        if (!sp.getLoginInfo(context, "idCard").isEmpty()) {
            setuserinfo_idcardnum.setText(sp.getLoginInfo(context, "idCard"));
            setuserinfo_idcardnum.setFocusable(false);
            setuserinfo_idcardnum.setEnabled(false);
        } else {
            setuserinfo_idcardnum.setText("");
            setuserinfo_idcardnum.setFocusable(true);
            setuserinfo_idcardnum.setEnabled(true);
        }

        if (sp.getLoginInfo(context, "gender").equals("m")
                || sp.getLoginInfo(context, "gender").equals("")) {
            set_radio_male.setChecked(true);
        } else {
            set_radio_female.setChecked(true);
        }

        birth = sp.getLoginInfo(context, "birth");
        if (birth.equals("")) {
            setuserinfo_borndate.setText("设置出生日期");
        } else {
            birth = CommonUtil.getStampTimeStr4(birth);
            setuserinfo_borndate.setText(birth);
        }

        if (sp.getLoginInfo(context, "marriage").equals("true")) {
            set_radio_married.setChecked(true);
        } else {
            set_radio_unmarried.setChecked(true);
        }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.publishtopic_popup_takephpto:
                doHandlerPhoto(PIC_FROM_CAMERA);
                break;
            case R.id.publishtopic_popup_choicepic:
                doHandlerPhoto(PIC_FROM＿LOCALPHOTO);
                break;
            case R.id.publishtopic_popup_canceltake:
                pop_takephoto.dismiss();
                break;
            case R.id.set_submituserinfo: // 提交个人设置
                uploadPics();
                break;
            case R.id.set_setheadimg:
                // TODO Auto-generated method stub
                pop_takephoto.showAtLocation(view, Gravity.BOTTOM
                        | Gravity.CENTER_HORIZONTAL, 0, 0);
                photoname = CommonUtil.getFileNameByDate() + ".jpeg";
                break;
            case R.id.setuserinfo_borndate:
                String bornDate = setuserinfo_borndate.getText().toString();
                initpopdatetime(bornDate);
                break;
        }
    }

//	Handler handler = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//			switch (msg.what) {
//			case 0: // 头像上传完成后，修改其他资料
//				headImg = sp.getLoginInfo(context, "headImg");
//				
//				break;
//			default:
//				break;
//			}
//		}
//	};

    /**
     * 上传用户头像
     *
     * @author zhangyl
     */
    private void uploadPics() {
        if (!photopath.equals("") && !photoname.equals("")) { // 修改头像
            UploadFileEngin.uploadFile(photoname, photopath, context,
                    new UploadCallback(context) {
                        @Override
                        public void onSuccess(ResponseInfo arg0) {
                            super.onSuccess(arg0);
                            canclDialog();
                            String head = arg0.result.toString();
                            modifyUserInfo(head);
                            // CommonToast.makeText(context, "头像上传成功");
//							handler.sendEmptyMessage(0);
                        }

                        @Override
                        public void onFailure(HttpException arg0, String arg1) {
                            // super.onFailure(arg0, arg1);
                            // CommonToast.makeText(context, "头像上传失败");
                        }
                    });
        } else { // 没有修改头像
            headImg = sp.getLoginInfo(context, "headImg");
            modifyUserInfo(headImg);
        }
    }

    /**
     * 修改用户信息接口
     *
     * @author zhangyl
     */
    private void modifyUserInfo(final String headImg) {
        nickName = setuserinfo_nickname.getText().toString();
        if (nickName.length() > 10) {
            CommonToast.makeText(context, "昵称不能超过10个字符");
            return;
        }

        phoneNumber = setuserinfo_mobile.getText().toString();

        email = setuserinfo_email.getText().toString();
        if (email.equals("")) {
            CommonToast.makeText(context, "邮箱不能为空");
            return;
        } else if (!CommonUtil.isEmail(email)) { // 验证邮箱
            CommonToast.makeText(context, "邮箱格式错误");
            return;
        }

        idCard = setuserinfo_idcardnum.getText().toString();
        if (idCard.equals("")) {
            CommonToast.makeText(context, "身份证号不能为空");
            return;
        } else if (!CommonUtil.isIdCard(idCard)) { // 验证身份证号
            CommonToast.makeText(context, "身份证号格式错误");
            return;
        }

        if (set_radio_married.isChecked()) {
            marriage = true;
        } else {
            marriage = false;
        }

        birth = setuserinfo_borndate.getText().toString();

        if (set_radio_male.isChecked()) {
            gender = "m";
        } else {
            gender = "w";
        }

        String bizId = "000";
        String serviceName = "modifyUserInfo";
        String servicePara = "";
        HashMap<String, Object> map = new HashMap<String, Object>();

        map.put("headImg", headImg);
        map.put("nickName", nickName);
        map.put("phoneNumber", phoneNumber);
        map.put("email", email);
        map.put("idCard", idCard);
        map.put("gender", gender);
        birth = String.valueOf(CommonUtil.getStampTime2(birth));
        map.put("birth", birth);
        map.put("marriage", marriage);

        map.put("customerId", sp.getLoginInfo(context, "customerId"));
        map.put("token", sp.getLoginInfo(context, "token"));

        servicePara = JSON.toJSONString(map);
        ServiceEngin.Request(context, bizId, serviceName, servicePara,
                new EnginCallback(context) {
                    @Override
                    public void onSuccess(ResponseInfo arg0) {
                        // TODO Auto-generated method stub
                        super.onSuccess(arg0);
                        String result = Des3.decode(arg0.result.toString());
                        JSONObject obj = JSON.parseObject(result);
                        if (obj.get("resultCode") != null
                                && obj.get("resultCode").toString().equals("0")) {
                            // 修改后的个人设置信息保存到单例中
                            sp.saveLoginInfo(context, "headImg", headImg);
                            sp.saveLoginInfo(context, "nickName", nickName);
                            sp.saveLoginInfo(context, "email", email);
                            sp.saveLoginInfo(context, "idCard", idCard);
                            sp.saveLoginInfo(context, "gender", gender);
                            sp.saveLoginInfo(context, "birth", birth);
                            sp.saveLoginInfo(context, "marriage", marriage + "");
                            CommonToast.makeText(context, "修改成功");
                        } else if (obj.get("errorCode").toString()
                                .equals("1123")) {
                            commonDialog = new CommonDialog(getActivity(), 2,
                                    "确定", "取消", new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    Intent intent = new Intent(
                                            getActivity(),
                                            LoginActivity.class);
                                    startActivityForResult(intent, ServiceEngin.REQUEST_LOGIN);
                                    commonDialog.dismiss();
                                }
                            }, new OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    commonDialog.dismiss();
                                }
                            }, null, "请使用您的身份证号码登录");
                            commonDialog.show();
                        }
                    }
                });
    }

    /*
     * 根据不同方式选择图片 ,0-本地相册选择，1为拍照
     */
    private void doHandlerPhoto(int type) {
        try {
            // 保存裁剪后的图片文件
            File pictureFileDir = new File(
                    Environment.getExternalStorageDirectory(),
                    "/ncihealth/Image");
            if (!pictureFileDir.exists()) {
                pictureFileDir.mkdirs();
            }
            File picFile = new File(pictureFileDir, photoname);
            if (!picFile.exists()) {
                picFile.createNewFile();
            }
            photoUri = Uri.fromFile(picFile);
            if (type == PIC_FROM_CAMERA) {
                Intent cameraIntent = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(cameraIntent, PIC_FROM_CAMERA);
                // Intent cameraIntent = new Intent(getActivity(),
                // CameraActivity.class);
                // cameraIntent.putExtra("photoUri", photoUri);
                // startActivityForResult(cameraIntent, PIC_FROM_CAMERA);
                pop_takephoto.dismiss();
            } else {
                // Intent photoIntent = new Intent();
                // photoIntent.setType("image/*"); // 设置文件类型
                // photoIntent.setAction(Intent.ACTION_GET_CONTENT);
                // 上边那种Intent跳转在华为手机上是打不开图片进行裁剪的
                Intent photoIntent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(photoIntent, PIC_FROM＿LOCALPHOTO);
                pop_takephoto.dismiss();
            }
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
                        cropImageUriByTakePhoto(photoUri);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case PIC_FROM＿LOCALPHOTO:
                    try {
                        Uri pUri = data.getData();
                        cropImageUriByTakePhoto(pUri);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case SAVE_IMAGE:
                    try {
                        if (photoUri != null) {
                            photopath = CommonUtil.getRealFilePath(context,
                                    photoUri);
                            Drawable d = Drawable.createFromPath(photopath);
                            // set_setheadimg.setBackgroundDrawable(d);自定义的圆形头像,不可以用xutils显示,也不可以用background设置
                            set_setheadimg.setImageDrawable(d);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 100:
                    // initview();
                    initdata();
                    // initpop_takephoto();// 创建takephoto
                    break;
            }

        } else {// 将当前图片名称和base都设置为"",防止添加多次或者添加不同名称的重复图片
            photoname = "";
        }
    }

    /**
     * 启动裁剪
     *
     * @param uri
     */
    private void cropImageUriByTakePhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置公用参数
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 600);
        intent.putExtra("outputY", 600);
        intent.putExtra("noFaceDetection", true); // no face detection
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        startActivityForResult(intent, SAVE_IMAGE);
    }

    /**
     * 初始化日期时间选择器
     */
    private void initpopdatetime(final String bornDate) {
        // TODO Auto-generated method stub

        final DatePickerPopWindow popWindow = new DatePickerPopWindow(context);
        popWindow.setCurrentTime(setuserinfo_borndate.getText().toString());
        WindowManager.LayoutParams lp = getActivity().getWindow()
                .getAttributes();
        lp.alpha = 0.5f;
        getActivity().getWindow().setAttributes(lp);
        popWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        popWindow.btnCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                setuserinfo_borndate.setText(bornDate);
                popWindow.dismiss();
            }
        });
        popWindow.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                // TODO Auto-generated method stub
                WindowManager.LayoutParams lp = getActivity().getWindow()
                        .getAttributes();
                lp.alpha = 1f;
                getActivity().getWindow().setAttributes(lp);
            }
        });
        popWindow.setOnTimeSelectListener(new OnTimeSelectListener() {

            @Override
            public void onTimeSelect(String time) {
                // TODO Auto-generated method stub
                setuserinfo_borndate.setText(time);
            }
        });
    }

}
