package com.tomgs.scheduler.quartz.customer;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public class SerializeUtil<T> {
	public static final ObjectMapper mapper = new ObjectMapper();
		
	public static byte [] serialize(Object obj) throws JsonProcessingException{
		return mapper.writeValueAsBytes(obj);
	}
	
	public static <T> T deserialize(byte[] byteData, Class<T> cls) throws JsonParseException, JsonMappingException, IOException{
		return mapper.readValue(byteData, cls);
	}
}
