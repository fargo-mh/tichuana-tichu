package ch.tichuana.tichu.commons.models;

import java.util.ArrayList;
import java.util.Collections;

public enum Combination {
	HighCard, OnePair, ThreeOfAKind, Steps, FullHouse, Straight, FourOfAKindBomb, FourOfAKindPhoenix, StraightFlushBomb;

	Combination() {
	}


	public static ArrayList<Card> clonedCards;
	/**
	 * All Combinations are going to be checked and returned by currentEval.
	 * @autor
	 * @return
	 * @param cards
	 */
	public static Combination evaluateCombination(ArrayList<Card> cards) {
		Combination currentEval = HighCard;

		clonedCards = (ArrayList<Card>) cards.clone();
		Collections.sort(clonedCards);

		if(isOnePair(clonedCards)) currentEval = OnePair;
		if(isThreeOfAKind(clonedCards)) currentEval = ThreeOfAKind;
		if(isSteps(clonedCards)) currentEval = Steps;
		if(isFullHouse(clonedCards)) currentEval = FullHouse;
		if(isStraight(clonedCards)) currentEval = Straight;
		if(isFourOfAKindBomb(clonedCards)) currentEval = FourOfAKindBomb;
		if(isFourOfAKindPhoenix(clonedCards)) currentEval = FourOfAKindPhoenix;
		if(isStraightFlushBomb(clonedCards)) currentEval = StraightFlushBomb;


		return currentEval;
	}


	/**
	 * Before evaluating every possible combination, we check if the card phoenix is used.
	 * @author
	 * @param cards
	 * @return
	 */

	public static boolean containsPhoenix(ArrayList<Card> cards){
		boolean containsPhoenix = false;

		for (int i = 0; i < cards.size() - 1; i++){
			if(cards.get(i).getRank().ordinal() == 13){
				containsPhoenix = true;
			}
		}
		return containsPhoenix;
	}

	/**
	 * Check if two cards in the Array have the same rank.
	 * Phoenix control implemented.
	 * @autor
	 * @return
	 * @param cards
	 */
	public static boolean isOnePair(ArrayList<Card> cards) {
		boolean phoenix = Combination.containsPhoenix(cards);
		boolean found = false;

		// if phoenix is included we just need to know, how many cards there are for onePair. if there are just 2
		// cards, the pair is automatically given.
		if (phoenix == true){
			if (cards.size()== 2) found = true;

		}else{
			for (int i = 0; i < cards.size() - 1 && !found; i++) {
				for (int j = i + 1; j < cards.size() && !found; j++) {
					if (cards.get(i).getRank() == cards.get(j).getRank()) {
						found = true;
					}
				}
			}
		}
		return found;
	}

	/**
	 * Check if three cards in the Array have the same rank.
	 * Phoenix control implemented.
	 * @autor
	 * @return
	 * @param cards
	 */
	public static boolean isThreeOfAKind(ArrayList<Card> cards) {
		boolean phoenix = Combination.containsPhoenix(cards);
		boolean found = false;
		// if two cards have the same rank and the third card is ordinal 13 --> phoenix, the combination is true.
		if (phoenix == true) {
			for (int i = 0; i < cards.size() - 2 && !found; i++) {
				for (int j = i + 1; j < cards.size() - 1 && !found; j++) {
					for (int k = j + 1; k < cards.size() && !found; k++) {
						if (cards.get(i).getRank() == cards.get(j).getRank() && cards.get(k).getRank().ordinal() == 13
						|| cards.get(i).getRank() == cards.get(k).getRank() && cards.get(j).getRank().ordinal() == 13
						|| cards.get(k).getRank() == cards.get(j).getRank() && cards.get(i).getRank().ordinal() == 13)
							found = true;
					}
				}
			}
		}else {
					for (int i = 0; i < cards.size() - 2 && !found; i++) {
						for (int j = i + 1; j < cards.size() - 1 && !found; j++) {
							for (int k = j + 1; k < cards.size() && !found; k++) {
								if (cards.get(i).getRank() == cards.get(j).getRank() &&
										cards.get(j).getRank() == cards.get(k).getRank())
									found = true;
							}
						}
					}
				}
		return found;
	}

	/**
	 * Check if there are 4 or more cards in the array, if not return false. If there are, check if they are pairs and
	 * the rank is one higher then the first pair.
	 * @author
	 * @return
	 * @param cards
	 */
	public static boolean isSteps(ArrayList<Card> cards) {
		ArrayList<Card> clonedCards = (ArrayList<Card>) cards.clone();

		boolean phoenix = Combination.containsPhoenix(cards);
		boolean stepsFound = false;

		if (phoenix == true){



		}else {

			switch (clonedCards.size()) {
				case 1:
					stepsFound = false;
					break;
				case 3:
					if (clonedCards.get(1).getRank().ordinal() == clonedCards.get(2).getRank().ordinal() - 1
							&& clonedCards.get(2).getRank() == clonedCards.get(3).getRank()) {
						stepsFound = true;
					}
					break;
				case 5:
					if (clonedCards.get(1).getRank().ordinal() + 1 == clonedCards.get(2).getRank().ordinal()
							&& clonedCards.get(2).getRank() == clonedCards.get(3).getRank() &&
							clonedCards.get(3).getRank().ordinal() + 1 == clonedCards.get(4).getRank().ordinal()
							&& clonedCards.get(4).getRank() == clonedCards.get(5).getRank()) {
						stepsFound = true;
					}
					break;
				case 7:
					if (clonedCards.get(1).getRank().ordinal() + 1 == clonedCards.get(2).getRank().ordinal()
							&& clonedCards.get(2).getRank() == clonedCards.get(3).getRank() &&
							clonedCards.get(3).getRank().ordinal() + 1 == clonedCards.get(4).getRank().ordinal()
							&& clonedCards.get(4).getRank() == clonedCards.get(5).getRank()
							&& clonedCards.get(5).getRank().ordinal() + 1 == clonedCards.get(6).getRank().ordinal()
							&& clonedCards.get(6).getRank() == clonedCards.get(7).getRank()) {
						stepsFound = true;
					}
					break;
			}
		}
		return stepsFound;


	}

	/**
	 * The first 3 cards and the second 2, or the first 2 and the following 3 cards need to be the same.
	 * Phoenix control implemented.
	 * @author
	 * @return
	 * @param cards
	 */
	public static boolean isFullHouse(ArrayList<Card> cards) {
		boolean phoenix = Combination.containsPhoenix(cards);
		boolean fullHouseFound = false;

		if (phoenix == true){
			//if first two cards have the same rank and card 3 & four have the same rank, fullHouse is given with phoenix attached.
			if (cards.get(0).getRank().ordinal() == cards.get(1).getRank().ordinal() &&
			cards.get(2).getRank().ordinal() == cards.get(3).getRank().ordinal() && cards.get(4).getRank().ordinal() == 13
			//if card 2,3,4 have the same rank, fullhouse is automatically given.
			|| cards.get(1).getRank().ordinal() == cards.get(2).getRank().ordinal() &&
			cards.get(2).getRank().ordinal() == cards.get(3).getRank().ordinal()){
				fullHouseFound = true;
			}
		}else {
			//first three cards must be equal & last two cards must be equal
			if (cards.get(0).getRank().ordinal() == cards.get(1).getRank().ordinal() &&
					cards.get(2).getRank().ordinal() == cards.get(3).getRank().ordinal() &&
					cards.get(3).getRank().ordinal() == cards.get(4).getRank().ordinal()
					||
					//or first two cards must be equal & last three cards must be equal
					cards.get(0).getRank().ordinal() == cards.get(1).getRank().ordinal() &&
							cards.get(1).getRank().ordinal() == cards.get(2).getRank().ordinal() &&
							cards.get(3).getRank().ordinal() == cards.get(4).getRank().ordinal()) {
				fullHouseFound = true;
			}
		}
		return fullHouseFound;
	}

	/**
	 * Check if every following Card is one rank higher then the one before.
	 * @author
	 * @return
	 * @param cards
	 */
	public static boolean isStraight(ArrayList<Card> cards) {
		boolean straightFound = true;

		for(int i = 0; i < cards.size()-1 && straightFound; i++) {
			if(cards.get(i).compareTo(cards.get(i+1)) != -1)
				straightFound = false;
		}
		return straightFound;
	}

	/**
	 * Check if all 4 Cards have same Rank.
	 * @author
	 * @return
	 * @param cards
	 */
	public static boolean isFourOfAKindBomb(ArrayList<Card> cards) {
		boolean found = false;
		if(cards.get(0).getRank().ordinal() == cards.get(2).getRank().ordinal() ||
				cards.get(3).getRank().ordinal() == cards.get(1).getRank().ordinal()) {
			found = true;

		}
		return found;
	}

	/**
	 * Same as method isFourOfAKindBomb, but with the phoenix it's not a bomb.
	 * Phoenix control.
	 * @author
	 * @param cards
	 * @return
	 */
	public static boolean isFourOfAKindPhoenix(ArrayList<Card> cards){
		boolean found = false;
		if ((((cards.get(0).getRank().ordinal() == cards.get(3).getRank().ordinal()) &&
				(cards.get(0).getRank().ordinal() == 13)) || (cards.get(3).getRank().ordinal() == 13)) ||
				((cards.get(4).getRank().ordinal() == cards.get(1).getRank().ordinal()) &&
						(cards.get(4).getRank().ordinal() == 13) || (cards.get(1).getRank().ordinal() == 13))){
			found = true;
		}
			return found;


	}

	/**
	 * Check first if it is a Straight, if true, check Suits of card.
	 * @author
	 * @return
	 * @param cards
	 */
	public static boolean isStraightFlushBomb(ArrayList<Card> cards) {
		boolean straight = Combination.isStraight(cards);
		boolean flush = true;
		boolean straightFlushBomb = straight && flush;

		Suit suitOfCard = cards.get(0).getSuit();
		for(int i = 1; i < cards.size() && flush; i++) {
			if(cards.get(i).getSuit() != suitOfCard)
				flush = false;
		}
		return straightFlushBomb;
	}

}