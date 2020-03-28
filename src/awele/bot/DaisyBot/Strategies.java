package awele.bot.DaisyBot;

import awele.core.Board;

public class Strategies {
	/** Seuil minimal de graines pour le début de partie */
	private static final int BGN_GAME_SEED_THRESHOLD = 40;
	
	/** Seuil maximal de graines pour la fin de partie */
	private static final int END_GAME_SEED_THRESHOLD = 12;
	
	/** Nombre de graines dans une partie */
	private static final int NB_SEEDS = 48;

	/**
	 * Choix de la stratégie à adopter selon le nombre de graines
	 * 
	 * @param decision Tableau des évaluations des coups à jouer pour chaque situation
	 * @param board L'état de la grille de jeu
	 * @return Tableau des évaluations mis à jour
	 */
	public static double[] strategies(double[] decision, Board board) {
		
		decision = globalStrategy(decision, board); // Stratégie de base
		
		if (board.getNbSeeds() > BGN_GAME_SEED_THRESHOLD)
			return bgnGameStrategy(decision, board); // Stratégie de début de partie
		else if (board.getNbSeeds() > END_GAME_SEED_THRESHOLD)
			return midGameStrategy(decision, board); // Stratégie de milieu de partie
		return endGameStrategy(decision, board); // Stratégie de fin de partie
	}

	/**
	 * Stratégie adoptée dans tous les cas
	 * 
	 * @param decision Tableau des évaluations des coups à jouer pour chaque situation
	 * @param board L'état de la grille de jeu
	 * @return Tableau des évaluations mis à jour
	 */
	private static double[] globalStrategy(double[] decision, Board board) {
		/** Score maximal possible en jouant une situation */
		double maxScore = -48;
		/** Trou avec le plus petit nombre de graines entre ceux maximisant le score */
		int minSeedHole = NB_SEEDS;
		
		for (int i = 0; i < decision.length; i++)
			if (decision[i] > maxScore)
				maxScore = decision[i];
		
		for (int i = 0; i < decision.length; i++)
			if (decision[i] == maxScore)
				if (board.getPlayerHoles()[i] < minSeedHole)
					minSeedHole = board.getPlayerHoles()[i];
		
		/** Choisit le trou maximisant le score ayant le moins de graines et étant le plus à gauche */
		for (int i = 0; i < decision.length; i++)
			if (decision[i] == maxScore && board.getPlayerHoles()[i] == minSeedHole) {
				decision[i] = NB_SEEDS;
				break;
			}

		return decision;
	}
	
	/**
	 * Stratégie de début de partie
	 * 
	 * @param decision Tableau des évaluations des coups à jouer pour chaque situation
	 * @param board L'état de la grille de jeu
	 * @return Tableau des évaluations mis à jour
	 */
	private static double[] bgnGameStrategy(double[] decision, Board board) {
		return decision;
	}
	
	/**
	 * Stratégie de milieu de partie
	 * 
	 * @param decision Tableau des évaluations des coups à jouer pour chaque situation
	 * @param board L'état de la grille de jeu
	 * @return Tableau des évaluations mis à jour
	 */
	private static double[] midGameStrategy(double[] decision, Board board) {
		return decision;
	}
	
	/**
	 * Stratégie de fin de partie
	 * 
	 * @param decision Tableau des évaluations des coups à jouer pour chaque situation
	 * @param board L'état de la grille de jeu
	 * @return Tableau des évaluations mis à jour
	 */
	private static double[] endGameStrategy(double[] decision, Board board) {
		return decision;
	}
}
