package com.automateeverything.control.systems.control;

import com.automateeverything.control.Player;
import com.automateeverything.main.Window;

/**
 * ZoomControl
 */
public class ZoomControl implements ControlSystem {

    @Override
    public void run(Player player, Window window) {
        player.r = (float) Math.exp(-window.scroll.y/3);
    }
}