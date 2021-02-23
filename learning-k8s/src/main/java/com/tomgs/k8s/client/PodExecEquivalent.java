package com.tomgs.k8s.client;

import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.ExecListener;
import io.fabric8.kubernetes.client.dsl.ExecWatch;
import java.io.ByteArrayOutputStream;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import okhttp3.Response;

/**
 * https://github.com/fabric8io/kubernetes-client/blob/master/kubernetes-examples/src/main/java/io/fabric8/kubernetes/examples/kubectl/equivalents/PodExecEquivalent.java
 *
 * @author tomgs
 * @since 2021/2/23
 */
public class PodExecEquivalent {

  private static final Logger logger = Logger.getLogger(PodExecEquivalent.class.getName());
  private static final CountDownLatch execLatch = new CountDownLatch(1);

  public static void main(String[] args) {
    Config config = new ConfigBuilder()
        .withMasterUrl("https://k8s-master.com")
        .build();
    try (final KubernetesClient k8s = new DefaultKubernetesClient(config)) {
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      ByteArrayOutputStream error = new ByteArrayOutputStream();

      ExecWatch execWatch = k8s.pods().inNamespace("ide").withName("theia-maven-release-v1-b699c999d-grlqp")
          .writingOutput(out)
          .writingError(error)
          .usingListener(new MyPodExecListener())
          .exec("sh", "-c", "cd /home/theia && pwd && ls");

      boolean latchTerminationStatus = execLatch.await(5, TimeUnit.SECONDS);
      if (!latchTerminationStatus) {
        logger.info("Latch could not terminate within specified time");
      }
      logger.log(Level.INFO, "Exec Output: {0} ", out);

      execWatch.close();
    } catch (InterruptedException ie) {
      Thread.currentThread().interrupt();
      ie.printStackTrace();
    }
  }

  private static class MyPodExecListener implements ExecListener {

    @Override
    public void onOpen(Response response) {
      logger.info("Shell was opened");
    }

    @Override
    public void onFailure(Throwable throwable, Response response) {
      logger.info("Some error encountered");
      execLatch.countDown();
    }

    @Override
    public void onClose(int i, String s) {
      logger.info("Shell Closing");
      execLatch.countDown();
    }
  }
}
