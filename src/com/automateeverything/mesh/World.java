package com.automateeverything.mesh;

import java.util.ArrayList;

import com.automateeverything.control.Player;

/**
 * World
 */
public class World {
    ArrayList<Object3D> objects;
    
    public World() {
        super();
        objects = new ArrayList<>();
    }

    public void add(Object3D toAdd) {
        objects.add(toAdd);
    }

    public void render(Player player) {
        objects.forEach(o -> o.render(player));
    }
}