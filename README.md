JAGAL: Java Graph Library
=========================

### About

<img align="right" src="http://iig-uni-freiburg.github.io/images/tools/jagal.png">The Java Graph Library (JAGAL) is a Java library for modelling directed graphs. It comes with implementations of various types of graphs and transition systems, as well as utilities for their modification and traversal.

Its key features include among others:

* Implementation of directed graphs
* Implementation of directed weighted graphs
* Graph algorithms (Tarjan for SCCs)
* Graph visualization (Circle layout)
* Graph traversal (depth first, breadth first)
* Traversal utils (Predecessors, Siblings, Cycles, ...)
* Implementation of Transition Systems
* Implementation of Labelled Transition Systems

### Library Dependencies

* JAGAL builds upon the Java library TOVAL, which is a set of Java classes for common programming issues. It is located under [https://github.com/GerdHolz/TOVAL](https://github.com/GerdHolz/TOVAL "TOVAL: Tom's Java Library").
* For the visualization of graphs, JAGAL uses the [JGraphX library](https://github.com/jgraph/jgraphx "JGraphX"), which is a Java Swing diagramming library specialized on node-edge graphs.

### Documentation

A detailled documentation of JAGAL can be found under [http://doku.telematik.uni-freiburg.de/jagal](http://doku.telematik.uni-freiburg.de/jagal "http://doku.telematik.uni-freiburg.de/jagal").

### Latest Release

The most recent release is JAGAL 1.0.2, released January 22, 2016.

* [jagal-1.0.2.jar](https://github.com/iig-uni-freiburg/JAGAL/releases/download/v1.0.2/jagal-1.0.2.jar)
* [jagal-1.0.2-sources.jar](https://github.com/iig-uni-freiburg/JAGAL/releases/download/v1.0.2/jagal-1.0.2-sources.jar)
* [jagal-1.0.2-javadoc.jar](https://github.com/iig-uni-freiburg/JAGAL/releases/download/v1.0.2/jagal-1.0.2-javadoc.jar)

To add a dependency on JAGAL using Maven, use the following:

```xml
<dependency>
  <groupId>de.uni.freiburg.iig.telematik</groupId>
  <artifactId>JAGAL</artifactId>
  <version>1.0.2</version>
</dependency>
```

### Older Releases

Older releases can be found under [https://github.com/iig-uni-freiburg/JAGAL/releases](https://github.com/iig-uni-freiburg/JAGAL/releases).
