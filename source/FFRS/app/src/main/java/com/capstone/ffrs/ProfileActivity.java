package com.capstone.ffrs;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.capstone.ffrs.controller.NetworkController;
import com.capstone.ffrs.utils.HostURLUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private String hostURL;
    private CircleImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        hostURL = HostURLUtils.getInstance(this).getHostURL();

        imageView = (CircleImageView) findViewById(R.id.profile_image);

        loadAccountInfo();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void loadAccountInfo() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final int userId = sharedPreferences.getInt("user_id", -1);
        String url = hostURL + getResources().getString(R.string.url_get_user_by_id);
        url = String.format(url, userId);

        RequestQueue queue = NetworkController.getInstance(this).getRequestQueue();
        JsonObjectRequest newsReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (!response.isNull("body")) {
                        JSONObject body = response.getJSONObject("body");
                        if (body != null && body.length() > 0) {
                            TextView txtUsername = (TextView) findViewById(R.id.text_username);
                            txtUsername.setText(body.getString("username"));

                            TextView txtTeamName = (TextView) findViewById(R.id.text_team_name);
                            txtTeamName.setText(body.getJSONObject("profileId").getString("name"));

                            TextView txtPhone = (TextView) findViewById(R.id.text_phone);
                            txtPhone.setText(body.getJSONObject("profileId").getString("phone"));

                            TextView txtRatingScore = (TextView) findViewById(R.id.text_rating_score);
                            txtRatingScore.setText(body.getJSONObject("profileId").getString("ratingScore"));

                            TextView txtBonus = (TextView) findViewById(R.id.text_bonus_points);
                            txtBonus.setText(body.getJSONObject("profileId").getInt("bonusPoint") + " điểm");

                            TextView txtBalance = (TextView) findViewById(R.id.text_balance);
                            txtBalance.setText(body.getJSONObject("profileId").getInt("balance") + " nghìn đồng");

                            TextView txtAvailableBalance = (TextView) findViewById(R.id.text_available_balance);
                            txtAvailableBalance.setText((body.getJSONObject("profileId").getInt("balance") - body.getJSONObject("profileId").getInt("accountPayable")) + " nghìn đồng");

                            RequestBuilder<Drawable> drawable = Glide.with(ProfileActivity.this).load(body.getJSONObject("profileId").getString("avatarUrl"));
                            drawable.error(Glide.with(ProfileActivity.this).load(getResources().getDrawable(R.drawable.people)));
                            drawable.into(imageView);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null && error.networkResponse.statusCode == 404) {

                } else if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getApplicationContext(), "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(getApplicationContext(), "Lỗi xác nhận!", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(getApplicationContext(), "Lỗi từ phía máy chủ!", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(getApplicationContext(), "Lỗi kết nối mạng!", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(getApplicationContext(), "Lỗi parse!", Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    String utf8String = new String(response.data, "UTF-8");
                    return Response.success(new JSONObject(utf8String), HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    // log error
                    return Response.error(new ParseError(e));
                } catch (JSONException e) {
                    // log error
                    return Response.error(new ParseError(e));
                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");

                return headers;
            }

        };
        queue.add(newsReq);
    }


    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;

    public void onClickChangeAvatar(View view) {
        final CharSequence[] items = {"Chụp máy ảnh", "Chọn từ thư viện máy", "Hủy bỏ"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Đổi ảnh đại diện");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Chụp máy ảnh")) {
                    checkCameraPermission();
                } else if (items[item].equals("Chọn từ thư viện máy")) {
                    checkStoragePermission();
                } else if (items[item].equals("Hủy bỏ")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == SELECT_FILE || requestCode == REQUEST_CAMERA) && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                Log.d("ERROR", "No data");
                return;
            }
            if (requestCode == REQUEST_CAMERA) {
                final Bundle extras = data.getExtras();
                if (extras != null) {
                    //Get image
                    Bitmap newProfilePic = extras.getParcelable("data");
                    Glide.with(this).load(newProfilePic).into(imageView);
                } else {
                    Log.d("ERROR", "No data");
                }
            }

            if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                String selectedImagePath = getPath(selectedImageUri);
                Glide.with(this).load(selectedImagePath).into(imageView);
            }
        }
    }

    /**
     * helper to retrieve the path of an image URI
     */
    public String getPath(Uri uri) {
        // just some safety built in
        if (uri == null) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
        // this is our fallback here
        return uri.getPath();
    }

    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final int READ_STORAGE_REQUEST_CODE = 101;

    public boolean checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown

                AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle("Yêu cầu đọc kho lưu trữ").
                        setMessage("Ứng dụng cần quyền đọc kho lữu trữ của thiết bị để có thể lấy hình ảnh từ máy. Vui lòng bật quyền để tiếp tục sử dụng ứng dụng.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(ProfileActivity.this,
                                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                                        READ_STORAGE_REQUEST_CODE);
                            }
                        }).create();
                alertDialog.setCancelable(false);
                alertDialog.show();

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_CAMERA_REQUEST_CODE);
            }
            return false;
        } else {
            Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
            getIntent.setType("image/*");

            Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickIntent.setType("image/*");
//        pickIntent.putExtra("crop", "true");
//        pickIntent.putExtra("scale", true);
//        pickIntent.putExtra("outputX", 256);
//        pickIntent.putExtra("outputY", 256);
//        pickIntent.putExtra("aspectX", 1);
//        pickIntent.putExtra("aspectY", 1);
//        pickIntent.putExtra("return-data", true);

            Intent chooserIntent = Intent.createChooser(getIntent, "Chọn ảnh");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

            startActivityForResult(chooserIntent, SELECT_FILE);
            return true;
        }
    }

    public boolean checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.CAMERA)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown

                AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle("Yêu cầu quyền máy ảnh").
                        setMessage("Ứng dụng cần quyền sử dụng máy ảnh của thiết bị để có thể chụp. Vui lòng bật quyền để tiếp tục sử dụng ứng dụng.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(ProfileActivity.this,
                                        new String[]{android.Manifest.permission.CAMERA},
                                        MY_CAMERA_REQUEST_CODE);
                            }
                        }).create();
                alertDialog.setCancelable(false);
                alertDialog.show();

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.CAMERA},
                        MY_CAMERA_REQUEST_CODE);
            }
            return false;
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQUEST_CAMERA);
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_CAMERA_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_GRANTED) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, REQUEST_CAMERA);
                    }

                } else {
                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "Ứng dụng phải có quyền sử dụng chụp ảnh của bạn để chụp ảnh", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case READ_STORAGE_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        getIntent.setType("image/*");

                        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        pickIntent.setType("image/*");

                        Intent chooserIntent = Intent.createChooser(getIntent, "Chọn ảnh");
                        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

                        startActivityForResult(chooserIntent, SELECT_FILE);
                    }

                } else {
                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "Ứng dụng phải có quyền sử dụng đọc kho lữu trữ của bạn để lấy ảnh", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
