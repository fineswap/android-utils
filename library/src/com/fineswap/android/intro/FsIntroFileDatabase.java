/**
 * IntroOverlay Utility - Part of Fineswap Android Utilities.
 * Copyright (C) 2014 Fineswap Blog & App. All rights reserved.
 * http://fineswap.com/
 *
 * Released under the MIT license
 * http://en.wikipedia.org/wiki/MIT_License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package com.fineswap.android.intro;

import com.fineswap.android.utils.FsSystem;
import com.fineswap.android.utils.FsVersion;
import java.io.File;

/**
 * A persistent file-based database backend to use with {@link FsIntroOverlay}.
 *
 * @author Noor Dawod
 * @since 1.0
 */
public class FsIntroFileDatabase
  implements
    com.fineswap.android.aux.FsException,
    FsIntroFactory.PersistentDatabase {

  /**
   * Storage directory to keep statistics.
   *
   * @since 1.0
   */
  private final File databaseDir;

  /**
   * Instantiates a new persistent database backend using files. If the
   * specified directory doesn't exist, it will be created. If the directory
   * cannot be created, or its value is null, an exception is raised.
   *
   * @param databaseDir Destination directory to keep statistics
   * @since 1.0
   */
  public FsIntroFileDatabase(File databaseDir) {
    if(null == databaseDir) {
      throw EXCEPTION_NULL_OR_INVALID;
    }
    if(databaseDir.isFile()) {
      databaseDir.delete();
    }
    if(!databaseDir.isDirectory() && !databaseDir.mkdirs()) {
      throw EXCEPTION_NULL_OR_INVALID;
    }
    this.databaseDir = databaseDir;
  }

  /**
   * @param introOverlay {@link FsIntroFactory.PersistentDatabase#isNewIntro(FsIntroOverlay)}
   * @return {@link FsIntroFactory.PersistentDatabase#isNewIntro(FsIntroOverlay)}
   * @since 1.0
   */
  public boolean isNewIntro(FsIntroOverlay introOverlay) {
    return isNewDirectory(getIntroOverlayDatabaseDir(introOverlay));
  }

  /**
   * @param introOverlay {@link FsIntroFactory.PersistentDatabase#registerIntro(FsIntroOverlay)}
   * @since 1.0
   */
  public void registerIntro(FsIntroOverlay introOverlay) {
    FsSystem.mkdirs(getIntroOverlayDatabaseDir(introOverlay));
  }

  /**
   * @param introOverlay {@link FsIntroFactory.PersistentDatabase#unregisterIntro(FsIntroOverlay)}
   * @since 1.0
   */
  public void unregisterIntro(FsIntroOverlay introOverlay) {
    FsSystem.rmdir(getIntroOverlayDatabaseDir(introOverlay));
  }

  /**
   * @param slideResource {@link FsIntroFactory.PersistentDatabase#isNewSlide(FsSlideResource)}
   * @return {@link FsIntroFactory.PersistentDatabase#isNewSlide(FsSlideResource)}
   * @since 1.0
   */
  public boolean isNewSlide(FsSlideResource slideResource) {
    return isNewDirectory(getSlideDatabaseDir(slideResource));
  }

  /**
   * @param slideResource {@link FsIntroFactory.PersistentDatabase#registerSlide(FsSlideResource)}
   * @since 1.0
   */
  public void registerSlide(FsSlideResource slideResource) {
    FsSystem.mkdirs(getSlideDatabaseDir(slideResource));
  }

  /**
   * @param slideResource {@link FsIntroFactory.PersistentDatabase#unregisterSlide(FsSlideResource)}
   * @since 1.0
   */
  public void unregisterSlide(FsSlideResource slideResource) {
    FsSystem.rmdir(getSlideDatabaseDir(slideResource));
  }

  /**
   * @param slideResource {@link FsIntroFactory.PersistentDatabase#isNewRing(FsSlideResource, FsRingResource)}
   * @param ringResource {@link FsIntroFactory.PersistentDatabase#isNewRing(FsSlideResource, FsRingResource)}
   * @return {@link FsIntroFactory.PersistentDatabase#isNewRing(FsSlideResource, FsRingResource)}
   * @since 1.0
   */
  public boolean isNewRing(FsSlideResource slideResource, FsRingResource ringResource) {
    File slideDatabaseDir = getSlideDatabaseDir(slideResource);
    if(null != slideDatabaseDir && slideDatabaseDir.isDirectory()) {
      FsVersion version = ringResource.getVersion();
      byte[] versionFileContents = FsSystem.readFile(new File(slideDatabaseDir, version.classId));
      if(null != versionFileContents && 0 < versionFileContents.length) {
        return version.isNewerThan(new String(versionFileContents));
      }
    }
    return true;
  }

  /**
   * @param slideResource {@link FsIntroFactory.PersistentDatabase#registerRing(FsSlideResource, FsRingResource)}
   * @param ringResource {@link FsIntroFactory.PersistentDatabase#registerRing(FsSlideResource, FsRingResource)}
   * @since 1.0
   */
  public void registerRing(FsSlideResource slideResource, FsRingResource ringResource) {
    File slideDatabaseDir = getSlideDatabaseDir(slideResource);
    if(null != slideDatabaseDir) {
      slideDatabaseDir.mkdirs();
      FsVersion version = ringResource.getVersion();
      File versionFile = new File(slideDatabaseDir, version.classId);
      FsSystem.writeFile(versionFile, version.getFullVersion().getBytes());
    }
  }

  /**
   * @param slideResource {@link FsIntroFactory.PersistentDatabase#unregisterRing(FsSlideResource, FsRingResource)}
   * @param ringResource {@link FsIntroFactory.PersistentDatabase#unregisterRing(FsSlideResource, FsRingResource)}
   * @since 1.0
   */
  public void unregisterRing(FsSlideResource slideResource, FsRingResource ringResource) {
    File slideDatabaseDir = getSlideDatabaseDir(slideResource);
    if(null != slideDatabaseDir && slideDatabaseDir.isDirectory()) {
      (new File(slideDatabaseDir, ringResource.getVersion().classId)).delete();
    }
  }

  private File getIntroOverlayDatabaseDir(FsIntroOverlay introOverlay) {
    return getResourceDir(introOverlay);
  }

  private File getSlideDatabaseDir(FsSlideResource slideResource) {
    // Get path to intro overlay's cache directory, if known.
    File introOverlayDatabasePath = getIntroOverlayDatabaseDir(slideResource.getIntroOverlay());

    // If the intro overlay instance is known, use it.
    File slidePath = null == introOverlayDatabasePath
      ? new File(databaseDir, slideResource.toString())
      : new File(introOverlayDatabasePath, slideResource.toString());

    return normalizeDatabaseDir(slidePath);
  }

  private File getResourceDir(Object resource) {
    return null == resource
      ? null
      : normalizeDatabaseDir(new File(databaseDir, resource.toString()));
  }

  private File normalizeDatabaseDir(File dir) {
    if(dir.isFile()) {
      if(!dir.delete()) {
        return null;
      }
    }
    return dir;
  }

  private boolean isNewDirectory(File dir) {
    return null == dir || !dir.isDirectory();
  }

}
