package com.tomgs.core.dag;

import java.util.List;
import lombok.Data;

/**
 * @author tomgs
 * @since 2021/3/29
 */
@Data
public class Node {

  private int level;

  private String name;

  private List<Node> parentNodes;

  private List<Node> childNodes;

  private int status;

  private Dag dag;

  void addParent(final Node node) {
    this.parentNodes.add(node);
    node.addChild(this);
  }

  private void addChild(final Node node) {
    this.childNodes.add(node);
  }

}
