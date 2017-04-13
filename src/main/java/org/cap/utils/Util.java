package org.cap.utils;

import java.util.UUID;

public class Util {
	
	public static String generateUUID(String uuid , String mail , String date){
		return UUID.nameUUIDFromBytes((uuid + "+" + mail + "+" + date).getBytes()).toString();
	}
}
