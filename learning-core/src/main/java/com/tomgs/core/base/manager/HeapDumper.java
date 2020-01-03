package com.tomgs.core.base.manager;

import com.sun.management.HotSpotDiagnosticMXBean;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;
import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.rmi.ssl.SslRMIClientSocketFactory;

/**
 * @author tangzy
 */
public class HeapDumper {

  // This is the name of the HotSpot Diagnostic MBean
  private static final String HOTSPOT_BEAN_NAME =
      "com.sun.management:type=HotSpotDiagnostic";

  // field to store the hotspot diagnostic MBean
  private static volatile HotSpotDiagnosticMXBean hotspotMBean;

  private static void dumpHeap(String fileName, boolean live) {
    // initialize hotspot diagnostic MBean
    initHotspotMBean();
    try {
      hotspotMBean.dumpHeap(fileName, live);
    } catch (RuntimeException re) {
      throw re;
    } catch (Exception exp) {
      throw new RuntimeException(exp);
    }
  }

  // initialize the hotspot diagnostic MBean field
  private static void initHotspotMBean() {
    if (hotspotMBean == null) {
      synchronized (HeapDumper.class) {
        if (hotspotMBean == null) {
          //hotspotMBean = getHotspotMBean();
          hotspotMBean = getRemoteHostspotMBean("service:jmx:rmi:///jndi/rmi://172.20.57.49:6666/jmxrmi");
        }
      }
    }
  }

  // get the hotspot diagnostic MBean from the
  // platform MBean server
  private static HotSpotDiagnosticMXBean getHotspotMBean() {
    try {
      MBeanServer server = ManagementFactory.getPlatformMBeanServer();
      HotSpotDiagnosticMXBean bean = ManagementFactory
          .newPlatformMXBeanProxy(server, HOTSPOT_BEAN_NAME, HotSpotDiagnosticMXBean.class);
      return bean;
    } catch (RuntimeException re) {
      throw re;
    } catch (Exception exp) {
      throw new RuntimeException(exp);
    }
  }

  private static final SslRMIClientSocketFactory sslRMIClientSocketFactory =
      new SslRMIClientSocketFactory();

  private static HotSpotDiagnosticMXBean getRemoteHostspotMBean(String remoteAddress) {
    try {
      Map<String, Object> env = new HashMap();
      JMXServiceURL jmxUrl = new JMXServiceURL(remoteAddress);
      JMXConnector jmxc = JMXConnectorFactory.newJMXConnector(jmxUrl, null);
      jmxc.addConnectionNotificationListener(new ProxyClient(), null, null);
      try {
        jmxc.connect(env);
      } catch (IOException e) {
        // Likely a SSL-protected RMI registry
        if ("rmi".equals(jmxUrl.getProtocol())) { // NOI18N
          env.put("com.sun.jndi.rmi.factory.socket", sslRMIClientSocketFactory); // NOI18N
          jmxc.connect(env);
        } else {
          throw e;
        }
      }

      MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();
      // 生成动态代理
      //conn = Checker.newChecker(this, mbsc);
      HotSpotDiagnosticMXBean bean = ManagementFactory
          .newPlatformMXBeanProxy(mbsc, HOTSPOT_BEAN_NAME, HotSpotDiagnosticMXBean.class);
      return bean;
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception exp) {
      throw new RuntimeException(exp);
    }
  }

  private static class ProxyClient implements NotificationListener {

    @Override
    public void handleNotification(Notification notification, Object handback) {
      System.out.println("=========" + notification + "==========" + handback);
    }
  }

  public static void main(String[] args) {
    // default heap dump file name
    String fileName = "/data/dump/heap.hprof";
    // by default dump only the live objects
    boolean live = true;
    // simple command line options
    switch (args.length) {
      case 2:
        live = args[1].equals("true");
      case 1:
        fileName = args[0];
    }
    // dump the heap
    dumpHeap(fileName, live);
  }

}
