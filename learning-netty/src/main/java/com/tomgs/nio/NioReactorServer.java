package com.tomgs.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * nio 实现reactor模式
 *
 * @author tangzy
 */
public class NioReactorServer {

  public static void main(String[] args) throws IOException, InterruptedException {
    Selector serverSelector = Selector.open();
    Selector clientSelector = Selector.open();

    // main thread
    new Thread(() -> {
      try {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress("127.0.0.1", 8080));
        serverSocketChannel.register(serverSelector, SelectionKey.OP_ACCEPT);

        while (true) {
          serverSelector.select();
          Set<SelectionKey> selectionKeys = serverSelector.selectedKeys();
          Iterator<SelectionKey> iterator = selectionKeys.iterator();
          while (iterator.hasNext()) {
            SelectionKey selectionKey = iterator.next();
            if (selectionKey.isAcceptable()) {

              try {
                // 就是说把channel注册到selector上之后，以后后面获取从selectionKey中获取即可
                ServerSocketChannel serverChannel= (ServerSocketChannel) selectionKey.channel();
                SocketChannel socketChannel = serverChannel.accept();
                System.out.println("acceptable channel " + socketChannel + "@" + socketChannel.hashCode());

                // 获取的channel将其注册到clientSelector上去，避免开线程
                socketChannel.configureBlocking(false);
                socketChannel.register(clientSelector, SelectionKey.OP_READ);
              } finally {
                iterator.remove();
              }

            }
          }
        }

      } catch (IOException e) {
        e.printStackTrace();
      }

    }, "boss-reactor").start();

    Thread.sleep(300);

    // work线程主要用来进行处理连接工作
    new Thread(() -> {
      while (true) {
        try {
          // 这里使用select(1)避免一直阻塞，出现接受不到读请求
          if (clientSelector.select(1) > 0) {
            Set<SelectionKey> selectionKeys = clientSelector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
              SelectionKey selectionKey = iterator.next();
              StringBuilder msg = new StringBuilder();
              if (selectionKey.isReadable()) {
                try {
                  SocketChannel channel = (SocketChannel)selectionKey.channel();
                  System.out.println("readable channel " + channel + "@" + channel.hashCode());

                  ByteBuffer recveBuffer = ByteBuffer.allocate(128);
                  int code;
                  while ((code = channel.read(recveBuffer)) > 0) {
                    byte b[] = new byte[recveBuffer.position()];
                    recveBuffer.flip();
                    recveBuffer.get(b);
                    msg.append((new String(b, StandardCharsets.UTF_8)));
                  }
                  selectionKey.interestOps(SelectionKey.OP_READ);

                  System.out.println(msg.toString());
                  if (code == -1 || msg.equals("BYE")) {
                    channel.close();
                    break;
                  }
                } finally {
                  iterator.remove();
                }
              }
            }
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }, "work-reactor").start();

  }

}
