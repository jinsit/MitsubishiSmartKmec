package com.jinsit.kmec.comm.jinLib;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import android.gesture.Gesture;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Base64;

public class SignUtil
{
	/**
	 * BMP Header 를 생성 한다.
	 * @param stream
	 * @param width
	 * @param height
	 * @param fileSize
	 * @param bitCount
	 * @throws IOException
	 */
	private static void encodeBmpHeader(DataOutputStream stream, int width,
										int height, int fileSize, short bitCount) throws IOException {
		// the magic number used to identify the BMP file: 0x42 0x4D
		stream.writeByte(0x42);
		stream.writeByte(0x4D);
		stream.writeInt(EndianUtils.swapInteger(fileSize));
		// reserved
		stream.writeInt(0);
		// the offset, i.e. starting address of the bitmap data
		stream.writeInt(EndianUtils.swapInteger(14 + 40));
		// INFORMATION HEADER (Windows V3 header) the size of this header (40 bytes)
		stream.writeInt(EndianUtils.swapInteger(40));
		// the bitmap width in pixels (signed integer).
		stream.writeInt(EndianUtils.swapInteger(width));
		// the bitmap height in pixels (signed integer).
		stream.writeInt(EndianUtils.swapInteger(height));
		// the number of colour planes being used. Must be set to 1.
		stream.writeShort(EndianUtils.swapShort((short) 1));
		// the number of bits per pixel, which is the colour depth of the image.
		stream.writeShort(EndianUtils.swapShort(bitCount));
		// the compression method being used.
		stream.writeInt(0);
		// image size. The size of the raw bitmap data. 0 is valid for uncompressed.
		stream.writeInt(0);
		// the horizontal resolution of the image. (pixel per meter, signed integer)
		stream.writeInt(0);
		// the vertical resolution of the image. (pixel per meter, signed integer)
		stream.writeInt(0);
		// the number of colours in the colour palette, or 0 to default to 2n.
		stream.writeInt(0);
		// the number of important colours used, or 0 when every colour is important. generally ignored.
		stream.writeInt(0);
	}

	/**
	 * 1bit bitmap파일을 생성한다.
	 * @param oneBitBuffer
	 * @param width
	 * @param height
	 * @return
	 * @throws IOException
	 */
	private static byte[] encodeTo1BitBmp(byte[] oneBitBuffer, int width, int height) throws IOException
	{
		int padding = (4 - (width % 4)) % 4;
		// the size of the BMP file in bytes
		int fileSize = 14 + 40 + 8 + (height * (padding + width))/8;

		ByteArrayOutputStream bytes = new ByteArrayOutputStream(fileSize);
		DataOutputStream out = new DataOutputStream(bytes);

		encodeBmpHeader(out, width, height, fileSize, (short)1);

		//pallet info
		byte[] pallet = new byte[4];
		pallet[0] = (byte)0x00;
		pallet[1] = (byte)0x00;
		pallet[2] = (byte)0x00;
		pallet[3] = (byte)0x00;
		out.write(pallet);

		byte[] rgbQuad = new byte[4];
		rgbQuad[0] = (byte)0xFF;
		rgbQuad[1] = (byte)0xFF;
		rgbQuad[2] = (byte)0xFF;
		rgbQuad[3] = (byte)0x00;
		out.write(rgbQuad);

		out.write(oneBitBuffer);
		byte[] encodeBytes = bytes.toByteArray();
		bytes.close();

		return encodeBytes;
	}

	/**
	 * 카드 승인을 위한 전자 서명(128 * 64 * 1)포맷의 BMP이미지를 생성한다.
	 * @param bmp(RGB_8888)
	 * @return
	 * @throws IOException
	 */
	public static byte[] encodeToSignBmp(Bitmap bmp) throws IOException
	{
		int width = bmp.getWidth();
		int height = bmp.getHeight();

		ByteBuffer bf = ByteBuffer.allocate(width * height * 4); 	//RGB_8888
		bmp.copyPixelsToBuffer(bf);
		byte[] rgb8888ImageData = bf.array();

		//1bit 이미지로 만든다.
		byte[]oneBitImageData = new byte[rgb8888ImageData.length/(4*8)];
		int oneByteColor = 0;
		int bitIndex = 0;
		for(int i = 0, j = 0 ; i < rgb8888ImageData.length ; i+=4, j++)
		{
			int r = rgb8888ImageData[i];			//Red
			int g = rgb8888ImageData[i+1];		//Green
			int b = rgb8888ImageData[i+2];		//Blue
			@SuppressWarnings("unused")
			int a = rgb8888ImageData[i+3];		//Alpha

			int oneBitColor = (r == 0 && g == 0 && b == 0) ? 1 : 0;  //흑백반

			//1bit당 1pixel로 변환
			int k = j%8;
			int l = 7-k;
			oneByteColor |= (oneBitColor << l);

			if(k == 7)
			{
				oneBitImageData[bitIndex] = (byte)oneByteColor;
				bitIndex++;
				oneByteColor = 0;
			}
		}

		//상하로 역전된 이미지를 돌린다.
		int bytesPerRow = width/8;
		byte[] reverseDataBuff = new byte[oneBitImageData.length];
		for(int row = 0 ; row < height; row++)
		{
			for(int col = 0 ; col < bytesPerRow ; col++)
			{
				reverseDataBuff[(bytesPerRow*((height-1)-row)) + col] = oneBitImageData[(bytesPerRow*row) + col];
			}
		}

		return encodeTo1BitBmp(reverseDataBuff, width, height);
	}

	/**
	 * gesture를 카드 승인을 위한 전자 서명(128 * 64 * 1)포맷의 BMP이미지로 생성한다.
	 * @param gesture
	 * @return
	 * @throws IOException
	 */
	public static byte[] encodeToSignBmp(Gesture gesture) throws IOException
	{
		int color = Color.WHITE;
		Bitmap bmp = gesture.toBitmap(128, 128, 0, color);
		return encodeToSignBmp(bmp);
	}

	/**
	 * 카드 승인을 위한 전자 서명(128 * 64 * 1)포맷의 BMP이미지로 변환후 base64로 변환 한다.
	 * @param bmp
	 * @return
	 * @throws IOException
	 */
	public static String encodeToBase64SignBmp(Bitmap bmp) throws IOException
	{
		return Base64.encodeToString(encodeToSignBmp(bmp), 0);
	}

	/**
	 * 카드 승인을 위한 전자 서명(128 * 64 * 1)포맷의 BMP이미지로 변환후 base64로 변환 한다.
	 * @param gesture
	 * @return
	 * @throws IOException
	 */
	public static String encodeToBase64SignBmp(Gesture gesture) throws IOException
	{
		return Base64.encodeToString(encodeToSignBmp(gesture), 0);
	}

	/**
	 * gesture를 카드 승인을 위한 전자 서명(128 * 64 * 1)포맷의 BMP이미지로 생성한다.
	 * @param gesture
	 * @return
	 * @throws IOException
	 */
	public static Bitmap encodeToSignBiteMap(Gesture gesture) throws IOException
	{
		int color = Color.WHITE;
		Bitmap bmp = gesture.toBitmap(128, 64, 0, color);
		return bmp;
	}

}
