import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.sun.speech.freetts.*;
import org.json.*;
import javax.swing.SwingUtilities;

public class SmartChatbot extends JFrame {

    public JTextPane chatPane;
    public StyledDocument doc;
    public JTextField inputField;
    public JButton sendButton, micButton;

    public static final String VOICENAME = "kevin16";

    public SmartChatbot() {
        setTitle("Smart Chatbot");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        chatPane = new JTextPane();
        chatPane.setFont(new Font("SansSerif", Font.PLAIN, 16));
        chatPane.setEditable(false);
        chatPane.setBackground(new Color(240, 248, 255));
        doc = chatPane.getStyledDocument();

        JScrollPane scrollPane = new JScrollPane(chatPane);

        inputField = new JTextField();
        inputField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        sendButton = new JButton("Send");
        micButton = new JButton("🎤");

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inputPanel.add(inputField, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(micButton);
        buttonPanel.add(sendButton);
        inputPanel.add(buttonPanel, BorderLayout.EAST);

        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        setJMenuBar(createMenuBar());

        sendButton.addActionListener(e -> processInput());
        micButton.addActionListener(e -> handleVoiceInput());
        inputField.addActionListener(e -> processInput());

        setVisible(true);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu toolsMenu = new JMenu("Tools");

        JMenuItem italicItem = new JMenuItem("Italicize Last");
        italicItem.addActionListener(e -> formatLastMessage("italic"));

        JMenuItem boldItem = new JMenuItem("Bold Last");
        boldItem.addActionListener(e -> formatLastMessage("bold"));

        JMenuItem underlineItem = new JMenuItem("Underline Last");
        underlineItem.addActionListener(e -> formatLastMessage("underline"));

        JMenu sizeMenu = new JMenu("Font Size");
        JMenuItem smallSize = new JMenuItem("Small");
        JMenuItem mediumSize = new JMenuItem("Medium");
        JMenuItem largeSize = new JMenuItem("Large");
        smallSize.addActionListener(e -> formatLastMessage("small"));
        mediumSize.addActionListener(e -> formatLastMessage("medium"));
        largeSize.addActionListener(e -> formatLastMessage("large"));
        sizeMenu.add(smallSize);
        sizeMenu.add(mediumSize);
        sizeMenu.add(largeSize);
     // Inside createMenuBar()
        JMenu themeMenu = new JMenu("Theme");

        JMenuItem lightMode = new JMenuItem("Light Mode");
        lightMode.addActionListener(e -> applyTheme(false));

        JMenuItem darkMode = new JMenuItem("Dark Mode");
        darkMode.addActionListener(e -> applyTheme(true));

        themeMenu.add(lightMode);
        themeMenu.add(darkMode);

        menuBar.add(toolsMenu);
        menuBar.add(themeMenu); // Add this line


        JMenu colorMenu = new JMenu("Text Color");
        JMenuItem redColor = new JMenuItem("Red");
        JMenuItem blueColor = new JMenuItem("Blue");
        JMenuItem greenColor = new JMenuItem("Green");
        redColor.addActionListener(e -> formatLastMessage("red"));
        blueColor.addActionListener(e -> formatLastMessage("blue"));
        greenColor.addActionListener(e -> formatLastMessage("green"));
        colorMenu.add(redColor);
        colorMenu.add(blueColor);
        colorMenu.add(greenColor);

        JMenuItem clearItem = new JMenuItem("Clear Chat");
        clearItem.addActionListener(e -> chatPane.setText(""));

        toolsMenu.add(italicItem);
        toolsMenu.add(boldItem);
        toolsMenu.add(underlineItem);
        toolsMenu.addSeparator();
        toolsMenu.add(sizeMenu);
        toolsMenu.add(colorMenu);
        toolsMenu.addSeparator();
        toolsMenu.add(clearItem);

        menuBar.add(toolsMenu);
        return menuBar;
    }
    private void applyTheme(boolean darkMode) {
        Color bgColor, fgColor, inputBg;

        if (darkMode) {
            bgColor = new Color(45, 45, 45); // dark background
            fgColor = Color.WHITE;
            inputBg = new Color(70, 70, 70);
        } else {
            bgColor = new Color(240, 248, 255); // light background
            fgColor = Color.BLACK;
            inputBg = Color.WHITE;
        }

        chatPane.setBackground(bgColor);
        chatPane.setForeground(fgColor);
        inputField.setBackground(inputBg);
        inputField.setForeground(fgColor);
        inputField.setCaretColor(fgColor);
        getContentPane().setBackground(bgColor);
    }

    public void formatLastMessage(String styleType) {
        try {
            Element root = doc.getDefaultRootElement();
            int count = root.getElementCount();
            if (count < 1) return;

            Element lastLine = root.getElement(count - 1);
            int start = lastLine.getStartOffset();
            int end = lastLine.getEndOffset();

            if (end > doc.getLength()) {
                end = doc.getLength();
            }

            String lastText = doc.getText(start, end - start);
            int lengthToRemove = end - start;

            if (start >= 0 && start + lengthToRemove <= doc.getLength()) {
                doc.remove(start, lengthToRemove);
            } else if (start >= 0 && start < doc.getLength()) {
                doc.remove(start, doc.getLength() - start);
            }

            Style style = doc.addStyle("Style", null);

            switch (styleType) {
                case "bold": StyleConstants.setBold(style, true); break;
                case "italic": StyleConstants.setItalic(style, true); break;
                case "underline": StyleConstants.setUnderline(style, true); break;
                case "small": StyleConstants.setFontSize(style, 12); break;
                case "medium": StyleConstants.setFontSize(style, 16); break;
                case "large": StyleConstants.setFontSize(style, 20); break;
                case "red": StyleConstants.setForeground(style, Color.RED); break;
                case "blue": StyleConstants.setForeground(style, Color.BLUE); break;
                case "green": StyleConstants.setForeground(style, new Color(0, 128, 0)); break;
            }

            doc.insertString(start, lastText, style);

        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public void processInput() {
        String userText = inputField.getText().trim();
        if (userText.isEmpty()) return;

        appendWithTimestamp("You: " + userText);
        inputField.setText("");

        new Thread(() -> {
            try {
                String response = getBotResponse(userText);
                appendWithTimestamp("Bot: " + response);
                speak(response);
            } catch (Exception e) {
                e.printStackTrace();
                appendWithTimestamp("Bot: Sorry, there was an error.");
            }
        }).start();
    }

    public void appendWithTimestamp(String message) {
        String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
        try {
            doc.insertString(doc.getLength(), "[" + time + "] " + message + "\n", null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public String getBotResponse(String input) throws IOException {
        URL url = new URL(getApiUrl());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        JSONObject content = new JSONObject();
        content.put("parts", new JSONArray().put(new JSONObject().put("text", input)));

        JSONObject body = new JSONObject();
        body.put("contents", new JSONArray().put(content));

        try (OutputStream os = connection.getOutputStream()) {
            byte[] inputBytes = body.toString().getBytes("utf-8");
            os.write(inputBytes, 0, inputBytes.length);
        }

        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }

        JSONObject jsonResponse = new JSONObject(response.toString());
        return jsonResponse.getJSONArray("candidates")
                .getJSONObject(0)
                .getJSONObject("content")
                .getJSONArray("parts")
                .getJSONObject(0)
                .getString("text").trim();
    }

    public static String getApiKey() {
        return "AIzaSyCHgHd3QMGT5Ad77f4KQRvpNBpYGChcak0";
    }

    public static String getApiUrl() {
        return "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + getApiKey();
    }

    public void speak(String text) {
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        VoiceManager vm = VoiceManager.getInstance();
        Voice voice = vm.getVoice(VOICENAME);
        if (voice != null) {
            voice.allocate();
            voice.speak(text);
        } else {
            System.out.println("Voice not found.");
        }
    }

    public void handleVoiceInput() {
        appendWithTimestamp("Listening... (simulated mic input)");
        String simulatedSpeech = "what is chatbot";
        inputField.setText(simulatedSpeech);
        processInput();
    }
   

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SmartChatbot bot = new SmartChatbot();
            bot.applyTheme(false);
            bot.greetUser();

            // For example, simulate a user input and format it after some delay:
            new Thread(() -> {
                try {
                    Thread.sleep(1500);
                    SwingUtilities.invokeLater(() -> {
                        bot.appendWithTimestamp("You: Hello, chatbot!");
                        bot.formatLastMessage("bold");
                        bot.formatLastMessage("green");
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        });
    }

    public void greetUser() {
        appendWithTimestamp("Bot: Welcome! Ask me anything.");
        speak("Welcome! Ask me anything.");
    }
}
