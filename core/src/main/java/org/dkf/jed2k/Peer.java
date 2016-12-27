package org.dkf.jed2k;

import lombok.Data;
import lombok.ToString;
import org.dkf.jed2k.protocol.Endpoint;

/**
 * Created by inkpot on 04.07.2016.
 * information about peer
 */
@Data
@ToString
public class Peer implements Comparable<Peer> {
    private long    lastConnected   = 0;
    private long    nextConnection  = 0;
    private int     failCount       = 0;
    public boolean connectable     = false;
    private int     sourceFlag      = 0;
    private PeerConnection  connection = null;
    private final Endpoint        endpoint;

    public Peer(Endpoint ep) {
        assert ep != null;
        endpoint = ep;
    }

    public Peer(Endpoint ep, boolean conn, int sourceFlag) {
        assert ep != null;
        this.endpoint = ep;
        this.connectable = conn;
        this.sourceFlag = sourceFlag;
    }

    @Override
    public int compareTo(Peer o) {
        return endpoint.compareTo(o.endpoint);
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof Peer) {
            Peer other = (Peer)o;
            return endpoint.equals(other.endpoint);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return endpoint.hashCode();
    }

    boolean hasConnection() {
        return connection != null;
    }
}
