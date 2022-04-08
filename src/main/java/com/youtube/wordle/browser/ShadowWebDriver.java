package com.youtube.wordle.browser;

import com.youtube.wordle.solver.Word;
import io.github.sukgu.Shadow;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class ShadowWebDriver implements AutoCloseable
{
    private static final Logger logger = LoggerFactory.getLogger(ShadowWebDriver.class);

    private final WebDriver driver;
    private final Shadow shadow;

    private ShadowWebDriver(WebDriver driver)
    {
        this.driver = driver;
        this.shadow = new Shadow(driver);
    }

    public static ShadowWebDriver open()
    {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        options.addArguments("--headless");

        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        return new ShadowWebDriver(driver);
    }

    public void get(String url)
    {
        driver.get(url);
    }

    public Optional<Element> findElement(CssSelector selector)
    {
        List<Element> rejectButtons = findElements(selector);

        if (rejectButtons.size() != 1)
        {
            return Optional.empty();
        }

        return Optional.of(rejectButtons.get(0));
    }

    public List<Element> findElements(CssSelector selector)
    {
        return shadow.findElements(selector.toString()).stream().map(Element::new).toList();
    }

    public Element waitForElement(CssSelector selector)
    {
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(driver -> !shadow.findElements(selector.toString()).isEmpty());

        return findElement(selector).orElseThrow();
    }

    @Override
    public void close()
    {
        driver.close();
    }

    public class Element
    {
        private final WebElement webElement;

        public Element(WebElement webElement)
        {
            this.webElement = webElement;
        }

        public void click()
        {
            webElement.click();
        }

        public void sendWord(Word word)
        {
            try
            {
                Thread.sleep(3_000L); // Yes, it's filthy
                webElement.sendKeys(word.toString(), Keys.ENTER);
            }
            catch (InterruptedException e)
            {
                logger.warn("Interrupted waiting to send word", e);
            }
        }

        public List<Element> findChildren(CssSelector selector)
        {
            return shadow.findElements(webElement, selector.toString()).stream().map(Element::new).toList();
        }

        public String getAttribute(String name, Function<String, Boolean> acceptable)
        {
            new WebDriverWait(driver, Duration.ofSeconds(10)).until(driver -> {
                String value = webElement.getAttribute(name);
                return value != null && acceptable.apply(value);
            });

            return webElement.getAttribute(name);
        }

        @Override
        public String toString()
        {
            return webElement.toString();
        }
    }
}
