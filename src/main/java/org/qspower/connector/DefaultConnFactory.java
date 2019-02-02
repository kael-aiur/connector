package org.qspower.connector;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * @author kael.
 */
class DefaultConnFactory implements ConnectionFactory {

    @Override
    public Connection createConnection(String host, int port){
        SocketAddress address = InetSocketAddress.createUnresolved(host,port);
        Socket socket = new Socket();
        try {
            socket.bind(address);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return new SocketConnection(socket);
    }
}
