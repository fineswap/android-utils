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
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * A collection of helpful, static functions for dealing with various system
 * situations.
 *
 * @author Noor Dawod
 * @since 1.0
 */
public class FsSystem implements com.fineswap.android.aux.FsSystem {

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
    return getCacheDir(ctx, resource.toString(), inDataDirIfNeeded);
  }

  /**
   * Get a cache directory corresponding to the specified resource.
   * The directory is created inside context's cache directory and its name is
   * based on the resource's name. If the directory cannot be created in the
   * cache directory, it will be created in the data directory.
   *
   * @param ctx App context
   * @param resourceName Resource name to create a corresponding directory for
   * @return Cache directory for the specified resource, or null on error
   * @since 1.0
   */
  public static File getCacheDir(Context ctx, String resourceName) {
    return getCacheDir(ctx, resourceName, true);
  }

  /**
   * Get a cache directory corresponding to the specified resource.
   * The directory is created inside context's cache directory and its name is
   * based on the resource's name and version.
   *
   * @param ctx App context
   * @param resourceName Resource name to create a corresponding directory for
   * @param inDataDirIfNeeded If the directory cannot be created in cache directory, create it in the data directory
   * @return Cache directory for the specified resource, or null on error
   * @since 1.0
   */
  public static File getCacheDir(Context ctx, String resourceName, boolean inDataDirIfNeeded) {
    File cacheDir = getCacheDir(ctx);
    if(null != cacheDir) {
      cacheDir = new File(cacheDir, resourceName);
      cacheDir.mkdirs();
      if(!cacheDir.exists() && inDataDirIfNeeded) {
        cacheDir = ctx.getDir(resourceName, Context.MODE_PRIVATE);
      }
    }
    return cacheDir;
  }

  /**
   * Safely close an I/O stream without raising an {@link IOException}.
   *
   * @param stream I/O stream to close
   * @since 1.0
   */
  public static void closeStream(Object stream) {
    if(null != stream) {
      try {
        if(stream instanceof InputStream) {
          ((InputStream)stream).close();
        } else if(stream instanceof OutputStream) {
          ((OutputStream)stream).close();
        } else if(stream instanceof Reader) {
          ((Reader)stream).close();
        } else if(stream instanceof Writer) {
          ((Writer)stream).close();
        }
      } catch(IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Read file's contents into a byte-array using default buffer size.
   *
   * @param filePath Absolute file path
   * @return File contents, or null if an error occurs
   * @since 1.0
   */
  public static byte[] readFile(String filePath) {
    return readFile(new File(filePath));
  }

  /**
   * Read file's contents into a byte-array using specified buffer size.
   *
   * @param filePath Absolute file path
   * @param bufferSize Size of read buffer to use
   * @return File contents, or null if an error occurs
   * @since 1.0
   */
  public static byte[] readFile(String filePath, int bufferSize) {
    return readFile(new File(filePath), bufferSize);
  }

  /**
   * Read file's contents into a byte-array using default buffer size.
   *
   * @param file Absolute file path
   * @return File contents, or null if an error occurs
   * @since 1.0
   */
  public static byte[] readFile(File file) {
    return readFile(file, FILEOP_BUFFER_SIZE);
  }

  /**
   * Read file's contents into a byte-array using specified buffer size.
   *
   * @param file Absolute file path
   * @param bufferSize Size of read buffer to use
   * @return File contents, or null if an error occurs
   * @since 1.0
   */
  public static byte[] readFile(File file, int bufferSize) {
    if(file.isFile() && file.canRead()) {
      InputStream is = null;
      ByteArrayOutputStream os = null;
      try {
        is = new BufferedInputStream(new FileInputStream(file), bufferSize);
        os = new ByteArrayOutputStream();
        int bytesRead;
        byte[] buffer = new byte[bufferSize];
        while(-1 != (bytesRead = is.read(buffer, 0, buffer.length))) {
          os.write(buffer, 0, bytesRead);
        }
        os.flush();
        return os.toByteArray();
      } catch(IOException e) {
        e.printStackTrace();
      } finally {
        closeStream(os);
        closeStream(is);
      }
    }
    return null;
  }

  /**
   * Generate a random file name, either for a file or a directory, which is
   * located alongside the original file (same directory location).
   *
   * @param file Source file to consider
   * @return Temporary file
   * @since 1.0
   */
  public static File getTempFileName(File file) {
    if(null != file) {
      // Full path to this file.
      String filePath = file.getAbsolutePath();

      // Temporary file name.
      String tempName = System.currentTimeMillis() + ".tmp";

      // In case of a directory, there's no need to add the directory separator.
      File tempFile;
      if(file.isDirectory()) {
        tempFile = new File(filePath + "-" + tempName);
      } else {
        // The directory name containing this file.
        String fileDir = File.separator.equals(filePath)
          ? File.separator
          : filePath.substring(0, filePath.lastIndexOf(File.separatorChar));

        // A new temporary file inside the parent directory.
        tempFile = new File(fileDir, tempName);
      }
      return tempFile;
    }
    return null;
  }

  /**
   * Write the passed byte-array into the specified file using the default
   * chunk size. If this file exists, it will be overwritten.
   *
   * @param file Target file to write to
   * @param bytes Contents to write to the file
   * @return True if all bytes were written successfully, false otherwise
   * @since 1.0
   */
  public static boolean writeFile(File file, byte[] bytes) {
    return writeFile(file, bytes, FILEOP_BUFFER_SIZE);
  }

  /**
   * Write the passed byte-array into the specified file using the passed chunk
   * size. If this file exists, it will be overwritten.
   *
   * @param file Target file to write to
   * @param bytes Contents to write to the file
   * @param chunkSize Size of each chunk of data to write to the file
   * @return True if all bytes were written successfully, false otherwise
   * @since 1.0
   */
  public static boolean writeFile(File file, byte[] bytes, int chunkSize) {
    if(null != file && null != bytes && !file.isDirectory()) {
      // In order not to overwrite an existing file in a case of a problem,
      // we first write the bytes in a file with a different name. When all
      // is successful, we remove the existing one (if it exists) and rename the
      // file.
      File tempPath = getTempFileName(file);

      // Remove this temporary file, in case it exists.
      tempPath.delete();

      // Position of the writing pin, and length of the bytes to write.
      int pos = 0, length = bytes.length;

      // Try to write the contents into the temporary file.
      InputStream is = null;
      BufferedOutputStream os = null;
      try {
        is = new ByteArrayInputStream(bytes);
        os = new BufferedOutputStream(new FileOutputStream(tempPath), chunkSize);

        // Writing loop.
        while(length > pos) {
          if(length < pos + chunkSize) {
            chunkSize = length - pos;
          }
          os.write(bytes, pos, chunkSize);
          pos += chunkSize;
        }
        os.flush();
      } catch(IOException e) {
        e.printStackTrace();
      } finally {
        closeStream(os);
        closeStream(is);
      }

      // If the writing pin is at the end, writing was successful.
      if(pos == length) {
        // Time to remove existing file...
        file.delete();

        // And to rename temporary file.
        return tempPath.renameTo(file);
      }
    }
    return false;
  }

  /**
   * Recursively delete a directory's files and sub-directories, including
   * itself.
   *
   * @param dir Directory to clean out
   * @since 1.0
   */
  public static void rmdir(File dir) {
    rmdir(dir, true);
  }

  /**
   * Recursively delete a directory's files and sub-directories, optionally
   * keeping itself when empty.
   *
   * @param dir Directory to clean out
   * @param removeSelf Whether to remove the directory itself when it's clean
   * @since 1.0
   */
  public static void rmdir(File dir, boolean removeSelf) {
    if(null != dir && !".".equals(dir.getName()) && !"..".equals(dir.getName())) {
      if(dir.isDirectory()) {
        File[] files = dir.listFiles();
        for(File file : files) {
          rmdir(file, true);
        }
      }
      if(removeSelf) {
        dir.delete();
      }
    }
  }

  /**
   * If the specified directory is a file, it will be removed prior to creating
   * the directory.
   *
   * @param dir Directory to create
   * @return True if the directory either exists or was created, false otherwise
   * @since 1.0
   */
  public static boolean mkdirs(File dir) {
    if(null != dir) {
      if(dir.isFile()) {
        dir.delete();
      }
      if(dir.isDirectory() || dir.mkdirs()) {
        return true;
      }
    }
    return false;
  }

}
