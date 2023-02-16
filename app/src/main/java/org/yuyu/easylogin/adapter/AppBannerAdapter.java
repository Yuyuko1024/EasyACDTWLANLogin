package org.yuyu.easylogin.adapter;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhpan.bannerview.BaseBannerAdapter;
import com.zhpan.bannerview.BaseViewHolder;

import org.yuyu.easylogin.R;
import org.yuyu.easylogin.bean.BannerDataBean;

public class AppBannerAdapter extends BaseBannerAdapter<BannerDataBean> {

    @Override
    protected void bindData(BaseViewHolder<BannerDataBean> holder, BannerDataBean data, int position, int pageSize) {
        ImageView bannerView = holder.findViewById(R.id.banner_image);
        TextView bannerTitle = holder.findViewById(R.id.title);
        Glide.with(bannerView)
                .load(data.getImgRes())
                .placeholder(R.drawable.emmmm)
                .into(bannerView);
        bannerView.setContentDescription(data.getImgDesc());
        bannerTitle.setText(data.getImgTitle());
        holder.setOnClickListener(R.id.banner_image, v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            String url = data.getImgUrl();
            if (url != null){
                intent.setData(Uri.parse(url));
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.banner_item;
    }
}
