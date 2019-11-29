package com.automateeverything.mesh;

import java.util.ArrayList;

import com.automateeverything.control.Player;

import org.dyn4j.geometry.Vector2;

/**
 * World
 */
public class World {
    ArrayList<Object3D> objects;
    org.dyn4j.dynamics.World physics;
    
    public World() {
        super();
        objects = new ArrayList<>();
        physics = new org.dyn4j.dynamics.World();
        physics.setGravity(new Vector2(0, -2));
    }

    public void add(Object3D toAdd) {
        objects.add(toAdd);
        physics.addBody(toAdd.collider);
    }

    public void render(Player player) {
        objects.forEach(o -> o.render(player));
    }

	public void update() {
        physics.step(1);
        objects.forEach(n->n.update());
	}
}