import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;

class Connector
{
    private CloseableHttpClient httpclient = HttpClients.createDefault();
    private String token = this.retrieveToken();
    private String path = System.getenv("PATH");

    public Connector()
    {

    }

    public String retrieveToken()
    {
        String configFilePath = System.getProperty("user.home") + "/.ffl.conf";
        String token = "";
        try {
            FileReader fr = new FileReader(configFilePath);
            token = "";
            BufferedReader br = new BufferedReader(fr);
            token = br.readLine();
        } catch (Exception e) {
            token = "";
        }
        return token;
    }
    public String getTodaysDate()
    {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(new Date());
        
    }

    public void addQuickCalories(double calories)
    {
        HttpPost httpPost = new HttpPost("https://api.fitbit.com/1/user/-/foods/log.json");

    }


    public String getCaloriesOut()
    {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String todaysDate = simpleDateFormat.format(new Date());
        //System.out.println(todaysDate);
        HttpGet request = new HttpGet("https://api.fitbit.com/1/user/-/activities/date/" + todaysDate + ".json");

        request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        try {
            CloseableHttpResponse response = httpclient.execute(request);
            //String responseBody = response.getStatusLine();
            //System.out.println(response.getStatusLine());
            HttpEntity entity = response.getEntity();
            //System.out.println(entity);
            Header headers = entity.getContentType();
            //System.out.println(headers);

            if (entity != null) {
                // return it as a String
                String result = EntityUtils.toString(entity);

                JSONObject obj = new JSONObject(result);

                JSONObject summary = (JSONObject) obj.get("summary");
                return summary.get("caloriesOut").toString();
            }
        } catch (Exception e) {
            System.out.println("Error " + e);
            return null;
        }
        return null;
    }

}