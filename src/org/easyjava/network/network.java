package org.easyjava.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

public class network {
		public BufferedReader Get(String url) {
			BufferedReader in =null;
			try {
				URL realUrl = new URL(url);
				URLConnection connection = realUrl.openConnection();
				connection.setRequestProperty("accept", "*/*");
				connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
				connection.connect();
				Map<String, List<String>> map = connection.getHeaderFields();
				in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			} catch (MalformedURLException e) {
				System.err.println("错误的网络地址");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.err.println("网络连接失败");
			}
			return in;
		}
}
