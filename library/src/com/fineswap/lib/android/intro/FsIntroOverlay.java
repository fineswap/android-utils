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

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import com.fineswap.lib.android.utils.FsGraphics;
import com.fineswap.lib.android.utils.FsMetaVersion;
import com.fineswap.lib.android.utils.FsSystem;
import com.fineswap.lib.android.utils.FsView;
import java.util.ArrayList;

/**
 * Take over an activity's window by presenting a tinted overlay on top.
 *
 * This is mostly used to present news, tips, tricks, help and intro slides to
 * users when a new version of an app is released.
 *
 * The overlay's contents are inflated dynamically and its views are selectively
 * shown or hidden based on the existence of associated companion views in the
 * activity's content view.
 *
 * @author Noor Dawod
 * @since 1.0
 */
public class FsIntroOverlay
  implements
    com.fineswap.lib.android.aux.FsLayout,
    com.fineswap.lib.android.aux.FsGraphics,
    com.fineswap.lib.android.aux.FsException {

  private final static String LOG_TAG = "FsIntroOverlay";

  /**
   * Thrown when dialog's content layout cannot be inflated.
   *
   * @since 1.0
   */
  private final static RuntimeException EXCEPTION_SLIDE_LAYOUT_INVALID =
    new RuntimeException("Slide's layout view either cannot be inflated or not a ViewGroup.");

  /**
   * Thrown when trying to attach a slide which is already attached.
   *
   * @since 1.0
   */
  private final static RuntimeException EXCEPTION_ALREADY_ATTACHED =
    new RuntimeException("Slide is already attached to another IntroOverlay and must be detached first.");

  /**
   * Thrown when trying to detach a slide which is already detached.
   *
   * @since 1.0
   */
  private final static RuntimeException EXCEPTION_ALREADY_DETACHED =
    new RuntimeException("Slide is already detached from this IntroOverlay!");

  /**
   * Paint mode to paint the rings.
   *
   * @since 1.0
   */
  private final static PorterDuffXfermode PORTER_DUFF_SRC =
    new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);

  /**
   * One layout inflater for all slides in this overlay.
   *
   * @since 1.0
   */
  private final LayoutInflater inflater;

  /**
   * The "holes" which will be made where the views are positioned in the page.
   *
   * @since 1.0
   */
  private final ArrayList<RectF> holes = new ArrayList(10);

  /**
   * The intro overlay utilize a single Dialog instance.
   *
   * @since 1.0
   */
  private final Dialog dialog;

  /**
   * The intro overlay uses one single view which contains the slide's tint.
   *
   * @since 1.0
   */
  private final View dialogTintView;

  /**
   * The intro overlay uses one single layout to hold all slides' content views.
   *
   * @since 1.0
   */
  private final FrameLayout dialogContentView;

  /**
   * An array holding instances of all attached slides.
   *
   * @since 1.0
   */
  private final ArrayList<FsSlideResource> slides = new ArrayList(5);

  /**
   * The version definition of this instance of FsIntroOverlay.
   * The meta-data-backed versioning object contains both the activity and the
   * target version to use.
   *
   * @since 1.0
   */
  private final FsMetaVersion<Activity> version;

  /**
   * The Activity's top-level content view which is showing on-screen.
   *
   * @since 1.0
   */
  private final ViewGroup activityContentView;

  /**
   * Current slide instance shown.
   *
   * @since 1.0
   */
  private FsSlideResource currentSlide;

  /**
   * Persistent database to keep statistics about rings, slides and instances.
   *
   * @since 1.0
   */
  private FsIntroFactory.PersistentDatabase db;

  /**
   * Instantiate a new IntroOverlay with the specified version.
   * Once created, start adding slides to it and then call show() to fire it up.
   *
   * @param activityVersion {@link #version}
   * @since 1.0
   */
  FsIntroOverlay(FsMetaVersion<Activity> activityVersion) {
    // Make sure the
    // Get activity's root content view.
    activityContentView = FsView.getRootContentView(activityVersion.data);
    if(null == activityContentView) {
      throw EXCEPTION_CONTENT_VIEW_NULL_OR_EMPTY;
    }

    // Set a default persistent database by default.
    db = new FsIntroFileDatabase(FsSystem.getCacheDir(activityVersion.data));

    // Version of this overlay instance.
    version = activityVersion;

    // System layout inflater.
    inflater = (LayoutInflater)activityVersion.data.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    // The overlay will be shown in a dialog.
    dialog = new Dialog(activityVersion.data, android.R.style.Theme_Translucent_NoTitleBar);

    // Make the dialog has no title at the top of the screen.
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

    // Don't dismiss the dialog when clicking on its canvas.
    dialog.setCanceledOnTouchOutside(false);

    // If Back key is pressed, clear the dialog.
    dialog.setCancelable(true);

    // There's just one tint view which serves all slides.
    dialogTintView = new View(activityVersion.data);
    dialogTintView.setLayoutParams(VIEWGROUP_MATCH_PARENT);

    // And it serves as the dialog's underlying content view.
    dialog.setContentView(dialogTintView);

    // The dialog's main content view, resting above the tint.
    dialogContentView = new FrameLayout(activityVersion.data);

    // Add it to the dialog's content views.
    dialog.addContentView(dialogContentView, FRAMELAYOUT_MATCH_PARENT);
  }

  @Override
  public String toString() {
    return version.toString();
  }

  /**
   * @since 1.0
   */
  @Override
  public boolean equals(Object o) {
    return
      null != o &&
      o instanceof FsIntroOverlay &&
      version.equals(((FsIntroOverlay)o).version);
  }

  /**
   * @since 1.0
   */
  @Override
  public int hashCode() {
    return version.hashCode();
  }

  /**
   * Get the current version of this {@link FsIntroOverlay} instance.
   *
   * @see #version
   * @return This instance's version
   * @since 1.0
   */
  public FsMetaVersion<Activity> getVersion() {
    return version;
  }

  /**
   * Get the current persistent database interface in this instance.
   *
   * @return Current persistent database interface
   * @since 1.0
   */
  public FsIntroFactory.PersistentDatabase getPersistentDatabase() {
    return db;
  }

  /**
   * Set a new persistent database interface for this instance.
   *
   * @param db New persistent database interface
   * @since 1.0
   */
  public void setPersistentDatabase(FsIntroFactory.PersistentDatabase db) {
    this.db = db;
  }

  /**
   * Attach a new slide to this FsIntroOverlay instance.
   * The slide is not automatically shown, but its associated layout is inflated
   * immediately.
   *
   * @param slideResource Slide definition to attach
   * @since 1.0
   */
  public synchronized void attach(FsSlideResource slideResource) {
    if(null != slideResource) {
      // Get current overlay, if applicable, attached to this slide.
      FsIntroOverlay introOverlay = slideResource.getIntroOverlay();
      if(!this.equals(introOverlay)) {
        // Only one instance can be attached to a slide.
        if(null != introOverlay) {
          throw EXCEPTION_ALREADY_ATTACHED;
        }

        // Inflate this slide's content view.
        ViewGroup layoutView = slideResource.inflate(inflater);
        if(null == layoutView) {
          throw EXCEPTION_SLIDE_LAYOUT_INVALID;
        }

        // Add the slide's layout view to dialog's content view.
        dialogContentView.addView(layoutView);

        // Add also to the internal array holding the slides.
        slides.add(slideResource);

        // Fire onAttach callback.
        slideResource.onAttachInternal(this);
      }
    }
  }

  /**
   * Detach an existing slide from this FsIntroOverlay instance.
   * The detached slide's content view is destroyed, any bound listeners are
   * cleared and all retained memory is released.
   *
   * @param slideResource Slide to detach
   * @since 1.0
   */
  public synchronized void detach(FsSlideResource slideResource) {
    if(null != slideResource && this.equals(slideResource.getIntroOverlay())) {
      // Get the slide's content view.
      ViewGroup layoutView = slideResource.getLayoutView();

      // Who removed this view!?
      if(null == layoutView) {
        throw EXCEPTION_ALREADY_DETACHED;
      }

      // Remove the slide's content view from dialog's content view.
      dialogContentView.removeView(layoutView);

      // Unbind click listeners from this slide.
      slideResource.hideInternal();

      // Fire onDetach callback.
      slideResource.onDetachInternal();

      // Deflate (GC) this slide.
      slideResource.deflate();

      // Remove from the internal array holding the slides.
      slides.remove(slideResource);
    }
  }

  /**
   * Checks the database whether there's a need to show any of the attached
   * slides.
   *
   * @since 1.0
   */
  public void autoShowSlide() {
    for(FsSlideResource slideResource : slides) {
      // If there is no database backend, or if this is a new intro/slide, try
      // to show the slide.
      if(showSlide(slideResource)) {
        return;
      }
    }

    // No slides should be shown, so destroy the dialog.
    destroy();
  }

  /**
   * Show the slide at the specified position (forced).
   *
   * @param slidePos Position of the slide to show
   * @return Return true if the slide is shown, false otherwise
   * @since 1.0
   */
  public boolean showSlide(int slidePos) {
    return showSlide(slides.get(slidePos));
  }

  /**
   * Show the slide at the specified position (forced).
   *
   * @param slidePos Position of the slide to show
   * @param doNotCheckDb Whether to check the database or not (checks by default)
   * @return Return true if the slide is shown, false otherwise
   * @since 1.0
   */
  public boolean showSlide(int slidePos, boolean doNotCheckDb) {
    return showSlide(slides.get(slidePos), doNotCheckDb);
  }

  /**
   * Show the specified, previously-attached slide.
   *
   * @param slideResource Slide to show, must be previously attached
   * @return Return true if the slide is shown, false otherwise
   * @since 1.0
   */
  public synchronized boolean showSlide(final FsSlideResource slideResource) {
    return showSlide(slideResource, false);
  }

  /**
   * Show the specified, previously-attached slide.
   *
   * @param slideResource Slide to show, must be previously attached
   * @param doNotCheckDb Whether to check the database or not (checks by default)
   * @return Return true if the slide is shown, false otherwise
   * @since 1.0
   */
  public synchronized boolean showSlide(final FsSlideResource slideResource, boolean doNotCheckDb) {
    // Make sure this slide is attached.
    if(null == slideResource || !slides.contains(slideResource)) {
      return false;
    }

    // If this is the slide currently shown, return abruptly.
    if(null != currentSlide && currentSlide.equals(slideResource)) {
      return true;
    }

    // Get the slide's content view.
    ViewGroup layoutView = slideResource.getLayoutView();
    if(null == layoutView) {
      // This shouldn't happen...
      return false;
    }

    // By default, the database will be checked.
    boolean isNewIntro = doNotCheckDb;

    // When using a persistent database, check whether to show the slide or not.
    if(!isNewIntro && null != db) {
      // Before showing the slide, make sure that either the Intro instance is
      // new, or the slide is new, or one ring at least in the slide is new.
      isNewIntro = db.isNewIntro(this);
      Log.d(LOG_TAG, "Instance: " + this);
      boolean shouldShowSlide = isNewIntro || db.isNewSlide(slideResource);
      Log.d(LOG_TAG, "  Slide: " + slideResource);
      if(!shouldShowSlide) {
        // Check and find if any rings in this slide are new.
        for(FsRingResource ringResource : slideResource.rings) {
          if(db.isNewRing(slideResource, ringResource)) {
            Log.d(LOG_TAG, "    Ring '" + ringResource + "' was not viewed yet!");
            shouldShowSlide = true;
            break;
          }
          Log.d(LOG_TAG, "    Ring '" + ringResource + "' was viewed.");
        }

        // If there's no need to show the slide, just return abruptly.
        if(!shouldShowSlide) {
          return true;
        }
      }
    }

    // The output bitmap which holds the slide's tint.
    Bitmap tint = Bitmap.createBitmap(activityContentView.getMeasuredWidth(), activityContentView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

    // Canvas on which to draw.
    Canvas canvas = new Canvas(tint);

    // Reuse the single painter.
    Paint painter = FsIntroFactory.painter;
    painter.reset();

    // Reset the painter object.
    painter.setFlags(Paint.ANTI_ALIAS_FLAG);

    // Initial state will have a fully-opaque bitmap.
    painter.setColor(slideResource.getTintColor());
    canvas.drawRect(new Rect(0, 0, activityContentView.getMeasuredWidth(), activityContentView.getMeasuredHeight()), painter);

    // Start by assuming that all rings are hidden.
    boolean allRingsHidden = 0 < slideResource.rings.size();

    // Cycle through the slide's rings to create "holes" in the canvas.
    for(FsRingResource ringResource : slideResource.rings) {
      // Find the view in the root window.
      View viewInPage = activityContentView.findViewById(ringResource.viewResourceInPage);

      // The view resting in the slide's content view.
      View viewInSlide = layoutView.findViewById(ringResource.viewResourceInSlide);

      // Is this a new ring?
      boolean isNewRing = null == db || db.isNewRing(slideResource, ringResource);

      // If the view isn't found, or it's hidden, continue to the next one.
      boolean shouldHide = null == viewInPage || View.VISIBLE != viewInPage.getVisibility() || !isNewRing;
      if(!shouldHide) {
        // Hide the view in slide's content view if it's not found and if not
        // being forced to show.
        shouldHide = !isNewRing && 0 < ringResource.viewResourceInSlide && null == viewInSlide;

        // Prepare to make a "hole" in the canvas:
        // Gather the view's coordinates and dimension.
        int[] coords = new int[2];
        viewInPage.getLocationInWindow(coords);
        int width = viewInPage.getWidth();
        int height = viewInPage.getHeight();

        // Show some debugging information.
        Log.d(LOG_TAG,
          "Ring '" + ringResource + "' details:\n" +
          "  Dimension: " + width + "x" + height + " pixels\n" +
          "  Location: " + coords[0] + "x" + coords[1]
        );

        // Try to detect the status bar height.
        int statusBarHeight = FsGraphics.getStatusBarHeight(getVersion().data);
        if(0 > statusBarHeight) {
          throw EXCEPTION_CONTENT_VIEW_NULL_OR_EMPTY;
        }

        // If the view's coordinates and dimension are valid, continue.
        if(0 < width && 0 < height && 0 <= coords[0] && 0 <= coords[1]) {
          FsRingResource.LookAndFeel laf = ringResource.ring.data;
          int leftX = coords[0] - laf.viewPadding;
          int rightX = coords[0] + width + laf.viewPadding;
          int topY = coords[1] - laf.viewPadding - statusBarHeight;
          int bottomY = coords[1] + height + laf.viewPadding - statusBarHeight;

          // Keep the dimension within defined max. value.
          if(0 < laf.viewMaxDimension && (laf.viewMaxDimension < width || laf.viewMaxDimension < height)) {
            width = Math.max(width, laf.viewMaxDimension);
            height = Math.max(height, laf.viewMaxDimension);
            Log.d(LOG_TAG,
              "  Restrained size: " + width + "x" + height + " pixels"
            );
          }

          // Which color to use for the rings?
          int ringColor = laf.isCustomRingColor ? laf.viewRingColor : slideResource.getTintColor();

          // How many rings to paint.
          int ringCount = slideResource.getRingCount();

          Log.d(LOG_TAG,
            "  LAF settings:\n" +
            "    Padding: " + laf.viewPadding + "\n" +
            "    Count: " + ringCount + "\n" +
            "    Color: 0x" + Integer.toHexString(ringColor) + " (From " + (laf.isCustomRingColor ? "LAF" : "Slide") + ")\n" +
            "    Thickness: " + laf.viewRingThickness + "\n" +
            "    Max. size: " + laf.viewMaxDimension + "\n" +
            "  Oval settings:\n" +
            "    Top: " + topY + "\n" +
            "    Bottom: " + bottomY + "\n" +
            "    Left: " + leftX + "\n" +
            "    Right: " + rightX
          );

          // Color of each step.
          if(0 < ringCount) {
            // How much lightening of the color in each ring.
            float stepPercent = 100.0f / (float)ringCount;

            // Initial color values for the outermost ring.
            int[] ringColorChannels = FsGraphics.decodeColorChannels(ringColor);

            // Initial alpha channel of the color.
            int ringColorAlpha = ringColorChannels[0];

            // Initial thickness to cut from the canvas.
            int thickness = ringCount * laf.viewRingThickness;

            // The color with which the rings will be painted.
            painter.setXfermode(PORTER_DUFF_SRC);

            // Draw successive holes in the canvas.
            while(0 < thickness) {
              ringColorAlpha -= stepPercent / 100 * ringColorAlpha;
              painter.setColor(Color.argb(ringColorAlpha, ringColorChannels[1], ringColorChannels[2], ringColorChannels[3]));
              canvas.drawOval(new RectF(leftX - thickness, topY - thickness, rightX + thickness, bottomY + thickness), painter);
              thickness -= laf.viewRingThickness;
            }
          }

          // Final cut of the ring in the canvas.
          holes.add(new RectF(leftX, topY, rightX, bottomY));
        } else {
          Log.d(LOG_TAG,
            "  Ignored because of incorrect dimension or position."
          );
        }
      }

      if(null != viewInSlide) {
        if(shouldHide) {
          viewInSlide.setVisibility(View.GONE);
          Log.d(LOG_TAG,
            "  View in slide is now hidden."
          );
        } else {
          allRingsHidden = false;
          viewInSlide.setVisibility(View.VISIBLE);
          Log.d(LOG_TAG,
            "  View in slide is now visible."
          );
          if(!doNotCheckDb && isNewRing && null != db) {
            db.registerRing(slideResource, ringResource);
            Log.d(LOG_TAG,
              "    Registered view."
            );
          }
        }
      } else {
        Log.d(LOG_TAG,
          "  Ignored because of view in slide is not found."
        );
      }
    }

    // If all rings are hidden, then nothing to show...
    if(allRingsHidden) {
      // Reset parameters.
      painter.reset();
      holes.clear();
      canvas = null;
      tint.recycle();
      tint = null;
      return false;
    }

    // Final cuts will be carved in the canvas, leaving a transparent color.
    painter.setXfermode(PORTER_DUFF_CLEAR);

    // Perform the final cuts.
    for(RectF hole : holes) {
      canvas.drawOval(hole, painter);
    }

    // Reset parameters.
    painter.reset();
    holes.clear();

    // Get the context associated with the dialog.
    Context ctx = dialog.getContext();

    // Turn the canvas into a bitmap.
    BitmapDrawable background = new BitmapDrawable(ctx.getResources(), tint);

    // Bind the slide's buttons.
    slideResource.showInternal();

    // Set the bitmap as a background for the tint.
    FsView.setBackgroundDrawable(dialogTintView, background);

    // Show the dialog.
    dialog.show();

    // Register this instance in preferences.
    if(!doNotCheckDb && null != db) {
      Log.d(LOG_TAG,
        "Registered slide."
      );
      db.registerSlide(slideResource);
      if(isNewIntro) {
        Log.d(LOG_TAG,
          "Registered instance."
        );
        db.registerIntro(this);
      }
    }

    // Keep a pointer to the current slide.
    currentSlide = slideResource;

    return true;
  }

  /**
   * Show the next slide after the currently showing one, or show the first
   * if no slide was showing before.
   *
   * @return True if the next slide is shown, false otherwise
   * @since 1.0
   */
  public boolean showNextSlide() {
    int slidePos = null == currentSlide ? 0 : 1 + slides.indexOf(currentSlide);
    return showSlide(slidePos);
  }

  /**
   * Show the previous slide before the currently showing one, or show the last
   * if no slide was showing before.
   *
   * @return True if the previous slide is shown, false otherwise
   * @since 1.0
   */
  public boolean showPreviousSlide() {
    int slidePos = null == currentSlide ? -1 : slides.indexOf(currentSlide);
    if(0 > slidePos) {
      slidePos = slides.size() - 1;
    }
    return showSlide(slidePos);
  }

  /**
   * Show the dialog if a slide was recently hidden using {@link #hide()}.
   *
   * @since 1.0
   */
  public void show() {
    dialog.show();
  }

  /**
   * Hide the currently showing slide, if one is showing.
   *
   * @since 1.0
   */
  public void hide() {
    dialog.hide();
  }

  /**
   * Destroys this instance by detaching its overlay from the Activity's content
   * view and releasing all retained memory.
   *
   * @since 1.0
   */
  public void destroy() {
    destroyInternal(true);
  }

  View findViewById(int resourceId) {
    return activityContentView.findViewById(resourceId);
  }

  void destroyInternal(boolean updateFactory) {
    // Dismiss the dialog.
    dialog.dismiss();

    // Cycle through all slides and free resources retained by them.
    for(FsSlideResource slideResource : slides) {
      destroySlideInternal(slideResource);
    }

    // Empty the dialog's tint view from all resources.
    FsView.empty(dialog.getWindow().getDecorView());

    // Nullify parameters.
    currentSlide = null;
    slides.clear();
    holes.clear();

    // Update the Factory concerning this instance.
    if(updateFactory) {
      FsIntroFactory.destroyInstance(this);
    }
  }

  private void destroySlideInternal(FsSlideResource slideResource) {
    // Hide the current slide and remove any bound listeners.
    if(null != slideResource) {
      // Unbind click listeners from this slide.
      slideResource.hideInternal();

      // Deflate (GC) this slide.
      slideResource.deflate();
    }
  }

}
