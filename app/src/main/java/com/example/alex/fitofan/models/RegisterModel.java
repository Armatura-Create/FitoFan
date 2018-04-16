package com.example.alex.fitofan.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * //TODO Создание модели
 * Модель ожидаемого ответа с сервера
 * Передавать в параметрах как Call<RegisterModel> (см. KultureAPI)
 *
 * SerializedName соответвует имени json поля
 * Данная модель распарсила бы объект типа:
 * {
 *     "example_name":"User",
 *     "example_surname":"Junior",
 *     "age":20
 * }
 */
public class RegisterModel {

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("surname")
    @Expose
    private String surname;

    @SerializedName("password")
    @Expose
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String phone) {
        this.surname = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toJSONString() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("email", email);
        json.put("password", password);
        json.put("phone_number", surname);
        return json.toString().replaceAll("\\\\", "");
    }
}
