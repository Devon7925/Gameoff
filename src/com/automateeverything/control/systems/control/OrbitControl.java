package com.automateeverything.control.systems.control;

import com.automateeverything.control.Player;
import com.automateeverything.main.Globals;
import com.automateeverything.main.Window;

/**
 * OrbitControl
 */
public class OrbitControl implements ControlSystem {

    @Override
    public void run(Player player, Window window) {
        if (Globals.inputmap.get("orbit button", window)) {
            player.angleX += Globals.dm.x/100;
            player.angleY += Globals.dm.y/100;
        }
    }
}