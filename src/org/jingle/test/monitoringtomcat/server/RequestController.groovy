package org.jingle.test.monitoringtomcat.server

public class RequestController {
    // attributes
    boolean started
    int requestCount
    int resourceCount
    int requestLimit
	String resource
    Map resources
	
	// constructors
	public RequestCopntroller(){}
	public RequestController(Object resource){}

    // operations
    void start(){  }
    void stop(){  }
    void putResource(String name, Object resource){  }
    void makeRequest(String res) {  }
    void makeRequest() {  }
	void setResource(Object name){}
//
//    static descriptor = [
//        name: "jmx.builder:type=EmbeddedObject",
//        operations: [ "start", "stop", "putResource" ]
//        attributes:"*"
//    ]
}