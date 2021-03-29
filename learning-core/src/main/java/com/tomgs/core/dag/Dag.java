package com.tomgs.core.dag;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * dag
 *
 * @author tomgs
 * @since 2021/3/29
 */
public class Dag {

  private String name;

  private List<Node> nodes;

  private Map<Integer, List<Node>> levelMap;

  public void addNode(Node node) {
    assert (node.getDag() == this);
    this.nodes.add(node);
  }

  public List<Node> getNodes() {
    return this.nodes;
  }

  public void addLevelNode(int level, Node node) {
    List<Node> nodes = levelMap.computeIfAbsent(level, k -> new ArrayList<>());
    nodes.add(node);
  }

}
