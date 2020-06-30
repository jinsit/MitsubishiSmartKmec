package com.jinsit.kmec.comm.jinLib;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jinsit.kmec.R;

/**
 * @class SimpleDialog
 * @author 원성민
 * @description AlertDialog상속
 * btnClickListener implements 하여 '닫기'버튼 이벤트 획득
 * @construct context, title, msg, listener (콜백이 필요없는 경우 listener 미포함)
 *
 * @sample 액티비티 안에서
 * @code btnClickListener bcl = new btnClickListener(){
//@Override
public void onButtonClick() {
// TODO Auto-generated method stub
//콜백구현
}			};
SimpleDialog sd = new SimpleDialog(context,"제목","내용",bcl);
sd.show();}
 */
public class SimpleDialog  extends AlertDialog {

	private static final int NORMAL_VIEW=0;
	private static final int LIST_VIEW=1;

	public btnClickListener bntListener;
	public OnItemClickListener listener ;
	String title, msg;
	ListView lv_dialogList;
	ListAdapter ladpater =null;
	int viewProperty = NORMAL_VIEW;
	boolean isSelector = false;
	private TextView tv01_popTitle;
	private TextView btn_popClose;
	// /////////
	public interface btnClickListener{
		void onButtonClick();
	}
	@Override
	public void setTitle(CharSequence title) {
		// TODO Auto-generated method stub
		super.setTitle(title);
	}

	@Override
	public void setMessage(CharSequence message) {
		// TODO Auto-generated method stub
		super.setMessage(message);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		switch(viewProperty){
			case NORMAL_VIEW:
				setContentView(R.layout.comm_dialog);

				TextView tv_title = (TextView)findViewById(R.id.tv_dialogSubject);
				tv_title.setText(title);
				TextView btn = (TextView)findViewById(R.id.btn_dialogBtn);
				btn.setOnClickListener(new android.view.View.OnClickListener(){
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(bntListener!=null)bntListener.onButtonClick();
						cancel();
					}

				});
				break;
			case LIST_VIEW:
				setContentView(R.layout.comm_list_dialog);
				tv01_popTitle = (TextView) findViewById(R.id.tv01_popTitle);
				btn_popClose = (TextView) findViewById(R.id.btn_popClose);
				btn_popClose.setOnClickListener(new View.OnClickListener(){

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						cancel();
					}

				});

				lv_dialogList = (ListView)findViewById(R.id.lv_dialogList);

				if(lv_dialogList!=null){
					if(isSelector)lv_dialogList.setFocusable(true);
					lv_dialogList.setAdapter(ladpater);
					lv_dialogList.setOnItemClickListener(listener);
				}
				tv01_popTitle.setText(title);
				break;
		}

		TextView message = (TextView)findViewById(R.id.tv_dialogMessage);

		if(message!=null)message.setText(msg);




	}

	@Override
	public void setView(View view) {
		// TODO Auto-generated method stub
		super.setView(view);
	}

	public SimpleDialog(Context context, String title, String message, btnClickListener cancelListener) {
		super(context);
		// TODO Auto-generated constructor stub
		this.bntListener = cancelListener;
		this.title = title;
		this.msg = message;

	}
	public SimpleDialog(Context context, String title, String message) {
		super(context);
		// TODO Auto-generated constructor stub
		this.title = title;
		this.msg = message;

	}

	public SimpleDialog(Context context, String title, Object adapter){
		super(context);
		this.viewProperty = LIST_VIEW;
		this.title = title;
		this.ladpater = (ListAdapter)adapter;
	}
	/**
	 * SimpleDialog sd = new SimpleDialog(context, "title", searchElevListAdapter, new OnItemClickListener(){
	 override
	 public void onItemClick(AdapterView<?> parent, View view,
	 int position, long id) {
	 // TODO Auto-generated method stub

	 }});
	 sd.show();
	 * @param context
	 * @param title
	 * @param adapter
	 * @param setOnItemClickListener
	 */

	public SimpleDialog(Context context, String title,
						Object adapter,
						OnItemClickListener setOnItemClickListener) {
		super(context);
		// TODO Auto-generated constructor stub
		this.viewProperty = LIST_VIEW;
		this.isSelector = true;
		this.title = title;
		this.ladpater = (ListAdapter)adapter;
		listener = (OnItemClickListener)setOnItemClickListener;

	}



}
