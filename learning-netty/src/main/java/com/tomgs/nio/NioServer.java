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
 * nio echo server
 *
 * @author tangzy
 */
public class NioServer {

  private static ByteBuffer echoBuffer = ByteBuffer.allocate(4);
  private static ByteBuffer sendBuffer = ByteBuffer.allocate(256);

  public static void start() throws IOException {
    Selector serverSelector = Selector.open();
    ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
    // 设置为非阻塞模式
    serverSocketChannel.configureBlocking(false);
    serverSocketChannel.bind(new InetSocketAddress("127.0.0.1", 8080));
    // 将channel注册到selector上去
    serverSocketChannel.register(serverSelector, SelectionKey.OP_ACCEPT);

    while (true) {
      serverSelector.select();
      Set<SelectionKey> selectionKeys = serverSelector.selectedKeys();
      System.out.println(selectionKeys.size());
      Iterator<SelectionKey> iterator = selectionKeys.iterator();
      while (iterator.hasNext()) {
        StringBuilder msg = new StringBuilder();
        SelectionKey selectionKey = iterator.next();
        if (selectionKey.isAcceptable()) {
          System.out.println("acceptable ....");
          ServerSocketChannel serverChannel = (ServerSocketChannel) selectionKey.channel();
          SocketChannel socketChannel = serverChannel .accept();
          socketChannel.configureBlocking(false);
          //注册可读通知
          socketChannel.register(serverSelector, SelectionKey.OP_READ);

          iterator.remove();
        }
        if (selectionKey.isReadable()) {
          SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
          System.out.println("readable " + socketChannel.hashCode());
          int code;
          while ((code = socketChannel.read(echoBuffer)) > 0) {
            byte b[] = new byte[echoBuffer.position()];
            echoBuffer.flip();
            echoBuffer.get(b);
            msg.append(new String(b, StandardCharsets.UTF_8));
          }
          //client关闭时，收到可读事件，code = -1
          if (code == -1 || msg.toString().toUpperCase().contains("BYE")) {
            socketChannel.close();
            break;
          } else {
            //code=0，消息读完或者echoBuffer空间不够时，部分消息内容下一次select后收到
            echoBuffer.clear();
          }
          System.out.println("msg: " + msg  + " from: " + socketChannel + "code:  " + code );

          //注册可写通知
          socketChannel.register(serverSelector, SelectionKey.OP_WRITE);

          iterator.remove();
        }
        if (selectionKey.isWritable()) {
          SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
          System.out.println("writable " + socketChannel.hashCode());

          sendBuffer.put("Message from server".getBytes());
          sendBuffer.flip();
          // 将数据写入通道
          while (sendBuffer.hasRemaining()) {
            socketChannel.write(sendBuffer);
          }
          //读完之后将其清除设置为可写
          sendBuffer.clear();
          System.out.println("Send message to client ");

          //在读通知里面注册为写事件，所以这里还需要注册为读，否则不在接受客户端消息
          socketChannel.register(serverSelector, SelectionKey.OP_READ);
          iterator.remove();
        }

      }
    }
  }

  public static void main(String[] args) throws IOException {
    start();
  }

}
