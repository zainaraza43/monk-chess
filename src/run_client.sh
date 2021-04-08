#!/bin/sh

javac com/net/Client.java &&\
	java com.net.Client &&\
	rm com/net/Client.class
