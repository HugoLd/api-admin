package org.cap.bean;

public enum MoodValue {

	REALLYBAD(0),
	BAD(1),
	AVERAGE(2),
	GOOD(3),
	REALLYGOOD(4);
	private int value;
	private MoodValue(int value){
		this.value = value;
	}
	public int getValue(){
		return value;
	}
	public static MoodValue getEnumValue(int val){
		for(MoodValue m : MoodValue.values()){
			if(m.value == val){
				return m;
			}			
		}
		return null;
	}
}
