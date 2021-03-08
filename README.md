#For Build**
  
mvn clean install

#For running

java -jar target/app-service-1.0


#Service Port

8000

#API-1 - Order Summary By customer Id

http://localhost:9000/customer/{customer_id}/order/summary


#API-2.  Order Details based on order Id

http://localhost:9000/order/{order_id}/detail
