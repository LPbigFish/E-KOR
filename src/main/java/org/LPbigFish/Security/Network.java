package org.LPbigFish.Security;

import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.peers.Number160;

import java.security.SecureRandom;

public class Network {
    protected Peer peer;
        //https://tomp2p.net/doc/quick/
    public Network() {
        try {
            peer = new PeerMaker(new Number160(new SecureRandom())).setPorts(9291).makeAndListen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
