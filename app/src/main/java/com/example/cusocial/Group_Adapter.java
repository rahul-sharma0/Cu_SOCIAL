package com.example.cusocial;

public class Group_Adapter {
//    String Group_id;
    String Group_name;

    public Group_Adapter( String group_name) {
//        Group_id = group_id;
        Group_name = group_name;
    }

    public Group_Adapter() {
    }

       public String getGroup_name() {
        return Group_name;
    }

    public void setGroup_name(String group_name) {
        Group_name = group_name;
    }
}
