package com.rxt.exoplayerdemo;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Desc:
 * Company: xuehai
 * Copyright: Copyright (c) 2018
 *
 * @author raoxuting
 * @since 2018/08/24 01/04
 */
public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.MusicListHolder> {

    private List<Music> musicList;

    public void setMusicList(List<Music> musicList) {
        this.musicList = musicList;
    }

    public interface OnItemClickListener {

        void onItemClick(Uri musicUri);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public MusicListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView textView = new TextView(parent.getContext());
        textView.setPadding(30, 10, 30, 10);
        textView.setTextSize(12);
        textView.setTextColor(parent.getResources().getColor(android.R.color.black));
        int measuredWidth = parent.getMeasuredWidth();
        textView.setWidth(measuredWidth);
        return new MusicListHolder(textView);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicListHolder holder, int position) {
        final Music music = musicList.get(position);
        ((TextView)holder.itemView).setText(music.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(music.getMusicUri());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return musicList == null ? 0 : musicList.size();
    }

    static class MusicListHolder extends RecyclerView.ViewHolder {

        public MusicListHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
