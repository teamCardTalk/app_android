package com.team.cardTalk;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpResponseHandler;
import org.apache.http.Header;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ArticleWriteFragment extends Fragment implements View.OnClickListener {

    private ImageButton bt_previous;
    private Button bt_complete;
    private ImageView ivArticleWriteIcon;
    private ImageButton bt_photo;
    private ProgressDialog progressDialog;
    private EditText tvArticleWriteTitle;
    private EditText tvArticleWriteContent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Drawable d = null;
        View view = inflater.inflate(R.layout.fragment_article_write, container, false);
        bt_previous = (ImageButton) view.findViewById(R.id.bt_previous);
        bt_complete = (Button) view.findViewById(R.id.bt_complete);
        ivArticleWriteIcon = (ImageView) view.findViewById(R.id.ivArticleWriteIcon);
        bt_photo = (ImageButton) view.findViewById(R.id.bt_photo);
        tvArticleWriteTitle = (EditText) view.findViewById(R.id.tvArticleWriteTitle);
        tvArticleWriteContent = (EditText) view.findViewById(R.id.tvArticleWriteContent);

        bt_previous.setOnClickListener(this);
        bt_complete.setOnClickListener(this);
        bt_photo.setOnClickListener(this);

        try {
            InputStream is = getActivity().getAssets().open("icon1.png");
            d = Drawable.createFromStream(is, null);
            ivArticleWriteIcon.setImageDrawable(d);

        } catch (IOException e) {
            Log.e("ERROR", "ERROR: " + e);
        }

        return view;
    }

    private String filePath;
    private String fileName;
    private static final int REQUEST_PHOTO_ALBUM = 1;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == REQUEST_PHOTO_ALBUM) {
                Uri uri = getRealPathUri(data.getData());
                filePath = uri.toString();
                fileName = uri.getLastPathSegment();

                Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                bt_photo.setImageBitmap(bitmap);
            }
        } catch (Exception e) {
            Log.e("test", "onActivityResult ERROR:" + e);
        }
    }

    private Uri getRealPathUri(Uri uri) {
        Uri filePathUri = uri;
        if (uri.getScheme().toString().compareTo("content") == 0) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                filePathUri = Uri.parse(cursor.getString(column_index));
            }
        }
        return filePathUri;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bt_previous:
                getFragmentManager().popBackStack();
                break;
            case R.id.bt_complete:

                progressDialog = ProgressDialog.show(getActivity(), "", "업로드중입니다...");

                String ID = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
                String DATE = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA).format(new Date());

                Article article = new Article(
                        "temp",
                        1,
                        tvArticleWriteTitle.getText().toString(),
                        "노란 조커",
                        00000001,
                        "icon/icon2.png",
                        DATE,
                        tvArticleWriteContent.getText().toString(),
                        5,
                        DATE,
                        "temp",
                        fileName
                );

                ArticleWritingProxy.uploadArticle(article, filePath,
                        new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                                Log.e("uploadArticle", "success: " + i);
                                progressDialog.cancel();
                                Toast.makeText(getActivity(), "onSuccess", Toast.LENGTH_SHORT).show();
                                getFragmentManager().popBackStack();
                            }

                            @Override
                            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                                Log.e("uploadArticle", "fail: " + i);
                                progressDialog.cancel();
                                Toast.makeText(getActivity(), "onFailure", Toast.LENGTH_SHORT).show();
                            }
                        });


                break;
            case R.id.bt_photo:
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_PHOTO_ALBUM);
                break;
        }
    }
}