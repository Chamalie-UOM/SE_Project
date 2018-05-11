package GUAclasses;

import java.util.ArrayList;

public class Password {
	ArrayList <String> PassSeq= new ArrayList<String>();
	
	public String getPassString() {
		StringBuilder sb = new StringBuilder();
		for(String s : PassSeq) {
			sb.append(s);
		}
		String key = sb.toString();
		return key;
	}

}
