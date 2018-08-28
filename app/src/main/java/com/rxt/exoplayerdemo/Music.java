package com.rxt.exoplayerdemo;

import android.net.Uri;

/**
 * Desc:
 * Company: xuehai
 * Copyright: Copyright (c) 2018
 *
 * @author raoxuting
 * @since 2018/08/28 15/46
 */
public class Music {
    private String name;
    private Uri musicUri;

    public Music(String name, Uri musicUri) {
        this.name = name;
        this.musicUri = musicUri;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri getMusicUri() {
        return musicUri;
    }

    public void setMusicUri(Uri musicUri) {
        this.musicUri = musicUri;
    }
}
