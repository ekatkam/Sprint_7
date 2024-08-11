public class OrderGenerator {



    public static Order getOrderData() {
        Order order = new Order();
        order.setFirstName("FirstName");
        order.setAddress("Address");
        order.setComment("Comment");
        order.setPhone("123456");
        order.setLastName("LastName");
        order.setDeliveryDate("09.08.2024");
        order.setMetroStation("metroStation");
        order.setRentTime(2);
        return order;
    }
}
