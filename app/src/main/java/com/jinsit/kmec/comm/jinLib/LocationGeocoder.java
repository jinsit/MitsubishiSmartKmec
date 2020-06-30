package com.jinsit.kmec.comm.jinLib;

import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

/**
 * 위도경도를 가지고 한글주소를 가져오는 클라스
 * LatLng 클래스를 써서 사용했지만
 * googlePlay라이브러리를 쓰지 않기때문에 그냥 getSearchAddr만 사용한다.
 * @author 원성민
 *
 */
public class LocationGeocoder {
	//Context context;
	Geocoder geocoder;

	public LocationGeocoder(Context context) {
		geocoder = new Geocoder(context, Locale.KOREAN);
	}


	public String getSearchAddress(double latitude, double longitude) {
		String returnAddress=null;
		List<Address> addressList = null;
		try {
			addressList = geocoder.getFromLocation(latitude, longitude, 3);
			if (addressList != null) {
				//contentsText.append("\n 찾아온 개수:" + addressList.size());
				for (int i = 0; i < addressList.size(); i++) {
					Address address = addressList.get(i);
					returnAddress = address.getAddressLine(0);
				}
			}
		} catch (Exception e) {
			returnAddress = "";
		}
		return returnAddress;
	}


/*	public LatLng searchLocation(String searchStr) {
		List<Address> addressList = null;
		LatLng latlng = null;

		try {

			addressList = geocoder.getFromLocationName(searchStr, 5);

			if (addressList != null) {
				//contentsText.setText("\n 찾아온 개수: " + addressList.size());

				for (int i = 0; i < addressList.size(); i++) {
					Address address = addressList.get(i);

					contentsText.append("\n 주소 :" + address.getAddressLine(0));
					contentsText.append("\n 위도: " + address.getLatitude());
					contentsText.append("\n 경도: " + address.getLongitude());
					Log.w("lat", "lati=" + address.getLatitude() + "longi="+address.getLongitude());
					latlng = new LatLng(address.getLatitude(),address.getLongitude());
				}

			}

		} catch (Exception e) {
		}
		return latlng;
	}

	public String searchAddress(double latitude, double longitude) {
		String returnAddress=null;
		List<Address> addressList = null;
		try {
			addressList = geocoder.getFromLocation(latitude, longitude, 3);
			if (addressList != null) {
				//contentsText.append("\n 찾아온 개수:" + addressList.size());
				for (int i = 0; i < addressList.size(); i++) {
					Address address = addressList.get(i);
					Log.i("lat", "lati=" + address.getLatitude() + "longi="+address.getLongitude());
					returnAddress = address.getAddressLine(0);
					contentsText.append("\n 주소:" + address.getAddressLine(0));
					contentsText.append("\n 위도:" + address.getLatitude());
					contentsText.append("\n 경도:" + address.getLongitude());
				}
			}
		} catch (Exception e) {
		}
		return returnAddress;
	}*/





}
