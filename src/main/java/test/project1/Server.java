package test.project1;

import java.awt.TextField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.netty.handler.codec.http.websocketx.ContinuationWebSocketFrame;
import io.vertx.core.*;
import io.vertx.core.http.*;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.*;
import io.vertx.ext.web.handler.BodyHandler;

import org.json.JSONArray;
import org.json.*;


public class Server extends AbstractVerticle {

	private Router router;
	List<Integer> intValues = new ArrayList<Integer>();
	List<String> stringValues = new ArrayList<String>();	



	


	@Override
	public void start(Future<Void> fut) throws Exception {
		

	    router = Router.router(vertx);
	    router.route().handler(BodyHandler.create());

	    router.post("/analyze").handler(this::analyze);

	    // Create Http server and pass the 'accept' method to the request handler
	    vertx.createHttpServer().requestHandler(router::accept).
	    
	            listen(config().getInteger("http.port", 8080),
	                    result -> {
	                        if (result.succeeded()) {
	                            System.out.println("Http server completed..");
	                            fut.complete();
	                        } else {
	                            fut.fail(result.cause());
	                            System.out.println("Http server failed..");
	                        }
	                    }
	            );
	}


	private void analyze(RoutingContext context) {
	    HttpServerResponse response = context.response();
	    String bodyAsString = context.getBodyAsString();
	    JsonObject body = context.getBodyAsJson();


	    if (body == null){
	        response.end("The Json body is null. Please recheck.." + System.lineSeparator());
	    }
	    else
	    {
	        String postedText = body.getString("text");
	        
	        String lexical = "";
            String value = getValue(postedText);
            if(value.equals("null"))
            	lexical = "null";
            else
             lexical = getLexical(postedText);

	        
	        response.setStatusCode(200);
	        response.putHeader("content-type", "text/html");
	        response.end("value :  " + value +"  lexical : " + lexical);
	    }

	}
	public String getLexical(String postedText){
		
		 int idx = Counter(postedText);

		 
		 return stringValues.get(idx).toString();
		
	}
	

	
	public int Counter(String postedText){
		
		List<Integer> counterLexical = new ArrayList<Integer>();
	    String[] parts = postedText.split("");	    
        
    	for(int j = 0; j < stringValues.size() - 1; j++){
    		String[] temp = stringValues.get(j).split("");
    		   int counter = 0;
  		       for(int k = 0; k < temp.length; k++ ){
       		 	    for(int i = 0 ; i < parts.length; i++){
       		 	    	 if(i==k){
       		 	    		if(temp[k].equals(parts[i])){
       		 	    			counter++;
       		 	    		}
       		 	    	}
       		 	    }

  			  
  		  }		    
  		     counterLexical.add(counter);

    	}
    	return counterLexical.indexOf(Collections.max(counterLexical));
	}

	
	
	
	
	public String getValue(String postedText){
		int sum = 0;
		List<String> abc = Arrays.asList("a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z");
		
	    String[] parts = postedText.split("");
	    
	    for(int i = 0 ; i < parts.length; i++){
	    	for(int j = 0 ; j < abc.size(); j++){
	    		if( parts[i].equals(abc.get(j)) == true)
	    			sum = sum + j + 1 ;
	    	}
	    }
        if(stringValues.isEmpty()){
            intValues.add(sum);
            stringValues.add(postedText);
        	return "null";
        }
        else{
    
        int getClose = nearestValue(sum, intValues);
        int getIndex = getIndex(getClose, intValues);
        intValues.add(sum);
        stringValues.add(postedText);
        
        return stringValues.get(getIndex).toString();
        }
	}
	


	
	
	public int nearestValue(int value, List<Integer>List)
	{
		int hefresh = Math.abs(value - List.get(0));
		int nearest = 0;
         for( int i = 1 ; i < List.size(); i++){
        	int temp =  Math.abs( value - List.get(i) );
        	if( hefresh > temp ){
        		nearest = List.get(i);
        		hefresh = temp;
        	}
         }
         
         return nearest;
	}
	


	
	public int getIndex(int value, List<Integer>List){
		int idx = 0;
    	for(int j = 0 ; j < List.size(); j++){
    		if(List.get(j) == value)
    			idx = j;
    	}
          return idx;
		
	}


	
}
