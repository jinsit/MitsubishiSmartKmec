package com.jinsit.kmec.trxn;

import com.jinsit.kmec.DM.DM.DM_DM05_R01P_Detail01;
import com.jinsit.kmec.DM.DM.DM_DM05_R01P_Detail02;
import com.jinsit.kmec.DM.DM.DM_DM05_R01P_Header01;
import com.jinsit.kmec.DM.DM.DM_DM05_R01P_Header02;
import com.jinsit.kmec.IR.PI.PartsHeaderItem;
import com.jinsit.kmec.IR.PI.PartsItem;
import com.jinsit.kmec.WO.WT.RI.WO_WT00_R04_Detail;
import com.jinsit.kmec.WO.WT.RI.WO_WT00_R04_Header;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.XmlMaker;
import org.jdom.XmlParser;
import org.jdom.output.XMLOutputter;

public class TrxnDocument {

    private XmlMaker XmlMaker = new XmlMaker();
    private LinkedHashMap<String, Object> Trxn;

    public TrxnDocument() {
    }

    private Document getTrxn(Document doc) {
        Element element = doc.getRootElement();
        /*this.XmlMaker.addAttribute(element, TrxnXmlTags.TRXN_ATTR_companyno, this.getCompanyNo());
        this.XmlMaker.addAttribute(element, TrxnXmlTags.TRXN_ATTR_storeno, this.getStoreNo());
        this.XmlMaker.addAttribute(element, TrxnXmlTags.TRXN_ATTR_posno, this.getPosNo());
        this.XmlMaker.addAttribute(element, TrxnXmlTags.TRXN_ATTR_saledate, this.getSaleDate());
        this.XmlMaker.addAttribute(element, TrxnXmlTags.TRXN_ATTR_trxnno, this.getTrxnNo());
        this.XmlMaker.addAttribute(element, TrxnXmlTags.TRXN_ATTR_trxntype, this.getTrxnType().toString());
        this.XmlMaker.addAttribute(element, TrxnXmlTags.TRXN_ATTR_dealtype, this.getDealType().toString());*/
        //this.XmlMaker.addAttribute(element, TrxnXmlTags.TRXN_ATTR_saletime, this.getSaleTime());
        return doc;
    }

    private void setTrxn(Document doc) {
        Element element = doc.getRootElement();
        Attribute attr = element.getAttribute(TrxnXmlTags.TRXN_ATTR_istestmode);
        /*if (attr != null) this.setIsTestMode(this.Sysutil.stringToBoolean(attr.getValue()));
        attr = element.getAttribute(TrxnXmlTags.TRXN_ATTR_companyno);
        if (attr != null) this.setCompanyNo(attr.getValue());
        attr = element.getAttribute(TrxnXmlTags.TRXN_ATTR_storeno);
        if (attr != null) this.setStoreNo(attr.getValue());
        attr = element.getAttribute(TrxnXmlTags.TRXN_ATTR_posno);
        if (attr != null) this.setPosNo(attr.getValue());
        attr = element.getAttribute(TrxnXmlTags.TRXN_ATTR_saledate);
        if (attr != null) this.setSaleDate(attr.getValue());
        attr = element.getAttribute(TrxnXmlTags.TRXN_ATTR_trxnno);
        if (attr != null) this.setTrxnNo(this.Sysutil.stringToLong(attr.getValue()));
        attr = element.getAttribute(TrxnXmlTags.TRXN_ATTR_trxntype);
        if (attr != null) this.setTrxnType(this.Sysutil.stringToTrxnType(attr.getValue()));
        attr = element.getAttribute(TrxnXmlTags.TRXN_ATTR_dealtype);
        if (attr != null) this.setDealType(this.Sysutil.stringToDealType(attr.getValue()));
        attr = element.getAttribute(TrxnXmlTags.TRXN_ATTR_saletime);
        if (attr != null) this.setSaleTime(attr.getValue());*/
    }

    private Document getPartsHeader_Trxn(Document doc) {
        Element element= XmlMaker.addElement(doc, TrxnXmlTags.TRXN_ELE_PARTSHEADER);
        ChangeStringMap(element, TrxnXmlTags.TRXN_ELE_PARTSHEADER_ATTR_PARTS_DT,this.getPartsHeader().getPartsDt());
        ChangeStringMap(element, TrxnXmlTags.TRXN_ELE_PARTSHEADER_ATTR_MAT_DU_DT,this.getPartsHeader().getMatDueDt());
        ChangeStringMap(element, TrxnXmlTags.TRXN_ELE_PARTSHEADER_ATTR_PARTS_CD,this.getPartsHeader().getPartsCd());
        ChangeStringMap(element, TrxnXmlTags.TRXN_ELE_PARTSHEADER_ATTR_REFCONTR_NO,this.getPartsHeader().getRefContrNo());
        ChangeStringMap(element, TrxnXmlTags.TRXN_ELE_PARTSHEADER_ATTR_FREE_CD1,this.getPartsHeader().getFreeCd1());
        ChangeStringMap(element, TrxnXmlTags.TRXN_ELE_PARTSHEADER_ATTR_FREE_CD2,this.getPartsHeader().getFreeCd2());
        ChangeStringMap(element, TrxnXmlTags.TRXN_ELE_PARTSHEADER_ATTR_PARTS_DEPT_CD,this.getPartsHeader().getPartsDeptCd());
        ChangeStringMap(element, TrxnXmlTags.TRXN_ELE_PARTSHEADER_ATTR_EMP_ID,this.getPartsHeader().getPartsEmpId());

        return doc;
    }

    private Document getPartsItems_Trxn(Document doc) {
        if (getPartsItems().size() > 0) {
            Element element = XmlMaker.addElement(doc, TrxnXmlTags.TRXN_ELE_PARTSITEMS);
            for (int loc = 0; loc < this.getPartsItems().size(); loc++) {
                getPartItem_Trxn(element, loc);
            }
        }
        return doc;
    }

    private void getPartItem_Trxn(Element pElement, int loc) {
        Element element = XmlMaker.addElement(pElement, TrxnXmlTags.TRXN_ELE_PARTSITEMS_ELE_PARTSITEM);
        ChangeStringMap(element, TrxnXmlTags.TRXN_ELE_PARTSITEM_ATTR_CAR_NO, this.getPartsItems().get(loc).getCarNo());
        ChangeStringMap(element, TrxnXmlTags.TRXN_ELE_PARTSITEM_ATTR_ITEM_ID, this.getPartsItems().get(loc).getItemId());
        ChangeStringMap(element, TrxnXmlTags.TRXN_ELE_PARTSITEM_ATTR_ITEM_NM, this.getPartsItems().get(loc).getItemNm());
        ChangeStringMap(element, TrxnXmlTags.TRXN_ELE_PARTSITEM_ATTR_SIZE, this.getPartsItems().get(loc).getSize());
        ChangeStringMap(element, TrxnXmlTags.TRXN_ELE_PARTSITEM_ATTR_QUANTITY, this.getPartsItems().get(loc).getQuantity());
        ChangeStringMap(element, TrxnXmlTags.TRXN_ELE_PARTSITEM_ATTR_UNITPRICE, this.getPartsItems().get(loc).getUnitPrice());
        ChangeStringMap(element, TrxnXmlTags.TRXN_ELE_PARTSITEM_ATTR_DRAW_NO, this.getPartsItems().get(loc).getDrawNo());
        ChangeStringMap(element, TrxnXmlTags.TRXN_ELE_PARTSITEM_ATTR_GLNO, this.getPartsItems().get(loc).getGlNo());
    }

    private void setPartsItems(Element rootElement) {
        Element element = rootElement.getChild(TrxnXmlTags.TRXN_ELE_PARTSITEMS);
        setPartsItem(element);
    }

    private void setPartsItem(Element rootElement) {
        PartsItem partsItem;
        Attribute attr;
        List<Element> elements = rootElement.getChildren(TrxnXmlTags.TRXN_ELE_PARTSITEMS_ELE_PARTSITEM);
        for (Element element : elements) {
            partsItem = new PartsItem();
            attr = element.getAttribute(TrxnXmlTags.TRXN_ELE_PARTSITEM_ATTR_CAR_NO);
            if (attr != null)partsItem.setCarNo((attr.getValue()));
            attr = element.getAttribute(TrxnXmlTags.TRXN_ELE_PARTSITEM_ATTR_ITEM_ID);
            if (attr != null)partsItem.setItemId((attr.getValue()));
            attr = element.getAttribute(TrxnXmlTags.TRXN_ELE_PARTSITEM_ATTR_ITEM_NM);
            if (attr != null)partsItem.setItemNm((attr.getValue()));
            attr = element.getAttribute(TrxnXmlTags.TRXN_ELE_PARTSITEM_ATTR_SIZE);
            if (attr != null)partsItem.setSize((attr.getValue()));
            attr = element.getAttribute(TrxnXmlTags.TRXN_ELE_PARTSITEM_ATTR_QUANTITY);
            if (attr != null)partsItem.setQuantity((attr.getValue()));
            attr = element.getAttribute(TrxnXmlTags.TRXN_ELE_PARTSITEM_ATTR_UNITPRICE);
            if (attr != null)partsItem.setUnitPrice((attr.getValue()));
            attr = element.getAttribute(TrxnXmlTags.TRXN_ELE_PARTSITEM_ATTR_DRAW_NO);
            if (attr != null)partsItem.setDrawNo((attr.getValue()));
            attr = element.getAttribute(TrxnXmlTags.TRXN_ELE_PARTSITEM_ATTR_GLNO);
            if (attr != null)partsItem.setGlNo((attr.getValue()));
            this.PartsItems.add(partsItem);
        }
    }

    private Document getMngCheckHeader_Trxn(Document doc) {
        Element element= XmlMaker.addElement(doc, TrxnXmlTags.TRXN_ELE_MNG_CHECK_HEADER);
        ChangeStringMap(element, "CS_EMP_ID", this.getwO_WT00_R04_Header().getCS_EMP_ID());
        ChangeStringMap(element, "WORK_DT", this.getwO_WT00_R04_Header().getWORK_DT());
        ChangeStringMap(element, "JOB_NO", this.getwO_WT00_R04_Header().getJOB_NO());
        ChangeStringMap(element, "CS_TM_FR", this.getwO_WT00_R04_Header().getSEL_CHK_ST_DT());
        ChangeStringMap(element, "CS_TM_TO", this.getwO_WT00_R04_Header().getSEL_CHK_END_DT());
        ChangeStringMap(element, "ELEVATOR_NO", this.getwO_WT00_R04_Header().getELEVATOR_NO());
        ChangeStringMap(element, "SELCHK_USID", this.getwO_WT00_R04_Header().getSELCHK_USID());
        ChangeStringMap(element, "PATICULS", this.getwO_WT00_R04_Header().getPATICULS());
        return doc;
    }

    private Document getMngCheckItems_Trxn(Document doc) {
        if (getwO_WT00_R04_Details().size() > 0) {
            Element element = XmlMaker.addElement(doc, TrxnXmlTags.TRXN_ELE_MNG_CHECK_ITEM);
            for (int loc = 0; loc < this.getwO_WT00_R04_Details().size(); loc++) {
                getMngCheckDetail_Trxn(element, loc);
            }
        }
        return doc;
    }

    private void getMngCheckDetail_Trxn(Element pElement, int loc) {
        Element element = XmlMaker.addElement(pElement, TrxnXmlTags.TRXN_ELE_MNG_CHECK_ITEM_ELE_DETAIL);
        ChangeStringMap(element, "CS_EMP_ID", this.getwO_WT00_R04_Details().get(loc).getCS_EMP_ID());
        ChangeStringMap(element, "WORK_DT", this.getwO_WT00_R04_Details().get(loc).getWORK_DT());
        ChangeStringMap(element, "JOB_NO", this.getwO_WT00_R04_Details().get(loc).getJOB_NO());
        ChangeStringMap(element, "SEL_CHK_ITEM_CD", this.getwO_WT00_R04_Details().get(loc).getSEL_CHK_ITEM_CD());
        ChangeStringMap(element, "INPUT_TP1", this.getwO_WT00_R04_Details().get(loc).getSEL_CHK_RESULT());
        ChangeStringMap(element, "INPUT_RMK", this.getwO_WT00_R04_Details().get(loc).getREMARK());

    }

    private Document getWorkSelectionHeader_Trxn(Document doc) {
        Element element= XmlMaker.addElement(doc, TrxnXmlTags.TRXN_ELE_WORK_SELECTION_HEADER);
        ChangeStringMap(element, "CS_EMP_ID", this.getDM_DM05_R01P_Header01().getCS_EMP_ID());
        ChangeStringMap(element, "WORK_DT", this.getDM_DM05_R01P_Header01().getWORK_DT());
        ChangeStringMap(element, "ATTEND_CD", this.getDM_DM05_R01P_Header01().getATTEND_CD());
        ChangeStringMap(element, "DAY_DUTY_CODE", this.getDM_DM05_R01P_Header01().getDAY_DUTY_CODE());
        ChangeStringMap(element, "DAY_DUTY_IF", this.getDM_DM05_R01P_Header01().getDAY_DUTY_IF());
        ChangeStringMap(element, "NIGHT_DUTY_CODE", this.getDM_DM05_R01P_Header01().getNIGHT_DUTY_CODE());
        ChangeStringMap(element, "NIGHT_DUTY_IF", this.getDM_DM05_R01P_Header01().getNIGHT_DUTY_IF());

        return doc;
    }

    private Document getWorkSelectionItems_Trxn(Document doc) {
        if (getDM_DM05_R01P_Detail01().size() > 0) {
            Element element = XmlMaker.addElement(doc, TrxnXmlTags.TRXN_ELE_WORK_SELECTION_ITEM);
            for (int loc = 0; loc < this.getDM_DM05_R01P_Detail01().size(); loc++) {
                getWorkSelectionDetail_Trxn(element, loc);
            }
        }
        return doc;
    }

    private void getWorkSelectionDetail_Trxn(Element pElement, int loc) {
        Element element = XmlMaker.addElement(pElement, TrxnXmlTags.TRXN_ELE_WORK_SELECTION_ITEM_ELE_DETAIL);
        ChangeStringMap(element, "JOB_NO", this.getDM_DM05_R01P_Detail01().get(loc).getJOB_NO());
        ChangeStringMap(element, "ACT_NO", this.getDM_DM05_R01P_Detail01().get(loc).getACT_NO());
        ChangeStringMap(element, "WORK_CD", this.getDM_DM05_R01P_Detail01().get(loc).getWORK_CD());
        ChangeStringMap(element, "JOB_TM_FR", this.getDM_DM05_R01P_Detail01().get(loc).getJOB_TM_FR());
        ChangeStringMap(element, "JOB_TM_TO", this.getDM_DM05_R01P_Detail01().get(loc).getJOB_TM_TO());
        ChangeStringMap(element, "BLDG_NO", this.getDM_DM05_R01P_Detail01().get(loc).getBLDG_NO());
        ChangeStringMap(element, "CAR_NO", this.getDM_DM05_R01P_Detail01().get(loc).getCAR_NO());
        ChangeStringMap(element, "REF_CONTR_NO", this.getDM_DM05_R01P_Detail01().get(loc).getREF_CONTR_NO());
        ChangeStringMap(element, "SUPPORT_CD", this.getDM_DM05_R01P_Detail01().get(loc).getSUPPORT_CD());
    }

    private Document getTimeWorkHeader_Trxn(Document doc) {
        Element element= XmlMaker.addElement(doc, TrxnXmlTags.TRXN_ELE_WORK_SELECTION_HEADER);
        ChangeStringMap(element, "EMP_ID", this.getDM_DM05_R01P_Header02().getEMP_ID());
        ChangeStringMap(element, "ATTEND_DT", this.getDM_DM05_R01P_Header02().getATTEND_DT());
        ChangeStringMap(element, "ATTEND_CD", this.getDM_DM05_R01P_Header02().getATTEND_CD());
        ChangeStringMap(element, "REP_ST", this.getDM_DM05_R01P_Header02().getREP_ST());
        ChangeStringMap(element, "BASE_LIMIT_MI", this.getDM_DM05_R01P_Header02().getBASE_LIMIT_MI());
        ChangeStringMap(element, "BASE_LIMIT_MI_10", this.getDM_DM05_R01P_Header02().getBASE_LIMIT_MI_10());
        ChangeStringMap(element, "BASE_LIMIT_MI_20", this.getDM_DM05_R01P_Header02().getBASE_LIMIT_MI_20());

        return doc;
    }

    private Document getTimeWorkItems_Trxn(Document doc) {
        if (getDM_DM05_R01P_Detail02().size() > 0) {
            Element element = XmlMaker.addElement(doc, TrxnXmlTags.TRXN_ELE_WORK_SELECTION_ITEM);
            for (int loc = 0; loc < this.getDM_DM05_R01P_Detail02().size(); loc++) {
                getTimeWorkDetail_Trxn(element, loc);
            }
        }
        return doc;
    }

    private void getTimeWorkDetail_Trxn(Element pElement, int loc) {
        Element element = XmlMaker.addElement(pElement, TrxnXmlTags.TRXN_ELE_WORK_SELECTION_ITEM_ELE_DETAIL);
        ChangeStringMap(element, "WORK_TP", this.getDM_DM05_R01P_Detail02().get(loc).getWORK_TP());
        ChangeStringMap(element, "S_NO", this.getDM_DM05_R01P_Detail02().get(loc).getS_NO());
        ChangeStringMap(element, "WORK_CD", this.getDM_DM05_R01P_Detail02().get(loc).getWORK_CD());
        ChangeStringMap(element, "DUTY_WORK_CD", this.getDM_DM05_R01P_Detail02().get(loc).getDUTY_WORK_CD());
        ChangeStringMap(element, "CS_TM_FR", this.getDM_DM05_R01P_Detail02().get(loc).getCS_TM_FR());
        ChangeStringMap(element, "CS_TM_TO", this.getDM_DM05_R01P_Detail02().get(loc).getCS_TM_TO());
        ChangeStringMap(element, "CS_TM", this.getDM_DM05_R01P_Detail02().get(loc).getCS_TM());
        ChangeStringMap(element, "BLDG_NO", this.getDM_DM05_R01P_Detail02().get(loc).getBLDG_NO());
        ChangeStringMap(element, "CAR_NO", this.getDM_DM05_R01P_Detail02().get(loc).getCAR_NO());
        ChangeStringMap(element, "REF_CONTR_NO", this.getDM_DM05_R01P_Detail02().get(loc).getREF_CONTR_NO());
        ChangeStringMap(element, "SUPPORT_CD", this.getDM_DM05_R01P_Detail02().get(loc).getSUPPORT_CD());
        ChangeStringMap(element, "JOB_NO", this.getDM_DM05_R01P_Detail02().get(loc).getJOB_NO());
        ChangeStringMap(element, "JOB_TM_FR", this.getDM_DM05_R01P_Detail02().get(loc).getJOB_TM_FR());
        ChangeStringMap(element, "JOB_TM_TO", this.getDM_DM05_R01P_Detail02().get(loc).getJOB_TM_TO());
    }


    private void ChangeStringMap(Element Element, String key, Object value) {
        XmlMaker.addAttribute(Element, key, String.valueOf(value));
    }

    //region 부품신청
    /// <summary>
    /// 부품 헤더정보
    /// </summary>
    private PartsHeaderItem PartsHeader;
    public PartsHeaderItem getPartsHeader() {
        return PartsHeader;
    }

    public void setPartsHeader(PartsHeaderItem partsHeader) {
        PartsHeader = partsHeader;
    }

    /// <summary>
    /// 부품정보
    /// </summary>
    private List<PartsItem> PartsItems;

    public List<PartsItem> getPartsItems() {
        return PartsItems;
    }

    public void setPartsItems(List<PartsItem> Value) {
        PartsItems = Value;
    }
    //endregion

    //region 승관원 정보전송
    private WO_WT00_R04_Header wO_WT00_R04_Header;

    public WO_WT00_R04_Header getwO_WT00_R04_Header() {
        return wO_WT00_R04_Header;
    }

    public void setwO_WT00_R04_Header(WO_WT00_R04_Header wO_WT00_R04_Header) {
        this.wO_WT00_R04_Header = wO_WT00_R04_Header;
    }

    private List<WO_WT00_R04_Detail> wO_WT00_R04_Details;

    public List<WO_WT00_R04_Detail> getwO_WT00_R04_Details() {
        return wO_WT00_R04_Details;
    }

    public void setwO_WT00_R04_Details(List<WO_WT00_R04_Detail> wO_WT00_R04_Details) {
        this.wO_WT00_R04_Details = wO_WT00_R04_Details;
    }
    //endregion

    //region 작업선택 저장
    /// <summary>
    /// 작업 헤더정보
    /// </summary>
    private DM_DM05_R01P_Header01 DM_DM05_R01P_Header01;

    public DM_DM05_R01P_Header01 getDM_DM05_R01P_Header01() {
        return DM_DM05_R01P_Header01;
    }

    public void setDM_DM05_R01P_Header01(DM_DM05_R01P_Header01 DM_DM05_R01P_Header01) {
        this.DM_DM05_R01P_Header01 = DM_DM05_R01P_Header01;
    }
    /// <summary>
    /// 작업 디테일정보
    /// </summary>
    private List<DM_DM05_R01P_Detail01> DM_DM05_R01P_Detail01;

    public List<DM_DM05_R01P_Detail01> getDM_DM05_R01P_Detail01() {
        return DM_DM05_R01P_Detail01;
    }

    public void setDM_DM05_R01P_Detail01(List<DM_DM05_R01P_Detail01> DM_DM05_R01P_Detail01) {
        this.DM_DM05_R01P_Detail01 = DM_DM05_R01P_Detail01;
    }
    //endregion

    //region 시간조정 저장
    /// <summary>
    /// 작업 헤더정보
    /// </summary>
    private DM_DM05_R01P_Header02 DM_DM05_R01P_Header02;

    public DM_DM05_R01P_Header02 getDM_DM05_R01P_Header02() {
        return DM_DM05_R01P_Header02;
    }

    public void setDM_DM05_R01P_Header02(DM_DM05_R01P_Header02 DM_DM05_R01P_Header02) {
        this.DM_DM05_R01P_Header02 = DM_DM05_R01P_Header02;
    }
    /// <summary>
    /// 작업 디테일정보
    /// </summary>
    private List<DM_DM05_R01P_Detail02> DM_DM05_R01P_Detail02;

    public List<DM_DM05_R01P_Detail02> getDM_DM05_R01P_Detail02() {
        return DM_DM05_R01P_Detail02;
    }

    public void setDM_DM05_R01P_Detail02(List<DM_DM05_R01P_Detail02> DM_DM05_R01P_Detail02) {
        this.DM_DM05_R01P_Detail02 = DM_DM05_R01P_Detail02;
    }
    //endregion


    /// <summary>
    /// TRXN을 XML 스트링으로 변환한다.
    /// </summary>
    /// <returns></returns>
    public String toXml() {
        try {
            Document Doc = XmlMaker.Make(TrxnXmlTags.TRXN_ROOT);

            getTrxn(Doc);
            getPartsHeader_Trxn(Doc);   //부품신청헤더
            getPartsItems_Trxn(Doc);    //부품신청Detail

            XMLOutputter outputter = new XMLOutputter();
            return outputter.outputString(Doc);
        } catch (Exception ex) {
            return ex.getMessage().toString();
        }
    }


    /// <summary>
    /// TRXN을 XML 스트링으로 변환한다.
    /// </summary>
    /// <returns></returns>
    public String toXmlMngCheck() {
        try {
            Document Doc = XmlMaker.Make(TrxnXmlTags.TRXN_ROOT);

            getTrxn(Doc);
            getMngCheckHeader_Trxn(Doc);    //승관원 체크점검표헤더
            getMngCheckItems_Trxn(Doc);     //승관원 체크점검표디테일

            XMLOutputter outputter = new XMLOutputter();
            return outputter.outputString(Doc);
        } catch (Exception ex) {
            return ex.getMessage().toString();
        }
    }

    /// <summary>
    /// TRXN을 XML 스트링으로 변환한다.
    /// </summary>
    /// <returns></returns>
    public String toXmlWorkSelection() {
        try {
            Document Doc = XmlMaker.Make(TrxnXmlTags.TRXN_WORK_SELECTION_ROOT);

            getTrxn(Doc);
            getWorkSelectionHeader_Trxn(Doc);    //작업선택 저장 헤더
            getWorkSelectionItems_Trxn(Doc);     //작업선택 저장 디테일

            XMLOutputter outputter = new XMLOutputter();
            return outputter.outputString(Doc);
        } catch (Exception ex) {
            return ex.getMessage().toString();
        }
    }
    /// <summary>
    /// TRXN을 XML 스트링으로 변환한다.
    /// </summary>
    /// <returns></returns>
    public String toXmlTimeWork() {
        try {
            Document Doc = XmlMaker.Make(TrxnXmlTags.TRXN_WORK_SELECTION_ROOT);

            getTrxn(Doc);
            getTimeWorkHeader_Trxn(Doc);    //작업선택 저장 헤더
            getTimeWorkItems_Trxn(Doc);     //작업선택 저장 디테일

            XMLOutputter outputter = new XMLOutputter();
            return outputter.outputString(Doc);
        } catch (Exception ex) {
            return ex.getMessage().toString();
        }
    }
    // / <summary>
    // / xml로 Deserialize 한다.
    // / </summary>
    // / <param name="xml"></param>
    // / <returns></returns>
    public String toObject(String xml) {
        try {
            XmlParser xmlParser = new XmlParser();
            Document document = xmlParser.xmlParser(xml);
            setTrxn(document);
            Element element = document.getRootElement();
            setPartsItems(element);
            return "1"; // 성공
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }
}

