package eu.hywse.horstblocks.hbhelper.modules.chatgui.gui;

import eu.hywse.horstblocks.hbhelper.HelperAddon;
import eu.hywse.horstblocks.hbhelper.utils.PlayerHead;
import lombok.Getter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("WeakerAccess")
@Getter
public class UserChatDesigner extends Panel {

    JTabbedPane tabbedPane;
    // Components
    JButton btnClose;
    JButton btnSend;
    JButton btnClearChat;
    JButton btnUploadHastebin;
    JButton btnCloseRead;
    JButton btnDeleteChat;
    JTextPane txtChat;
    JTextArea txtMsg;

    private String username;

    public UserChatDesigner(String username, JTabbedPane tabbedPane) {
        this.username = username;
        this.tabbedPane = tabbedPane;

        btnClose = new JButton("\uD83D\uDD12");
        btnSend = new JButton("Senden");
        btnClearChat = new JButton("Chat leeren");
        btnUploadHastebin = new JButton("Hastebin");
        btnCloseRead = new JButton("C"); // ☠
        btnDeleteChat = new JButton("⚠ Löschen"); // ⚠

        // Tooltip
        btnClose.setToolTipText("Schließt den aktuellen Chat");
        btnSend.setToolTipText("Sendet die Nachricht");
        btnClearChat.setToolTipText("Chatverlauf löschen");
        btnUploadHastebin.setToolTipText("Chatverlauf auf HasteBin hochladen");
        btnCloseRead.setToolTipText("Alle gelesenen Chatverläufe schließen");
        btnDeleteChat.setToolTipText("Chatverlauf löschen");


        txtChat = new JTextPane();
        txtMsg = new JTextArea(5, 5);

        setPreferredSize(new Dimension(1314, 749));
        setLayout(null);

        btnClose.setBounds(1195, 30, 50, 25);
        btnCloseRead.setBounds(1245, 30, 50, 25);

        btnClearChat.setBounds(1195, 65, 100, 25);
        btnUploadHastebin.setBounds(1195, 100, 100, 25);
        btnSend.setBounds(1080, 680, 100, 45);
        btnDeleteChat.setBounds(1195, 680, 100, 25);

        txtChat.setBounds(25, 30, 1157, 635);
        txtMsg.setBounds(25, 680, 1040, 50);

        // Text Box
        txtChat.setBorder(new EmptyBorder(10, 10, 10, 10));
        txtChat.setMargin(new Insets(5, 5, 5, 5));
        txtChat.setAutoscrolls(true);
        txtChat.setEditable(false);

        txtMsg.setFont(new Font("Lucida Console", Font.PLAIN, 24));

        add(btnClose);
        add(btnSend);
        add(btnClearChat);
        add(btnUploadHastebin);
        add(btnCloseRead);
        add(btnDeleteChat);

        add(txtChat);
        add(txtMsg);
    }

    public void appendComment(String comment) {
        if (txtChat.getDocument().getLength() > 0) {
            appendTxtChat("\n", Color.BLACK);
        }
        appendTxtChat("// " + comment, Color.GRAY);
    }

    void addChatMessage(String from, String message) {
        if (txtChat.getDocument().getLength() > 0) {
            appendTxtChat("\n", Color.BLACK);
        }

        appendTxtChat(String.format("[%s]: ", new SimpleDateFormat("HH:mm:ss").format(new Date())), Color.DARK_GRAY);

        long start = System.currentTimeMillis();
        appendTxtChatIcon(PlayerHead.getIcon(from != null ? getUsername() : HelperAddon.getInstance().getApi().getPlayerUsername(), 24));
        // System.out.println("Took: " + (System.currentTimeMillis() - start) + " ms");

        appendTxtChat(" " + (from == null ? "Du" : from), from == null ? Color.RED : Color.BLUE);
        appendTxtChat(" >> ", Color.DARK_GRAY);
        appendTxtChat(message, from == null ? Color.RED : Color.BLUE);
    }

    void appendTxtChat(String message, Color color) {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet attributeSet = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, color);
        attributeSet = sc.addAttribute(attributeSet, StyleConstants.FontFamily, "Lucida Console");
        attributeSet = sc.addAttribute(attributeSet, StyleConstants.FontSize, 24);
        attributeSet = sc.addAttribute(attributeSet, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        txtChat.setEditable(true);
        int len = txtChat.getDocument().getLength();
        txtChat.setCaretPosition(len);
        txtChat.setCharacterAttributes(attributeSet, false);
        txtChat.replaceSelection(message);
        txtChat.setEditable(false);
    }

    void appendTxtChatIcon(Icon icon) {
        int len = txtChat.getDocument().getLength();

        txtChat.setEditable(true);
        txtChat.setCaretPosition(len);
        txtChat.insertIcon(icon);
        txtChat.setEditable(false);
    }

    public String getUsername() {
        return username;
    }

    public boolean inFocus() {
        return SwingUtilities.isDescendingFrom(this, tabbedPane.getSelectedComponent());
    }

    public int getTabIndex() {
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            if (SwingUtilities.isDescendingFrom(tabbedPane.getComponentAt(i), this)) {
                return i;
            }
        }
        return -1;
    }
}
