/*
 * Created by Angel Leon (@gubatron), Alden Torres (aldenml)
 * Copyright (c) 2011-2016, FrostWire(R). All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.dkf.jed2k.android;

import android.content.Context;
import android.os.Build;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * @author gubatron
 * @author aldenml
 */
public final class AndroidPlatform implements Platform {

    private final FileSystem fileSystem;
    private final AndroidPaths systemPaths;
    private final AndroidSettings appSettings;

    private static final Logger LOG = LoggerFactory.getLogger(AndroidPlatform.class);

    private static final int VERSION_CODE_LOLLIPOP = 21;

    private final int sdk;

    public AndroidPlatform(Context app) {
        fileSystem = buildFileSystem(app);
        systemPaths = new AndroidPaths(app);
        appSettings = new AndroidSettings();
        this.sdk = Build.VERSION.SDK_INT;
    }

    @Override
    public FileSystem fileSystem() {
        return fileSystem;
    }

    @Override
    public AndroidPaths systemPaths() {
        return systemPaths;
    }

    @Override
    public AndroidSettings appSettings() {
        return appSettings;
    }

    public boolean android() {
        return true;
    }

    public int androidVersion() {
        return sdk;
    }

    public Platform.NetworkType networkType() {
        if (NetworkManager.instance().isDataMobileUp()) {
            return Platform.NetworkType.MOBILE;
        }

        if (NetworkManager.instance().isDataWIFIUp()) {
            return Platform.NetworkType.WIFI;
        }

        if (NetworkManager.instance().isDataWiMAXUp()) {
            return Platform.NetworkType.WIMAX;
        }

        return Platform.NetworkType.NONE;
    }

    public static boolean saf() {
        Platform p = Platforms.get();
        return p.fileSystem() instanceof LollipopFileSystem;
    }

    /**
     * This method determines if the file {@code f} is protected by
     * the SAF framework because it's in the real external SD card.
     *
     * @param f
     * @return
     */
    public static boolean saf(File f) {
        Platform p = Platforms.get();

        if (!(p.fileSystem() instanceof LollipopFileSystem)) {
            return false;
        }

        if (f.getPath().contains("/Android/data/com.frostwire.android/")) {
            // private file, FUSE give us standard POSIX operations
            return false;
        }

        LollipopFileSystem fs = (LollipopFileSystem) p.fileSystem();

        return fs.getExtSdCardFolder(f) != null;
    }

    private static FileSystem buildFileSystem(Context app) {
        FileSystem fs;

        if (Build.VERSION.SDK_INT >= VERSION_CODE_LOLLIPOP) {
            LollipopFileSystem lfs = new LollipopFileSystem(app);
            //LibTorrent.setPosixWrapper(new PosixCalls(lfs));
            fs = lfs;
        } else {
            fs = new DefaultFileSystem() {
                @Override
                public void scan(File file) {
                    //Librarian.instance().scan(file);
                }
            };
        }

        return fs;
    }
}
