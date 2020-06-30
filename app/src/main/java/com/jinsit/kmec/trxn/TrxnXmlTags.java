package com.jinsit.kmec.trxn;

public class TrxnXmlTags {


    //trxn root
    public static final String TRXN_ROOT = "Trxn";
    public static final String TRXN_ATTR_istestmode = "IsTestMode";
    public static final String TRXN_ATTR_companyno = "CompanyNo";
    public static final String TRXN_ATTR_storeno = "StoreNo";
    public static final String TRXN_ATTR_posno = "PosNo";
    public static final String TRXN_ATTR_saledate = "SaleDate";
    public static final String TRXN_ATTR_trxnno = "TrxnNo";
    public static final String TRXN_ATTR_trxntype = "TrxnType";
    public static final String TRXN_ATTR_dealtype = "DealType";
    public static final String TRXN_ATTR_saletime = "SaleTime";

    public static final String TRXN_ELE_casher =  "Casher";
    public static final String TRXN_ELE_CASHER_ATTR_id ="Id";
    public static final String TRXN_ELE_CASHER_ATTR_name = "Name";

    //부품헤더정보
    public static final String TRXN_ELE_PARTSHEADER = "PartsHaeder";
    public static final String TRXN_ELE_PARTSHEADER_ATTR_PARTS_DT = "PartsDt";
    public static final String TRXN_ELE_PARTSHEADER_ATTR_MAT_DU_DT = "MatDueDt";   //자재요청일자
    public static final String TRXN_ELE_PARTSHEADER_ATTR_PARTS_CD = "PartsCd";
    public static final String TRXN_ELE_PARTSHEADER_ATTR_REFCONTR_NO = "RefContrNo";
    public static final String TRXN_ELE_PARTSHEADER_ATTR_FREE_CD1 = "FreeCd1";
    public static final String TRXN_ELE_PARTSHEADER_ATTR_FREE_CD2 = "FreeCd2";
    public static final String TRXN_ELE_PARTSHEADER_ATTR_PARTS_DEPT_CD = "PartsDeptCd";
    public static final String TRXN_ELE_PARTSHEADER_ATTR_EMP_ID = "PartsEmpId";

    //부품정보
    public static final String TRXN_ELE_PARTSITEMS = "PartsItems";
    public static final String TRXN_ELE_PARTSITEMS_ELE_PARTSITEM = "PartsItem";
    public static final String TRXN_ELE_PARTSITEM_ATTR_CAR_NO = "CarNo";
    public static final String TRXN_ELE_PARTSITEM_ATTR_ITEM_ID = "ItemId";
    public static final String TRXN_ELE_PARTSITEM_ATTR_ITEM_NM = "ItemNm";
    public static final String TRXN_ELE_PARTSITEM_ATTR_SIZE = "Size";
    public static final String TRXN_ELE_PARTSITEM_ATTR_QUANTITY = "Quantity";
    public static final String TRXN_ELE_PARTSITEM_ATTR_UNITPRICE = "UnitPrice";
    public static final String TRXN_ELE_PARTSITEM_ATTR_DRAW_NO = "DrawNo";
    public static final String TRXN_ELE_PARTSITEM_ATTR_GLNO = "GlNo";

    //승관원 점검 체크표
    public static final String TRXN_ELE_MNG_CHECK_HEADER = "Header";
    public static final String TRXN_ELE_MNG_CHECK_ITEM = "CheckItems";
    public static final String TRXN_ELE_MNG_CHECK_ITEM_ELE_DETAIL = "Detail";

    //작업선택
    public static final String TRXN_WORK_SELECTION_ROOT = "ROOT";
    public static final String TRXN_ELE_WORK_SELECTION_HEADER = "MASTER";
    public static final String TRXN_ELE_WORK_SELECTION_ITEM = "DETAIL";
    public static final String TRXN_ELE_WORK_SELECTION_ITEM_ELE_DETAIL = "WORK";
}
