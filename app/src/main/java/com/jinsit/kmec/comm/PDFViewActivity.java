package com.jinsit.kmec.comm;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.jinsit.kmec.R;
import com.shockwave.pdfium.PdfDocument;

import java.util.List;

public class PDFViewActivity extends Activity implements OnPageChangeListener, OnLoadCompleteListener {
    private static final String TAG = PDFViewActivity.class.getSimpleName();
    public static final String SAMPLE_FILE = "sample_pdf.pdf";
    PDFView pdfView;
    Integer pageNumber = 0;
    String pdfFileName;

    private TextView 	 mEmpIdView,mEmpIdView2,mEmpIdView3,mEmpIdView4,mEmpIdView5,mEmpIdView6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfview);

        mEmpIdView = (TextView)findViewById(R.id.tv_empId);
        mEmpIdView2 = (TextView)findViewById(R.id.tv_empId2);
        mEmpIdView3 = (TextView)findViewById(R.id.tv_empId3);
        mEmpIdView4 = (TextView)findViewById(R.id.tv_empId4);
        mEmpIdView5 = (TextView)findViewById(R.id.tv_empId5);
        mEmpIdView6 = (TextView)findViewById(R.id.tv_empId6);
        mEmpIdView.setVisibility(View.VISIBLE);
        mEmpIdView2.setVisibility(View.VISIBLE);
        mEmpIdView3.setVisibility(View.VISIBLE);
        mEmpIdView4.setVisibility(View.VISIBLE);
        mEmpIdView5.setVisibility(View.VISIBLE);
        mEmpIdView6.setVisibility(View.VISIBLE);
        CommonSession cs = new CommonSession(this);
        mEmpIdView.setText(cs.getEmpId());
        mEmpIdView2.setText(cs.getEmpId());
        mEmpIdView3.setText(cs.getEmpId());
        mEmpIdView4.setText(cs.getEmpId());
        mEmpIdView5.setText(cs.getEmpId());
        mEmpIdView6.setText(cs.getEmpId());

        pdfView = (PDFView) findViewById(R.id.pdfView);
        Intent intent = getIntent();
        Uri uri = intent.getData();
        displayFromUri(uri);
    }

    /**
     * 어셋파일
     * @param assetFileName
     */
    private void displayFromAsset(String assetFileName) {
        pdfFileName = assetFileName;

        pdfView.fromAsset(SAMPLE_FILE)
                .defaultPage(pageNumber)
                .enableSwipe(true)

                .swipeHorizontal(false)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();
    }

    private void displayFromUri(Uri uri){
        pdfFileName = uri.toString();

        pdfView.fromUri(uri)
                .defaultPage(pageNumber)
                .enableSwipe(true)

                .swipeHorizontal(false)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();
    }


    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
        setTitle(String.format("%s %s / %s", "페이지 ", page + 1, pageCount));
    }


    @Override
    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = pdfView.getDocumentMeta();
        printBookmarksTree(pdfView.getTableOfContents(), "-");

    }

    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {

            Log.e(TAG, String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));

            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }

}