package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.leapmotion.leap.*;
import java.io.*;
/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    private BitmapText HUD;
    private static boolean isRunning;
    private static Controller controller;
    private java.util.Vector<BitmapText> HUDelements;
    private Geometry geom;
    private boolean firstFrame = true;
    private long lastID = 0;
    
    public static void main(String[] args) {
        isRunning = true;
        controller = new Controller();
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        
        HUDelements = new java.util.Vector<BitmapText>();
        Box b = new Box(1, 1, 1);
        geom = new Geometry("Box", b);
        
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        geom.setMaterial(mat);
        /** Write text on the screen (HUD) */
        guiNode.detachAllChildren();
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
//        HUD = new BitmapText(guiFont, false);
//        HUD.setSize(guiFont.getCharSet().getRenderedSize());
//        HUD.setText("HUD text");
//        HUD.setLocalTranslation(300, HUD.getLineHeight()*2, 0);
//        guiNode.attachChild(HUD);
        int maxEl = 14;
        for(int ide = 0; ide < maxEl; ++ide)
        {
            BitmapText hudelement = new BitmapText(guiFont, false);
            hudelement.setSize(guiFont.getCharSet().getRenderedSize());
            hudelement.setText("Element " + ide);
            hudelement.setLocalTranslation(300, hudelement.getLineHeight()*(maxEl - ide), 0);
            HUDelements.add(hudelement);
            guiNode.attachChild(HUDelements.get(ide));
        }
        
        rootNode.attachChild(geom);
    }

    @Override
    public void simpleUpdate(float tpf) {
        if(controller.isConnected() && !firstFrame)
            getFrameAndPrint();
        else
        {
            setFirstPosition();
            firstFrame = false;
        }
            
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
    
    private void getFrameAndPrint()
    {
        Frame frame = controller.frame();
        if(lastID == frame.id())
            return;
        Vector curHandPos = new Vector();
        lastID = frame.id();
        String id = " Frame ID: " + Long.toString(frame.id()), timestamp = "Frame Timestamp: " + Long.toString(frame.timestamp());
        String hands = "Frame Hands: " + Long.toString(frame.hands().count()), fingers = "Frame Fingers: " + Long.toString(frame.fingers().count());
        HUDelements.get(0).setText("INFO: " + id + "; " + timestamp + "; " + 
                                    hands + "; " + fingers + ";");
        HUDelements.get(1).setText("HAND 1");
        HUDelements.get(2).setText("HAND 2");
        HUDelements.get(3).setText("FINGER 1.1");
        HUDelements.get(4).setText("FINGER 1.2");
        HUDelements.get(5).setText("FINGER 1.3");
        HUDelements.get(6).setText("FINGER 1.4");
        HUDelements.get(7).setText("FINGER 1.5");
        HUDelements.get(8).setText("FINGER 2.1");
        HUDelements.get(9).setText("FINGER 2.2");
        HUDelements.get(10).setText("FINGER 2.3");
        HUDelements.get(11).setText("FINGER 2.4");
        HUDelements.get(12).setText("FINGER 2.5");
        int idh = 1, idf = 3;
        for(Hand hand : frame.hands())
        {
            Vector handNormal = hand.palmNormal();
            Vector handDirection = hand.direction();
            Vector handPosition = hand.palmPosition();
            curHandPos = hand.palmPosition();
            String handDir = CoordSysConverter.outputDirectionStandard(handDirection);
            String handNorm = CoordSysConverter.outputDirectionStandard(handNormal);
            String handPos = CoordSysConverter.formatVector(handPosition);
//            String normx = String.format(java.util.Locale.US, "%.5f", handNormal.getX());
//            String normy = String.format(java.util.Locale.US, "%.5f", handNormal.getY());
//            String normz = String.format(java.util.Locale.US, "%.5f", handNormal.getZ());
//            String dirx = String.format(java.util.Locale.US, "%.5f", handDirection.getX());
//            String diry = String.format(java.util.Locale.US, "%.5f", handDirection.getY());
//            String dirz = String.format(java.util.Locale.US, "%.5f", handDirection.getZ());
//            String handInfo = "Hand " + idh + ", Direction: (" + normx + ", " + normy + ", " + normz + "), normal: (" + 
//                              dirx + ", " + diry + ", " + dirz + ")";
            String handInfo = "Hand: " + idh + ", Direction: " + handDir + ", Normal: " + handNorm + ", Position; " + handPos;
            HUDelements.get(idh).setText(handInfo);
            ++idh;
            for(Finger finger : hand.fingers())
            {
                String fingerType = finger.type().toString();
                String fingerID = Integer.toString(finger.id());
                String fingerLength = String.format(java.util.Locale.US, "%.5f", finger.length());
                String fingerWidth = String.format(java.util.Locale.US, "%.5f", finger.width());
//                String fingerDirection = finger.bone(Bone.Type.TYPE_DISTAL).direction().toString();
                String fingerDirection = CoordSysConverter.outputDirectionStandard(
                        CoordSysConverter.invertVector(finger.bone(Bone.Type.TYPE_DISTAL).direction()));
//                String fingerDirection = CoordSysConverter.outputDirectionStandard(finger.bone(Bone.Type.TYPE_DISTAL).direction());
                String fingerInfo = "Finger: " + fingerID + ", Type: " + fingerType + 
                                    ", Length: " + fingerLength + ", Width: " + fingerWidth + 
                                    ", Direction: " + fingerDirection;
                HUDelements.get(idf).setText(fingerInfo);
                ++idf;
            }
        }
        HUDelements.get(13).setText("ARMS");
        Vector lastHandPos;
        Frame lastFrame = controller.frame(1);
        HandList lastHands = lastFrame.hands();
        Hand lastHand = lastHands.get(0);
        lastHandPos = lastHand.palmPosition();
        Vector3f moveByThis = getMoveAmount(CoordSysConverter.leapToLocal(curHandPos), CoordSysConverter.leapToLocal(lastHandPos));
        geom.move(moveByThis);
    }
    
    private void setFirstPosition()
    {
        Frame firstFrame = controller.frame();
        HandList list = firstFrame.hands();
        Hand hand = list.get(0);
        geom.move(CoordSysConverter.leapVecToJMEVec(hand.palmPosition()));
    }
    
    private Vector3f getMoveAmount(Vector now, Vector last)
    {
        Vector3f result = CoordSysConverter.leapVecToJMEVec(CoordSysConverter.subVector(now, last));
        return result;
    }
}
