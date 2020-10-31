package com.configuration;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.Set;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.Query;
import javax.management.QueryExp;
import javax.servlet.http.HttpServlet;

import com.global.Global;

public class ServerConfigation extends HttpServlet {

	private static final long serialVersionUID = 5706590732627679043L;
	private static boolean IPv4Process = false;
	private static boolean IPv6Process = false;

	private static String IPv4Scheme;
	private static String IPv6Scheme;

	private static String IPv4Host;
	private static String IPv6Host;

	private static String IPv4Port;
	private static String IPv6Port;

	public static void IPprocess(){
		try {
			MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
			QueryExp subQuery1 = Query.match(Query.attr("protocol"), Query.value("HTTP/1.1"));
			QueryExp subQuery2 = Query.anySubString(Query.attr("protocol"), Query.value("Http11"));
			QueryExp query = Query.or(subQuery1, subQuery2);
			Set<ObjectName> objs = mbs.queryNames(new ObjectName("*:type=Connector,*"), query);
			String hostname = InetAddress.getLocalHost().getHostName();
			InetAddress[] addresses = InetAddress.getAllByName(hostname);
			for (Iterator<ObjectName> i = objs.iterator(); i.hasNext();) {
				ObjectName obj = i.next();
				String scheme = mbs.getAttribute(obj, "scheme").toString();
				String port = obj.getKeyProperty("port");
				for (InetAddress addr : addresses) {
					if (addr.isAnyLocalAddress() || addr.isLoopbackAddress() || addr.isMulticastAddress()) {
						continue;
					}
					String host = addr.getHostAddress();
					if (!IPv4Process) {
						IPv4Scheme = scheme;
						IPv4Host = host;
						IPv4Port = port;
					} else if (!IPv6Process) {
						IPv6Scheme = scheme;
						IPv6Host = host;
						IPv6Port = port;
					}
				}
			}
		}catch(Exception e) {
			Global.getLogger.error(ServerConfigation.class.getName(), "", e);
		}
	}

	public static boolean isIPv4Process() {
		return IPv4Process;
	}

	public static boolean isIPv6Process() {
		return IPv6Process;
	}

	public static String getIPv4Scheme() {
		return IPv4Scheme;
	}

	public static String getIPv6Scheme() {
		return IPv6Scheme;
	}

	public static String getIPv4Host() {
		return IPv4Host;
	}

	public static String getIPv6Host() {
		return IPv6Host;
	}

	public static String getIPv4Port() {
		return IPv4Port;
	}

	public static String getIPv6Port() {
		return IPv6Port;
	}

}
