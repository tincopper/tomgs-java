package com.tomgs.core.dag;

import java.util.HashMap;
import java.util.Map;

/**
 * @author tomgs
 * @since 2021/3/29
 */
public class DagManager {

  private Dag dag;

  // nodeName -> node
  private Map<String, Node> nodeMap = new HashMap<>();

  //
  private void addParentNode(String childNodeName, String parentNodeName) {
    Node childNode = nodeMap.get(childNodeName);
    Node parentNode = nodeMap.get(parentNodeName);

    if (parentNode.getLevel() == 0) {
      parentNode.setLevel(childNode.getLevel() + 1);
    }

    childNode.addParent(parentNode);
    dag.addNode(childNode);
    dag.addLevelNode(1, childNode);

  }

}
