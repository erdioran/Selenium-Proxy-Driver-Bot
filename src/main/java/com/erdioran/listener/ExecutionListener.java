package com.erdioran.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IExecutionListener;

public class ExecutionListener  implements IExecutionListener {

    private static final Logger LOGGER = LogManager.getLogger(ExecutionListener.class);

    @Override
    public void onExecutionStart() {
        LOGGER.info("Start of automated test execution");
    }

    @Override
    public void onExecutionFinish() {
        LOGGER.info("End of automated test execution");
    }
}
