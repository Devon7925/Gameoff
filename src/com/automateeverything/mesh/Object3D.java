package com.automateeverything.mesh;

import com.automateeverything.control.Player;
import com.automateeverything.main.Shader;

import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Object3D
 */
public class Object3D extends Mesh{
    private Matrix4f modelViewMatrix;
    Vector3f pos;

    public Object3D(Vector3f[] verticies, Face[] faces, Vector3f pos) {
        super(verticies, faces);
        setPos(pos);
    }

    public Object3D(String objPath, Shader shader, Vector3f pos) {
        super(objPath, shader);
        setPos(pos);
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
}