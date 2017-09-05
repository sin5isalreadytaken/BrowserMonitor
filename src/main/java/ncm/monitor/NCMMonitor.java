package ncm.monitor;

import ncm.config.Configure;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by wenxiangzhou214164 on 2017/7/25.
 */
public abstract class NCMMonitor<T> {
    private Logger logger = LoggerFactory.getLogger(getClass());

    protected List<T> ts;
    protected WebDriver driver = null;
    protected boolean ready = false;
    protected int retry = 10;
    private long defaultTimeout = 10 * 1000;

    @Value(value = "${schedule.recordUrl}")
    protected String url;
    @Value(value = "${schedule.user}")
    protected String user;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void task(Date date) {
        getContent(date);
        handle(date);
    }

    protected boolean loginAndJump(boolean login) {
        if (login) {
            try {
                if (driver != null) {
                    driver.close();
                }
                if (null == driver) {
                    driver = makeDriver();
                    logger.info("[loginAndJump] open browser");
                }
                if (driver == null) {
                    logger.info("[loginAndJump] make driver failed");
                    return false;
                }
                driver.navigate().to(url);
                waitForElement(By.cssSelector("a.itm-2[data-type=\"netease\"]")).click();
                List<WebElement> logins = waitForElements(By.cssSelector("input.js-input.u-txt"));
                logins.get(0).clear();
                logins.get(1).clear();
                logins.get(0).sendKeys(Configure.get("account"));
                logins.get(1).sendKeys(Configure.get("password"));
                Thread.sleep(1000);
                waitForElement(By.cssSelector("a.js-primary.u-btn2.u-btn2-2")).click();
                Thread.sleep(1000);
                return true;
            } catch (Exception e) {
                logger.info("login failed");
                return false;
            }
        }
        else {
            return loginAndJump();
        }
    }

    protected boolean loginAndJump() {
        if (driver != null) {
            driver.close();
        }
        if (null == driver) {
            driver = makeDriver();
            logger.info("[loginAndJump] open browser");
        }
        if (driver == null) {
            logger.info("[loginAndJump] make driver failed");
            return false;
        }
        driver.navigate().to(url);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

    protected void refresh() {
        ts = null;
        try {
            if (!checkUrl(url)) {
                driver.navigate().to(url);
            }
            else {
                driver.navigate().refresh();
            }
            logger.info("[refresh] refresh");
            Thread.sleep(3000);
        } catch (Exception e) {
            logger.info("[refresh] open browser while refresh failed");
            driver = null;
            ready = false;
        }
    }

    protected abstract void getContent(Date date);

    protected abstract void handle(Date date);

    public WebDriver makeDriver() {
        FirefoxProfile profile = new FirefoxProfile();
        profile.setEnableNativeEvents(true);
        profile.setPreference("intl.charset.default", "UTF-8");
//        profile.setPreference("general.useragent.override", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:2.0b9pre) Gecko/20101228 Firefox/4.0b9pre");
//        String path = NCMMonitor.class.getResource("/geckodriverAll/win64/geckodriver.exe").getPath();
//        System.setProperty("webdriver.gecko.driver", path);
        System.setProperty("webdriver.gecko.dirver", "/mnt/sin5files/homework/idea/geckodriver");
        WebDriver webDriver = new FirefoxDriver(profile);
        return webDriver;
    }

    public WebElement waitForElement(WebElement we, By by) {
        return waitForElement(we, by, defaultTimeout);
    }

    public WebElement waitForElement(By by) {
        return waitForElement(by, defaultTimeout);
    }

    public WebElement waitForElement(By by, long lTimeout) {
        long lStart = System.currentTimeMillis();
        while (System.currentTimeMillis() - lStart < lTimeout) {
            try {
                WebElement pElemenet = driver.findElement(by);
                if (null != pElemenet) {
                    return pElemenet;
                } else {
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                    }
                }
            } catch (Exception e) {
            }
        }
        logger.info("[waitForElement] waitForElement {} timeout.", by.toString());
        return null;
    }

    public WebElement waitForElement(WebElement we, By by, long lTimeout) {
        long lStart = System.currentTimeMillis();
        while (System.currentTimeMillis() - lStart < lTimeout) {
            try {
                WebElement pElemenet = we.findElement(by);
                if (null != pElemenet) {
                    return pElemenet;
                } else {
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                    }
                }
            } catch (Exception e) {
            }
        }
        logger.info("[waitForElement] waitForElement {} timeout.", by.toString());
        return null;
    }

    public List<WebElement> waitForElements(WebElement we, By by) {
        return waitForElements(we, by, defaultTimeout);
    }

    public List<WebElement> waitForElements(WebElement we, By by, long lTimeout) {
        long lStart = System.currentTimeMillis();
        while (System.currentTimeMillis() - lStart < lTimeout) {
            try {
                List<WebElement> pElemenets = we.findElements(by);
                if (null != pElemenets) {
                    return pElemenets;
                } else {
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                    }
                }
            } catch (Exception e) {
            }
        }
        logger.info("[waitForElements] waitForElements {} timeout.", by.toString());
        return null;
    }

    public List<WebElement> waitForElements(By by, long lTimeout) {
        long lStart = System.currentTimeMillis();
        while (System.currentTimeMillis() - lStart < lTimeout) {
            try {
                List<WebElement> pElemenets = driver.findElements(by);
                if (null != pElemenets) {
                    return pElemenets;
                } else {
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                    }
                }
            } catch (Exception e) {
            }
        }
        logger.info("[waitForElements] waitForElements {} timeout.", by.toString());
        return null;
    }

    public List<WebElement> waitForElements(By by) {
        return waitForElements(by, defaultTimeout);
    }

    public boolean checkUrl(String url) {
        try {
            if (url.equals(driver.getCurrentUrl())) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    protected boolean waitForUrl(String strUrl) {
        return waitForUrl(strUrl, defaultTimeout);
    }

    protected boolean waitForUrl(String strUrl, long lTimeout) {
        long lStart = System.currentTimeMillis();
        while (System.currentTimeMillis() - lStart < lTimeout) {
            if (driver.getCurrentUrl().contains(strUrl)) {
                logger.info("[waitForUrl] success at:{}", driver.getCurrentUrl());
                return true;
            } else {
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                }
            }
        }
        logger.info("[waitForUrl] wait for url:{} timeout at:{}", strUrl, driver.getCurrentUrl());
        return false;
    }

    /**
     * 获取指定元素的截图byte[]
     */
    protected byte[] captureElement(WebElement we) throws Exception {
        BufferedImage bi = getElementImage(we);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bi, "png", bos);
        return bos.toByteArray();
    }

    /**
     * 获取指定元素的截图
     */
    protected BufferedImage getElementImage(WebElement we) {
        Point location = we.getLocation();
        Dimension size = we.getSize();
        BufferedImage originalImage = null;
        try {
            originalImage = ImageIO.read(new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
        } catch (IOException e) {
            logger.info("read img failed");
        }
        return originalImage.getSubimage(location.getX(), location.getY(), size.getWidth(), size.getHeight());
    }

    protected void executeJS(String js) {
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript(js);
    }

    protected void nonBlockingNavigate(String url) {
        executeJS("window.location.href='" + url + "'");
    }

    protected void nonBlockingRefresh(String url) {
        driver.navigate().to("http://www.baidu.com");
        nonBlockingNavigate(url);
    }

}
