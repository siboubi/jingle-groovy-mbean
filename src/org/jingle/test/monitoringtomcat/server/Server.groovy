package org.jingle.test.monitoringtomcat.server

import javax.management.*
import com.sun.jdmk.comm.HtmlAdaptorServer
import groovy.jmx.builder.JmxBuilder
import java.rmi.registry.LocateRegistry


def server = MBeanServerFactory.createMBeanServer('test')
LocateRegistry.createRegistry(9000)
def jmx = new JmxBuilder(server)
jmx.connectorServer(port: 9000).start()

//def adapter = new HtmlAdaptorServer()
//def httpName = new ObjectName('system:name=http')
//server.registerMBean(adapter, httpName)
//adapter.setPort(9292)
//adapter.start()

// explicit
def ctrl = new RequestController(resource:"Hello World")
def objectNameToExportAllAttributesWithWildcard = new ObjectName("jmx.tutorial.export.all.attributes.with.wildcard:type=Object")
def objectNameToExportAttributeList = new ObjectName("jmx.tutorial.export.attribute.list:type=Object")
def objectNameToExportAttributeWithExplicitDescriptors = new ObjectName("jmx.tutorial.export.attribute.with.explicit.descriptors:type=Object")
def objectNameToExportAllConstructorsWithWildcard = new ObjectName("jmx.tutorial.export.all.constructors.with.wildcard:type=Object")
def objectNameToExportAllConstructorsUsingParameterDescriptor = new ObjectName("jmx.tutorial.export.all.constructors.using.parameter.descriptor:type=Object")
def objectNameToExportConstructorWithExplicitDescriptors = new ObjectName("jmx.tutorial.constructor.with.explicit.descriptors:type=Object")
def objectNameToExportAllOperationWithWildcard = new ObjectName("jmx.tutorial.export.all.operation.with.wildcard:type=Object")
def objectNameToExportOperationList = new ObjectName("jmx.tutorial.export.operation.list:type=Object")
def objectNameToExportOperationBySignature = new ObjectName("jmx.tutorial.export.operation.by.signature:type=Object")
def objectNameToExportOperationWithExplicitDescriptors = new ObjectName("jmx.tutorial.operation.with.explicit.descriptors:type=Object")
def adaptor = new HtmlAdaptorServer(port: 9292)
def beans = jmx.export(policy: "replace") {
	bean(target: adaptor, name:'system:name=http')
	bean(new Foo())
	bean(new Bar())
	bean(new SomeBar())
	bean(target:ctrl, name:"jmx.tutorial.specifying.mbean.objectname:type=Object")
	bean(target: new RequestController(),
		name: objectNameToExportAllAttributesWithWildcard,
		attributes: "*")
	bean(
		target: new RequestController(),
		name: objectNameToExportAttributeList,
		attributes: [ "Resource", "RequestCount" ]
	)
	bean(
		target: new RequestController(),
		name: objectNameToExportAttributeWithExplicitDescriptors,
		attributes: [
			"Resource":[desc: "The resource to request.", readable: true, writable: true, defaultValue:"Hello"],
			"RequestCount":"*"
		]
	)
	bean(
		target: new RequestController(),
		name: objectNameToExportAllConstructorsWithWildcard,
		constructors: "*"
	)
	bean(
		target: new RequestController(),
		name: objectNameToExportAllConstructorsUsingParameterDescriptor,
		constructors:[
			"RequestController":[ "Object" ]
		]
	)
	bean(target: new RequestController(), name: objectNameToExportConstructorWithExplicitDescriptors,
		constructors:[
			"RequestController":[
				desc:"Constructor takes param",
				params:[ "Object" : [name:"Resource", desc:"Resource for controller"] ]
			]
		]
	)
	bean(
		target: new RequestController(),
		name: objectNameToExportAllOperationWithWildcard,
		operations: "*"
	)
	bean(
		target: new RequestController(),
		name: objectNameToExportOperationList,
		operations: [ "start", "stop" ]
	)
	bean(
		target: new RequestController(),
		name: objectNameToExportOperationBySignature,
		operations: [
			"makeRequest":[ "String" ]
		]
	)
	bean(target: new RequestController(), name: objectNameToExportOperationWithExplicitDescriptors,
		operations: [
			"start": [desc:"Starts request controller"],
			"stop":[desc:"Stops the request controller"],
			"setResource":[params:[ "Object" ]],
			"makeRequest": [
				desc:"Executes the request.",
				params: [
					"String":[name:"Resource",desc:"The resource to request"]
				]
			]

		]
	)
}
adaptor.start()

