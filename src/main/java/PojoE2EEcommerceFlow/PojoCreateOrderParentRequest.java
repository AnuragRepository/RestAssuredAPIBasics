package PojoE2EEcommerceFlow;

import java.util.List;

public class PojoCreateOrderParentRequest {

    private List<PojoCreateOrderChildRequest> orders;

    public List<PojoCreateOrderChildRequest> getOrders() {
        return orders;
    }

    public void setOrders(List<PojoCreateOrderChildRequest> orders) {
        this.orders = orders;
    }



}
