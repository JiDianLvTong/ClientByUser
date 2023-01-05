package com.android.jidian.client.mvp.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.jidian.client.R;

import java.util.List;

/**
 * @author : PTT
 * date: 2020/7/28 17:38
 * company: 兴达智联
 * description:
 */
public class MainSceneDialogSecondAdapter extends RecyclerView.Adapter<MainSceneDialogSecondAdapter.ViewHolder> {
    private List<String> data;

    public MainSceneDialogSecondAdapter(List<String> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_main_scene_dialog_second_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvContent.setText(((position + 1) + ". " + data.get(position)));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvContent;

        public ViewHolder(View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tv_item_msds_content);
        }
    }
}