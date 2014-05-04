/**
 * System-related Utils - Part of Fineswap Android Utilities.
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

package com.fineswap.android.utils;

import android.content.Context;
import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * A collection of helpful, static functions for dealing with various system
 * situations.
 *
 * @author Noor Dawod
 * @since 1.0
 */
public class FsSystem {

  /**
   * Compute the SHA-1 digest hash for the specified
   * {@link java.lang.StringBuilder} instance.
   *
   * @param sb {@link java.lang.StringBuilder} instance to compute
   * @return Computed SHA-1 digest hash, or null on error
   * @since 1.0
   */
  public static String getSha1Digest(StringBuilder sb) {
    return getSha1Digest(sb.toString());
  }

  /**
   * Compute the SHA-1 digest hash for the specified string.
   *
   * @param sourceString Source string to compute
   * @return Computed SHA-1 digest hash, or null on error
   * @since 1.0
   */
  public static String getSha1Digest(String sourceString) {
    return getSha1Digest(sourceString.getBytes());
  }

  /**
   * Compute the SHA-1 digest hash for the specified byte-array.
   *
   * @param sourceBytes Source byte-array to compute
   * @return Computed SHA-1 digest hash, or null on error
   * @since 1.0
   */
  public static String getSha1Digest(byte[] sourceBytes) {
    try {
      MessageDigest hasher = MessageDigest.getInstance("SHA-1");
      hasher.update(sourceBytes, 0, sourceBytes.length);
      sourceBytes = hasher.digest();
      StringBuilder sb = new StringBuilder();
      for(byte oneByte : sourceBytes) {
        sb.append(String.format("%02X", oneByte));
      }
      return sb.toString();
    } catch(NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch(NullPointerException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Get the cache directory for the specified context.
   * The directory will be created when necessary.
   *
   * @param ctx App context
   * @return Cache directory for the specified context, or null on error
   * @since 1.0
   */
  public static File getCacheDir(Context ctx) {
    File cacheDir = ctx.getCacheDir();
    if(null != cacheDir) {
      if(cacheDir.isFile()) {
        cacheDir.delete();
      } else if(!cacheDir.exists()) {
        cacheDir.mkdirs();
      }
    }
    return cacheDir;
  }

  /**
   * Get a cache directory corresponding to the specified resource version.
   * The directory is created inside context's cache directory and its name is
   * based on the resource's name and version. If the directory cannot be
   * created in the cache directory, it will be created in the data directory.
   *
   * @see FsVersion
   * @param ctx App context
   * @param resource Resource version to create a corresponding directory for
   * @return Cache directory for the specified resource, or null on error
   * @since 1.0
   */
  public static File getCacheDir(Context ctx, FsVersion resource) {
    return getCacheDir(ctx, resource, true);
  }

  /**
   * Get a cache directory corresponding to the specified resource version.
   * The directory is created inside context's cache directory and its name is
   * based on the resource's name and version.
   *
   * @see FsVersion
   * @param ctx App context
   * @param resource Resource version to create a corresponding directory for
   * @param inDataDirIfNeeded If the directory cannot be created in cache directory, create it in the data directory
   * @return Cache directory for the specified resource, or null on error
   * @since 1.0
   */
  public static File getCacheDir(Context ctx, FsVersion resource, boolean inDataDirIfNeeded) {
    File cacheDir = getCacheDir(ctx);
    if(null != cacheDir) {
      String dirName = resource.toString();
      cacheDir = new File(cacheDir, dirName);
      cacheDir.mkdirs();
      if(!cacheDir.exists() && inDataDirIfNeeded) {
        cacheDir = ctx.getDir(dirName, Context.MODE_PRIVATE);
      }
    }
    return cacheDir;
  }

}
