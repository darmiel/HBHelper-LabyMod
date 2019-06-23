package eu.hywse.horstblocks.hbhelper.modules.chatgui.gui;

import eu.hywse.horstblocks.hbhelper.HelperAddon;
import eu.hywse.horstblocks.hbhelper.utils.PlayerHead;
import lombok.Getter;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("WeakerAccess")
@Getter
public class UserChatDesigner extends JPanel {

    JTabbedPane tabbedPane;
    JButton btnSend = new JButton("Absenden");
    JScrollPane scrollChatBox;
    JTextPane txtChatBox = new JTextPane();
    JButton btnCloseSingle = new JButton("\uD83D\uDD12");
    JButton btnClearChat = new JButton("Chat leeren");
    JButton btnDelete = new JButton("L\u00F6schen");
    JLabel lblChatHochladen = new JLabel("Chat hochladen auf:");
    JButton btnUploadHastebin = new JButton("Hastebin");
    JButton btnCloseAll = new JButton("\uD83D\uDD12\uD83D\uDD12");
    JTextField txtInputField = new JTextField();

    private String username;

    public UserChatDesigner(String username, JTabbedPane tabbedPane) {
        this.username = username;
        this.tabbedPane = tabbedPane;

        this.scrollChatBox = new JScrollPane(txtChatBox,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // Tooltip
        btnCloseSingle.setToolTipText("Schließt den aktuellen Chat");
        btnSend.setToolTipText("Sendet die Nachricht");
        btnClearChat.setToolTipText("Chatverlauf löschen");
        btnUploadHastebin.setToolTipText("Chatverlauf auf HasteBin hochladen");
        btnCloseAll.setToolTipText("Alle gelesenen Chatverläufe schließen");
        btnClearChat.setToolTipText("Chatverlauf löschen");

        // Settings
        txtInputField.setText("");
        txtInputField.setFont(new Font("Lucida Console", Font.PLAIN, 23));
        txtInputField.setColumns(10);

        // Text Box
        txtChatBox.setBorder(new EmptyBorder(10, 10, 10, 10));
        txtChatBox.setMargin(new Insets(5, 5, 5, 5));
        txtChatBox.setAutoscrolls(true);
        txtChatBox.setEditable(false);

        GroupLayout groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(Alignment.TRAILING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
                                        .addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
                                                .addComponent(txtInputField, GroupLayout.DEFAULT_SIZE, 648, Short.MAX_VALUE)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(btnSend, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE))
                                        .addComponent(scrollChatBox, GroupLayout.DEFAULT_SIZE, 739, Short.MAX_VALUE))
                                .addGap(10)
                                .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addComponent(btnCloseSingle)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(btnCloseAll, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE))
                                        .addComponent(btnDelete, GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
                                        .addComponent(btnClearChat, GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
                                        .addComponent(btnUploadHastebin, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(lblChatHochladen))
                                .addContainerGap())
        );
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(Alignment.TRAILING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                        .addComponent(scrollChatBox, GroupLayout.DEFAULT_SIZE, 499, Short.MAX_VALUE)
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                                        .addComponent(btnCloseSingle, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(btnCloseAll, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE))
                                                .addGap(18)
                                                .addComponent(btnClearChat)
                                                .addGap(18)
                                                .addComponent(lblChatHochladen)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(btnUploadHastebin, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
                                        .addComponent(btnDelete, Alignment.TRAILING)
                                        .addComponent(btnSend, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtInputField, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
        );
        setLayout(groupLayout);
    }

    public void appendComment(String comment) {
        if (txtChatBox.getDocument().getLength() > 0) {
            appendTxtChat("\n", Color.BLACK);
        }
        appendTxtChat("// " + comment, Color.GRAY);
    }

    void addChatMessage(String from, String message) {
        if (txtChatBox.getDocument().getLength() > 0) {
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

        txtChatBox.setEditable(true);
        int len = txtChatBox.getDocument().getLength();
        txtChatBox.setCaretPosition(len);
        txtChatBox.setCharacterAttributes(attributeSet, false);
        txtChatBox.replaceSelection(message);
        txtChatBox.setEditable(false);
    }

    void appendTxtChatIcon(Icon icon) {
        int len = txtChatBox.getDocument().getLength();

        txtChatBox.setEditable(true);
        txtChatBox.setCaretPosition(len);
        txtChatBox.insertIcon(icon);
        txtChatBox.setEditable(false);
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
