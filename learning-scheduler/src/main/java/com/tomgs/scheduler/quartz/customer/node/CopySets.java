package com.tomgs.scheduler.quartz.customer.node;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.*;

/**
 * https://github.com/chartbeat-labs/trepl
 *
 * @author tangzy
 * @since 1.0
 */
public class CopySets {

  public static List<List<String>> buildCopySets(List<String> nodes, int r, int s) {

    List<Set<String>> copySets = Lists.newArrayList();
    Map<String, Integer> scatterWidths = Maps.newHashMap();
    nodes.sort(Comparable::compareTo);

    for (; ; ) {
      boolean modified = false;
      for (String node : nodes) {
        if (scatterWidths.getOrDefault(node, 0) >= s) {
          continue;
        }
        HashSet<String> copySet = Sets.newHashSet(node);
        List<String> sortedNodes = Lists.newArrayList();
        for (String n : nodes) {
          scatterWidths.putIfAbsent(n, 0);
          if (!n.equals(node)) {
            sortedNodes.add(n);
            sortedNodes.sort(Comparator.comparingInt(scatterWidths::get));
          }
        }

        for (String sortedNode : sortedNodes) {
          copySet.add(sortedNode);
          if (!checker(copySets, copySet) || copySets.contains(copySet)) {
            copySet.remove(sortedNode);
            continue;
          }
          if (copySet.size() == r) {
            copySets.add(copySet);
            modified = true;
            break;
          }
        }

        Map<String, Set<String>> scatterSets = Maps.newHashMap();
        for (Set<String> cs : copySets) {
          for (String n : cs) {
            Set<String> result = Sets.newHashSet(cs);
            Set<String> tmp = Sets.newHashSet(n);
            result.removeAll(tmp);
            scatterSets.putIfAbsent(n, Sets.newHashSet());
            scatterSets.get(n).addAll(result);
          }
        }
        scatterSets.forEach((k, v) -> scatterWidths.put(k, v.size()));
      }
      if (!modified) {
        throw new RuntimeException("Couldn't create valid copySets");
      }
      boolean present = nodes.stream().anyMatch(n -> scatterWidths.get(n) < s);
      if (!present) {
        break;
      }
    }
    List<List<String>> result = new ArrayList<>();
    copySets.forEach(e -> {
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
