package com.tomgs.algorithm.list;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * https://github.com/chartbeat-labs/trepl
 *
 * @author tangzy
 * @since 1.0
 */
public class CopySets {

  // checker = checker or checkers.defalut
  List<String> nodes = new ArrayList<>();

  public static List<Set<String>> buildCopySets(List<String> nodes, int r, int s) {
    nodes.sort(Comparable::compareTo);
    System.out.println(nodes);

    List<Set<String>> copysets = new ArrayList<>();
    Map<String, Integer> scatterWidths = new HashMap<>();
    Set<String> copyset = new HashSet<>();

    for (;;) {
      boolean modified = false;
      for (String node : nodes) {
        if (scatterWidths.getOrDefault(node, 0) >= s) {
          continue;
        }
        copyset.add(node);

        List<String> sortedNodes = new ArrayList<>();
        for (String n : nodes) {
          if (!n.equals(node)) {
            sortedNodes.add(n);
            sortedNodes.sort(Comparable::compareTo);
          }
        }

        for (String sortedNode : sortedNodes) {
          copyset.add(sortedNode);
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
            scatterSets.getOrDefault(n, new HashSet<>()).addAll(result);
          }
        }
      }
    }

    //return copysets;
  }

  private static boolean checker(List<Set<String>> copysets, Set<String> copyset) {
    return true;
  }

  public static void main(String[] args) {
    List<String> nodes = new ArrayList<>();
    nodes.add("node1");
    nodes.add("node2");
    nodes.add("node3");

    //List<String> result = buildCopySets(nodes, 2, 1);
    //System.out.println(result);

  }

}
