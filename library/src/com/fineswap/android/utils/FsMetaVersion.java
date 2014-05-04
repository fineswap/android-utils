/**
 * Meta-backed Versioning Utility - Part of Fineswap Android Utilities.
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

/**
 * Extend {@link FsVersion} and add a meta-data object to go along with the base
 * versioning class.
 *
 * @see FsVersion
 * @author Noor Dawod
 * @since 1.0
 */
public class FsMetaVersion extends FsVersion {

  /**
   * Meta-data object to attach to this versioning object.
   *
   * @since 1.0
   */
  public final Object meta;

  /**
   * Instantiates a new meta-data-backed versioning object with the specified
   * class id, meta-data and major; uses default settings for minor and patch.
   *
   * @param classId {@link #classId}
   * @param meta {@link #meta}
   * @param major {@link #major}
   */
  public FsMetaVersion(String classId, Object meta, int major) {
    super(classId, major);
    this.meta = meta;
  }

  /**
   * Instantiates a new meta-data-backed versioning object with the specified
   * class id, meta-data, major and minor; uses default value for patch.
   *
   * @param classId {@link #classId}
   * @param meta {@link #meta}
   * @param major {@link #major}
   * @param minor {@link #minor}
   */
  public FsMetaVersion(String classId, Object meta, int major, int minor) {
    super(classId, major, minor);
    this.meta = meta;
  }

  /**
   * Instantiates a new meta-data-backed versioning object with the specified
   * class id, meta-data and major, minor and patch level.
   *
   * @param classId {@link #classId}
   * @param meta {@link #meta}
   * @param major {@link #major}
   * @param minor {@link #minor}
   * @param patch {@link #patch}
   */
  public FsMetaVersion(String classId, Object meta, int major, int minor, int patch) {
    super(classId, major, minor, patch);
    this.meta = meta;
  }

  /**
   * @since 1.0
   */
  @Override
  public boolean equals(Object o) {
    return
      super.equals(o) &&
      o instanceof FsMetaVersion &&
      (
        (null == meta && null == ((FsMetaVersion)o).meta) ||
        meta.equals(((FsMetaVersion)o).meta)
      );
  }

  /**
   * @since 1.0
   */
  @Override
  public int hashCode() {
    return toString().hashCode() + (null == meta ? 0 : meta.hashCode());
  }

}
