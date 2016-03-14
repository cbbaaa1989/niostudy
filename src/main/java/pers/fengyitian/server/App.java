package pers.fengyitian.server;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException
    {
       	new Server(8080).service();
    }
}
