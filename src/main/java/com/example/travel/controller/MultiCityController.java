package com.example.travel.controller;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
		
		if(qtdeUrls > 5) {
			String[] urlFormatada =  url.split("\\*");
			System.out.println(urlFormatada);
			
			String primeiraUrl = urlFormatada[0];
			
			String ultimaUrl = urlFormatada[urlFormatada.length - 1];
			
			String antepenultimaUrl = urlFormatada[urlFormatada.length - 2];
			
			String urlPrimeiroUltimo = null;
			String urlMiolo = "";
			int qtdeMiolo = 0;
			
			for (String valor : urlFormatada) {
				
				if (valor.equals(primeiraUrl)) {
					
					String[] arrayPrimeiraUrl = primeiraUrl.split("\\=");
					urlPrimeiroUltimo = arrayPrimeiraUrl[1];
					
				}else if (valor.equals(ultimaUrl)) {
					String[] arrayUltimaUrl = ultimaUrl.split(";");
					urlPrimeiroUltimo += "*" + arrayUltimaUrl[0];
				}else {
					if (valor.equals(antepenultimaUrl)) {
						urlMiolo += valor;
					}else {
						urlMiolo += valor + "*";
					}
					qtdeMiolo++;
				}
				
				
			}
			
			String urlIdaEVolta = "https://www.google.com.br/flights/#search;iti=" + urlPrimeiroUltimo + ";tt=m";
			WebDriver driverIdaEVolta = instanciarDriver();
			driverIdaEVolta.get(urlIdaEVolta);
			List<Object> detalhesIdaEVolta =  getMioloDestinos(2, driverIdaEVolta);
			
			Object firstItem = detalhesIdaEVolta.get(0);
			Object lastItem = detalhesIdaEVolta.get(1);
			
			String urlDestinos = "https://www.google.com.br/flights/#search;iti=" + urlMiolo + ";tt=m";
			WebDriver driverMiolo = instanciarDriver();
			driverMiolo.get(urlDestinos);
			List<Object> detalhesMiolo =  getMioloDestinos(qtdeMiolo, driverMiolo);
			List<Object> novosDetalhes = new ArrayList<Object>();
			novosDetalhes.add(firstItem);
			for (Object object : detalhesMiolo) {
				novosDetalhes.add(object);
			}
			novosDetalhes.add(lastItem);
			detalhes = novosDetalhes;
			
			
		}else {
			WebDriver driver = instanciarDriver();
			driver.get(url);
			List<Object> detalhesUrl =  getMioloDestinos(qtdeUrls, driver);
			detalhes = detalhesUrl;
		}

		return detalhes;
	}

	private List<Object> getMioloDestinos(int qtdeUrls, WebDriver driver) throws InterruptedException {
		
		List<Object> detalhes = new ArrayList<Object>();

		
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