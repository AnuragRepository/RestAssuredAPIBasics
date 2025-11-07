package PojoE2EEcommerceFlow;

import java.util.List;

public class PojoCreateOrderResponse
{
    private List<String> orders;
    private List<String> productOrderId;
    private String message;

    public List<String> getProductOrderId() {
        return productOrderId;
    }

    public void setProductOrderId(List<String> productOrderId) {
        this.productOrderId = productOrderId;
    }

    public List<String> getOrders() {
        return orders;
    }

    public void setOrders(List<String> orders) {
        this.orders = orders;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}

