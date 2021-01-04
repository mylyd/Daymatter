package com.mobo.daymatter.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mobo.daymatter.beans.PunchClockBean;
import com.mobo.daymatter.beans.PunchClockRecorder;
import com.mobo.daymatter.beans.PunchClockTargetMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 打卡数据管理；由于打卡记录可能很多，单独开一个sp来存储;
 * 1、打卡记录的查询、增加；依据打卡类型进行存储
 * a) 以 tostring() 为key值、getStoreKey() 为value值，记录每天打卡key值，以每天打卡key记录具体记录
 * b) 以getStoreKey() 为key值，以具体记录为value值
 * 2、打卡详情的增删改查
 */
public class PunchClockManager {
    private static final String SP_NAME = "punchClock";
    private static final String PUNCH_ARCHIVE_LIST = "punch_archive_list";
    private static final String DETAIL_LIST = "detail_list"; // 打卡列表
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private ArrayList<PunchClockBean> mDetailList; // 由于列表信息会不停查询，所以做缓存

    private PunchClockManager() {
    }

    public static PunchClockManager get() {
        return Inner.sManager;
    }

    public PunchClockManager init(Context context) {
        if (context == null || mSharedPreferences != null) {
            return this;
        }
        mSharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        String beans = mSharedPreferences.getString(DETAIL_LIST, "");
        if (TextUtils.isEmpty(beans)) {
            mDetailList = new ArrayList<>();
        } else {
            mDetailList = new Gson().fromJson(beans, new TypeToken<ArrayList<PunchClockBean>>() {
            }.getType());
        }
        return this;
    }

    // 获取打卡详情列表
    public ArrayList<PunchClockBean> getDetailData() {
        return mDetailList;
    }

    /**
     * 增加打卡详情
     *
     * @param bean
     * @param isCommit
     */
    public void addDetailData(PunchClockBean bean, boolean isCommit) {
        if (bean != null) {
            mDetailList.add(bean);
            mEditor.putString(DETAIL_LIST, new Gson().toJson(mDetailList, new TypeToken<ArrayList<PunchClockBean>>() {
            }.getType()));
            if (isCommit) {
                mEditor.apply();
            }
        }
    }

    /**
     * 删除打卡详情
     *
     * @param bean
     * @param isCommit
     */
    public void deleteDetailData(PunchClockBean bean, boolean isCommit) {
        if (bean != null) {
            int index = -1;
            for (int i = 0; i < mDetailList.size(); i++) {
                if (mDetailList.get(i).equals(bean)) {
                    index = i;
                    break;
                }
            }
            if (index != -1) {
                mDetailList.remove(index);
                mEditor.putString(DETAIL_LIST, new Gson().toJson(mDetailList, new TypeToken<ArrayList<PunchClockBean>>() {
                }.getType()));
                if (isCommit) {
                    mEditor.apply();
                }
            }
        }
    }

    /**
     * 修改打卡详情
     *
     * @param old
     * @param bean
     */
    public void editDetailData(PunchClockBean old, PunchClockBean bean) {
        deleteDetailData(old, false);
        addDetailData(bean, true);
    }

    // 获取打卡信息对应的打卡记录索引
    public ArrayList<String> getRecorderKeys(PunchClockBean bean) {
        if (bean == null) {
            return null;
        }
        String key = bean.toString();
        String str = mSharedPreferences.getString(key, "");
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return new Gson().fromJson(str, new TypeToken<ArrayList<String>>() {
        }.getType());
    }

    // 获取所有的打卡记录
    public ArrayList<PunchClockRecorder> getAllRecorder(PunchClockBean bean) {
        List<String> keys = getRecorderKeys(bean);
        return getAllRecorder(keys);
    }

    // 获取所有的打卡记录
    public ArrayList<PunchClockRecorder> getRecorder(String key) {
        String str = mSharedPreferences.getString(key, "");
        if (!TextUtils.isEmpty(str)) {
            return new Gson().fromJson(str, new TypeToken<ArrayList<PunchClockRecorder>>() {
            }.getType());
        }
        return null;
    }

    /**
     * 依据key值来获取所有
     *
     * @param keys
     * @return
     */
    public ArrayList<PunchClockRecorder> getAllRecorder(List<String> keys) {
        if (keys == null || keys.isEmpty()) {
            return null;
        }
        ArrayList<PunchClockRecorder> beans = new ArrayList<>();
        ArrayList<PunchClockRecorder> temp;
        for (String key : keys) {
            String str = mSharedPreferences.getString(key, "");
            if (!TextUtils.isEmpty(str)) {
                temp = new Gson().fromJson(str, new TypeToken<ArrayList<PunchClockRecorder>>() {
                }.getType());
                if (temp == null || temp.isEmpty()) {
                    continue;
                }
                beans.addAll(temp);
            }
        }
        return beans;
    }

    /**
     * 获取打卡记录,依据打卡重复类型来获取相应天/周所有记录
     *
     * @param bean
     * @return
     */
    public ArrayList<PunchClockRecorder> getRecorderByMode(PunchClockBean bean) {
        if (bean == null) {
            return null;
        }
        String key = bean.getStoreKey();
        String str = mSharedPreferences.getString(key, "");
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return new Gson().fromJson(str, new TypeToken<ArrayList<PunchClockRecorder>>() {
        }.getType());
    }

    // 添加归档
    public void addArchive(PunchClockBean bean, boolean isCommit) {
        deleteDetailData(bean, false);
        String str = mSharedPreferences.getString(PUNCH_ARCHIVE_LIST, "");
        ArrayList<PunchClockBean> arrayList;
        if (TextUtils.isEmpty(str)) {
            arrayList = new ArrayList<>();
        } else {
            arrayList = new Gson().fromJson(str, new TypeToken<ArrayList<PunchClockBean>>() {
            }.getType());
        }
        arrayList.add(bean);
        mEditor.putString(PUNCH_ARCHIVE_LIST, new Gson().toJson(arrayList, new TypeToken<ArrayList<PunchClockBean>>() {
        }.getType()));
        if (isCommit) {
            mEditor.apply();
        }
    }

    public ArrayList<PunchClockBean> getArchives() {
        String str = mSharedPreferences.getString(PUNCH_ARCHIVE_LIST, "");
        if (!TextUtils.isEmpty(str)) {
            return new Gson().fromJson(str, new TypeToken<ArrayList<PunchClockBean>>() {
            }.getType());
        }
        return null;
    }

    /**
     * 添加打卡记录,依据打卡重复类型来获取;
     * 如果是不重复，且满足当前次数，则放置在归档任务中
     *
     * @param bean
     * @return
     */
    public void addRecorder(PunchClockBean bean, PunchClockRecorder recorder, boolean isCommit) {
        if (bean == null || recorder == null) {
            return;
        }

        String storeKey = bean.getStoreKey();
        String tempKey = bean.toString();

        ArrayList<String> temp = getRecorderKeys(bean);
        if (temp == null) {
            temp = new ArrayList<>();
            temp.add(storeKey);
            mEditor.putString(tempKey, new Gson().toJson(temp, new TypeToken<ArrayList<String>>() {
            }.getType()));
        } else {
            if (!temp.contains(storeKey)) {
                temp.add(storeKey);
                mEditor.putString(tempKey, new Gson().toJson(temp, new TypeToken<ArrayList<String>>() {
                }.getType()));
            }
        }

        ArrayList<PunchClockRecorder> recorders = getRecorderByMode(bean);
        if (recorders == null) {
            recorders = new ArrayList<>();
        }
        recorders.add(recorder);
        mEditor.putString(bean.getStoreKey(), new Gson().toJson(recorders, new TypeToken<ArrayList<PunchClockRecorder>>() {
        }.getType()));

        if (bean.getRepeatMode() == PunchClockBean.REPEAT_ONE_TIMES) {
            PunchClockTargetMode mode = bean.getTargetMode();
            ArrayList<PunchClockRecorder> allRecorders = getAllRecorder(temp);
            int size = allRecorders == null ? 1 : (allRecorders.size() + 1);
            //没有目标，就是任务可以一直循环，不会自动归档
            if (mode.getMode() == PunchClockTargetMode.MODE_NONE) {
                //addArchive(bean, false);
            } else if (mode.getMode() == PunchClockTargetMode.MODE_COUNT && mode.getCount() <= size) {
                addArchive(bean, false);
            } else if (mode.getMode() == PunchClockTargetMode.MODE_TIMES && mode.getTimes() <= size * bean.getTime()) {
                addArchive(bean, false);
            }
        }

        if (isCommit) {
            mEditor.apply();
        }
    }

    private static class Inner {
        private static final PunchClockManager sManager = new PunchClockManager();
    }
}
