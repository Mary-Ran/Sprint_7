package DTO;

import lombok.Data;

import java.util.List;
@Data
public class OrderCreateRequest {
    String firstName;
    String lastName;
    String address;
    String metroStation;
    String phone;
    Integer rentTime;
    String deliveryDate;
    String comment;
    List<String> color;

    public OrderCreateRequest(String firstName, String lastName, String address, String metroStation, String phone, Integer rentTime, String deliveryDate, String comment) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
    }

    public OrderCreateRequest() {

    }

}
