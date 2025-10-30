

import java.util.ArrayList;

public class TestLista {
	public static void main(String[] args) {
		// Crear ArrayList de Strings
		ArrayList<String> ListaDeCompras = new ArrayList<>();

		// Añadir 3 productos
		ListaDeCompras.add("leche");
		ListaDeCompras.add("pan");
		ListaDeCompras.add("huevos");

		// Imprimir tamaño (debería ser 3)
		System.out.println("Tamaño de la lista: " + ListaDeCompras.size());

		// Eliminar el segundo producto (índice 1 -> "pan")
		ListaDeCompras.remove(1);

		// Imprimir el producto en el índice 0
		System.out.println("Producto en índice 0: " + ListaDeCompras.get(0));

		// Recorrer la lista con for-each e imprimir los productos restantes
		System.out.println("Productos restantes:");
		for (String producto : ListaDeCompras) {
			System.out.println(producto);
		}
	}
}

