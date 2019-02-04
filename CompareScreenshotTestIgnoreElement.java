package CompareScreenshot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;
import ru.yandex.qatools.ashot.coordinates.Coords;
import ru.yandex.qatools.ashot.coordinates.WebDriverCoordsProvider;

public class CompareScreenshotTestIgnoreElement {

	WebDriver driver = null;
	
	@Test
	public void TC1_CompareImages() throws Exception{

		System.out.println("In Method....");
		System.setProperty("webdriver.chrome.driver", "C:\\Yogesh Learning\\Drivers\\chromedriver_win32_2.41\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("http://www.clocktab.com/");

		Set<By> bySet = new HashSet<>();
		bySet.add(By.xpath("(//td[@id='digit1'])[1]"));
		bySet.add(By.xpath("(//td[@id='digit2'])[1]"));
		
		String expImagePath="./ExpectedImage.png";
		String actualImagePath="./ActualImage.png";

		Thread.sleep(10000);
		
		Screenshot sc1 = new AShot().takeScreenshot(driver);
		ImageIO.write(sc1.getImage(), "png", new File (expImagePath));

		Screenshot sc2 = new AShot().coordsProvider(new WebDriverCoordsProvider()).ignoredElements(bySet).takeScreenshot(driver);
		ImageIO.write(sc2.getImage(), "png", new File (actualImagePath));

		Set<Coords> coordsIgnore = sc2.getIgnoredAreas();
		
		Screenshot sc3 = new Screenshot(ImageIO.read(new File(expImagePath)));
		sc3.setIgnoredAreas(coordsIgnore);
		
		ImageDiffer imageDifferWithIgnored = new ImageDiffer();	
		ImageDiff diff = imageDifferWithIgnored.makeDiff(sc3, sc2);
		
		BufferedImage diffImage = diff.getMarkedImage();
		ImageIO.write(diffImage, "png", new File ("./ClockDifference.png"));
		
		Assert.assertFalse("Images are not same",diff.hasDiff());
		driver.quit();
	}

}
