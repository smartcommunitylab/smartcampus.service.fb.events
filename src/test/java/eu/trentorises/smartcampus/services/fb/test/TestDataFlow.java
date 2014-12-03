package eu.trentorises.smartcampus.services.fb.test;

import it.sayservice.platform.client.InvocationException;
import it.sayservice.platform.client.ServiceBusClient;
import it.sayservice.platform.client.jms.JMSServiceBusClient;
import it.sayservice.platform.core.common.exception.ServiceException;
import it.sayservice.platform.core.message.Core.ActionInvokeParameters;
import it.sayservice.platform.servicebus.test.DataFlowTestHelper;

import java.util.HashMap;
import java.util.Map;

import javax.jms.ConnectionFactory;

import junit.framework.TestCase;

import org.apache.activemq.ActiveMQConnectionFactory;

import eu.trentorise.smartcampus.services.fb.events.impl.GetEventsDataFlow;

public class TestDataFlow extends TestCase {

	public void testDataFlow() throws ServiceException {
		DataFlowTestHelper helper = new DataFlowTestHelper("test");
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("source", "280474468734649");
		map.put("token", "");
		map.put("overrideLocation", ".+=Osteria La Scaletta");
		Map<String, Object> out = helper.executeDataFlow(
				"eu.trentorise.smartcampus.services.fb.events.FacebookEvents", 
				"GetEvents", 
				new GetEventsDataFlow(), 
				map);
		System.err.println(out);

	}
	
	public void testRemote() throws InvocationException {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("source", "280474468734649");
		map.put("token", "");
		map.put("overrideLocation", ".+=Osteria La Scaletta");

		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
		ServiceBusClient client = new JMSServiceBusClient(connectionFactory);
		
		ActionInvokeParameters invokeService = client.invokeService("eu.trentorise.smartcampus.services.fb.events.FacebookEvents", 
				"GetEvents", map);
		System.err.println(invokeService.getDataCount());
	}

}
