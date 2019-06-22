package eu.hywse.horstblocks.hbhelper.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class ResourceUtil {

    public static IResource getResource(String path) {
        try {
            return Minecraft.getMinecraft().getResourceManager().getResource(
                    new ResourceLocation("horstblocks/hbhelper/" + path)
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
