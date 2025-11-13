package no.oslomet.cs.algdat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.StringJoiner;

public class SøkeBinærTre<T>  implements Beholder<T> {

    // En del kode er ferdig implementert, hopp til linje 91 for Oppgave 1

    private static final class Node<T> { // En indre nodeklasse
        private T verdi; // Nodens verdi
        private Node<T> venstre, høyre, forelder; // barn og forelder

        // Konstruktører
        private Node(T verdi, Node<T> v, Node<T> h, Node<T> f) {
            this.verdi = verdi;
            venstre = v; høyre = h; forelder = f;
        }
        private Node(T verdi, Node<T> f) {
            this(verdi, null, null, f);
        }

        @Override
        public String toString() {return verdi.toString();}
    } // class Node

    private final class SBTIterator implements Iterator<T> {
        Node<T> neste;
        public SBTIterator() {
            neste = førstePostorden(rot);
        }

        public boolean hasNext() {
            return (neste != null);
        }

        public T next() {
            Node<T> denne = neste;
            neste = nestePostorden(denne);
            return denne.verdi;
        }
    }

    public Iterator<T> iterator() {
        return new SBTIterator();
    }

    private Node<T> rot;
    private int antall;
    private int endringer;

    private final Comparator<? super T> comp;

    public SøkeBinærTre(Comparator<? super T> c) {
        rot = null; antall = 0;
        comp = c;
    }

    public boolean inneholder(T verdi) {
        if (verdi == null) return false;

        Node<T> p = rot;
        while (p != null) {
            int cmp = comp.compare(verdi, p.verdi);
            if (cmp < 0) p = p.venstre;
            else if (cmp > 0) p = p.høyre;
            else return true;
        }
        return false;
    }

    public int antall() { return antall; }

    public String toStringPostOrder() {
        if (tom()) return "[]";

        StringJoiner s = new StringJoiner(", ", "[", "]");

        Node<T> p = førstePostorden(rot);
        while (p != null) {
            s.add(p.verdi.toString());
            p = nestePostorden(p);
        }

        return s.toString();
    }

    public boolean tom() { return antall == 0; }

    // Oppgave 1
    public boolean leggInn(T verdi) {
        if (verdi == null) throw new NullPointerException();
        Node<T> currentNode = rot, prevNode = null;
        int cmp = 0;
        while(currentNode != null){
            prevNode = currentNode;
            cmp = comp.compare(verdi, currentNode.verdi);
            currentNode = cmp < 0 ? currentNode.venstre : currentNode.høyre;
        }
        currentNode = new Node<>(verdi, null, null, prevNode);
        if(prevNode == null) rot = currentNode;
        else if(cmp < 0) prevNode.venstre = currentNode;
        else prevNode.høyre = currentNode;

        antall++;
        return true;
    }


    // Oppgave 2
    public int antall(T verdi){
        if(verdi == null)return 0;
        int antallDukketOpp = 0;
        Node<T> currentNode = rot;

        while (currentNode != null) {
            int cmp = comp.compare(verdi, currentNode.verdi);
            if (cmp < 0) currentNode = currentNode.venstre;
            else if (cmp > 0) currentNode = currentNode.høyre;
            else {
                antallDukketOpp++;
                currentNode = currentNode.høyre;
            }
        }
        return antallDukketOpp;
    }

    // Oppgave 3
    private Node<T> førstePostorden(Node<T> p) {
        Node<T> currentNode = p;
        while(true){
            if(currentNode.venstre != null) currentNode = currentNode.venstre;
            else if(currentNode.høyre != null) currentNode = currentNode.høyre;
            else return currentNode;
        }
    }

    private Node<T> nestePostorden(Node<T> p) {
        Node<T> f = p.forelder;
        if(f == null) {
            return null;
        }
        else if(f.høyre == p || f.høyre == null){
            return f;
        }else if(f.høyre != null){
            return førstePostorden(f.høyre);
        }else{
            return f;
        }
    }

    // Oppgave 4
    public void postOrden(Oppgave<? super T> oppgave) {
        Node<T> currentNode = førstePostorden(rot);
        while(currentNode != null){
            oppgave.utførOppgave(currentNode.verdi);
            currentNode = nestePostorden(currentNode);
        }
    }

    public void postOrdenRekursiv(Oppgave<? super T> oppgave) {
        postOrdenRekursiv(rot, oppgave); // Ferdig implementert
    }

    private void postOrdenRekursiv(Node<T> p, Oppgave<? super T> oppgave) {
        if(p != null){
            postOrdenRekursiv(p.venstre, oppgave);
            postOrdenRekursiv(p.høyre, oppgave);
            oppgave.utførOppgave(p.verdi);
        }
    }

    // Oppgave 5
    public boolean fjern(T verdi) { throw new UnsupportedOperationException(); }
    public int fjernAlle(T verdi) { throw new UnsupportedOperationException(); }
    public void nullstill() { throw new UnsupportedOperationException(); }
}