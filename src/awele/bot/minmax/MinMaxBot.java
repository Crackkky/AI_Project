//package awele.bot.minmax;
//
//import awele.bot.DemoBot;
//import awele.core.Board;
//import awele.core.InvalidBotException;
//
///**
// * @author Alexandre Blansché
// * Bot qui selon le MinMax
// */
//public class MinMaxBot extends DemoBot
//{
//    /**
//     * @throws InvalidBotException
//     */
//    public MinMaxBot () throws InvalidBotException
//    {
//        this.setBotName ("MinMax");
//        this.addAuthor ("Alexandre Blansché");
//    }
//
//    /**
//     * Rien à faire
//     */
//    @Override
//    public void initialize ()
//    {
//    }
//
//    /**
//     * Pas d'apprentissage
//     */
//    @Override
//    public void learn ()
//    {
//    }
//
//    /**
//     * Sélection du coup selon l'algorithme MinMax
//     */
//    @Override
//    public double [] getDecision (Board board)
//    {
//        MinMaxNode.initialize (board, 2);
//        return new MaxNode (board).getDecision ();
//    }
//
//    /**
//     * Rien à faire
//     */
//    @Override
//    public void finish ()
//    {
//    }
//}
