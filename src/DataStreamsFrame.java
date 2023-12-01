import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class DataStreamsFrame extends JFrame {

    private JPanel searchPanel;
    private JLabel searchLabel;
    private JTextField searchTextField;
    private JPanel displayPanel;
    private JTextArea originalTextArea;
    private JTextArea productTextArea;

    private JPanel buttonPanel;
    private JButton loadBtn;
    private JButton searchBtn;
    private JButton quitBtn;

    private Path filePath;

    public DataStreamsFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setTitle("Java Data Streams");
        setLayout(new BorderLayout());

        initSearchPanel();
        initDisplayPanel();
        initButtonPanel();

        add(searchPanel, BorderLayout.NORTH);
        add(displayPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);

        loadBtn.addActionListener(e -> loadFile());
        searchBtn.addActionListener(e -> searchFile());
        quitBtn.addActionListener(e -> System.exit(0));
    }

    private void initSearchPanel() {
        searchPanel = new JPanel();
        searchLabel = new JLabel("Enter Search String:");
        searchTextField = new JTextField(20);

        searchPanel.add(searchLabel);
        searchPanel.add(searchTextField);
    }

    private void initDisplayPanel() {
        displayPanel = new JPanel();
        displayPanel.setLayout(new GridLayout(1, 2));
        originalTextArea = new JTextArea(20, 30);
        productTextArea = new JTextArea(20, 30);

        originalTextArea.setEditable(false);
        productTextArea.setEditable(false);

        displayPanel.add(new JScrollPane(originalTextArea));
        displayPanel.add(new JScrollPane(productTextArea));
    }

    private void initButtonPanel() {
        buttonPanel = new JPanel();
        loadBtn = new JButton("Load File");
        searchBtn = new JButton("Search");
        quitBtn = new JButton("Quit");

        buttonPanel.add(loadBtn);
        buttonPanel.add(searchBtn);
        buttonPanel.add(quitBtn);
    }

    private void loadFile() {
        JFileChooser fileChooser = new JFileChooser("src");
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            filePath = fileChooser.getSelectedFile().toPath();
            try {
                Stream<String> lines = Files.lines(filePath);
                lines.forEach(line -> originalTextArea.append(line + "\n"));
                lines.close();
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error loading file!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void searchFile() {
        if (filePath == null) {
            JOptionPane.showMessageDialog(this, "Please load a file first!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String searchTerm = searchTextField.getText();
        try {
            Stream<String> lines = Files.lines(filePath);
            productTextArea.setText("");
            lines.filter(line -> line.contains(searchTerm))
                    .forEach(line -> productTextArea.append(line + "\n"));
            lines.close();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error searching file!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
