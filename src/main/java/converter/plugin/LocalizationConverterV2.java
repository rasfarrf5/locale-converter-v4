package converter.plugin;

import converter.plugin.support.LocaleTask;
import converter.plugin.support.LocaleTaskListener;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static converter.plugin.support.ColumnInfo.*;

public class LocalizationConverterV2 extends JPanel implements LocaleTaskListener {

    private JCheckBox allCheck = new JCheckBox("All");
    private JCheckBox englishCheck = new JCheckBox("English");
    private JCheckBox chineseCheck = new JCheckBox("Chinese");
    private JCheckBox tradChineseCheck = new JCheckBox("Trad Chinese");
    private JCheckBox germanCheck = new JCheckBox("German");
    private JCheckBox portugueseCheck = new JCheckBox("Portuguese");
    private JCheckBox frenchCheck = new JCheckBox("French");
    private JCheckBox japaneseCheck = new JCheckBox("Japanese");
    private JCheckBox koreanCheck = new JCheckBox("Korean");
    private JCheckBox russianCheck = new JCheckBox("Russian");
    private JCheckBox spanishCheck = new JCheckBox("Spanish");
    private JCheckBox indonesiaCheck = new JCheckBox("Bahasa Indonesia");
    private JCheckBox dutchCheck = new JCheckBox("Dutch");
    private JCheckBox italianCheck = new JCheckBox("Italian");
    private JCheckBox thaiCheck = new JCheckBox("Thai");

    private JRadioButton androidRadioButton = new JRadioButton("Android", true);
    private JRadioButton iosRadioButton = new JRadioButton("iOS", false);

    private JLabel statusLabel = new JLabel("", JLabel.CENTER);
    private JLabel localeFileLabel = new JLabel("", JLabel.CENTER);
    private JLabel destinationFolderLabel = new JLabel("", JLabel.CENTER);

    private File selectedFile = null;
    private File destinationPath = null;
    private JPanel mainPanel = new JPanel();

    public LocalizationConverterV2() {
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JPanel panelFile = fileChooserPanel();
        mainPanel.add(panelFile);

        JPanel osPanel = getOsPanel();
        mainPanel.add(osPanel);

        // JPanel optionPanel = getOptionPanel();
        // mainPanel.add(optionPanel);

        JPanel outputPanel = getOutputPanel();
        mainPanel.add(outputPanel);
    }

    public JPanel getContent() {
        return mainPanel;
    }

    private JPanel getOutputPanel() {
        JPanel outputPanel = new JPanel();
        outputPanel.setBorder(BorderFactory.createTitledBorder("Output status:"));

        JButton convertBtn = new JButton("Start");
        convertBtn.addActionListener(x -> startConverter());
        outputPanel.add(convertBtn);
        outputPanel.add(statusLabel);
        return outputPanel;
    }

    private JPanel getOptionPanel() {
        JPanel optionPanel = new JPanel();
        optionPanel.setBorder(BorderFactory.createTitledBorder("Choose locale to convert :"));
        optionPanel.add(allCheck);
        optionPanel.add(englishCheck);
        optionPanel.add(chineseCheck);
        optionPanel.add(tradChineseCheck);
        optionPanel.add(germanCheck);
        optionPanel.add(portugueseCheck);
        optionPanel.add(frenchCheck);
        optionPanel.add(japaneseCheck);
        optionPanel.add(koreanCheck);
        optionPanel.add(russianCheck);
        optionPanel.add(spanishCheck);
        optionPanel.add(indonesiaCheck);
        optionPanel.add(dutchCheck);
        optionPanel.add(italianCheck);
        optionPanel.add(thaiCheck);

        allCheck.addItemListener(e -> {
            englishCheck.setSelected(e.getStateChange() == ItemEvent.SELECTED);
            chineseCheck.setSelected(e.getStateChange() == ItemEvent.SELECTED);
            tradChineseCheck.setSelected(e.getStateChange() == ItemEvent.SELECTED);
            germanCheck.setSelected(e.getStateChange() == ItemEvent.SELECTED);
            portugueseCheck.setSelected(e.getStateChange() == ItemEvent.SELECTED);
            frenchCheck.setSelected(e.getStateChange() == ItemEvent.SELECTED);
            japaneseCheck.setSelected(e.getStateChange() == ItemEvent.SELECTED);
            koreanCheck.setSelected(e.getStateChange() == ItemEvent.SELECTED);
            russianCheck.setSelected(e.getStateChange() == ItemEvent.SELECTED);
            spanishCheck.setSelected(e.getStateChange() == ItemEvent.SELECTED);
            indonesiaCheck.setSelected(e.getStateChange() == ItemEvent.SELECTED);
            dutchCheck.setSelected(e.getStateChange() == ItemEvent.SELECTED);
            italianCheck.setSelected(e.getStateChange() == ItemEvent.SELECTED);
            thaiCheck.setSelected(e.getStateChange() == ItemEvent.SELECTED);
        });

        allCheck.setSelected(true);
        optionPanel.setLayout(new GridLayout(5, 3));
        return optionPanel;
    }

    private JPanel getOsPanel() {
        JPanel osPanel = new JPanel();
        osPanel.setBorder(BorderFactory.createTitledBorder("Choose OS type :"));
        osPanel.setLayout(new FlowLayout());
        ButtonGroup osGroup = new ButtonGroup();

        osGroup.add(androidRadioButton);
        osGroup.add(iosRadioButton);

        osPanel.add(androidRadioButton);
        osPanel.add(iosRadioButton);
        return osPanel;
    }

    private JPanel fileChooserPanel() {
        JPanel panelFile = new JPanel();
        panelFile.setBorder(BorderFactory.createTitledBorder("Choose localization file :"));

        panelFile.setLayout(new GridLayout(2, 2));

        JButton pickFileBtn = new JButton("Localization File :");
        JButton destinationFolderBtn = new JButton("Destination Folder :");

        panelFile.add(pickFileBtn);
        panelFile.add(localeFileLabel);
        panelFile.add(destinationFolderBtn);
        panelFile.add(destinationFolderLabel);

        pickFileBtn.addActionListener(x -> openFileChooser());
        destinationFolderBtn.addActionListener(x -> openDestinationFile());

        return panelFile;
    }

    private void openFileChooser() {
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

        int returnValue = jfc.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();

            this.selectedFile = selectedFile;
            localeFileLabel.setText(selectedFile.getName());
        }
    }

    private void openDestinationFile() {
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int returnValue = jfc.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();

            this.destinationPath = selectedFile;
            destinationFolderLabel.setText(selectedFile.getPath());
        }
    }

    public void startConverter() {
        boolean isAndroid = androidRadioButton.isSelected();

        if (selectedFile == null) {
            statusLabel.setText("Please select file to proceed.");
            return;
        }

        statusLabel.setText("Processing...");

        try {
            List<Integer> languageToConvert = new ArrayList<>();
            // if (allCheck.isSelected()) {
            languageToConvert.add(COLUMN_ENGLISH);
            languageToConvert.add(COLUMN_SIMPLIFIED_CHINESE);
            languageToConvert.add(COLUMN_TRADITIONAL_CHINESE);
            languageToConvert.add(COLUMN_GERMAN);
            languageToConvert.add(COLUMN_PORTUGAL);
            languageToConvert.add(COLUMN_FRENCH);
            languageToConvert.add(COLUMN_JAPANESE);
            languageToConvert.add(COLUMN_KOREAN);
            languageToConvert.add(COLUMN_RUSSIAN);
            languageToConvert.add(COLUMN_SPANISH);
            languageToConvert.add(COLUMN_INDONESIA);
            languageToConvert.add(COLUMN_DUTCH);
            languageToConvert.add(COLUMN_ITALY);
            languageToConvert.add(COLUMN_THAI);
			/*} else {
				if (englishCheck.isSelected()) {
					languageToConvert.add(COLUMN_ENGLISH);
				}
				if (chineseCheck.isSelected()) {
					languageToConvert.add(COLUMN_SIMPLIFIED_CHINESE);
				}
				if (tradChineseCheck.isSelected()) {
					languageToConvert.add(COLUMN_TRADITIONAL_CHINESE);
				}
				if (germanCheck.isSelected()) {
					languageToConvert.add(COLUMN_GERMAN);
				}
				if (portugueseCheck.isSelected()) {
					languageToConvert.add(COLUMN_PORTUGAL);
				}
				if (frenchCheck.isSelected()) {
					languageToConvert.add(COLUMN_FRENCH);
				}
				if (japaneseCheck.isSelected()) {
					languageToConvert.add(COLUMN_JAPANESE);
				}
				if (koreanCheck.isSelected()) {
					languageToConvert.add(COLUMN_KOREAN);
				}
				if (russianCheck.isSelected()) {
					languageToConvert.add(COLUMN_RUSSIAN);
				}
				if (spanishCheck.isSelected()) {
					languageToConvert.add(COLUMN_SPANISH);
				}
				if (indonesiaCheck.isSelected()) {
					languageToConvert.add(COLUMN_INDONESIA);
				}
				if (dutchCheck.isSelected()) {
					languageToConvert.add(COLUMN_DUTCH);
				}
				if (italianCheck.isSelected()) {
					languageToConvert.add(COLUMN_ITALY);
				}
				if (thaiCheck.isSelected()) {
					languageToConvert.add(COLUMN_THAI);
				}
			}*/

            new LocaleTask(isAndroid, selectedFile, destinationPath, languageToConvert, this).execute();

        } catch (Exception e) {
            onTaskFailed();
        }
    }

    @Override
    public void onTaskSuccess() {
        statusLabel.setText("Strings converted successfully");
        statusLabel.setForeground(Color.GREEN);
    }

    @Override
    public void onTaskFailed() {
        statusLabel.setText("Problem with converter please try again!!");
        statusLabel.setForeground(Color.RED);
    }
}
