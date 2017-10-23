package com.capstone.ffrs.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.capstone.ffrs.FieldSuggestActivity;
import com.capstone.ffrs.FieldTimeActivity;
import com.capstone.ffrs.entity.Field;
import com.capstone.ffrs.R;
import com.capstone.ffrs.controller.NetworkController;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by AndroidNovice on 6/5/2016.
 */
public class FieldAdapter extends RecyclerView.Adapter<FieldAdapter.MyViewHolder> {

    private List<Field> fieldList;
    private Context context;
    private LayoutInflater inflater;
    private int userId;

    public FieldAdapter(Context context, List<Field> fieldList) {

        this.context = context;
        this.fieldList = fieldList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = inflater.inflate(R.layout.field_item, parent, false);
        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Field field = fieldList.get(position);

        holder.itemView.setTag(R.id.card_view, field.getId());
        holder.title.setText(field.getFieldName());
        holder.content.setText(field.getAddress());
        holder.progressBar.setVisibility(View.VISIBLE);
        String url = field.getImgURL();
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
        holder.setImageURL(url);
        holder.imageview.setVisibility(View.GONE);
        holder.imageview.setDefaultImageResId(R.mipmap.placeholder);
        holder.imageview.setErrorImageResId(R.mipmap.placeholder);
        ImageLoader imageLoader = NetworkController.getInstance(context).getImageLoader();
        imageLoader.get(url, new ImageLoader.ImageListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                holder.progressBar.setVisibility(View.GONE);
                holder.imageview.setVisibility(View.VISIBLE);
            }

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                if (response.getBitmap() != null) {
                    holder.progressBar.setVisibility(View.GONE);
                    holder.imageview.setImageBitmap(response.getBitmap());
                    holder.imageview.setVisibility(View.VISIBLE);
                } else {
                    holder.progressBar.setVisibility(View.GONE);
                    holder.imageview.setVisibility(View.VISIBLE);
                }
            }
        });
        holder.imageview.setImageUrl(url, imageLoader);

    }

    @Override
    public int getItemCount() {
        return fieldList.size();
    }

//    @Override
//    public Filter getFilter() {
//
//        return new Filter() {
//            @Override
//            protected FilterResults performFiltering(CharSequence charSequence) {
//
//                String charString = charSequence.toString();
//
//                if (charString.isEmpty()) {
//                    fieldList = fieldList;
//                } else {
//                    charString = normalizeText(charString);
//
//                    List<Field> filteredList = new ArrayList<>();
//                    for (Field field : fieldList) {
//
//                        // Vietnamese characters handling
//                        String fieldName = field.getFieldName().toLowerCase();
//                        try {
//                            fieldName = normalizeText(fieldName);
//                        } catch (Exception e) {
//                            Log.d("ERROR", e.getMessage());
//                        }
//
//                        // Search like processing
//                        if (fieldName.contains(charString)) {
//                            filteredList.add(field);
//                        }
//
//                        fieldList = filteredList;
//                    }
//                }
//                FilterResults filterResults = new FilterResults();
//                filterResults.values = fieldList;
//                return filterResults;
//            }
//
//            @Override
//            protected void publishResults(CharSequence charSequence, FilterResults
//                    filterResults) {
//                fieldList = (ArrayList<Field>) filterResults.values;
//                Intent intent = new Intent("search-message");
//                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
//                intent.putExtra("empty_list", fieldList.isEmpty());
//                notifyDataSetChanged();
//            }
//        };
//    }
//
//    public String normalizeText(String rawText) {
//        String temp = Normalizer.normalize(rawText, Normalizer.Form.NFD);
//        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
//        rawText = pattern.matcher(temp).replaceAll("").replaceAll("đ", "d");
//        return rawText;
//    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView content, title;
        private NetworkImageView imageview;
        private ProgressBar progressBar;
        private String image_url;

        public void setImageURL(String url) {
            image_url = url;
        }

        public MyViewHolder(final View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title_view);
            content = (TextView) itemView.findViewById(R.id.content_view);
            // Volley's NetworkImageView which will load Image from URL
            imageview = (NetworkImageView) itemView.findViewById(R.id.thumbnail);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FieldTimeActivity.class);
                    int id = (int) itemView.getTag(R.id.card_view);
                    intent.putExtra("field_id", id);
                    intent.putExtra("field_name", title.getText());
                    intent.putExtra("field_address", content.getText());
//                    if (image_url.isEmpty()) {
//                        image_url = "http://bongda.phanmemvang.com.vn/wp-content/uploads/2015/03/lan2chaoluanganhgnhe-1-e1426212803227.jpg";
//                    }
//                    intent.putExtra("image_url", image_url);
                    intent.putExtra("user_id", userId);
                    context.startActivity(intent);
                }
            });
        }
    }
}