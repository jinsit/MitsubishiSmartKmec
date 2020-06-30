package com.jinsit.kmec.IR.PI;

public class PartsHeaderItem {
    private String PartsDt; //청구일자(currentDAte)
    private String MatDueDt; //자재요청일자
    private String PartsCd;
    private String RefContrNo;
    private String FreeCd1;
    private String FreeCd2;
    private String PartsDeptCd;
    private String PartsEmpId;

    public PartsHeaderItem() {
    }

    public PartsHeaderItem(String partsDt, String  matDueDt, String partsCd, String refContrNo, String freeCd1, String freeCd2, String partsDeptCd, String partsEmpId) {
        PartsDt = partsDt;
        MatDueDt = matDueDt;
        PartsCd = partsCd;
        RefContrNo = refContrNo;
        FreeCd1 = freeCd1;
        FreeCd2 = freeCd2;
        PartsDeptCd = partsDeptCd;
        PartsEmpId = partsEmpId;
    }

    public String getPartsDt() {
        return PartsDt;
    }

    public void setPartsDt(String partsDt) {
        PartsDt = partsDt;
    }

    public String getPartsCd() {
        return PartsCd;
    }

    public void setPartsCd(String partsCd) {
        PartsCd = partsCd;
    }

    public String getRefContrNo() {
        return RefContrNo;
    }

    public void setRefContrNo(String refContrNo) {
        RefContrNo = refContrNo;
    }

    public String getFreeCd1() {
        return FreeCd1;
    }

    public void setFreeCd1(String freeCd1) {
        FreeCd1 = freeCd1;
    }

    public String getFreeCd2() {
        return FreeCd2;
    }

    public void setFreeCd2(String freeCd2) {
        FreeCd2 = freeCd2;
    }

    public String getPartsDeptCd() {
        return PartsDeptCd;
    }

    public void setPartsDeptCd(String partsDeptCd) {
        PartsDeptCd = partsDeptCd;
    }

    public String getPartsEmpId() {
        return PartsEmpId;
    }

    public void setPartsEmpId(String partsEmpId) {
        PartsEmpId = partsEmpId;
    }

    public String getMatDueDt() {
        return MatDueDt;
    }

    public void setMatDueDt(String matDueDt) {
        MatDueDt = matDueDt;
    }
}
