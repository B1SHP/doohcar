package bps.doohcar.enums;

public enum TipoUrl {

    PROPAGANDA(1),
    LOCAL(2),
    EVENTO(3);

    public int key;

    private TipoUrl(int key){

        this.key = key;

    }
    
}
