package com.rxt.exoplayerdemo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Desc:
 * Company: xuehai
 * Copyright: Copyright (c) 2018
 *
 * @author raoxuting
 * @since 2018/08/24 01/04
 */
public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.MusicListHolder> {

    private List<String> musicList;

    public void setMusicList(List<String> musicList) {
        this.musicList = musicList;
    }

    @NonNull
    @Override
    public MusicListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView textView = new TextView(parent.getContext());
        textView.setPadding(30, 10, 30, 10);
        textView.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12,
                parent.getResources().getDisplayMetrics()));
        textView.setTextColor(parent.getResources().getColor(android.R.color.black));
        return new MusicListHolder(textView);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicListHolder holder, int position) {
        ((TextView)holder.itemView).setText(musicList.get(position));
    }

    @Override
    public int getItemCount() {
        return musicList == null ? 0 : musicList.size();
    }

    static class MusicListHolder extends RecyclerView.ViewHolder {

        public MusicListHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
