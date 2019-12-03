package com.automateeverything.loaders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.automateeverything.main.Material;
import com.automateeverything.mesh.Face;
import com.automateeverything.mesh.Object3D;

import org.dyn4j.dynamics.Body;
import org.joml.Vector3f;

/**
 * ObjLoader
 */
public class ObjLoader {
    public static ArrayList<Object3D> decompose(String path, HashMap<Integer, Material> materials) {
        HashMap<String, Body> collideMap = SvgLoader.decompose(path+".svg", materials);
        ArrayList<Object3D> objs = new ArrayList<>();
        String name = null;
        ArrayList<Vector3f> verticies = new ArrayList<>();
        ArrayList<Face> faces = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(path+".obj")));
            String line;

            int pointTotal = 0;
            while ((line = reader.readLine()) != null) {
                String[] split = line.split(" ");
                if (split[0].equals("o")) {
                    if(name != null) {
                        pointTotal += verticies.size();
                        objs.add(finish(collideMap.get(name), verticies, faces));
                    }
                    name = split[1];
                } else if (split[0].equals("v")) {
                    verticies.add(new Vector3f(Float.parseFloat(split[1]), Float.parseFloat(split[2]),
                            Float.parseFloat(split[3])));
                } else if (split[0].equals("f")) {
                    if (split.length > 4) {
                        faces.add(
                                new Face(
                                        new Vector3f((float) Math.random(), (float) Math.random(),
                                                (float) Math.random()),
                                        Integer.parseInt(split[1].split("/")[0]) - pointTotal - 1,
                                        Integer.parseInt(split[2].split("/")[0]) - pointTotal - 1,
                                        Integer.parseInt(split[3].split("/")[0]) - pointTotal - 1));
                        faces.add(
                                new Face(
                                        new Vector3f((float) Math.random(), (float) Math.random(),
                                                (float) Math.random()),
                                        Integer.parseInt(split[1].split("/")[0]) - pointTotal - 1,
                                        Integer.parseInt(split[3].split("/")[0]) - pointTotal - 1,
                                        Integer.parseInt(split[4].split("/")[0]) - pointTotal - 1));
                    } else
                        faces.add(
                                new Face(
                                        new Vector3f((float) Math.random(), (float) Math.random(),
                                                (float) Math.random()),
                                        Integer.parseInt(split[1].split("/")[0]) - pointTotal - 1,
                                        Integer.parseInt(split[2].split("/")[0]) - pointTotal - 1,
                                        Integer.parseInt(split[3].split("/")[0]) - pointTotal - 1));
                }
            }
            objs.add(finish(collideMap.get(name), verticies, faces));
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return objs;
    }

    private static Object3D finish(Body body, ArrayList<Vector3f> verticies, ArrayList<Face> faces) {
        Object3D obj = new Object3D(verticies.toArray(new Vector3f[0]), faces.toArray(new Face[0]), new Vector3f(), body);
        obj.collider.setMass(obj.collider.getMass().getType());
        verticies.clear();
        faces.clear();
        return obj;
    }
    
}