package DataCollector.Sentence;

import AnnotatedSentence.*;
import AnnotatedSentence.AutoProcessor.AutoDisambiguation.TurkishSentenceAutoDisambiguator;
import MorphologicalAnalysis.FsmMorphologicalAnalyzer;
import MorphologicalAnalysis.FsmParseList;

import java.awt.*;

public class SentenceMorphologicalAnalyzerPanel extends AnnotatorPanel{
    private FsmMorphologicalAnalyzer fsm;
    private TurkishSentenceAutoDisambiguator turkishSentenceAutoDisambiguator;

    public SentenceMorphologicalAnalyzerPanel(String currentPath, String fileName, FsmMorphologicalAnalyzer fsm, TurkishSentenceAutoDisambiguator turkishSentenceAutoDisambiguator){
        super(currentPath, fileName, ViewLayerType.INFLECTIONAL_GROUP, null);
        this.fsm = fsm;
        this.turkishSentenceAutoDisambiguator = turkishSentenceAutoDisambiguator;
        setLayout(new BorderLayout());
    }

    public void autoDetect(){
        turkishSentenceAutoDisambiguator.autoDisambiguate(sentence);
        sentence.save();
        this.repaint();
    }

    public int populateLeaf(AnnotatedSentence sentence, int wordIndex){
        int selectedIndex = -1;
        AnnotatedWord word = (AnnotatedWord) sentence.getWord(wordIndex);
        listModel.clear();
        FsmParseList fsmParseList = fsm.robustMorphologicalAnalysis(word.getName());
        for (int i = 0; i < fsmParseList.size(); i++){
            if (word.getParse() != null && word.getParse().toString().equals(fsmParseList.getFsmParse(i).transitionList())){
                selectedIndex = i;
            }
            listModel.addElement(fsmParseList.getFsmParse(i));
        }
        return selectedIndex;
    }


}
