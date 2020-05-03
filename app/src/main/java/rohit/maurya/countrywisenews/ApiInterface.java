package rohit.maurya.countrywisenews;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface
{
    @GET("top-headlines?country=in&apiKey=acdd5af6be1a4ed281a6fd1df7424cb8")
    Call<News> getTopNews();

    @GET("top-headlines?country=in&category=business&apiKey=acdd5af6be1a4ed281a6fd1df7424cb8")
    Call<News> getBusinessNews();

    @GET("top-headlines?country=in&category=entertainment&apiKey=acdd5af6be1a4ed281a6fd1df7424cb8")
    Call<News> getEntertainmentNews();

    @GET("top-headlines?country=in&category=health&apiKey=acdd5af6be1a4ed281a6fd1df7424cb8")
    Call<News> getHealthNews();

    @GET("top-headlines?country=in&category=science&apiKey=acdd5af6be1a4ed281a6fd1df7424cb8")
    Call<News> getScienceNews();

    @GET("top-headlines?country=in&category=sports&apiKey=acdd5af6be1a4ed281a6fd1df7424cb8")
    Call<News> getSportsNews();

    @GET("top-headlines?country=in&category=technology&apiKey=acdd5af6be1a4ed281a6fd1df7424cb8")
    Call<News> getTechnologyNews();
}
