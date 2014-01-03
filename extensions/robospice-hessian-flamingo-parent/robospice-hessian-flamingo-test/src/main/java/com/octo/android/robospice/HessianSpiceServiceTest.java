package com.octo.android.robospice;

import android.test.ServiceTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.octo.android.robospice.hessian.test.HessianTestService;
import com.octo.android.robospice.hessian.test.model.json.Weather;
import com.octo.android.robospice.hessian.test.stub.HessianSpiceRequestStub;
import com.octo.android.robospice.hessian.test.stub.RequestListenerStub;

//Thanks to http://stackoverflow.com/questions/2300029/servicetestcaset-getservice
@SmallTest
public class HessianSpiceServiceTest extends ServiceTestCase<HessianTestService> {

    private static final int REQUEST_COMPLETION_TIMEOUT = 1000;
    private SpiceManager spiceManager;

    public HessianSpiceServiceTest() {
        super(HessianTestService.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        spiceManager = new SpiceManager(HessianTestService.class);
    }

    @Override
    protected void tearDown() throws Exception {
        shutdownService();
        if (spiceManager.isStarted()) {
            spiceManager.shouldStopAndJoin(REQUEST_COMPLETION_TIMEOUT);
        }
        super.tearDown();
    }

    public void test_addRequest_injects_request_factory() throws InterruptedException {
        // given
        spiceManager.start(getContext());
        HessianSpiceRequestStub hessianSpiceRequest = new HessianSpiceRequestStub(Weather.class);

        // when
        spiceManager.execute(hessianSpiceRequest, new RequestListenerStub<Weather>());
        hessianSpiceRequest.await(REQUEST_COMPLETION_TIMEOUT);

        // test
        assertNotNull(hessianSpiceRequest.getHessianProxyFactory());
    }
}
