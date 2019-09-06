package eu.hywse.horstblocks.hbhelper.modules.chatgui;

/**
 * Implement this
 */
@Deprecated
public class AutoAfkReply {

//    private boolean afk;
//    private int currentTick = 0;
//
//    private long lastMove;
//
//    private int lastMouseX;
//    private int lastMouseY;
//    private final static int IDLE_SECONDS = 600;
//
//    @SubscribeEvent
//    public void onTick(TickEvent.ClientTickEvent event) {
//        if (event.phase != TickEvent.Phase.END) {
//            return;
//        }
//        if (LabyModCore.getMinecraft().getPlayer() == null) {
//            return;
//        }
//        int currentMouseX = Mouse.getX();
//        int currentMouseY = Mouse.getY();
//
//        long currentMillis = System.currentTimeMillis();
//
//
//        if (this.lastMouseX != currentMouseX || this.lastMouseY != currentMouseY || (LabyModCore.getMinecraft().getPlayer()).moveForward != 0.0F ||
//                (LabyModCore.getMinecraft().getPlayer()).moveStrafing != 0.0F ||
//                (LabyModCore.getMinecraft().getPlayer()).fallDistance != 0.0F) {
//
//            if (this.afk) {
//                this.afk = false;
//            }
//
//            this.lastMove = currentMillis;
//        } else if (this.afk) {
//            if (++this.currentTick >= 20) {
//                this.currentTick = 0;
//            }
//        } else if (currentMillis - this.lastMove >= (1000 * IDLE_SECONDS)) {
//            this.afk = true;
//        }
//
//        this.lastMouseX = currentMouseX;
//        this.lastMouseY = currentMouseY;
//    }

}
