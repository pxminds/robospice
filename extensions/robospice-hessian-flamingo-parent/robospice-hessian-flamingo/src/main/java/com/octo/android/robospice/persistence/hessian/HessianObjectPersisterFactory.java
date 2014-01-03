package com.octo.android.robospice.persistence.hessian;

import java.io.File;
import java.util.List;

import android.app.Application;

import com.octo.android.robospice.persistence.exception.CacheCreationException;
import com.octo.android.robospice.persistence.file.InFileObjectPersister;
import com.octo.android.robospice.persistence.file.InFileObjectPersisterFactory;

public class HessianObjectPersisterFactory extends InFileObjectPersisterFactory {

    // ----------------------------------
    // CONSTRUCTORS
    // ----------------------------------

    public HessianObjectPersisterFactory(Application application) throws CacheCreationException {
        super(application);
    }

    public HessianObjectPersisterFactory(Application application, List<Class<?>> listHandledClasses)
        throws CacheCreationException {
        super(application, listHandledClasses);
    }

    public HessianObjectPersisterFactory(Application application, File cacheFolder) throws CacheCreationException {
        super(application, cacheFolder);
    }

    public HessianObjectPersisterFactory(Application application, List<Class<?>> listHandledClasses, File cacheFolder)
        throws CacheCreationException {
        super(application, listHandledClasses, cacheFolder);
    }

    // ----------------------------------
    // API
    // ----------------------------------
    @Override
    public <DATA> InFileObjectPersister<DATA> createInFileObjectPersister(Class<DATA> clazz, File cacheFolder)
        throws CacheCreationException {
        return new HessianObjectPersister<DATA>(getApplication(), clazz, cacheFolder);
    }

}
