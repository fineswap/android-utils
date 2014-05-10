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

import android.app.Activity;
import android.graphics.Paint;
import com.fineswap.android.utils.FsMetaVersion;
import java.util.HashMap;
import java.util.Set;

/**
 * Factory utility to create new {@link FsIntroOverlay} instances.
 *
 * @author Noor Dawod
 * @since 1.0
 */
public class FsIntroFactory {

  /**
   * An interface to enable {@link FsIntroOverlay} to store viewed rings, slides
   * and instances.
   *
   * @since 1.0
   */
  public static interface PersistentDatabase {

    /**
     * Check whether the specified {@link FsIntroOverlay} instance has already
     * been shown, or it's a new one.
     *
     * @param introOverlay {@link FsIntroOverlay} instance
     * @return True if this instance is new, false otherwise
     * @since 1.0
     */
    public boolean isNewIntro(FsIntroOverlay introOverlay);

    /**
     * Register the specified {@link FsIntroOverlay} instance as being shown.
     *
     * @param introOverlay {@link FsIntroOverlay} instance
     * @since 1.0
     */
    public void registerIntro(FsIntroOverlay introOverlay);

    /**
     * Unregister the specified {@link FsIntroOverlay} instance, thus making it
     * appear as new.
     *
     * @param introOverlay {@link FsIntroOverlay} instance
     * @since 1.0
     */
    public void unregisterIntro(FsIntroOverlay introOverlay);

    /**
     * Check whether the specified {@link FsSlideResource} instance has already
     * been shown, or it's a new one.
     *
     * @param slideResource {@link FsSlideResource} instance
     * @return True if this instance is new, false otherwise
     * @since 1.0
     */
    public boolean isNewSlide(FsSlideResource slideResource);

    /**
     * Register the specified {@link FsSlideResource} instance as being shown.
     *
     * @param slideResource {@link FsSlideResource} instance
     * @since 1.0
     */
    public void registerSlide(FsSlideResource slideResource);

    /**
     * Unregister the specified {@link FsSlideResource} instance, thus making it
     * appear as new.
     *
     * @param slideResource {@link FsSlideResource} instance
     * @since 1.0
     */
    public void unregisterSlide(FsSlideResource slideResource);

    /**
     * Check whether the specified {@link FsRingResource} instance has already
     * been shown in the supplied {@link FsSlideResource} instance, or it's a
     * new one.
     *
     * @param slideResource {@link FsSlideResource} instance
     * @param ringResource {@link FsRingResource} instance
     * @return True if this instance is new, false otherwise
     * @since 1.0
     */
    public boolean isNewRing(FsSlideResource slideResource, FsRingResource ringResource);

    /**
     * Register the specified {@link FsSlideResource} instance that appear in
     * the supplied {@link FsSlideResource} instance as being shown.
     *
     * @param slideResource {@link FsSlideResource} instance
     * @param ringResource {@link FsRingResource} instance
     * @since 1.0
     */
    public void registerRing(FsSlideResource slideResource, FsRingResource ringResource);

    /**
     * Unregister the specified {@link FsRingResource} instance that appear in
     * the supplied {@link FsSlideResource} instance, thus making it appear as
     * new.
     *
     * @param slideResource {@link FsSlideResource} instance
     * @param ringResource {@link FsRingResource} instance
     * @since 1.0
     */
    public void unregisterRing(FsSlideResource slideResource, FsRingResource ringResource);

  }

  /**
   * All created instances of IntroOverlay.
   *
   * @since 1.0
   */
  private final static HashMap<FsMetaVersion<Activity>, FsIntroOverlay> instances = new HashMap();

  /**
   * A single Paint instance for all painting jobs.
   * ("One painter to control them all").
   *
   * @since 1.0
   */
  final static Paint painter = new Paint();

  /**
   * Default tint color for newly-created slides.
   *
   * @since 1.0
   */
  static int defaultTintColor = 0xC0000000;

  /**
   * How many rings to paint around each view.
   *
   * @since 1.0
   */
  static int defaultRingCount = 4;

  /**
   * Default thickness of each ring (physical pixels count).
   *
   * @since 1.0
   */
  static int defaultRingThickness = 10;

  /**
   * Default padding for the rings (physical pixels count).
   *
   * @since 1.0
   */
  static int defaultViewPadding = 2;

  /**
   * Set the default tint color for newly-created slides.
   *
   * @param color ARGB color to use (ex.: 0xC0F0F0F0)
   * @since 1.0
   */
  public static void setDefaultTintColor(int color) {
    defaultTintColor = color;
  }

  /**
   * Set how many rings to paint around a view.
   *
   * @param count Rings count value
   * @since 1.0
   */
  public static void setDefaultRingCount(int count) {
    defaultRingCount = count;
  }

  /**
   * Set the default thickness of each ring (physical pixels count).
   *
   * @param thickness Thickness value of each ring
   * @since 1.0
   */
  public static void setDefaultRingThickness(int thickness) {
    defaultRingThickness = thickness;
  }

  /**
   * Set the default padding for the rings (physical pixels count).
   *
   * @param padding Padding value
   * @since 1.0
   */
  public static void setDefaultPadding(int padding) {
    defaultViewPadding = padding;
  }

  /**
   * Get an instance of {@link FsIntroOverlay} for the specified version of the
   * activity.
   *
   * @param activityVersion Target Activity and version to get an instance for
   * @return Instance of FsIntroOverlay for the specified activity
   * @since 1.0
   */
  public static FsIntroOverlay getInstance(FsMetaVersion<Activity> activityVersion) {
    synchronized(instances) {
      FsIntroOverlay activityInstance = instances.get(activityVersion);
      if(null == activityInstance) {
        activityInstance = new FsIntroOverlay(activityVersion);
        instances.put(activityVersion, activityInstance);
      }
      return activityInstance;
    }
  }

  /**
   * Clears internal cache of all instances of {@link FsIntroOverlay}.
   * This function will not destroy the instances which will continue to live
   * elsewhere. It's the user's responsibility to properly destroy them by
   * calling their respective {@link FsIntroOverlay#destroy()} method.
   *
   * @since 1.0
   */
  public static void clear() {
    synchronized(instances) {
      instances.clear();
    }
  }

  /**
   * Clear and destroy all cached instances of {@link FsIntroOverlay}.
   *
   * @since 1.0
   */
  public static void destroy() {
    synchronized(instances) {
      Set<FsMetaVersion<Activity>> activitiesVersions = instances.keySet();
      for(FsMetaVersion<Activity> activityVersion : activitiesVersions) {
        FsIntroOverlay activityInstance = instances.get(activityVersion);
        if(null != activityInstance) {
          activityInstance.destroyInternal(false);
        }
      }
      instances.clear();
    }
  }

  static void destroyInstance(FsIntroOverlay instance) {
    synchronized(instances) {
      if(null != instance) {
        instances.remove(instance.getVersion());
      }
    }
  }

}
