/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.math.Vector3f;
import com.leapmotion.leap.*;
import java.io.*;

/**
 *
 * @author Andreas
 */
public class CoordSysConverter {
    
    public static final String FORWARD = "Forward", REVERSE = "Reverse", UP = "Up", 
                               DOWN = "Down", LEFT = "Left", RIGHT = "Right";
    public static final Vector FORWARDV = new Vector(0, 0, -1), REVERSEV = new Vector(0, 0, 1), 
                               UPV = new Vector(0, 1, 0), DOWNV = new Vector(0, -1, 0), 
                               LEFTV = new Vector(-1, 0, 0), RIGHTV = new Vector(1, 0, 0);
    private static final float ratio = 100;
    
    public static Vector localToLeap(Vector localVector)
    {
        Vector result = multVector(localVector, ratio);
        return result;
    }
    
    public static Vector leapToLocal(Vector leapVector)
    {
        Vector result = divVector(leapVector, ratio);
        return result;
    }
    
    public static String outputDirectionStandard(Vector dir)
    {
        String result = "";
        Boolean picked = false;
        double limit = 45*(Math.PI/180);
        double angles[] = new double[6];
        angles[0] = getAngle(dir, FORWARDV);
        angles[1] = getAngle(dir, REVERSEV);
        angles[2] = getAngle(dir, UPV);
        angles[3] = getAngle(dir, DOWNV);
        angles[4] = getAngle(dir, LEFTV);
        angles[5] = getAngle(dir, RIGHTV);
        int savedID = 0;
        double smallestAngle = 1000;
        for(int idd = 0; idd < 6; ++idd)
        {
            if(smallestAngle > angles[idd])
            {
                smallestAngle = angles[idd];
                savedID = idd;
            }
        }
        switch(savedID)
        {
            case 0:
                result = FORWARD;
                break;
            case 1:
                result = REVERSE;
                break;
            case 2:
                result = UP;
                break;
            case 3:
                result = DOWN;
                break;
            case 4:
                result = LEFT;
                break;
            case 5:
                result = RIGHT;
                break;
        }
        return result;
    }
    
    public static double getAngle(Vector v1, Vector v2)
    {
        return Math.acos(scalarProduct(v1, v2)/(returnLength(v1)*returnLength(v2)));
    }
    
    public static double returnLength(Vector v1)
    {
        return Math.sqrt(Math.pow(v1.getX(), 2) + Math.pow(v1.getY(), 2) + Math.pow(v1.getZ(), 2));
    }
    
    public static double scalarProduct(Vector v1, Vector v2)
    {
        return (v1.getX()*v2.getX()) + (v1.getY()*v2.getY()) + (v1.getZ()*v2.getZ());
    }
    
    public static Vector normalize(Vector vec)
    {
        float length = (float)returnLength(vec);
        float normx = vec.getX()/length;
        float normy = vec.getY()/length;
        float normz = vec.getZ()/length;
        Vector result = new Vector(normx, normy, normz);
        return result;
    }
    
    public static Vector invertVector(Vector vec)
    {
        Vector result = new Vector(-vec.getX(), -vec.getY(), -vec.getZ());
        return result;
    }
    
    public static Vector3f invertVector(Vector3f vec)
    {
        Vector3f result = new Vector3f(-vec.x, -vec.y, -vec.z);
        return result;
    }
    
    public static String formatVector(Vector vec)
    {
        String result;
        String xcoord = String.format(java.util.Locale.US, "%.5f", vec.getX());
        String ycoord = String.format(java.util.Locale.US, "%.5f", vec.getY());
        String zcoord = String.format(java.util.Locale.US, "%.5f", vec.getZ());
        result = "(" + xcoord + ", " + ycoord + ", " + zcoord + ")";
        return result;
    }
    
    //Basic vector arithmetic functions
    public static Vector addVector(Vector v1, Vector v2)
    {
        Vector result = new Vector(v1.getX() + v2.getX(), v1.getY() + v2.getY(), v1.getZ() + v2.getZ());
        return result;
    }
    
    public static Vector3f addVector(Vector3f v1, Vector3f v2)
    {
        Vector3f result = new Vector3f(v1.getX() + v2.getX(), v1.getY() + v2.getY(), v1.getZ() + v2.getZ());
        return result;
    }
    
    public static Vector subVector(Vector v1, Vector v2)
    {
        Vector result = new Vector(v1.getX() - v2.getX(), v1.getY() - v2.getY(), v1.getZ() - v2.getZ());
        return result;
    }
    
    public static Vector3f subVector(Vector3f v1, Vector3f v2)
    {
        Vector3f result = new Vector3f(v1.getX() - v2.getX(), v1.getY() - v2.getY(), v1.getZ() - v2.getZ());
        return result;
    }
    
    public static Vector multVector(Vector v1, float val)
    {
        Vector result = new Vector(v1.getX()*val, v1.getY()*val, v1.getZ()*val);
        return result;
    }
    
    public static Vector3f multVector(Vector3f v1, float val)
    {
        Vector3f result = new Vector3f(v1.getX()*val, v1.getY()*val, v1.getZ()*val);
        return result;
    }
    
    public static Vector divVector(Vector v1, float val)
    {
        Vector result = new Vector(v1.getX()/val, v1.getY()/val, v1.getZ()/val);
        return result;
    }
    
    public static Vector3f divVector(Vector3f v1, float val)
    {
        Vector3f result = new Vector3f(v1.getX()/val, v1.getY()/val, v1.getZ()/val);
        return result;
    }
    
    public static Vector3f leapVecToJMEVec(Vector vec)
    {
        Vector3f result = new Vector3f();
        result.set(vec.getX(), vec.getY(), vec.getZ());
        return result;
    }
    
    public static Vector jmeVecToLeapVec(Vector3f vec)
    {
        Vector result = new Vector(vec.x, vec.y, vec.z);
        return result;
    }
    
}   
