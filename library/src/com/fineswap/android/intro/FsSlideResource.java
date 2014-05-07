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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.fineswap.android.utils.FsMetaVersion;
import com.fineswap.android.utils.FsVersion;
import com.fineswap.android.utils.FsView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Defines a slide to be presented in an {@link FsIntroOverlay} instance.
 *
 * A slide contains information about the Activity's rings, a layout resource id
 * to inflate when the slide is shown to the user and various other data-data
 * for presentation.
 *
 * @author Noor Dawod
 * @since 1.0
 */
public class FsSlideResource implements com.fineswap.android.aux.FsLayout {

  /**
   * Interface definition for callbacks to be invoked when a slide is
   * attached or detached from an FsIntroOverlay instance.
   *
   * @since 1.0
   */
  public static interface OnStateChangedHandler {

    /**
     * Called when a slide is attached to an FsIntroOverlay instance.
     *
     * @param slide The FsSlideResource instance containing the clicked view.
     * @since 1.0
     */
    public void onAttach(final FsSlideResource slide);

    /**
     * Called when a slide is detached from an FsIntroOverlay instance.
     *
     * @param slide The FsSlideResource instance containing the clicked view.
     * @since 1.0
     */
    public void onDetach(final FsSlideResource slide);

  }

  /**
   * Interface definition for a callback to be invoked when a view is clicked.
   */
  public static interface OnClickHandler {

    /**
     * Interface definition for callback to be invoked when a bound View is
     * clicked by the user.
     *
     * @param slide The FsSlideResource instance containing the clicked view
     * @param view The view that was clicked
     * @since 1.0
     */
    void onClick(final FsSlideResource slide, final View view);

  }

  /**
   * Auxiliary class to wrap click events performed on a single slide.
   *
   * @since 1.0
   */
  static class OnClickListenerWrapper implements View.OnClickListener {

    /**
     * Points to target slide.
     *
     * @since 1.0
     */
    private final FsSlideResource slide;

    /**
     * Points to target slide's content view.
     *
     * @since 1.0
     */
    private final ViewGroup layoutView;

    /**
     * Constructor.
     *
     * @param slide Target slide instance
     * @since 1.0
     */
    public OnClickListenerWrapper(FsSlideResource slide) {
      this.slide = slide;
      this.layoutView = slide.getLayoutView();
    }

    /**
     * Callback handler to be invoked when a click event occurs.
     *
     * @param view View on which the click occurred
     * @since 1.0
     */
    @Override
    public void onClick(View view) {
      int viewId = view.getId();
      OnClickHandler callback = slide.listeners.get(viewId);
      if(null != callback) {
        callback.onClick(slide, view);
      }
    }

    /**
     * Binds listeners to their associated views in the slide's content view.
     *
     * @since 1.0
     */
    void bind() {
      // Bind button listeners, if there are any.
      bindInternal(this);
    }

    /**
     * Unbinds listeners from their associated views in the slide's content view.
     *
     * @since 1.0
     */
    void unbind() {
      // Remove bound button listeners, if there are any.
      bindInternal(null);
    }

    /**
     * Implementation for binding and unbinding.
     *
     * @param callback Callback handler to bind to the click event
     * @since 1.0
     */
    private void bindInternal(View.OnClickListener callback) {
      Set<Integer> buttonsIds = slide.listeners.keySet();
      for(int buttonId : buttonsIds) {
        View buttonView = layoutView.findViewById(buttonId);
        if(null != buttonView) {
          buttonView.setOnClickListener(callback);
        }
      }
    }

  }

  /**
   * Views in the Activity's window which will be surrounded by rings.
   *
   * @since 1.0
   */
  final ArrayList<FsRingResource> rings = new ArrayList();

  /**
   * Click listeners attached to views which appear in the slide's content view.
   *
   * @since 1.0
   */
  private final HashMap<Integer, OnClickHandler> listeners = new HashMap(10);

  /**
   * A unique version definition for this slide.
   * It is advisable to bump up the version in order to let {@link FsIntroOverlay}
   * instance to forcibly show a slide, for example when the developer changes
   * the page's structure and needs to inform the user of the changes.
   *
   * The slide's layout id (resource) is saved as a data-data.
   *
   * @since 1.0
   */
  public final FsMetaVersion<Integer> slide;

  /**
   * Layout content view; will be populated when slide is attached.
   *
   * @since 1.0
   */
  private ViewGroup layoutView;

  /**
   * Color of the tinted view appearing underneath the slide's content view.
   *
   * @since 1.0
   */
  private int tintColor;

  /**
   * Number of rings to paint around each ring's "hole".
   *
   * @since 1.0
   */
  private int ringCount;

  /**
   * Attached FsIntroOverlay instance.
   *
   * @see FsIntroOverlay
   * @since 1.0
   */
  private FsIntroOverlay introOverlay;

  /**
   * StateChange handler callback.
   *
   * @see OnStateChangedHandler
   * @since 1.0
   */
  private OnStateChangedHandler onStateChangedHandler;

  /**
   * Click handler callback.
   *
   * @see OnClickListenerWrapper
   * @since 1.0
   */
  OnClickListenerWrapper onClickWrapper;

  /**
   * Instantiates a new slide instance using default tint color and ring count.
   *
   * @see IntroOverlay#defaultTintColor
   * @param slide {@link #slide}
   * @since 1.0
   */
  public FsSlideResource(FsMetaVersion<Integer> slide) {
    this(slide, FsIntroFactory.defaultTintColor);
  }

  /**
   * Instantiates a new slide instance using default ring count.
   *
   * @see IntroOverlay#defaultRingCount
   * @param slide {@link #slide}
   * @param tintColor {@link #tintColor}
   * @since 1.0
   */
  public FsSlideResource(FsMetaVersion<Integer> slide, int tintColor) {
    this(slide, tintColor, FsIntroFactory.defaultRingCount);
  }

  /**
   * Instantiates a new slide instance using the supplied values.
   *
   * @param slide {@link #slide}
   * @param tintColor {@link #tintColor}
   * @param ringCount {@link #ringCount}
   * @since 1.0
   */
  public FsSlideResource(FsMetaVersion<Integer> slide, int tintColor, int ringCount) {
    this.slide = slide;
    this.tintColor = tintColor;
    this.ringCount = ringCount;
  }

  /**
   * @since 1.0
   */
  @Override
  public String toString() {
    return slide.toString();
  }

  /**
   * @since 1.0
   */
  @Override
  public boolean equals(Object o) {
    return
      null != o &&
      o instanceof FsSlideResource &&
      slide.equals(((FsSlideResource)o).slide);
  }

  /**
   * @since 1.0
   */
  @Override
  public int hashCode() {
    return toString().hashCode();
  }

  /**
   * Get the current version of this {@link FsSlideResource} instance.
   *
   * @see #slide
   * @return This instance's version
   * @since 1.0
   */
  public FsVersion getVersion() {
    return slide;
  }

  /**
   * Get the attached {@link FsIntroOverlay} instance for this slide.
   *
   * @return {@link FsIntroOverlay} instance, or null if it's unattached yet
   * @since 1.0
   */
  public final FsIntroOverlay getIntroOverlay() {
    return introOverlay;
  }

  /**
   * Get the inflated layout view for this slide.
   *
   * @return Inflated layout view, or null if slide is unattached yet
   * @since 1.0
   */
  public final ViewGroup getLayoutView() {
    return layoutView;
  }

  /**
   * Get the configured tint color for this slide.
   *
   * @return Configured tint color
   * @since 1.0
   */
  public int getTintColor() {
    return tintColor;
  }

  /**
   * Set a new tint color for this slide.
   *
   * @param color New tint color to set
   * @since 1.0
   */
  public void setTintColor(int color) {
    tintColor = color;
  }

  /**
   * Get configured ring count for this slide.
   *
   * @return Configured ring count
   * @since 1.0
   */
  public int getRingCount() {
    return ringCount;
  }

  /**
   * Set new ring count for this slide.
   *
   * @param ringCount New ring count to set
   * @since 1.0
   */
  public void setRingCount(int ringCount) {
    this.ringCount = ringCount;
  }

  /**
   * Get the configured callback when the slide is attached/detached.
   *
   * @return Configured callback
   * @since 1.0
   */
  public OnStateChangedHandler getOnStateChangedCallback() {
    return onStateChangedHandler;
  }

  /**
   * Set a new callback to be invoked when the slide is attached/detached.
   *
   * @param callback New callback to set
   * @since 1.0
   */
  public void setOnStateChangedCallback(OnStateChangedHandler callback) {
    this.onStateChangedHandler = callback;
  }

  /**
   * Get the configured callback for the specified button id.
   *
   * @param buttonResourceId Button id to retrieve its associated click handler
   * @return Configured click handler, or null if none was previously set
   * @since 1.0
   */
  public OnClickHandler getOnClickListener(int buttonResourceId) {
    return listeners.get(buttonResourceId);
  }

  /**
   * Set a click handler for the specified button id.
   *
   * @param buttonResourceId Button id to configure
   * @param onClickListener Click handler to set
   * @since 1.0
   */
  public void setOnClickListener(int buttonResourceId, OnClickHandler onClickListener) {
    if(null != onClickListener && !listeners.containsKey(buttonResourceId)) {
      listeners.put(buttonResourceId, onClickListener);
    }
  }

  /**
   * Add a new ring definition to this slide.
   *
   * @param ringResource A {@link FsRingResource} instance to add
   * @since 1.0
   */
  public void addRingResource(FsRingResource ringResource) {
    if(null != ringResource && !rings.contains(ringResource)) {
      rings.add(ringResource);
    }
  }

  /**
   * Remove a previously-added ring from this slide.
   *
   * @param ringResource A {@link FsRingResource} instance to remove
   * @since 1.0
   */
  public void removeRingResource(FsRingResource ringResource) {
    if(null != ringResource) {
      rings.remove(ringResource);
    }
  }

  ViewGroup inflate(LayoutInflater inflater) {
    View view = inflater.inflate(slide.data, null);
    if(null == view || !(view instanceof ViewGroup)) {
      return null;
    }
    layoutView = (ViewGroup)view;

    // Stretch the layout's size to match its parent.
    layoutView.setLayoutParams(VIEWGROUP_MATCH_PARENT);

    // Initial layout visibility is hidden.
    layoutView.setVisibility(View.GONE);

    return layoutView;
  }

  void deflate() {
    if(null != layoutView) {
      FsView.empty(layoutView);
      layoutView = null;
    }
  }

  void showInternal() {
    // Bind button listeners, if there are any.
    onClickWrapper = new FsSlideResource.OnClickListenerWrapper(this);
    onClickWrapper.bind();

    // Make the slide's content view visible.
    layoutView.setVisibility(View.VISIBLE);
  }

  void hideInternal() {
    // Initial layout visibility is hidden.
    layoutView.setVisibility(View.GONE);

    if(null != onClickWrapper) {
      onClickWrapper.unbind();
      onClickWrapper = null;
    }
  }

  void onAttachInternal(FsIntroOverlay introOverlay) {
    this.introOverlay = introOverlay;
    if(null != onStateChangedHandler) {
      onStateChangedHandler.onAttach(this);
    }
  }

  void onDetachInternal() {
    if(null != onStateChangedHandler) {
      onStateChangedHandler.onDetach(this);
    }
    this.introOverlay = null;
  }

}
