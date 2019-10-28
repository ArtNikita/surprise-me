package ru.nikky.surpriseme;

import com.google.gson.JsonObject;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;

public interface UserClient {

    @POST("/api/profile/name/")
    Call<JsonObject> changeName(@HeaderMap Map<String, String> headers,
                                @Body FirstName firstName);

}