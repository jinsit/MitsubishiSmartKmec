package com.jinsit.kmec.IR.NM.NFC;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.Tag;
import android.nfc.tech.Ndef;

public class NfcParser {

    // Utility class
    private NfcParser() {

    }

    private static String readText(NdefRecord record) {
        byte[] payload = record.getPayload();
        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
        int languageCodeLength = payload[0] & 0063;
        try {
			return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
    }
	
	public static String getTagText(Tag detectedTag)
	{
		Ndef ndef = Ndef.get(detectedTag);
		if (ndef == null) {
			return "";
		}
		NdefMessage ndefMessage = ndef.getCachedNdefMessage();
		String text = "";
		try {
			NdefRecord[] records = ndefMessage.getRecords();
			for (NdefRecord ndefRecord : records) {
				if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN
						&& Arrays.equals(ndefRecord.getType(),
								NdefRecord.RTD_TEXT)) {
					text = readText(ndefRecord);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return "";
		}
		return text;
	}
     
	public static String getTagId(Tag detectedTag)
	{
		return toHexString(detectedTag.getId());
	}
	
	private static final String CHARS = "0123456789ABCDEF";

	private static String toHexString(byte[] data) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < data.length; ++i) {
			sb.append(CHARS.charAt((data[i] >> 4) & 0x0F)).append(
					CHARS.charAt(data[i] & 0x0F));
		}
		return sb.toString();
	}
}
