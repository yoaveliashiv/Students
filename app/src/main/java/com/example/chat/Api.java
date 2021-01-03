package com.example.chat;

import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface Api {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAA1VesbtQ:APA91bEy3VAgIM9QHNUlz6qv8z2ROgMzo9JDvzmjLsWsCXYC2GBjBq_ECFPpKXZNloEG0rm31JQW0ge9BAX4DBRbrz5NY9QYfEkc3oVh30kZhvgu7KEb8q8-tYHzs19l0cDlCUcisSKd"
    })
    @POST("fcm/send")
    Call<MyResponse> sendNotifcation(@Body NotificationSender body);

}
