package org.jingle.test.monitoringtomcat.client

import groovy.jmx.builder.JmxBuilder
import groovy.util.GroovyMBean

def jmx = new JmxBuilder()
def client = jmx.connectorClient(port: 9000)
client.connect()
client.getMBeanServerConnection()

