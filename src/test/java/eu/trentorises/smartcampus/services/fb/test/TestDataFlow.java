package eu.trentorises.smartcampus.services.fb.test;

import it.sayservice.platform.core.common.exception.ServiceException;
import it.sayservice.platform.servicebus.test.DataFlowTestHelper;

import java.util.HashMap;
import java.util.Map;

import eu.trentorise.smartcampus.services.fb.events.impl.GetEventsDataFlow;

public class TestDataFlow {

	public static void main(String[] args) throws ServiceException {
		DataFlowTestHelper helper = new DataFlowTestHelper();
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
}
