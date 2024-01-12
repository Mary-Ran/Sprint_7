package DTO;

import lombok.Data;

@Data
public class CourierCreateRequest {
    String login;
    String password;
    String first_name;

    public CourierCreateRequest(String login, String password, String first_name) {
        this.login = login;
        this.password = password;
        this.first_name = first_name;
    }

    public CourierCreateRequest() {

    }

}
