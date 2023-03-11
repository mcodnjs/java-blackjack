package blackjack.domain;

import blackjack.domain.participant.Result;

public class Profit {

    private final int value;

    private Profit(final int value) {
        this.value = value;
    }

    public static Profit of(final int value) {
        validateProfit(value);
        return new Profit(value);
    }

    private static void validateProfit(final int value) {
        if (value <= 0) {
            throw new IllegalArgumentException("베팅 금액은 양수여야 합니다.");
        }
    }

    public Profit calculateProfit(final Result result) {
        return new Profit((int) (this.value * result.getRate()));
    }

    public static Profit dealerProfit(final int dealerProfit) {
        return new Profit(dealerProfit);
    }

    public int getProfit() {
        return this.value;
    }
}
