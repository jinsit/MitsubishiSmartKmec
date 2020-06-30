package com.jinsit.kmec.IR.PI;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jinsit.kmec.CM.CM_SearchBldg;
import com.jinsit.kmec.CM.CM_SearchBldgInfo_ITEM01;
import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.SwipeDismissListViewTouchListener;
import com.jinsit.kmec.comm.jinLib.ActivityAdmin;
import com.jinsit.kmec.comm.jinLib.AlertView;
import com.jinsit.kmec.comm.jinLib.DataConvertor;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.ImageResize;
import com.jinsit.kmec.comm.jinLib.ListViewUtil;
import com.jinsit.kmec.comm.jinLib.ScreenUtil;
import com.jinsit.kmec.comm.jinLib.SimpleDialog;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.trxn.TrxnDocument;
import com.jinsit.kmec.webservice.GetHttp;
import com.jinsit.kmec.widget.HomeNaviPreference;
import com.jinsit.kmec.widget.HomeNavigation;
import com.jinsit.kmec.widget.JActionbar;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class IR_PI06_R00 extends Activity implements View.OnClickListener,
        AdapterView.OnItemClickListener {

    private static final int REQCODE_CALENDAR = 10;
    private Context context;
    private Activity activity;
    private TextView btn_pi06_searchBldg;
    private TextView btn_pi06_requestTp;
    private TextView btn_pi06_refTp;
    private TextView btn_pi06_requestDate;
    private LinearLayout ll_pi06_freeReason;
    private TextView btn_pi06_freeReason;
    private TextView btn_pi06_freeRequest;
    private TextView btn_pi06_completePartsRequest;
    private TextView btn_pi06_addParts;
    private ListView lv_pi06_partsListView;
    private IR_PI06_R00_Adapter ir_pi06_r00_adapter;
    private CommonSession cs;
    private android.app.ProgressDialog ProgressDialog;

    private String partsCd;
    private String refContrNo;
    private String requestDate;
    private String freeReason;
    private String freeRequest;
    ArrayList<PartsItem> partsItemList = new ArrayList<>();
    private OnCallbackGetPhoto onCallbackGetPhoto;

    public interface OnCallbackGetPhoto{
        void getImage(String image);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ir_pi06_r00);
        android.app.ActionBar aBar = getActionBar();
        aBar.setTitle("부품신청 등록");
        aBar.setDisplayShowHomeEnabled(false);
        this.context = this;
        this.activity = this;
        cs = new CommonSession(context);
        JActionbar.setActionBar(this, aBar);
        getInstances();
    }

    protected void getInstances() {
        btn_pi06_searchBldg = (TextView) findViewById(R.id.btn_pi06_searchBldg);
        btn_pi06_requestTp = (TextView) findViewById(R.id.btn_pi06_requestTp);
        btn_pi06_refTp = (TextView) findViewById(R.id.btn_pi06_refTp);
        btn_pi06_requestDate = (TextView) findViewById(R.id.btn_pi06_requestDate);
        ll_pi06_freeReason = (LinearLayout)findViewById(R.id.ll_pi06_freeReason);
        btn_pi06_freeReason = (TextView) findViewById(R.id.btn_pi06_freeReason);
        btn_pi06_freeRequest = (TextView) findViewById(R.id.btn_pi06_freeRequest);
        btn_pi06_completePartsRequest = (TextView) findViewById(R.id.btn_pi06_completePartsRequest);
        btn_pi06_addParts = (TextView)findViewById(R.id.btn_pi06_addParts);
        lv_pi06_partsListView = (ListView)findViewById(R.id.lv_pi06_partsListView);
        ir_pi06_r00_adapter= new IR_PI06_R00_Adapter(context, partsItemList);
        lv_pi06_partsListView.setAdapter(ir_pi06_r00_adapter);

        setEvents();
    }
    protected void setEvents() {
        btn_pi06_searchBldg.setOnClickListener(this);
        btn_pi06_requestTp.setOnClickListener(this);
        btn_pi06_refTp.setOnClickListener(this);
        btn_pi06_requestDate.setOnClickListener(this);
        btn_pi06_freeReason.setOnClickListener(this);
        btn_pi06_freeRequest.setOnClickListener(this);
        btn_pi06_completePartsRequest.setOnClickListener(this);
        btn_pi06_addParts.setOnClickListener(this);

        setConfig();
    }

    private void setConfig(){
        ActivityAdmin.getInstance().addActivity(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_pi06_searchBldg:
                searchBldg();
                break;
            case R.id.btn_inquery:
                if(this.bldgNo == null || this.bldgNo.equals("") ){
                    AlertView.showAlert("건물명을 조회 해주세요", context);
                    return;
                }
                //this.setInqueryDate(this.inqueryStartDate, this.inqueryEndDate);
                //progress(true);
                break;
            case R.id.btn_pi06_requestDate:
                getCalendar(REQCODE_CALENDAR);
                break;
            case R.id.btn_pi06_refTp:  //관련계약번호
                if(this.partsCd == null || this.partsCd.equals("") ){
                    AlertView.showAlert("청구형태를 선택하십시오.", context);
                    return;
                }
                if(this.bldgNo == null || this.bldgNo.equals("") ){
                    AlertView.showAlert("건물명을 조회 해주세요", context);
                    return;
                }
                searchRequestPopup("4");
                break;
            case R.id.btn_pi06_requestTp:   //청구형태
                searchRequestPopup("7");
                break;
            case R.id.btn_pi06_freeReason:  //무상확인사유
                if(this.partsCd == null || this.partsCd.equals("") ){
                    AlertView.showAlert("청구형태를 선택하십시오.", context);
                    return;
                }
                searchRequestPopup("8");
                break;
            case R.id.btn_pi06_freeRequest:  //무상청구구분
                searchRequestPopup("9");
                break;
            case R.id.btn_pi06_addParts:
                addParts();
                break;
            case R.id.btn_pi06_completePartsRequest:
                completePartsRequest();
                break;

            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        // TODO Auto-generated method stub
    }

    private void searchBldg() {

        final CM_SearchBldg eBldg = new CM_SearchBldg(this);
        eBldg.show();
        eBldg.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                // TODO Auto-generated method stub
                CM_SearchBldgInfo_ITEM01 item = eBldg.getCurrentSelectedItem();
                if(item != null) {
                    setBldgInfo(item);
                }
            }

        });
    }
    private String bldgNo;
    private void setBldgInfo(CM_SearchBldgInfo_ITEM01 bldgInfo){

        btn_pi06_searchBldg.setText(bldgInfo.getBldgNm());
        bldgNo = bldgInfo.getBldgNo();
    }


    private void searchRequestPopup(final String tp) {
        final IR_PI06_R00P iR_PI06_R00P = new IR_PI06_R00P(context, tp, bldgNo, partsCd, cs.getEmpId());
        iR_PI06_R00P.show();
        iR_PI06_R00P.requestPopupSearch();
        iR_PI06_R00P.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                // TODO Auto-generated method stub
                if(iR_PI06_R00P.getCurrentItem() == null)return;

                if(tp.equals("4")){
                    refContrNo = iR_PI06_R00P.getCurrentItem().getCD();
                    btn_pi06_refTp.setText(iR_PI06_R00P.getCurrentItem().getCD());
                }else if(tp.equals("7")){
                    partsCd = iR_PI06_R00P.getCurrentItem().getCD();
                    btn_pi06_requestTp.setText(iR_PI06_R00P.getCurrentItem().getNM());
                    headerInfoInit();
                }else if(tp.equals("8")){
                    freeReason = iR_PI06_R00P.getCurrentItem().getCD();
                    btn_pi06_freeReason.setText(iR_PI06_R00P.getCurrentItem().getNM());
                }else if(tp.equals("9")){
                    freeRequest = iR_PI06_R00P.getCurrentItem().getCD();
                    btn_pi06_freeRequest.setText(iR_PI06_R00P.getCurrentItem().getNM());
                }
            }

        });
    }

    /**
     * 청구형태가 변경되면 관련계약번호와 무상확인사유
     * 20190118 yowonsm
     */
    private void headerInfoInit(){
        refContrNo = "";
        btn_pi06_refTp.setText("");
        freeReason = "";
        btn_pi06_freeReason.setText("");
        if("N".equals(partsCd)){
            ll_pi06_freeReason.setVisibility(View.GONE);
        }else{
            ll_pi06_freeReason.setVisibility(View.VISIBLE);
        }
    }

    private void addParts(){
        final IR_PI06_R01P iR_PI06_R01P = new IR_PI06_R01P(context, bldgNo, activity);
        iR_PI06_R01P.show();
        iR_PI06_R01P.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                // TODO Auto-generated method stub
                if(iR_PI06_R01P.getiR_PI06_R01P_Items() == null || iR_PI06_R01P.getiR_PI06_R01P_Items().size() == 0)return;
                ArrayList<IR_PI06_R01P_Item> items = iR_PI06_R01P.getiR_PI06_R01P_Items();
                for (IR_PI06_R01P_Item item: items) {
                    partsItemList.add(new PartsItem(item, iR_PI06_R01P.getCarNo()));
                }
                ir_pi06_r00_adapter.notifyDataSetChanged();
                ListViewUtil.listViewHeightSet(ir_pi06_r00_adapter, lv_pi06_partsListView);
            }

        });

        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(lv_pi06_partsListView, new SwipeDismissListViewTouchListener.DismissCallbacks() {
                    @Override
                    public boolean canDismiss(int position) {
                        return true;
                    }

                    @Override
                    public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                        for (int position : reverseSortedPositions) {
                            partsItemList.remove(partsItemList.get(position));
                        }
                        ir_pi06_r00_adapter.notifyDataSetChanged();
                    }
                });
        lv_pi06_partsListView.setOnTouchListener(touchListener);
        lv_pi06_partsListView.setOnScrollListener(touchListener.makeScrollListener());
    }

    private String inputXml;
    private void completePartsRequest(){
        if(this.bldgNo == null || this.bldgNo.equals("") ){
            AlertView.showAlert("건물명을 조회 해주세요", context);
            return;
        }
        if(this.partsCd == null || this.partsCd.equals("") ){
            AlertView.showAlert("청구형태를 선택해주세요.", context);
            return;
        }
        if(this.refContrNo == null || this.refContrNo.equals("") ){
            AlertView.showAlert("관련계약 번호를 선택해 주세요.", context);
            return;
        }
        if(this.requestDate == null || this.requestDate.equals("") ){
            AlertView.showAlert("자재요청일자를 선택해 주세요.", context);
            return;
        }
        if((this.freeReason == null || this.freeReason.equals("")) && "N".equals(partsCd)){
            AlertView.showAlert("무상확인사유를 선택해 주세요.", context);
            return;
        }
        if(this.freeRequest == null || this.freeRequest.equals("") ){
            AlertView.showAlert("무상청구구분을 선택해 주세요.", context);
            return;
        }


        TrxnDocument document = new TrxnDocument();
        try{
            PartsHeaderItem header = new PartsHeaderItem(cs.getWorkDt(), requestDate, partsCd, refContrNo, freeReason, freeRequest, cs.getDeptCd(), cs.getEmpId());
            document.setPartsHeader(header);
            document.setPartsItems(partsItemList);
            inputXml = document.toXml();
        }catch(Exception ex){
            AlertView.showAlert(ex.toString(), context);
            return;
        }
        progress(true);
        new CompletePartsRequest().execute();
    }


    public class CompletePartsRequest extends AsyncTask<Void, Void, Void> {

        private String message = "";
        private String rtn = "";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            try {

                GetHttp getHttp = new GetHttp();
                String param_url = WebServerInfo.getUrl()+"ir/completePartsRequest.do";

                List<NameValuePair> arguments = new ArrayList<NameValuePair>();
                arguments.add(new BasicNameValuePair("inputXml", inputXml));
                JSONObject returnJson = getHttp.getPost(param_url, arguments, true);

                try {
                    EasyJsonList ejl = new EasyJsonList(returnJson.getJSONArray("dataList"));
                    int jsonSize = returnJson.getJSONArray("dataList").length();

                    if(jsonSize > 0){
                        message = ejl.getValue(0, "PARTS_NO");
                        rtn = ejl.getValue(0, "RTNCD");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    message = e.getMessage().toString();
                }


            } catch (Exception ex) {
                // 로그인이 실패하였습니다 띄어주기
                message = ex.getMessage().toString();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ProgressDialog.dismiss();
            if("Y".equals(rtn)){
                SimpleDialog sm01 = new SimpleDialog(context, "알림", "자재청구 신청을 완료하였습니다.\n 자재청구번호 = " + message , new SimpleDialog.btnClickListener() {
                    @Override
                    public void onButtonClick() {
                        finish();
                    }
                });
                sm01.setCancelable(false);
                sm01.show();
            }else{
                SimpleDialog sm01 = new SimpleDialog(context, "알림", "자재청구 신청실패.\n 메시지 = " + message , new SimpleDialog.btnClickListener() {
                    @Override
                    public void onButtonClick() {
                    }
                });
                sm01.setCancelable(false);
                sm01.show();
            }
        }
    }
    private void progress(Boolean isActivated){
        if(isActivated){
            this.ProgressDialog =
                    android.app.ProgressDialog.show(context, "알림","조회중 입니다.");
        }else{
            this.ProgressDialog.dismiss();
        }
    }

    private void getCalendar(int fromTo) {
        Intent intent = new Intent(this, com.jinsit.kmec.comm.CalendarGridNoDataActivity.class);
        startActivityForResult(intent, fromTo);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //카메라앱 호출인 경우 결과성공이 아니면 리턴한다.
        if(requestCode != REQCODE_CALENDAR && resultCode != RESULT_OK){
            return;
        }
        switch (requestCode) {
            case REQCODE_CALENDAR:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    requestDate = bundle.getString("dateSelected");

                    long now = System.currentTimeMillis();
                    Date today = new Date(now);
                    Date furture = null;
                    SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
                    try {
                        furture = format.parse(requestDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (furture.after(today)) {
                        btn_pi06_requestDate.setText(requestDate);         // 변경 알림 메세지
                    }else {
                        AlertView.showAlert("자재요청일자는 금일 이전 날짜는 선택 할 수 없습니다.", context);
                    }

                }
                break;


            case CROP_FROM_CAMERA: {
                // 크롭이 된 이후의 이미지를 넘겨 받습니다. 이미지뷰에 이미지를 보여준다거나 부가적인 작업 이후에
                // 임시 파일을 삭제합니다.

                final Bundle extras = data.getExtras(); //mImageCaptureUri = data.getData();
                if (extras != null) {
                    Bitmap
                            photo = extras.getParcelable("data");
                    if(albumFlag){
                        albumFlag = false;
                    }else{
                        uriPath = mImageCaptureUri.getPath();
                    }
                    ImageResize ir = new ImageResize();
                    //photo= ir.bitmapResize(uriPath);

                    String str = getBaseImg(photo);
                    Log.e("baseEncode", "Base64 Encoding : " + str);
                    onCallbackGetPhoto.getImage(str);
                }

                // 임시 파일 삭제
                File f = new File(mImageCaptureUri.getPath());
                if(f.exists()) {
                    f.delete();
                }
                break;
            }

            case PICK_FROM_ALBUM: {
                // 이후의 처리가 카메라와 같으므로 일단 break없이 진행합니다.
                // 실제 코드에서는 좀더 합리적인 방법을 선택하시기 바랍니다.
                albumFlag = true;
                mImageCaptureUri = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor imagecursor = getContentResolver().query(
                        mImageCaptureUri, filePathColumn, null, null, null);
                imagecursor.moveToFirst();

                int coulmidx = imagecursor.getColumnIndex(filePathColumn[0]);
                String imageP = imagecursor.getString(coulmidx);
                imagecursor.close();
                uriPath = imageP;

                ImageResize ir = new ImageResize();
                Bitmap photo = ir.bitmapResize(uriPath);

                String str = getBaseImg(photo);
                Log.e("baseEncode", "Base64 Encoding : " + str);
                onCallbackGetPhoto.getImage(str);

                // 임시 파일 삭제
                File f = new File(mImageCaptureUri.getPath());
                if (f.exists()) {
                    f.delete();
                }
                break;
            }

            case PICK_FROM_CAMERA: {
                // 이미지를 가져온 이후의 리사이즈할 이미지 크기를 결정합니다.
                // 이후에 이미지 크롭 어플리케이션을 호출하게 됩니다.

//			Intent intent = new Intent("com.android.camera.action.CROP");
//			intent.setDataAndType(mImageCaptureUri, "image/*");
//
//			intent.putExtra("outputX", 200);
//			intent.putExtra("outputY", 200);
//			intent.putExtra("aspectX", 1);
//			intent.putExtra("aspectY", 1);
//			intent.putExtra("scale", true);
//			intent.putExtra("return-data", true);
//			startActivityForResult(intent, CROP_FROM_CAMERA);

                uriPath = mImageCaptureUri.getPath();

                ImageResize ir = new ImageResize();
                Bitmap photo= ir.bitmapResize(uriPath);

                String str = getBaseImg(photo);
                Log.e("baseEncode", "Base64 Encoding : " + str);
                onCallbackGetPhoto.getImage(str);

                // 임시 파일 삭제
                File f = new File(mImageCaptureUri.getPath());
                if (f.exists()) {
                    f.delete();
                }

                break;
            }
        }

    }
    //region NAVI바
    //----------------------내비게이션 영역--------------------------------------//
    private HomeNavigation homeNavi;
    private FrameLayout testnavi;
    boolean isHide;
    private HomeNaviPreference naviPref;

    private void setToggleNavi(){
        boolean isHide = naviPref.isHide();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int totalHeight = ScreenUtil.getRealScreenHeight(this);;
        int naviHeight = getResources().getDrawable(R.drawable.home_menu_on).getIntrinsicHeight();
        int viewArea = naviHeight/6;
        int setPadding = totalHeight-viewArea-naviHeight;
        if(isHide){
            testnavi.setPadding(0, setPadding, 0, 0);testnavi.setAlpha((float)0.5);
            Log.e("isHide", "naviHide = " +isHide );
        }else{
            testnavi.setPadding(0, 0, 0, 0); testnavi.setAlpha((float)1);
            Log.e("isHide", "naviHide = " +naviPref.isHide() );
        }
    }
    private boolean isFirstHide = false;
    private void navigationInit(){
        testnavi = (FrameLayout) findViewById(R.id.fl_naviBar);
        homeNavi = new HomeNavigation(context, null);
        homeNavi = (HomeNavigation) findViewById(R.id.hn_homeNavi);
        if(!isFirstHide){
            naviPref.setHide(true);
            isFirstHide = true;
        }
        homeNavi.setToggleNavi();
        Button navi = (Button) homeNavi.getBtn_naviHOME();
        navi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                naviPref.setHide(!naviPref.isHide());
                homeNavi.setToggleNavi();

            }
        });
    }
    @Override
    public void onResume(){
        super.onResume();
        naviPref = new HomeNaviPreference(context);
        navigationInit();
    }
    //endregion

    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_CAMERA = 2;

    boolean albumFlag = false;
    private Uri mImageCaptureUri;
    String uriPath;

    public void actionCamera(OnCallbackGetPhoto listener) {
        onCallbackGetPhoto = listener;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // 임시로 사용할 파일의 경로를 생성

        String url = "tmp_" + String.valueOf(System.currentTimeMillis())
                + ".jpg";
        mImageCaptureUri = Uri.fromFile(new File(Environment
                .getExternalStorageDirectory(), url));

        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
                mImageCaptureUri);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PICK_FROM_CAMERA);
    }

    public void actionGallery(OnCallbackGetPhoto listener) {
        onCallbackGetPhoto = listener;
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);

        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    private String getBaseImg(Bitmap bitmap){
        return DataConvertor.BitmapBase64(bitmap);
        /*String base64Str ="";
        if(bitmap==null){

        }else{
            BitmapToByteArray btb = new BitmapToByteArray();
            byte[] img = btb.bitmapToByteArray(bitmap);
            base64Str = Base64.encodeToString(img, Base64.NO_WRAP);

        }

        return base64Str;*/

    }
}