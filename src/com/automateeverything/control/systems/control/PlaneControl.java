package com.automateeverything.control.systems.control;

import com.automateeverything.control.Player;
import com.automateeverything.main.Globals;
import com.automateeverything.main.Window;

import org.joml.Vector3f;

/**
 * PlaneControl
 */
public class PlaneControl implements ControlSystem {

    @Override
    public void run(Player player, Window window) {
        if(Globals.inputmap.get("perp button", window)){
            Vector3f rot = new Vector3f(0.0f, 0.0f, 1.0f).rotateX(-player.angleY/2).rotateY(player.angleX/2);
            if(Globals.dm.x != 0) player.move(new Vector3f(rot).cross(0,1,0).setComponent(1, 0).normalize().mul((float) Globals.dm.x/30));
            if(Globals.dm.y != 0) player.move(rot.cross(-rot.z, 0, rot.x).setComponent(1, 0).normalize().mul((float) -Globals.dm.y/30));
        }
    }
}