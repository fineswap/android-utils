/**
 * Versioning Utility - Part of Fineswap Android Utilities.
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
 * A helpful versioning class to define arbitrary objects and assign a version
 * to them.
 *
 * @author Noor Dawod
 * @since 1.0
 */
public class FsVersion
  implements
    com.fineswap.android.aux.FsSystem,
    com.fineswap.android.aux.FsException {

  private final static NumberFormatException EXCEPTION_NUMBER_ERROR =
    new NumberFormatException();

  /**
   * A unique identifier for this object.
   *
   * @since 1.0
   */
  public final String classId;

  /**
   * Major part of the version.
   *
   * @since 1.0
   */
  public final int major;

  /**
   * Minor part of the version.
   *
   * @since 1.0
   */
  public final int minor;

  /**
   * Patch level of the version.
   *
   * @since 1.0
   */
  public final int patch;

  /**
   * Parse a string representation of a version (x.y.z) and return its parts as
   * an integer array.
   *
   * @param version Version, as a string, to parse
   * @return Parsed parts of the version
   * @throws NumberFormatException
   * @since 1.0
   */
  public static int[] parse(String version) throws NumberFormatException {
    String[] parsed = PATTERN_DOT.split(version, 3);
    if(null == parsed || 1 > parsed.length) {
      throw EXCEPTION_NUMBER_ERROR;
    }
    int[] parts = new int[3];
    parts[0] = Integer.parseInt(parsed[0]);
    parts[1] = 1 < parsed.length ? Integer.parseInt(parsed[1]) : 0;
    parts[2] = 2 < parsed.length ? Integer.parseInt(parsed[2]) : 0;
    return parts;
  }

  /**
   * Instantiates a new versioning object with the specified class id and
   * major; use zero for both minor and patch level.
   *
   * @param classId {@link #classId}
   * @param major {@link #major}
   * @since 1.0
   */
  public FsVersion(String classId, int major) {
    this(classId, major, 0);
  }

  /**
   * Instantiates a new version for an object with the specified class id, major
   * and manor; use zero for patch level.
   *
   * @param classId {@link #classId}
   * @param major {@link #major}
   * @param minor {@link #minor}
   * @since 1.0
   */
  public FsVersion(String classId, int major, int minor) {
    this(classId, major, minor, 0);
  }

  /**
   * Instantiates a new version for an object with the specified class id,
   * major, manor and patch level.
   *
   * @param classId {@link #classId}
   * @param major {@link #major}
   * @param minor {@link #minor}
   * @param patch {@link #patch}
   * @since 1.0
   */
  public FsVersion(String classId, int major, int minor, int patch) {
    if(null == classId) {
      throw EXCEPTION_NULL_VALUE;
    }
    this.classId = classId;
    this.major = major;
    this.minor = minor;
    this.patch = patch;
  }

  /**
   * Instantiates a new version for an object with the specified class id and
   * version representation (as a string).
   *
   * @param classId {@link #classId}
   * @param version {@link #getFullVersion()}
   * @throws NumberFormatException
   * @since 1.0
   */
  public FsVersion(String classId, String version) throws NumberFormatException {
    if(null == classId || null == version) {
      throw EXCEPTION_NULL_VALUE;
    }
    int[] parts = FsVersion.parse(version);
    this.classId = classId;
    this.major = parts[0];
    this.minor = parts[1];
    this.patch = parts[2];
  }

  /**
   * @since 1.0
   */
  @Override
  public String toString() {
    return classId + "-" + getFullVersion();
  }

  /**
   * @since 1.0
   */
  @Override
  public boolean equals(Object o) {
    return
      null != o &&
      o instanceof FsVersion &&
      classId.equals(((FsVersion)o).classId) &&
      major == ((FsVersion)o).major &&
      minor == ((FsVersion)o).minor &&
      patch == ((FsVersion)o).patch;
  }

  /**
   * @since 1.0
   */
  @Override
  public int hashCode() {
    return toString().hashCode();
  }

  /**
   * Get a short representation of this version (without the patch level).
   *
   * @return Short version representation
   * @since 1.0
   */
  public String getVersion() {
    return major + "." + minor;
  }

  /**
   * Get a full representation of this version.
   *
   * @return Full version representation
   * @since 1.0
   */
  public String getFullVersion() {
    return major + "." + minor + "." + patch;
  }

  /**
   * Test whether the version identified by the specified string is equal to
   * this version.
   *
   * @param version {@link #parse(String)} Version representation to test
   * @return True if and only if the passed version is equal, false otherwise
   * @throws NumberFormatException
   * @since 1.0
   */
  public boolean isEqualTo(String version) throws NumberFormatException {
    int[] parts = parse(version);
    return isEqualTo(parts[0], parts[1], parts[2]);
  }

  /**
   * Test whether the version identified by specified parameters is equal to
   * this version.
   *
   * @param versionMajor {@link #major}
   * @param versionMinor {@link #minor}
   * @return True if and only if the passed version is equal, false otherwise
   * @since 1.0
   */
  public boolean isEqualTo(int versionMajor, int versionMinor) {
    return isEqualTo(versionMajor, versionMinor, 0);
  }

  /**
   * Test whether the version identified by specified parameters is equal to
   * this version.
   *
   * @param versionMajor {@link #major}
   * @param versionMinor {@link #minor}
   * @param versionPatch {@link #patch}
   * @return True if and only if the passed version is equal, false otherwise
   * @since 1.0
   */
  public boolean isEqualTo(int versionMajor, int versionMinor, int versionPatch) {
    return major == versionMajor && minor == versionMinor && patch == versionPatch;
  }

  /**
   * Test whether the version identified by specified string is newer than
   * this version.
   *
   * @param version {@link #parse(String)} Version representation to test
   * @return True if and only if the passed version is newer, false otherwise
   * @throws NumberFormatException
   * @since 1.0
   */
  public boolean isNewerThan(String version) throws NumberFormatException {
    int[] parts = parse(version);
    return isNewerThan(parts[0], parts[1], parts[2]);
  }

  /**
   * Test whether the version identified by specified parameters is newer than
   * this version.
   *
   * @param versionMajor {@link #major}
   * @param versionMinor {@link #minor}
   * @return True if and only if the passed version is newer, false otherwise
   * @since 1.0
   */
  public boolean isNewerThan(int versionMajor, int versionMinor) {
    return
      major > versionMajor ||
      (major == versionMajor && minor > versionMinor);
  }

  /**
   * Test whether the version identified by specified parameters is newer than
   * this version.
   *
   * @param versionMajor {@link #major}
   * @param versionMinor {@link #minor}
   * @param versionPatch {@link #patch}
   * @return True if and only if the passed version is newer, false otherwise
   * @since 1.0
   */
  public boolean isNewerThan(int versionMajor, int versionMinor, int versionPatch) {
    return
      isNewerThan(versionMajor, versionMinor) ||
      (major == versionMajor && minor == versionMinor && patch > versionPatch);
  }

  /**
   * Test whether the passed version is newer than this version.
   *
   * @see FsVersion
   * @param version The version to test against
   * @return True if and only if the passed version is newer, false otherwise
   * @since 1.0
   */
  public boolean isNewerThan(FsVersion version) {
    return isNewerThan(version.major, version.minor, version.patch);
  }

  /**
   * Test whether the version identified by the specified string is older than
   * this version.
   *
   * @param version {@link #parse(String)} Version representation to test
   * @return True if and only if the passed version is older, false otherwise
   * @throws NumberFormatException
   * @since 1.0
   */
  public boolean isOlderThan(String version) throws NumberFormatException {
    int[] parts = parse(version);
    return isOlderThan(parts[0], parts[1], parts[2]);
  }

  /**
   * Test whether the version identified by specified parameters is older than
   * this version.
   *
   * @param versionMajor {@link #major}
   * @param versionMinor {@link #minor}
   * @return True if and only if the passed version is older, false otherwise
   * @since 1.0
   */
  public boolean isOlderThan(int versionMajor, int versionMinor) {
    return
      major < versionMajor ||
      (major == versionMajor && minor < versionMinor);
  }

  /**
   * Test whether the version identified by specified parameters is older than
   * this version.
   *
   * @param versionMajor {@link #major}
   * @param versionMinor {@link #minor}
   * @param versionPatch {@link #patch}
   * @return True if and only if the passed version is older, false otherwise
   * @since 1.0
   */
  public boolean isOlderThan(int versionMajor, int versionMinor, int versionPatch) {
    return
      isOlderThan(versionMajor, versionMinor) ||
      (major == versionMajor && minor == versionMinor && patch < versionPatch);
  }

  /**
   * Test whether the passed version is older than this version.
   *
   * @see FsVersion
   * @param version The version to test against
   * @return True if and only if the passed version is older, false otherwise
   * @since 1.0
   */
  public boolean isOlderThan(FsVersion version) {
    return isOlderThan(version.major, version.minor, version.patch);
  }

}
