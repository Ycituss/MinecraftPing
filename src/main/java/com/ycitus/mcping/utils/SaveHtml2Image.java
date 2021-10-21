package com.ycitus.mcping.utils;

import com.ycitus.mcping.debug.LoggerManager;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.*;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class SaveHtml2Image {

    public String html2Image(String url, String id) throws InterruptedException, TimeoutException, IOException {
        String imagePath = "data/MinecraftPing/" + id + ".jpg";
        File image = new File(imagePath);
        if (image.exists()) return imagePath;
        String webDriverPath = "data/MinecraftPing/";
        if (System.getProperties().getProperty("os.name").contains("Windows")){
            webDriverPath += "phantomjs.exe";
        }else {
            webDriverPath += "phantomjs";
        }
        String htmlPath = saveHtml(url, id);
        if (htmlPath.isEmpty()) LoggerManager.logDebug("McVersion", "Get Page Failed!", true);
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability("acceptSslCerts", true);
        desiredCapabilities.setCapability("takesScreenshot",true);
        desiredCapabilities.setCapability("cssSelectorsEnabled", true);
        desiredCapabilities.setJavascriptEnabled(true);
        desiredCapabilities.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, webDriverPath);
        PhantomJSDriver phantomJSDriver = new PhantomJSDriver(desiredCapabilities);
        WebDriver driver = phantomJSDriver;
        driver.manage().window().setSize(new Dimension(640, 1080));
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        driver.get(htmlPath);
        driver.navigate().refresh();
        Thread.sleep(8000);
        String pageHtml = driver.getPageSource();
        File srcfile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(srcfile, image);
        driver.close();
        return imagePath;
    }

    public String saveHtml(String url, String id) throws IOException {
        HttpGet httpGet = new HttpGet();
        String str = httpGet.doGet(url);
        if (str.isEmpty()) return null;
        Document document = Jsoup.parse(str);

        Elements elements = document.getElementsByClass("text parbase aem-GridColumn aem-GridColumn--default--12");
        Element element = elements.get(0);
        Elements elements1 = document.getElementsByClass("row justify-content-center text-center");
        Element element1 = elements1.get(0);
        String html = "<meta charset=\"UTF-8\">\n<body style=\"background-color:white;\">";
        html = html + element1.append(element.toString()) + "</body>";
        String filePath = "data/MinecraftPing/" + id + ".html";
        File file= new File(filePath);
        if(!file.exists()){
            file.createNewFile();
        }
        FileWriter writer = new FileWriter(filePath);
        writer.write(html);
        writer.flush();
        writer.close();
        return filePath;
    }
}
