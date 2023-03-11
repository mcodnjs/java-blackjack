package blackjack.domain;

import blackjack.domain.participant.Result;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ProfitTest {

    @DisplayName("Profit 정상적으로 생성된다.")
    @Test
    void createBet() {
        // given
        int input = 1000;

        // when & then
        assertDoesNotThrow(() -> Profit.of(input));
    }

    @ParameterizedTest(name = "양수가 아닐 경우 예외가 발생한다.")
    @ValueSource(ints = {-1000, 0})
    void validatePositiveNumber(int input) {
        // when & then
        assertThatThrownBy(() -> Profit.of(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("베팅 금액은 양수여야 합니다.");
    }

    @DisplayName("결과에 따라 수익이 정상적으로 생성된다.")
    @Test
    void calculateProfit() {
        // given
        int money = 1000;
        Profit profit = Profit.of(money);

        // when
        int finalProfit1 = profit.calculateProfit(Result.WIN).getProfit();
        int finalProfit2 = profit.calculateProfit(Result.BLACKJACK).getProfit();
        int finalProfit3 = profit.calculateProfit(Result.LOSE).getProfit();
        int finalProfit4 = profit.calculateProfit(Result.PUSH).getProfit();

        // then
        assertThat(finalProfit1).isEqualTo(1000);
        assertThat(finalProfit2).isEqualTo(1500);
        assertThat(finalProfit3).isEqualTo(-1000);
        assertThat(finalProfit4).isEqualTo(1000);
    }
}
