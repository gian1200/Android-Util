package com.gian1200.util.achievements;

import java.util.Vector;

public class AchievementManager {
	Vector<Achievement> achievements = new Vector<Achievement>();

	public Achievement createAchievement(int id, String name, String description) {
		Achievement achievement = new Achievement(id, name, description);
		achievements.add(achievement);
		return achievement;
	}
}
