package model;

import java.net.URISyntaxException;

/**
 * Created by makaimark on 2017.01.11..
 */

/**
 * Irrelevant class for test purposes.
 * To be removed.
 */
public class MainTest {

    public static void main(String[] args) {
        Graph graph = new Graph();
        try {
            String url = graph.buildGraphURL("200x300", "TestGraph", "Coding|Sleeping",
                    "80,20", "7D858F,586F8E");
            System.out.println(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
