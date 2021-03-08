package test.backend.assigment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import test.backend.assigment.dto.SKU;

@Service
public class CatalogService {
	
	@Value("${catalog.service.url}")
	private String url;

	
	@Autowired
	RestTemplate restTemplate;
	
	
	public SKU getSKU(String skuId)
	{
		String skuUrl = url + skuId; 
		SKU sku = restTemplate.getForObject(skuUrl, SKU.class);
		return sku;
		
	}

}
