package com.jinsit.kmec.IR.PI;

public class PartsItem {

    private String CarNo;
    private String ItemId;
    private String ItemNm;
    private String Size;
    private String Quantity;
    private String UnitPrice;
    private String DrawNo;
    private String GlNo;
    private String Amt;

    public PartsItem() {
    }

    public PartsItem(String carNo, String itemId, String itemNm, String size, String quantity, String unitPrice, String drawNo, String glNo) {
        CarNo = carNo;
        ItemId = itemId;
        ItemNm = itemNm;
        Size = size;
        Quantity = quantity;
        UnitPrice = unitPrice;
        DrawNo = drawNo;
        GlNo = glNo;
    }

    public PartsItem(IR_PI06_R01P_Item item, String carNo) {
        CarNo = carNo;
        ItemId = item.getItemId();
        ItemNm = item.getItemNm();
        Size = item.getSize();
        //Quantity = item.getQuantity();
        Quantity = "1";
        UnitPrice = item.getUnitPrice();
        DrawNo = item.getDrawNo();
        GlNo = item.getGlNo();
    }

    public String getCarNo() {
        return CarNo;
    }

    public void setCarNo(String carNo) {
        CarNo = carNo;
    }

    public String getItemId() {
        return ItemId;
    }

    public void setItemId(String itemId) {
        ItemId = itemId;
    }

    public String getItemNm() {
        return ItemNm;
    }

    public void setItemNm(String itemNm) {
        ItemNm = itemNm;
    }

    public String getSize() {
        return Size;
    }

    public void setSize(String size) {
        Size = size;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getUnitPrice() {
        return UnitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        UnitPrice = unitPrice;
    }

    public String getDrawNo() {
        return DrawNo;
    }

    public void setDrawNo(String drawNo) {
        DrawNo = drawNo;
    }

    public String getGlNo() {
        return GlNo;
    }

    public void setGlNo(String glNo) {
        GlNo = glNo;
    }


    public String getAmt(String quantity, String unitPrice) {
        return Integer.toString(Integer.parseInt(quantity) * Integer.parseInt(unitPrice));
    }


}
