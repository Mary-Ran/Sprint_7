package DTO;

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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }
}
