package com.android.jidian.repair.mvp.FailureAdd;

import android.opengl.Visibility;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.android.jidian.repair.R;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @author : xiaoming
 * date: 2023/2/2 15:45
 * description:
 */
public class FailureImgAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private OnClickListener mListener;

    public FailureImgAdapter() {
        super(R.layout.item_image_view);
    }

    @Override
    protected void convert(BaseViewHolder holder, String s) {
        if (TextUtils.isEmpty(s)) {
            holder.setGone(R.id.delectImage, false);
            holder.setGone(R.id.addImage, true);
            holder.setGone(R.id.imageImage, false);
        } else {
            holder.setGone(R.id.delectImage, true);
            holder.setGone(R.id.addImage, false);
            holder.setGone(R.id.imageImage, true);
            Glide.with(mContext).load(s).into(((ImageView) holder.getView(R.id.imageImage)));
        }
        holder.getView(R.id.delectImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onClickDelete(holder.getAdapterPosition());
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onClickItem(holder.getAdapterPosition());
                }
            }
        });
    }

    public void setListener(OnClickListener listener) {
        mListener = listener;
    }

    public interface OnClickListener {
        void onClickDelete(int position);

        void onClickItem(int position);

    }
}
