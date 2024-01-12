package DTO;

import lombok.Data;

@Data
public class CourierLoginRequest {
    String login;
    String password;

    public CourierLoginRequest(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public CourierLoginRequest() {
    }

}
