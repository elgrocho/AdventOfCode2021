package adventofcode.day12;

import java.io.InputStream;
import java.util.*;

public class PassagePathing {
    public static void main(String[] args) {
        InputStream inputStream = PassagePathing.class.getResourceAsStream("input.txt");
        Map<Node, Set<Node>> graph = new HashMap<>();
        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNextLine()) {
            String[] split = scanner.nextLine().split("-");
            Node node1 = createNode(split[0]);
            Node node2 = createNode(split[1]);
            Set<Node> node1Nodes = graph.getOrDefault(node1, new HashSet<>());
            node1Nodes.add(node2);
            graph.put(node1, node1Nodes);
            Set<Node> node2Nodes = graph.getOrDefault(node2, new HashSet<>());
            node2Nodes.add(node1);
            graph.put(node2, node2Nodes);
        }

        //part 1
        List<List<Node>> paths = findPaths(new Node("start", true), new ArrayList<>(), graph, true);
        System.out.println(paths.size());
        //part 2
        List<List<Node>> paths2 = findPaths(new Node("start", true), new ArrayList<>(), graph, false);
        System.out.println(paths2.size());
    }

    private static List<List<Node>> findPaths(Node node,
                                               List<Node> path,
                                               Map<Node, Set<Node>> graph,
                                               boolean visitedSmallCavernTwice) {

        path.add(node);

        if(node.name().equals("end")) {
            return List.of(path);
        }

        List<List<Node>> paths = new ArrayList<>();
        Set<Node> neighbours = graph.get(node);
        for (Node neighbour : neighbours) {
            if(neighbour.name().equals("start")) {
                continue;
            }
            boolean visitedAlready = neighbour.visitOnce() && path.contains(neighbour);
            if(visitedAlready) {
                if(!visitedSmallCavernTwice)
                    paths.addAll(findPaths(neighbour, new ArrayList<>(path), graph, true));
                continue;
            }
            paths.addAll(findPaths(neighbour, new ArrayList<>(path), graph, visitedSmallCavernTwice));
        }
        return paths;
    }

    private static Node createNode(String name) {
        return new Node(name, !name.toUpperCase().equals(name));
    }

    record Node(String name, boolean visitOnce) {}
}
