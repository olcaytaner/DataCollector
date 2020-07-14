package DataCollector.Sentence;

import AnnotatedSentence.AnnotatedCorpus;
import DataCollector.ParseTree.TreeEditorPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

public class SentenceShallowParseFrame extends AnnotatorFrame{

    public SentenceShallowParseFrame(){
        super();
        AnnotatedCorpus corpus;
        corpus = new AnnotatedCorpus(new File(TreeEditorPanel.phrasePath));
        JMenuItem itemViewAnnotated = addMenuItem(projectMenu, "View Annotations", KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        itemViewAnnotated.addActionListener(e -> {
            new ViewShallowParseAnnotationFrame(corpus, this);
        });
    }

    @Override
    protected AnnotatorPanel generatePanel(String currentPath, String rawFileName) {
        return new SentenceShallowParsePanel(currentPath, rawFileName);
    }
}
