import java.util.*;
import java.lang.*;
import java.io.*;

public class TestInheritance {
	private static class Base {
		public void print() {
			System.out.println("Calling from base proper");
		}
	}
	
	private static class BaseImpl1 extends Base {
		public void print1() {
			System.out.println("Calling from base2");
		}
	}
	
	private static class BaseImpl2 extends Base {
		public void print2() {
			System.out.println("Calling from base1");
		}
	}
	
	public static void main (String[] args) {
		
		Base impl1 = new BaseImpl1();
		if (impl1 instanceof BaseImpl1) {
			System.out.println("Base impl1");
			impl1.print();
			((BaseImpl1) impl1).print1();
		}
		
		Base impl2 = new BaseImpl2();
		if (impl2 instanceof BaseImpl2) {
			System.out.println("Base impl2");
			impl2.print();
			((BaseImpl2) impl2).print2();
		}
	}
}