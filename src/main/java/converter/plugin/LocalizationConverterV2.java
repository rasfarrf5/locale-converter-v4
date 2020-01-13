package converter.plugin;

import com.intellij.ui.JBColor;
import converter.plugin.support.LocaleTask;
import converter.plugin.support.LocaleTaskListener;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static converter.plugin.support.ColumnInfo.*;

public class LocalizationConverterV2 extends JPanel implements LocaleTaskListener {

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

        int returnValue = jfc.showOpenDialog(mainPanel);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();

            this.selectedFile = selectedFile;
            localeFileLabel.setText(selectedFile.getName());
        }
    }

    private void openDestinationFile() {
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int returnValue = jfc.showOpenDialog(mainPanel);
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
        setDefaultStatusColor();

        try {
            List<Integer> languageToConvert = new ArrayList<>();
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

            new LocaleTask(isAndroid, selectedFile, destinationPath, languageToConvert, this).execute();

        } catch (Exception e) {
            onTaskFailed();
        }
    }

    private void setDefaultStatusColor() {
        if (JBColor.isBright()) {
            statusLabel.setForeground(JBColor.DARK_GRAY);
        } else {
            statusLabel.setForeground(JBColor.WHITE);
        }
    }

    @Override
    public void onTaskSuccess() {
        statusLabel.setText("Strings converted successfully");
        statusLabel.setForeground(JBColor.GREEN);
    }

    @Override
    public void onTaskFailed() {
        statusLabel.setText("Problem with converter please try again!!");
        statusLabel.setForeground(JBColor.RED);
    }
}
