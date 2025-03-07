package id.ac.ui.cs.advprog.eshop.enums;

public enum PaymentMethod {
    VOUCHER(),
    COD();

    public static boolean contains(String param) {
        for (OrderStatus orderStatus : OrderStatus.values()) {
            if (orderStatus.name().equals(param)) {
                return true;
            }
        }
        return false;
    }
}
