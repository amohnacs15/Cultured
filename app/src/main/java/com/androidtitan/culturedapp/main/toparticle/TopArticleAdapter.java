package com.androidtitan.culturedapp.main.toparticle;

import android.content.Context;
import android.graphics.Bitmap;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.androidtitan.culturedapp.R;
import com.androidtitan.culturedapp.common.view.TopArticleHeaderLayout;
import com.androidtitan.culturedapp.model.newyorktimes.Article;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Adrian Mohnacs on 12/10/16.
 */

public class TopArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final String TAG = getClass().getSimpleName();

    private Context context;
    public List<Article> articleList;

    private WeakReference<TopArticleAdapter.OnClick> weakOnClick;


    public TopArticleAdapter(OnClick callback, Context context, List<Article> articleList) {
        this.context = context;
        this.articleList = articleList;
        this.weakOnClick = new WeakReference<>(callback);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View v1 = inflater.inflate(R.layout.top_article_row_layout_xlarge, parent, false);
        viewHolder = new XLargeArticleViewHolder(v1);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        XLargeArticleViewHolder holder2 = (XLargeArticleViewHolder) holder;
        initViewHolder(holder2, articleList.get(position));
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    private void initViewHolder(final XLargeArticleViewHolder holder, Article article) {

        DateFormat formatter = new SimpleDateFormat("MMM dd h:mm a");
        final String dateFormatted = formatter.format(article.getCreatedDate());

        try {
            Glide.with(context)
                    .load(article.getMultimedia().get(0).getUrl())
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .animate(R.anim.fade_in)
                    .placeholder(R.drawable.im_placeholder)
                    .into(new BitmapImageViewTarget(((TopArticleAdapter.XLargeArticleViewHolder) holder).articleImage) {
                        @Override
                        public void onResourceReady(final Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            super.onResourceReady(resource, glideAnimation);

                            holder.topArticleHeaderLayout.setGradientViewHeight(holder.articleImage);
                            holder.topArticleHeaderLayout.setTitleText(article.getTitle());
                            holder.topArticleHeaderLayout.setAbstractText(article.getAbstract());
                            holder.topArticleHeaderLayout.setDateText(dateFormatted);

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();

            if (holder.topArticleHeaderLayout != null) {
                holder.topArticleHeaderLayout.setGradientViewHeight(holder.articleImage);
            }
            holder.topArticleHeaderLayout.setTitleText(article.getTitle());
            holder.topArticleHeaderLayout.setAbstractText(article.getAbstract());
            //holder.topArticleHeaderLayout.setSectionText(articleList.get(position).getGeoFacet().get(0));
            holder.topArticleHeaderLayout.setDateText(dateFormatted);

        }

        if(article.getGeoFacet() != null) {
            if (article.getGeoFacet().size() > 0) {
                holder.topArticleHeaderLayout.setSectionText(article.getGeoFacet().get(0).getFacetText());
            }
        }

        holder.clickLayout.setOnClickListener(v -> {
            if (weakOnClick.get() != null) {
                weakOnClick.get().sendDetailActivity(article, holder.articleImage);
            }
        });

    }

    public static class XLargeArticleViewHolder extends RecyclerView.ViewHolder {

        @Nullable
        @BindView(R.id.rippleForeground)
        RelativeLayout clickLayout;
        @Nullable
        @BindView(R.id.articleImageView)
        ImageView articleImage;
        @Nullable
        @BindView(R.id.newsHeaderLayout)
        TopArticleHeaderLayout topArticleHeaderLayout;

        public XLargeArticleViewHolder(View itemView) {
            super(itemView);
            try {
                ButterKnife.bind(this, itemView);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    public interface OnClick {
        void sendDetailActivity(Article article, ImageView imageView);
    }
}
