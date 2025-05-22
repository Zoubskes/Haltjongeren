package com.example.deelproduct21.model;

public abstract class Persoon {

    protected String haltjongere;

    public void setHaltjongere(String haltjongere) {
        this.haltjongere = haltjongere;
    }

    public String getHaltjongere() {
        return haltjongere;
    }
    @Override
    public String toString() {
        return "Persoon " + haltjongere + " ";
    }
}
