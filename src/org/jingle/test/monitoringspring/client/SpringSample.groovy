package org.jingle.test.monitoringspring.client

import org.springframework.context.support.ClassPathXmlApplicationContext
import java.lang.management.ManagementFactory
import javax.management.ObjectName
import javax.management.Attribute

// get normal bean
def ctx = new ClassPathXmlApplicationContext("beans.xml")
def calc = ctx.getBean("calcBean")

Thread.start{
	// access bean via JMX, use a separate thread just to
	// show that we could access remotely if we wanted
	def server = ManagementFactory.platformMBeanServer
	def mbean = new GroovyMBean(server, 'bean:name=calcMBean')
	sleep 1000
	assert 8 == mbean.add(7, 1)
	mbean.Base = 8
	assert '10' == mbean.addStrings('7', '1')
	mbean.Base = 16
	sleep 2000
	println "Number of invocations: $mbean.Invocations"
	println mbean
}

assert 15 == calc.add(9, 6)
assert '11' == calc.addStrings('10', '1')
sleep 2000
assert '20' == calc.addStrings('1f', '1')

