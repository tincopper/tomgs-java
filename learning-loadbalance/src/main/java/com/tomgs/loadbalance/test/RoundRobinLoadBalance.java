package com.tomgs.loadbalance.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * 平滑加权轮询负载均衡实现
 * @author tangzhongyuan
 *
 */
public class RoundRobinLoadBalance {
	class Server {
		String name;
		int weight;
		int curWeight = 0;
		
		public Server(String name, int weight) {
			super();
			this.name = name;
			this.weight = weight;
		}
	}
	
	public List<Server> initServers(Map<String, Integer> serverMap) {
		List<Server> servers = new ArrayList<Server>();
		Iterator<Entry<String, Integer>> iterator = serverMap.entrySet().iterator();
		for (;iterator.hasNext();) {
			Entry<String, Integer> entry = iterator.next();
			servers.add(new Server(entry.getKey(), entry.getValue()));
		}
		return servers;
	}
	
	public Server doSelect(List<Server> servers) {
		//对权重进行求和
		int total = 0;
		Server selectServer = null;
		for (Server server : servers) {
			server.curWeight += server.weight;
			total += server.weight;
			if (selectServer == null || selectServer.curWeight < server.curWeight) {
				selectServer = server;
			}
		}
		for (Server server : servers) {
			if (server.equals(selectServer)) {
				server.curWeight -= total;
				break;
			}
		}
		return selectServer;
	}
	
	public static void main(String[] args) {
		RoundRobinLoadBalance loadBalance = new RoundRobinLoadBalance();
		Map<String, Integer> serverMap =  new TreeMap<String, Integer>();
		serverMap.put("host1", 1);
		serverMap.put("host2", 2);
		serverMap.put("host3", 3);
		serverMap.put("host4", 4);
		
		List<Server> servers = loadBalance.initServers(serverMap);
		System.out.printf("The server is : ");
		for (Server server : servers) {
			System.out.printf("%s(%d) ", server.name, server.weight);
		}
		System.out.println();
		
		for (int i = 1; i <= 100; i++) {
			Server server = loadBalance.doSelect(servers);
			System.out.println(i + " select server is " + server.name);
		}
	}
}
