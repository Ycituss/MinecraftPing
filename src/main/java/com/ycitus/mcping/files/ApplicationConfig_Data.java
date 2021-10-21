package com.ycitus.mcping.files;

import java.util.ArrayList;
import java.util.HashMap;

public class ApplicationConfig_Data {


	public Debug Debug = new Debug();
	public class Debug {
		public boolean enable = false;
	}

	public Admin Admin = new Admin();
	public class Admin {

		public ArrayList<Long> botAdministrators = new ArrayList<Long>() {
			{
				this.add(2799282971L);
			}
		};
	}

	public Server Server = new Server();
	public class Server {
		public HashMap<Long, String> serverList = new HashMap<Long, String>() {
			{
			}
		};

		public String User_Agent = "PostmanRuntime/7.28.4";

		public boolean isCheckStatus = false;
		public boolean isShowFavicon = true;
		public boolean isCheckVersion = false;
		public boolean isRelease = false;
		public String SnapshotVersion = "21w42a";
		public String SnapshotVersionUrl = "https://www.minecraft.net/zh-hans/article/minecraft-snapshot-21w42a";
		public String ReleaseVersion = "1.17.1";
		public String ReleaseVersionUrl = "https://www.minecraft.net/zh-hans/article/minecraft-java-edition-1-17-1";
	}

	public TimeSet TimeSet = new TimeSet();
	public class TimeSet{
		public int sentence_hour = 7;
		public int sentence_min = 20;
		public int temp_hour = 0;
		public int temp_min = 0;
	}

	public PlayerDate PlayerDate = new PlayerDate();
	public class PlayerDate {
		public HashMap<String, Integer> PlayerTimeNow = new HashMap<String, Integer>() {
			{

			}
		};
		public HashMap<String, Integer> PlayerTimeTemp = new HashMap<String, Integer>() {
			{

			}
		};
	}


}
