package DataCollector.WordNet;

import Dictionary.TurkishWordComparator;
import Dictionary.TxtDictionary;
import Util.DrawingButton;
import WordNet.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Locale;
import java.util.Properties;

public abstract class DomainEditorFrame extends JFrame implements ActionListener {
    protected WordNet turkish, domainWordNet;
    protected TxtDictionary dictionary;
    protected JToolBar toolBar;

    protected static final String SAVE = "save";

    private String domainWordNetFileName;
    private String domainDictionaryFileName;
    protected String domainPrefix = "turkish";
    protected String wordNetPrefix = "TUR10-";
    protected int finalId;
    protected boolean modified = false;
    abstract void loadContents();

    @Override
    public void actionPerformed(ActionEvent e) {
        if (SAVE.equals(e.getActionCommand())) {
            domainWordNet.saveAsXml(domainWordNetFileName);
            dictionary.saveAsTxt(domainDictionaryFileName);
            modified = false;
        }
    }

    protected SynSet addSynSet(SynSet addedSynSet, String root){
        boolean newOne = false;
        SynSet newSynSet = domainWordNet.getSynSetWithId(addedSynSet.getId());
        if (newSynSet == null){
            newOne = true;
            newSynSet = new SynSet(addedSynSet.getId());
            newSynSet.setPos(addedSynSet.getPos());
            newSynSet.setDefinition(addedSynSet.getLongDefinition());
        }
        boolean found = false;
        for (int i = 0; i < addedSynSet.getSynonym().literalSize(); i++){
            if (addedSynSet.getSynonym().getLiteral(i).getName().toLowerCase(new Locale("tr")).startsWith(root.toLowerCase(new Locale("tr")))){
                domainWordNet.addLiteralToLiteralList(addedSynSet.getSynonym().getLiteral(i));
                newSynSet.addLiteral(addedSynSet.getSynonym().getLiteral(i));
                found = true;
                break;
            }
        }
        if (!found){
            newSynSet.addLiteral(new Literal(root, 1, newSynSet.getId()));
        }
        if (newOne){
            domainWordNet.addSynSet(newSynSet);
        }
        return newSynSet;
    }

    private int getFinalId(){
        int max = 0;
        for (SynSet synSet : domainWordNet.synSetList()){
            if (synSet.getId().startsWith(wordNetPrefix)){
                int id = Integer.parseInt(synSet.getId().substring(wordNetPrefix.length()));
                if (id > max){
                    max = id;
                }
            }
        }
        return max;
    }

    private void addButtons() {
        JButton save = new DrawingButton(WordNetEditorFrame.class, this, "save", SAVE, "Save");
        toolBar.add(save);
    }

    public DomainEditorFrame(){
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(new File("config.properties")));
            wordNetPrefix = properties.getProperty("wordNetPrefix");
            domainPrefix = properties.getProperty("domainPrefix");
            domainWordNetFileName = domainPrefix + "_wordnet.xml";
            domainDictionaryFileName = domainPrefix + "_dictionary.txt";
        } catch (IOException e) {
            e.printStackTrace();
        }
        dictionary = new TxtDictionary(domainDictionaryFileName, new TurkishWordComparator());
        domainWordNet = new WordNet(domainWordNetFileName, new Locale("tr"));
        finalId = getFinalId();
        turkish = new WordNet();
        toolBar = new JToolBar("ToolBox");
        addButtons();
        add(toolBar, BorderLayout.PAGE_START);
        toolBar.setVisible(true);
        loadContents();
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e) {
                JFrame frame = (JFrame) e.getSource();
                if (modified){
                    int result = JOptionPane.showConfirmDialog(frame,
                            "Are you sure you want to exit the application without saving?",
                            frame.getName(),
                            JOptionPane.YES_NO_OPTION);
                    if (result == JOptionPane.YES_OPTION){
                        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    }
                } else {
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                }
            }
        });
        setVisible(true);
    }

}
