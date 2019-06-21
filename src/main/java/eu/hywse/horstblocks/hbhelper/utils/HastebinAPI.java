package eu.hywse.horstblocks.hbhelper.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.labymod.main.LabyMod;
import org.apache.commons.io.IOUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HastebinAPI {

    private static final JsonParser JSON_PARSER = new JsonParser();

    public static String upload(String content, boolean copy, boolean open) {
        String res;

        try {
            byte[] postData = content.getBytes(StandardCharsets.UTF_8);
            int postDataLength = postData.length;
            String request = "https://hastebin.com/documents";
            URL url = new URL(request);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            conn.setRequestProperty("Content-Type", "text/plain");
            conn.setRequestProperty("charset", "utf-8");
            conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
            conn.setUseCaches(false);
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.write(postData);
            Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));

            String json = IOUtils.toString(in);

            JsonElement jsonElement = JSON_PARSER.parse(json);
            if (!jsonElement.getAsJsonObject().has("key")) {
                JOptionPane.showMessageDialog(null, json, "Fehler beim Hochladen des Chats", JOptionPane.ERROR_MESSAGE);
                return null;
            }

            String key = jsonElement.getAsJsonObject().get("key").getAsString();
            res = "https://hastebin.com/" + key;

            /* Copy to clipboard */
            if(copy) {
                StringSelection selection = new StringSelection("https://hastebin.com/" + key);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(selection, selection);
            }

            /* Open in Webbrowser */
            if (open) {
                LabyMod.getInstance().openWebpage(res, false);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Fehler beim Hochladen des Chats", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        return res;
    }

}
