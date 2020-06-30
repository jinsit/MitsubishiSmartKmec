package com.jinsit.kmec.IR.PI;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jinsit.kmec.CM.CM_SearchElev;
import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.jinLib.AlertView;
import com.jinsit.kmec.comm.jinLib.DataConvertor;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class IR_PI06_R02P extends Dialog implements View.OnClickListener, AdapterView.OnItemClickListener, DialogInterface.OnDismissListener, IR_PI06_R00.OnCallbackGetPhoto {
    public IR_PI06_R02P(Context c, String searchString, Activity activity) {
        super(c);
        // TODO Auto-generated constructor stub
        context = c;
        this.parentActivity = (IR_PI06_R00) activity;
        this.searchString = searchString;
    }

    private android.app.ProgressDialog ProgressDialog;
    ArrayList<IR_PI06_R01P_Item> iR_PI06_R01P_Items;
    private EasyJsonList ejl;
    Button btn_cancel;
    TextView btn_popClose;
    private TextView btn_pi06_inquery;
    private EditText et_pi06_inqueryText;

    ListView lv_pi06_partsListView;
    IR_PI06_R02P_Adapter iR_PI06_R02P_Adapter;
    Context context;
    private IR_PI06_R00 parentActivity;
    String searchString;
    String searchImageString;

    CheckBox chk_pi06_check;
    TextView btn_pi06_savePartsRequest;
    IR_PI06_R01P_Item currentItem;
    ArrayList<IR_PI06_R01P_Item> iR_PI06_R02P_Items = new ArrayList<>();
    private View.OnClickListener imageClickListener;
    CommonSession cs;

    public IR_PI06_R01P_Item getCurrentItem() {
        return currentItem;
    }

    public void setCurrentItem(IR_PI06_R01P_Item currentItem) {
        this.currentItem = currentItem;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ir_pi06_r02p);
        TextView title = (TextView) findViewById(R.id.tv01_popTitle);
        title.setText("부품목록");
        btn_popClose = (TextView) findViewById(R.id.btn_popClose);
        btn_popClose.setOnClickListener(this);
        lv_pi06_partsListView = (ListView) findViewById(R.id.lv_pi06_partsListView);
        lv_pi06_partsListView.setOnItemClickListener(this);

        lv_pi06_partsListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        chk_pi06_check = (CheckBox) findViewById(R.id.chk_pi06_check);


        btn_pi06_savePartsRequest = (TextView) findViewById(R.id.btn_pi06_savePartsRequest);
        btn_pi06_savePartsRequest.setOnClickListener(this);
        btn_pi06_inquery = (TextView) findViewById(R.id.btn_pi06_inquery);
        btn_pi06_inquery.setOnClickListener(this);
        et_pi06_inqueryText = (EditText) findViewById(R.id.et_pi06_inqueryText);

        et_pi06_inqueryText.setText(searchString);
        imageClickListener = this;
        cs = new CommonSession(context);
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_popClose:
                dismiss();
                break;

            case R.id.btn_pi06_inquery:
                if (et_pi06_inqueryText.getText().toString() == null || et_pi06_inqueryText.getText().toString().equals("")) {
                    AlertView.showAlert("아이템명, 아이템번호, 규격 중 하나를 입력하십시오.", context);
                    return;
                }
                searchString = et_pi06_inqueryText.getText().toString();
                requestPartsItemSearch();
                break;

            case R.id.iv_pi06_partsImage:
                ImageView button = (ImageView) v;
                String imageYN = button.getTag(R.id.tag_ir_pi06_image_yn).toString();
                searchImageString = button.getTag(R.id.tag_ir_pi06_image_no).toString();
                if ("Y".equals(imageYN)) {
                    //이미지가 있으면 이미지 보여주기
                    progress(true);
                    new PartsImageSearch().execute();
                } else {
                    //이미지가 없으면 업로드 호출
                    popupDialog();

                }
                break;
            case R.id.btn_pi06_savePartsRequest:
                checkitemList();
                dismiss();
                break;


        }
    }

    public void popupDialog() {
        String item[] = {"카메라", "갤러리에서 선택"};
        new AlertDialog.Builder(parentActivity).setTitle("아이템 사진등록")
                .setItems(item, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                parentActivity.actionCamera(IR_PI06_R02P.this);
                                break;
                            case 1:
                                parentActivity.actionGallery(IR_PI06_R02P.this);
                                break;
                        }
                    }
                }).show();
    }

    private void progress(Boolean isActivated) {
        if (isActivated) {
            this.ProgressDialog =
                    android.app.ProgressDialog.show(getContext(), "알림", "조회중 입니다.");
        } else {
            this.ProgressDialog.dismiss();
        }
    }

    public void requestPartsItemSearch() {
        // TODO Auto-generated method stub
        progress(true);
        new PartsItemSearch().execute();
    }

    private String uploadImage;

    @Override
    public void getImage(String image) {

        if (image != null) {
            uploadImage = image;
            new UploadPartsImage().execute();
        }
    }

    public class PartsItemSearch extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            try {

                iR_PI06_R01P_Items = new ArrayList<IR_PI06_R01P_Item>();
                GetHttp getHttp = new GetHttp();
                String param_url = WebServerInfo.getUrl() + "ir/selectPartsRequestImage.do";

                List<NameValuePair> arguments = new ArrayList<NameValuePair>();
                arguments.add(new BasicNameValuePair("itemNo", searchString));        //검색어(품번, 품명, 규격 중)
                arguments.add(new BasicNameValuePair("tp", "1"));                //상품검색, 2는  사진
                JSONObject returnJson = getHttp.getPost(param_url, arguments, true);

                try {
                    ejl = new EasyJsonList(returnJson.getJSONArray("dataList"));
                    iR_PI06_R01P_Items.clear();
                    int jsonSize = returnJson.getJSONArray("dataList").length();
                    for (int i = 0; i < jsonSize; i++) {
                        iR_PI06_R01P_Items.add(new IR_PI06_R01P_Item(
                                ejl.getValue(i, "PITEM_NO"),
                                ejl.getValue(i, "PITEM_NM"),
                                ejl.getValue(i, "ITEM_NO"),
                                ejl.getValue(i, "ITEM_NM"),
                                ejl.getValue(i, "SIZE"),
                                ejl.getValue(i, "DRAW_NO"),
                                ejl.getValue(i, "GL_NO"),
                                ejl.getValue(i, "UNIT_PRC"),
                                ejl.getValue(i, "ITEM_NM2"),
                                ejl.getValue(i, "IMAGE_YN")));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } catch (Exception ex) {
                // 로그인이 실패하였습니다 띄어주기
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ProgressDialog.dismiss();
            iR_PI06_R02P_Adapter = new IR_PI06_R02P_Adapter(context, iR_PI06_R01P_Items, imageClickListener);
            lv_pi06_partsListView.setAdapter(iR_PI06_R02P_Adapter);
            lv_pi06_partsListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        }
    }

    public class PartsImageSearch extends AsyncTask<Void, Void, Void> {

        private String partsImage;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            try {
                GetHttp getHttp = new GetHttp();
                String param_url = WebServerInfo.getUrl() + "ir/selectItemImage.do";

                List<NameValuePair> arguments = new ArrayList<NameValuePair>();
                arguments.add(new BasicNameValuePair("itemNo", searchImageString));        //선택된 ItemNo를 가져와야 한다.
                //arguments.add(new BasicNameValuePair("tp", "2"));				//상품검색, 2는  사진
                JSONObject returnJson = getHttp.getPost(param_url, arguments, true);


                try {
                    ejl = new EasyJsonList(returnJson.getJSONArray("dataList"));
                    int jsonSize = returnJson.getJSONArray("dataList").length();
                    for (int i = 0; i < jsonSize; i++) {
                        try {
                            partsImage = ejl.getValue(0, "IMG1");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } catch (Exception ex) {
                // 로그인이 실패하였습니다 띄어주기
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ProgressDialog.dismiss();
            try {
                Bitmap bm = DataConvertor.Base64Bitmap(partsImage);
                IR_PI04_R01P ir01 = new IR_PI04_R01P(context, bm);
                ir01.show();
            } catch (Exception ex) {
                Toast.makeText(context, "이미지 불러오기를 실패했습니다.", Toast.LENGTH_SHORT)
                        .show();
            }

        }
    }


    public class UploadPartsImage extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            try {
                GetHttp getHttp = new GetHttp();
                String param_url = WebServerInfo.getUrl() + "ir/uploadPartsImage.do";

                List<NameValuePair> arguments = new ArrayList<NameValuePair>();
                arguments.add(new BasicNameValuePair("itemNo", searchImageString));        //선택된 ItemNo를 가져와야 한다.
                arguments.add(new BasicNameValuePair("regId", "1"));        //		//regId는 하드코딩 되어 있더라
                arguments.add(new BasicNameValuePair("image", uploadImage));        // 업로드 할 이미지
                arguments.add(new BasicNameValuePair("userId", cs.getEmpId()));        //선택된 ItemNo를 가져와야 한다.
                //arguments.add(new BasicNameValuePair("tp", "2"));				//상품검색, 2는  사진
                JSONObject returnJson = getHttp.getPost(param_url, arguments, true);


                try {
                    ejl = new EasyJsonList(returnJson.getJSONArray("dataList"));
                    int jsonSize = returnJson.getJSONArray("dataList").length();
                    for (int i = 0; i < jsonSize; i++) {

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } catch (Exception ex) {
                // 로그인이 실패하였습니다 띄어주기
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ProgressDialog.dismiss();
            Toast.makeText(context, "이미지 업로드 성공", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        // TODO Auto-generated method stub
        iR_PI06_R02P_Adapter.setChecked(position);
        iR_PI06_R02P_Adapter.notifyDataSetChanged();
    }

    private void checkitemList() {
        for (int i = 0; i < iR_PI06_R01P_Items.size(); i++) {
            if (iR_PI06_R02P_Adapter.getChecked(i)) {
                IR_PI06_R01P_Item resData = iR_PI06_R01P_Items.get(i);
                iR_PI06_R02P_Items.add(resData);
            }
        }
        iR_PI06_R01P_Items = iR_PI06_R02P_Items;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        // TODO Auto-generated method stub

    }


}