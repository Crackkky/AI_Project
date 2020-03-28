package awele.bot.Daisy.ChoiceTree;

import awele.core.Board;

public class ChoiceNode {

    static int threshold = 4;

    int nbChildren;
    ChoiceNode[] children;
    Boolean[] choices;

    ChoiceNode(Board b){
        nbChildren = Board.NB_HOLES - threshold;
        children = new ChoiceNode [nbChildren];
        choices = new Boolean [Board.NB_HOLES];
    }

    void makeChoice(int index, boolean bool){
        choices[index] = bool;
    }

    boolean choice(int index){
        return choices[index];
    }

}
