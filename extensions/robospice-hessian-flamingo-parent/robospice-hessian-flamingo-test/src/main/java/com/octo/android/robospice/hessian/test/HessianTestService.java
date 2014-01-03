package com.octo.android.robospice.hessian.test;

import java.io.File;

import android.app.Application;

import com.octo.android.robospice.HessianSpiceService;
import com.octo.android.robospice.persistence.CacheManager;
import com.octo.android.robospice.persistence.exception.CacheCreationException;
import com.octo.android.robospice.persistence.hessian.HessianObjectPersisterFactory;

public class HessianTestService extends HessianSpiceService {

    @Override
    public CacheManager createCacheManager(Application application) throws CacheCreationException {
        CacheManager cacheManager = new CacheManager();
        cacheManager.addPersister(new HessianObjectPersisterFactory(application, new File("/")));
        return cacheManager;
    }

}
