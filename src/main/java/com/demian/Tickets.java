package com.demian;

import java.util.concurrent.CountDownLatch;

public class Tickets {
    private final CountDownLatch cdlConnection = new CountDownLatch(1);

    public void initConnection() {
        if (cdlConnection.getCount() > 0) {
            // Here the code to establish the connection
            cdlConnection.countDown();
        }
    }

    public void processTicket(int ticket) throws InterruptedException {
        cdlConnection.await();
        // Here run all the queries once the connection is done
    }
}
