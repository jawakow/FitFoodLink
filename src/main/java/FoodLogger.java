import jdk.nashorn.api.scripting.AbstractJSObject;
import jdk.nashorn.api.scripting.JSObject;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;

public class FoodLogger extends Connector{
    public FoodLogger() {
        super();

    }

    public void AddQuickCalories(String mealTimeName, int kcal, String date) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {

            HttpPost httpPost = new HttpPost("https://api.fitbit.com/1/user/-/foods/log.json");
            MealTime mealTime = new MealTime();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("foodName", "Quick Add");
            jsonObject.put("mealTypeId", mealTime.getMealTimeId(mealTimeName));
            jsonObject.put("unitId", 1); // to be checked - better to use Get Food Units API
            jsonObject.put("amount", 1);
            jsonObject.put("date", date);
            jsonObject.put("calories", kcal);

            HttpPost request = new HttpPost("https://api.fitbit.com/1/user/-/foods/log.json");
            String jsonObjectAsString = jsonObject.toString();
            StringEntity params = new StringEntity(jsonObjectAsString);
            request.setEntity(params);
            request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + retrieveToken());
            HttpResponse response = httpclient.execute(request);
            System.out.println(response);

        }

        catch (Exception e) {}
        //        StringEntity params = new StringEntity(
//
//        );
    }

}
