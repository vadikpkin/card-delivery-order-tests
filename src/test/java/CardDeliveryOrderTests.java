import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import java.time.LocalDate;

import static com.codeborne.selenide.Selenide.*;

class CardDeliveryOrderTests {
    private static final String url = "http://localhost:9999/";
    private static final LocalDate dayOfMeeting = LocalDate.now().plusDays(4);
    private static final String dayOfMeetingString = dayOfMeeting.getDayOfMonth()
            + Integer.toString(dayOfMeeting.getMonthValue()) + dayOfMeeting.getYear();

    @CsvFileSource(resources = "/cardDeliveryOrderTestSubmitSources", numLinesToSkip = 1)
    @ParameterizedTest
    void shouldSubmitRequest(String town, String name, String tel){
        open(url);
        $("[placeholder = 'Город']").setValue(town);
        SelenideElement dateWebElement = $("[placeholder = 'Дата встречи']");
        dateWebElement.setValue("\b\b\b\b\b\b\b\b\b\b");
        dateWebElement.setValue(dayOfMeetingString);
        $("[name='name']").setValue(name);
        $("[name='phone']").setValue(tel);
        $(".checkbox__box").click();
        $(".button").click();
        $(new Selectors.ByText("Забронировать")).click();
        $("[data-test-id='notification']").waitUntil(Condition.visible, 15000);
    }
}
