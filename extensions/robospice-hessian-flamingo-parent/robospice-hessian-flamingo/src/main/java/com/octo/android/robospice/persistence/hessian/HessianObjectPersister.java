package com.octo.android.robospice.persistence.hessian;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import roboguice.util.temp.Ln;
import android.app.Application;

import com.octo.android.robospice.persistence.exception.CacheCreationException;
import com.octo.android.robospice.persistence.exception.CacheLoadingException;
import com.octo.android.robospice.persistence.exception.CacheSavingException;
import com.octo.android.robospice.persistence.file.InFileObjectPersister;

public class HessianObjectPersister<T> extends InFileObjectPersister<T> {

    // ============================================================================================
    // CONSTRUCTOR
    // ============================================================================================
    public HessianObjectPersister(Application application, Class<T> clazz, File cacheFolder)
        throws CacheCreationException {
        super(application, clazz, cacheFolder);
    }

    public HessianObjectPersister(Application application, Class<T> clazz) throws CacheCreationException {
        super(application, clazz);
    }

    // ============================================================================================
    // METHODS
    // ============================================================================================

    @SuppressWarnings("unchecked")
    @Override
    protected T readCacheDataFromFile(File file) throws CacheLoadingException {
        ObjectInputStream ois = null;
        try {
            synchronized (file.getAbsolutePath().intern()) {
                ois = new ObjectInputStream(new FileInputStream(file));
                return (T) ois.readObject();
            }
        } catch (FileNotFoundException e) {
            // Should not occur (we test before if file exists)
            // Do not throw, file is not cached
            Ln.w("file " + file.getAbsolutePath() + " does not exists", e);
            return null;
        } catch (Exception e) {
            throw new CacheLoadingException(e);
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    throw new CacheLoadingException(e);
                }
            }
        }
    }

    @Override
    public T saveDataToCacheAndReturnData(final T data, final Object cacheKey) throws CacheSavingException {

        try {
            if (isAsyncSaveEnabled()) {
                Thread t = new Thread() {
                    @Override
                    public void run() {
                        try {
                            saveData(data, cacheKey);
                        } catch (IOException e) {
                            Ln.e(e, "An error occured on saving request " + cacheKey + " data asynchronously");
                        } catch (CacheSavingException e) {
                            Ln.e(e, "An error occured on saving request " + cacheKey + " data asynchronously");
                        }
                    };
                };
                t.start();
            } else {
                saveData(data, cacheKey);
            }
        } catch (CacheSavingException e) {
            throw e;
        } catch (Exception e) {
            throw new CacheSavingException(e);
        }
        return data;
    }

    protected void saveData(T data, Object cacheKey) throws IOException, CacheSavingException {

        File cacheFile = getCacheFile(cacheKey);
        synchronized (cacheFile.getAbsolutePath().intern()) {
            ObjectOutputStream oos = null;
            try {
                oos = new ObjectOutputStream(new FileOutputStream(cacheFile));
                oos.writeObject(data);
            } finally {
                if (oos != null) {
                    oos.flush();
                    oos.close();
                }
            }
        }
    }
}
