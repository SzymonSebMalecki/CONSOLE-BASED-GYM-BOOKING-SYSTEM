import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;

public class ClientServer{
	private static Socket socket;
	static Scanner sc = new Scanner(System.in);
	public static void main(String[] args){
		try{
			socket = new Socket("localhost", 2000);
			System.out.println("Your Client Server is Up and Running");
			ApplicationProtocol.main(new String[0]); //THIS ALLOWS MAIN CLASS TO RUN
			 
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	}