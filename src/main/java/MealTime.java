import java.util.HashMap;
import java.util.Map;
import java.util.*;

public class MealTime {
//    public String[] MealTime = {"Anytime", "Breakfast", "Lunch", "Dinner", "Supper", "Morning Snack",
//            "Afternoon snack", "Evening Snack"};
    //HashMap<> mealTimeMap = new HashMap<String,Integer> ();  // will to that later

    public int getMealTimeId(String mealTimeName) {
        switch (mealTimeName) {
            case "Breakfast":
                return 1;
            case "Morning Snack":
                return 2;
            case "Lunch":
                return 3;
            case "Afternoon snack":
                return 4;
            case "Dinner":
                return 5;
            case "Anytime":
                return 7;
        }
        return 0;
    }


}
