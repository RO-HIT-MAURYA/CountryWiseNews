package rohit.maurya.countrywisenews;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface
{
    @GET("top-headlines?country=in&apiKey=acdd5af6be1a4ed281a6fd1df7424cb8")
    Call<ResponseFormat> getTopNews();

    @GET("top-headlines?country=in&category=business&apiKey=acdd5af6be1a4ed281a6fd1df7424cb8")
    Call<ResponseFormat> getBusinessNews();

    @GET("top-headlines?country=in&category=entertainment&apiKey=acdd5af6be1a4ed281a6fd1df7424cb8")
    Call<ResponseFormat> getEntertainmentNews();

    @GET("top-headlines?country=in&category=health&apiKey=acdd5af6be1a4ed281a6fd1df7424cb8")
    Call<ResponseFormat> getHealthNews();

    @GET("top-headlines?country=in&category=science&apiKey=acdd5af6be1a4ed281a6fd1df7424cb8")
    Call<ResponseFormat> getScienceNews();

    @GET("top-headlines?country=in&category=sports&apiKey=acdd5af6be1a4ed281a6fd1df7424cb8")
    Call<ResponseFormat> getSportsNews();

    @GET("top-headlines?country=in&category=technology&apiKey=acdd5af6be1a4ed281a6fd1df7424cb8")
    Call<ResponseFormat> getTechnologyNews();
}
