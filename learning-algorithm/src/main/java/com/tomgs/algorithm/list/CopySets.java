package com.tomgs.algorithm.list;

import java.util.*;

/**
 * https://github.com/chartbeat-labs/trepl
 *
 * @author tangzy
 * @since 1.0
 */
public class CopySets {

  public static List<List<String>> buildCopySets(List<String> nodes, int r, int s) {
    nodes.sort(Comparable::compareTo);
    //System.out.println(nodes);

    List<Set<String>> copysets = new ArrayList<>();
    Map<String, Integer> scatterWidths = new HashMap<>();

    for (;;) {
      boolean modified = false;
      for (String node : nodes) {
        if (scatterWidths.getOrDefault(node, 0) >= s) {
          continue;
        }
        Set<String> copyset = new HashSet<>();
        copyset.add(node);

        List<String> sortedNodes = new ArrayList<>();
        for (String n : nodes) {
          scatterWidths.putIfAbsent(n, 0);
          if (!n.equals(node)) {
            sortedNodes.add(n);
            sortedNodes.sort(Comparator.comparingInt(scatterWidths::get));
          }
        }

        System.out.println("copyset：" + copyset);
        System.out.println("copysets:" + copysets);
        System.out.println("sortedNodes:" + sortedNodes);
        System.out.println("scatterWidths:" + scatterWidths);

        System.out.println("--------------------------------");

        for (String sortedNode : sortedNodes) {
          copyset.add(sortedNode);
          System.out.println("n:" + sortedNode);
          // 进行条件检查
          if (!checker(copysets, copyset) || copysets.contains(copyset)) {
            copyset.remove(sortedNode);
            continue;
          }
          if (copyset.size() == r) {
            copysets.add(copyset);
            modified = true;
            break;
          }
        }

        Map<String, Set<String>> scatterSets = new HashMap<>();
        for (Set<String> cs : copysets) {
          for (String n : cs) {
            Set<String> tmp = new HashSet<>();
            tmp.add(n);
            Set<String> result = new HashSet<>(cs);
            result.removeAll(tmp);
            Set<String> strings = scatterSets.get(n);
            if (strings == null) {
              Set<String> objects = new HashSet<>();
              scatterSets.put(n, objects);
            }
            strings = scatterSets.get(n);
            strings.addAll(result);
          }
        }
        scatterSets.forEach((k, v) -> scatterWidths.put(k, v.size()));
      }
      if (!modified) {
        throw new RuntimeException("Couldn't create valid copysets");
      }
      boolean present = nodes.stream().anyMatch(n -> scatterWidths.get(n) < s);
      if (!present) {
        break;
      }
    }
    List<List<String>> result = new ArrayList<>();
    copysets.forEach(e -> {
      ArrayList<String> list = new ArrayList<>(e);
      list.sort(Comparable::compareTo);
      result.add(list);
    });
    return result;
  }

  private static boolean checker(List<Set<String>> copysets, Set<String> copyset) {
    return true;
  }

  public static void main(String[] args) {
    List<String> nodes = new ArrayList<>();
    nodes.add("node1");
    nodes.add("node2");
    nodes.add("node3");

    List<List<String>> result = buildCopySets(nodes, 2, 1);
    System.out.println(">>>>>>" + result);
    List<List<String>> result1 = buildCopySets(nodes, 2, 2);
    System.out.println(">>>>>>" + result1);

    nodes.add("node4");
    nodes.add("node5");
    nodes.add("node6");
    List<List<String>> result2 = buildCopySets(nodes, 3, 3);
    System.out.println(">>>>>>" + result2);
  }

}
