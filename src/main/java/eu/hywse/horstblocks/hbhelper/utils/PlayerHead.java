package eu.hywse.horstblocks.hbhelper.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

@SuppressWarnings("WeakerAccess")
public class PlayerHead {

  private static final File CACHE_DIRECTORY = new File("LabyMod//HBHelper//cache");

  public static URL getUrl(String username, int size) {
    try {
      return new URL("http://minotar.net/avatar/" + username + "/" + size);
    } catch (MalformedURLException e) {
      return null;
    }
  }

  public static File getFile(String username, int size) {
    return new File(CACHE_DIRECTORY, username + "_" + size);
  }

  public static boolean cachePlayer(String username, int size) {
    URL url = getUrl(username, size);
    if (url == null) {
      return false;
    }

    if (!CACHE_DIRECTORY.exists()) {
      System.out.println("CACHE: " + CACHE_DIRECTORY.mkdirs());
    }

    try (BufferedInputStream in = new BufferedInputStream(url.openStream());
        FileOutputStream fileOutputStream = new FileOutputStream(getFile(username, size))) {
      byte[] dataBuffer = new byte[1024];
      int bytesRead;
      while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
        fileOutputStream.write(dataBuffer, 0, bytesRead);
      }
      return true;
    } catch (IOException e) {
      System.out.println("ERROR CACHING " + username + " : " + e.getMessage());
      return false;
    }
  }

  public static ImageIcon getIcon(String username, int size) {
    if (!getFile(username, size).exists()) {
      if (!(cachePlayer(username, size))) {
        return null;
      }
    }
    try {
      return new ImageIcon(ImageIO.read(getFile(username, size)));
    } catch (IOException e) {
      return null;
    }
  }

}
