package com.automateeverything.mesh;

import com.automateeverything.control.Player;
import com.automateeverything.main.Shader;

import org.dyn4j.dynamics.Body;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Object3D
 */
public class Object3D extends Mesh{
    private Matrix4f modelViewMatrix;
    Vector3f pos;
	public Body collider;

    public Object3D(Vector3f[] verticies, Face[] faces, Vector3f pos, Body collider) {
        super(verticies, faces);
        setPos(pos);
        this.collider = collider;
    }

    public Object3D(String objPath, Shader shader, Vector3f pos, Body collider) {
        super(objPath, shader);
        setPos(pos);
        this.collider = collider;
    }

    public void render(Player player){
        render(player, modelViewMatrix, new Matrix4f());
    }

    public void setPos(Vector3f pos){
        this.pos = pos;
        modelViewMatrix = new Matrix4f().translate(pos);
    }

	public Vector3f getPos() {
		return pos;
	}

	public void update() {
        pos.add(0, (float) collider.getChangeInPosition().y, (float) -collider.getChangeInPosition().x);
        modelViewMatrix = new Matrix4f().translate(pos);
	}
}