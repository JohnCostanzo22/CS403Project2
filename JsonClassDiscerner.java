package csi403; 

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.Serializable;
import java.util.List; 

/*
*This class is unused in project. Can be used as an alternative method of receiving
*the Json 
*/

public class JsonClassDiscerner {

    public JsonClassDiscerner() {
    }

    public String discern(String jsonStr) {
        ObjectMapper mapper = new ObjectMapper();
        // mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		
        try {
            PriorityQueueObject object = mapper.readValue(jsonStr, PriorityQueueObject.class);
            return "enqueue"; 
        }
        catch (Exception e) {
            //return(e.toString()); 
        }
		return "<Incorrect Object>"; 
    }
}
