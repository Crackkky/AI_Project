//package awele.bot.Daisy2;
//
//import awele.bot.CompetitorBot;
//import awele.bot.Daisy.MinMax.MaxNodeDaisy;
//import awele.bot.Daisy.MinMax.MinMaxNodeDaisy;
//import awele.core.Board;
//import awele.core.InvalidBotException;
//
//public class Daisy2 extends CompetitorBot {
//
//    public Daisy2() throws InvalidBotException {
//        addAuthor("Arnaud GARRIGUE");
//        addAuthor("Julian ROUSSEL");
//        setBotName("Daisy2");
//    }
//
//    @Override
//    public void initialize() {
//
//    }
//
//    @Override
//    public void finish() {
//
//    }
//
//    @Override
//    public double[] getDecision(Board board) {
//        MinMaxNodeDaisy.initialize (board, 4);
//        return new MaxNodeDaisy(board).getDecision();
//    }
//
//    @Override
//    public void learn() {
//
//    }
//}
