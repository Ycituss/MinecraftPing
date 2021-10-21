package com.ycitus.mcping.framework;

import com.ycitus.mcping.MinecraftPing;
import net.mamoe.mirai.contact.*;

public class BotManager {

	public static ContactList<Friend> getAllQQFriends() {
		return MinecraftPing.getCurrentBot().getFriends();
	}

	public static ContactList<Group> getAllQQGroups() {
		return MinecraftPing.getCurrentBot().getGroups();
	}

	public static ContactList<Stranger> getAllStrangers() {
		return MinecraftPing.getCurrentBot().getStrangers();
	}

	public static Member getGroupMemberCard(long fromGroup, long fromQQ) {
		Group group = MinecraftPing.getCurrentBot().getGroup(fromGroup);
		Member groupMember = group.get(fromQQ);
		return groupMember;
	}

	public static String getGroupMemberName(Member groupMember) {

		String ret = groupMember.getNameCard();
		if (ret.isEmpty()) {
			ret = groupMember.getNick();
		}

		return ret;
	}


}
