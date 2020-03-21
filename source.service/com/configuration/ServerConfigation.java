package com.configuration;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.management.AttributeNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.Query;
import javax.management.QueryExp;

public class ServerConfigation {

	public static boolean IPv4Process = false;
	public static boolean IPv6Process = false;

	public static String IPv4Scheme;
	public static String IPv6Scheme;

	public static String IPv4Host;
	public static String IPv6Host;

	public static String IPv4Port;
	public static String IPv6Port;

	public static void IPprocess() throws Exception {
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
		} catch (Exception e) {
			throw e;
		}
	}
}
