package test.backend.assigment.api;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import test.backend.assigment.dto.OrderDetailResponsne;
import test.backend.assigment.dto.OrderSummaryResponse;
import test.backend.assigment.service.OrderService;

@RestController
public class OrderController {

	@Autowired
	OrderService orderService;

	@GetMapping("customer/{custId}/order/summary")
	public List<OrderSummaryResponse> getCustomerOrderSummary(@PathVariable(value = "custId") String custId)
			throws ClientProtocolException, IOException {

		return orderService.getOrderSummary(custId);

	}

	@GetMapping("/order/{orderId}/detail")
	public OrderDetailResponsne getOrderDetail(@PathVariable(value = "orderId") Long orderId)
			throws ClientProtocolException, IOException {

		return orderService.getOrderDetails(orderId);

	}

}
