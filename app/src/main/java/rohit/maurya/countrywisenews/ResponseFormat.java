package rohit.maurya.countrywisenews;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ResponseFormat
{
    @SerializedName("articles")
    private JsonArray jsonArray;

    public JsonArray getJsonArray()
    {
        return jsonArray;
    }
}
