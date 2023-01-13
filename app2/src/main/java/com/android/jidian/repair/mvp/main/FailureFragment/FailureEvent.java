package com.android.jidian.repair.mvp.main.FailureFragment;

import com.luck.picture.lib.entity.LocalMedia;

/**
 * @author : xiaoming
 * date: 2023/1/11 17:40
 * description:
 */
public class FailureEvent {
    public static int FAILURE_TAKE_PHOTO = 1;
    private int event;
    private LocalMedia media;

    public FailureEvent(int event, LocalMedia media) {
        this.event = event;
        this.media = media;
    }

    public int getEvent() {
        return event;
    }

    public void setEvent(int event) {
        this.event = event;
    }

    public LocalMedia getMedia() {
        return media;
    }

    public void setMedia(LocalMedia media) {
        this.media = media;
    }
}
