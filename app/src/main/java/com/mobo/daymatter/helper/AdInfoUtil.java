package com.mobo.daymatter.helper;

import com.mobo.daymatter.BuildConfig;
import com.mobo.daymatter.R;
import com.mobo.daymatter.adcontroller.EventController;

import ad.mobo.base.bean.PullInfos;
import ad.mobo.base.view.AbsNativeDisplayView;
import ad.mobo.base.view.AdViewIdsHolder;
import ad.mobo.base.view.NativeControllerLayout;
import ad.mobo.common.request.AdRequestHelper;

public final class AdInfoUtil {
    private AdInfoUtil() {
    }

    private static final int BASE_NATIVE = 1001;
    private static final NativeInfo[] sNativeInfo = {
            new NativeInfo("ca-app-pub-3707640778474213/8380908235", "548840219402418_548840536069053"),//原生
            new NativeInfo("ca-app-pub-3707640778474213/4441663225", "548840219402418_548842552735518"),//原生
            new NativeInfo("ca-app-pub-3707640778474213/8928784714", "548840219402418_548843782735395"),//原生
            new NativeInfo("ca-app-pub-3707640778474213/4748119674", "548840219402418_575010946785345"),//原生
            new NativeInfo("ca-app-pub-3707640778474213/8463717998", "548840219402418_575011246785315"),//原生

    };

    /**
     * @param relatedId 相对于BASE_NATIVE的值
     * @return
     */
    public static PullInfos getAdsIdInfo(int relatedId) {
        if (relatedId < sNativeInfo.length) {
            return sNativeInfo[relatedId].get(relatedId + BASE_NATIVE);
        }
        return null;
    }

    /**
     * 原生插页广告id存在一起，此方法用作区分处理，避免广告使用使用错乱
     *
     * @return
     */
    public static int[] getNoTypeAdsId() {
        return new int[]{3};
    }

    private static class NativeInfo {
        private static final String DEBUG = "ca-app-pub-3940256099942544/8691691433";

        public NativeInfo(String admob, String facebook) {
            if (!BuildConfig.DEBUG) {
                this.admob = admob;
            }
            this.facebook = facebook;
        }

        String admob = DEBUG;
        String facebook;


        public PullInfos get(int adId) {
            return AdRequestHelper.getPullInfos(adId + "", 1,
                    AdRequestHelper.getAdInfos(admob, facebook, "", "", adId + ""));
        }
    }

    public static void initNativeView(NativeControllerLayout view, String grayKey, boolean defValue) {
        if (view == null || view.getContext() == null) return;
        view.setIntercepter(new EventController(view, grayKey, defValue));
        view.inflateLayout(R.layout.mediation_native_ad_base_layout, getDefaultAdIdsHolder());
    }

    public static AdViewIdsHolder getAdIdsHolder(int titleId, int iconId, int desId, int contentId, int tagid, int choiceId, int ctaId) {
        AdViewIdsHolder holder = new AdViewIdsHolder();
        holder.setTitleId(titleId);
        holder.setCtaId(ctaId);
        holder.setIconId(iconId);
        holder.setDescriptionId(desId);
        holder.setContentId(contentId);
        holder.setTagId(tagid);
        holder.setChoiceId(choiceId);
        return holder;
    }

    public static AdViewIdsHolder getDefaultAdIdsHolder() {
        return getAdIdsHolder(R.id.ad_title, R.id.ad_icon, R.id.ad_desc,
                R.id.ad_banner_container, R.id.ad_tag,
                R.id.ad_choice_container, R.id.ad_btn);
    }
}
