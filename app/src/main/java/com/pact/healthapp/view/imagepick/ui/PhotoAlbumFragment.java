package com.pact.healthapp.view.imagepick.ui;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lidroid.xutils.ViewUtils;
import com.pact.healthapp.R;
import com.pact.healthapp.universal.BaseFragment;
import com.pact.healthapp.view.imagepick.adapter.PhotoAlbumLVAdapter;
import com.pact.healthapp.view.imagepick.model.PhotoAlbumLVItem;
import com.pact.healthapp.view.imagepick.utils.Utility;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Administrator on 2015/12/22.
 */
@SuppressLint("ValidFragment")
public class PhotoAlbumFragment extends BaseFragment {
    private View view;
    private Context context;

    public PhotoAlbumFragment(){
    }
   public PhotoAlbumFragment(Context context){
        this.context=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.view_imagepick_photo_album, null);
        ViewUtils.inject(this, view);
        Intent t = getActivity().getIntent();
        if (!t.hasExtra("latest_count")) {
            return null;
        }
        ListView listView = (ListView) view
                .findViewById(R.id.view_imagepick_album_lv);
        // //第一种方式：使用file
        // File rootFile = new File(Utility.getSDcardRoot());
        // //屏蔽/mnt/sdcard/DCIM/.thumbnails目录
        // String ignorePath = rootFile + File.separator + "DCIM" +
        // File.separator + ".thumbnails";
        // getImagePathsByFile(rootFile, ignorePath);

        // 第二种方式：使用ContentProvider。（效率更高）
        final ArrayList<PhotoAlbumLVItem> list = new ArrayList<PhotoAlbumLVItem>();
        // “最近照片”
        list.add(new PhotoAlbumLVItem(getResources().getString(
                R.string.latest_image), t.getIntExtra("latest_count", -1),
                t.getStringExtra("latest_first_img")));
        // 相册
        list.addAll(getImagePathsByContentProvider());
        PhotoAlbumLVAdapter adapter = new PhotoAlbumLVAdapter(
                getActivity(), list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(context,
                        PhotoWallActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                // 第一行为“最近照片”
                if (position == 0) {
                    intent.putExtra("code", 200);
                } else {
                    intent.putExtra("code", 100);
                    intent.putExtra("folderPath", list.get(position)
                            .getPathName());
                }
                startActivity(intent);
            }
        });


        return view;
    }


    /**
     * 使用ContentProvider读取SD卡所有图片。
     */
    private ArrayList<PhotoAlbumLVItem> getImagePathsByContentProvider() {
        Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String key_MIME_TYPE = MediaStore.Images.Media.MIME_TYPE;
        String key_DATA = MediaStore.Images.Media.DATA;

        ContentResolver mContentResolver = context.getContentResolver();

        // 只查询jpg和png的图片
        Cursor cursor = mContentResolver.query(mImageUri,
                new String[] { key_DATA }, key_MIME_TYPE + "=? or "
                        + key_MIME_TYPE + "=? or " + key_MIME_TYPE + "=?",
                new String[] { "image/jpg", "image/jpeg", "image/png" },
                MediaStore.Images.Media.DATE_MODIFIED);

        ArrayList<PhotoAlbumLVItem> list = null;
        if (cursor != null) {
            if (cursor.moveToLast()) {
                // 路径缓存，防止多次扫描同一目录
                HashSet<String> cachePath = new HashSet<String>();
                list = new ArrayList<PhotoAlbumLVItem>();

                while (true) {
                    // 获取图片的路径
                    String imagePath = cursor.getString(0);

                    File parentFile = new File(imagePath).getParentFile();
                    String parentPath = parentFile.getAbsolutePath();

                    // 不扫描重复路径
                    if (!cachePath.contains(parentPath)) {
                        list.add(new PhotoAlbumLVItem(parentPath,
                                getImageCount(parentFile),
                                getFirstImagePath(parentFile)));
                        cachePath.add(parentPath);
                    }

                    if (!cursor.moveToPrevious()) {
                        break;
                    }
                }
            }

            cursor.close();
        }

        return list;
    }


    /**
     * 获取目录中图片的个数。
     */
    private int getImageCount(File folder) {
        int count = 0;
        File[] files = folder.listFiles();
        for (File file : files) {
            if (Utility.isImage(file.getName())) {
                count++;
            }
        }

        return count;
    }

    /**
     * 获取目录中最新的一张图片的绝对路径。
     */
    private String getFirstImagePath(File folder) {
        File[] files = folder.listFiles();
        for (int i = files.length - 1; i >= 0; i--) {
            File file = files[i];
            if (Utility.isImage(file.getName())) {
                return file.getAbsolutePath();
            }
        }

        return null;
    }

}
