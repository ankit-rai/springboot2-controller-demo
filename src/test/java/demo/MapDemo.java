package demo;

import com.beust.jcommander.internal.Maps;

import java.util.Collections;

/**
 * TODO: Add a description
 * 
 * @author Niranjan Nanda
 */
public class MapDemo {
    public static void main(String[] args) {
        System.out.println(Collections.unmodifiableMap(Maps.newHashMap()));
    }
}
