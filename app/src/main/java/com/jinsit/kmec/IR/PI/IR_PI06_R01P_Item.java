package com.jinsit.kmec.IR.PI;

public class IR_PI06_R01P_Item extends PartsItem {

    private String pItemNo;
    private String pItemNm;
    private String itemNm2;
    private String imageYN;

    public IR_PI06_R01P_Item() {
    }

    public IR_PI06_R01P_Item(String pItemNo, String pItemNm, String itemId, String carNo, String itemNm, String size, String quantity, String unitPrice, String drawNo, String glNo, String itemNm2) {
        this.setCarNo(carNo);
        this.setItemId(itemId);
        this.setItemNm(itemNm);
        this.setSize(size);
        this.setQuantity(quantity);
        this.setUnitPrice(unitPrice);
        this.setDrawNo(drawNo);
        this.setGlNo(glNo);
        this.pItemNo = pItemNo;
        this.pItemNm = pItemNm;
        this.itemNm2 = itemNm2;
    }

    public IR_PI06_R01P_Item(String pItemNo, String pItemNm, String itemId, String itemNm, String size, String drawNo, String glNo, String unitPrice, String itemNm2, String imageYN) {
        this.pItemNo = pItemNo;
        this.pItemNm = pItemNm;
        this.setItemId(itemId);
        this.setItemNm(itemNm);
        this.setSize(size);
        this.setUnitPrice(unitPrice);
        this.setDrawNo(drawNo);
        this.setGlNo(glNo);

        this.itemNm2 = itemNm2;
        this.imageYN = imageYN;
    }

    public String getpItemNo() {
        return pItemNo;
    }

    public void setpItemNo(String pItemNo) {
        this.pItemNo = pItemNo;
    }

    public String getpItemNm() {
        return pItemNm;
    }

    public void setpItemNm(String pItemNm) {
        this.pItemNm = pItemNm;
    }

    public String getItemNm2() {
        return itemNm2;
    }

    public void setItemNm2(String itemNm2) {
        itemNm2 = itemNm2;
    }

    public String getImageYN() {
        return imageYN;
    }

    public void setImageYN(String imageYN) {
        this.imageYN = imageYN;
    }
}
