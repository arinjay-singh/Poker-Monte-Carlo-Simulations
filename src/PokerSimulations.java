import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

/*
SUITS:
Spades - 0
Clubs - 1
Hearts - 2
Diamonds - 3

DENOMINATIONS:
J - 11
Q - 12
K - 13
A - 14
 */
public class PokerSimulations {
    //Card Class
    public static class Card {
        String suit;
        int denomination;

        public Card(String s, int d) {
            this.suit = s;
            this.denomination = d;
        }

        public String toString() {
            return denomination + " of " + suit;
        }
    }

    public static class Hand {
        Card[] hand = new Card[2];

        public Hand(Card c1, Card c2) {
            this.hand[0] = c1;
            this.hand[1] = c2;
        }
    }

    //one trial
    public static boolean pokerTrial(ArrayList<Card> originalDeck, int numPlayers) {
        ArrayList<Card> deck = new ArrayList<>(originalDeck);
        //table of 5 cards
        Card[] table = new Card[5];
        for (int i = 0; i < 5; i++) {
            table[i] = deck.remove(new Random().nextInt(52 - i));
        }
        //distribute hands
        Hand[] hands = new Hand[numPlayers];
        for (int i = 0; i < numPlayers; i++) {
            hands[i] = new Hand(deck.remove(new Random().nextInt(47 - (2 * i))), deck.remove(new Random().nextInt(46 - (2 * i))));
        }
        //check for royal flush
        for (int i = 0; i < numPlayers; i++) {
            Card[] totalHand = new Card[7];  //result array of size first array and second array
            System.arraycopy(table, 0, totalHand, 0, 5);
            System.arraycopy(hands[i].hand, 0, totalHand, 5, 2);

            HashMap<String, Integer> suitFreq = new HashMap<String, Integer>();
            suitFreq.put("Hearts", 0);
            suitFreq.put("Spades", 0);
            suitFreq.put("Diamonds", 0);
            suitFreq.put("Clubs", 0);
            for (int x = 0; x < 7; x++) {
                if (totalHand[x].suit.equals("Hearts")) suitFreq.put("Hearts", suitFreq.get("Hearts") + 1);
                else if (totalHand[x].suit.equals("Spades")) suitFreq.put("Spades", suitFreq.get("Spades") + 1);
                else if (totalHand[x].suit.equals("Diamonds")) suitFreq.put("Diamonds", suitFreq.get("Diamonds") + 1);
                else if (totalHand[x].suit.equals("Clubs")) suitFreq.put("Clubs", suitFreq.get("Clubs") + 1);
            }
            String max = "Hearts";
            for (String suit : suitFreq.keySet()) {
                if (suitFreq.get(suit) > suitFreq.get(max)) max = suit;
            }
            int count = 0;
            for (int x = 0; x < 7; x++) {
                if (totalHand[x].suit.equals(max) && totalHand[x].denomination >= 10) {
                    count++;
                }
            }
            if (count == 5) {
                for (int x = 0; x < 7; x++) {
                    if (totalHand[x].suit.equals(max) && totalHand[x].denomination >= 10) {
                        System.out.println(totalHand[x].toString());
                    }
                }
                return true;
            }
        }
        return false;
    }

    public static double pokerSimulation(ArrayList<Card> deck, int numPlayers, int numTrials) {
        int successes = 0;
        for (int i = 0; i < numTrials; i++) {
            if (pokerTrial(deck, numPlayers)) successes++;
            if ((i % 100000) == 0) System.out.println("Trial #" + i);
        }
        System.out.println("Successes: " + successes);
        System.out.println("Trials: " + numTrials);
        return (((double) successes) / ((double) numTrials)) * 100.0;
    }

    public static void main(String[] args) {
        //create deck of cards
        ArrayList<Card> deck = new ArrayList<>();
        //hearts
        for (int i = 2; i < 15; i++) {
            deck.add(new Card("Hearts", i));
        }
        //spades
        for (int i = 2; i < 15; i++) {
            deck.add(new Card("Spades", i));
        }
        //Diamonds
        for (int i = 2; i < 15; i++) {
            deck.add(new Card("Diamonds", i));
        }
        //clubs
        for (int i = 2; i < 15; i++) {
            deck.add(new Card("Clubs", i));
        }

        Collections.shuffle(deck);

        //check for royal flush
        int players = 1;
        int trials = 100000000;
        double probability = pokerSimulation(deck, players, trials);
        System.out.println("Expected Successes: " + ((0.0000015 * (double) players) * (double) trials));
        System.out.println("Got: " + probability + "%");
    }
}
