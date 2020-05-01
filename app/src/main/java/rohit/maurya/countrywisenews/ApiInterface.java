package rohit.maurya.countrywisenews;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface
{
    @GET("top-headlines?country=in&apiKey=acdd5af6be1a4ed281a6fd1df7424cb8")
    Call<News> getNews();
}
