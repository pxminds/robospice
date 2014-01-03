package com.octo.android.robospice.hessian.test.stub;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import com.octo.android.robospice.hessian.test.model.json.Weather;
import com.octo.android.robospice.request.hessian.HessianSpiceRequest;

public class HessianSpiceRequestStub extends HessianSpiceRequest<Weather, HessianServiceStub> {
    private final ReentrantLock reentrantLock = new ReentrantLock();
    private final Condition loadDataFromNetworkHasBeenExecuted = reentrantLock.newCondition();

    public HessianSpiceRequestStub(Class<Weather> clazz) {
        super(clazz);
    }

    @Override
    public Weather loadDataFromNetwork() throws Exception {
        try {
            reentrantLock.lock();
            loadDataFromNetworkHasBeenExecuted.signal();
        } finally {
            reentrantLock.unlock();
        }
        return new Weather();
    }

    public void await(long timeout) throws InterruptedException {
        try {
            reentrantLock.lock();
            loadDataFromNetworkHasBeenExecuted.await(timeout, TimeUnit.MILLISECONDS);
        } finally {
            reentrantLock.unlock();
        }
    }

}
