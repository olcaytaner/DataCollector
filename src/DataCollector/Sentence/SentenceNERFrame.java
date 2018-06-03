package DataCollector.Sentence;

import javax.swing.*;

public class SentenceNERFrame extends AnnotatorFrame {
    private JCheckBox autoNERDetectionOption;

    public SentenceNERFrame(){
        super("ner");
        autoNERDetectionOption = new JCheckBox("Auto Named Entity Recognition", false);
        toolBar.add(autoNERDetectionOption);
    }

    @Override
    protected AnnotatorPanel generatePanel(String currentPath, String rawFileName) {
        return new SentenceNERPanel(currentPath, rawFileName);
    }

    public void next(){
        super.next();
        SentenceNERPanel current;
        current = (SentenceNERPanel) ((JScrollPane) projectPane.getSelectedComponent()).getViewport().getView();
        if (autoNERDetectionOption.isSelected()){
            current.autoDetect();
        }
    }

    public void previous(){
        super.previous();
        SentenceNERPanel current;
        current = (SentenceNERPanel) ((JScrollPane) projectPane.getSelectedComponent()).getViewport().getView();
        if (autoNERDetectionOption.isSelected()){
            current.autoDetect();
        }
    }

}
