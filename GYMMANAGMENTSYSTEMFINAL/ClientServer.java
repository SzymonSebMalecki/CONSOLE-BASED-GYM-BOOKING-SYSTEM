import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;

/* ------------------------- CLIENT SERVER  --------------------------- */

public class ClientServer{
	private static Socket ClientSocket;
	public static void main(String[] args) throws Exception, SQLException{
	try{
			ClientSocket = new Socket("localhost", 2000);
			System.out.println("Your Client Server is Up and Running on:" + ClientSocket);
			ApplicationProtocol.main(new String[0]); //This method calls Main Method in Application Protocol class and it starts commandline managment system
			 
	} catch(Exception e) {
		System.out.println(e);
		}
	}
}

/* ------------------------- CLIENT SERVER  --------------------------- */