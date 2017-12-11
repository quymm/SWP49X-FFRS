package com.capstone.ffrs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReservationInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button btRatingField = (Button) findViewById(R.id.btRatingField);
        Button btRatingOpponent = (Button) findViewById(R.id.btRatingOpponent);
        Button btReport = (Button) findViewById(R.id.btReport);
        Button btCancel = (Button) findViewById(R.id.btCancel);

        Bundle b = getIntent().getExtras();
        String status = b.getString("status");
        TextView txtStatus = (TextView) findViewById(R.id.text_status);
        txtStatus.setText(status);

        boolean tourMatchMode = b.getBoolean("tour_match_mode");
        TableRow opponentRow = (TableRow) findViewById(R.id.opponent_layout);
        if (!tourMatchMode) {
            opponentRow.setVisibility(View.GONE);

            TextView txtMatchMode = (TextView) findViewById(R.id.text_match_mode);
            txtMatchMode.setText("Đá riêng");

            switch (status) {
                case "Sắp tới":
                    txtStatus.setTextColor(getResources().getColor(R.color.red));
                    btRatingField.setVisibility(View.GONE);
                    break;
                case "Đang diễn ra":
                    txtStatus.setTextColor(getResources().getColor(R.color.yellow));
                    btRatingField.setVisibility(View.GONE);
                    btCancel.setVisibility(View.GONE);
                    break;
                case "Đã xong":
                    txtStatus.setTextColor(getResources().getColor(R.color.green));
                    btRatingField.setVisibility(View.VISIBLE);
                    btCancel.setVisibility(View.GONE);
                    break;
                default:
                    btRatingField.setVisibility(View.GONE);
                    break;
            }

            btRatingOpponent.setVisibility(View.GONE);
            btReport.setVisibility(View.GONE);
        } else {
            opponentRow.setVisibility(View.VISIBLE);

            TextView txtOpponentName = (TextView) findViewById(R.id.text_opponent);
            txtOpponentName.setText(b.getString("opponent_team_name"));

            TextView txtMatchMode = (TextView) findViewById(R.id.text_match_mode);
            txtMatchMode.setText("Đá chung");

            switch (status) {
                case "Sắp đá":
                    txtStatus.setTextColor(getResources().getColor(R.color.red));
                    btRatingField.setVisibility(View.GONE);
                    btRatingOpponent.setVisibility(View.GONE);
                    break;
                case "Đang diễn ra":
                    txtStatus.setTextColor(getResources().getColor(R.color.yellow));
                    btRatingField.setVisibility(View.GONE);
                    btRatingOpponent.setVisibility(View.GONE);
                    btCancel.setVisibility(View.GONE);
                    break;
                case "Đã xong":
                    txtStatus.setTextColor(getResources().getColor(R.color.green));
                    btRatingField.setVisibility(View.VISIBLE);
                    btRatingOpponent.setVisibility(View.VISIBLE);
                    btCancel.setVisibility(View.GONE);
                    break;
                default:
                    btRatingField.setVisibility(View.GONE);
                    btRatingOpponent.setVisibility(View.GONE);
                    break;
            }

            btCancel.setVisibility(View.GONE);
        }

        TextView txtFieldName = (TextView) findViewById(R.id.text_field_name);
        txtFieldName.setText(b.getString("field_name"));

        TextView txtDate = (TextView) findViewById(R.id.text_date);
        String strDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date(Long.valueOf(b.getString("date"))));
        txtDate.setText(strDate);

        TextView txtFieldType = (TextView) findViewById(R.id.text_field_type);
        txtFieldType.setText(getResources().getStringArray(R.array.field_types)[b.getInt("field_type_id") - 1]);

        SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
        try {
            Date startTime = sdf.parse(b.getString("start_time"));
            Date endTime = sdf.parse(b.getString("end_time"));

            sdf = new SimpleDateFormat("H:mm");

            TextView txtStartTime = (TextView) findViewById(R.id.text_from);
            txtStartTime.setText(sdf.format(startTime));

            TextView txtEndTime = (TextView) findViewById(R.id.text_to);
            txtEndTime.setText(sdf.format(endTime));

            long duration = (endTime.getTime() - startTime.getTime()) / (1000 * 60);
            long hours = (duration / 60);
            long minutes = (duration % 60);
            String strDuration = (hours != 0 ? hours + " tiếng " : "") + (minutes != 0 ? minutes + " phút" : "");
            TextView txtDuration = (TextView) findViewById(R.id.text_duration);
            txtDuration.setText(strDuration);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onClickRateField(View view) {
        Bundle b = getIntent().getExtras();
        Intent intent = new Intent(this, RatingFieldActivity.class);
        intent.putExtra("id", b.getInt("id"));
        intent.putExtra("user_id", b.getInt("user_id"));
        intent.putExtra("field_id", b.getInt("field_id"));
        intent.putExtra("date", b.getString("date"));
        intent.putExtra("start_time", b.getString("start_time"));
        intent.putExtra("end_time", b.getString("end_time"));
        intent.putExtra("field_name", b.getString("field_name"));
        startActivity(intent);
    }

    public void onClickRateOpponent(View view) {
        Bundle b = getIntent().getExtras();
        Intent intent = new Intent(this, RatingOpponentActivity.class);
        intent.putExtra("id", b.getInt("id"));
        intent.putExtra("user_id", b.getInt("user_id"));
        intent.putExtra("opponent_id", b.getInt("opponent_id"));
        intent.putExtra("field_id", b.getInt("field_id"));
        intent.putExtra("tour_match_id", b.getInt("tour_match_id"));
        intent.putExtra("date", b.getString("date"));
        intent.putExtra("start_time", b.getString("start_time"));
        intent.putExtra("end_time", b.getString("end_time"));
        intent.putExtra("opponent_team_name", b.getString("opponent_team_name"));
        intent.putExtra("field_name", b.getString("field_name"));
        startActivity(intent);
    }


    public void onClickReport(View view) {
        Bundle b = getIntent().getExtras();
        boolean tourMatchMode = b.getBoolean("tour_match_mode");
        if (!tourMatchMode) {
//            Intent intent = new Intent(this, RatingFieldActivity.class);
//            intent.putExtra("id", b.getInt("id"));
//            intent.putExtra("user_id", b.getInt("user_id"));
//            intent.putExtra("field_id", b.getInt("field_id"));
//            intent.putExtra("date", b.getString("date"));
//            intent.putExtra("start_time", b.getString("start_time"));
//            intent.putExtra("end_time", b.getString("end_time"));
//            intent.putExtra("field_name", b.getString("field_name"));
//            startActivity(intent);
        } else {
            Intent intent = new Intent(this, ReportOpponentActivity.class);
            intent.putExtra("id", b.getInt("id"));
            intent.putExtra("user_id", b.getInt("user_id"));
            intent.putExtra("opponent_id", b.getInt("opponent_id"));
            intent.putExtra("field_id", b.getInt("field_id"));
            intent.putExtra("tour_match_id", b.getInt("tour_match_id"));
            intent.putExtra("date", b.getString("date"));
            intent.putExtra("start_time", b.getString("start_time"));
            intent.putExtra("end_time", b.getString("end_time"));
            intent.putExtra("opponent_team_name", b.getInt("opponent_team_name"));
            intent.putExtra("field_name", b.getString("field_name"));
            startActivity(intent);
        }
    }

    public void onClickCancel(View view) {
        Bundle b = getIntent().getExtras();
        boolean tourMatchMode = b.getBoolean("tour_match_mode");
        if (!tourMatchMode) {
            // Cancel
        }
    }
}
