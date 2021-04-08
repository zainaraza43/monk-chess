#!/bin/sh

javac com/net/Server.java &&\
	java com.net.Server &&\
	rm com/net/*.class
