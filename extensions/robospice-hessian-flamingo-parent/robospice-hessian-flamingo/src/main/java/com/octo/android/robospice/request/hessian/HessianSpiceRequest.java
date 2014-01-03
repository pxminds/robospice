package com.octo.android.robospice.request.hessian;

import roboguice.util.temp.Ln;

import com.caucho.hessian.client.HessianProxyFactory;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.SpiceRequest;

public abstract class HessianSpiceRequest<RESULT, SERVICE> extends SpiceRequest<RESULT> {

    private HessianProxyFactory hessianProxyFactory;

    public HessianSpiceRequest(Class<RESULT> clazz) {
        super(clazz);
    }

    public HessianProxyFactory getHessianProxyFactory() {
        return hessianProxyFactory;
    }

    public void setHessianProxyFactory(HessianProxyFactory hessianProxyFactory) {
        this.hessianProxyFactory = hessianProxyFactory;
    }

    @SuppressWarnings("unchecked")
    public SERVICE createService(Class<SERVICE> serviceInterface, String uri) throws SpiceException {
        try {
            return (SERVICE) hessianProxyFactory.create(serviceInterface, uri);
        } catch (Exception e) {
            Ln.e(e);
            throw new SpiceException("Could not create Hessian remote proxy.", e);
        }
    }

    @Override
    /**
     * This method doesn't really work within the Hessian Flamingo module : once the request is
     * loading data from network, there is no way to interrupt it. This is weakness of the hessian framework,
     * and seems to come from even deeper. The IO operations on which it relies don't support the interrupt flag
     * properly.
     * Nevertheless, there are still some opportunities to cancel the request, basically during cache operations.
     */
    public void cancel() {
        super.cancel();
        Ln.w(HessianSpiceRequest.class.getName(), "Cancel can't be invoked directly on "
                + HessianSpiceRequest.class.getName() + " requests. You must call SpiceManager.cancelAllRequests().");
    }
}
