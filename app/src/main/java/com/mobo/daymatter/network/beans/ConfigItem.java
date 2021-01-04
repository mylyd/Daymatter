package com.mobo.daymatter.network.beans;

public class ConfigItem {
    private int status;
    private String msg;
    private ConfigBean data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ConfigBean getData() {
        return data;
    }

    public void setData(ConfigBean data) {
        this.data = data;
    }

    public static class ConfigBean {
        private int versioncode;
        private String updateTint;

        public int getVersioncode() {
            return versioncode;
        }

        public void setVersioncode(int versioncode) {
            this.versioncode = versioncode;
        }

        public String getUpdateTint() {
            return updateTint;
        }

        public void setUpdateTint(String updateTint) {
            this.updateTint = updateTint;
        }
    }
}
