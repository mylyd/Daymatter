package com.mobo.daymatter.adcontroller;

import android.view.View;

/**
 * 广告布局缓存
 * created by tliu
 * 2019-09-29
 */
public class AdViewHolder {
    private View titleView, iconView, mediaView, ctaView, decView, tagView, choiceView;

    public AdViewHolder(View rootView, int titleId, int decId, int iconId, int ctaId, int choiceId, int tagId, int mediaId) {
        titleView = rootView.findViewById(titleId);
        decView = rootView.findViewById(decId);
        iconView = rootView.findViewById(iconId);
        ctaView = rootView.findViewById(ctaId);
        choiceView = rootView.findViewById(choiceId);
        tagView = rootView.findViewById(tagId);
        mediaView = rootView.findViewById(mediaId);
    }

    public AdViewHolder(View titleView, View iconView, View mediaView, View ctaView, View decView, View tagView, View choiceView) {
        this.tagView = tagView;
        this.titleView = titleView;
        this.iconView = iconView;
        this.mediaView = mediaView;
        this.ctaView = ctaView;
        this.decView = decView;
        this.choiceView = choiceView;
        this.mediaView = mediaView;
    }

    public View getTitleView() {
        return titleView;
    }

    public View getIconView() {
        return iconView;
    }

    public View getMediaView() {
        return mediaView;
    }

    public View getCtaView() {
        return ctaView;
    }

    public View getDecView() {
        return decView;
    }

    public View getTagView() {
        return tagView;
    }

    public View getChoiceView() {
        return choiceView;
    }
}
