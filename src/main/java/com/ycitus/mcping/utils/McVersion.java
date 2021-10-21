package com.ycitus.mcping.utils;


import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.ycitus.mcping.debug.LoggerManager;
import com.ycitus.mcping.files.FileManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class McVersion {

    String releaseVersionUrl = FileManager.applicationConfig_File.getSpecificDataInstance().Server.ReleaseVersionUrl;
    String releaseVersion = FileManager.applicationConfig_File.getSpecificDataInstance().Server.ReleaseVersion;
    String snapshotVersionUrl = FileManager.applicationConfig_File.getSpecificDataInstance().Server.SnapshotVersionUrl;
    String snapshotVersion = FileManager.applicationConfig_File.getSpecificDataInstance().Server.SnapshotVersion;

    public McVersion() {
        String path = "https://launchermeta.mojang.com/mc/game/version_manifest.json";
        String str = "";

        try {
            HttpGet httpGet = new HttpGet();
            str = httpGet.doGet(path);
            str = str.replace("\"", "").replace("{", "").replace("}", "");
            String[] strings = str.split(":|,");
            String release = strings[2].replace(" ", "");
            String snapshot = strings[4].replace(" ", "");
            if (!releaseVersion.equals(release)){
                releaseVersion = release;
                releaseVersionUrl = "https://www.minecraft.net/zh-hans/article/minecraft-java-edition";
                String[] releaseStrings = release.split("\\.");
                for (String string : releaseStrings) {
                    releaseVersionUrl = releaseVersionUrl + "-" + string;
                }
            }
            if (!snapshotVersion.equals(snapshot)){
                snapshotVersion = snapshot;
                snapshotVersionUrl = "https://www.minecraft.net/zh-hans/article/minecraft";
                if (snapshot.contains("-")) {
                    String[] snapshotStrings = snapshot.split("-");
                    for (String string : snapshotStrings[0].split("\\.")) {
                        snapshotVersionUrl = snapshotVersionUrl + "-" + string;
                    }
                    String substring = snapshotStrings[1].substring(0, snapshotStrings[1].length() - 1);
                    String num = snapshotStrings[1].substring(snapshotStrings[1].length() - 1);
                    if (substring.equals("rc")) {
                        snapshotVersionUrl = snapshotVersionUrl + "-release-candidate-" + num;
                    }else if (substring.equals("pre")) {
                        snapshotVersionUrl = snapshotVersionUrl + "-pre-release-" + num;
                    }
                }else {
                    snapshotVersionUrl = snapshotVersionUrl + "-snapshot-" + snapshot;
                }
            }
        } catch (Exception e) {
            LoggerManager.logDebug("McVersion", "Get Version Failed!", true);
        }
    }

    public String getReleaseVersion() {
        return releaseVersion;
    }

    public String getReleaseVersionUrl() {
        return releaseVersionUrl;
    }

    public String getSnapshotVersion() {
        return snapshotVersion;
    }

    public String getSnapshotVersionUrl() {
        return snapshotVersionUrl;
    }
}
