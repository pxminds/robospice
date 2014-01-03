package com.octo.android.robospice;

import java.util.Set;

import android.app.Application;

import com.caucho.hessian.client.HessianProxyFactory;
import com.octo.android.robospice.persistence.CacheManager;
import com.octo.android.robospice.persistence.exception.CacheCreationException;
import com.octo.android.robospice.persistence.hessian.HessianObjectPersisterFactory;
import com.octo.android.robospice.request.CachedSpiceRequest;
import com.octo.android.robospice.request.hessian.HessianSpiceRequest;
import com.octo.android.robospice.request.listener.RequestListener;

/**
 * This class offers a {@link SpiceService} that injects a
 * {@link HessianProxyFactory} from Flamingo into every
 * {@link HessianSpiceRequest} it has to execute.
 * 
 * @author Sebastian Harder
 */
public class HessianSpiceService extends SpiceService {

    private HessianProxyFactory proxyFactory;

    @Override
    public void onCreate() {
        super.onCreate();
        proxyFactory = new HessianProxyFactory();
        proxyFactory.setOverloadEnabled(true);
    }

    @Override
    public void addRequest(CachedSpiceRequest<?> request, Set<RequestListener<?>> listRequestListener) {
        if (request.getSpiceRequest() instanceof HessianSpiceRequest) {
            ((HessianSpiceRequest<?, ?>) request.getSpiceRequest()).setHessianProxyFactory(proxyFactory);
        }
        super.addRequest(request, listRequestListener);
    }

    @Override
    public CacheManager createCacheManager(Application application) throws CacheCreationException {
        CacheManager cacheManager = new CacheManager();
        cacheManager.addPersister(new HessianObjectPersisterFactory(application));
        return cacheManager;
    }

}
