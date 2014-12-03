/*******************************************************************************
 * Copyright 2012-2013 Trento RISE
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 * http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package eu.trentorise.smartcampus.services.fb.events.scripts;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.protobuf.Message;

import eu.trentorise.smartcampus.services.fb.events.data.message.Events.Event;

public class FBEventsScript {

	private static final String DATE_FORMAT_LONG = "yyyy-MM-dd'T'HH:mm:ssZ";
	private static final String DATE_FORMAT_SHORT = "yyyy-MM-dd";

	public List<String> extractId(String events) throws Exception {
		List<String> result = new ArrayList<String>();
		ObjectMapper m = new ObjectMapper();
		JsonNode rootNode;
		rootNode = m.readValue(events, JsonNode.class);
		ArrayNode arrayNode = (ArrayNode) rootNode.get("data");
		Iterator<JsonNode> elements = arrayNode.elements();
		while (elements.hasNext()) {
			JsonNode node = elements.next();
			result.add(node.path("id").textValue());
		}
		
		return result;
	}

	public Message getEvent(String events, String override) throws Exception {
		Event.Builder builder = Event.newBuilder();
		ObjectMapper m = new ObjectMapper();
		JsonNode node;
		node = m.readValue(events, JsonNode.class);
		builder.setId(node.path("id").textValue());
		builder.setName(node.path("name").textValue());
		String location = "";
		if (node.has("location")) {
				location = replaceLocation(node.path("location").textValue(), override);
		}
		builder.setLocation(location);
		if (node.has("description")) {
			builder.setDescription(cleanString(node.path("description").textValue()));
		}
		boolean dateOnly = node.path("is_date_only").booleanValue();
		builder.setStartTime(convertTime(node.path("start_time").textValue(), dateOnly));
		if (node.has("end_time")) {
			builder.setEndTime(convertTime(node.path("end_time").textValue(), dateOnly));
		}
		builder.setOwner(node.get("owner").path("name").textValue());
		return builder.build();
	}

	private long convertTime(String time, boolean dateOnly) throws DatatypeConfigurationException, ParseException {
		if (dateOnly) {
			return new SimpleDateFormat(DATE_FORMAT_SHORT).parse(time).getTime();
		} else {
			return new SimpleDateFormat(DATE_FORMAT_LONG).parse(time).getTime();
		}
	}	
	
	private String cleanString(String s) {
		return s;
	}
	
	private String replaceLocation(String location, String override) {
		if (override.length() == 0) {
			return location;
		}
		String tmpLocation = location;
		String exprs[] = override.split(",");
		for (String expr : exprs) {
			String lr[] = expr.split("=");
			if (lr.length == 1) {
				tmpLocation = lr[0];
			} else {
				tmpLocation = tmpLocation.replaceAll(lr[0], lr[1]);
			}
		}

		return tmpLocation;
	}
	
}
