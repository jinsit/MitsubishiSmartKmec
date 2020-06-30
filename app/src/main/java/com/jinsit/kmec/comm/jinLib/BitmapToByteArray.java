package com.jinsit.kmec.comm.jinLib;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

public class BitmapToByteArray {


    public byte[] bitmapToByteArray( Bitmap $bitmap ) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream() ;
        $bitmap.compress( CompressFormat.JPEG, 50, stream) ;
        //soap 올릴 때 이미지가 너무커서 뻗는다. 제이펙이미지 압축시 압축률을 50으로 하면 500k짜리가 70k까지 떨어진다.
        //압축을 해도 해상도의 차이는 육안으로 큰차이가 없다.
        byte[] byteArray = stream.toByteArray() ;
        return byteArray ;
    }




    public Bitmap byteArrayToBitmap( byte[] $byteArray ) {
        Bitmap bitmap = BitmapFactory.decodeByteArray( $byteArray, 0, $byteArray.length ) ;
        return bitmap ;
    }
}
