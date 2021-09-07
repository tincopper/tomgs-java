/*
 * Copyright 2018 LinkedIn Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.tomgs.core.dag.refactor.test;

//import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.tomgs.core.dag.refactor.*;
import org.junit.Test;

import java.io.File;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Tests for running flows.
 */
public class FlowRunner2Test {

  private final DagService dagService = new DagService(new ExecutorServiceUtils());
  private final CountDownLatch flowFinishedLatch = new CountDownLatch(1);
  final ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("dag-service").build();
  private final ExecutorService executorService = Executors.newFixedThreadPool(8);

  // The recorded event sequence.
  private final List<String> eventSequence = new ArrayList<>();

  @Test
  public void runSimpleV2Flow() throws Exception {
    //final NodeBean flowNode = loadFlowNode();
    NodeBean flowNode = createFlowNode();
    final Dag dag = createDag(flowNode);
    this.dagService.startDag(dag);
    this.flowFinishedLatch.await(2, TimeUnit.SECONDS);
    //assertThat(this.eventSequence).isEqualTo(Arrays.asList("n1", "n2"));
    this.dagService.shutdownAndAwaitTermination();
  }

  @Test
  public void printFlow() {
    //NodeBean flowNode = createFlowNode();
    //NodeBean flowNode = createFlowNode2();
    NodeBean flowNode = createFlowNode3();
    final Dag dag = createDag(flowNode);
    List<Node> nodes = dag.getNodes().stream().sorted(Comparator.comparingInt(Node::getLayer))
        .collect(Collectors.toList());
    for (Node node : nodes) {
      System.out.println(node);
    }
  }

  @Test
  public void executeFlow() throws InterruptedException {
    //NodeBean flowNode = createFlowNode();
    //NodeBean flowNode = createFlowNode2();
    NodeBean flowNode = createFlowNode3();
    final Dag dag = createDag(flowNode);
    Map<Integer, List<Node>> collect = dag.getLayerNodeMap();
    for (Map.Entry<Integer, List<Node>> nodeEntry : collect.entrySet()) {
      System.out.println("------------------ execute layer " + nodeEntry.getKey());
      CountDownLatch countDownLatch = new CountDownLatch(nodeEntry.getValue().size());
      nodeEntry.getValue().forEach(n -> executorService.execute(() -> {
        System.out.println(Thread.currentThread().getName() + "执行：" + n);
        countDownLatch.countDown();
        n.setStatus(Status.SUCCESS);
      }));
      countDownLatch.await();
    }

  }

  @Test
  public void executeFlow2() {
    NodeBean flowNode = createFlowNode3();
    Dag dag = createDag(flowNode);
    Optional<NodeLayer> firstLayer = dag.getFirstLayer();
    dag.setStatus(Status.RUNNING);
    firstLayer.ifPresent(nodeLayer -> {
      List<Node> nodes = nodeLayer.getNodes();
      nodeLayer.setStatus(Status.RUNNING);
      nodeLayer.setRunningNodes(nodes.size());
      for (Node node : nodes) {
        System.out.println(node);
      }
    });

  }

  @Test
  public void executeLayerFlow() throws InterruptedException {
    NodeBean flowNode = createFlowNode3();
    final Dag dag = createDag(flowNode);
    int layerIndex = 1;
    // 获取指定层的任务
    NodeLayer layer = dag.getLayer(layerIndex);
    Status status = layer.getStatus();
    CountDownLatch countDownLatch = new CountDownLatch(layer.getNodes().size());
    if (status == Status.READY || status == Status.RUNNING) {
      for (Node node : layer.getNodes()) {
        if (node.getStatus() == Status.READY) {
          executorService.execute(() -> {
            node.setStatus(Status.SUCCESS);
            System.out.println("节点运行成功:" + node);
            countDownLatch.countDown();
          });
        }
      }
    }

    countDownLatch.await();
    System.out.println("DAG运行结果：" + dag);
  }

  private NodeBean createFlowNode() {
    NodeBean rootNode = buildSubFlowNode("root");
    NodeBean n1 = buildSubFlowNode("n1", "root");
    NodeBean n2 = buildSubFlowNode("n2", "root");
    NodeBean n3 = buildSubFlowNode("n3", "root");

    NodeBean n4 = buildSubFlowNode("n4", "n2");
    NodeBean n5 = buildSubFlowNode("n5", "n2");
    NodeBean n6 = buildSubFlowNode("n6", "n2");

    NodeBean n7 = buildSubFlowNode("n7", "n1", "n3", "n4", "n5", "n6");

    NodeBean flowNode = buildSubFlowNode("flow");
    flowNode.setNodes(ImmutableList.of(rootNode, n4, n5, n6, n7, n1, n2, n3));

    return flowNode;
  }

  private NodeBean createFlowNode2() {
    NodeBean rootNode = buildSubFlowNode("root");
    NodeBean n1 = buildSubFlowNode("n1", "root");
    NodeBean n2 = buildSubFlowNode("n2", "root");
    NodeBean n3 = buildSubFlowNode("n3", "root");

    NodeBean n4 = buildSubFlowNode("n4", "n2");
    NodeBean n5 = buildSubFlowNode("n5", "n2");
    NodeBean n6 = buildSubFlowNode("n6", "n2");

    NodeBean n7 = buildSubFlowNode("n7", "n1", "n3", "n4", "n5", "n6");

    NodeBean flowNode = buildSubFlowNode("flow");
    flowNode.setNodes(ImmutableList.of(rootNode, n1, n4, n7, n5, n6, n2, n3));

    return flowNode;
  }

  private NodeBean createFlowNode3() {
    NodeBean rootNode = buildSubFlowNode("root");
    NodeBean rn1 = buildSubFlowNode("n1", "root");
    NodeBean rn2 = buildSubFlowNode("r-n2", "root");

    NodeBean n2 = buildSubFlowNode("n2", "n1");
    NodeBean n3 = buildSubFlowNode("n3", "n1");
    NodeBean n4 = buildSubFlowNode("n4", "n1");

    NodeBean n5 = buildSubFlowNode("n5", "n2");
    NodeBean n6 = buildSubFlowNode("n6", "n2");
    NodeBean n7 = buildSubFlowNode("n7", "n2");

    NodeBean n8 = buildSubFlowNode("n8", "n4");
    NodeBean n9 = buildSubFlowNode("n9", "n4");
    NodeBean n10 = buildSubFlowNode("n10", "n4");

    NodeBean n11 = buildSubFlowNode("n11", "n5", "n6", "n7");
    NodeBean n12 = buildSubFlowNode("n12", "n3");
    NodeBean n13 = buildSubFlowNode("n13", "n8", "n9", "n10");

    NodeBean n14 = buildSubFlowNode("n14", "n11", "n12", "n13");

    NodeBean flowNode = buildSubFlowNode("flow");
    flowNode.setNodes(ImmutableList.of(rootNode, rn1, rn2, n4, n7, n5, n6, n2, n3, n8, n9, n11, n13, n14, n12, n10));

    return flowNode;
  }

  private NodeBean buildSubFlowNode(String name, String... depends) {
    NodeBean node = new NodeBean();
    node.setName(name);
    node.setDependsOn(Lists.newArrayList(depends));
    return node;
  }

  //private NodeBean loadFlowNode() throws Exception {
  //  final File flowFile = loadFlowFileFromResource();
  //  final NodeBeanLoader beanLoader = new NodeBeanLoader();
  //  return beanLoader.load(flowFile);
  //}

  private Dag createDag(final NodeBean flowNode) {
    final DagCreator creator = new DagCreator(flowNode);
    return creator.create();
  }

  private static class DagCreator {

    private final NodeBean flowNode;
    private final DagBuilder dagBuilder;

    DagCreator(final NodeBean flowNode) {
      final String flowName = flowNode.getName();
      this.flowNode = flowNode;
      this.dagBuilder = new DagBuilder(flowName);
    }

    Dag create() {
      createNodes();
      linkNodes();
      return this.dagBuilder.build();
    }

    private void createNodes() {
      for (final NodeBean node : this.flowNode.getNodes()) {
        createNode(node);
      }
    }

    private void createNode(final NodeBean node) {
      final String nodeName = node.getName();
      JobDesc jobDesc = buildJobDesc(node);
      this.dagBuilder.createNode(nodeName, jobDesc);
    }

    private JobDesc buildJobDesc(NodeBean node) {
      JobDesc jobDesc = new JobDesc();
      jobDesc.setJobName(node.getName());
      return jobDesc;
    }

    private void linkNodes() {
      for (final NodeBean node : this.flowNode.getNodes()) {
        linkNode(node);
      }
    }

    private void linkNode(final NodeBean node) {
      final String name = node.getName();
      final List<String> parents = node.getDependsOn();
      if (parents == null) {
        return;
      }
      for (final String parentNodeName : parents) {
        this.dagBuilder.addParentNode(name, parentNodeName);
      }
    }
  }

  private File loadFlowFileFromResource() {
    final ClassLoader loader = getClass().getClassLoader();
    return new File(loader.getResource("hello_world_flow.flow").getFile());
  }

}
