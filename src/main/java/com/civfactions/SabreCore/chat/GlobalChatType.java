package com.civfactions.SabreCore.chat;

public enum GlobalChatType {
	GLOBAL("global"),
	WORLD("world"),
	RADIUS("radius");

	private final String text;

	GlobalChatType(String text) {
		this.text = text;
	}
	
	public static GlobalChatType fromString(String text) {
		if (text != null) {
			for (GlobalChatType c : GlobalChatType.values()) {
				if (text.equalsIgnoreCase(c.text)) {
					return c;
				}
			}
		}
		return GLOBAL;
	}
	
	@Override
	public String toString() {
		return text;
	}
}
