package id.ac.ui.cs.advprog.eshop.enums;

public enum PaymentStatus {
    SUCCESS(),
    REJECTED(),
    PENDING();

    public static boolean contains(String param) {
        for (OrderStatus orderStatus : OrderStatus.values()) {
            if (orderStatus.name().equals(param)) {
                return true;
            }
        }
        return false;
    }
}
