package com.pact.healthapp.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore.Images.ImageColumns;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.util.EMLog;
import com.easemob.util.PathUtil;
import com.pact.healthapp.R;
import com.pact.healthapp.data.Constants;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class CommonUtil {
    public static SimpleDateFormat sdf = new SimpleDateFormat(
            "yyyyMMddHHmmssSSS");

    /**
     * 统一文件名命名方式(图片[头像,帖子,病历],音频,视频)
     *
     * @return
     * @author zhangyl
     */
    public static String getFileNameByDate() {
        return sdf.format(new Date());
    }

    /**
     * 判断是否为手机号
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
        /*
         * 移动：134、135、136、137、138、139、147（数据卡）、150、151、157(TD)、158、159、178（4G）、182
		 * 、183、184、187、188 联通：130、131、132、145(数据卡）、152、155、156、176（4G）、185、186
		 * 电信：133、153、177（4G）、180、189、（1349卫通） 虚拟运营商：170
		 * 总结起来就是第一位必定为1，第二位必定为3或4或5或7或8，其他位置的可以为0-9
		 */
        String telRegex = "[1][0-9]\\d{9}";//"[1]"代表第1位为数字1，"[34578]"代表第二位可以为3、4、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);

    }

    /**
     * 获取应用版本号
     *
     * @param context
     * @return
     * @throws Exception
     */
    public static String getVersionName(Context context) throws Exception {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(
                context.getPackageName(), 0);
        return packInfo.versionName;
    }

    /**
     * 创建bitmap
     *
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap getBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    /**
     * 获取屏幕宽
     *
     * @param activity
     */
    public static int getScreenWide(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    /**
     * 获取屏幕高
     *
     * @param activity
     */
    public static int getScreenHeight(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }

    /**
     * 创建bitmap
     *
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap getBitMap2(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inSampleSize = 2;
        opt.inInputShareable = true;
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    /**
     * "yyyy-MM-dd HH:mm:ss"格式转long
     *
     * @param releaseTime
     * @return
     */
    public static long getStampTime(String releaseTime) {
        String format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date;
        try {
            if (releaseTime.endsWith(".0")) {// 服务器数据异常处理
                releaseTime = releaseTime.substring(0, format.length());
            }
            date = sdf.parse(releaseTime);
            return date.getTime();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return System.currentTimeMillis();
    }

    /**
     * "yyyy年MM月dd日"格式转long
     *
     * @param releaseTime
     * @return
     */
    public static long getStampTime2(String releaseTime) {
        String format = "yyyy年MM月dd日";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date;
        try {
            if (releaseTime.endsWith(".0")) {// 服务器数据异常处理
                releaseTime = releaseTime.substring(0, format.length());
            }
            date = sdf.parse(releaseTime);
            return date.getTime();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return System.currentTimeMillis();
    }

    /**
     * long格式转"yyyy-MM-dd HH:mm:ss"
     *
     * @param stampTime
     * @return
     */
    public static String getStampTimeStr(String stampTime) {
        String format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            long time = Long.parseLong(stampTime);
            return sdf.format(new Date(time));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    /**
     * long格式转"yyyy-MM-dd"
     *
     * @param stampTime
     * @return
     */
    public static String getStampTimeStr2(String stampTime) {
        String format = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            long time = Long.parseLong(stampTime);
            return sdf.format(new Date(time));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    /**
     * long格式转"yyyy-MM-dd HH:mm"
     *
     * @param stampTime
     * @return
     */
    public static String getStampTimeStr3(String stampTime) {
        String format = "yyyy-MM-dd HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            long time = Long.parseLong(stampTime);
            return sdf.format(new Date(time));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return sdf.format(System.currentTimeMillis());
    }

    /**
     * long格式转"yyyy年MM月dd日"
     *
     * @param stampTime
     * @return
     */
    public static String getStampTimeStr4(String stampTime) {
        String format = "yyyy年MM月dd日";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            long time = Long.parseLong(stampTime);
            return sdf.format(new Date(time));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return sdf.format(new Date());
    }

    /**
     * long格式转"HH:mm"
     *
     * @param stampTime
     * @return
     */
    public static String getStampTimeStr5(String stampTime) {
        String format = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            long time = Long.parseLong(stampTime);
            return sdf.format(new Date(time));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return sdf.format(new Date());
    }

    /**
     * @param mss 要转换的毫秒数
     * @return 该毫秒数转换为 * days * hours * minutes * seconds 后的格式
     */
    public static String formatDuring(long mss) {
        long days = mss / (1000 * 60 * 60 * 24);
        long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (mss % (1000 * 60)) / 1000;
        return days + "天" + hours + "小时";
    }

    /**
     * @param begin 时间段的开始
     * @param end   时间段的结束
     * @return 输入的两个Date类型数据之间的时间间格用* days * hours * minutes * seconds的格式展示
     */
    public static String formatDuring(Date begin, Date end) {
        return formatDuring(end.getTime() - begin.getTime());
    }

    /**
     * dp转px
     *
     * @param context
     * @param dp
     * @return
     */
    public static int convertDpToPixelInt(Context context, float dp) {

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int px = (int) (dp * (metrics.densityDpi / 160f));
        return px;
    }

    /**
     * px转dp
     *
     * @param ctx
     * @param px
     * @return
     */
    public float convertPixelsToDp(Context ctx, float px) {
        DisplayMetrics metrics = ctx.getResources().getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;

    }

    /**
     * 根据路径获得图片并压缩，返回bitmap用于显示
     *
     * @param filePath 图片路径
     * @return 返回压缩后图片
     * @author zhangyl
     */
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 计算图片的缩放值
     *
     * @param options   压缩比
     * @param reqWidth  压缩后宽
     * @param reqHeight 压缩后高
     * @return
     * @author zhangyl
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /**
     * Try to return the absolute file path from the given Uri
     *
     * @param context
     * @param uri     图片uri
     * @return 图片路径
     */
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri)
            return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri,
                    new String[]{ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 检测Sdcard是否存在
     *
     * @return
     */
    public static boolean isExitsSdcard() {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }

    /**
     * 生成图片路径
     *
     * @param remoteUrl 环信的图片地址
     * @return
     * @author zhangyl
     */
    public static String getImagePath(String remoteUrl) {
        String imageName = remoteUrl.substring(remoteUrl.lastIndexOf("/") + 1,
                remoteUrl.length());
        String path = PathUtil.getInstance().getImagePath() + "/" + imageName;
        EMLog.d("msg", "image path:" + path);
        return path;

    }

    /**
     * 获取缩略图
     *
     * @param thumbRemoteUrl 缩略图url
     * @return
     * @author zhangyl
     */
    public static String getThumbnailImagePath(String thumbRemoteUrl) {
        String thumbImageName = thumbRemoteUrl.substring(
                thumbRemoteUrl.lastIndexOf("/") + 1, thumbRemoteUrl.length());
        String path = PathUtil.getInstance().getImagePath() + "/" + "th"
                + thumbImageName;
        EMLog.d("msg", "thum image path:" + path);
        return path;
    }

    /**
     * 根据消息内容和消息类型获取消息内容提示
     *
     * @param message
     * @param context
     * @return
     */
    public static String getMessageDigest(EMMessage message, Context context) {
        String digest = "";
        switch (message.getType()) {
            case LOCATION: // 位置消息
                if (message.direct == EMMessage.Direct.RECEIVE) {
                    // 从sdk中提到了ui中，使用更简单不犯错的获取string方法
                    // digest = EasyUtils.getAppResourceString(context,
                    // "location_recv");
                    digest = getString(context, R.string.location_recv);
                    digest = String.format(digest, message.getFrom());
                    return digest;
                } else {
                    // digest = EasyUtils.getAppResourceString(context,
                    // "location_prefix");
                    digest = getString(context, R.string.location_prefix);
                }
                break;
            case IMAGE: // 图片消息
                digest = getString(context, R.string.picture);
                break;
            case VOICE:// 语音消息
                digest = getString(context, R.string.voice);
                break;
            case VIDEO: // 视频消息
                digest = getString(context, R.string.video);
                break;
            case TXT: // 文本消息
                if (!message.getBooleanAttribute(
                        Constants.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
                    TextMessageBody txtBody = (TextMessageBody) message.getBody();
                    digest = txtBody.getMessage();
                } else {
                    TextMessageBody txtBody = (TextMessageBody) message.getBody();
                    digest = getString(context, R.string.voice_call)
                            + txtBody.getMessage();
                }
                break;
            case FILE: // 普通文件消息
                digest = getString(context, R.string.file);
                break;
            default:
                System.err.println("error, unknow type");
                return "";
        }

        return digest;
    }

    /**
     * 通过id获取string
     *
     * @param context
     * @param resId
     * @return
     * @author zhangyl
     */
    public static String getString(Context context, int resId) {
        return context.getResources().getString(resId);
    }

    /**
     * 压缩文件夹生成zip压缩包
     *
     * @param zipFileName 生成压缩文件输出路径
     * @param inputFile   被压缩的文件夹
     * @throws Exception
     * @author zhangyl
     */
    public void zipFile(String zipFileName, File inputFile) throws Exception {
        System.out.println("压缩中...");
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
                zipFileName));
        BufferedOutputStream bo = new BufferedOutputStream(out);
        zip(out, inputFile, inputFile.getName(), bo);
        bo.close();
        out.close(); // 输出流关闭
        System.out.println("压缩完成");
    }

    /**
     * 压缩
     *
     * @param out
     * @param f
     * @param base
     * @param bo
     * @throws Exception
     * @author zhangyl
     */
    private void zip(ZipOutputStream out, File f, String base,
                     BufferedOutputStream bo) throws Exception { // 方法重载
        if (f.isDirectory()) {
            File[] fl = f.listFiles();
            if (fl.length == 0) {
                out.putNextEntry(new ZipEntry(base + "/")); // 创建zip压缩进入点base
                System.out.println(base + "/");
            }
            for (int i = 0; i < fl.length; i++) {
                zip(out, fl[i], base + "/" + fl[i].getName(), bo); // 递归遍历子文件夹
            }
        } else {
            out.putNextEntry(new ZipEntry(base)); // 创建zip压缩进入点base
            System.out.println(base);
            FileInputStream in = new FileInputStream(f);
            BufferedInputStream bi = new BufferedInputStream(in);
            int b;
            while ((b = bi.read()) != -1) {
                bo.write(b); // 将字节流写入当前zip目录
            }
            bi.close();
            in.close(); // 输入流关闭
        }
    }

    /**
     * 解压zip压缩包到指定路径
     *
     * @param zipfilename 输入源zip路径路径,被解压的文件
     * @param outputpath  输出路径（文件夹目录）
     * @author zhangyl
     */
    public static void unzipFile(String zipfilename, String outputpath) {
        // TODO Auto-generated method stub
        long startTime = System.currentTimeMillis();
        try {
            ZipInputStream Zin = new ZipInputStream(new FileInputStream(
                    zipfilename));// 输入源zip路径
            BufferedInputStream Bin = new BufferedInputStream(Zin);
            File Fout = null;
            ZipEntry entry;
            try {
                while ((entry = Zin.getNextEntry()) != null
                        && !entry.isDirectory()) {
                    Fout = new File(outputpath, entry.getName());
                    if (!Fout.exists()) {
                        (new File(Fout.getParent())).mkdirs();
                    }
                    FileOutputStream out = new FileOutputStream(Fout);
                    BufferedOutputStream Bout = new BufferedOutputStream(out);
                    int b;
                    while ((b = Bin.read()) != -1) {
                        Bout.write(b);
                    }
                    Bout.close();
                    out.close();
                    System.out.println(Fout + "解压成功");
                }
                Bin.close();
                Zin.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("耗费时间： " + (endTime - startTime) + " ms");
    }

    /**
     * 验证邮箱
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        boolean flag = false;
        try {
            String check = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 18位或者15位身份证验证 18位的最后一位可以是字母x
     *
     * @param idCard
     * @return
     */
    public static boolean isIdCard(String idCard) {
        boolean flag = false;
        try {
            String regx = "[0-9]{17}x";
            String regx1 = "[0-9]{17}X";
            String reg1 = "[0-9]{15}";
            String regex = "[0-9]{18}";
            flag = idCard.matches(regx) || idCard.matches(regx1) || idCard.matches(reg1)
                    || idCard.matches(regex);
        } catch (Exception e) {
            // TODO: handle exception
            flag = false;
        }
        return flag;
    }

    /**
     * 压缩图片至100kb以下
     *
     * @param filePath1 待处理图片,原图
     * @param filePath2 处理完成图片,上传用,用完删除
     * @return
     * @author zhangyl
     */
    public static void compressBmpToFile(String filePath1, String filePath2) {
        // 解码图片
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath1, options);
        // 计算压缩比例
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        int reqHeight = 800;
        int reqWidth = 480;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        // 在内存中创建bitmap对象，这个对象按照缩放大小创建的
        options.inSampleSize = calculateInSampleSize(options, 480, 800);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath1, options);
        try {
            FileOutputStream fos = new FileOutputStream(filePath2);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, fos);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 删除文件或文件夹
     *
     * @param file
     */
    public static void delete(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }

        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }

            for (int i = 0; i < childFiles.length; i++) {
                delete(childFiles[i]);
            }
            file.delete();
        }
    }

    /**
     * 时间转换 如"下午 01:35"，转换成"下午 1:35"
     *
     * @param time
     */
    public static String convertTime(String time) {
        String temp = time;
        String tempTime = time;
        temp = temp.substring(time.length() - 5, time.length() - 4);
        if (temp.equals("0")) {
            tempTime = time.substring(0, 2) + " "
                    + time.substring(time.length() - 4, time.length());
            return tempTime;
        } else {
            return time;
        }
    }

    /**
     * 根据用户生日计算年龄
     */
    public static int getAgeByBirthday(Date birthday) {
        Calendar cal = Calendar.getInstance();
        if (cal.before(birthday)) {
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }

        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

        cal.setTime(birthday);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH) + 1;
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                // monthNow==monthBirth
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                }
            } else {
                // monthNow>monthBirth
                age--;
            }
        }
        return age;
    }

    /**
     * 判断当前日期是星期几
     *
     * @param pTime 设置的需要判断的时间  //格式如12/31/2012  月/日/年
     * @return dayForWeek 判断结果
     * @Exception 发生异常
     */
    public static String getWeek(String pTime) {
        String Week = "";
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(pTime));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            Week += "星期日";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 2) {
            Week += "星期一";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 3) {
            Week += "星期二";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 4) {
            Week += "星期三";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 5) {
            Week += "星期四";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 6) {
            Week += "星期五";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 7) {
            Week += "星期六";
        }
        return Week;
    }

    /**
     * 金额保留两位小数
     *
     * @param price
     * @return
     */
    public static String toPrice(String price) {
        if (price.equals("")) {
            return "0.00";
        } else {
            BigDecimal bd = new BigDecimal(Double.parseDouble(price));
            bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
            return bd.toString();
        }
    }

    /**
     * 把开始时间MM/dd/yyyy HH:mm 结束时间MM/dd/yyyy HH:mm
     * 转换成时间段yyyy-MM-dd HH:mm-HH:mm
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static String convertTime(String startTime, String endTime) {
        String tempTime;
        String format = "MM/dd/yyyy HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            if (startTime.endsWith(".0")) {// 服务器数据异常处理
                startTime = startTime.substring(0, format.length());
            }
            date = sdf.parse(startTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String format2 = "yyyy-MM-dd HH:mm";
        SimpleDateFormat sdf2 = new SimpleDateFormat(format2);
        tempTime = sdf2.format(new Date(date.getTime())) + "-"
                + endTime.substring(endTime.length() - 5, endTime.length());
        return tempTime;
    }

    /**
     * @param serviceDate 2016-02-01
     * @param startHour   9
     * @param startMinute 0
     * @param endHour     9
     * @param endMinute   30
     * @return 2016年02月01日 09:00-09:30
     */
    public static String convertTime1(String serviceDate, String startHour,
                                      String startMinute, String endHour, String endMinute) {
        if (serviceDate.length() > 0) {
            StringBuilder builder = new StringBuilder();
            builder.append(serviceDate + " ");
            if (startHour.length() == 1 && Integer.parseInt(startHour) < 10) {
                builder.append("0" + startHour + ":");
            } else if (startHour.length() == 2) {
                builder.append(startHour + ":");
            }

            if (startMinute.length() == 1 && Integer.parseInt(startMinute) < 10) {
                builder.append("0" + startMinute + "-");
            } else if (startMinute.length() == 2) {
                builder.append(startMinute + "-");
            }

            if (endHour.length() == 1 && Integer.parseInt(endHour) < 10) {
                builder.append("0" + endHour + ":");
            } else if (endHour.length() == 2) {
                builder.append(endHour + ":");
            }

            if (endMinute.length() == 1 && Integer.parseInt(endMinute) < 10) {
                builder.append("0" + endMinute);
            } else if (endMinute.length() == 2) {
                builder.append(endMinute);
            }
            return builder.toString();
        } else {
            return "";
        }
    }


}
