package awele.bot.DaisyBot;

import awele.bot.CompetitorBot;
import awele.core.Board;
import awele.core.InvalidBotException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class DaisyBot extends CompetitorBot {
	/**
	 * @throws InvalidBotException
	 */

	private MinMAxData won;

	private int compteurCoups;

	private static final int MAX_LEARN_TIME = 5;//0 * 60 * 1000;
	private long start, end;

	private static final int k = 10; // voisin
	private int NB_HOLES = Board.NB_HOLES; // nb trou

	private Random random = new Random();

	private int MAX_PARTIE = 30; // le nombre de partie jouer au max pour l'apprentissage
	private int DEPTH_LEARN = 11; // profondeur du minmax de l'apprentissage
	private int MAX_VOISIN = 10;

	private int DEPTH = 8;// profondeur du minmax

	public DaisyBot() throws InvalidBotException {
		this.setBotName("Daisy");
		this.addAuthor("Julian ROUSSEL");
		this.addAuthor("Arnaud GARRIGUE");
	}

	/**
	 * Rien à faire
	 */

	@Override
	public void initialize() {
		compteurCoups = 0;
	}

	/**
	 * Pas d'apprentissage
	 */
	@Override
	public void learn() {

		System.gc();
		start = System.currentTimeMillis();

		////////////////////////////////////////////
		// initialisation variable
		////////////////////////////////////////////
		this.won = new MinMAxData(true);
		// this.lost = new MinMAxData(false);
		// data = AweleData.getInstance();
		double save;
		int indice;
		int coupAleatoirePlay = 0;
		int coupDecision = Integer.MIN_VALUE;
		int nbPartie = random.nextInt(MAX_PARTIE);
		Board copy = new Board();
		Board copysave = new Board();
		double[] decision = new double[NB_HOLES];
		int[] apprentissage = new int[13];

		ArrayList<Board> listMinMax = new ArrayList<Board>();

		Set set = new HashSet();

		////////////////////////////////////////////
		// création de board minmax
		////////////////////////////////////////////
		for (int i = 0; i < 4; i++) {
			copy = copysave;
			int j = 0, score = 0;
			do {
				try {
					listMinMax.add(copy);
					MinMaxNode.initialize(copy, 6 + i);
					decision = new MaxNode(copy).getDecision();
					score = copy.playMoveSimulationScore(j % 2, decision);
					copy = copy.playMoveSimulationBoard(j % 2, decision);
				} catch (InvalidBotException e) {
					e.printStackTrace();
				}
				j++;
			} while (!((score < 0) || (copy.getScore(Board.otherPlayer(copy.getCurrentPlayer())) >= 25)
					|| (copy.getNbSeeds() <= 6)));
			listMinMax.add(copy);
		}

		int saveSize = listMinMax.size();

		for (int i = 0; i < NB_HOLES; i++) {
			decision = new double[NB_HOLES];
			decision[i] = Double.MAX_VALUE;
			try {
				for (int j = 0; j < saveSize; j++) {
					listMinMax.add((Board) listMinMax.get(j)
							.playMoveSimulationBoard(Board.otherPlayer(listMinMax.get(j).getCurrentPlayer()), decision)
							.clone());
				}
			} catch (InvalidBotException e) {
				e.printStackTrace();
			}
		}

		////////////////////////////////////////////
		// boucle apprentissage contre minmax
		////////////////////////////////////////////
		set.addAll(listMinMax);

		listMinMax.clear();
		listMinMax.addAll(set);

		////////////////////////////////////////////
		// boucle apprentissage contre minmax
		////////////////////////////////////////////
		for (Board copyLearn : listMinMax) {

			////////////////////////////////////////////
			// MinMax sur le board minmax
			////////////////////////////////////////////
			MinMaxNode.initialize(copyLearn, DEPTH_LEARN);
			decision = new MaxNode(copyLearn).getDecision();

			////////////////////////////////////////////
			// Récupération du coup fort pour l'apprentissage
			////////////////////////////////////////////
			save = decision[0];
			indice = 0;
			for (int i = 1; i < NB_HOLES; i++) {
				if (save < decision[i]) {
					save = decision[i];
					indice = i;
				}
			}

			////////////////////////////////////////////
			// formatage et ajout dans la liste des coups forts
			////////////////////////////////////////////
			for (int j = 0; j < NB_HOLES; j++)
				apprentissage[j] = copyLearn.getPlayerHoles()[j];
			for (int j = 0; j < NB_HOLES; j++)
				apprentissage[j + NB_HOLES] = copyLearn.getOpponentHoles()[j];
			apprentissage[12] = indice + 1;

			this.won.data.add(apprentissage.clone());
		}

		listMinMax.clear();

		////////////////////////////////////////////
		// boucle d'apprentissage
		////////////////////////////////////////////
		do {

			////////////////////////////////////////////
			// création de board aléatoire
			////////////////////////////////////////////
			copy = new Board();
			try {
				for (int i = 0; i < nbPartie; i++) {
					coupAleatoirePlay = random.nextInt(6);
					for (int j = 0; j < NB_HOLES; j++) {
						if (coupAleatoirePlay == j)
							decision[j] = 1;
						else
							decision[j] = 0;
					}

					copy = copy.playMoveSimulationBoard(i % 2, decision);
				}

				////////////////////////////////////////////
				// MinMax sur le board aléatoire
				////////////////////////////////////////////
				MinMaxNode.initialize(copy, DEPTH_LEARN);
				decision = new MaxNode(copy).getDecision();

				////////////////////////////////////////////
				// Récupération du coup fort pour l'apprentissage
				////////////////////////////////////////////
				save = decision[0];
				indice = 0;
				for (int i = 1; i < NB_HOLES; i++) {
					if (save < decision[i]) {
						save = decision[i];
						indice = i;
					}
				}

				////////////////////////////////////////////
				// formatage et ajout dans la liste des coups forts
				////////////////////////////////////////////
				for (int j = 0; j < 6; j++)
					apprentissage[j] = copy.getPlayerHoles()[j];
				for (int j = 0; j < 6; j++)
					apprentissage[j + 6] = copy.getOpponentHoles()[j];
				apprentissage[12] = indice + 1;

				this.won.data.add(apprentissage.clone());

			} catch (InvalidBotException e) {
				e.printStackTrace();
			}

			end = System.currentTimeMillis();

		} while (MAX_LEARN_TIME > (end - start));

	}

	/**
	 * Sélection du coup selon l'algorithme MinMax
	 */
	@Override
	public double[] getDecision(Board board) {

		int[] x = new int[12];
		int[] holes = board.getPlayerHoles();
		for (int i = 0; i < 6; i++)
			x[i] = holes[i];
		holes = board.getOpponentHoles();
		for (int i = 0; i < 6; i++)
			x[i + 6] = holes[i];
		double[] neighborsWon = this.won.countNeighbors(x, k);
		double save = neighborsWon[0];
		for (int i = 1; i < neighborsWon.length; i++) {
			if (save < neighborsWon[i])
				save = neighborsWon[i];
		}

		if (compteurCoups < 2) {
			if (compteurCoups == 0) {
				double[] decision = new double[Board.NB_HOLES];
				decision[5] = Double.MAX_VALUE;
				compteurCoups++;
				return decision;
			}
			if (compteurCoups == 1) {
				double[] decision = new double[Board.NB_HOLES];
				decision[3] = Double.MAX_VALUE;
				compteurCoups++;
				return decision;
			}
			compteurCoups++;
		}

		if (save >= MAX_VOISIN)
			return neighborsWon;
		else {
			MinMaxNode.initialize(board, DEPTH);
			double[] decisionTest = new MaxNode(board).getDecision();

			decisionTest = Strategies.strategies(decisionTest, board);

			return decisionTest;
		}
	}

	/**
	 * Rien à faire
	 */
	@Override
	public void finish() {
	}
}
