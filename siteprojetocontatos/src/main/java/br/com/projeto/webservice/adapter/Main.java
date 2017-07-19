package br.com.projeto.webservice.adapter;

import java.util.Locale;
import java.util.Scanner;

public class Main {
	
	public static void main(String[] args) {
		double pi = 3.14159;
		
		Scanner leitor = new Scanner(System.in);
		leitor.useLocale(Locale.ENGLISH);
		double raio = leitor.nextDouble();
		
		double area = pi*Math.pow(raio,2);
		
		System.out.println("A=" + Math.round(area * 100) / 100d);
	}

}
