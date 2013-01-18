package org.jingle.test.monitoringtomcat.client

import javax.management.ObjectName
import javax.management.remote.JMXConnectorFactory as JmxFactory
import javax.management.remote.JMXServiceURL as JmxUrl
import org.jfree.chart.ChartFactory
import org.jfree.data.category.DefaultCategoryDataset as Dataset
import org.jfree.chart.plot.PlotOrientation as Orientation
import groovy.swing.SwingBuilder
import javax.swing.WindowConstants as WC
import com.sun.jdmk.comm.HtmlAdaptorServer


//def serverUrl = 'service:jmx:rmi:///jndi/rmi://localhost:9004/jmxrmi'
def serverUrl = 'service:jmx:rmi:///jndi/rmi://10.158.16.101:9004/jmxrmi'
def server = JmxFactory.connect(new JmxUrl(serverUrl)).MBeanServerConnection
def serverInfo = new GroovyMBean(server, 'Catalina:type=Server').serverInfo
println "Connected to: $serverInfo"

def query = new ObjectName('Catalina:*')
String[] allNames = server.queryNames(query, null)
def modules = allNames.findAll{ name ->
    name.contains('j2eeType=WebModule')
}.collect{ new GroovyMBean(server, it) }

// Print out on the server log
println "Found ${modules.size()} web modules. Processing ..."
def dataset = new Dataset()

modules.each{ m ->
    println m.name()
    dataset.addValue m.processingTime, 0, m.path
}


// Swing application
def labels = ['Time per Module', 'Module', 'Time']
def options = [false, true, true]
def chart = ChartFactory.createBarChart(*labels, dataset,
                Orientation.VERTICAL, *options)
def swing = new SwingBuilder()
def frame = swing.frame(title:'Catalina Module Processing Time',
        defaultCloseOperation:WC.EXIT_ON_CLOSE) {
    panel(id:'canvas') { rigidArea(width:600, height:250) }
}
frame.pack()
frame.show()
chart.draw(swing.canvas.graphics, swing.canvas.bounds)