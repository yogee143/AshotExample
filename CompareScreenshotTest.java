package CompareScreenshot;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;

public class CompareScreenshotTest {

	WebDriver driver = null;
	
	@Test
	public void TC1_CompareImages() throws Exception{

		System.out.println("In Method....");
		System.setProperty("webdriver.chrome.driver", "C:\\Yogesh Learning\\Drivers\\chromedriver_win32_2.41\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("http://www.clocktab.com/");

		String image1Path="./ClockImage1.png";
		String image2Path="./ClockImage2.png";
		
		
		Thread.sleep(5000);
		
		Screenshot sc1 = new AShot().takeScreenshot(driver);
		ImageIO.write(sc1.getImage(), "png", new File (image1Path));

		
		//Screenshot sc2 = new AShot().coordsProvider(new WebDriverCoordsProvider()).addIgnoredElement(By.xpath("(//*[@id='digit2'])[1]")).takeScreenshot(driver,digit);
		Screenshot sc2 = new AShot().takeScreenshot(driver);
		ImageIO.write(sc2.getImage(), "png", new File (image2Path));

		BufferedImage expectedImage = ImageIO.read(new File(image1Path));
		BufferedImage actualImage = ImageIO.read(new File(image2Path));

		ImageDiffer imageDifferWithIgnored = new ImageDiffer().withColorDistortion(1);	
		ImageDiff diff = imageDifferWithIgnored.makeDiff(expectedImage, actualImage);
		BufferedImage diffImage = diff.getMarkedImage();
		
		ImageIO.write(diffImage, "png", new File ("./ClockDifference.png"));
		
		Assert.assertFalse("Images are not same",diff.hasDiff());
		

		driver.quit();
	}

}
