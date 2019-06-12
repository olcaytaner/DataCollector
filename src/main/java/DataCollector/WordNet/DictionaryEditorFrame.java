package DataCollector.WordNet;

import Dictionary.Pos;
import Dictionary.TxtWord;
import MorphologicalAnalysis.Transition;
import WordNet.Literal;
import WordNet.SynSet;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class DictionaryEditorFrame extends DomainEditorFrame implements ActionListener {
    private ArrayList<String> data;
    private JTable dataTable;
    private ImageIcon addIcon, deleteIcon;
    private HashMap<String, PanelObject> display;

    //protected final String domainDataFileName = "estate_data.txt";
    protected final String domainDataFileName = "tourism_data.txt";

    @Override
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        switch (e.getActionCommand()) {
            case SAVE:
                saveData();
                break;
        }
    }

    public void saveData(){
        BufferedWriter outfile;
        try {
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(domainDataFileName), "UTF-8");
            outfile = new BufferedWriter(writer);
            for (String root : data) {
                outfile.write(root + "\n");
            }
            outfile.close();
        } catch (IOException ioException) {
            System.out.println("Output file can not be opened");
        }
    }

    public class FlagObject{
        String[] flags = null;

        public FlagObject(TxtWord word){
            if (word != null){
                String[] items = word.toString().split(" ");
                flags = Arrays.copyOfRange(items, 1, items.length);
            }
        }
    }

    public class SynSetObject{
        ArrayList<SynSet> synSets = new ArrayList<>();
        ArrayList<SynSet> extraSynSets = new ArrayList<>();

        public SynSetObject(TxtWord word){
            Transition verbTransition = new Transition("mAk");
            if (word != null){
                String verbForm = verbTransition.makeTransition(word, word.getName());
                synSets = domainWordNet.getSynSetsWithLiteral(word.getName());
                synSets.addAll(domainWordNet.getSynSetsWithLiteral(verbForm));
                ArrayList<SynSet> candidates = turkish.getSynSetsWithLiteral(word.getName());
                for (SynSet synSet : candidates){
                    if (!synSets.contains(synSet)){
                        extraSynSets.add(synSet);
                    }
                }
                candidates = turkish.getSynSetsWithLiteral(verbForm);
                for (SynSet synSet : candidates){
                    if (!synSets.contains(synSet)){
                        extraSynSets.add(synSet);
                    }
                }
            }
        }
    }

    public class PanelObject{
        private FlagObject flagObject;
        private SynSetObject synSetObject;
        private TxtWord word;
        private String root;
        private JPanel flagPanel;
        private JPanel synSetIdPanel;
        private JPanel synSetPosPanel;
        private JPanel synSetEditPanel;

        private void createFlagPanel(int row){
            flagPanel = new JPanel(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.gridy = 0;
            if (flagObject.flags != null){
                for (String flag : flagObject.flags){
                    JButton delete = new JButton();
                    delete.setIcon(deleteIcon);
                    c.gridx = 0;
                    flagPanel.add(delete, c);
                    c.gridx = 1;
                    flagPanel.add(new JLabel(flag), c);
                    c.gridy++;
                    delete.addActionListener(e -> {
                        word.removeFlag(flag);
                        PanelObject panelObject = new PanelObject(root, row);
                        display.put(root, panelObject);
                    });
                }
                dataTable.setRowHeight(row, 35 * (flagObject.flags.length + 1));
            } else {
                dataTable.setRowHeight(row, 35);
            }
            JComboBox flagComboBox = new JComboBox();
            flagComboBox.addItem("CL_ISIM");
            flagComboBox.addItem("IS_OA");
            flagComboBox.addItem("IS_HM");
            flagComboBox.addItem("IS_ADJ");
            flagComboBox.addItem("IS_ADVERB");
            flagComboBox.addItem("CL_FIIL");
            flagComboBox.addItem("IS_SAYI");
            flagComboBox.addItem("IS_ZM");
            flagComboBox.addItem("IS_CONJ");
            flagComboBox.addItem("IS_QUES");
            flagComboBox.addItem("IS_INTERJ");
            flagComboBox.addItem("IS_SD");
            flagComboBox.addItem("IS_UD");
            flagComboBox.addItem("IS_KG");
            flagComboBox.addItem("IS_ST");
            flagComboBox.addItem("F_SD");
            flagComboBox.addItem("F_UD");
            flagComboBox.addItem("F_GUD");
            flagComboBox.addItem("IS_KIS");
            flagComboBox.addItem("IS_DUP");
            flagComboBox.addItem("IS_BILEŞ");
            flagComboBox.addItem("IS_POSTP");
            flagComboBox.addItem("IS_CA");
            flagComboBox.addItem("F1P1-NO-REF");
            flagComboBox.addItem("F2P1-NO-REF");
            flagComboBox.addItem("F3P1-NO-REF");
            flagComboBox.addItem("F4P1-NO-REF");
            flagComboBox.addItem("F4PR-NO-REF");
            flagComboBox.addItem("F4PL-NO-REF");
            flagComboBox.addItem("F4PW-NO-REF");
            flagComboBox.addItem("F5PL-NO-REF");
            flagComboBox.addItem("F5PR-NO-REF");
            flagComboBox.addItem("F5PW-NO-REF");
            flagComboBox.addItem("F1P1");
            flagComboBox.addItem("F2P1");
            flagComboBox.addItem("F2PL");
            flagComboBox.addItem("F3P1");
            flagComboBox.addItem("F4P1");
            flagComboBox.addItem("F4PR");
            flagComboBox.addItem("F4PL");
            flagComboBox.addItem("F4PW");
            flagComboBox.addItem("F5P1");
            flagComboBox.addItem("F5PL");
            flagComboBox.addItem("F5PR");
            flagComboBox.addItem("F5PW");
            flagComboBox.addItem("F6P1");
            flagComboBox.addItem("PASSIVE-HN");
            JButton add = new JButton();
            add.setIcon(addIcon);
            c.gridx = 0;
            add.addActionListener(e -> {
                dictionary.addWithFlag(root, (String) flagComboBox.getSelectedItem());
                PanelObject panelObject = new PanelObject(root, row);
                display.put(root, panelObject);
            });
            flagPanel.add(add, c);
            c.gridx = 1;
            flagPanel.add(flagComboBox, c);
        }

        public void createSynSetPanel(int column, int row){
            JPanel newPanel = new JPanel(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.gridy = 0;
            c.gridx = 0;
            for (SynSet synSet : synSetObject.synSets){
                switch (column){
                    case 1:
                        newPanel.add(new JLabel(synSet.getId()), c);
                        c.gridy++;
                        break;
                    case 2:
                        newPanel.add(new JLabel(synSet.getPos().toString()), c);
                        c.gridy++;
                        break;
                    case 4:
                        JButton delete = new JButton();
                        delete.setIcon(deleteIcon);
                        delete.addActionListener(e -> {
                            domainWordNet.removeSynSet(synSet);
                            PanelObject panelObject = new PanelObject(root, row);
                            display.put(root, panelObject);
                        });
                        c.gridx = 0;
                        newPanel.add(delete, c);
                        c.gridx = 1;
                        if (synSet.getDefinition() != null && synSet.getDefinition().equals(" ")){
                            newPanel.add(new JLabel("No Definition"), c);
                        } else {
                            if (synSet.getDefinition() != null && synSet.getDefinition().length()  > 70){
                                newPanel.add(new JLabel(synSet.getDefinition().substring(0, 69) + "..."), c);
                            } else {
                                newPanel.add(new JLabel(synSet.getDefinition()), c);
                            }
                        }
                        c.gridy++;
                        break;
                }
            }
            if (column == 4){
                c.gridx = 0;
                JComboBox synSetChooser = new JComboBox();
                if (word != null){
                    if (word.isNominal()){
                        synSetChooser.addItem("New SynSet (NOUN)");
                    }
                    if (word.isAdjective()){
                        synSetChooser.addItem("New SynSet (ADJECTIVE)");
                    }
                    if (word.isVerb()){
                        synSetChooser.addItem("New SynSet (VERB)");
                    }
                    if (word.isAdverb()){
                        synSetChooser.addItem("New SynSet (ADVERB)");
                    }
                }
                for (SynSet  synSet : synSetObject.extraSynSets){
                    if (synSet.getDefinition() != null && synSet.getDefinition().length()  > 70){
                        synSetChooser.addItem(synSet.getDefinition().substring(0, 69) + "...");
                    } else {
                        synSetChooser.addItem(synSet.getDefinition());
                    }
                }
                if (synSetChooser.getItemCount() > 0){
                    JButton add = new JButton();
                    add.setIcon(addIcon);
                    add.addActionListener(e -> {
                        if (synSetChooser.getSelectedIndex() != -1){
                            int extraRows = 0;
                            for (int i = 0; i < synSetChooser.getItemCount(); i++){
                                if (((String) synSetChooser.getItemAt(i)).startsWith("New SynSet")){
                                    extraRows++;
                                } else {
                                    break;
                                }
                            }
                            SynSet addedSynSet;
                            Literal addedLiteral;
                            Pos pos;
                            if (synSetChooser.getSelectedIndex() < extraRows){
                                finalId += 10;
                                String newSynSetId = prefix + "" + finalId;
                                String selectedText = (String) synSetChooser.getSelectedItem();
                                if (selectedText.contains("NOUN")){
                                    addedLiteral = new Literal(root, 1, newSynSetId);
                                    pos = Pos.NOUN;
                                } else {
                                    if (selectedText.contains("ADJECTIVE")){
                                        addedLiteral = new Literal(root, 1, newSynSetId);
                                        pos = Pos.ADJECTIVE;
                                    } else {
                                        if (selectedText.contains("VERB")){
                                            Transition verbTransition = new Transition("mAk");
                                            String verbForm = verbTransition.makeTransition(word, word.getName());
                                            addedLiteral = new Literal(verbForm, 1, newSynSetId);
                                            pos = Pos.VERB;
                                        } else {
                                            addedLiteral = new Literal(root, 1, newSynSetId);
                                            pos = Pos.ADVERB;
                                        }
                                    }
                                }
                                addedSynSet = new SynSet(newSynSetId);
                                addedSynSet.addLiteral(addedLiteral);
                                addedSynSet.setPos(pos);
                                domainWordNet.addSynSet(addedSynSet);
                                domainWordNet.addLiteralToLiteralList(addedLiteral);
                            } else {
                                addedSynSet = synSetObject.extraSynSets.get(synSetChooser.getSelectedIndex() - extraRows);
                                if (addedSynSet.getPos().equals(Pos.VERB)){
                                    Transition verbTransition = new Transition("mAk");
                                    String verbForm = verbTransition.makeTransition(word, word.getName());
                                    addSynSet(addedSynSet, verbForm);
                                } else {
                                    addSynSet(addedSynSet, root);
                                }
                            }
                            PanelObject panelObject = new PanelObject(root, row);
                            display.put(root, panelObject);
                        }
                    });
                    newPanel.add(add, c);
                    c.gridx = 1;
                    newPanel.add(synSetChooser, c);
                }
            }
            switch (column){
                case 1:
                    synSetIdPanel = newPanel;
                    break;
                case 2:
                    synSetPosPanel = newPanel;
                    break;
                case 4:
                    synSetEditPanel = newPanel;
                    break;
            }
        }

        public PanelObject(String root, int row){
            this.root = root;
            word = (TxtWord) dictionary.getWord(root);
            flagObject = new FlagObject(word);
            synSetObject = new SynSetObject(word);
            createFlagPanel(row);
            createSynSetPanel(1, row);
            createSynSetPanel(2, row);
            createSynSetPanel(4, row);
        }
    }

    public PanelObject addIfNotExists(String root, int row){
        if (display.containsKey(root)){
            return display.get(root);
        } else {
            PanelObject panelObject = new PanelObject(root, row);
            display.put(root, panelObject);
            return panelObject;
        }
    }

    public class SynSetCell extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {

        private JPanel getPanel(int row, int column){
            PanelObject panelObject = addIfNotExists(data.get(row), row);
            switch (column){
                case 1:
                    return panelObject.synSetIdPanel;
                case 2:
                    return panelObject.synSetPosPanel;
                case 4:
                    return panelObject.synSetEditPanel;
                default:
                    return null;
            }
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return getPanel(row, column);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            return getPanel(row, column);
        }

        @Override
        public Object getCellEditorValue() {
            return null;
        }
    }

    public class FlagCell extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            PanelObject panelObject = addIfNotExists(data.get(row), row);
            return panelObject.flagPanel;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            PanelObject panelObject = addIfNotExists(data.get(row), row);
            return panelObject.flagPanel;
        }

        @Override
        public Object getCellEditorValue() {
            return null;
        }
    }

    public class TableDataModel extends AbstractTableModel {

        public int getColumnCount() {
            return 6;
        }

        public int getRowCount() {
            return data.size();
        }

        public String getColumnName(int col) {
            switch (col){
                case 0:
                    return "No";
                case 1:
                    return "WordNet ID";
                case 2:
                    return "Pos";
                case 3:
                    return "Root";
                case 4:
                    return "Meaning";
                case 5:
                    return "Flags";
                default:
                    return "";
            }
        }

        public Class getColumnClass(int col){
            switch (col){
                case 1:
                case 2:
                case 4:
                    return SynSetObject.class;
                case 5:
                    return FlagObject.class;
            }
            return Object.class;
        }

        public Object getValueAt(int row, int col) {
            PanelObject panelObject;
            int currentHeight, newHeight;
            switch (col){
                case 0:
                    return row + 1;
                case 1:
                case 2:
                case 4:
                    panelObject = addIfNotExists(data.get(row), row);
                    currentHeight = dataTable.getRowHeight(row);
                    newHeight = (panelObject.synSetObject.synSets.size() + 1) * 35;
                    if (newHeight > currentHeight){
                        dataTable.setRowHeight(row, newHeight);
                    }
                    return panelObject.synSetObject;
                case 3:
                    return data.get(row);
                case 5:
                    panelObject = addIfNotExists(data.get(row), row);
                    currentHeight = dataTable.getRowHeight(row);
                    if (panelObject.flagObject.flags != null){
                        newHeight = (panelObject.flagObject.flags.length + 1) * 35;
                        if (newHeight > currentHeight){
                            dataTable.setRowHeight(row, newHeight);
                        }
                    }
                    return panelObject.flagObject;
                default:
                    return "";
            }
        }

        public boolean isCellEditable(int row, int col) {
            if (col < 3) {
                return false;
            } else {
                return true;
            }
        }

        public void setValueAt(Object value, int row, int col) {
            switch (col){
                case 3:
                    data.set(row, (String) value);
                    break;
            }
            fireTableCellUpdated(row, col);
        }
    }

    public void loadContents(){
        setName("Dictionary Editor");
        display = new HashMap<>();
        data = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(domainDataFileName), "UTF8"));
            String line = br.readLine();
            while (line != null) {
                data.add(line);
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String imgLocation = "/icons/addparent.png";
        URL imageURL = this.getClass().getResource(imgLocation);
        addIcon = new ImageIcon(imageURL);
        imgLocation = "/icons/delete.png";
        imageURL = this.getClass().getResource(imgLocation);
        deleteIcon = new ImageIcon(imageURL);
        dataTable = new JTable(new TableDataModel());
        dataTable.getColumnModel().getColumn(0).setMinWidth(60);
        dataTable.getColumnModel().getColumn(0).setMaxWidth(60);
        dataTable.getColumnModel().getColumn(1).setMinWidth(120);
        dataTable.getColumnModel().getColumn(1).setMaxWidth(120);
        dataTable.getColumnModel().getColumn(2).setMinWidth(90);
        dataTable.getColumnModel().getColumn(2).setMaxWidth(90);
        dataTable.getColumnModel().getColumn(5).setMinWidth(200);
        dataTable.getColumnModel().getColumn(5).setMaxWidth(200);
        FlagCell flagCell = new FlagCell();
        dataTable.setDefaultRenderer(FlagObject.class, flagCell);
        dataTable.setDefaultEditor(FlagObject.class, flagCell);
        SynSetCell synSetCell = new SynSetCell();
        dataTable.setDefaultRenderer(SynSetObject.class, synSetCell);
        dataTable.setDefaultEditor(SynSetObject.class, synSetCell);
        JScrollPane tablePane = new JScrollPane(dataTable);
        add(tablePane, BorderLayout.CENTER);
    }
}
