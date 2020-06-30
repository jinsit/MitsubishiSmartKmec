package com.jinsit.kmec.comm.jinLib;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Log;

public class ImageResize {

	public ImageResize() {

	}



	public Bitmap bitmapResize(String fileName) {
		//int MAX_IMAGE_SIZE = 800;
		Bitmap bm = null;
		BitmapFactory.Options bfo = new BitmapFactory.Options();
		int scale = 1;
		int imgWidth = getBitmapOfWidth(fileName);
		int imgHeight = getBitmapOfHeight(fileName);
		Log.e("widthxHeight", imgWidth + "x" + imgHeight);

		/*if (imgHeight > MAX_IMAGE_SIZE || imgWidth > MAX_IMAGE_SIZE) {
			scale = (int) Math.pow(2,(int) Math.round(Math.log(MAX_IMAGE_SIZE
							/ (double) Math.max(imgHeight, imgWidth))
							/ Math.log(0.5)));
		}

		bfo.inSampleSize = scale;*/
		Log.v("what scale", "waht salce" + scale);
		if (imgWidth > 8000 || imgHeight > 8000) {
			bfo.inSampleSize = 16;
		} else if (imgWidth > 4000 || imgHeight > 4000) {
			bfo.inSampleSize = 8;
		} else if (imgWidth > 2000 || imgHeight > 2000) {
			bfo.inSampleSize = 4;
		} else if (imgWidth > 1000 || imgHeight > 1000) {
			bfo.inSampleSize = 2;
		} else {
			bfo.inSampleSize = 1;
		}

		bfo.inScaled = true; // 이부분에서 아웃오브 메모리 발생한다 조심해라
		//inScaled를 false로 설정하면 이미지를 resize없이 부르기 때문에 아웃오브메모리 발생가능성 UP

		bm = BitmapFactory.decodeFile(fileName, bfo);
		int degree = GetExifOrientation(fileName);
		Bitmap orientBitmap = GetRotatedBitmap(bm, degree);
		// Bitmap resized = Bitmap.createScaledBitmap(orientBitmap, 200, 200,
		// true);
		Log.i("widthxHeight",
				orientBitmap.getWidth() + "x" + orientBitmap.getHeight());

		return orientBitmap;
	}


	public static Bitmap thumbnailResize(Bitmap bitmap) {
		Bitmap thumnail = null;
		if (bitmap != null)
			thumnail = Bitmap.createScaledBitmap(bitmap, 300, 300, true);
		if (bitmap != null)
			Log.w("widthxHeight", thumnail.getWidth() + "x");

		return thumnail;

	}

	/**
	 * 비트맵의 w/h 를 변경하여준다.
	 * @param bitmap
	 * @return
	 */
	public static Bitmap getScaleBitmap(Bitmap bitmap) {
		Bitmap thumnail = null;
		if (bitmap != null)
			thumnail = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
		if (bitmap != null)
			Log.w("widthxHeight", thumnail.getWidth() + "x");

		return thumnail;

	}

	/** Get Bitmap's Width **/
	public static int getBitmapOfWidth(String fileName) {
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(fileName, options);
			return options.outWidth;
		} catch (Exception e) {
			return 0;
		}
	}

	/** Get Bitmap's height **/
	public static int getBitmapOfHeight(String fileName) {

		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(fileName, options);

			return options.outHeight;
		} catch (Exception e) {
			return 0;
		}
	}

	/*
	 * 이미지파일의 오리엔탈을 얻어오는 함수
	 */
	public synchronized static int GetExifOrientation(String filepath) {
		int degree = 0;
		ExifInterface exif = null;

		try {
			exif = new ExifInterface(filepath);
		} catch (IOException e) {
			// Log.e(TAG, "cannot read exif");
			e.printStackTrace();
		}

		if (exif != null) {
			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION, -1);

			if (orientation != -1) {
				// We only recognize a subset of orientation tag values.
				switch (orientation) {
					case ExifInterface.ORIENTATION_ROTATE_90:
						degree = 90;
						break;

					case ExifInterface.ORIENTATION_ROTATE_180:
						degree = 180;
						break;

					case ExifInterface.ORIENTATION_ROTATE_270:
						degree = 270;
						break;
				}

			}
		}

		return degree;
	}

	/*
	 * 비트맵파일을 원하는 방향으로 회전시키는 함수 기본 오리엔탈 포틀릿
	 */
	public synchronized static Bitmap GetRotatedBitmap(Bitmap bitmap,
													   int degrees) {
		if (degrees != 0 && bitmap != null) {
			Matrix m = new Matrix();
			m.setRotate(degrees, (float) bitmap.getWidth() / 2,
					(float) bitmap.getHeight() / 2);
			try {
				Bitmap b2 = Bitmap.createBitmap(bitmap, 0, 0,
						bitmap.getWidth(), bitmap.getHeight(), m, true);
				if (bitmap != b2) {
					bitmap.recycle();
					bitmap = b2;
				}
			} catch (OutOfMemoryError ex) {
				// We have no memory to rotate. Return the original bitmap.
			}
		}

		return bitmap;
	}

}
