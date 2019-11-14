package com.automateeverything.control.systems;

import com.automateeverything.control.Player;
import com.automateeverything.main.Window;
import com.automateeverything.mesh.Object3D;

/**
 * FollowControl
 */
public class FollowControl implements ControlSystem {
    Object3D followed;
    public FollowControl(Object3D toFollow) {
        super();
        followed = toFollow;
    }

    @Override
    public void run(Player player, Window window) {
        player.focus = followed.getPos();
    }
}