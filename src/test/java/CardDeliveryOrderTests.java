import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import ulit.DateCorrector;

import java.time.LocalDate;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.*;

class CardDeliveryOrderTests {
    private static final String url = "http://localhost:9999/";
    private static final String validDayOfMeeting = DateCorrector.correctDate(LocalDate.now().plusDays(3));
    private static final String invalidDayOfMeeting = DateCorrector.correctDate(LocalDate.now());

    @CsvFileSource(resources = "/cardDeliveryOrderTestSubmitSources", numLinesToSkip = 1)
    @ParameterizedTest
    void shouldSubmitRequest(String town, String name, String tel) {
        open(url);
        $("[placeholder = 'Город']").setValue(town);
        SelenideElement dateWebElement = $("[placeholder = 'Дата встречи']");
        dateWebElement.setValue("\b\b\b\b\b\b\b\b\b\b");
        dateWebElement.setValue(validDayOfMeeting);
        $("[name='name']").setValue(name);
        $("[name='phone']").setValue(tel);
        $(".checkbox__box").click();
        $(new Selectors.ByText("Забронировать")).click();
        $("[data-test-id='notification']").waitUntil(Condition.visible, 15000);
    }

    @CsvFileSource(resources = "/cardDeliveryOrderTestInvalidName", numLinesToSkip = 1)
    @ParameterizedTest
    void shouldDeclineRequestForInvalidName(String town, String name, String tel) {
        open(url);
        $("[placeholder = 'Город']").setValue(town);
        SelenideElement dateWebElement = $("[placeholder = 'Дата встречи']");
        dateWebElement.setValue("\b\b\b\b\b\b\b\b\b\b");
        dateWebElement.setValue(validDayOfMeeting);
        $("[name='name']").setValue(name);
        $("[name='phone']").setValue(tel);
        $(".checkbox__box").click();
        $(new Selectors.ByText("Забронировать")).click();
        $("[data-test-id='name'] .input__sub").shouldHave(Condition
                .exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @CsvFileSource(resources = "/cardDeliveryOrderTestInvalidTel", numLinesToSkip = 1)
    @ParameterizedTest
    void shouldDeclineRequestForInvalidTel(String town, String name, String tel) {
        open(url);
        $("[placeholder = 'Город']").setValue(town);
        SelenideElement dateWebElement = $("[placeholder = 'Дата встречи']");
        dateWebElement.setValue("\b\b\b\b\b\b\b\b\b\b");
        dateWebElement.setValue(validDayOfMeeting);
        $("[name='name']").setValue(name);
        $("[name='phone']").setValue(tel);
        $(".checkbox__box").click();
        $(new Selectors.ByText("Забронировать")).click();
        $("[data-test-id='phone'] .input__sub").shouldHave(Condition
                .exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldDeclineRequestForInvalidCity() {
        open(url);
        $("[placeholder = 'Город']").setValue("Сосновый Бор");
        SelenideElement dateWebElement = $("[placeholder = 'Дата встречи']");
        dateWebElement.setValue("\b\b\b\b\b\b\b\b\b\b");
        dateWebElement.setValue(validDayOfMeeting);
        $("[name='name']").setValue("Костя Воронин");
        $("[name='phone']").setValue("+79215683733");
        $(".checkbox__box").click();
        $(new Selectors.ByText("Забронировать")).click();
        $("[data-test-id='city'] .input__sub").shouldHave(Condition
                .exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldDeclineRequestForInvalidDate() {
        open(url);
        $("[placeholder = 'Город']").setValue("Москва");
        SelenideElement dateWebElement = $("[placeholder = 'Дата встречи']");
        dateWebElement.setValue("\b\b\b\b\b\b\b\b\b\b");
        dateWebElement.setValue(invalidDayOfMeeting);
        $("[name='name']").setValue("Костя Воронин");
        $("[name='phone']").setValue("+79215683733");
        $(".checkbox__box").click();
        $(new Selectors.ByText("Забронировать")).click();
        $(new Selectors.ByText("Заказ на выбранную дату невозможен")).shouldHave(Condition
                .exactText("Заказ на выбранную дату невозможен"));
    }

    @Test
    void shouldDeclineRequestForInvalidTypeOfDate() {
        open(url);
        $("[placeholder = 'Город']").setValue("Москва");
        SelenideElement dateWebElement = $("[placeholder = 'Дата встречи']");
        dateWebElement.setValue("\b\b\b\b\b\b\b\b\b\b");
        dateWebElement.setValue(LocalDate.now().toString().replaceAll("-", ""));
        $("[name='name']").setValue("Костя Воронин");
        $("[name='phone']").setValue("+79215683733");
        $(".checkbox__box").click();
        $(new Selectors.ByText("Забронировать")).click();
        $(new Selectors.ByText("Неверно введена дата"))
                .shouldHave(Condition.exactText("Неверно введена дата"));
    }

    @Test
    void shouldDeclineRequestForNotCheckedCheckbox() {
        open(url);
        $("[placeholder = 'Город']").setValue("Москва");
        SelenideElement dateWebElement = $("[placeholder = 'Дата встречи']");
        dateWebElement.setValue("\b\b\b\b\b\b\b\b\b\b");
        dateWebElement.setValue(validDayOfMeeting);
        $("[name='name']").setValue("Костя Воронин");
        $("[name='phone']").setValue("+79215683733");
        $(new Selectors.ByText("Забронировать")).click();
        final String colorRedRGB = "rgba(255, 92, 92, 1)";
        String actualColor = $("[data-test-id='agreement'] .checkbox__text")
                .getCssValue("color");
        assertEquals(colorRedRGB, actualColor);
    }

    @Test
    void shouldDeclineRequestForNullCity() {
        open(url);
        $("[placeholder = 'Город']").setValue(null);
        SelenideElement dateWebElement = $("[placeholder = 'Дата встречи']");
        dateWebElement.setValue("\b\b\b\b\b\b\b\b\b\b");
        dateWebElement.setValue(validDayOfMeeting);
        $("[name='name']").setValue("Костя Воронин");
        $("[name='phone']").setValue("+79215683733");
        $(".checkbox__box").click();
        $(new Selectors.ByText("Забронировать")).click();
        $("[data-test-id='city'] .input__sub").shouldHave(Condition
                .exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldDeclineRequestForNullName() {
        open(url);
        $("[placeholder = 'Город']").setValue("Москва");
        SelenideElement dateWebElement = $("[placeholder = 'Дата встречи']");
        dateWebElement.setValue("\b\b\b\b\b\b\b\b\b\b");
        dateWebElement.setValue(validDayOfMeeting);
        $("[name='name']").setValue(null);
        $("[name='phone']").setValue("+79215683733");
        $(".checkbox__box").click();
        $(new Selectors.ByText("Забронировать")).click();
        $("[data-test-id='name'] .input__sub").shouldHave(Condition
                .exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldDeclineRequestForNullDate() {
        open(url);
        $("[placeholder = 'Город']").setValue("Москва");
        SelenideElement dateWebElement = $("[placeholder = 'Дата встречи']");
        dateWebElement.setValue("\b\b\b\b\b\b\b\b\b\b");
        dateWebElement.setValue(null);
        $("[name='name']").setValue("Костя Воронин");
        $("[name='phone']").setValue("+79215683733");
        $(".checkbox__box").click();
        $(new Selectors.ByText("Забронировать")).click();
        $(new Selectors.ByText("Неверно введена дата"))
                .shouldHave(Condition.exactText("Неверно введена дата"));
    }

    @Test
    void shouldDeclineRequestForNullTel() {
        open(url);
        $("[placeholder = 'Город']").setValue("Москва");
        SelenideElement dateWebElement = $("[placeholder = 'Дата встречи']");
        dateWebElement.setValue("\b\b\b\b\b\b\b\b\b\b");
        dateWebElement.setValue(validDayOfMeeting);
        $("[name='name']").setValue("Костя Воронин");
        $("[name='phone']").setValue(null);
        $(".checkbox__box").click();
        $(new Selectors.ByText("Забронировать")).click();
        $("[data-test-id='phone'] .input__sub").shouldHave(Condition
                .exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldSubmitRequestWhenCityChosenFromAppearedList(){
        open(url);
        $("[placeholder = 'Город']").setValue("Аб");
        $(".menu-item__control").click();
        SelenideElement dateWebElement = $("[placeholder = 'Дата встречи']");
        dateWebElement.setValue("\b\b\b\b\b\b\b\b\b\b");
        dateWebElement.setValue(validDayOfMeeting);
        $("[name='name']").setValue("Костя Воронни");
        $("[name='phone']").setValue("+79215683733");
        $(".checkbox__box").click();
        $(new Selectors.ByText("Забронировать")).click();
        $("[data-test-id='notification']").waitUntil(Condition.visible, 15000);
    }
}
