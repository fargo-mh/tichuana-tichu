package ch.tichuana.tichu.commons.test;

import ch.tichuana.tichu.commons.models.Card;
import ch.tichuana.tichu.commons.models.Combination;
import ch.tichuana.tichu.commons.models.Rank;
import ch.tichuana.tichu.commons.models.Suit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class IsValidMoveTest {

    static Rank[] rank = Rank.values();
    static ArrayList<Card> cards = new ArrayList<>();
    static ArrayList<Card> oldMove = new ArrayList<>();
    static ArrayList<Card> newMove = new ArrayList<>();

    @BeforeAll
    public static void makeCards(){
        // create all cards
        cards.add(new Card(Rank.dog));
        cards.add(new Card(Rank.mahjong));
        for (int i = 2; i<15; i++){
            cards.add(new Card(Suit.Pagodas, rank[i]));
        }
        cards.add(new Card(Rank.phoenix));
        cards.add(new Card(Rank.dragon));
    }

    @Test
    public void testHighCard(){
        // Test if two beats mahjong
        oldMove.add(cards.get(1));
        newMove.add(cards.get(2));
        assertTrue(Combination.isValidMove(oldMove,newMove));

        oldMove.clear(); newMove.clear();

        // Check if ten beats four
        oldMove.add(cards.get(4));
        newMove.add(cards.get(10));
        assertTrue(Combination.isValidMove(oldMove,newMove));

        oldMove.clear(); newMove.clear();

        // Check if Dragon beats Ace
        oldMove.add(cards.get(14));
        newMove.add(cards.get(16));
        assertTrue(Combination.isValidMove(oldMove,newMove));

        oldMove.clear(); newMove.clear();

        // Check if Dragon beats Phoenix
        Combination.beforePhoenix = cards.get(10);
        oldMove.add(cards.get(15));
        newMove.add(cards.get(16));
        assertTrue(Combination.isValidMove(oldMove,newMove));

        oldMove.clear(); newMove.clear();

        // Ensure that lower Ranks don't beat highers
        oldMove.add(cards.get(13));
        newMove.add(cards.get(12));
        assertFalse(Combination.isValidMove(oldMove,newMove));

        oldMove.clear(); newMove.clear();

        // Ensure that dog can't be played on Mahjong
        oldMove.add(cards.get(1));
        newMove.add(cards.get(0));
        assertFalse(Combination.isValidMove(oldMove,newMove));

        oldMove.clear(); newMove.clear();

        // Ensure that dog can't be played on Mahjong
        oldMove.add(cards.get(0));
        newMove.add(cards.get(1));
        assertFalse(Combination.isValidMove(oldMove,newMove));

        oldMove.clear(); newMove.clear();

        // test phoenix beats ace
        Combination.beforePhoenix = cards.get(10);
        newMove.add(cards.get(15));
        oldMove.add(cards.get(14));
        assertTrue(Combination.isValidMove(oldMove,newMove));

        oldMove.clear(); newMove.clear();

        // test phoenix can still be beaten
        Combination.beforePhoenix = cards.get(10);
        oldMove.add(cards.get(15));
        newMove.add(cards.get(14));
        assertTrue(Combination.isValidMove(oldMove,newMove));

    }

    @Test
    public void testOnePair(){
        oldMove.clear(); newMove.clear();

        // test regular pairs
        oldMove.add(new Card(Suit.Stars,Rank.two));
        oldMove.add(new Card(Suit.Pagodas,Rank.two));
        newMove.add(new Card(Suit.Stars,Rank.three));
        newMove.add(new Card(Suit.Pagodas,Rank.three));

        assertTrue(Combination.isValidMove(oldMove, newMove));
        assertFalse(Combination.isValidMove(newMove, oldMove));

        // test 1 card too many
        newMove.add(new Card(Suit.Jade,Rank.three));
        assertFalse(Combination.isValidMove(newMove, oldMove));

        // test phoenix in new Move
        newMove.clear();
        newMove.add(new Card(Suit.Stars,Rank.three));
        newMove.add(new Card(Rank.phoenix));

        assertTrue(Combination.isValidMove(oldMove, newMove));
        assertFalse(Combination.isValidMove(newMove, oldMove));

        // test phoenix in oldmove
        newMove.remove(1);
        newMove.add(new Card(Suit.Pagodas,Rank.three));
        oldMove.remove(1);
        oldMove.add(new Card(Rank.phoenix));

        assertTrue(Combination.isValidMove(oldMove, newMove));
        assertFalse(Combination.isValidMove(newMove, oldMove));
    }

    @Test
    public void testSteps(){
        ArrayList<Card> stepFourLower = new ArrayList<>();
        ArrayList<Card> stepFourHigher = new ArrayList<>();

        stepFourHigher.add(new Card(Suit.Stars,Rank.six));
        stepFourHigher.add(new Card(Suit.Pagodas,Rank.six));
        stepFourHigher.add(new Card(Suit.Stars,Rank.five));
        stepFourHigher.add(new Card(Suit.Pagodas,Rank.five));

        stepFourLower.add(new Card(Suit.Stars,Rank.two));
        stepFourLower.add(new Card(Suit.Pagodas,Rank.two));
        stepFourLower.add(new Card(Suit.Stars,Rank.three));
        stepFourLower.add(new Card(Suit.Pagodas,Rank.three));

        // test same size steps
        assertTrue(Combination.isValidMove(stepFourLower,stepFourHigher));
        assertFalse(Combination.isValidMove(stepFourHigher,stepFourLower));

        ArrayList<Card> stepSixLower = new ArrayList<>(stepFourLower);
        stepSixLower.add(new Card(Suit.Stars,Rank.four));
        stepSixLower.add(new Card(Suit.Pagodas,Rank.four));

        // bigger step beats smaller even with higher pairs
        assertTrue(Combination.isValidMove(stepFourHigher,stepSixLower));
        assertFalse(Combination.isValidMove(stepSixLower,stepFourHigher));

        ArrayList<Card> stepSixHigher = new ArrayList<>(stepFourHigher);
        stepSixHigher.add(new Card(Suit.Stars,Rank.seven));
        stepSixHigher.add(new Card(Suit.Pagodas,Rank.seven));

        assertTrue(Combination.isValidMove(stepSixLower,stepSixHigher));
        assertFalse(Combination.isValidMove(stepSixHigher,stepSixLower));

        // test phoenix
        stepSixLower.remove(2);
        stepSixLower.add(new Card(Rank.phoenix));

        assertTrue(Combination.isValidMove(stepSixLower,stepSixHigher));
        assertFalse(Combination.isValidMove(stepSixHigher,stepSixLower));
    }
}