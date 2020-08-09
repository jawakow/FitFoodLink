import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Timer;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;

public class Main extends Application
{
    public Label caloriesField;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        primaryStage.setTitle("FitFoodLink");
        caloriesField = new Label();

        Button refreshBtn = new Button("Refresh");
        Button addKcalBtn = new Button("Add calories");
        TilePane r = new TilePane();
        r.getChildren().add(refreshBtn);
        r.getChildren().add(addKcalBtn);
        r.getChildren().add(caloriesField);
        Scene scene = new Scene(r, 200, 100);
        primaryStage.setScene(scene);
        primaryStage.show();

        // set timer to update data every 1 minute
        Timer forceUpdateTimer = new Timer();
        forceUpdateTimer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                Connector connector = new Connector();
                Platform.runLater(() -> caloriesField.setText(connector.getCaloriesOut() + " kcal"));
            }
        }, 60 * 1000);

        refreshBtn.setOnAction(new EventHandler()
        {
            public void handle(Event event)
            {
                Connector connector = new Connector();
                caloriesField.setText(connector.getCaloriesOut() + " kcal");
            }

        });

        addKcalBtn.setOnAction(new EventHandler()
        {
            public void handle(Event event)
            {

            }
        });
    }

    public static void main(String[] args)
    {
        // new GUI();
        Application.launch(args);
        Connector connector = new Connector();
        System.out.println(connector.getCaloriesOut());
    }
}

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