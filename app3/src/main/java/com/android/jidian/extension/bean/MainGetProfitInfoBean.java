package com.android.jidian.extension.bean;

import java.util.ArrayList;

public class MainGetProfitInfoBean {

    private ArrayList<Lists> lists;
    private String incomet;
    private String montht;

    public String getIncomet() {
        return incomet;
    }

    public void setIncomet(String incomet) {
        this.incomet = incomet;
    }

    public String getMontht() {
        return montht;
    }

    public void setMontht(String montht) {
        this.montht = montht;
    }

    public ArrayList<Lists> getLists() {
        return lists;
    }

    public void setLists(ArrayList<Lists> lists) {
        this.lists = lists;
    }

    public class Lists{
        private String id;
        private String real_name;
        private String phone;
        private String income;
        private String indate;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getReal_name() {
            return real_name;
        }

        public void setReal_name(String real_name) {
            this.real_name = real_name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getIncome() {
            return income;
        }

        public void setIncome(String income) {
            this.income = income;
        }

        public String getIndate() {
            return indate;
        }

        public void setIndate(String indate) {
            this.indate = indate;
        }
    }
}
