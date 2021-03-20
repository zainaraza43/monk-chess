
Chapter 31. A Networked Two-Person Game

From:
  Killer Game Programming in Java
  Andrew Davison
  O'Reilly, May 2005
  ISBN: 0-596-00730-2
  http://www.oreilly.com/catalog/killergame/
  Web Site for the book: http://fivedots.coe.psu.ac.th/~ad/jg

Contact Address:
  Dr. Andrew Davison
  Dept. of Computer Engineering
  Prince of Songkla University
  Hat Yai, Songkhla 90112, Thailand
  E-mail: ad@fivedots.coe.psu.ac.th

If you use this code, please mention my name, and include a link
to the book's Web site.

Thanks,
  Andrew

---------

This chapter discusses two versions of the FourByFour game:

FourByFour:	the basic two-person game, no networking;
                stored in FourByFour/

NetFourByFour:	the two-person game using threaded 
		TCP Clients and Server;
                stored in NetFourByFour/  (this directory)

--------
Compilation:

$ javac *.java

You must have Java 3D installed for the compilation to succeed;
Java 3D is available from http://java.sun.com/products/java-media/3D/

-----
Execution:

The code is set up to run on the same machine (i.e. the server's 
address is localhost).

Start the FBF server:

$ Java FBFServer

Start some FBF clients:

$ java NetFourByFour
$ java NetFourByFour
   // in separate DOS windows

---------
Last updated: 20th April 2005
