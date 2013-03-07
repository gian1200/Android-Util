package com.gian1200.util.achievements;

class Achievement {
	int id;
	String name, description;

	Achievement(String name) {
		this.name = name;
	}

	Achievement(int id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
	}

}
