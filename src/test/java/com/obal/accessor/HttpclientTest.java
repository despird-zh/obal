package com.obal.accessor;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;

public class HttpclientTest {
	
	 public static void main(String[] args) {
		    
		    // Create an HttpClient with the MultiThreadedHttpConnectionManager.
		    // This connection manager must be used if more than one thread will
		    // be using the HttpClient.
		    HttpClient httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
		    // Set the default host/protocol for the methods to connect to.
		    // This value will only be used if the methods are not given an absolute URI
		    httpClient.getHostConfiguration().setHost("localhost", 8080, "http");
		    
		    // create an array of URIs to perform GETs on
		    String[] urisToGet = {
		        "/demo/mapredresult/",
		    };
		    
		    // create a thread for each URI
		    SendThread[] threads = new SendThread[urisToGet.length];
		    for (int i = 0; i < threads.length; i++) {
		        //GetMethod get = new GetMethod(urisToGet[i]);
		        //get.setFollowRedirects(true);
		        PostMethod post = new PostMethod(urisToGet[i]);
		        
		        threads[i] = new SendThread(httpClient, post, i + 1);
		    }
		    
		    // start the threads
		    for (int j = 0; j < threads.length; j++) {
		        threads[j].start();
		    }
		    
		}

		/**
		 * A thread that performs a GET.
		     */
		static class SendThread extends Thread {
		    
		    private HttpClient httpClient;
		    private PostMethod method;
		    private int id;
		    
		    public SendThread(HttpClient httpClient, PostMethod method, int id) {
		        this.httpClient = httpClient;
		        this.method = method;
		        this.id = id;
		    }
		    
		    /**
		      * Executes the GetMethod and prints some satus information.
		      */
		     public void run() {
		         
		         try {
		             
		             System.out.println(id + " - about to post something to " + method.getURI());
		             String repstr = "--DONE--";
		             int i =0;
		             while(i<5){
			             if("--DONE--".equals(repstr)){
			            	 
			            	 RequestEntity entity=new InputStreamRequestEntity(this.getClass().getClassLoader().getResourceAsStream("obal-config.properties"));
				             method.setRequestEntity(entity);
				             // execute the method
				             httpClient.executeMethod(method);
				             
				             System.out.println(id + " - get executed");
				             // get the response body as an array of bytes
				             byte[] bytes = method.getResponseBody();
				             repstr = new String(bytes);
				             
				             System.out.println(id + " - " + bytes.length + " bytes read");
				             System.out.println("repstr:"+repstr);
				             i++;
			             }
		             }
		         } catch (Exception e) {
		             System.out.println(id + " - error: " + e);
		         } finally {
		             // always release the connection after we're done 
		             method.releaseConnection();
		             System.out.println(id + " - connection released");
		         }
		     }
		    
		 }
}
