package csi403;


// Import required java libraries
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.json.*;
import java.util.PriorityQueue;

// Extend HttpServlet class
public class DiscernJsonService extends HttpServlet {

  // Standard servlet method 
    public void init() throws ServletException { 
        // Do any required initialization here - likely none
    }
    
    // Standard servlet method - we will handle a POST operation
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException { 
        doService(request, response); 
    }

    // Standard Servlet method
    public void destroy() { 
        // Do any required tear-down here, likely nothing.
    }

    // Standard servlet method - we will not respond to GET
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException { 
        // Set response content type and return an error message
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        // We can always create JSON by hand just concating a string.  
        out.println("{ 'message' : 'Use POST!'}");
    }
    
    // Our main worker method
	//Creates a PriorityQueue that responds to Json in the form
	//{"cmd": "enqueue", "name": "job1", "pri": 2} or {"cmd": "dequeue"}
    private void doService(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException { 
        // Get received JSON data from HTTP request
		//Handle any errors from erroneous input
		try {
			//Create priorityQueue to add input too
			PriorityQueue<PriorityQueueObject> priorityQueue = new PriorityQueue<PriorityQueueObject>();
			PrintWriter out = response.getWriter();
			BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
			
			//Using a string buffer to add all input into a single string
			StringBuffer bufferedJson = new StringBuffer();
			String line = null;
			//Add each line to bufferedJson until the BufferedReader reaches null
			while((line = br.readLine()) != null){
				bufferedJson.append(line);
			}
      
			//Convert the bufferedString to a regular String
			String jsonStr = bufferedJson.toString();
			
			// Create JsonReader object
			StringReader strReader = new StringReader(jsonStr);
			JsonReader reader = Json.createReader(strReader);
			// Get the singular JSON object ("inList" :[list of commands]) in this message.    
			JsonObject obj = reader.readObject();
			// From the object get the array named "inList"
			JsonArray inArray = obj.getJsonArray("inList");
		
			//Get each command as an object in the array and then process the command
			for(int i = 0; i < inArray.size(); i++) {
				JsonObject jsonObj = inArray.getJsonObject(i);
				//Any invalid input within the Json list gets skipped over (throws an error but error is handled)
				try {
					//get the cmd from the object
					String input = jsonObj.getString("cmd");
					//if the command is not null create a PriorityQueueObject
					if(input != null) {
						PriorityQueueObject object = new PriorityQueueObject(input);
						//test for dequeue first
						//if it is a dequeue command then dequeue
						if(object.testDequeue()) {
							priorityQueue.poll();
						} else {
							//else it must be an enqueue
							//So get name and pri fields
							String name = jsonObj.getString("name");
							int pri = jsonObj.getInt("pri");
							object.setName(name);
							object.setPri(pri);
							//if it is a valid enqueue then add. if not then do nothing
							if(object.testEnqueue()) {
								priorityQueue.add(object);
							}
						}
					}
				} catch (Exception e) {		
				
				}
			}
			
			//now all of commands have been processed so generate Json response
			response.setContentType("application/json");
			//If too many dequeue have occurred then an error is thrown 
			//return a message saying the priorityQueue is empty 
			try {
				//get first object from queue
				PriorityQueueObject pqTemp = priorityQueue.poll();
				//Create a string buffer to add all output too
				StringBuffer stringBuffer = new StringBuffer("{\"outList\" :[");
				String temp = pqTemp.toString();
				//first object in queue is formatted differently so use first to keep track
				//add the job to the string in priority order
				boolean first = true;
				while(temp != null) {
					if(first) {
						stringBuffer.append("\"" + temp + "\"");
						first = false;
					} else {
						stringBuffer.append(",\"" + temp + "\"");
					}	
					//get the next object
					pqTemp = priorityQueue.poll();
					if(pqTemp != null) {
						temp = pqTemp.toString();
						//temp = temp.replaceAll("^\"|\"$", "");
					} else {
						temp = null;
					}
				}
				stringBuffer.append("]}");
				out.println(stringBuffer.toString());
			} catch(Exception e) {
				response.setContentType("application/json");
				out.println("{ 'message' : 'PriorityQueue is empty'}");
			}
		} catch(Exception e) {
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			out.println("{ 'message' : 'Error: invalid input'}");
		}
			
			/* Different way of generating Json response
			JsonArrayBuilder outArrayBuilder = Json.createArrayBuilder();
			String temp = priorityQueue.poll().toString();
			temp = temp.replaceAll("^\"|\"$", "");
			out.println(temp);
			while(temp!= null) {
				outArrayBuilder.add(temp);
				PriorityQueueObject pqTemp = priorityQueue.poll();
				if(pqTemp != null) {
					temp = pqTemp.toString();
					temp = temp.replaceAll("^\"|\"$", "");
					out.println(temp);
				} else {
					temp = null;
				}
			}
			response.setContentType("application/json");
			String json = Json.createObjectBuilder()
				.add("outList", outArrayBuilder.build().toString())
				.build()
				.toString();
			out.println(json);*/
    }
    
    
}

