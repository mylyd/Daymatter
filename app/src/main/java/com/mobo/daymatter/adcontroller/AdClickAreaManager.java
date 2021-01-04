package com.mobo.daymatter.adcontroller;

public class AdClickAreaManager {
    public AdClickAreaManager(AdViewHolder holder) {
        this.holder = holder;
    }

    private AdClickAreaHelper areaHelper = AdClickAreaHelper.getInstance();
    private AdViewHolder holder = null;

    public void begainPoint(float x, float y) {
        areaHelper.initEventPos(x, y);
    }

    public void checkArea() {
        if (holder == null) {
            return;
        }
        if (!areaHelper.checkChoice(holder.getChoiceView())
                && !areaHelper.checkCta(holder.getCtaView())
                && !areaHelper.checkTitle(holder.getTitleView())
                && !areaHelper.checkDec(holder.getDecView())
                && !areaHelper.checkIcon(holder.getIconView())
                && !areaHelper.checkTag(holder.getTagView())
                && !areaHelper.checkImg(holder.getMediaView())) {
            areaHelper.resetUnkown();
        }
    }

    public String getClickArea() {
        return areaHelper.getClickArea();
    }

    public boolean isCtaArea() {
        return areaHelper.isCtaArea();
    }

    public boolean isChoiceArea() {
        return areaHelper.isChoiceArea();
    }

    public boolean isMediaArea() {
        return areaHelper.isMediaArea();
    }
}
