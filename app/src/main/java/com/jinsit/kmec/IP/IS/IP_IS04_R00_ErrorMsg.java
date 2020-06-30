package com.jinsit.kmec.IP.IS;

public class IP_IS04_R00_ErrorMsg {
    private String Msg_Id;
    private String Msg_Desc;

    public IP_IS04_R00_ErrorMsg(String Msg_Id, String Msg_Desc) {
        this.Msg_Id = Msg_Id;
        this.Msg_Desc = Msg_Desc;
    }

    public String getMsg_Id() {
        return Msg_Id;
    }
    public void setMsg_Id(String Msg_id) {
        this.Msg_Id = Msg_id;
    }
    public String getMsg_Desc() {
        return Msg_Desc;
    }
    public void setMsg_Desc(String Msg_Desc) {
        this.Msg_Desc = Msg_Desc;
    }

}
