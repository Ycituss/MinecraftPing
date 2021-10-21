package com.ycitus.mcping.framework;

import com.ycitus.mcping.MinecraftPing;
import com.ycitus.mcping.debug.LoggerManager;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.contact.Stranger;
import net.mamoe.mirai.message.code.MiraiCode;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageUtils;
import net.mamoe.mirai.message.data.PlainText;

//用于管理Message的类
public class MessageManager {

	public static String checkLengthAndModifySendMsg(String sendMsg) {
		return checkLengthAndModifySendMsg(sendMsg, "很抱歉，本次发送的字数超过上限，已取消发送！\n字数："
				+ sendMsg.length());
	}

	public static String checkLengthAndModifySendMsg(String sendMsg, String defaultMsg) {

		if (sendMsg == null) {
			return null;
		}

		LoggerManager.logDebug("SendSystem",
				"checkSendMsgLength() -> length = " + sendMsg.length());

		int msgMaxLength = 4500;

		if (sendMsg.length() >= msgMaxLength && msgMaxLength != 0) {
			return defaultMsg;
		} else {
			return sendMsg;
		}
	}

	public static String transSendMsgSpecialCode(String message) {
		return message.replace("#SPACE", " ")
				.replace("#space", " ").replace("#ENTER", "\n")
				.replace("#enter", "\n");
	}

	public static void sendMessageBySituation(long fromGroup, long fromQQ,
											 String msg) {

		LoggerManager.logDebug("SendSystem", "sendBySituation(): fromGroup = " + fromGroup
				+ ", fromQQ = " + fromQQ);

		// 发送目标: QQ群 ?
		if (fromGroup != -1) {

			// 发送目标: QQ群的指定成员 ?
			if (fromQQ != -1) {
				Member member = MinecraftPing.getCurrentBot().getGroup(fromGroup)
						.get(fromQQ);
				MessageManager.sendMessageToQQGroup(fromGroup, MessageUtils
						.newChain(new At(member.getId())).plus(MiraiCode.deserializeMiraiCode("\n" + msg)));
			} else {
				MessageManager.sendMessageToQQGroup(fromGroup, MessageUtils
						.newChain(new PlainText(msg)));
			}

		} else {
			// 发送目标: QQ好友 ?
			if (MinecraftPing.getCurrentBot().getFriends().contains(fromQQ)) {
				MessageManager.sendMessageToQQFriend(fromQQ,
						MiraiCode.deserializeMiraiCode(msg));
				return;
			}

			// 发送目标: 陌生人?
			if (MinecraftPing.getCurrentBot().getStrangers().contains(fromQQ)) {
				MessageManager.sendMessageToStranger(fromQQ,
						MiraiCode.deserializeMiraiCode(msg));
				return;
			}

			// Report Error.
			MinecraftPing.getInstance().getLogger().error("sendMessageBySituation(): can't find send target -> fromGroup = " + fromGroup + ", fromQQ = " + fromQQ);
		}


	}


	public static void sendMessageToQQFriend(long QQ,
											MessageChain messageChain) {

		LoggerManager.logDebug("SendSystem", "sendMessageToQQFriend: " + QQ);
		Friend friend = MinecraftPing.getCurrentBot().getFriend(QQ);
		friend.sendMessage(messageChain);
	}

	public static void sendMessageToStranger(long QQ,
											MessageChain messageChain) {

		LoggerManager.logDebug("SendSystem", "sendMessageToStranger: " + QQ);

		Stranger stranger = MinecraftPing.getCurrentBot().getStranger(QQ);
		stranger.sendMessage(messageChain);

	}

	public static void sendMessageToStranger(long QQ,
											String msg) {
		sendMessageToStranger(QQ, MiraiCode.deserializeMiraiCode(msg));
	}

	public static void sendMessageToQQFriend(long QQ,
											String msg) {
		/** 对发送的文本进行字数检测 **/
		msg = checkLengthAndModifySendMsg(msg);

		sendMessageToQQFriend(QQ, MiraiCode.deserializeMiraiCode(msg));
	}


	public static void sendMessageToQQGroup(long group, MessageChain messageChain) {
		LoggerManager.logDebug("SendSystem", "给某个QQ群发送信息-QQ群的号码为：" + group);

		try {
			MinecraftPing.getCurrentBot().getGroup(group).sendMessage(messageChain);
		} catch (IllegalStateException e) {
			LoggerManager.logDebug("SendSystem",
					"IllegalStateException: Group = " + group);
		}

	}

	public static void sendMessageToQQGroup(long group, String msg) {
		/** 对发送的文本进行字数检测 **/
		msg = checkLengthAndModifySendMsg(msg);
		sendMessageToQQGroup(group, MiraiCode.deserializeMiraiCode(msg));
	}



}
