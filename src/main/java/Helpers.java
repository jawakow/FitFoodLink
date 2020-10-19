import java.text.SimpleDateFormat;
import java.util.Date;

public class Helpers {
    public static String getTodaysDate()
    {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(new Date());
    }
}

