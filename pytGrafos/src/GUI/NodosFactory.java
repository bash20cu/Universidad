package GUI;

import java.util.ArrayList;

public class NodosFactory {

    private static final ArrayList<Nodos> listnodos = new ArrayList<>();

    public static Nodos addNodo(String idNodo, int x, int y) {
        Nodos n = new Nodos(idNodo, x, y);
        if (!IDexist(idNodo)) {
            listnodos.add(n);
            return n;
        } else {
            return null;
        }
    }

    public static Nodos addNodo(Nodos nodo) {
        if (!IDexist(nodo.getID())) {
            listnodos.add(nodo);
            return nodo;
        } else {
            return null;
        }
    }

    public static ArrayList<Nodos> getListaNodos() {
        return listnodos;
    }

    public static Nodos getNodo(String idNodo) {
        Nodos nodo = null;
        for (int n = 0; n < listnodos.size(); n++) {
            if (listnodos.get(n).getID().equals(idNodo)) {
                nodo = listnodos.get(n);
                break;
            }
        }
        return nodo;
    }

    public static boolean IDexist(String id) {
        return getNodo(id) != null;
    }

}
