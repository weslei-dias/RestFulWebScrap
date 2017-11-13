package com.example.travel.controller;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
 
@RestController
public class MultiCityController
{

	@RequestMapping(value = "/consulta/", method = RequestMethod.GET)
	public List<Object> getDetalhesVoos(@RequestParam("url") String url,
			@RequestParam("qtdeUrls") int qtdeUrls) throws InterruptedException {
		List<Object> detalhes = new ArrayList<Object>();

		WebDriver driver = instanciarDriver();
		driver.get(url);

		for (int i = 0; i < qtdeUrls; i++) {
			Thread.sleep(2000);
			WebElement elemento = (new WebDriverWait(driver, 60))
					.until(ExpectedConditions.presenceOfElementLocated(By
							.className("LJV2HGB-d-Ab")));
			elemento.click();
		}
		Thread.sleep(2000);

		List<WebElement> detalhesVoo = (new WebDriverWait(driver, 12))
				.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By
						.className("LJV2HGB-dc-h")));
		List<WebElement> nomeVoo = (new WebDriverWait(driver, 12))
				.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By
						.className("LJV2HGB-dc-f")));
		
		int i = 0;
		for (WebElement elemento : detalhesVoo) {
			String hmtlDetalheVoo = elemento.getAttribute("innerHTML");
			String detalheDestino = nomeVoo.get(i).getText();

			Map<String, Object> obj = new HashMap<String, Object>();
			obj.put("detalhesVoo", hmtlDetalheVoo);
			obj.put("destalheDestino", detalheDestino);
			detalhes.add(obj);
			i++;
		}

		driver.close();

		return detalhes;
	}
    
    public WebDriver instanciarDriver()
    {
    	DesiredCapabilities dCaps = new DesiredCapabilities();
    	dCaps.setJavascriptEnabled(true);
    	dCaps.setCapability(
				"phantomjs.page.settings.userAgent",
				"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
		dCaps.setBrowserName("Chrome");
		dCaps.setVersion("59.0.3071.115");
    	dCaps.setCapability(
				PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
				"/home/weslei.dias/travel/sistema/phantom/phantomjs");
    	WebDriver driver = new PhantomJSDriver(dCaps);
    	return driver;    	
    }
    
    
    
    
}