import org.junit.jupiter.api.Test;
import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryOrderTests {
    static final String url = "http://localhost:9999/";

    @Test
    void shouldOpenURL(){
        open(url);
    }

}
