package test.backend.assigment.mock;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.matching.UrlPattern;

import test.backend.assigment.dto.Address;
import test.backend.assigment.dto.DateRange;
import test.backend.assigment.dto.Item;
import test.backend.assigment.dto.Order;
import test.backend.assigment.dto.SKU;
import test.backend.assigment.dto.Shipment;
import test.backend.assigment.dto.Status;
import test.backend.assigment.dto.Type;

@Component
public class MockUtil {

	WireMockServer wireMockServer = new WireMockServer(options().port(8080));;
	
	Map<String, List<Long>> custIdToOrderId = new HashMap<>();

	Map<Long, Order> orderMap = new HashMap<>();
	
	ObjectMapper mapper = new ObjectMapper();
	
	// SKU Map
	Map<String, SKU> skuMap = new HashMap<>();

	@PostConstruct
	public void init() throws JsonProcessingException {
		createOrders();
		createSku();
		
		wireMockServer.start();
		mockOrderService();
		mockCatelogService();

	}

	public void mockOrderService() throws JsonProcessingException {
		
		UrlPattern externalUrl = urlPathMatching("/order/[1-9]+");
		configureFor("localhost", 8080);
		stubFor(get(externalUrl).willReturn(aResponse().withStatus(200)
				.withHeader("Content-Type", "application/json").withBody(mapper.writeValueAsString(orderMap.get(1L)))));
	}
	
	public void mockCatelogService() throws JsonProcessingException {
		
		UrlPattern externalUrl = urlPathMatching("/catalog/sku/1");
		configureFor("localhost", 8080, "/");
		stubFor(get(externalUrl).willReturn(aResponse().withStatus(200)
				.withHeader("Content-Type", "application/json").withBody(mapper.writeValueAsString(skuMap.get("1")))));
		
		externalUrl = urlPathMatching("/catalog/sku/2");

		wireMockServer.stubFor(get(externalUrl).willReturn(aResponse().withStatus(200)
				.withHeader("Content-Type", "application/json").withBody(mapper.writeValueAsString(skuMap.get("2")))));
	}

	private void createOrders() {

		DateRange estimatedShipDateRangeItem1 = new DateRange("Original Estimated Date",
				"2021-03-10T08:32:12.124-04:00", "2021-03-10T11:00:52.774-03:00");
		DateRange newEstimatedShipDateRangeItem1 = new DateRange("New Estimated Date",
				"2021-03-010T14:39:52.774-05:00", "2021-03-191T18:00:52.774-04:00");

		// Item 1 is in Ordered status
		Item item1 = new Item(101L, "Aipods", "plan123", "1", 1, "409.406.3322", Status.ordered, false,
				estimatedShipDateRangeItem1, newEstimatedShipDateRangeItem1, true, "2021-03-07T13:39:52.774-05:00");

		// Item 2 is in Shipped Status
		DateRange estimatedShipDateRangeItem2 = new DateRange("Estimated ship date",
				"2021-02-04T13:39:52.774-05:00", "2021-02-08T11:00:52.774-04:00");
		Item item2 = new Item(102L, "Aipods Pro", "plan113", "2", 1, "449.506.9922", Status.shipped, false,
				estimatedShipDateRangeItem2, null, false, null);

		ArrayList<Item> listOfAllItems = new ArrayList<>();
		listOfAllItems.add(item1);
		listOfAllItems.add(item2);

		// List of item shipped with the Shipment below
		List<Long> items = new ArrayList<>();
		items.add(item1.getId());
		items.add(item2.getId());
		Shipment shipment = new Shipment(1001L, items, "USPS", "234567","https://tools.tracking?id=234567",
				"2021-03-08T13:39:52.774-05:00", "2021-03-04T13:39:52.774-05:00");

		Address address = new Address("Vally Drive", "Robert Jogn", "NY", "10011");

		ArrayList<Shipment> shipments = new ArrayList<>();
		shipments.add(shipment);

		Order order = new Order(100001L, 1L, "customer1", listOfAllItems, Status.shipped, false, address, shipments);
		orderMap.put(1L, order);
		List<Long> orders = new ArrayList<>();
		orders.add(1L);
		custIdToOrderId.put("customer1", orders);

	}
	
	public void createSku() {
		
		SKU sku1 = new SKU("1",Type.plan,"Gold Plan");
		SKU sku2 = new SKU("2",Type.hardgood,"Charger");
		
		skuMap.put(sku1.getId(), sku1);
		skuMap.put(sku2.getId(), sku2);
	}

	public Map<String, List<Long>> getCustIdToOrderId() {
		return custIdToOrderId;
	}

	public void setCustIdToOrderId(Map<String, List<Long>> custIdToOrderId) {
		this.custIdToOrderId = custIdToOrderId;
	}

	public Map<Long, Order> getOrderIdToOrder() {
		return orderMap;
	}

	public void setOrderIdToOrder(Map<Long, Order> orderIdToOrder) {
		this.orderMap = orderIdToOrder;
	}

	public Map<String, SKU> getSkuIdToSku() {
		return skuMap;
	}

	public void setSkuIdToSku(Map<String, SKU> skuIdToSku) {
		this.skuMap = skuIdToSku;
	}
}
