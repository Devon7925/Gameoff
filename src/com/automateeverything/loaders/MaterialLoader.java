package com.automateeverything.loaders;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import com.automateeverything.main.Material;

/**
 * MaterialLoader
 */
public class MaterialLoader {

    public static HashMap<Integer, Material> decompose(String materialFile) {
        String text = null;
        try {
            text = new String(Files.readAllBytes(Paths.get(materialFile)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (text == null)
            throw new IllegalStateException();
        HashMap<Integer, Material> materials = new HashMap<>();
        for(String line : text.split(System.lineSeparator())){
            String[] args = line.split("\\s+");
            materials.put(Integer.parseInt(args[0]), new Material(Double.parseDouble(args[1]), Double.parseDouble(args[2])));
        }
        return materials;
    }
}