package org.example;

import io.prometheus.client.Histogram;
import io.prometheus.client.Summary;
import io.prometheus.client.exporter.HTTPServer;

import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TXMonitor {
    // Create a metric to track transaction duration times
    private static boolean initialized;
    private static HTTPServer server;
    private static final Logger LOG = LogManager.getLogger(TXMonitor.class);

    private static final Histogram transactionDuration = Histogram.build()
            .name("transaction_duration_milliseconds")
            .help("Transaction duration in milliseconds")
            .labelNames("transaction_id")
            .register();

    public static void initialize()
    {
        try {
            server = new HTTPServer(8000);
            initialized=true;
        }
        catch(java.io.IOException e){
            e.printStackTrace();
        }
    }

    private static void processTransaction(String transactionID, long txDuration) {
        if (!initialized){
            LOG.error("the monitor was not initialized, I will do it for you");
            initialize();
        }
        if (initialized){
            transactionDuration.labels(transactionID).observe(txDuration);  // Record the duration
        }else{
            LOG.error("the monitor is still not initialized, houston we have a problem");
        }

        
    }
}
