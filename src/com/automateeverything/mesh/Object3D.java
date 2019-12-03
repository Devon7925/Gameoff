package com.automateeverything.mesh;

import com.automateeverything.control.Player;
import com.automateeverything.main.Shader;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Transform;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Object3D
 */
public class Object3D extends Mesh{
    private Matrix4f modelViewMatrix;
    Vector3f pos;
    Vector3f rot;
	public Body collider;

    public Object3D(Vector3f[] verticies, Face[] faces, Vector3f pos, Body collider) {
        super(verticies, faces);
        rot = new Vector3f();
        setPos(pos);
        this.collider = collider;
    }
    
    public Object3D(String objPath, Shader shader, Vector3f pos, Body collider) {
        super(objPath, shader);
        rot = new Vector3f();
        setPos(pos);
        this.collider = collider;
    }

    public void render(Player player){
        render(player, modelViewMatrix, new Matrix4f());
    }

    public void setPos(Vector3f pos){
        this.pos = pos;
        updateMatrix();
    }

	public Vector3f getPos() {
		return pos;
	}

    public Vector3f getRot() {
        return rot;
    }

    public void setRot(Vector3f rot) {
        this.rot = rot;
        updateMatrix();
    }

	public void update() {
        Transform trans = collider.getTransform();
        pos.set(0, (float) trans.getTranslationY(), (float) -trans.getTranslationX());
        rot.set((float) trans.getRotation(), 0, 0);
        updateMatrix();
    }
    
    public void updateMatrix(){
        modelViewMatrix = new Matrix4f().translate(pos);
        modelViewMatrix.rotateAffineXYZ(rot.x, rot.y, rot.z);
    }
}