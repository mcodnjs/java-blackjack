package blackjack.domain;

import blackjack.domain.card.Card;
import blackjack.domain.card.ShufflingMachine;
import blackjack.domain.card.Deck;
import blackjack.domain.participant.Dealer;
import blackjack.domain.participant.Participant;
import blackjack.domain.participant.Player;
import blackjack.domain.participant.Players;
import blackjack.domain.participant.Result;

public class BlackJackGame {

    private static final int NUMBER_OF_INITIAL_CARD = 2;

    private final Dealer dealer;
    private final Players players;

    public BlackJackGame(final String inputNames) {
        this.dealer = new Dealer();
        this.players = new Players(inputNames);
    }

    public void handOutCardTo(final ShufflingMachine shufflingMachine, final Participant participant) {
        final Card card = Deck.from(shufflingMachine.draw());
        participant.receiveCard(card);
    }

    public void handOutInitCards(final ShufflingMachine shufflingMachine) {
        handOutInitCardsTo(shufflingMachine, dealer);
        players.getPlayers()
                .forEach(player -> handOutInitCardsTo(shufflingMachine, player));
    }

    private void handOutInitCardsTo(final ShufflingMachine shufflingMachine, final Participant participant) {
        for (int i = 0; i < NUMBER_OF_INITIAL_CARD; i++) {
            final Card card = Deck.from(shufflingMachine.draw());
            participant.receiveCard(card);
        }
    }

    public void findWinner() {
        final int sumOfDealer = dealer.calculateSumOfRank();

        for (final Player player : players.getPlayers()) {
            final int sumOfPlayer = player.calculateSumOfRank();
            judgeResult(player, sumOfDealer, sumOfPlayer);
        }
    }

    private void judgeResult(final Player player, final int sumOfDealer, final int sumOfPlayer) {
        if (judgeResultWhenPlayerIsBlackJack(player)) {
            return;
        }
        if (judgeResultWhenPlayerIsBust(player)) {
            return;
        }
        judgeResultWhenPlayerIsNotBust(sumOfDealer, player, sumOfPlayer);
    }

    private boolean judgeResultWhenPlayerIsBlackJack(final Player player) {
        if (player.isBlackJack() && dealer.isBlackJack()) {
            setUpResultWhenPush(player);
            return true;
        }
        if (player.isBlackJack() && !dealer.isBlackJack()) {
            setUpResultWhenPlayerWin(player);
            return true;
        }
        return false;
    }

    private boolean judgeResultWhenPlayerIsBust(final Player player) {
        if (player.isBust()) {
            setUpResultWhenDealerWin(player);
            return true;
        }
        return false;
    }

    private void judgeResultWhenPlayerIsNotBust(final int sumOfDealer, final Player player, final int sumOfPlayer) {
        if (dealer.isBust() || sumOfPlayer > sumOfDealer) {
            setUpResultWhenPlayerWin(player);
            return;
        }
        if (dealer.isBlackJack() || sumOfPlayer < sumOfDealer) {
            setUpResultWhenDealerWin(player);
            return;
        }
        setUpResultWhenPush(player);
    }

    private void setUpResultWhenPush(final Player player) {
        dealer.setResults(Result.PUSH);
        player.setResult(Result.PUSH);
    }

    private void setUpResultWhenDealerWin(final Player player) {
        dealer.setResults(Result.WIN);
        player.setResult(Result.LOSE);
    }

    private void setUpResultWhenPlayerWin(final Player player) {
        dealer.setResults(Result.LOSE);
        player.setResult(Result.WIN);
    }

    public Dealer getDealer() {
        return this.dealer;
    }

    public Players getPlayers() {
        return this.players;
    }
}
