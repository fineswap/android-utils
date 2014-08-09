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

package com.fineswap.lib.android.intro;

import com.fineswap.lib.android.utils.FsMetaVersion;
import com.fineswap.lib.android.utils.FsVersion;

/**
 * Defines a portion on the Activity's content view (called a Ring) with
 * additional meta-data for presentation.
 *
 * @author Noor Dawod
 * @since 1.0
 */
public class FsRingResource implements com.fineswap.lib.android.aux.FsException {

  /**
   * Define a ring's look-and-feel when it is being painted.
   *
   * @since 1.0
   */
  public static class LookAndFeel {

    /**
     * Default LAF to be associated with rings which have none.
     *
     * @since 1.0
     */
    public final static LookAndFeel DEFAULT = new LookAndFeel();

    /**
     * The padding, in pixels, around the ring's associated view.
     *
     * @since 1.0
     */
    public final int viewPadding;

    /**
     * The color, in 0xAARRGGBB format, of the outermost ring.
     * Subsequent rings' color will fade out according to the number of rings.
     * To use the slide's tint color, pass -1 as the value.
     *
     * @since 1.0
     */
    public final int viewRingColor;

    /**
     * Whether the ring's color is supplied by the user or not.
     *
     * @since 1.0
     */
    boolean isCustomRingColor;

    /**
     * The thickness, in pixels, of each ring.
     * All rings will have the same thickness.
     *
     * @since 1.0
     */
    public final int viewRingThickness;

    /**
     * The maximum allowed dimension, pixels; in either X or Y coordinates, of
     * the ring. If unspecified, or -1 is specified, the ring's dimension will
     * be equal to its associated view.
     *
     * @since 1.0
     */
    public final int viewMaxDimension;

    /**
     * Instantiates a new LAF object with default values.
     *
     * @since 1.0
     */
    public LookAndFeel() {
      this(FsIntroFactory.defaultViewPadding);
    }

    /**
     * Instantiates a new LAF object with default values, except for the ring's
     * padding.
     *
     * @param viewPadding {@link #viewPadding}
     * @since 1.0
     */
    public LookAndFeel(int viewPadding) {
      this(viewPadding, -1);
      this.isCustomRingColor = false;
    }

    /**
     * Instantiates a new LAF object with default values, except for the ring's
     * padding and color.
     *
     * @param viewPadding {@link #viewPadding}
     * @param viewRingColor {@link #viewRingColor}
     * @since 1.0
     */
    public LookAndFeel(int viewPadding, int viewRingColor) {
      this(viewPadding, viewRingColor, FsIntroFactory.defaultRingThickness);
    }

    /**
     * Instantiates a new LAF object with default values, except for the ring's
     * padding, color and thickness.
     *
     * @param viewPadding {@link #viewPadding}
     * @param viewRingColor {@link #viewRingColor}
     * @param viewRingThickness {@link #viewRingThickness}
     * @since 1.0
     */
    public LookAndFeel(int viewPadding, int viewRingColor, int viewRingThickness) {
      this(viewPadding, viewRingColor, viewRingThickness, -1);
    }

    /**
     * Instantiates a new LAF object by supplying all of its values.
     *
     * @param viewPadding {@link #viewPadding}
     * @param viewRingColor {@link #viewRingColor}
     * @param viewRingThickness {@link #viewRingThickness}
     * @param viewMaxDimension {@link #viewMaxDimension}
     * @since 1.0
     */
    public LookAndFeel(int viewPadding, int viewRingColor, int viewRingThickness, int viewMaxDimension) {
      this.viewPadding = viewPadding;
      this.viewRingColor = viewRingColor;
      this.isCustomRingColor = true;
      this.viewRingThickness = viewRingThickness;
      this.viewMaxDimension = viewMaxDimension;
    }

  }

  /**
   * A unique version definition for this ring.
   * It is advisable to bump up the version in order to let {@link FsIntroOverlay}
   * instance to forcibly show a ring, for example when the developer changes
   * its implementation and needs to inform the user of the changes.
   *
   * @since 1.0
   */
  public final FsMetaVersion<LookAndFeel> ring;

  /**
   * The view (resource id) associated with this ring which is to be found in the
   * Activity's content view.
   *
   * @since 1.0
   */
  public final int viewResourceInPage;

  /**
   * The intro view (resource id) associated with this ring which is to be found
   * in the content view of {@link FsIntroOverlay}.
   * If the value is equal to -1, it will cause the ring to show always whether
   * this view is present or not.
   *
   * @since 1.0
   */
  public final int viewResourceInSlide;

  /**
   * Instantiates a new ring matching the specified view definitions.
   * The {@link LookAndFeel} details is embedded as meta-data in the
   * {@link #ring} parameter.
   *
   * @param laf {@link #ring}
   * @param viewResourceInPage {@link #viewResourceInPage}
   * @param viewResourceInSlide {@link #viewResourceInSlide}
   * @since 1.0
   */
  public FsRingResource(FsMetaVersion<LookAndFeel> laf, int viewResourceInPage, int viewResourceInSlide) {
    if(null == laf || null == laf.data) {
      throw EXCEPTION_NULL_OR_EMPTY;
    }
    this.ring = laf;
    this.viewResourceInPage = viewResourceInPage;
    this.viewResourceInSlide = viewResourceInSlide;
  }

  /**
   * @since 1.0
   */
  @Override
  public String toString() {
    return ring.toString();
  }

  /**
   * @since 1.0
   */
  @Override
  public boolean equals(Object o) {
    return
      null != o &&
      o instanceof FsRingResource &&
      ring.equals(((FsRingResource)o).ring);
  }

  /**
   * @since 1.0
   */
  @Override
  public int hashCode() {
    return toString().hashCode();
  }

  /**
   * Get the current version of this {@link FsRingResource} instance.
   *
   * @see #ring
   * @return This instance's version
   * @since 1.0
   */
  public FsVersion getVersion() {
    return ring;
  }

}
