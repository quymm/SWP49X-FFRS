package com.capstone.ffrs.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.capstone.ffrs.FieldTimeActivity;
import com.capstone.ffrs.R;
import com.capstone.ffrs.entity.FieldOwner;
import com.capstone.ffrs.entity.Promotion;

import java.util.List;

/**
 * Created by AndroidNovice on 6/5/2016.
 */
public class PromotionAdapter extends RecyclerView.Adapter<PromotionAdapter.PromotionViewHolder> {

    private List<Promotion> promotionList;
    private Context context;
    private LayoutInflater inflater;
    private int userId;

    public PromotionAdapter(Context context, List<Promotion> promotionList) {
        this.context = context;
        this.promotionList = promotionList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public PromotionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = inflater.inflate(R.layout.promotion_item, parent, false);
        return new PromotionViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(PromotionViewHolder holder, int position) {
        Promotion promotion = promotionList.get(position);

        holder.itemView.setTag(R.id.card_view, promotion);
        holder.promotionName.setText("Khuyến mãi giảm " + promotion.getSaleOff() + "%" + (!promotion.getFreeServices().isEmpty() ? " kèm " + promotion.getFreeServices().toLowerCase() : ""));
        holder.promotionTime.setText("Từ " + promotion.getStartDate() + " " + promotion.getStartTime() + " đến " + promotion.getEndDate() + " " + promotion.getEndTime());
        holder.fieldName.setText("Tại " + promotion.getField().getFieldName());
        String url = promotion.getField().getImgURL();
        loadImageWithGlide(holder.imageview, url);
    }

    public void loadImageWithGlide(ImageView imageView, String url) {
        RequestBuilder<Drawable> requestBuilder = Glide.with(context).load(url);
        requestBuilder.error(Glide.with(context).load(context.getResources().getDrawable(R.drawable.img_placeholder)));
        requestBuilder.into(imageView);
    }

    @Override
    public int getItemCount() {
        return promotionList.size();
    }

    public class PromotionViewHolder extends RecyclerView.ViewHolder {

        private TextView promotionName, promotionTime, fieldName;
        private ImageView imageview;

        public PromotionViewHolder(final View itemView) {
            super(itemView);
            promotionName = (TextView) itemView.findViewById(R.id.text_promotion_name);
            promotionTime = (TextView) itemView.findViewById(R.id.text_promotion_time);
            fieldName = (TextView) itemView.findViewById(R.id.text_promotion_field);
            imageview = (ImageView) itemView.findViewById(R.id.thumbnail);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FieldTimeActivity.class);
                    Promotion promotion = (Promotion) itemView.getTag(R.id.card_view);
                    intent.putExtra("field_id", promotion.getField().getId());
                    intent.putExtra("field_name", promotion.getField().getFieldName());
                    intent.putExtra("field_address", promotion.getField().getAddress());
                    intent.putExtra("user_id", userId);
                    context.startActivity(intent);
                }
            });
        }
    }
}